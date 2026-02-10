package com.danieldpereira.usersystem.dao;

import io.github.cdimascio.dotenv.Dotenv;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static Dotenv dotenv;

    static {
        String projectPath = System.getProperty("user.dir");

        System.out.println("📂 Buscando .env em: " + projectPath);

        File envFile = new File(projectPath, ".env");

        if (envFile.exists()) {

            dotenv = Dotenv.configure()
                    .directory(projectPath)
                    .ignoreIfMalformed()
                    .ignoreIfMissing()
                    .load();

            System.out.println("✅ Arquivo .env detectado!");
        } else {
            System.err.println("❌ CRÍTICO: O arquivo .env NÃO está em: " + projectPath);
        }
    }

    public static Connection getConnection() {
        // Se o dotenv não carregou (arquivo não achado), retorna null e avisa
        if (dotenv == null) {
            System.err.println("⚠️ Dotenv não inicializado. Verifique o arquivo .env");
            return null;
        }

        // Tenta pegar as variáveis (com tratamento para nulos)
        String url = dotenv.get("DB_URL");
        String user = dotenv.get("DB_USER");
        String pass = dotenv.get("DB_PASSWORD");

        if (url == null || user == null) {
            System.err.println("❌ Variáveis do .env estão vazias ou não foram lidas!");
            return null;
        }

        try {
            return DriverManager.getConnection(url, user, pass);
        } catch (SQLException e) {
            System.err.println("❌ Erro ao conectar no MySQL: " + e.getMessage());
            return null;
        }
    }

    public static void main(String[] args) {
        Connection conn = getConnection();
        if (conn != null) {
            System.out.println("Banco de dados conectado com sucesso");
        }
    }
}