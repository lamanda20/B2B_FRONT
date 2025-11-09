package com.example.b2bfront.model;

import java.time.LocalDateTime;

public class Delivery {
    private Long id;
    private Long orderId;
    private ShippingAddress shippingAddress;
    private String carrier; // Transporteur (Maroc Poste, Jumia Express, etc.)
    private DeliveryStatus status;
    private Double shippingCost;
    private String trackingNumber;
    private LocalDateTime estimatedDeliveryDate;
    private LocalDateTime actualDeliveryDate;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Delivery() {
    }

    public Delivery(Long id, Long orderId, ShippingAddress shippingAddress, String carrier,
                   DeliveryStatus status, Double shippingCost, String trackingNumber,
                   LocalDateTime estimatedDeliveryDate, LocalDateTime actualDeliveryDate,
                   String notes, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.orderId = orderId;
        this.shippingAddress = shippingAddress;
        this.carrier = carrier;
        this.status = status;
        this.shippingCost = shippingCost;
        this.trackingNumber = trackingNumber;
        this.estimatedDeliveryDate = estimatedDeliveryDate;
        this.actualDeliveryDate = actualDeliveryDate;
        this.notes = notes;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public ShippingAddress getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(ShippingAddress shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public DeliveryStatus getStatus() {
        return status;
    }

    public void setStatus(DeliveryStatus status) {
        this.status = status;
    }

    public Double getShippingCost() {
        return shippingCost;
    }

    public void setShippingCost(Double shippingCost) {
        this.shippingCost = shippingCost;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public LocalDateTime getEstimatedDeliveryDate() {
        return estimatedDeliveryDate;
    }

    public void setEstimatedDeliveryDate(LocalDateTime estimatedDeliveryDate) {
        this.estimatedDeliveryDate = estimatedDeliveryDate;
    }

    public LocalDateTime getActualDeliveryDate() {
        return actualDeliveryDate;
    }

    public void setActualDeliveryDate(LocalDateTime actualDeliveryDate) {
        this.actualDeliveryDate = actualDeliveryDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Delivery{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", shippingAddress=" + shippingAddress +
                ", carrier='" + carrier + '\'' +
                ", status=" + status +
                ", shippingCost=" + shippingCost +
                ", trackingNumber='" + trackingNumber + '\'' +
                ", estimatedDeliveryDate=" + estimatedDeliveryDate +
                ", actualDeliveryDate=" + actualDeliveryDate +
                ", notes='" + notes + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
