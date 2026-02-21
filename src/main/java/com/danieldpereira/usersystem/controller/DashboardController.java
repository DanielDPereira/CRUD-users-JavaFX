package com.danieldpereira.usersystem.controller;
import com.danieldpereira.usersystem.model.LogAtividade;
import java.time.LocalDateTime;
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
import javafx.stage.Modality;
import java.io.IOException;
import com.danieldpereira.usersystem.dao.LogDAO;

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

    // Componentes da Aba de Auditoria
    @FXML private TabPane tabPane;
    @FXML private Tab tabAuditoria;
    @FXML private TableView<LogAtividade> tabelaLogs;
    @FXML private TableColumn<LogAtividade, Long> colLogId;
    @FXML private TableColumn<LogAtividade, LocalDateTime> colLogData;
    @FXML private TableColumn<LogAtividade, Long> colLogUsuarioId;
    @FXML private TableColumn<LogAtividade, String> colLogAcao;
    @FXML private TableColumn<LogAtividade, String> colLogDetalhes;

    // Botões de Ação
    @FXML private Button btnEditar;
    @FXML private Button btnExcluir;

    private Usuario usuarioLogado;
    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    private LogDAO logDAO = new LogDAO();

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
        // Colunas da aba Usuários
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colUsuario.setCellValueFactory(new PropertyValueFactory<>("usuario"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colNivel.setCellValueFactory(new PropertyValueFactory<>("nivelAcesso"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("statusConta"));

        // Colunas da aba Logs
        colLogId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colLogData.setCellValueFactory(new PropertyValueFactory<>("dataHora"));
        colLogUsuarioId.setCellValueFactory(new PropertyValueFactory<>("usuarioId"));
        colLogAcao.setCellValueFactory(new PropertyValueFactory<>("acao"));
        colLogDetalhes.setCellValueFactory(new PropertyValueFactory<>("detalhes"));
    }

    private void carregarDados() {
        // Carrega Usuários
        ObservableList<Usuario> dadosUsuarios = FXCollections.observableArrayList(usuarioDAO.listarTodos());
        tabelaUsuarios.setItems(dadosUsuarios);

        // Carrega Logs
        ObservableList<LogAtividade> dadosLogs = FXCollections.observableArrayList(logDAO.listarTodos());
        tabelaLogs.setItems(dadosLogs);
    }

    public void setUsuarioLogado(Usuario usuario) {
        this.usuarioLogado = usuario;
        lblBemVindo.setText("Bem-vindo, " + usuario.getUsuario() + " (" + usuario.getNivelAcesso() + ")");

        // REGRA DE SEGURANÇA NA INTERFACE:
        // Se o usuário logado NÃO for ADMIN, removemos a aba de Auditoria
        if (usuario.getNivelAcesso() != NivelAcesso.ADMIN) {
            tabPane.getTabs().remove(tabAuditoria);
        }
    }

    // --- MÉTODOS DOS BOTÕES ---

    @FXML
    private void handleNovoUsuario() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/danieldpereira/usersystem/view/FormUsuario.fxml"));
            Parent page = loader.load();

            FormUsuarioController controller = loader.getController();
            controller.setUsuarioLogado(usuarioLogado);

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Novo Usuário");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(lblBemVindo.getScene().getWindow());
            dialogStage.setScene(new Scene(page));

            dialogStage.showAndWait();

            if (controller.isBtnSalvarClicado()) {
                carregarDados();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleEditarUsuario() {
        Usuario selecionado = tabelaUsuarios.getSelectionModel().getSelectedItem();
        if (selecionado != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/danieldpereira/usersystem/view/FormUsuario.fxml"));
                Parent page = loader.load();

                FormUsuarioController controller = loader.getController();
                controller.setUsuarioLogado(usuarioLogado);
                controller.setUsuario(selecionado);

                Stage dialogStage = new Stage();
                dialogStage.setTitle("Editar Usuário");
                dialogStage.initModality(Modality.WINDOW_MODAL);
                dialogStage.initOwner(lblBemVindo.getScene().getWindow());
                dialogStage.setScene(new Scene(page));

                dialogStage.showAndWait();

                if (controller.isBtnSalvarClicado()) {
                    carregarDados();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleExcluirUsuario() {
        Usuario selecionado = tabelaUsuarios.getSelectionModel().getSelectedItem();
        if (selecionado != null) {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Excluir Usuário");
            alert.setHeaderText("Atenção!");
            alert.setContentText("Deseja realmente excluir: " + selecionado.getUsuario() + "?");

            if (alert.showAndWait().get() == ButtonType.OK) {
                usuarioDAO.excluirUsuario(selecionado.getId());

                // --- LOG: Exclusão ---
                logDAO.registrarLog(usuarioLogado.getId(), "EXCLUIR_USUARIO", "Excluiu o utilizador: " + selecionado.getUsuario());

                carregarDados();
            }
        }
    }

    @FXML
    private void handleLogout() {
        try {
            // --- LOG: Logout ---
            if (usuarioLogado != null) {
                logDAO.registrarLog(usuarioLogado.getId(), "LOGOUT", "Encerrou a sessão.");
            }

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