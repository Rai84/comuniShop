package com.pi.comuniShop.service;

import com.pi.comuniShop.model.ImagemCatalogo;
import com.pi.comuniShop.repository.ImagemCatalogoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.Comparator;
import java.util.List;

@Service
public class ImagemCatalogoService {

    private static final String UPLOAD_DIR = "src/main/resources/static/uploads/catalogo/";

    @Autowired
    private ImagemCatalogoRepository imagemCatalogoRepository;

    public List<ImagemCatalogo> listarPorCatalogo(Long catalogoId) {
        return imagemCatalogoRepository.findByCatalogoId(catalogoId);
    }

    public ImagemCatalogo salvar(ImagemCatalogo imagem) {
        return imagemCatalogoRepository.saveAndFlush(imagem);
    }

    public ImagemCatalogo buscarPorId(Long id) {
        return imagemCatalogoRepository.findById(id).orElse(null);
    }

    public void excluir(Long id) {
        imagemCatalogoRepository.deleteById(id);
    }

    public void salvarArquivos(MultipartFile[] arquivos, Long catalogoId) throws IOException {
        if (arquivos == null || arquivos.length == 0)
            return;

        Files.createDirectories(Paths.get(UPLOAD_DIR));

        // Conta quantas imagens já existem para definir a próxima ordem
        int ultimaOrdem = imagemCatalogoRepository.countByCatalogoId(catalogoId);

        for (MultipartFile file : arquivos) {
            if (file.isEmpty())
                continue;

            String nomeArquivo = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path destino = Paths.get(UPLOAD_DIR + nomeArquivo);
            Files.copy(file.getInputStream(), destino, StandardCopyOption.REPLACE_EXISTING);

            ImagemCatalogo img = new ImagemCatalogo();
            img.setCatalogo(new com.pi.comuniShop.model.Catalogo());
            img.getCatalogo().setId(catalogoId);
            img.setUrl("/uploads/catalogo/" + nomeArquivo);
            img.setPrincipal(false);

            // 🔢 Define a ordem incremental
            ultimaOrdem++;
            img.setOrdem(ultimaOrdem);

            imagemCatalogoRepository.saveAndFlush(img);
        }
    }

    @Transactional
    public void definirComoPrincipal(Long imagemId, Long catalogoId) {
        List<ImagemCatalogo> imagens = imagemCatalogoRepository.findByCatalogoId(catalogoId);

        for (ImagemCatalogo img : imagens) {
            img.setPrincipal(img.getId().equals(imagemId)); // só essa fica true
            imagemCatalogoRepository.save(img);
        }
    }

    @Transactional
    public void moverImagem(Long id, String direcao) {
        ImagemCatalogo atual = imagemCatalogoRepository.findById(id).orElse(null);
        if (atual == null)
            return;

        List<ImagemCatalogo> imagens = imagemCatalogoRepository
                .findByCatalogoId(atual.getCatalogo().getId())
                .stream()
                .sorted(Comparator.comparing(ImagemCatalogo::getOrdem))
                .toList();

        int idxAtual = imagens.indexOf(atual);
        if (idxAtual == -1)
            return;

        if (direcao.equals("up") && idxAtual > 0) {
            trocarOrdem(atual, imagens.get(idxAtual - 1));
        } else if (direcao.equals("down") && idxAtual < imagens.size() - 1) {
            trocarOrdem(atual, imagens.get(idxAtual + 1));
        }
    }

    private void trocarOrdem(ImagemCatalogo a, ImagemCatalogo b) {
        int ordemA = a.getOrdem();
        a.setOrdem(b.getOrdem());
        b.setOrdem(ordemA);
        imagemCatalogoRepository.save(a);
        imagemCatalogoRepository.save(b);
    }

}
