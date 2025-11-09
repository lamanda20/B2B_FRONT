package com.example.b2bfront.service;

import com.example.b2bfront.model.Delivery;
import com.example.b2bfront.util.Constants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Service pour intégrer Livraisons avec le Module Commandes
 * Permet aux autres modules d'accéder aux informations de livraison
 */
public class OrderDeliveryIntegration {

    private final DeliveryService deliveryService;
    private final ApiService apiService;
    private final Gson gson;

    public OrderDeliveryIntegration() {
        this.apiService = new ApiService();
        this.deliveryService = new DeliveryService(apiService);
        this.gson = new Gson();
    }

    /**
     * Récupère toutes les livraisons associées à une commande
     * À utiliser par le Module Commandes (Personne 4)
     */
    public List<Delivery> getDeliveriesForOrder(Long orderId) throws IOException {
        return deliveryService.getDeliveriesByOrderId(orderId);
    }

    /**
     * Vérifie si une commande a déjà une livraison
     * À utiliser par le Module Paiement (Personne 5)
     */
    public boolean hasDelivery(Long orderId) throws IOException {
        List<Delivery> deliveries = deliveryService.getDeliveriesByOrderId(orderId);
        return !deliveries.isEmpty();
    }

    /**
     * Crée automatiquement une livraison après un paiement réussi
     * À appeler par le Module Paiement (Personne 5) après validation du paiement
     */
    public Delivery createDeliveryAfterPayment(Long orderId,
                                               String recipientName,
                                               String address,
                                               String city,
                                               String postalCode,
                                               String phoneNumber) throws IOException {
        Delivery delivery = new Delivery();
        delivery.setOrderId(orderId);
        delivery.setCarrier("Maroc Poste"); // Par défaut

        com.example.b2bfront.model.ShippingAddress shippingAddress =
            new com.example.b2bfront.model.ShippingAddress();
        shippingAddress.setRecipientName(recipientName);
        shippingAddress.setFullAddress(address);
        shippingAddress.setCity(city);
        shippingAddress.setPostalCode(postalCode);
        shippingAddress.setPhoneNumber(phoneNumber);

        delivery.setShippingAddress(shippingAddress);
        delivery.setStatus(com.example.b2bfront.model.DeliveryStatus.PENDING);

        return deliveryService.createDelivery(delivery);
    }

    /**
     * Obtient le statut de livraison d'une commande
     * Retourne le statut de la dernière livraison de la commande
     */
    public String getDeliveryStatusForOrder(Long orderId) throws IOException {
        List<Delivery> deliveries = deliveryService.getDeliveriesByOrderId(orderId);
        if (deliveries.isEmpty()) {
            return "Aucune livraison";
        }
        // Retourne le statut de la dernière livraison
        Delivery lastDelivery = deliveries.get(deliveries.size() - 1);
        return lastDelivery.getStatus().getDisplayName();
    }

    /**
     * Calcule les frais de livraison pour une commande
     * À utiliser lors de la création de commande pour estimer le coût total
     */
    public Double calculateShippingCostForOrder(String city) throws IOException {
        return deliveryService.calculateShippingCost(city);
    }

    /**
     * Envoie une notification de livraison via le Module Notifications (Personne 7)
     * Cette méthode sera étendue quand le module notifications sera prêt
     */
    public void notifyDeliveryUpdate(Long orderId, String newStatus) {
        NotificationService notificationService = NotificationService.getInstance();
        notificationService.notifyAll(
            "Commande #" + orderId + " - Statut de livraison: " + newStatus,
            NotificationService.NotificationType.STATUS_CHANGE
        );
    }
}

