package com.pi.comuniShop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.pi.comuniShop.model.Usuario;
import com.pi.comuniShop.repository.UsuarioRepository;

@Controller
public class RedirectController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Após o login, o Spring redireciona para /redirect
    @GetMapping("/redirect")
    public String redirect(@AuthenticationPrincipal User user, Model model) {
        if (user == null) {
            return "redirect:/login";
        }

        Usuario usuario = usuarioRepository.findByEmail(user.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        model.addAttribute("usuario", usuario);

        // Redireciona conforme o tipo do usuário
        switch (usuario.getTipo()) {
            case ADMIN:
                return "redirect:/admin/painel";
            case ESTOQUISTA:
                return "redirect:/estoquista/painel";
            case CLIENTE:
                return "redirect:/cliente/home";
            default:
                return "redirect:/home";
        }
    }

    // Painel do ADMIN
    @GetMapping("/admin/painel")
    public String painelAdmin(@AuthenticationPrincipal User user, Model model) {
        Usuario usuario = usuarioRepository.findByEmail(user.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        model.addAttribute("usuario", usuario);
        return "admin/painel";
    }

    // Painel do FUNCIONÁRIO
    @GetMapping("/funcionario/painel")
    public String painelFuncionario(@AuthenticationPrincipal User user, Model model) {
        Usuario usuario = usuarioRepository.findByEmail(user.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        model.addAttribute("usuario", usuario);
        return "funcionario/painel";
    }

    // Página do CLIENTE
    @GetMapping("/cliente/home")
    public String homeCliente(@AuthenticationPrincipal User user, Model model) {
        Usuario usuario = usuarioRepository.findByEmail(user.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        model.addAttribute("usuario", usuario);
        return "cliente/home";
    }
}
