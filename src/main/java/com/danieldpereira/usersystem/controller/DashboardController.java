package com.danieldpereira.usersystem.controller;

import com.danieldpereira.usersystem.dao.LogDAO;
import com.danieldpereira.usersystem.dao.UsuarioDAO;
import com.danieldpereira.usersystem.model.LogAtividade;
import com.danieldpereira.usersystem.model.NivelAcesso;
import com.danieldpereira.usersystem.model.StatusConta;
import com.danieldpereira.usersystem.model.Usuario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;

public class DashboardController {

    @FXML private Label lblBemVindo;

    // Componentes da Tabela - Usuários
    @FXML private TableView<Usuario> tabelaUsuarios;
    @FXML private TableColumn<Usuario, Long> colId;
    @FXML private TableColumn<Usuario, String> colUsuario;
    @FXML private TableColumn<Usuario, String> colEmail;
    @FXML private TableColumn<Usuario, NivelAcesso> colNivel;
    @FXML private TableColumn<Usuario, StatusConta> colStatus;

    // Filtro e Paginação - Usuários
    @FXML private TextField txtBuscaUsuario;
    @FXML private Button btnPrevUsuario;
    @FXML private Button btnNextUsuario;
    @FXML private Label lblPaginaUsuario;

    // Componentes da Aba de Auditoria
    @FXML private TabPane tabPane;
    @FXML private Tab tabAuditoria;
    @FXML private TableView<LogAtividade> tabelaLogs;
    @FXML private TableColumn<LogAtividade, Long> colLogId;
    @FXML private TableColumn<LogAtividade, LocalDateTime> colLogData;
    @FXML private TableColumn<LogAtividade, Long> colLogUsuarioId;
    @FXML private TableColumn<LogAtividade, String> colLogAcao;
    @FXML private TableColumn<LogAtividade, String> colLogDetalhes;

    // Filtro e Paginação - Logs
    @FXML private TextField txtBuscaLog;
    @FXML private Button btnPrevLog;
    @FXML private Button btnNextLog;
    @FXML private Label lblPaginaLog;

    // Botões de Ação
    @FXML private Button btnEditar;
    @FXML private Button btnExcluir;

    // Variáveis de Controle
    private Usuario usuarioLogado;
    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    private LogDAO logDAO = new LogDAO();

    // Configurações de Paginação
    private static final int ITENS_POR_PAGINA = 15;

    private ObservableList<Usuario> masterUsuarios = FXCollections.observableArrayList();
    private FilteredList<Usuario> filteredUsuarios;
    private int paginaAtualUsuarios = 0;

    private ObservableList<LogAtividade> masterLogs = FXCollections.observableArrayList();
    private FilteredList<LogAtividade> filteredLogs;
    private int paginaAtualLogs = 0;

    @FXML
    public void initialize() {
        configurarColunas();
        configurarFiltros();
        carregarDados();

        // Controla habilitação dos botões Editar/Excluir pela seleção da tabela
        tabelaUsuarios.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            boolean linhaSelecionada = (newSelection != null);
            if (btnEditar != null && btnExcluir != null) {
                btnEditar.setDisable(!linhaSelecionada);
                btnExcluir.setDisable(!linhaSelecionada);
            }
        });
    }

    private void configurarColunas() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colUsuario.setCellValueFactory(new PropertyValueFactory<>("usuario"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colNivel.setCellValueFactory(new PropertyValueFactory<>("nivelAcesso"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("statusConta"));

        colLogId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colLogData.setCellValueFactory(new PropertyValueFactory<>("dataHora"));
        colLogUsuarioId.setCellValueFactory(new PropertyValueFactory<>("usuarioId"));
        colLogAcao.setCellValueFactory(new PropertyValueFactory<>("acao"));
        colLogDetalhes.setCellValueFactory(new PropertyValueFactory<>("detalhes"));
    }

    private void configurarFiltros() {
        // Filtro em tempo real para Usuários
        txtBuscaUsuario.textProperty().addListener((observable, oldValue, newValue) -> {
            if (filteredUsuarios != null) {
                filteredUsuarios.setPredicate(user -> {
                    if (newValue == null || newValue.isEmpty()) return true; // Mostra tudo
                    String filtro = newValue.toLowerCase();
                    return user.getUsuario().toLowerCase().contains(filtro) ||
                            user.getEmail().toLowerCase().contains(filtro);
                });
                paginaAtualUsuarios = 0; // Volta para a página 1 ao pesquisar
                atualizarTabelaUsuarios();
            }
        });

        // Filtro em tempo real para Logs
        txtBuscaLog.textProperty().addListener((observable, oldValue, newValue) -> {
            if (filteredLogs != null) {
                filteredLogs.setPredicate(log -> {
                    if (newValue == null || newValue.isEmpty()) return true;
                    String filtro = newValue.toLowerCase();
                    return log.getAcao().toLowerCase().contains(filtro) ||
                            (log.getDetalhes() != null && log.getDetalhes().toLowerCase().contains(filtro));
                });
                paginaAtualLogs = 0;
                atualizarTabelaLogs();
            }
        });
    }

    private void carregarDados() {
        // Carrega do banco de dados para as listas "Master"
        masterUsuarios.setAll(usuarioDAO.listarTodos());
        filteredUsuarios = new FilteredList<>(masterUsuarios, p -> true);

        masterLogs.setAll(logDAO.listarTodos());
        filteredLogs = new FilteredList<>(masterLogs, p -> true);

        // Dispara a atualização visual das páginas
        atualizarTabelaUsuarios();
        atualizarTabelaLogs();
    }

    // --- LÓGICA DE PAGINAÇÃO ---

    private void atualizarTabelaUsuarios() {
        int totalItems = filteredUsuarios.size();
        int totalPages = (int) Math.ceil((double) totalItems / ITENS_POR_PAGINA);
        if (totalPages == 0) totalPages = 1;

        lblPaginaUsuario.setText("Página " + (paginaAtualUsuarios + 1) + " de " + totalPages);
        btnPrevUsuario.setDisable(paginaAtualUsuarios == 0);
        btnNextUsuario.setDisable(paginaAtualUsuarios >= totalPages - 1);

        int fromIndex = paginaAtualUsuarios * ITENS_POR_PAGINA;
        int toIndex = Math.min(fromIndex + ITENS_POR_PAGINA, totalItems);

        if (fromIndex <= toIndex && fromIndex < totalItems) {
            tabelaUsuarios.setItems(FXCollections.observableArrayList(filteredUsuarios.subList(fromIndex, toIndex)));
        } else {
            tabelaUsuarios.setItems(FXCollections.observableArrayList());
        }
    }

    private void atualizarTabelaLogs() {
        int totalItems = filteredLogs.size();
        int totalPages = (int) Math.ceil((double) totalItems / ITENS_POR_PAGINA);
        if (totalPages == 0) totalPages = 1;

        lblPaginaLog.setText("Página " + (paginaAtualLogs + 1) + " de " + totalPages);
        btnPrevLog.setDisable(paginaAtualLogs == 0);
        btnNextLog.setDisable(paginaAtualLogs >= totalPages - 1);

        int fromIndex = paginaAtualLogs * ITENS_POR_PAGINA;
        int toIndex = Math.min(fromIndex + ITENS_POR_PAGINA, totalItems);

        if (fromIndex <= toIndex && fromIndex < totalItems) {
            tabelaLogs.setItems(FXCollections.observableArrayList(filteredLogs.subList(fromIndex, toIndex)));
        } else {
            tabelaLogs.setItems(FXCollections.observableArrayList());
        }
    }

    @FXML private void handlePrevUsuario() {
        if (paginaAtualUsuarios > 0) {
            paginaAtualUsuarios--;
            atualizarTabelaUsuarios();
        }
    }

    @FXML private void handleNextUsuario() {
        int totalPages = (int) Math.ceil((double) filteredUsuarios.size() / ITENS_POR_PAGINA);
        if (paginaAtualUsuarios < totalPages - 1) {
            paginaAtualUsuarios++;
            atualizarTabelaUsuarios();
        }
    }

    @FXML private void handlePrevLog() {
        if (paginaAtualLogs > 0) {
            paginaAtualLogs--;
            atualizarTabelaLogs();
        }
    }

    @FXML private void handleNextLog() {
        int totalPages = (int) Math.ceil((double) filteredLogs.size() / ITENS_POR_PAGINA);
        if (paginaAtualLogs < totalPages - 1) {
            paginaAtualLogs++;
            atualizarTabelaLogs();
        }
    }

    // --- CONTROLE DE SESSÃO E CRUD ---

    public void setUsuarioLogado(Usuario usuario) {
        this.usuarioLogado = usuario;
        lblBemVindo.setText("Bem-vindo, " + usuario.getUsuario() + " (" + usuario.getNivelAcesso() + ")");

        if (usuario.getNivelAcesso() != NivelAcesso.ADMIN) {
            tabPane.getTabs().remove(tabAuditoria);
        }
    }

    @FXML
    private void handleNovoUsuario() {
        abrirFormulario(null, "Novo Usuário");
    }

    @FXML
    private void handleEditarUsuario() {
        Usuario selecionado = tabelaUsuarios.getSelectionModel().getSelectedItem();
        if (selecionado != null) {
            abrirFormulario(selecionado, "Editar Usuário");
        }
    }

    private void abrirFormulario(Usuario usuario, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/danieldpereira/usersystem/view/FormUsuario.fxml"));
            Parent page = loader.load();

            FormUsuarioController controller = loader.getController();
            controller.setUsuarioLogado(usuarioLogado);
            if (usuario != null) {
                controller.setUsuario(usuario);
            }

            Stage dialogStage = new Stage();
            dialogStage.setTitle(titulo);
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
    private void handleExcluirUsuario() {
        Usuario selecionado = tabelaUsuarios.getSelectionModel().getSelectedItem();
        if (selecionado != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Excluir Usuário");
            alert.setHeaderText("Atenção!");
            alert.setContentText("Deseja realmente excluir: " + selecionado.getUsuario() + "?");

            if (alert.showAndWait().get() == ButtonType.OK) {
                usuarioDAO.excluirUsuario(selecionado.getId());
                logDAO.registrarLog(usuarioLogado.getId(), "EXCLUIR_USUARIO", "Excluiu o utilizador: " + selecionado.getUsuario());
                carregarDados();
            }
        }
    }

    @FXML
    private void handleLogout() {
        try {
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