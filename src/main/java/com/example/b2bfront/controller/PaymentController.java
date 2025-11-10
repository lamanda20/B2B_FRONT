package com.example.b2bfront.controller;

import com.example.b2bfront.model.Payment;
import com.example.b2bfront.service.ApiService;
import com.example.b2bfront.service.PaymentService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

public class PaymentController {

    // ==================== FXML COMPONENTS - STATUS ====================
    @FXML private Label statusLabel;

    // ==================== FXML COMPONENTS - FILTERS ====================
    @FXML private ComboBox<String> cmbStatusFilter;
    @FXML private TextField txtTransactionSearch;

    // ==================== FXML COMPONENTS - TABLE ====================
    @FXML private TableView<Payment> paymentTable;
    @FXML private TableColumn<Payment, Long> colId;
    @FXML private TableColumn<Payment, String> colOrderId;
    @FXML private TableColumn<Payment, Double> colAmount;
    @FXML private TableColumn<Payment, String> colMethod;
    @FXML private TableColumn<Payment, String> colStatus;
    @FXML private TableColumn<Payment, String> colDate;

    // ==================== FXML COMPONENTS - CREATE FORM ====================
    @FXML private TextField txtOrderIdInput;
    @FXML private TextField txtAmount;
    @FXML private ComboBox<String> cmbPaymentMethod;
    @FXML private TextField txtReference;
    @FXML private TextArea txtPaymentNotes;

    // ==================== FXML COMPONENTS - DETAILS PANE ====================
    @FXML private VBox detailsPane;
    @FXML private Label lblPaymentId, lblTransactionId, lblOrderId, lblAmount, lblPaymentMethod;
    @FXML private Label lblPaymentDate, lblValidationDate, lblStatus, lblReference;
    @FXML private Label lblSenderAccount, lblReceiverAccount, lblAuthCode;
    @FXML private TextArea txtNotes, txtHistory;
    @FXML private Label lblCalculatedCost;

    // ==================== SERVICES ====================
    private ApiService apiService;
    private PaymentService paymentService;

    // ==================== INITIALIZATION ====================
    @FXML
    private void initialize() {
        System.out.println("PaymentController initialized");
        statusLabel.setText("Module Paiements chargé avec succès ✅");

        // Initialiser les services
        apiService = new ApiService();
        paymentService = new PaymentService(apiService);

        // Initialiser les ComboBox
        initializeComboBoxes();

        // Cacher le panneau de détails au démarrage
        if (detailsPane != null) detailsPane.setVisible(false);

        // Configurer le TableView
        initializeTable();

        System.out.println("PaymentController prêt à l'emploi");
    }

    private void initializeComboBoxes() {
        if (cmbStatusFilter != null) {
            cmbStatusFilter.getItems().addAll("Tous","EN_ATTENTE","VALIDÉ","REFUSÉ","REMBOURSÉ");
            cmbStatusFilter.setValue("Tous");
        }
        if (cmbPaymentMethod != null) {
            cmbPaymentMethod.getItems().addAll("CARTE_BANCAIRE","VIREMENT","CHEQUE","ESPECES","PAYPAL");
        }
    }

    private void initializeTable() {
        colId.setCellValueFactory(data -> new javafx.beans.property.SimpleLongProperty(data.getValue().getId()).asObject());
        colOrderId.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getOrderId()));
        colAmount.setCellValueFactory(data -> new javafx.beans.property.SimpleDoubleProperty(data.getValue().getAmount()).asObject());
        colMethod.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getMethod()));
        colStatus.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getStatus()));
        colDate.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDate()));
    }

    // ==================== ACTION HANDLERS ====================
    @FXML
    private void onLoadAllPayments() {
        System.out.println("📥 Chargement de tous les paiements...");
        statusLabel.setText("Chargement des paiements...");
        try {
            List<Payment> payments = paymentService.getAllPayments();
            paymentTable.getItems().setAll(payments);
            statusLabel.setText("✅ Paiements chargés");
        } catch (IOException e) {
            statusLabel.setText("❌ Erreur lors du chargement");
            showAlert("Erreur", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void onFilterByStatus() {
        String selectedStatus = cmbStatusFilter.getValue();
        System.out.println("🔍 Filtrage par statut: " + selectedStatus);
        statusLabel.setText("Filtrage par: " + selectedStatus);
        if (!"Tous".equals(selectedStatus)) {
            paymentTable.getItems().removeIf(payment -> !payment.getStatus().equals(selectedStatus));
        }
    }

    @FXML
    private void onSearchTransaction() {
        String transactionId = txtTransactionSearch.getText();
        if (transactionId == null || transactionId.trim().isEmpty()) {
            showAlert("Erreur", "Veuillez entrer un ID de transaction", Alert.AlertType.WARNING);
            return;
        }
        statusLabel.setText("Recherche de: " + transactionId);
        // Ici tu peux appeler un endpoint spécifique si existant
    }

    @FXML
    private void onCreatePayment() {
        if (!validateCreateForm()) return;
        String orderId = txtOrderIdInput.getText();
        double amount = Double.parseDouble(txtAmount.getText());
        String method = cmbPaymentMethod.getValue();
        String reference = txtReference.getText();
        String notes = txtPaymentNotes.getText();

        Payment payment = new Payment(orderId, amount, method, reference, notes);
        try {
            Payment created = paymentService.createPayment(payment);
            paymentTable.getItems().add(created);
            showAlert("Succès", "Paiement créé avec succès!", Alert.AlertType.INFORMATION);
            clearCreateForm();
            statusLabel.setText("✅ Paiement créé");
        } catch (IOException e) {
            statusLabel.setText("❌ Erreur lors de la création");
            showAlert("Erreur", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML private void onValidatePayment() {
        Payment selected = paymentTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Erreur", "Veuillez sélectionner un paiement", Alert.AlertType.WARNING);
            return;
        }
        try {
            paymentService.validatePayment(selected.getId());
            selected.setStatus("VALIDÉ");
            paymentTable.refresh();
            showAlert("Succès", "Paiement validé", Alert.AlertType.INFORMATION);
        } catch (IOException e) {
            showAlert("Erreur", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML private void onCancelPayment() {
        Payment selected = paymentTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Erreur", "Veuillez sélectionner un paiement", Alert.AlertType.WARNING);
            return;
        }
        try {
            paymentService.cancelPayment(selected.getId());
            selected.setStatus("REFUSÉ");
            paymentTable.refresh();
            showAlert("Succès", "Paiement annulé", Alert.AlertType.INFORMATION);
        } catch (IOException e) {
            showAlert("Erreur", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML private void onResetForm() {
        clearCreateForm();
        statusLabel.setText("Formulaire réinitialisé");
    }

    // ==================== HELPER METHODS ====================
    private boolean validateCreateForm() {
        if (txtOrderIdInput.getText().trim().isEmpty()) { showAlert("Erreur", "Le numéro de commande est obligatoire", Alert.AlertType.ERROR); return false; }
        if (txtAmount.getText().trim().isEmpty()) { showAlert("Erreur", "Le montant est obligatoire", Alert.AlertType.ERROR); return false; }
        try { double amount = Double.parseDouble(txtAmount.getText()); if (amount <= 0) { showAlert("Erreur", "Le montant doit être positif", Alert.AlertType.ERROR); return false; } }
        catch (NumberFormatException e) { showAlert("Erreur", "Le montant doit être un nombre valide", Alert.AlertType.ERROR); return false; }
        if (cmbPaymentMethod.getValue() == null) { showAlert("Erreur", "Veuillez sélectionner une méthode de paiement", Alert.AlertType.ERROR); return false; }
        return true;
    }

    private void clearCreateForm() {
        txtOrderIdInput.clear();
        txtAmount.clear();
        cmbPaymentMethod.setValue(null);
        txtReference.clear();
        txtPaymentNotes.clear();
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
