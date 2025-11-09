package com.example.b2bfront.controller;

import com.example.b2bfront.model.Delivery;
import com.example.b2bfront.model.DeliveryStatus;
import com.example.b2bfront.model.ShippingAddress;
import com.example.b2bfront.service.ApiService;
import com.example.b2bfront.service.DeliveryService;
import com.example.b2bfront.service.NotificationService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DeliveryController {

    // TableView pour afficher les livraisons
    @FXML
    private TableView<Delivery> deliveryTable;
    @FXML
    private TableColumn<Delivery, Long> colId;
    @FXML
    private TableColumn<Delivery, Long> colOrderId;
    @FXML
    private TableColumn<Delivery, String> colCarrier;
    @FXML
    private TableColumn<Delivery, String> colStatus;
    @FXML
    private TableColumn<Delivery, Double> colShippingCost;
    @FXML
    private TableColumn<Delivery, String> colTrackingNumber;

    // Détails de la livraison sélectionnée
    @FXML
    private VBox detailsPane;
    @FXML
    private Label lblDeliveryId;
    @FXML
    private Label lblOrderId;
    @FXML
    private Label lblStatus;
    @FXML
    private Label lblCarrier;
    @FXML
    private Label lblTrackingNumber;
    @FXML
    private Label lblShippingCost;
    @FXML
    private Label lblEstimatedDelivery;
    @FXML
    private Label lblActualDelivery;
    @FXML
    private TextArea txtNotes;

    // Adresse de livraison
    @FXML
    private Label lblRecipientName;
    @FXML
    private Label lblAddress;
    @FXML
    private Label lblCity;
    @FXML
    private Label lblPostalCode;
    @FXML
    private Label lblPhoneNumber;

    // Barre de progression du statut
    @FXML
    private ProgressBar statusProgressBar;
    @FXML
    private Label lblProgressStatus;

    // Formulaire pour créer/modifier une livraison
    @FXML
    private TextField txtOrderIdInput;
    @FXML
    private TextField txtRecipientName;
    @FXML
    private TextField txtFullAddress;
    @FXML
    private TextField txtCity;
    @FXML
    private TextField txtPostalCode;
    @FXML
    private TextField txtPhoneNumber;
    @FXML
    private ComboBox<String> cmbCarrier;
    @FXML
    private Label lblCalculatedCost;

    // Filtre par statut
    @FXML
    private ComboBox<DeliveryStatus> cmbStatusFilter;

    // Tracking
    @FXML
    private TextField txtTrackingSearch;

    @FXML
    private Label statusLabel;

    private DeliveryService deliveryService;
    private NotificationService notificationService;
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private final ObservableList<Delivery> deliveryList = FXCollections.observableArrayList();
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @FXML
    private void initialize() {
        ApiService apiService = new ApiService();
        deliveryService = new DeliveryService(apiService);
        notificationService = NotificationService.getInstance();

        // Enregistrer un listener pour afficher les notifications dans la console
        notificationService.addListener((message, type) -> {
            System.out.println("🔔 NOTIFICATION [" + type + "]: " + message);
        });

        setupTableColumns();
        setupCarrierComboBox();
        setupStatusFilterComboBox();
        setupTableSelectionListener();

        statusLabel.setText("Module Livraison & Suivi - Prêt");
        detailsPane.setVisible(false);

        System.out.println("DeliveryController initialized");
    }

    private void setupTableColumns() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colOrderId.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        colCarrier.setCellValueFactory(new PropertyValueFactory<>("carrier"));
        colStatus.setCellValueFactory(cellData ->
            new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getStatus() != null ?
                cellData.getValue().getStatus().getDisplayName() : ""
            ));
        colShippingCost.setCellValueFactory(new PropertyValueFactory<>("shippingCost"));
        colTrackingNumber.setCellValueFactory(new PropertyValueFactory<>("trackingNumber"));

        deliveryTable.setItems(deliveryList);
    }

    private void setupCarrierComboBox() {
        if (cmbCarrier != null) {
            cmbCarrier.setItems(FXCollections.observableArrayList(
                "Maroc Poste",
                "Jumia Express",
                "Amana",
                "CTM",
                "DHL Express",
                "FedEx",
                "Autre"
            ));
            cmbCarrier.getSelectionModel().selectFirst();
        }
    }

    private void setupStatusFilterComboBox() {
        if (cmbStatusFilter != null) {
            cmbStatusFilter.setItems(FXCollections.observableArrayList(DeliveryStatus.values()));
            cmbStatusFilter.setPromptText("Filtrer par statut");
        }
    }

    private void setupTableSelectionListener() {
        deliveryTable.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> {
                if (newValue != null) {
                    displayDeliveryDetails(newValue);
                }
            }
        );
    }

    @FXML
    private void onLoadAllDeliveries() {
        statusLabel.setText("Chargement des livraisons...");
        statusLabel.setStyle("-fx-text-fill: orange;");

        executorService.submit(() -> {
            try {
                List<Delivery> deliveries = deliveryService.getAllDeliveries();
                Platform.runLater(() -> {
                    deliveryList.clear();
                    deliveryList.addAll(deliveries);
                    statusLabel.setText("✓ " + deliveries.size() + " livraison(s) chargée(s)");
                    statusLabel.setStyle("-fx-text-fill: green;");
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    statusLabel.setText("✗ Erreur: " + e.getMessage());
                    statusLabel.setStyle("-fx-text-fill: red;");
                    showError("Erreur de chargement", e.getMessage());
                });
                e.printStackTrace();
            }
        });
    }

    @FXML
    private void onFilterByStatus() {
        DeliveryStatus selectedStatus = cmbStatusFilter.getValue();
        if (selectedStatus == null) {
            onLoadAllDeliveries();
            return;
        }

        statusLabel.setText("Filtrage par statut: " + selectedStatus.getDisplayName());
        statusLabel.setStyle("-fx-text-fill: orange;");

        executorService.submit(() -> {
            try {
                List<Delivery> deliveries = deliveryService.getDeliveriesByStatus(selectedStatus);
                Platform.runLater(() -> {
                    deliveryList.clear();
                    deliveryList.addAll(deliveries);
                    statusLabel.setText("✓ " + deliveries.size() + " livraison(s) avec statut " +
                                       selectedStatus.getDisplayName());
                    statusLabel.setStyle("-fx-text-fill: green;");
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    statusLabel.setText("✗ Erreur: " + e.getMessage());
                    statusLabel.setStyle("-fx-text-fill: red;");
                    showError("Erreur de filtrage", e.getMessage());
                });
                e.printStackTrace();
            }
        });
    }

    @FXML
    private void onTrackDelivery() {
        String trackingNumber = txtTrackingSearch.getText().trim();
        if (trackingNumber.isEmpty()) {
            showWarning("Tracking", "Veuillez entrer un numéro de suivi");
            return;
        }

        statusLabel.setText("Recherche de la livraison...");
        statusLabel.setStyle("-fx-text-fill: orange;");

        executorService.submit(() -> {
            try {
                Delivery delivery = deliveryService.trackDelivery(trackingNumber);
                Platform.runLater(() -> {
                    deliveryList.clear();
                    deliveryList.add(delivery);
                    deliveryTable.getSelectionModel().select(delivery);
                    statusLabel.setText("✓ Livraison trouvée");
                    statusLabel.setStyle("-fx-text-fill: green;");
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    statusLabel.setText("✗ Livraison non trouvée");
                    statusLabel.setStyle("-fx-text-fill: red;");
                    showError("Tracking", "Aucune livraison trouvée avec ce numéro: " + trackingNumber);
                });
                e.printStackTrace();
            }
        });
    }

    @FXML
    private void onCalculateShippingCost() {
        String city = txtCity.getText().trim();
        if (city.isEmpty()) {
            showWarning("Calcul des frais", "Veuillez entrer une ville");
            return;
        }

        executorService.submit(() -> {
            try {
                Double cost = deliveryService.calculateShippingCost(city);
                Platform.runLater(() -> {
                    if (lblCalculatedCost != null) {
                        lblCalculatedCost.setText(String.format("%.2f MAD", cost));
                        lblCalculatedCost.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                    }
                    showInfo("Frais de livraison",
                            String.format("Frais pour %s: %.2f MAD", city, cost));
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    showError("Erreur de calcul", e.getMessage());
                });
                e.printStackTrace();
            }
        });
    }

    @FXML
    private void onCreateDelivery() {
        try {
            // Validation
            if (txtOrderIdInput.getText().trim().isEmpty()) {
                showWarning("Validation", "Le numéro de commande est requis");
                return;
            }

            // Créer l'adresse de livraison
            ShippingAddress address = new ShippingAddress();
            address.setRecipientName(txtRecipientName.getText().trim());
            address.setFullAddress(txtFullAddress.getText().trim());
            address.setCity(txtCity.getText().trim());
            address.setPostalCode(txtPostalCode.getText().trim());
            address.setPhoneNumber(txtPhoneNumber.getText().trim());

            // Créer la livraison
            Delivery delivery = new Delivery();
            delivery.setOrderId(Long.parseLong(txtOrderIdInput.getText().trim()));
            delivery.setShippingAddress(address);
            delivery.setCarrier(cmbCarrier.getValue());
            delivery.setStatus(DeliveryStatus.PENDING);

            statusLabel.setText("Création de la livraison...");
            statusLabel.setStyle("-fx-text-fill: orange;");

            executorService.submit(() -> {
                try {
                    Delivery created = deliveryService.createDelivery(delivery);
                    Platform.runLater(() -> {
                        deliveryList.add(created);
                        clearForm();
                        statusLabel.setText("✓ Livraison créée avec succès");
                        statusLabel.setStyle("-fx-text-fill: green;");

                        // 🔔 NOTIFICATION: Nouvelle livraison créée
                        notificationService.notifyNewDelivery(
                            created.getId(),
                            created.getTrackingNumber(),
                            created.getShippingAddress().getCity()
                        );

                        showInfo("Succès", "Livraison créée avec le numéro: " +
                                created.getTrackingNumber());
                    });
                } catch (Exception e) {
                    Platform.runLater(() -> {
                        statusLabel.setText("✗ Erreur de création");
                        statusLabel.setStyle("-fx-text-fill: red;");
                        notificationService.notifyError("Échec de création de la livraison: " + e.getMessage());
                        showError("Erreur", e.getMessage());
                    });
                    e.printStackTrace();
                }
            });
        } catch (NumberFormatException e) {
            showWarning("Validation", "Le numéro de commande doit être un nombre");
        }
    }

    @FXML
    private void onUpdateStatus() {
        Delivery selected = deliveryTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Sélection", "Veuillez sélectionner une livraison");
            return;
        }

        // Dialogue pour choisir le nouveau statut
        ChoiceDialog<DeliveryStatus> dialog = new ChoiceDialog<>(
            selected.getStatus(), DeliveryStatus.values());
        dialog.setTitle("Changer le statut");
        dialog.setHeaderText("Livraison #" + selected.getId());
        dialog.setContentText("Nouveau statut:");

        dialog.showAndWait().ifPresent(newStatus -> {
            DeliveryStatus oldStatus = selected.getStatus();

            executorService.submit(() -> {
                try {
                    Delivery updated = deliveryService.updateDeliveryStatus(
                        selected.getId(), newStatus);
                    Platform.runLater(() -> {
                        int index = deliveryList.indexOf(selected);
                        deliveryList.set(index, updated);
                        displayDeliveryDetails(updated);
                        statusLabel.setText("✓ Statut mis à jour");
                        statusLabel.setStyle("-fx-text-fill: green;");

                        // 🔔 NOTIFICATION: Changement de statut
                        notificationService.notifyStatusChange(
                            updated.getId(),
                            oldStatus.getDisplayName(),
                            newStatus.getDisplayName(),
                            updated.getTrackingNumber()
                        );

                        // 🔔 NOTIFICATION SPÉCIALE: Si livré
                        if (newStatus == DeliveryStatus.DELIVERED) {
                            notificationService.notifyDelivered(
                                updated.getId(),
                                updated.getTrackingNumber(),
                                updated.getShippingAddress().getRecipientName()
                            );
                        }

                        // 🔔 NOTIFICATION SPÉCIALE: Si retourné
                        if (newStatus == DeliveryStatus.RETURNED) {
                            notificationService.notifyReturned(
                                updated.getId(),
                                updated.getTrackingNumber(),
                                updated.getNotes()
                            );
                        }
                    });
                } catch (Exception e) {
                    Platform.runLater(() -> {
                        notificationService.notifyError("Échec de mise à jour du statut: " + e.getMessage());
                        showError("Erreur", e.getMessage());
                    });
                    e.printStackTrace();
                }
            });
        });
    }

    private void displayDeliveryDetails(Delivery delivery) {
        detailsPane.setVisible(true);

        lblDeliveryId.setText("ID: #" + delivery.getId());
        lblOrderId.setText("Commande: #" + delivery.getOrderId());
        lblStatus.setText(delivery.getStatus() != null ?
            delivery.getStatus().getDisplayName() : "N/A");
        lblCarrier.setText(delivery.getCarrier());
        lblTrackingNumber.setText(delivery.getTrackingNumber());
        lblShippingCost.setText(String.format("%.2f MAD",
            delivery.getShippingCost() != null ? delivery.getShippingCost() : 0.0));

        if (delivery.getEstimatedDeliveryDate() != null) {
            lblEstimatedDelivery.setText(delivery.getEstimatedDeliveryDate().format(dateFormatter));
        } else {
            lblEstimatedDelivery.setText("Non définie");
        }

        if (delivery.getActualDeliveryDate() != null) {
            lblActualDelivery.setText(delivery.getActualDeliveryDate().format(dateFormatter));
        } else {
            lblActualDelivery.setText("Non livrée");
        }

        txtNotes.setText(delivery.getNotes() != null ? delivery.getNotes() : "");

        // Afficher l'adresse de livraison
        if (delivery.getShippingAddress() != null) {
            ShippingAddress address = delivery.getShippingAddress();
            lblRecipientName.setText(address.getRecipientName());
            lblAddress.setText(address.getFullAddress());
            lblCity.setText(address.getCity());
            lblPostalCode.setText(address.getPostalCode());
            lblPhoneNumber.setText(address.getPhoneNumber());
        }

        // Mettre à jour la barre de progression
        updateStatusProgress(delivery.getStatus());
    }

    private void updateStatusProgress(DeliveryStatus status) {
        if (statusProgressBar == null || lblProgressStatus == null) return;

        double progress;
        String statusText;

        switch (status) {
            case PENDING:
                progress = 0.25;
                statusText = "En attente";
                statusProgressBar.setStyle("-fx-accent: orange;");
                break;
            case IN_PROGRESS:
                progress = 0.5;
                statusText = "En cours de livraison";
                statusProgressBar.setStyle("-fx-accent: blue;");
                break;
            case DELIVERED:
                progress = 1.0;
                statusText = "Livraison terminée";
                statusProgressBar.setStyle("-fx-accent: green;");
                break;
            case RETURNED:
                progress = 0.75;
                statusText = "Retournée";
                statusProgressBar.setStyle("-fx-accent: red;");
                break;
            case CANCELLED:
                progress = 0.0;
                statusText = "Annulée";
                statusProgressBar.setStyle("-fx-accent: gray;");
                break;
            default:
                progress = 0.0;
                statusText = "Statut inconnu";
        }

        statusProgressBar.setProgress(progress);
        lblProgressStatus.setText(statusText);
    }

    private void clearForm() {
        txtOrderIdInput.clear();
        txtRecipientName.clear();
        txtFullAddress.clear();
        txtCity.clear();
        txtPostalCode.clear();
        txtPhoneNumber.clear();
        if (lblCalculatedCost != null) {
            lblCalculatedCost.setText("");
        }
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showWarning(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void shutdown() {
        executorService.shutdown();
    }
}

