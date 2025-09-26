package com.pi.comuniShop.controller;

import com.pi.comuniShop.service.AdminService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/painel")
    public String painel(Model model) {
        model.addAttribute("clientes", adminService.listarClientes());
        model.addAttribute("usuarios", adminService.listarUsuarios());
        return "admin/painel";
    }
}
