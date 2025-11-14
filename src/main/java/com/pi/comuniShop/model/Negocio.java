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

    private String cnpj;
    private String descricao;

    @Column(name = "telefone_comercial")
    private String telefoneComercial;

    @Column(name = "email_comercial")
    private String emailComercial;

    private LocalTime horaAbertura = LocalTime.of(8, 0);
    private LocalTime horaFechamento = LocalTime.of(18, 0);

    private boolean entregasAtivas = false;
    private boolean catalogoAtivo = true;
    private boolean agendamentoAtivo = false;
    private boolean funcionariosAtivos = false;
    private boolean ativo = true;
    private boolean verificado = false;

    // Dono do negócio
    @ManyToOne
    @JoinColumn(name = "dono_id", nullable = false)
    private Usuario dono;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PlanoTipo plano = PlanoTipo.FREE;

    @OneToMany(mappedBy = "negocio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Catalogo> catalogos;

    // ============================
    // ENDEREÇO DO NEGÓCIO
    // ============================

    @Column(nullable = false)
    private String cep;

    @Column(nullable = false)
    private String estado;

    @Column(nullable = false)
    private String cidade;

    @Column(nullable = false)
    private String bairro;

    @Column(nullable = false)
    private String logradouro;

    @Column(nullable = false)
    private String numero;

    @Column
    private Double latitude;

    @Column
    private Double longitude;

    private String complemento;

    // ============================
    // GETTERS E SETTERS
    // ============================

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

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
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

    public Usuario getDono() {
        return dono;
    }

    public void setDono(Usuario dono) {
        this.dono = dono;
    }

    public PlanoTipo getPlano() {
        return plano;
    }

    public void setPlano(PlanoTipo plano) {
        this.plano = plano;
    }

    public List<Catalogo> getCatalogos() {
        return catalogos;
    }

    public void setCatalogos(List<Catalogo> catalogos) {
        this.catalogos = catalogos;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }
}
