package com.pi.comuniShop.service;

import com.pi.comuniShop.model.Negocio;
import com.pi.comuniShop.model.Usuario;
import com.pi.comuniShop.repository.NegocioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class NegocioService {

    @Autowired
    private NegocioRepository negocioRepository;

    @Autowired
    private GeoService geoService;

    // 🔹 Salvar ou atualizar um negócio
    public Negocio salvar(Negocio negocio) {

        if (negocio.getDono() == null) {
            throw new IllegalArgumentException("O negócio precisa ter um dono vinculado.");
        }

        // Se tiver CEP → buscar lat/long
        if (negocio.getCep() != null && !negocio.getCep().isEmpty()) {

            Map<String, Double> coords = geoService.buscarLatLongPorCep(negocio.getCep());

            if (coords != null) {
                negocio.setLatitude(coords.get("lat"));
                negocio.setLongitude(coords.get("lon"));
            }
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

    // 🔹 Buscar por dono
    public Negocio buscarPorDono(Usuario dono) {
        return negocioRepository.findByDono(dono);
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
