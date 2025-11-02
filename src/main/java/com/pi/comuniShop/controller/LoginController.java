package com.pi.comuniShop.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
        // busca o usuário logado pelo e-mail (username)
        Usuario usuario = usuarioRepository.findByEmail(user.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Log de depuração
        log.info("🟢 Entrou no método /usuario/home para o usuário: {}", usuario.getEmail());

        // carrega os produtos do catálogo
        var produtos = catalogoService.listarTodos();
        log.info("📦 Produtos carregados: {}", produtos.size());

        model.addAttribute("usuario", usuario);
        model.addAttribute("produtos", produtos);
        return "usuario/home";
    }
}
