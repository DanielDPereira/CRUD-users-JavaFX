package com.danieldpereira.usersystem;

import com.danieldpereira.usersystem.dao.UsuarioDAO;
import com.danieldpereira.usersystem.model.NivelAcesso;
import com.danieldpereira.usersystem.model.Usuario;

public class TesteCadastro {
    public static void main(String[] args) {
        // 1. Instancia o DAO
        UsuarioDAO dao = new UsuarioDAO();

        // 2. Cria um usuário fictício (Simulando dados vindos de um formulário)
        // Nota: A senha aqui é "123456", mas será salva como hash no banco!
        Usuario novoUser = new Usuario(
                "admin_teste",
                "admin@email.com",
                "123456",
                NivelAcesso.ADMIN
        );

        System.out.println("🚀 Tentando cadastrar usuário...");
        dao.cadastrarUsuario(novoUser);
    }
}