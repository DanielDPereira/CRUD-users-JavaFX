package com.danieldpereira.usersystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Carrega o arquivo FXML da pasta resources
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/danieldpereira/usersystem/view/Login.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root, 400, 500); // Tamanho da janela (Largura, Altura)

            primaryStage.setTitle("Login - User Management System");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false); // Impede redimensionar a tela de login
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erro ao carregar FXML: Verifique o caminho do arquivo em resources.");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}