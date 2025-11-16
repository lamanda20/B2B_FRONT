package com.example.b2bfront.controller;

import com.example.b2bfront.model.Livraison;
import com.example.b2bfront.service.LivraisonService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

// IMPORTS AJOUTÉS POUR LA NAVIGATION
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;


public class LivraisonController {

    @FXML private TextField adresseField;
    @FXML private TextField villeField;
    @FXML private TextField codePostalField;
    @FXML private TextField telephoneField;
    @FXML private Button calculateButton;
    @FXML private Button saveButton;
    @FXML private Label statusLabel;

    private final LivraisonService livraisonService = new LivraisonService();
    private final Livraison livraisonEnCours = new Livraison();

    @FXML
    public void initialize() {
        saveButton.setDisable(true);
        statusLabel.setText("Veuillez entrer une ville pour calculer les frais.");
    }

    @FXML
    private void onCalculateFraisClick() {
        String ville = villeField.getText();
        if (ville == null || ville.isBlank()) {
            statusLabel.setText("Erreur: La ville est obligatoire.");
            statusLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        statusLabel.setText("Calcul en cours...");
        statusLabel.setStyle("-fx-text-fill: black;");
        calculateButton.setDisable(true);

        livraisonService.calculateFrais(ville)
            .thenAccept(frais -> {
                livraisonEnCours.setFraisLivraison(frais);
                livraisonEnCours.setVille(ville);
                Platform.runLater(() -> {
                    statusLabel.setText("Frais de livraison pour " + ville + ": " + frais + " DH");
                    statusLabel.setStyle("-fx-text-fill: green;");
                    saveButton.setDisable(false);
                    calculateButton.setDisable(false);
                });
            })
            .exceptionally(ex -> {
                Platform.runLater(() -> {
                    Throwable cause = ex.getCause() != null ? ex.getCause() : ex;
                    statusLabel.setText("Erreur de calcul: " + cause.getMessage());
                    statusLabel.setStyle("-fx-text-fill: red;");
                    calculateButton.setDisable(false);
                });
                return null;
            });
    }

    /**
     * MÉTHODE MISE À JOUR
     * Appelé lorsque l'utilisateur clique sur "Enregistrer".
     */
    @FXML
    private void onSaveClick() {
        // 1. Récupérer toutes les données du formulaire
        livraisonEnCours.setAdresse(adresseField.getText());
        livraisonEnCours.setCodePostal(codePostalField.getText());
        livraisonEnCours.setTelephone(telephoneField.getText());

        // On met une valeur par défaut, on la changera à l'étape "Shipping"
        livraisonEnCours.setTransporteur("En attente");

        statusLabel.setText("Sauvegarde en cours...");
        saveButton.setDisable(true);

        // 2. Appeler le service pour créer la livraison
        livraisonService.createLivraison(livraisonEnCours)
            .thenAccept(livraisonSauvegardee -> {
                // Succès !
                Platform.runLater(() -> {
                    // On a reçu la livraison avec son ID. On la passe à l'étape suivante.
                    navigateToShipping(livraisonSauvegardee);
                });
            })
            .exceptionally(ex -> {
                // Gérer les erreurs
                Platform.runLater(() -> {
                    Throwable cause = ex.getCause() != null ? ex.getCause() : ex;
                    statusLabel.setText("Erreur de sauvegarde: " + cause.getMessage());
                    statusLabel.setStyle("-fx-text-fill: red;");
                    saveButton.setDisable(false);
                });
                return null;
            });
    }

    /**
     * NOUVELLE MÉTHODE
     * Nouvelle méthode pour naviguer vers l'écran Shipping
     */
    private void navigateToShipping(Livraison livraison) {
        try {
            // 1. Charger la nouvelle vue FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/b2bfront/ShippingView.fxml"));
            Parent root = loader.load();

            // 2. Récupérer le contrôleur de la nouvelle vue
            ShippingController shippingController = loader.getController();

            // 3. Lui passer la livraison que nous venons de créer
            shippingController.initData(livraison);

            // 4. Changer la scène
            Stage stage = (Stage) saveButton.getScene().getWindow();
            Scene scene = new Scene(root, 450, 480); // Garde la même taille

            // 5. Ré-appliquer le CSS
            URL cssUrl = getClass().getResource("/com/example/b2bfront/styles.css");
            if (cssUrl != null) {
                scene.getStylesheets().add(cssUrl.toExternalForm());
            }

            stage.setScene(scene);
            stage.setTitle("Gestion de Livraison B2B - Étape 2: Transporteur");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            statusLabel.setText("Erreur: Impossible de charger l'étape 'Shipping'.");
        }
    }
}