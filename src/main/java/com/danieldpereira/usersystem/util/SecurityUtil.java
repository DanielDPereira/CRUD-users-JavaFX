package com.danieldpereira.usersystem.util;

import org.mindrot.jbcrypt.BCrypt;

public class SecurityUtil {

    /**
     * Gera um hash seguro da senha usando BCrypt.
     * @param senha A senha em texto plano.
     * @return O hash da senha.
     */
    public static String criptografarSenha(String senha) {
        // O gensalt() gera um "sal" aleatório a cada execução, tornando o hash único
        return BCrypt.hashpw(senha, BCrypt.gensalt());
    }

    /**
     * Verifica se a senha em texto plano corresponde ao hash armazenado.
     * @param senhaTextoPlano A senha fornecida pelo usuário no login.
     * @param hashArmazenado O hash salvo no banco de dados.
     * @return true se a senha estiver correta, false caso contrário.
     */
    public static boolean verificarSenha(String senhaTextoPlano, String hashArmazenado) {
        if (senhaTextoPlano == null || hashArmazenado == null) {
            return false;
        }
        try {
            return BCrypt.checkpw(senhaTextoPlano, hashArmazenado);
        } catch (IllegalArgumentException e) {
            System.err.println("Erro ao verificar senha: Hash inválido.");
            return false;
        }
    }
}