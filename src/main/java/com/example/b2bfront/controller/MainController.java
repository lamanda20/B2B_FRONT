package com.example.b2bfront.controller;

import com.example.b2bfront.service.ApiService;
import com.example.b2bfront.util.Constants;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.application.Platform;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainController {

    @FXML
    private Label statusLabel;

    private ApiService apiService;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @FXML
    private void initialize() {
        apiService = new ApiService();
        Constants.printConfiguration();
        System.out.println("Controller initialized");

        // Afficher l'URL du backend dans l'interface
        statusLabel.setText("Ready - Backend: " + Constants.API_BASE_URL);
    }

    @FXML
    private void onPingBackend() {
        statusLabel.setText("Connecting to backend...");
        statusLabel.setStyle("-fx-text-fill: orange;");

        // Appel asynchrone avec ExecutorService
        executorService.submit(() -> {
            try {
                String response = apiService.get(Constants.ENDPOINT_HEALTH);
                Platform.runLater(() -> {
                    statusLabel.setText("✓ Backend connected: " + response);
                    statusLabel.setStyle("-fx-text-fill: green;");
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    statusLabel.setText("✗ Error: " + e.getMessage());
                    statusLabel.setStyle("-fx-text-fill: red;");
                });
                e.printStackTrace();
            }
        });
    }

    /**
     * Nettoyer les ressources quand le contrôleur est détruit
     */
    public void shutdown() {
        executorService.shutdown();
    }
}
