package com.pi.comuniShop.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.pi.comuniShop.repository.UsuarioRepository;
import com.pi.comuniShop.model.Usuario;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Set;
import java.util.Optional;
import org.springframework.security.core.userdetails.User;

@Component
public class CustomSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        User user = (User) authentication.getPrincipal();

        Optional<Usuario> optUsuario = usuarioRepository.findByEmail(user.getUsername());
        if (optUsuario.isEmpty()) {
            response.sendRedirect("/login?error=true");
            return;
        }

        Usuario usuario = optUsuario.get();

        switch (usuario.getTipo()) {
            case USUARIO -> response.sendRedirect("/usuario/home");
            case ADMIN -> response.sendRedirect("/admin/painel");
            case ESTOQUISTA -> response.sendRedirect("/estoquista/painel");
            case CLIENTE -> response.sendRedirect("/cliente/home");
            default -> response.sendRedirect("/index");
        
        }
    }
}
