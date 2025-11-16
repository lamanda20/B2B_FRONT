package com.example.b2bfront;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // 1. Charger le FXML
        // Il cherche dans /resources/com/example/b2bfront/LivraisonView.fxml
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("LivraisonView.fxml"));

        // 2. Créer la scène
        Scene scene = new Scene(fxmlLoader.load(), 450, 480); // Largeur, Hauteur

        // 3. Charger le CSS
        URL cssUrl = Main.class.getResource("styles.css");
        if (cssUrl != null) {
            scene.getStylesheets().add(cssUrl.toExternalForm());
        } else {
            System.out.println("Attention: Fichier styles.css non trouvé.");
        }

        // 4. Configurer et afficher la fenêtre principale
        stage.setTitle("Gestion de Livraison B2B");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}