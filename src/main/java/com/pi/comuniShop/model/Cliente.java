package com.pi.comuniShop.model;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "cliente", schema = "public")
public class Cliente {

    @Id
    @Column(name = "usuario_id")
    private Integer usuarioId;

    @OneToOne
    @MapsId // garante que a PK de Cliente seja a mesma do Usuario
    @JoinColumn(name = "usuario_id", referencedColumnName = "id")
    private Usuario usuario;

    @Column(name = "criado_em", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime criadoEm;

    @Column(nullable = false, unique = true)
    private String cpf;

    @Column(nullable = false)
    private String telefone;

    // Getters e Setters
    public Integer getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public OffsetDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(OffsetDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
}