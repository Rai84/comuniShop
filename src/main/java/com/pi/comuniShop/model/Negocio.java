package com.pi.comuniShop.model;

import jakarta.persistence.*;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "negocio")
public class Negocio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "telefone_comercial")
    private String telefoneComercial;

    @Column(name = "email_comercial")
    private String emailComercial;

    @Column(name = "hora_abertura")
    private LocalTime horaAbertura = LocalTime.of(8, 0);

    @Column(name = "hora_fechamento")
    private LocalTime horaFechamento = LocalTime.of(18, 0);

    @Column(name = "tempo_medio_servico")
    private Integer tempoMedioServico = 30;

    private boolean entregasAtivas = false;
    private boolean catalogoAtivo = true;
    private boolean agendamentoAtivo = false;
    private boolean funcionariosAtivos = false;
    private boolean ativo = true;

    @Column(name = "verificado")
    private boolean verificado = false;

    @OneToMany(mappedBy = "negocio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Catalogo> catalogos;

    // Construtores
    public Negocio() {
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTelefoneComercial() {
        return telefoneComercial;
    }

    public void setTelefoneComercial(String telefoneComercial) {
        this.telefoneComercial = telefoneComercial;
    }

    public String getEmailComercial() {
        return emailComercial;
    }

    public void setEmailComercial(String emailComercial) {
        this.emailComercial = emailComercial;
    }

    public LocalTime getHoraAbertura() {
        return horaAbertura;
    }

    public void setHoraAbertura(LocalTime horaAbertura) {
        this.horaAbertura = horaAbertura;
    }

    public LocalTime getHoraFechamento() {
        return horaFechamento;
    }

    public void setHoraFechamento(LocalTime horaFechamento) {
        this.horaFechamento = horaFechamento;
    }

    public Integer getTempoMedioServico() {
        return tempoMedioServico;
    }

    public void setTempoMedioServico(Integer tempoMedioServico) {
        this.tempoMedioServico = tempoMedioServico;
    }

    public boolean isEntregasAtivas() {
        return entregasAtivas;
    }

    public void setEntregasAtivas(boolean entregasAtivas) {
        this.entregasAtivas = entregasAtivas;
    }

    public boolean isCatalogoAtivo() {
        return catalogoAtivo;
    }

    public void setCatalogoAtivo(boolean catalogoAtivo) {
        this.catalogoAtivo = catalogoAtivo;
    }

    public boolean isAgendamentoAtivo() {
        return agendamentoAtivo;
    }

    public void setAgendamentoAtivo(boolean agendamentoAtivo) {
        this.agendamentoAtivo = agendamentoAtivo;
    }

    public boolean isFuncionariosAtivos() {
        return funcionariosAtivos;
    }

    public void setFuncionariosAtivos(boolean funcionariosAtivos) {
        this.funcionariosAtivos = funcionariosAtivos;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public boolean isVerificado() {
        return verificado;
    }

    public void setVerificado(boolean verificado) {
        this.verificado = verificado;
    }
}
