package com.danieldpereira.usersystem.dao;

import com.danieldpereira.usersystem.model.Usuario;
import com.danieldpereira.usersystem.model.NivelAcesso;
import com.danieldpereira.usersystem.model.StatusConta;
import com.danieldpereira.usersystem.util.SecurityUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {

    /**
     * Busca um usuário pelo nome de login (para autenticação).
     * @param usuarioLogin O nome de usuário a pesquisar.
     * @return Objeto Usuario se encontrado, ou null se não existir.
     */
    public Usuario buscarPorUsuario(String usuarioLogin) {
        String sql = "SELECT * FROM usuarios WHERE usuario = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (conn == null) return null;

            stmt.setString(1, usuarioLogin);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Agora o Java reconhece NivelAcesso e StatusConta
                    return new Usuario(
                            rs.getLong("id"),
                            rs.getString("usuario"),
                            rs.getString("email"),
                            rs.getString("senha_hash"),
                            NivelAcesso.valueOf(rs.getString("nivel_acesso")),
                            StatusConta.valueOf(rs.getString("status_conta")),
                            rs.getTimestamp("ultimo_acesso") != null ? rs.getTimestamp("ultimo_acesso").toLocalDateTime() : null,
                            rs.getTimestamp("data_cadastro").toLocalDateTime()
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Erro ao buscar usuário: " + e.getMessage());
        }
        return null;
    }

    /**
     * Cadastra um novo usuário no banco de dados.
     * @param usuario Objeto contendo os dados do formulário (sem ID).
     */
    public void cadastrarUsuario(Usuario usuario) {
        String sql = "INSERT INTO usuarios (usuario, email, senha_hash, nivel_acesso, status_conta) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (conn == null) {
                System.err.println("❌ Falha na conexão com o banco!");
                return;
            }

            stmt.setString(1, usuario.getUsuario());
            stmt.setString(2, usuario.getEmail());

            String hash = SecurityUtil.criptografarSenha(usuario.getSenhaHash());
            stmt.setString(3, hash);

            stmt.setString(4, usuario.getNivelAcesso().name());
            stmt.setString(5, usuario.getStatusConta().name());

            stmt.executeUpdate();
            System.out.println("✅ Usuário cadastrado com sucesso: " + usuario.getUsuario());

        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                System.err.println("❌ Erro: Usuário ou Email já cadastrados.");
            } else {
                System.err.println("❌ Erro ao cadastrar usuário: " + e.getMessage());
            }
        }
    }
}