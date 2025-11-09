package com.example.b2bfront.model;

public enum DeliveryStatus {
    PENDING("En attente"),
    IN_PROGRESS("En cours"),
    DELIVERED("Livrée"),
    RETURNED("Retournée"),
    CANCELLED("Annulée");

    private final String displayName;

    DeliveryStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}

