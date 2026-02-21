package com.danieldpereira.usersystem.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LogDAO {

    /**
     * Regista uma nova atividade no sistema.
     * * @param usuarioId ID do utilizador logado que fez a ação (pode ser null se for uma ação do sistema)
     * @param acao O nome da ação (ex: "LOGIN", "CRIAR_USUARIO", "EXCLUIR_USUARIO")
     * @param detalhes Texto extra descrevendo o que aconteceu
     */
    public void registrarLog(Long usuarioId, String acao, String detalhes) {
        String sql = "INSERT INTO logs_atividade (usuario_id, acao, detalhes) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (conn == null) return;

            // Se for uma ação sem utilizador logado (ex: tentativa de login falhada), passamos NULL
            if (usuarioId != null) {
                stmt.setLong(1, usuarioId);
            } else {
                stmt.setNull(1, java.sql.Types.BIGINT);
            }

            stmt.setString(2, acao);
            stmt.setString(3, detalhes);

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("❌ Erro ao registar log: " + e.getMessage());
        }
    }
}