package com.example.b2bfront.model;

public class Payment {
    private Long id;
    private String orderId;
    private double amount;
    private String method;
    private String status;
    private String date;
    private String reference;
    private String notes;

    // Constructeurs
    public Payment() {}
    public Payment(String orderId, double amount, String method, String reference, String notes) {
        this.orderId = orderId;
        this.amount = amount;
        this.method = method;
        this.reference = reference;
        this.notes = notes;
    }

    // Getters et setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getReference() { return reference; }
    public void setReference(String reference) { this.reference = reference; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
