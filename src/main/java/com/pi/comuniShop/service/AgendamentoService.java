package com.pi.comuniShop.service;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pi.comuniShop.model.*;
import com.pi.comuniShop.repository.*;

@Service
public class AgendamentoService {

    @Autowired
    private AgendamentoRepository repo;

    @Autowired
    private UsuarioRepository usuarioRepo;

    @Autowired
    private NegocioService negocioService;

    @Autowired
    private CatalogoService catalogoService;

    public Agendamento salvar(Long usuarioId,
            Long negocioId,
            Long itemId,
            String data,
            String horaInicio,
            String horaFim,
            Integer duracao) {

        Usuario u = usuarioRepo.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Negocio n = negocioService.buscarPorId(negocioId);

        Catalogo item = catalogoService.buscarPorId(itemId);

        Agendamento a = new Agendamento();
        a.setCliente(u);
        a.setNegocio(n);
        a.setItem(item);

        a.setData(LocalDate.parse(data));
        a.setHoraInicio(LocalTime.parse(horaInicio));
        a.setHoraFim(LocalTime.parse(horaFim));
        a.setDuracaoMinutos(duracao);

        return repo.save(a);
    }
}
