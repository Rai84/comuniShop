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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalTime;
import java.util.List;

@Controller
@RequestMapping("/negocios")
public class NegocioController {

    @Autowired
    private NegocioService negocioService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private CatalogoService catalogoService;

    // =============================
    // CRIAR NEGÓCIO
    // =============================
    @PostMapping("/salvar")
    public String salvarNegocioComDono(
            @RequestParam String nome,
            @RequestParam String cpf,
            @RequestParam String telefone,
            @RequestParam String email,
            @RequestParam String senha,
            @RequestParam String nomeNegocio,
            @RequestParam(required = false) String cnpj,
            @RequestParam String cep,
            @RequestParam String estado,
            @RequestParam String cidade,
            @RequestParam String bairro,
            @RequestParam String logradouro,
            @RequestParam String numero,
            @RequestParam(required = false) String complemento) {

        Usuario dono = new Usuario();
        dono.setNome(nome);
        dono.setCpf(cpf);
        dono.setTelefone(telefone);
        dono.setEmail(email);
        dono.setSenha(senha);
        dono.setTipo(TipoUsuario.CLIENTE);

        usuarioService.salvar(dono);

        Negocio negocio = new Negocio();
        negocio.setNome(nomeNegocio);
        negocio.setCnpj(cnpj);
        negocio.setDono(dono);
        negocio.setEmailComercial(email);
        negocio.setTelefoneComercial(telefone);

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

    // =============================
    // PAINEL DO NEGÓCIO
    // =============================
    @GetMapping("/{id}")
    public String detalhes(@PathVariable Long id,
            Authentication authentication,
            Model model) {

        Negocio negocio = negocioService.buscarPorId(id);
        if (negocio == null)
            return "redirect:/negocios";

        String emailLogado = authentication.getName();
        if (!emailLogado.equals(negocio.getDono().getEmail()))
            return "redirect:/negocios";

        List<Catalogo> itens = catalogoService.listarPorNegocio(negocio.getId());

        model.addAttribute("negocio", negocio);
        model.addAttribute("itens", itens);

        return "negocio/negocio-detalhes";
    }

    // =============================
    // EDITAR NEGÓCIO
    // =============================
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

    // =============================
    // UPLOAD LOGO
    // =============================
    @PostMapping("/{id}/upload-logo")
    public String uploadLogo(@PathVariable Long id,
            @RequestParam("logo") MultipartFile file) {

        Negocio negocio = negocioService.buscarPorId(id);
        if (negocio == null)
            return "redirect:/negocios";

        String url = negocioService.salvarImagem(file, id, "logo");

        if (url != null) {
            negocio.setLogoUrl(url);
            negocioService.salvar(negocio);
        }

        return "redirect:/negocios/" + id;
    }

    // =============================
    // UPLOAD PAINEL
    // =============================
    @PostMapping("/{id}/upload-painel")
    public String uploadPainel(@PathVariable Long id,
            @RequestParam("painel") MultipartFile file) {

        System.out.println("🔥 MÉTODO CHAMADO!");
        System.out.println("Arquivo recebido: " + file.getOriginalFilename());

        Negocio negocio = negocioService.buscarPorId(id);
        if (negocio == null)
            return "redirect:/negocios";

        String url = negocioService.salvarImagem(file, id, "painel");

        if (url != null) {
            negocio.setPainelUrl(url);
            negocioService.salvar(negocio);
        }

        return "redirect:/negocios/" + id;
    }

    @PostMapping("/{id}/remover-logo")
    public String removerLogo(@PathVariable Long id) {
        Negocio negocio = negocioService.buscarPorId(id);
        if (negocio == null)
            return "redirect:/negocios";

        negocio.setLogoUrl(null);
        negocioService.salvar(negocio);

        return "redirect:/negocios/" + id;
    }

    @PostMapping("/{id}/remover-painel")
    public String removerPainel(@PathVariable Long id) {
        Negocio negocio = negocioService.buscarPorId(id);
        if (negocio == null)
            return "redirect:/negocios";

        negocio.setPainelUrl(null);
        negocioService.salvar(negocio);

        return "redirect:/negocios/" + id;
    }

    // =============================
    // LISTAR / EXCLUIR
    // =============================
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("negocios", negocioService.listarTodos());
        return "negocio/negocio-list";
    }

    @GetMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id) {
        negocioService.excluir(id);
        return "redirect:/negocios";
    }
}
