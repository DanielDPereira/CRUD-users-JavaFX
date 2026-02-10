package com.danieldpereira.usersystem;

import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("Sistema de Usuários - Iniciando");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}