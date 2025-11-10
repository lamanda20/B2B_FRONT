package com.example.b2bfront.service;

import com.example.b2bfront.model.Payment;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class PaymentService {

    private final ApiService apiService;

    public PaymentService(ApiService apiService) {
        this.apiService = apiService;
    }

    public List<Payment> getAllPayments() throws IOException {
        Payment[] payments = apiService.get("/payments", Payment[].class);
        return Arrays.asList(payments);
    }

    public Payment createPayment(Payment payment) throws IOException {
        return apiService.post("/payments", payment, Payment.class);
    }

    public Payment validatePayment(Long id) throws IOException {
        return apiService.put("/payments/" + id + "/validate", null, Payment.class);
    }

    public Payment cancelPayment(Long id) throws IOException {
        return apiService.put("/payments/" + id + "/cancel", null, Payment.class);
    }
}
