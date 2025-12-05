package com.pi.comuniShop.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pi.comuniShop.model.Pedido;
import com.pi.comuniShop.repository.NegocioRepository;
import com.pi.comuniShop.repository.PedidoRepository;
import com.pi.comuniShop.repository.UsuarioRepository;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private NegocioRepository negocioRepository;

    @GetMapping("/meus")
    public ResponseEntity<?> meusPedidos(@AuthenticationPrincipal User user) {

        // usuário não está logado
        if (user == null) {
            return ResponseEntity.ok(List.of());
        }

        Long clienteId = usuarioRepository.findByEmail(user.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"))
                .getId();

        List<Pedido> pedidos = pedidoRepository.findByClienteIdOrderByCriadoEmDesc(clienteId);

        // montar resposta amigável para o frontend
        List<Map<String, Object>> resposta = pedidos.stream().map(p -> Map.of(
                "id", p.getId(),
                "status", p.getStatus() == null ? "pendente" : p.getStatus(),
                "total", p.getTotal(),
                "criadoEm", p.getCriadoEm().toString(),
                "loja",
                negocioRepository.findById(p.getNegocioId())
                        .map(n -> n.getNome())
                        .orElse("Loja não encontrada"),

                "itens", p.getItens() // lista já contém: nome, quantidade, precoUnitario, totalItem
        )).toList();

        return ResponseEntity.ok(resposta);
    }
}
