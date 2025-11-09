module com.example.b2bfront {
    requires javafx.controls;
    requires javafx.fxml;
    requires okhttp3;
    requires com.google.gson;

    opens com.example.b2bfront to javafx.fxml;
    opens com.example.b2bfront.controller to javafx.fxml;
    opens com.example.b2bfront.model to com.google.gson;

    exports com.example.b2bfront;
    exports com.example.b2bfront.controller;
}
