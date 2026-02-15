package com.danieldpereira.usersystem.controller;

import com.danieldpereira.usersystem.dao.UsuarioDAO;
import com.danieldpereira.usersystem.model.NivelAcesso;
import com.danieldpereira.usersystem.model.StatusConta;
import com.danieldpereira.usersystem.model.Usuario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;

public class DashboardController {

    @FXML
    private Label lblBemVindo;

    // Componentes da Tabela
    @FXML private TableView<Usuario> tabelaUsuarios;
    @FXML private TableColumn<Usuario, Long> colId;
    @FXML private TableColumn<Usuario, String> colUsuario;
    @FXML private TableColumn<Usuario, String> colEmail;
    @FXML private TableColumn<Usuario, NivelAcesso> colNivel;
    @FXML private TableColumn<Usuario, StatusConta> colStatus;

    // Botões de Ação
    @FXML private Button btnEditar;
    @FXML private Button btnExcluir;

    private Usuario usuarioLogado;
    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    // Método executado automaticamente quando a tela abre
    @FXML
    public void initialize() {
        configurarColunas();
        carregarDados();

        // Listener: Monitora a seleção na tabela
        // Se algo for selecionado, habilita os botões. Se não, desabilita.
        tabelaUsuarios.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            boolean linhaSelecionada = (newSelection != null);

            // Verifica se os botões foram carregados antes de tentar alterar propriedades
            if (btnEditar != null && btnExcluir != null) {
                btnEditar.setDisable(!linhaSelecionada);
                btnExcluir.setDisable(!linhaSelecionada);
            }
        });
    }

    private void configurarColunas() {
        // Diz para cada coluna qual atributo da classe Usuario ela deve mostrar
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colUsuario.setCellValueFactory(new PropertyValueFactory<>("usuario"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colNivel.setCellValueFactory(new PropertyValueFactory<>("nivelAcesso"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("statusConta"));
    }

    private void carregarDados() {
        // Busca do banco e converte para uma lista que o JavaFX entende (ObservableList)
        ObservableList<Usuario> dados = FXCollections.observableArrayList(usuarioDAO.listarTodos());
        tabelaUsuarios.setItems(dados);
    }

    public void setUsuarioLogado(Usuario usuario) {
        this.usuarioLogado = usuario;
        lblBemVindo.setText("Bem-vindo, " + usuario.getUsuario() + " (" + usuario.getNivelAcesso() + ")");
    }

    // --- MÉTODOS DOS BOTÕES ---

    @FXML
    private void handleNovoUsuario() {
        System.out.println("Clicou em Novo Usuário");
        // Futuro: Abrir formulário modal (Pop-up)
    }

    @FXML
    private void handleEditarUsuario() {
        Usuario selecionado = tabelaUsuarios.getSelectionModel().getSelectedItem();
        if (selecionado != null) {
            System.out.println("Editando: " + selecionado.getUsuario());
            // Futuro: Abrir formulário preenchido
        }
    }

    @FXML
    private void handleExcluirUsuario() {
        Usuario selecionado = tabelaUsuarios.getSelectionModel().getSelectedItem();
        if (selecionado != null) {

            // Confirmação simples
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Excluir Usuário");
            alert.setHeaderText(null);
            alert.setContentText("Tem certeza que deseja excluir o usuário " + selecionado.getUsuario() + "?");

            if (alert.showAndWait().get() == ButtonType.OK) {
                System.out.println("Excluindo: " + selecionado.getUsuario());
                // Futuro: Chamar DAO e atualizar tabela
            }
        }
    }

    @FXML
    private void handleLogout() {
        try {
            Stage stageAtual = (Stage) lblBemVindo.getScene().getWindow();
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