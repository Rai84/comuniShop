package com.pi.comuniShop.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.pi.comuniShop.dto.AgendamentoDTO;
import com.pi.comuniShop.model.Agendamento;
import com.pi.comuniShop.model.Usuario;
import com.pi.comuniShop.repository.UsuarioRepository;
import com.pi.comuniShop.repository.AgendamentoRepository;
import com.pi.comuniShop.service.AgendamentoService;

@RestController
@RequestMapping("/agendamentos")
public class AgendamentoController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AgendamentoRepository agendamentoRepository;

    @Autowired
    private AgendamentoService service;

    // ==================================
    // SALVAR AGENDAMENTO
    // ==================================
    @PostMapping("/salvar")
    public ResponseEntity<?> salvar(@RequestBody JsonNode body,
            @AuthenticationPrincipal User user) {

        Usuario cliente = usuarioRepository.findByEmail(user.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Long negocioId = body.get("lojaId").asLong();
        Long itemId = body.get("itemId").asLong();
        String data = body.get("data").asText();
        String horaInicio = body.get("horaInicio").asText();
        String horaFim = body.get("horaFim").asText();
        Integer duracao = body.get("duracao").asInt();

        service.salvar(
                cliente.getId(),
                negocioId,
                itemId,
                data,
                horaInicio,
                horaFim,
                duracao);

        return ResponseEntity.ok(Map.of(
                "status", "ok",
                "mensagem", "Agendamento criado com sucesso!"));
    }

    // ==================================
    // LISTAR AGENDAMENTOS DO USUÁRIO LOGADO
    // ==================================
    @GetMapping("/meus")
    public List<AgendamentoDTO> meusAgendamentos(
            @AuthenticationPrincipal User user) {

        Usuario usuario = usuarioRepository.findByEmail(user.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        return agendamentoRepository.findByClienteId(usuario.getId())
        .stream()
        .map(a -> new AgendamentoDTO(
                a.getId(),
                a.getData().toString(),
                a.getHoraInicio().toString(),
                a.getHoraFim().toString(),
                a.getNegocio() != null ? a.getNegocio().getNome() : "Loja removida",
                a.getItem() != null ? a.getItem().getNome() : "Item removido",
                a.getDuracaoMinutos()
        ))
        .toList();
}
}
