package com.example.b2bfront.service;

import com.example.b2bfront.model.Delivery;
import com.example.b2bfront.model.DeliveryStatus;
import com.example.b2bfront.model.ShippingAddress;
import com.example.b2bfront.model.ShippingCostResponse;
import com.example.b2bfront.util.Constants;
import com.example.b2bfront.util.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeliveryService {

    private final ApiService apiService;
    private final Gson gson;

    public DeliveryService(ApiService apiService) {
        this.apiService = apiService;
        // Configurer Gson avec l'adaptateur pour LocalDateTime
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
    }

    /**
     * Récupère toutes les livraisons
     */
    public List<Delivery> getAllDeliveries() throws IOException {
        String response = apiService.get(Constants.ENDPOINT_DELIVERIES);
        Type listType = new TypeToken<List<Delivery>>(){}.getType();
        return gson.fromJson(response, listType);
    }

    /**
     * Récupère une livraison par ID
     */
    public Delivery getDeliveryById(Long id) throws IOException {
        String endpoint = Constants.ENDPOINT_DELIVERIES + "/" + id;
        return apiService.get(endpoint, Delivery.class);
    }

    /**
     * Récupère les livraisons d'une commande spécifique
     */
    public List<Delivery> getDeliveriesByOrderId(Long orderId) throws IOException {
        String endpoint = Constants.ENDPOINT_DELIVERIES + "/order/" + orderId;
        String response = apiService.get(endpoint);
        Type listType = new TypeToken<List<Delivery>>(){}.getType();
        return gson.fromJson(response, listType);
    }

    /**
     * Récupère les livraisons par statut
     */
    public List<Delivery> getDeliveriesByStatus(DeliveryStatus status) throws IOException {
        String endpoint = Constants.ENDPOINT_DELIVERIES + "/status/" + status.toBackendValue();
        String response = apiService.get(endpoint);
        Type listType = new TypeToken<List<Delivery>>(){}.getType();
        return gson.fromJson(response, listType);
    }

    /**
     * Crée une nouvelle livraison
     */
    public Delivery createDelivery(Delivery delivery) throws IOException {
        return apiService.post(Constants.ENDPOINT_DELIVERIES, delivery, Delivery.class);
    }

    /**
     * Met à jour le statut d'une livraison
     */
    public Delivery updateDeliveryStatus(Long deliveryId, DeliveryStatus newStatus) throws IOException {
        String endpoint = Constants.ENDPOINT_DELIVERIES + "/" + deliveryId + "/status";
        Map<String, String> statusUpdate = new HashMap<>();
        statusUpdate.put("status", newStatus.name());
        return apiService.post(endpoint, statusUpdate, Delivery.class);
    }

    /**
     * Met à jour une livraison complète
     */
    public Delivery updateDelivery(Long deliveryId, Delivery delivery) throws IOException {
        String endpoint = Constants.ENDPOINT_DELIVERIES + "/" + deliveryId;
        return apiService.post(endpoint, delivery, Delivery.class);
    }

    /**
     * Calcule les frais de livraison selon la ville
     */
    public Double calculateShippingCost(String city) throws IOException {
        String endpoint = Constants.ENDPOINT_DELIVERIES + "/calculate-shipping?city=" + city;
        String response = apiService.get(endpoint);
        ShippingCostResponse result = gson.fromJson(response, ShippingCostResponse.class);
        return result.getShippingCost();
    }

    /**
     * Suit une livraison par numéro de tracking
     */
    public Delivery trackDelivery(String trackingNumber) throws IOException {
        String endpoint = Constants.ENDPOINT_DELIVERIES + "/track/" + trackingNumber;
        return apiService.get(endpoint, Delivery.class);
    }

    /**
     * Crée ou met à jour une adresse de livraison
     */
    public ShippingAddress saveShippingAddress(ShippingAddress address) throws IOException {
        return apiService.post(Constants.ENDPOINT_SHIPPING_ADDRESS, address, ShippingAddress.class);
    }

    /**
     * Récupère toutes les adresses de livraison de l'utilisateur
     */
    public List<ShippingAddress> getUserShippingAddresses() throws IOException {
        String response = apiService.get(Constants.ENDPOINT_SHIPPING_ADDRESS);
        Type listType = new TypeToken<List<ShippingAddress>>(){}.getType();
        return gson.fromJson(response, listType);
    }

    /**
     * Récupère toutes les commandes disponibles
     */
    public List<Map<String, Object>> getAvailableOrders() throws IOException {
        String response = apiService.get("/commandes");
        Type listType = new TypeToken<List<Map<String, Object>>>(){}.getType();
        return gson.fromJson(response, listType);
    }
}
