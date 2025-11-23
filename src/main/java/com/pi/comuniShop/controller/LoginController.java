package com.pi.comuniShop.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.pi.comuniShop.model.Catalogo;
import com.pi.comuniShop.model.Negocio;
import com.pi.comuniShop.model.Usuario;
import com.pi.comuniShop.repository.UsuarioRepository;

import com.pi.comuniShop.service.CatalogoService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class LoginController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CatalogoService catalogoService;

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @GetMapping({ "/", "/login" })
    public String index(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "index";
    }

    @GetMapping("/usuario/home")
    public String home(@AuthenticationPrincipal User user, Model model) {
        
        // busca o usuário logado
        Usuario usuario = usuarioRepository.findByEmail(user.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        
        log.info("🟢 Entrou no método /usuario/home para o usuário: {}", usuario.getEmail());
        
        // carrega todos itens do catálogo
        List<Catalogo> itens = catalogoService.listarTodos();
        log.info("📦 Itens carregados: {}", itens.size());
        
        // AGRUPA itens por negócio
        Map<Negocio, List<Catalogo>> catalogosPorNegocio = itens.stream()
                .collect(Collectors.groupingBy(Catalogo::getNegocio));
        
        // envia para a view
        model.addAttribute("usuario", usuario);
        model.addAttribute("itens", itens);                     // lista normal
        model.addAttribute("catalogosPorNegocio", catalogosPorNegocio); // agrupado
        
        return "usuario/home";
    }


}
