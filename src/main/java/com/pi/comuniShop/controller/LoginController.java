package com.pi.comuniShop.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.pi.comuniShop.model.Catalogo;
import com.pi.comuniShop.model.ImagemCatalogo;
import com.pi.comuniShop.model.Negocio;
import com.pi.comuniShop.model.Usuario;
import com.pi.comuniShop.repository.UsuarioRepository;
import com.pi.comuniShop.repository.ImagemCatalogoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.pi.comuniShop.service.CatalogoService;

@Controller
public class LoginController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CatalogoService catalogoService;

    @Autowired
    private ImagemCatalogoRepository ImagemCatalogoRepository;

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);


    @GetMapping({ "/", "/login" })
    public String index(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "index";
    }

    @GetMapping("/usuario/home")
    public String home(@AuthenticationPrincipal User user, Model model) throws Exception {

        Usuario usuario = usuarioRepository.findByEmail(user.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        List<Catalogo> itens = catalogoService.listarTodos();

        // AGRUPA por negócio
        Map<Negocio, List<Catalogo>> mapa = itens.stream()
                .collect(Collectors.groupingBy(Catalogo::getNegocio));

        // ------------ CONVERTE PARA JSON LIMPO (USADO NO MODAL ÚNICO) -------------
        List<Map<String, Object>> lojasList = new ArrayList<>();

        for (Negocio n : mapa.keySet()) {
            Map<String, Object> loja = new LinkedHashMap<>();
            loja.put("id", n.getId());
            loja.put("nome", n.getNome());
            loja.put("horaAbertura", n.getHoraAbertura().toString());
            loja.put("horaFechamento", n.getHoraFechamento().toString());
            loja.put("agendamentoAtivo", n.isAgendamentoAtivo());

            List<Map<String, Object>> itensList = new ArrayList<>();
            for (Catalogo c : mapa.get(n)) {
                Map<String, Object> item = new LinkedHashMap<>();
                item.put("id", c.getId());
                item.put("nome", c.getNome());
                item.put("descricao", c.getDescricao());
                item.put("preco", c.getPreco());
                item.put("disponivel", c.isDisponivel());
                item.put("duracao", c.getDuracaoMinutos());
                item.put("imagens",
                        c.getImagens().stream().map(ImagemCatalogo::getUrl).toList());
                itensList.add(item);
            }

            loja.put("itens", itensList);
            lojasList.add(loja);
        }

        String lojasJson = new ObjectMapper().writeValueAsString(lojasList);

        // ----------------- ENVIA TUDO PARA A VIEW (HTML) -----------------------

        model.addAttribute("usuario", usuario);

        // O HTML AINDA USA ESSAS DUAS VARIÁVEIS:
        model.addAttribute("itens", itens);
        model.addAttribute("catalogosPorNegocio", mapa);

        // JSON usado no modal novo
        model.addAttribute("lojasJson", lojasJson);

        return "usuario/home";
    }


}
