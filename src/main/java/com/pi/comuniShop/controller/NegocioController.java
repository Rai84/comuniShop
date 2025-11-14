package com.pi.comuniShop.controller;

import com.pi.comuniShop.model.Catalogo;
import com.pi.comuniShop.model.Negocio;
import com.pi.comuniShop.model.Usuario;
import com.pi.comuniShop.model.TipoUsuario;
import com.pi.comuniShop.service.CatalogoService;
import com.pi.comuniShop.service.NegocioService;
import com.pi.comuniShop.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/negocios")
public class NegocioController {

    private static final Logger logger = LoggerFactory.getLogger(NegocioController.class);

    @Autowired
    private NegocioService negocioService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private CatalogoService catalogoService;

    // ✅ Criar Negócio + Dono (modal)
    @PostMapping("/salvar")
    public String salvarNegocioComDono(
            @RequestParam String nome,
            @RequestParam String cpf,
            @RequestParam String telefone,
            @RequestParam String email,
            @RequestParam String senha,
            @RequestParam String nomeNegocio,
            @RequestParam(required = false) String cnpj,

            // ENDEREÇO DO NEGÓCIO
            @RequestParam String cep,
            @RequestParam String estado,
            @RequestParam String cidade,
            @RequestParam String bairro,
            @RequestParam String logradouro,
            @RequestParam String numero,
            @RequestParam(required = false) String complemento) {

        // 1️⃣ CRIA O DONO
        Usuario dono = new Usuario();
        dono.setNome(nome);
        dono.setCpf(cpf);
        dono.setTelefone(telefone);
        dono.setEmail(email);
        dono.setSenha(senha);
        dono.setTipo(TipoUsuario.CLIENTE);

        usuarioService.salvar(dono);

        // 2️⃣ CRIA O NEGOCIO
        Negocio negocio = new Negocio();
        negocio.setNome(nomeNegocio);
        negocio.setCnpj(cnpj);
        negocio.setDono(dono);

        negocio.setEmailComercial(email);
        negocio.setTelefoneComercial(telefone);

        // ⭐ AQUI ESTAVA FALTANDO → ENDEREÇO
        negocio.setCep(cep);
        negocio.setEstado(estado);
        negocio.setCidade(cidade);
        negocio.setBairro(bairro);
        negocio.setLogradouro(logradouro);
        negocio.setNumero(numero);
        negocio.setComplemento(complemento);

        negocioService.salvar(negocio);

        return "redirect:/negocios/" + negocio.getId();
    }

    // ✅ Página de detalhes do negócio (painel do dono)
    @GetMapping("/{id}")
    public String detalhes(@PathVariable Long id,
            Authentication authentication,
            Model model) {

        Negocio negocio = negocioService.buscarPorId(id);
        if (negocio == null)
            return "redirect:/negocios";

        String emailLogado = authentication.getName();
        if (!emailLogado.equals(negocio.getDono().getEmail())) {
            logger.warn("🚫 Tentativa de acesso bloqueada! Dono={}, Logado={}",
                    negocio.getDono().getEmail(), emailLogado);
            return "redirect:/negocios";
        }

        List<Catalogo> itens = catalogoService.listarPorNegocio(negocio.getId());

        model.addAttribute("negocio", negocio);
        model.addAttribute("itens", itens);

        System.out.println("Produtos do negócio " + negocio.getId() + ": " + itens.size());
        itens.forEach(item -> System.out.println(" - " + item.getNome()));

        return "negocio/negocio-detalhes"; // painel
    }

    // ✅ Formulário de edição
    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        Negocio negocio = negocioService.buscarPorId(id);

        if (negocio == null)
            return "redirect:/negocios";

        model.addAttribute("negocio", negocio);
        return "negocio/negocio-form";
    }

    @PostMapping("/{id}/atualizar")
    public String atualizar(@PathVariable Long id,
            @ModelAttribute Negocio dados) {

        Negocio negocio = negocioService.buscarPorId(id);
        if (negocio == null)
            return "redirect:/negocios";

        negocio.setNome(dados.getNome());
        negocio.setDescricao(dados.getDescricao());
        negocio.setEmailComercial(dados.getEmailComercial());
        negocio.setTelefoneComercial(dados.getTelefoneComercial());
        negocio.setHoraAbertura(dados.getHoraAbertura());
        negocio.setHoraFechamento(dados.getHoraFechamento());
        negocio.setCatalogoAtivo(dados.isCatalogoAtivo());
        negocio.setAgendamentoAtivo(dados.isAgendamentoAtivo());
        negocio.setEntregasAtivas(dados.isEntregasAtivas());

        // ENDEREÇO ATUALIZA AQUI 👇
        negocio.setCep(dados.getCep());
        negocio.setEstado(dados.getEstado());
        negocio.setCidade(dados.getCidade());
        negocio.setBairro(dados.getBairro());
        negocio.setLogradouro(dados.getLogradouro());
        negocio.setNumero(dados.getNumero());
        negocio.setComplemento(dados.getComplemento());

        negocioService.salvar(negocio);

        return "redirect:/negocios/" + id;
    }

    // ✅ Listar todos os negócios (somente para admin)
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("negocios", negocioService.listarTodos());
        return "negocio/negocio-list";
    }

    // ✅ Excluir negocio
    @GetMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id) {
        negocioService.excluir(id);
        return "redirect:/negocios";
    }
}
