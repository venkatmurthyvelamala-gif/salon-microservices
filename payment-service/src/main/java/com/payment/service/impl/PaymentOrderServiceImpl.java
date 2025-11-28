package com.payment.service.impl;

import com.payment.domain.PaymentMethod;
import com.payment.domain.PaymentOrderStatus;
import com.payment.model.PaymentOrder;
import com.payment.payload.dto.BookingDTO;
import com.payment.payload.dto.UserDTO;
import com.payment.payload.response.PaymentLinkResponse;
import com.payment.repository.PaymentOrderRepository;
import com.payment.service.PaymentService;
import com.razorpay.Payment;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PaymentOrderServiceImpl implements PaymentService {

    @Autowired
    PaymentOrderRepository paymentOrderRepository;

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    @Value("${razorpay.api.key}")
    private String razorpayApiKey;

    @Value("${razorpay.api.secret}")
    private String razorpayApiSecret;

    @Override
    public PaymentLinkResponse createOrder(UserDTO user, BookingDTO booking, PaymentMethod paymentMethod) throws RazorpayException, StripeException {

        Long amount = (long) booking.getTotalPrice();

        PaymentOrder order = new PaymentOrder();
        order.setAmount(amount);
        order.setPaymentMethod(paymentMethod);
        order.setBookingId(booking.getId());
        order.setSalonId(booking.getSalonId());
        PaymentOrder saveOrder = paymentOrderRepository.save(order);

        PaymentLinkResponse paymentLinkResponse = new PaymentLinkResponse();

        if (paymentMethod.equals(PaymentMethod.RAZORPAY)) {
            PaymentLink payment = createRazorpayPaymentLink(user,
                    saveOrder.getAmount(),
                    saveOrder.getId());

            String paymentUrl = payment.get("short_url");
            String paymentUrlId = payment.get("id");

            paymentLinkResponse.setPayment_link_url(paymentUrl);
            paymentLinkResponse.setPayment_link_id(paymentUrlId);

            saveOrder.setPaymentLinkId(paymentUrlId);

            paymentOrderRepository.save(saveOrder);
        } else {
            String paymentUrl = createStripePaymentLink(user,
                    saveOrder.getAmount(),
                    saveOrder.getId());
            paymentLinkResponse.setPayment_link_url(paymentUrl);
        }
        return paymentLinkResponse;
    }

    @Override
    public PaymentOrder getPaymentOrderById(Long id) throws Exception {
        PaymentOrder paymentOrder = paymentOrderRepository.findById(id).orElse(null);
        if (paymentOrder == null) {
            throw new Exception("payment order not found with this id:" + id);
        }
        return paymentOrder;
    }

    @Override
    public PaymentOrder getPaymentOrderByPaymentId(String paymentId) {

        return paymentOrderRepository.findByPaymentLinkId(paymentId);
    }

    @Override
    public PaymentLink createRazorpayPaymentLink(UserDTO user,
                                                 Long Amount,
                                                 Long orderId) throws RazorpayException {

        Long amount = Amount * 100;

        RazorpayClient razorpay = new RazorpayClient(razorpayApiKey, razorpayApiSecret);

        JSONObject paymentLinkResponse = new JSONObject();
        paymentLinkResponse.put("amount", amount);
        paymentLinkResponse.put("currency", "INR");

        JSONObject customer = new JSONObject();
        customer.put("name", user.getName());
        customer.put("email", user.getEmail());

        paymentLinkResponse.put("customer", customer);

        JSONObject notify = new JSONObject();
        notify.put("email", true);

        paymentLinkResponse.put("notify", notify);

        paymentLinkResponse.put("remainder_enable", true);
        paymentLinkResponse.put("callback_url", "localhost://3000/payment-sucess" + orderId);
        paymentLinkResponse.put("callback_method", "get");

        return razorpay.paymentLink.create(paymentLinkResponse);

    }

    @Override
    public String createStripePaymentLink(UserDTO user, Long Amount, Long orderId) throws StripeException {

        Stripe.apiKey = stripeApiKey;

        SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:3000/payment-successfull" + orderId)
                .setCancelUrl("http://localhost:3000/payment/cancel")
                .addLineItem(SessionCreateParams.LineItem.builder()
                        .setQuantity(1L)
                        .setPriceData(
                                SessionCreateParams.LineItem.PriceData.builder()
                                        .setCurrency("usd")
                                        .setUnitAmount(Amount * 100)
                                        .setProductData(
                                                SessionCreateParams
                                                        .LineItem
                                                        .PriceData
                                                        .ProductData
                                                        .builder().setName("salon appointment booking")
                                                        .build()
                                        ).build()
                        ).build()
                ).build();

        Session session = Session.create(params);
        return session.getUrl();
    }

    @Override
    public Boolean proceedPayment(PaymentOrder paymentOrder, String paymentId, String paymentLinkId) throws RazorpayException {

        if (paymentOrder.getStatus().equals(PaymentOrderStatus.PENDING)) {

            if (paymentOrder.getPaymentMethod().equals(PaymentMethod.RAZORPAY)) {
                RazorpayClient razorpay = new RazorpayClient(razorpayApiKey, razorpayApiSecret);

                Payment payment = razorpay.payments.fetch(paymentId);
                Integer amount = payment.get("amount");
                String status = payment.get("status");

                if (status.equals("captured")) {
                    //kafka event for notification
                    paymentOrder.setStatus(PaymentOrderStatus.SUCCESS);
                    paymentOrderRepository.save(paymentOrder);
                    return true;
                }
                return false;
            } else {
                paymentOrder.setStatus(PaymentOrderStatus.FAILED);
                paymentOrderRepository.save(paymentOrder);
                return true;
            }

        }
        return false;
    }

}