package com.pi.comuniShop.service;

import com.pi.comuniShop.model.Negocio;
import com.pi.comuniShop.model.Usuario;
import com.pi.comuniShop.repository.NegocioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

@Service
public class NegocioService {

    @Autowired
    private NegocioRepository negocioRepository;

    @Autowired
    private GeoService geoService;

    public Negocio salvar(Negocio negocio) {

        if (negocio.getDono() == null) {
            throw new IllegalArgumentException("O negócio precisa ter um dono vinculado.");
        }

        if (negocio.getCep() != null && !negocio.getCep().isEmpty()) {

            Map<String, Double> coords = geoService.buscarLatLongPorCep(negocio.getCep());

            if (coords != null) {
                negocio.setLatitude(coords.get("lat"));
                negocio.setLongitude(coords.get("lon"));
            }
        }

        return negocioRepository.save(negocio);
    }

    // ==============================
    // UPLOAD DE IMAGEM
    // ==============================
    public String salvarImagem(MultipartFile file, Long negocioId, String tipo) {
        if (file.isEmpty())
            return null;

        try {
            // Caminho real quando rodando via Spring Boot (importante!)
            Path pasta = Path.of("src/main/resources/static/uploads/negocio");

            if (!Files.exists(pasta)) {
                Files.createDirectories(pasta);
            }

            String nomeArquivo = tipo + "_" + negocioId + "_" + file.getOriginalFilename();

            Path caminhoArquivo = pasta.resolve(nomeArquivo);

            Files.write(caminhoArquivo, file.getBytes());

            System.out.println("Arquivo recebido: " + file.getOriginalFilename());
            System.out.println("Salvo em: " + caminhoArquivo.toAbsolutePath());
            // URL que será acessada no navegador
            return "/uploads/negocio/" + nomeArquivo;

        } catch (Exception e) {
            System.out.println("❌ ERRO AO SALVAR IMAGEM:");
            e.printStackTrace();
            return null;
        }
    }

    public List<Negocio> listarTodos() {
        return negocioRepository.findAll();
    }

    public Negocio buscarPorId(Long id) {
        return negocioRepository.findById(id).orElse(null);
    }

    public Negocio buscarPorDono(Usuario dono) {
        return negocioRepository.findByDono(dono);
    }

    public void excluir(Long id) {
        if (negocioRepository.existsById(id)) {
            negocioRepository.deleteById(id);
        }
    }

    public List<Negocio> listarPorDono(Long donoId) {
        return negocioRepository.findByDonoId(donoId);
    }
}
