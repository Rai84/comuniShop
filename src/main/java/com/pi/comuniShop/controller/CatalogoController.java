package com.pi.comuniShop.controller;

import com.pi.comuniShop.model.Catalogo;
import com.pi.comuniShop.model.ImagemCatalogo;
import com.pi.comuniShop.service.CatalogoService;
import com.pi.comuniShop.service.ImagemCatalogoService;
import com.pi.comuniShop.service.NegocioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/catalogo")
public class CatalogoController {

    @Autowired
    private CatalogoService catalogoService;

    @Autowired
    private NegocioService negocioService;

    @Autowired
    private ImagemCatalogoService imagemCatalogoService;

    // ==================== LISTAR ====================
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("itens", catalogoService.listarTodos());
        return "catalogo/catalogo-list";
    }

    // ==================== NOVO ====================
    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("item", new Catalogo());
        model.addAttribute("negocios", negocioService.listarTodos());
        return "catalogo/catalogo-form";
    }

    // ==================== SALVAR ====================
    @PostMapping("/salvar")
    public String salvar(
            @ModelAttribute("item") Catalogo item,
            @RequestParam(value = "files", required = false) MultipartFile[] files) throws IOException {

        Catalogo salvo;

        if (item.getId() != null) {
            Catalogo existente = catalogoService.buscarPorId(item.getId());
            if (existente == null)
                return "redirect:/catalogo";

            // ✅ Mantém o negócio existente caso o campo venha vazio no form
            if (item.getNegocio() == null || item.getNegocio().getId() == null) {
                item.setNegocio(existente.getNegocio());
            }

            existente.setNegocio(item.getNegocio());
            existente.setNome(item.getNome());
            existente.setDescricao(item.getDescricao());
            existente.setPreco(item.getPreco());
            existente.setDisponivel(item.isDisponivel());
            existente.setAtualizadoEm(java.time.LocalDateTime.now());

            salvo = catalogoService.salvarSomenteCatalogo(existente);
        } else {
            // ✅ Garante que o negócio não está nulo no cadastro novo
            if (item.getNegocio() == null || item.getNegocio().getId() == null) {
                return "redirect:/catalogo/novo?erro=negocio";
            }
            salvo = catalogoService.salvarSomenteCatalogo(item);
        }

        // Salvar imagens enviadas
        if (files != null && files.length > 0) {
            imagemCatalogoService.salvarArquivos(files, salvo.getId());
        }

        return "redirect:/catalogo";
    }

    // ==================== EDITAR ====================
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Catalogo item = catalogoService.buscarPorId(id);
        if (item == null)
            return "redirect:/catalogo";

        model.addAttribute("item", item);
        model.addAttribute("negocios", negocioService.listarTodos());

        // 👇 Aqui entra a ordenação por ordem
        model.addAttribute("imagens", imagemCatalogoService
                .listarPorCatalogo(id)
                .stream()
                .sorted(Comparator.comparing(ImagemCatalogo::getOrdem))
                .toList());

        return "catalogo/catalogo-form";
    }

    // ==================== DEFINIR COMO PRINCIPAL ====================
    @GetMapping("/imagem/principal/{id}")
    public String definirComoPrincipal(@PathVariable Long id) {
        ImagemCatalogo img = imagemCatalogoService.buscarPorId(id);
        if (img != null) {
            Long catalogoId = img.getCatalogo().getId();
            imagemCatalogoService.definirComoPrincipal(id, catalogoId);
            return "redirect:/catalogo/editar/" + catalogoId;
        }
        return "redirect:/catalogo";
    }

    // ==================== EXCLUIR ITEM ====================
    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id) {
        catalogoService.excluir(id);
        return "redirect:/catalogo";
    }

    // ==================== EXCLUIR IMAGEM ====================
    @GetMapping("/imagem/excluir/{id}")
    public String excluirImagem(@PathVariable Long id) {
        ImagemCatalogo img = imagemCatalogoService.buscarPorId(id);
        if (img != null) {
            Long catalogoId = img.getCatalogo().getId();
            imagemCatalogoService.excluir(id);
            return "redirect:/catalogo/editar/" + catalogoId;
        }
        return "redirect:/catalogo";
    }

    // ==================== DETALHES ====================
    @GetMapping("/{id}")
    public String detalhes(@PathVariable Long id, Model model) {
        Catalogo item = catalogoService.buscarPorId(id);
        if (item == null)
            return "redirect:/catalogo";

        // 🔢 Ordena as imagens antes de enviar pro template
        List<ImagemCatalogo> imagens = imagemCatalogoService.listarPorCatalogo(id)
                .stream()
                .sorted(Comparator.comparing(ImagemCatalogo::getOrdem))
                .toList();

        model.addAttribute("item", item);
        model.addAttribute("imagens", imagens);

        return "catalogo/catalogo-detalhes";
    }

    @GetMapping("/imagem/mover/{id}/up")
    public String moverImagemCima(@PathVariable Long id) {
        ImagemCatalogo img = imagemCatalogoService.buscarPorId(id);
        if (img != null) {
            imagemCatalogoService.moverImagem(id, "up");
            return "redirect:/catalogo/editar/" + img.getCatalogo().getId();
        }
        return "redirect:/catalogo";
    }

    @GetMapping("/imagem/mover/{id}/down")
    public String moverImagemBaixo(@PathVariable Long id) {
        ImagemCatalogo img = imagemCatalogoService.buscarPorId(id);
        if (img != null) {
            imagemCatalogoService.moverImagem(id, "down");
            return "redirect:/catalogo/editar/" + img.getCatalogo().getId();
        }
        return "redirect:/catalogo";
    }

}
