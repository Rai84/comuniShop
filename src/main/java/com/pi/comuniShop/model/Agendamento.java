package com.pi.comuniShop.model;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Agendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Cliente que agendou
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Usuario cliente;

    // Loja / negócio
    @ManyToOne
    @JoinColumn(name = "negocio_id")
    private Negocio negocio;

    // Item do catálogo
    @ManyToOne
    @JoinColumn(name = "catalogo_id")
    private Catalogo item;

    private LocalDate data;
    private LocalTime horaInicio;
    private LocalTime horaFim;
    private Integer duracaoMinutos;
}
