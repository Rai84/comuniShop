package com.pi.comuniShop.dto;

public record AgendamentoDTO(
        Long id,
        String data,
        String horaInicio,
        String horaFim,
        String negocio,
        String item,
        Integer duracaoMinutos) {
}
