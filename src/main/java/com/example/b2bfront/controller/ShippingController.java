package com.example.b2bfront.controller;

import com.example.b2bfront.model.Livraison;
import com.example.b2bfront.service.LivraisonService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;

// Imports pour la navigation (étape suivante)
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;

public class ShippingController {

    @FXML private ToggleGroup transporteurGroup;
    @FXML private Button paymentButton;
    @FXML private Label statusLabel;

    private Livraison livraisonEnCours;
    
    // On ajoute le service pour pouvoir faire le UPDATE
    private final LivraisonService livraisonService = new LivraisonService();

    /**
     * Méthode appelée par le LivraisonController pour passer la livraison.
     */
    public void initData(Livraison livraison) {
        this.livraisonEnCours = livraison;
        String info = String.format("Mise à jour Livraison ID: %d (%s, %s)", 
                livraison.getIdLivraison(),
                livraison.getAdresse(), 
                livraison.getVille());
        statusLabel.setText(info);
    }

    /**
     * Appelé lorsque l'utilisateur clique sur "Continuer vers le Paiement".
     */
    @FXML
    private void onContinueToPaymentClick() {
        if (transporteurGroup.getSelectedToggle() == null) {
            statusLabel.setText("Erreur: Veuillez choisir un transporteur.");
            statusLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        // 1. Récupérer le choix
        String transporteurChoisi = (String) transporteurGroup.getSelectedToggle().getUserData();
        
        statusLabel.setText("Mise à jour vers '" + transporteurChoisi + "' en cours...");
        statusLabel.setStyle("-fx-text-fill: black;");
        paymentButton.setDisable(true);

        // 2. Appeler le service pour faire le UPDATE dans la BD
        livraisonService.updateTransporteur(livraisonEnCours.getIdLivraison(), transporteurChoisi)
            .thenAccept(voidResponse -> {
                // Succès !
                Platform.runLater(() -> {
                    // 3. Mettre à jour notre objet local
                    livraisonEnCours.setTransporteur(transporteurChoisi);
                    
                    statusLabel.setText("Transporteur mis à jour ! Passage au paiement...");
                    statusLabel.setStyle("-fx-text-fill: blue;");

                    // 4. Naviguer vers l'écran de Paiement
                    navigateToPayment(livraisonEnCours);
                });
            })
            .exceptionally(ex -> {
                // Gérer les erreurs
                Platform.runLater(() -> {
                    Throwable cause = ex.getCause() != null ? ex.getCause() : ex;
                    statusLabel.setText("Erreur: " + cause.getMessage());
                    statusLabel.setStyle("-fx-text-fill: red;");
                    paymentButton.setDisable(false);
                });
                return null;
            });
    }

    /**
     * Nouvelle méthode pour naviguer vers l'écran Paiement
     */
    private void navigateToPayment(Livraison livraison) {
        // Nous allons implémenter ceci à la prochaine étape
        System.out.println("Navigation vers Paiement (non implémenté) pour Livraison ID: " + livraison.getIdLivraison());
        
        /*
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("PaymentView.fxml"));
            Parent root = loader.load();
            
            // PaymentController paymentController = loader.getController();
            // paymentController.initData(livraison);

            Stage stage = (Stage) paymentButton.getScene().getWindow();
            Scene scene = new Scene(root, 450, 480);
            
            URL cssUrl = getClass().getResource("styles.css");
            if (cssUrl != null) {
                scene.getStylesheets().add(cssUrl.toExternalForm());
            }
            
            stage.setScene(scene);
            stage.setTitle("Gestion de Livraison B2B - Étape 3: Paiement");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            statusLabel.setText("Erreur: Impossible de charger l'étape 'Paiement'.");
        }
        */
    }
}