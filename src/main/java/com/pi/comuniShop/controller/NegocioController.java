package com.pi.comuniShop.controller;

import com.pi.comuniShop.model.Negocio;
import com.pi.comuniShop.model.Usuario;
import com.pi.comuniShop.model.TipoUsuario;
import com.pi.comuniShop.service.NegocioService;
import com.pi.comuniShop.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
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

    // POST do modal (criar conta de negócio)
    @PostMapping("/salvar")
    public String salvarNegocioComDono(
            @RequestParam String nome,
            @RequestParam String cpf,
            @RequestParam String telefone,
            @RequestParam String email,
            @RequestParam String senha,
            @RequestParam String nomeNegocio,
            @RequestParam(required = false) String cnpj) {
        logger.info("🚀 Recebendo cadastro de negócio...");
        logger.info("Dono: {}, Email: {}, Nome Negócio: {}", nome, email, nomeNegocio);

        try {
            Usuario dono = new Usuario();
            dono.setNome(nome);
            dono.setCpf(cpf);
            dono.setTelefone(telefone);
            dono.setEmail(email);
            dono.setSenha(senha);
            dono.setTipo(TipoUsuario.CLIENTE);

            usuarioService.salvar(dono);
            logger.info("✅ Usuário salvo com sucesso (ID: {})", dono.getId());

            Negocio negocio = new Negocio();
            negocio.setNome(nomeNegocio);
            negocio.setCnpj(cnpj);
            negocio.setDono(dono);
            negocio.setEmailComercial(email);
            negocio.setTelefoneComercial(telefone);

            negocioService.salvar(negocio);
            logger.info("✅ Negócio salvo com sucesso (ID: {})", negocio.getId());

            return "redirect:/";

        } catch (Exception e) {
            logger.error("❌ Erro ao salvar negócio: {}", e.getMessage(), e);
            throw e; // mostra no console também
        }
    }

    // ✅ Listar todos (opcional)
    @GetMapping
    public String listar(org.springframework.ui.Model model) {
        model.addAttribute("negocios", negocioService.listarTodos());
        return "negocio/negocio-list";
    }

    // ✅ Detalhes (opcional)
    @GetMapping("/{id}")
    public String detalhes(@PathVariable Long id, org.springframework.ui.Model model) {
        Negocio negocio = negocioService.buscarPorId(id);
        if (negocio == null) {
            return "redirect:/negocios";
        }
        model.addAttribute("negocio", negocio);
        return "negocio/negocio-detalhes";
    }

    // ✅ Excluir (opcional)
    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id) {
        negocioService.excluir(id);
        return "redirect:/negocios";
    }
}
