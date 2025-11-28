package com.payment.controller;

import com.payment.domain.PaymentMethod;
import com.payment.model.PaymentOrder;
import com.payment.payload.dto.BookingDTO;
import com.payment.payload.dto.UserDTO;
import com.payment.payload.response.PaymentLinkResponse;
import com.payment.service.PaymentService;
import com.razorpay.RazorpayException;
import com.stripe.exception.StripeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    PaymentService paymentService;

    @PostMapping("/create")
    public ResponseEntity<PaymentLinkResponse> createPaymentLink(
            @RequestBody BookingDTO booking,
            @RequestParam PaymentMethod paymentMethod
            ) throws StripeException, RazorpayException {

        UserDTO user = new UserDTO();
        user.setName("Ashok");
        user.setEmail("ashok.gmail.com");
        user.setId(1L);

        PaymentLinkResponse response = paymentService.createOrder(user,
                                                                  booking,paymentMethod);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{paymentOrderId}")
    public ResponseEntity<PaymentOrder> getPaymentOrderById(@PathVariable Long paymentOrderId) throws Exception {

        PaymentOrder response = paymentService.getPaymentOrderById(paymentOrderId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/proceed")
    public ResponseEntity<Boolean> proceedPayment(
            @RequestParam String paymentId,
            @RequestParam String paymentLinkId
    ) throws RazorpayException {
        PaymentOrder paymentOrder = paymentService.getPaymentOrderByPaymentId(paymentLinkId);

        Boolean res = paymentService.proceedPayment(paymentOrder,
                paymentId,paymentLinkId);

        return ResponseEntity.ok(res);

    }






}
