package com.payment.service;

import com.payment.domain.PaymentMethod;
import com.payment.model.PaymentOrder;
import com.payment.payload.dto.BookingDTO;
import com.payment.payload.dto.UserDTO;
import com.payment.payload.response.PaymentLinkResponse;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayException;
import com.stripe.exception.StripeException;

public interface PaymentService {

    PaymentLinkResponse createOrder(UserDTO user,
                                    BookingDTO booking,
                                    PaymentMethod paymentMethod) throws RazorpayException, StripeException;

    PaymentOrder getPaymentOrderById(Long id) throws Exception;

    PaymentOrder getPaymentOrderByPaymentId(String paymentId);

    PaymentLink createRazorpayPaymentLink(UserDTO user,
                                          Long Amount,
                                          Long orderId) throws RazorpayException;

    String createStripePaymentLink(UserDTO user,
                                        Long Amount,
                                        Long orderId) throws StripeException;


    Boolean proceedPayment(PaymentOrder paymentOrder,
                           String paymentId,
                           String paymentLinkId) throws RazorpayException;


}
