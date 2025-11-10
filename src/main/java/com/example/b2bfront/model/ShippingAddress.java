package com.example.b2bfront.model;

import com.google.gson.annotations.SerializedName;

public class ShippingAddress {
    private Long id;

    @SerializedName("adresse")
    private String fullAddress;

    @SerializedName("ville")
    private String city;

    private String postalCode;

    @SerializedName("telephone")
    private String phoneNumber;

    private String recipientName;

    public ShippingAddress() {
    }

    public ShippingAddress(Long id, String fullAddress, String city, String postalCode,
                          String phoneNumber, String recipientName) {
        this.id = id;
        this.fullAddress = fullAddress;
        this.city = city;
        this.postalCode = postalCode;
        this.phoneNumber = phoneNumber;
        this.recipientName = recipientName;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    @Override
    public String toString() {
        return "ShippingAddress{" +
                "id=" + id +
                ", fullAddress='" + fullAddress + '\'' +
                ", city='" + city + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", recipientName='" + recipientName + '\'' +
                '}';
    }
}
