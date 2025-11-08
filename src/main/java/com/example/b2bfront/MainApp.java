package com.example.b2bfront;

import com.example.b2bfront.util.Constants;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/b2bfront/main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), Constants.APP_WIDTH, Constants.APP_HEIGHT);
        stage.setTitle(Constants.APP_TITLE);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
