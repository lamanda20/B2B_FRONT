package com.example.b2bfront.model;

/**
 * Réponse du backend pour le calcul des frais de livraison
 */
public class ShippingCostResponse {
    private Double shippingCost;
    private String city;
    private String currency;

    public ShippingCostResponse() {
    }

    public Double getShippingCost() {
        return shippingCost;
    }

    public void setShippingCost(Double shippingCost) {
        this.shippingCost = shippingCost;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}

