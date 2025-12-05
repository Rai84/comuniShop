package com.pi.comuniShop.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CarrinhoItem {
    private Long id;
    private String nome;
    private BigDecimal preco;
    private int quantidade;
    private String imagem;
}

