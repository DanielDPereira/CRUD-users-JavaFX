package com.danieldpereira.usersystem.controller;

import com.danieldpereira.usersystem.dao.UsuarioDAO;
import com.danieldpereira.usersystem.model.Usuario;
import com.danieldpereira.usersystem.util.SecurityUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class LoginController {

    @FXML
    private TextField txtUsuario;

    @FXML
    private PasswordField txtSenha;

    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    @FXML
    private void handleLogin() {
        String login = txtUsuario.getText();
        String senha = txtSenha.getText();

        if (login.isEmpty() || senha.isEmpty()) {
            mostrarAlerta("Erro", "Por favor, preencha todos os campos.");
            return;
        }

        Usuario usuario = usuarioDAO.buscarPorUsuario(login);

        if (usuario != null && SecurityUtil.verificarSenha(senha, usuario.getSenhaHash())) {

            try {
                // 1. Carregar o FXML do Dashboard
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/danieldpereira/usersystem/view/Dashboard.fxml"));
                Parent root = loader.load();

                // 2. Pegar o controlador e passar o usuário
                DashboardController dashboardController = loader.getController();
                dashboardController.setUsuarioLogado(usuario);

                // 3. Criar a nova cena
                Stage stage = new Stage();
                stage.setTitle("Sistema de Usuários - Dashboard");
                stage.setScene(new Scene(root));
                stage.setMaximized(true); // Abrir em tela cheia se quiser
                stage.show();

                // 4. Fechar a janela de Login
                Stage loginStage = (Stage) txtUsuario.getScene().getWindow();
                loginStage.close();

            } catch (IOException e) {
                e.printStackTrace();
                mostrarAlerta("Erro Crítico", "Não foi possível carregar o Dashboard.");
            }
            // -----------------------------------------------

        } else {
            mostrarAlerta("Falha no Login", "Usuário ou senha incorretos.");
        }
    }

    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}