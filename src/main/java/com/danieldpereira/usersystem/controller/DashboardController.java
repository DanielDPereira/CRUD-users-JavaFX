package com.danieldpereira.usersystem.controller;

import com.danieldpereira.usersystem.model.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.io.IOException;

public class DashboardController {

    @FXML
    private Label lblBemVindo;

    private Usuario usuarioLogado;

    // Método chamado pelo LoginController para passar os dados
    public void setUsuarioLogado(Usuario usuario) {
        this.usuarioLogado = usuario;
        lblBemVindo.setText("Bem-vindo, " + usuario.getUsuario() + " (" + usuario.getNivelAcesso() + ")");
    }

    @FXML
    private void handleLogout() {
        try {
            // 1. Fecha a tela atual (Dashboard)
            Stage stageAtual = (Stage) lblBemVindo.getScene().getWindow();
            stageAtual.close();

            // 2. Reabre a tela de Login
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/danieldpereira/usersystem/view/Login.fxml"));
            Parent root = loader.load();

            Stage stageLogin = new Stage();
            stageLogin.setTitle("Login - User Management System");
            stageLogin.setScene(new Scene(root));
            stageLogin.setResizable(false);
            stageLogin.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}