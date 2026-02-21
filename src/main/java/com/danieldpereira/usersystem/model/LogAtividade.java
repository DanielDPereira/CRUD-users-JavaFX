package com.danieldpereira.usersystem.model;

import java.time.LocalDateTime;

public class LogAtividade {
    private Long id;
    private Long usuarioId;
    private String acao;
    private String detalhes;
    private LocalDateTime dataHora;

    public LogAtividade() {
    }

    public LogAtividade(Long usuarioId, String acao, String detalhes) {
        this.usuarioId = usuarioId;
        this.acao = acao;
        this.detalhes = detalhes;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }

    public String getAcao() { return acao; }
    public void setAcao(String acao) { this.acao = acao; }

    public String getDetalhes() { return detalhes; }
    public void setDetalhes(String detalhes) { this.detalhes = detalhes; }

    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }
}