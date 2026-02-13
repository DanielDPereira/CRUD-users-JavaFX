package com.danieldpereira.usersystem;

import com.danieldpereira.usersystem.dao.UsuarioDAO;
import com.danieldpereira.usersystem.model.Usuario;
import com.danieldpereira.usersystem.util.SecurityUtil;

public class TesteLogin {
    public static void main(String[] args) {
        UsuarioDAO dao = new UsuarioDAO();
        String usuarioAlvo = "admin_teste"; // O mesmo que criamos antes

        System.out.println("🔍 Buscando usuário: " + usuarioAlvo);
        Usuario userEncontrado = dao.buscarPorUsuario(usuarioAlvo);

        if (userEncontrado != null) {
            System.out.println("✅ Usuário encontrado: " + userEncontrado.getEmail());
            System.out.println("🔐 Hash no banco: " + userEncontrado.getSenhaHash());

            // Teste 1: Senha Correta
            String senhaDigitada = "123456";
            if (SecurityUtil.verificarSenha(senhaDigitada, userEncontrado.getSenhaHash())) {
                System.out.println("🔓 SUCESSO: Senha '" + senhaDigitada + "' é válida! Login aprovado.");
            } else {
                System.out.println("🚫 FALHA: Senha incorreta.");
            }

            // Teste 2: Senha Errada
            String senhaErrada = "senha_incorreta";
            if (SecurityUtil.verificarSenha(senhaErrada, userEncontrado.getSenhaHash())) {
                System.out.println("🔓 SUCESSO: Senha válida!");
            } else {
                System.out.println("🛡️ SEGURANÇA OK: Senha '" + senhaErrada + "' foi rejeitada.");
            }

        } else {
            System.err.println("❌ Usuário não encontrado no banco.");
        }
    }
}