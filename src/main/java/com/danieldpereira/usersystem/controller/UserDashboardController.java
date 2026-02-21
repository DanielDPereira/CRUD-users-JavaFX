package com.danieldpereira.usersystem.controller;

import com.danieldpereira.usersystem.dao.LogDAO;
import com.danieldpereira.usersystem.model.Usuario;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class UserDashboardController {

    @FXML
    private Label lblOla;

    private Usuario usuarioLogado;
    private LogDAO logDAO = new LogDAO();

    public void setUsuarioLogado(Usuario usuario) {
        this.usuarioLogado = usuario;

        // Formata o texto para "Olá, nome!" (Capitalizando a primeira letra)
        String nome = usuario.getUsuario();
        String nomeFormatado = nome.substring(0, 1).toUpperCase() + nome.substring(1);

        lblOla.setText("Olá, " + nomeFormatado + "!");
    }

    @FXML
    private void handleLogout() {
        try {
            if (usuarioLogado != null) {
                logDAO.registrarLog(usuarioLogado.getId(), "LOGOUT", "Encerrou a sessão.");
            }

            Stage stageAtual = (Stage) lblOla.getScene().getWindow();
            stageAtual.close();

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