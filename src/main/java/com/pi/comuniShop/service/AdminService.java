package com.pi.comuniShop.service;

import org.springframework.stereotype.Service;
import com.pi.comuniShop.model.Cliente;
import com.pi.comuniShop.model.Usuario;
import com.pi.comuniShop.repository.ClienteRepository;
import com.pi.comuniShop.repository.UsuarioRepository;
import java.util.List;

@Service
public class AdminService {
    private final ClienteRepository clienteRepo;
    private final UsuarioRepository usuarioRepo;

    public AdminService(ClienteRepository clienteRepo, UsuarioRepository usuarioRepo) {
        this.clienteRepo = clienteRepo;
        this.usuarioRepo = usuarioRepo;
    }

    public List<Cliente> listarClientes() {
        return clienteRepo.findAll();
    }

    public List<Usuario> listarUsuarios() {
        return usuarioRepo.findAll();
    }
}
