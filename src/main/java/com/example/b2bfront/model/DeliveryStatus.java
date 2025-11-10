package com.example.b2bfront.model;

import com.google.gson.annotations.SerializedName;

public enum DeliveryStatus {
    @SerializedName(value = "PENDING", alternate = {"EN_ATTENTE", "EN_PREPARATION"})
    PENDING("En attente"),

    @SerializedName(value = "IN_PROGRESS", alternate = {"EN_COURS", "EXPEDIEE"})
    IN_PROGRESS("En cours"),

    @SerializedName(value = "DELIVERED", alternate = {"LIVREE"})
    DELIVERED("Livrée"),

    @SerializedName(value = "RETURNED", alternate = {"RETOURNEE"})
    RETURNED("Retournée"),

    @SerializedName(value = "CANCELLED", alternate = {"ANNULEE"})
    CANCELLED("Annulée");

    private final String displayName;

    DeliveryStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    /**
     * Retourne la valeur du statut pour l'API backend (en français)
     */
    public String toBackendValue() {
        switch (this) {
            case PENDING:
                return "EN_ATTENTE";
            case IN_PROGRESS:
                return "EN_COURS";
            case DELIVERED:
                return "LIVREE";
            case RETURNED:
                return "RETOURNEE";
            case CANCELLED:
                return "ANNULEE";
            default:
                return this.name();
        }
    }

    @Override
    public String toString() {
        return displayName;
    }
}
