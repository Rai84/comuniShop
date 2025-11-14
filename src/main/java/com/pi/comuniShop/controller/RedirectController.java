package com.pi.comuniShop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.pi.comuniShop.model.Negocio;
import com.pi.comuniShop.model.Usuario;
import com.pi.comuniShop.repository.UsuarioRepository;
import com.pi.comuniShop.repository.NegocioRepository;

@Controller
public class RedirectController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private NegocioRepository negocioRepository;

    @GetMapping("/redirect")
    public String redirect(@AuthenticationPrincipal User user, Model model) {
        if (user == null) {
            return "redirect:/login";
        }

        Usuario usuario = usuarioRepository.findByEmail(user.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        model.addAttribute("usuario", usuario);

        switch (usuario.getTipo()) {
            case ADMIN:
                return "redirect:/admin/painel";
            case CLIENTE:
                Negocio negocio = negocioRepository.findByDono(usuario);
                if (negocio != null) {
                    return "redirect:/negocios/" + negocio.getId();
                }
                return "redirect:/cliente/home";

            default:
                return "redirect:/usuario/home";
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
