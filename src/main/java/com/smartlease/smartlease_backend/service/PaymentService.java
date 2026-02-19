package com.smartlease.smartlease_backend.service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.smartlease.smartlease_backend.exception.PaymentFailedException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class PaymentService {

    @Value("${razorpay.key.id}")
    private String keyId;

    @Value("${razorpay.key.secret}")
    private String keySecret;

    //create order (called when user clicks "book now")
    @Transactional
    public String createOrder(Double amount) {

        try {
            RazorpayClient client = new RazorpayClient(keyId, keySecret);

            //razorpay takes amount in paise
            //so 5000 becomes 500000
            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", amount * 100);
            orderRequest.put("currency", "INR");

            String uniqueReceipt = "txn_" + UUID.randomUUID().toString(); // for making order requests unique
            orderRequest.put("receipt", uniqueReceipt);

            //talk to razorpay
            Order order = client.orders.create(orderRequest);

            //return the orderi
            return order.get("id").toString();
        } catch (RazorpayException e) {
            System.err.println("Razorpay failed" + e.getMessage());
            throw new PaymentFailedException("Payment gateway is down");
        }
    }
}
