package com.danieldpereira.usersystem.controller;

import com.danieldpereira.usersystem.dao.UsuarioDAO;
import com.danieldpereira.usersystem.model.NivelAcesso;
import com.danieldpereira.usersystem.model.StatusConta;
import com.danieldpereira.usersystem.model.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class FormUsuarioController {

    @FXML private TextField txtUsuario;
    @FXML private TextField txtEmail;
    @FXML private PasswordField txtSenha;
    @FXML private ComboBox<NivelAcesso> cbNivel;
    @FXML private ComboBox<StatusConta> cbStatus;

    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    private boolean btnSalvarClicado = false; // Para saber se o usuário salvou ou cancelou

    @FXML
    public void initialize() {
        // Preenche os comboboxes com os valores dos ENUMs
        cbNivel.getItems().setAll(NivelAcesso.values());
        cbStatus.getItems().setAll(StatusConta.values());

        // Seleciona valores padrão
        cbNivel.getSelectionModel().select(NivelAcesso.COMUM);
        cbStatus.getSelectionModel().select(StatusConta.ATIVO);
    }

    @FXML
    private void handleSalvar() {
        if (validarCampos()) {
            Usuario novoUsuario = new Usuario(
                    txtUsuario.getText(),
                    txtEmail.getText(),
                    txtSenha.getText(), // Nota: O DAO vai criptografar isso
                    cbNivel.getValue()
            );
            novoUsuario.setStatusConta(cbStatus.getValue());

            usuarioDAO.cadastrarUsuario(novoUsuario);

            btnSalvarClicado = true;
            fecharJanela();
        }
    }

    @FXML
    private void handleCancelar() {
        fecharJanela();
    }

    private void fecharJanela() {
        Stage stage = (Stage) txtUsuario.getScene().getWindow();
        stage.close();
    }

    // Retorna true se salvou com sucesso (útil para o Dashboard saber se deve atualizar a tabela)
    public boolean isBtnSalvarClicado() {
        return btnSalvarClicado;
    }

    private boolean validarCampos() {
        String erros = "";
        if (txtUsuario.getText() == null || txtUsuario.getText().isEmpty()) erros += "Usuário inválido\n";
        if (txtEmail.getText() == null || !txtEmail.getText().contains("@")) erros += "Email inválido\n";
        if (txtSenha.getText() == null || txtSenha.getText().length() < 3) erros += "Senha muito curta\n";

        if (erros.length() > 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro de Validação");
            alert.setHeaderText(null);
            alert.setContentText(erros);
            alert.showAndWait();
            return false;
        }
        return true;
    }
}