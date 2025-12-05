package com.pi.comuniShop.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pi.comuniShop.model.Agendamento;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    // Busca agendamentos da loja por dia (para bloquear horários)
    List<Agendamento> findByNegocioIdAndData(Long negocioId, LocalDate data);

    List<Agendamento> findByClienteId(Long clienteId);

    // Verifica conflito
    boolean existsByNegocioIdAndDataAndHoraInicio(Long negocioId, LocalDate data, LocalTime horaInicio);

}
