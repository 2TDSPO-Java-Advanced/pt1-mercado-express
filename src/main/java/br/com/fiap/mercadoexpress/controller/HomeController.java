package br.com.fiap.mercadoexpress.controller;

import br.com.fiap.mercadoexpress.model.Categoria;
import br.com.fiap.mercadoexpress.service.ProdutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ProdutoService produtoService;

    @GetMapping({"/", "/index"})
    public String index(Model model) {
        model.addAttribute("totalProdutos", produtoService.contar());
        model.addAttribute("categorias", Categoria.values());
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
            return "redirect:/produtos";
        }
        return "login";
    }

    @GetMapping("/acesso-negado")
    public String acessoNegado() {
        return "erro/acesso-negado";
    }
}
