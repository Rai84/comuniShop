package com.pi.comuniShop.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;

import com.pi.comuniShop.dto.CarrinhoItem;
import com.pi.comuniShop.model.Catalogo;
import com.pi.comuniShop.model.ItemPedido;
import com.pi.comuniShop.model.Pedido;
import com.pi.comuniShop.repository.CatalogoRepository;
import com.pi.comuniShop.repository.PedidoRepository;
import com.pi.comuniShop.repository.UsuarioRepository;

@RestController
@RequestMapping("/carrinho")
public class CarrinhoController {

    @Autowired
    private CatalogoRepository catalogoRepo;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    private List<CarrinhoItem> getCarrinho(HttpSession session) {
        List<CarrinhoItem> carrinho = (List<CarrinhoItem>) session.getAttribute("carrinho");

        if (carrinho == null) {
            carrinho = new ArrayList<>();
            session.setAttribute("carrinho", carrinho);
        }

        return carrinho;
    }

    // ===================== ADICIONAR =====================

    @PostMapping("/adicionar")
    public Map<String, Object> adicionar(@RequestBody Map<String, Long> body, HttpSession session) {
        Long itemId = body.get("itemId");
        Catalogo item = catalogoRepo.findById(itemId).orElse(null);

        if (item == null) {
            return Map.of("erro", "Item não encontrado");
        }

        List<CarrinhoItem> carrinho = getCarrinho(session);

        // Se já existe, aumenta a quantidade
        for (CarrinhoItem c : carrinho) {
            if (c.getId().equals(itemId)) {
                c.setQuantidade(c.getQuantidade() + 1);
                return Map.of("status", "ok", "msg", "Quantidade aumentada");
            }
        }

        // Senão adiciona novo item
        carrinho.add(new CarrinhoItem(
                item.getId(),
                item.getNome(),
                item.getPreco(),
                1,
                item.getImagens().isEmpty() ? "/img/no-image.png" : item.getImagens().get(0).getUrl()));

        return Map.of("status", "ok", "msg", "Item adicionado");
    }

    // ===================== LISTAR =====================

    @GetMapping("/listar")
    public List<CarrinhoItem> listar(HttpSession session) {
        return getCarrinho(session);
    }

    // ===================== REMOVER =====================

    @PostMapping("/remover")
    public Map<String, Object> remover(@RequestBody Map<String, Long> body, HttpSession session) {
        Long itemId = body.get("itemId");

        List<CarrinhoItem> carrinho = getCarrinho(session);

        carrinho.removeIf(c -> c.getId().equals(itemId));

        return Map.of("status", "ok");
    }

    // ===================== LIMPAR =====================

    @PostMapping("/limpar")
    public Map<String, Object> limpar(HttpSession session) {
        session.removeAttribute("carrinho");
        return Map.of("status", "ok");
    }

    // ===================== ALTERAR QUANTIDADE (+ / -) =====================

    @PostMapping("/alterar-quantidade")
    public ResponseEntity<?> alterarQuantidade(HttpSession session,
            @RequestBody Map<String, Object> body) {

        Long itemId = Long.valueOf(body.get("itemId").toString());
        int delta = Integer.parseInt(body.get("delta").toString()); // +1 ou -1

        List<CarrinhoItem> carrinho = getCarrinho(session);

        carrinho.removeIf(c -> {
            if (c.getId().equals(itemId)) {
                int novaQtd = c.getQuantidade() + delta;

                if (novaQtd <= 0) {
                    return true; // remove item
                }

                c.setQuantidade(novaQtd);
            }
            return false;
        });

        session.setAttribute("carrinho", carrinho);
        return ResponseEntity.ok(carrinho);
    }

    @PostMapping("/finalizar")
    public ResponseEntity<?> finalizarPedido(HttpSession session,
            @AuthenticationPrincipal User user) {

        List<CarrinhoItem> carrinho = getCarrinho(session);
        if (carrinho == null || carrinho.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("erro", "Carrinho vazio"));
        }

        BigDecimal total = carrinho.stream()
                .map(i -> i.getPreco().multiply(BigDecimal.valueOf(i.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<ItemPedido> itensPedido = carrinho.stream().map(i -> {
            ItemPedido ip = new ItemPedido();
            ip.setItemId(i.getId());
            ip.setNome(i.getNome());
            ip.setQuantidade(i.getQuantidade());
            ip.setPrecoUnitario(i.getPreco());
            ip.setTotalItem(i.getPreco().multiply(BigDecimal.valueOf(i.getQuantidade())));
            return ip;
        }).toList();

        Pedido pedido = new Pedido();

        // ------------------------------
        // PEGAR A LOJA DO PRIMEIRO ITEM
        // ------------------------------
        Catalogo cat = catalogoRepo.findById(carrinho.get(0).getId()).get();
        pedido.setNegocioId(cat.getNegocio().getId());

        pedido.setClienteId(usuarioRepository.findByEmail(user.getUsername()).get().getId());
        pedido.setItens(itensPedido);
        pedido.setTotal(total);

        pedidoRepository.save(pedido);

        session.removeAttribute("carrinho");

        return ResponseEntity.ok(Map.of(
                "status", "ok",
                "msg", "Pedido realizado com sucesso!",
                "pedidoId", pedido.getId()));
    }

}
