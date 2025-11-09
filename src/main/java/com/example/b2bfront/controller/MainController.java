package com.example.b2bfront.controller;

import com.example.b2bfront.service.ApiService;
import com.example.b2bfront.util.Constants;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.Parent;

import java.io.IOException;

public class MainController {

    @FXML
    private Label statusLabel;

    @FXML
    private Label backendStatusLabel;

    @FXML
    private StackPane contentArea;

    private ApiService apiService;

    @FXML
    private void initialize() {
        apiService = new ApiService();
        Constants.printConfiguration();
        System.out.println("MainController initialized - Shell de navigation prêt");

        // Afficher l'URL du backend dans l'interface
        statusLabel.setText("Backend: " + Constants.API_BASE_URL);
        backendStatusLabel.setText("✅ Connecté à " + Constants.API_BASE_URL);
    }

    /**
     * Méthode utilitaire pour charger un module FXML dans la zone de contenu
     * Les autres membres de l'équipe peuvent utiliser cette méthode comme exemple
     */
    private void loadModule(String fxmlPath, String moduleName) {
        try {
            System.out.println("Chargement du module: " + moduleName);
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent moduleView = loader.load();

            // Remplacer le contenu actuel par le module chargé
            contentArea.getChildren().clear();
            contentArea.getChildren().add(moduleView);

            System.out.println("✅ Module " + moduleName + " chargé avec succès");
        } catch (IOException e) {
            System.err.println("❌ Erreur lors du chargement du module " + moduleName);
            e.printStackTrace();
            showModuleNotAvailable(moduleName);
        }
    }

    /**
     * Affiche un message lorsqu'un module n'est pas encore disponible
     */
    private void showModuleNotAvailable(String moduleName) {
        try {
            contentArea.getChildren().clear();
            javafx.scene.layout.VBox placeholder = new javafx.scene.layout.VBox(20);
            placeholder.setAlignment(javafx.geometry.Pos.CENTER);
            placeholder.setStyle("-fx-padding: 50;");

            javafx.scene.control.Label icon = new javafx.scene.control.Label("⚠️");
            icon.setStyle("-fx-font-size: 72;");

            javafx.scene.control.Label title = new javafx.scene.control.Label("Module " + moduleName);
            title.setStyle("-fx-font-size: 24; -fx-font-weight: bold;");

            javafx.scene.control.Label message = new javafx.scene.control.Label(
                "Ce module n'est pas encore disponible.\n" +
                "Il sera intégré par un autre membre de l'équipe."
            );
            message.setStyle("-fx-font-size: 14; -fx-text-fill: gray;");
            message.setWrapText(true);
            message.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

            javafx.scene.control.Button backButton = new javafx.scene.control.Button("↩️ Retour à l'accueil");
            backButton.setStyle("-fx-font-size: 14; -fx-padding: 10 20; -fx-background-color: #3498db; -fx-text-fill: white;");
            backButton.setOnAction(e -> onShowHome());

            placeholder.getChildren().addAll(icon, title, message, backButton);
            contentArea.getChildren().add(placeholder);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ==================== HANDLERS DE NAVIGATION ====================

    @FXML
    private void onShowHome() {
        System.out.println("Navigation: Accueil");
        // Recharger la vue par défaut
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/b2bfront/main.fxml"));
            loader.load();
            Parent defaultView = loader.getRoot();
            StackPane defaultContent = (StackPane) defaultView.lookup("#contentArea");
            if (defaultContent != null && !defaultContent.getChildren().isEmpty()) {
                contentArea.getChildren().clear();
                contentArea.getChildren().add(defaultContent.getChildren().get(0));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onShowProducts() {
        System.out.println("Navigation: Produits");
        // TODO: À implémenter par la personne responsable du module Produits
        // Exemple: loadModule("/com/example/b2bfront/products.fxml", "Produits");
        showModuleNotAvailable("Produits");
    }

    @FXML
    private void onShowCart() {
        System.out.println("Navigation: Panier");
        // TODO: À implémenter par la personne responsable du module Panier
        // Exemple: loadModule("/com/example/b2bfront/cart.fxml", "Panier");
        showModuleNotAvailable("Panier");
    }

    @FXML
    private void onShowOrders() {
        System.out.println("Navigation: Commandes");
        // TODO: À implémenter par la personne responsable du module Commandes
        // Exemple: loadModule("/com/example/b2bfront/orders.fxml", "Commandes");
        showModuleNotAvailable("Commandes");
    }

    @FXML
    private void onShowPayments() {
        System.out.println("Navigation: Paiements");
        // TODO: À implémenter par la personne responsable du module Paiements
        // Exemple: loadModule("/com/example/b2bfront/payments.fxml", "Paiements");
        showModuleNotAvailable("Paiements");
    }

    @FXML
    private void onShowDelivery() {
        System.out.println("Navigation: Livraisons (Module Actif ✅)");
        // ✅ MODULE LIVRAISON - IMPLÉMENTÉ
        loadModule("/com/example/b2bfront/delivery.fxml", "Livraisons");
    }

    @FXML
    private void onShowNotifications() {
        System.out.println("Navigation: Notifications");
        // TODO: À implémenter par la personne responsable du module Notifications
        // Exemple: loadModule("/com/example/b2bfront/notifications.fxml", "Notifications");
        showModuleNotAvailable("Notifications");
    }

    @FXML
    private void onPingBackend() {
        statusLabel.setText("Backend configured at: " + Constants.API_BASE_URL);
        statusLabel.setStyle("-fx-text-fill: green;");

        // Afficher un message de confirmation sans appeler d'endpoint
        System.out.println("Backend URL: " + Constants.API_BASE_URL);
        System.out.println("Note: Pour tester la connexion, utilisez le module de livraison");
    }
}
