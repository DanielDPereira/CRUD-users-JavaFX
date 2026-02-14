package com.danieldpereira.usersystem.controller;

import com.danieldpereira.usersystem.dao.UsuarioDAO;
import com.danieldpereira.usersystem.model.Usuario;
import com.danieldpereira.usersystem.util.SecurityUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

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

        // 1. Busca o usuário no banco
        Usuario usuario = usuarioDAO.buscarPorUsuario(login);

        // 2. Verifica se existe e se a senha bate
        if (usuario != null && SecurityUtil.verificarSenha(senha, usuario.getSenhaHash())) {
            mostrarAlerta("Sucesso", "Login realizado com sucesso! Bem-vindo, " + usuario.getUsuario());
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