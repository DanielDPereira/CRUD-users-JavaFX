package com.danieldpereira.usersystem.model;

import java.time.LocalDateTime;

public class Usuario {
    private Long id;
    private String usuario;
    private String email;
    private String senhaHash;
    private NivelAcesso nivelAcesso;
    private StatusConta statusConta;
    private LocalDateTime ultimoAcesso;
    private LocalDateTime dataCadastro;

    public Usuario() {
    }

    // Construtor para cadastro (sem ID e datas, pois o banco/sistema define)
    public Usuario(String usuario, String email, String senhaHash, NivelAcesso nivelAcesso) {
        this.usuario = usuario;
        this.email = email;
        this.senhaHash = senhaHash;
        this.nivelAcesso = nivelAcesso;
        this.statusConta = StatusConta.ATIVO; // Padrão ao criar
    }

    // Construtor completo (para leitura do banco)
    public Usuario(Long id, String usuario, String email, String senhaHash,
                   NivelAcesso nivelAcesso, StatusConta statusConta,
                   LocalDateTime ultimoAcesso, LocalDateTime dataCadastro) {
        this.id = id;
        this.usuario = usuario;
        this.email = email;
        this.senhaHash = senhaHash;
        this.nivelAcesso = nivelAcesso;
        this.statusConta = statusConta;
        this.ultimoAcesso = ultimoAcesso;
        this.dataCadastro = dataCadastro;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenhaHash() { return senhaHash; }
    public void setSenhaHash(String senhaHash) { this.senhaHash = senhaHash; }

    public NivelAcesso getNivelAcesso() { return nivelAcesso; }
    public void setNivelAcesso(NivelAcesso nivelAcesso) { this.nivelAcesso = nivelAcesso; }

    public StatusConta getStatusConta() { return statusConta; }
    public void setStatusConta(StatusConta statusConta) { this.statusConta = statusConta; }

    public LocalDateTime getUltimoAcesso() { return ultimoAcesso; }
    public void setUltimoAcesso(LocalDateTime ultimoAcesso) { this.ultimoAcesso = ultimoAcesso; }

    public LocalDateTime getDataCadastro() { return dataCadastro; }
    public void setDataCadastro(LocalDateTime dataCadastro) { this.dataCadastro = dataCadastro; }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", usuario='" + usuario + '\'' +
                ", email='" + email + '\'' +
                ", nivel=" + nivelAcesso +
                ", status=" + statusConta +
                '}';
    }
}