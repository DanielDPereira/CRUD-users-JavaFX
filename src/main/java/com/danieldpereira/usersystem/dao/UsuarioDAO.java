package com.danieldpereira.usersystem.dao;

import com.danieldpereira.usersystem.model.Usuario;
import com.danieldpereira.usersystem.util.SecurityUtil; // Importando nosso utilitário
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UsuarioDAO {

    /**
     * Cadastra um novo usuário no banco de dados.
     * @param usuario Objeto contendo os dados do formulário (sem ID).
     */
    public void cadastrarUsuario(Usuario usuario) {
        String sql = "INSERT INTO usuarios (usuario, email, senha_hash, nivel_acesso, status_conta) VALUES (?, ?, ?, ?, ?)";

        // 1. Obtém conexão
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (conn == null) {
                System.err.println("❌ Falha na conexão com o banco!");
                return;
            }

            // 2. Prepara os dados
            stmt.setString(1, usuario.getUsuario());
            stmt.setString(2, usuario.getEmail());

            // 🔒 CRIPTOGRAFIA: Nunca salvamos a senha pura!
            String hash = SecurityUtil.criptografarSenha(usuario.getSenhaHash());
            stmt.setString(3, hash);

            // Conversão de ENUM para String (ex: NivelAcesso.ADMIN -> "ADMIN")
            stmt.setString(4, usuario.getNivelAcesso().name());
            stmt.setString(5, usuario.getStatusConta().name());

            // 3. Executa
            stmt.executeUpdate();
            System.out.println("✅ Usuário cadastrado com sucesso: " + usuario.getUsuario());

        } catch (SQLException e) {
            // Tratamento básico de erro (ex: duplicidade de email/usuário)
            if (e.getErrorCode() == 1062) { // Código de erro MySQL para duplicate entry
                System.err.println("❌ Erro: Usuário ou Email já cadastrados.");
            } else {
                System.err.println("❌ Erro ao cadastrar usuário: " + e.getMessage());
            }
        }
    }
}