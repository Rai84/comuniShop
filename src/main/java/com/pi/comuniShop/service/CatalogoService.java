package com.pi.comuniShop.service;

import com.pi.comuniShop.model.Catalogo;
import com.pi.comuniShop.repository.CatalogoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class CatalogoService {

    @Autowired
    private CatalogoRepository catalogoRepository;

    public List<Catalogo> listarTodos() {
        return catalogoRepository.findAll();
    }

    public Catalogo buscarPorId(Long id) {
        return catalogoRepository.findById(id).orElse(null);
    }

    public Catalogo salvar(Catalogo catalogo) {
        return catalogoRepository.save(catalogo);
    }

    public void excluir(Long id) {
        catalogoRepository.deleteById(id);
    }

    @Transactional
    public Catalogo salvarSomenteCatalogo(Catalogo catalogo) {
        return catalogoRepository.save(catalogo);
    }
}
