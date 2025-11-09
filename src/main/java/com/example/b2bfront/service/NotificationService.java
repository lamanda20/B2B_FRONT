package com.example.b2bfront.service;

import javafx.scene.control.Alert;
import javafx.application.Platform;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Service de notifications pour le module Livraison
 * Envoie des notifications quand le statut d'une livraison change
 */
public class NotificationService {

    private static NotificationService instance;
    private final List<NotificationListener> listeners = new ArrayList<>();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private NotificationService() {}

    public static NotificationService getInstance() {
        if (instance == null) {
            instance = new NotificationService();
        }
        return instance;
    }

    /**
     * Enregistre un listener pour recevoir les notifications
     */
    public void addListener(NotificationListener listener) {
        listeners.add(listener);
    }

    /**
     * Supprime un listener
     */
    public void removeListener(NotificationListener listener) {
        listeners.remove(listener);
    }

    /**
     * Envoie une notification de changement de statut
     */
    public void notifyStatusChange(Long deliveryId, String oldStatus, String newStatus, String trackingNumber) {
        String message = String.format(
            "📦 Livraison #%d - Statut changé\n" +
            "Numéro de suivi: %s\n" +
            "%s → %s\n" +
            "Date: %s",
            deliveryId, trackingNumber, oldStatus, newStatus,
            LocalDateTime.now().format(formatter)
        );

        notifyAll(message, NotificationType.STATUS_CHANGE);
        showPopupNotification("Changement de statut", message);
    }

    /**
     * Envoie une notification de nouvelle livraison
     */
    public void notifyNewDelivery(Long deliveryId, String trackingNumber, String city) {
        String message = String.format(
            "🎉 Nouvelle livraison créée!\n" +
            "ID: #%d\n" +
            "Numéro de suivi: %s\n" +
            "Destination: %s\n" +
            "Statut: En attente",
            deliveryId, trackingNumber, city
        );

        notifyAll(message, NotificationType.NEW_DELIVERY);
        showPopupNotification("Nouvelle livraison", message);
    }

    /**
     * Envoie une notification de livraison terminée
     */
    public void notifyDelivered(Long deliveryId, String trackingNumber, String recipientName) {
        String message = String.format(
            "✅ Livraison terminée!\n" +
            "ID: #%d\n" +
            "Numéro de suivi: %s\n" +
            "Destinataire: %s\n" +
            "Date de livraison: %s",
            deliveryId, trackingNumber, recipientName,
            LocalDateTime.now().format(formatter)
        );

        notifyAll(message, NotificationType.DELIVERED);
        showPopupNotification("Livraison terminée", message);
    }

    /**
     * Envoie une notification d'erreur
     */
    public void notifyError(String errorMessage) {
        String message = "❌ Erreur: " + errorMessage;
        notifyAll(message, NotificationType.ERROR);
    }

    /**
     * Envoie une notification de retour
     */
    public void notifyReturned(Long deliveryId, String trackingNumber, String reason) {
        String message = String.format(
            "🔙 Livraison retournée\n" +
            "ID: #%d\n" +
            "Numéro de suivi: %s\n" +
            "Raison: %s\n" +
            "Date: %s",
            deliveryId, trackingNumber,
            reason != null ? reason : "Non spécifiée",
            LocalDateTime.now().format(formatter)
        );

        notifyAll(message, NotificationType.RETURNED);
        showPopupNotification("Livraison retournée", message);
    }

    /**
     * Notifie tous les listeners
     */
    public void notifyAll(String message, NotificationType type) {
        for (NotificationListener listener : listeners) {
            listener.onNotification(message, type);
        }
    }

    /**
     * Affiche une notification popup dans l'interface JavaFX
     */
    private void showPopupNotification(String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.show(); // Non-bloquant

            // Auto-fermeture après 3 secondes
            new Thread(() -> {
                try {
                    Thread.sleep(3000);
                    Platform.runLater(alert::close);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        });
    }

    /**
     * Interface pour les listeners de notifications
     */
    public interface NotificationListener {
        void onNotification(String message, NotificationType type);
    }

    /**
     * Types de notifications
     */
    public enum NotificationType {
        STATUS_CHANGE,
        NEW_DELIVERY,
        DELIVERED,
        RETURNED,
        ERROR,
        INFO
    }
}
