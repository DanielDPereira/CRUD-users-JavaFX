package com.danieldpereira.usersystem.controller;

import com.danieldpereira.usersystem.dao.LogDAO;
import com.danieldpereira.usersystem.dao.UsuarioDAO;
import com.danieldpereira.usersystem.model.NivelAcesso;
import com.danieldpereira.usersystem.model.StatusConta;
import com.danieldpereira.usersystem.model.Usuario;
import com.danieldpereira.usersystem.util.SecurityUtil;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class FormUsuarioController {

    @FXML private TextField txtUsuario;
    @FXML private TextField txtEmail;
    @FXML private PasswordField txtSenha;
    @FXML private ComboBox<NivelAcesso> cbNivel;
    @FXML private ComboBox<StatusConta> cbStatus;

    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    private LogDAO logDAO = new LogDAO(); // <-- NOVO

    private Usuario usuarioEdicao = null;
    private Usuario usuarioLogado = null; // <-- NOVO: Para sabermos quem está a fazer a alteração
    private boolean btnSalvarClicado = false;

    @FXML
    public void initialize() {
        cbNivel.getItems().setAll(NivelAcesso.values());
        cbStatus.getItems().setAll(StatusConta.values());
        cbNivel.getSelectionModel().select(NivelAcesso.COMUM);
        cbStatus.getSelectionModel().select(StatusConta.ATIVO);
    }

    // Recebe o utilizador logado a partir do Dashboard
    public void setUsuarioLogado(Usuario usuarioLogado) {
        this.usuarioLogado = usuarioLogado;
    }

    public void setUsuario(Usuario usuario) {
        this.usuarioEdicao = usuario;
        txtUsuario.setText(usuario.getUsuario());
        txtEmail.setText(usuario.getEmail());
        cbNivel.setValue(usuario.getNivelAcesso());
        cbStatus.setValue(usuario.getStatusConta());
        txtSenha.setPromptText("Deixe vazio para manter a senha");
    }

    @FXML
    private void handleSalvar() {
        if (validarCampos()) {

            if (usuarioEdicao == null) {
                Usuario novoUsuario = new Usuario(
                        txtUsuario.getText(),
                        txtEmail.getText(),
                        SecurityUtil.criptografarSenha(txtSenha.getText()),
                        cbNivel.getValue()
                );
                novoUsuario.setStatusConta(cbStatus.getValue());
                usuarioDAO.cadastrarUsuario(novoUsuario);

                // --- LOG: Criação ---
                if (usuarioLogado != null) {
                    logDAO.registrarLog(usuarioLogado.getId(), "CRIAR_USUARIO", "Criou o utilizador: " + novoUsuario.getUsuario());
                }

            } else {
                usuarioEdicao.setUsuario(txtUsuario.getText());
                usuarioEdicao.setEmail(txtEmail.getText());
                usuarioEdicao.setNivelAcesso(cbNivel.getValue());
                usuarioEdicao.setStatusConta(cbStatus.getValue());

                if (!txtSenha.getText().isEmpty()) {
                    usuarioEdicao.setSenhaHash(SecurityUtil.criptografarSenha(txtSenha.getText()));
                }

                usuarioDAO.atualizarUsuario(usuarioEdicao);

                // --- LOG: Edição ---
                if (usuarioLogado != null) {
                    logDAO.registrarLog(usuarioLogado.getId(), "EDITAR_USUARIO", "Atualizou o utilizador ID: " + usuarioEdicao.getId());
                }
            }

            btnSalvarClicado = true;
            fecharJanela();
        }
    }

    @FXML
    private void handleCancelar() {
        fecharJanela();
    }

    private void fecharJanela() {
        ((Stage) txtUsuario.getScene().getWindow()).close();
    }

    public boolean isBtnSalvarClicado() {
        return btnSalvarClicado;
    }

    private boolean validarCampos() {
        String erros = "";
        if (txtUsuario.getText() == null || txtUsuario.getText().isEmpty()) erros += "Usuário inválido\n";
        if (txtEmail.getText() == null || !txtEmail.getText().contains("@")) erros += "Email inválido\n";

        if (usuarioEdicao == null && (txtSenha.getText() == null || txtSenha.getText().length() < 3)) {
            erros += "Senha deve ter no mínimo 3 caracteres\n";
        }

        if (erros.length() > 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setContentText(erros);
            alert.showAndWait();
            return false;
        }
        return true;
    }
}