package com.example.b2bfront.model;

import com.google.gson.annotations.SerializedName;
import java.time.LocalDateTime;

public class Delivery {
    private Long id;

    @SerializedName("commandeId")
    private Long orderId;

    // Champs d'adresse mappés directement depuis le backend
    @SerializedName("adresse")
    private String fullAddress;

    @SerializedName("ville")
    private String city;

    @SerializedName("telephone")
    private String phoneNumber;

    private String postalCode;
    private String recipientName;

    @SerializedName("transporteur")
    private String carrier;

    @SerializedName("statut")
    private DeliveryStatus status;

    @SerializedName("fraisLivraison")
    private Double shippingCost;

    private String trackingNumber;

    @SerializedName("dateEstimee")
    private LocalDateTime estimatedDeliveryDate;

    @SerializedName("dateEnvoi")
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
        if (shippingAddress != null) {
            this.fullAddress = shippingAddress.getFullAddress();
            this.city = shippingAddress.getCity();
            this.phoneNumber = shippingAddress.getPhoneNumber();
            this.postalCode = shippingAddress.getPostalCode();
            this.recipientName = shippingAddress.getRecipientName();
        }
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

    // Méthode pour obtenir l'adresse comme objet ShippingAddress
    public ShippingAddress getShippingAddress() {
        ShippingAddress address = new ShippingAddress();
        address.setFullAddress(this.fullAddress);
        address.setCity(this.city);
        address.setPhoneNumber(this.phoneNumber);
        address.setPostalCode(this.postalCode);
        address.setRecipientName(this.recipientName);
        return address;
    }

    // Méthode pour définir l'adresse depuis un objet ShippingAddress
    public void setShippingAddress(ShippingAddress address) {
        if (address != null) {
            this.fullAddress = address.getFullAddress();
            this.city = address.getCity();
            this.phoneNumber = address.getPhoneNumber();
            this.postalCode = address.getPostalCode();
            this.recipientName = address.getRecipientName();
        }
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

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
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
                ", fullAddress='" + fullAddress + '\'' +
                ", city='" + city + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
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
