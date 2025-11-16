module b2bfront {
    // 1. Requis pour JavaFX
    requires javafx.controls;
    requires javafx.fxml;

    // 2. Requis pour le Client HTTP
    requires java.net.http;

    // 3. Requis pour Jackson
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jsr310; // Pour les LocalDate

    // 4. Ouvre tes packages pour que JavaFX et Jackson fonctionnent
    opens com.example.b2bfront to javafx.fxml;
    opens com.example.b2bfront.controller to javafx.fxml;
    opens com.example.b2bfront.model to com.fasterxml.jackson.databind;

    // Exporte le package principal
    exports com.example.b2bfront;
}