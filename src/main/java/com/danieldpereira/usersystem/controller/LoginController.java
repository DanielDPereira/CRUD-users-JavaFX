package com.danieldpereira.usersystem.controller;

import com.danieldpereira.usersystem.model.NivelAcesso;
import com.danieldpereira.usersystem.dao.LogDAO;
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

    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtSenha;

    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    private LogDAO logDAO = new LogDAO(); // <-- NOVO: Instanciamos o LogDAO

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

            // --- REGISTO DE LOG: Sucesso ---
            logDAO.registrarLog(usuario.getId(), "LOGIN", "Login realizado com sucesso.");

            try {
                // VERIFICA O NÍVEL DE ACESSO PARA DECIDIR QUAL TELA ABRIR
                boolean isAdmin = usuario.getNivelAcesso() == com.danieldpereira.usersystem.model.NivelAcesso.ADMIN;
                String fxmlPath = isAdmin ? "/com/danieldpereira/usersystem/view/Dashboard.fxml"
                        : "/com/danieldpereira/usersystem/view/UserDashboard.fxml";

                FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
                Parent root = loader.load();

                // Passa os dados do usuário para o controller correto
                if (isAdmin) {
                    DashboardController controller = loader.getController();
                    controller.setUsuarioLogado(usuario);
                } else {
                    UserDashboardController controller = loader.getController();
                    controller.setUsuarioLogado(usuario);
                }

                Stage stage = new Stage();
                stage.setTitle(isAdmin ? "Painel do Administrador" : "Painel do Usuário");
                stage.setScene(new Scene(root));

                // Maximizar apenas para o admin (o usuário comum fica numa janela menor e centralizada)
                if (isAdmin) {
                    stage.setMaximized(true);
                } else {
                    stage.setWidth(800);
                    stage.setHeight(600);
                }

                stage.show();

                Stage loginStage = (Stage) txtUsuario.getScene().getWindow();
                loginStage.close();

            } catch (IOException e) {
                e.printStackTrace();
                mostrarAlerta("Erro Crítico", "Não foi possível carregar o sistema.");
            }

        } else {
            // --- REGISTO DE LOG: Falha ---
            // Passamos null no ID porque não sabemos quem tentou logar (ou se o utilizador sequer existe)
            logDAO.registrarLog(null, "FALHA_LOGIN", "Tentativa de login falhada para o utilizador: " + login);
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