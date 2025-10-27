package com.pi.comuniShop.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.pi.comuniShop.model.Usuario;
import com.pi.comuniShop.model.Negocio;
import com.pi.comuniShop.repository.UsuarioRepository;
import com.pi.comuniShop.repository.NegocioRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private NegocioRepository negocioRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication)
            throws IOException, ServletException {

        User user = (User) authentication.getPrincipal();
        Usuario usuario = usuarioRepository.findByEmail(user.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        switch (usuario.getTipo()) {
            case ADMIN -> response.sendRedirect("/admin/painel");
            case ESTOQUISTA -> response.sendRedirect("/estoquista/painel");

            case CLIENTE -> {
                Negocio negocio = negocioRepository.findByDono(usuario);
                if (negocio != null) {
                    response.sendRedirect("/negocios/" + negocio.getId());
                } else {
                    response.sendRedirect("/cliente/home");
                }
            }

            default -> response.sendRedirect("/usuario/home");
        }
    }

}
