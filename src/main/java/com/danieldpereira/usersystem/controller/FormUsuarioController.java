package com.danieldpereira.usersystem.controller;

import com.danieldpereira.usersystem.dao.UsuarioDAO;
import com.danieldpereira.usersystem.model.NivelAcesso;
import com.danieldpereira.usersystem.model.StatusConta;
import com.danieldpereira.usersystem.model.Usuario;
import com.danieldpereira.usersystem.util.SecurityUtil; // IMPORTANTE
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class FormUsuarioController {

    @FXML private TextField txtUsuario;
    @FXML private TextField txtEmail;
    @FXML private PasswordField txtSenha;
    @FXML private ComboBox<NivelAcesso> cbNivel;
    @FXML private ComboBox<StatusConta> cbStatus;
    @FXML private Label lblTitulo; // (Opcional) Para mudar texto "Cadastro" para "Edição"

    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    private Usuario usuarioEdicao = null; // Se for null, é NOVO. Se tiver objeto, é EDIÇÃO.
    private boolean btnSalvarClicado = false;

    @FXML
    public void initialize() {
        cbNivel.getItems().setAll(NivelAcesso.values());
        cbStatus.getItems().setAll(StatusConta.values());

        cbNivel.getSelectionModel().select(NivelAcesso.COMUM);
        cbStatus.getSelectionModel().select(StatusConta.ATIVO);
    }

    // Método chamado pelo Dashboard para passar os dados do usuário a ser editado
    public void setUsuario(Usuario usuario) {
        this.usuarioEdicao = usuario;

        txtUsuario.setText(usuario.getUsuario());
        txtEmail.setText(usuario.getEmail());
        cbNivel.setValue(usuario.getNivelAcesso());
        cbStatus.setValue(usuario.getStatusConta());

        // Senha fica vazia. Se o usuário digitar algo, trocamos. Se deixar vazia, mantemos a antiga.
        txtSenha.setPromptText("Deixe vazio para manter a senha");
    }

    @FXML
    private void handleSalvar() {
        if (validarCampos()) {

            if (usuarioEdicao == null) {
                // --- MODO CADASTRO (Novo) ---
                Usuario novoUsuario = new Usuario(
                        txtUsuario.getText(),
                        txtEmail.getText(),
                        SecurityUtil.criptografarSenha(txtSenha.getText()), // Criptografa aqui
                        cbNivel.getValue()
                );
                novoUsuario.setStatusConta(cbStatus.getValue());
                usuarioDAO.cadastrarUsuario(novoUsuario);

            } else {
                // --- MODO EDIÇÃO (Atualizar) ---
                usuarioEdicao.setUsuario(txtUsuario.getText());
                usuarioEdicao.setEmail(txtEmail.getText());
                usuarioEdicao.setNivelAcesso(cbNivel.getValue());
                usuarioEdicao.setStatusConta(cbStatus.getValue());

                // Lógica da Senha na Edição
                if (!txtSenha.getText().isEmpty()) {
                    // Se digitou senha nova, criptografa e salva
                    usuarioEdicao.setSenhaHash(SecurityUtil.criptografarSenha(txtSenha.getText()));
                }
                // Se não digitou, mantém o hash antigo que já está no objeto usuarioEdicao

                usuarioDAO.atualizarUsuario(usuarioEdicao);
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

        // Validação de senha: Obrigatória APENAS se for NOVO cadastro
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