package com.pi.comuniShop.service;

import org.springframework.stereotype.Service;
import com.pi.comuniShop.model.Usuario;
import com.pi.comuniShop.repository.UsuarioRepository;
import java.util.List;

@Service
public class AdminService {
    private final UsuarioRepository usuarioRepo;

    public AdminService(UsuarioRepository usuarioRepo) {
        this.usuarioRepo = usuarioRepo;
    }

    public List<Usuario> listarUsuarios() {
        return usuarioRepo.findAll();
    }
}
