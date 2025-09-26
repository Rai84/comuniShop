package com.pi.comuniShop.model;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "usuario", schema = "public")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "criado_em", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime criadoEm;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String senha;

    // O tipo é um ENUM no Postgres (public.tipo_usuario)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoUsuario tipo = TipoUsuario.USUARIO;

    @Column(name = "atualizad_em", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime atualizadEm;

    // Getters e Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public OffsetDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(OffsetDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public TipoUsuario getTipo() {
        return tipo;
    }

    public void setTipo(TipoUsuario tipo) {
        this.tipo = tipo;
    }

}
