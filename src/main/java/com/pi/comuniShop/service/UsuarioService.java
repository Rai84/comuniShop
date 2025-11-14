package com.pi.comuniShop.service;

import com.pi.comuniShop.model.Usuario;
import com.pi.comuniShop.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private GeoService geoService;

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    public Usuario salvar(Usuario usuario) {

        // Evita duplicidade de e-mail
        if (usuario.getId() == null && usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new RuntimeException("E-mail já cadastrado.");
        }

        // Criptografa senha se for novo usuário
        if (usuario.getId() == null) {
            usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        }

        // Se o usuário tiver CEP → buscar lat/lon
        if (usuario.getCep() != null && !usuario.getCep().isEmpty()) {

            Map<String, Double> coords = geoService.buscarLatLongPorCep(usuario.getCep());

            if (coords != null) {
                usuario.setLatitude(coords.get("lat"));
                usuario.setLongitude(coords.get("lon"));
            }
        }

        return usuarioRepository.save(usuario);
    }

    public void excluir(Long id) {
        usuarioRepository.deleteById(id);
    }

}
