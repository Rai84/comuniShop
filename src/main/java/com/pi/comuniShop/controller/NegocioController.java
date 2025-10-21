package com.pi.comuniShop.controller;

import com.pi.comuniShop.model.Negocio;
import com.pi.comuniShop.service.NegocioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/negocios")
public class NegocioController {

    @Autowired
    private NegocioService negocioService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("negocios", negocioService.listarTodos());
        return "negocio/negocio-list";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("negocio", new Negocio());
        return "negocio/negocio-form";
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute("negocio") Negocio negocio) {
        negocioService.salvar(negocio);
        return "redirect:/negocios";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Negocio negocio = negocioService.buscarPorId(id);
        if (negocio == null)
            return "redirect:/negocios";
        model.addAttribute("negocio", negocio);
        return "negocio/negocio-form";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id) {
        negocioService.excluir(id);
        return "redirect:/negocios";
    }

    @GetMapping("/{id}")
    public String detalhes(@PathVariable Long id, Model model) {
        Negocio negocio = negocioService.buscarPorId(id);
        if (negocio == null)
            return "redirect:/negocios";
        model.addAttribute("negocio", negocio);
        return "negocio/negocio-detalhes";
    }
}
