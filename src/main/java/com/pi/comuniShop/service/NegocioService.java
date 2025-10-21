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

    public List<Negocio> listarTodos() {
        return negocioRepository.findAll();
    }

    public Negocio buscarPorId(Long id) {
        return negocioRepository.findById(id).orElse(null);
    }

    public Negocio salvar(Negocio negocio) {
        return negocioRepository.save(negocio);
    }

    public void excluir(Long id) {
        negocioRepository.deleteById(id);
    }
}
