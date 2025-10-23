package com.pi.comuniShop.service;

import com.pi.comuniShop.model.Negocio;
import com.pi.comuniShop.repository.NegocioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class NegocioService {

    @Autowired
    private NegocioRepository negocioRepository;

    // 🔹 Salvar ou atualizar um negócio
    public Negocio salvar(Negocio negocio) {
        if (negocio.getDono() == null) {
            throw new IllegalArgumentException("O negócio precisa ter um dono vinculado.");
        }
        return negocioRepository.save(negocio);
    }

    // 🔹 Listar todos
    public List<Negocio> listarTodos() {
        return negocioRepository.findAll();
    }

    // 🔹 Buscar por ID
    public Negocio buscarPorId(Long id) {
        return negocioRepository.findById(id).orElse(null);
    }

    // 🔹 Excluir
    public void excluir(Long id) {
        if (negocioRepository.existsById(id)) {
            negocioRepository.deleteById(id);
        }
    }

    // 🔹 Buscar negócios por dono (caso queira exibir só os do usuário logado)
    public List<Negocio> listarPorDono(Long donoId) {
        return negocioRepository.findByDonoId(donoId);
    }
}
