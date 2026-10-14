package br.com.fiap.mercadoexpress.controller;

import br.com.fiap.mercadoexpress.model.Perfil;
import br.com.fiap.mercadoexpress.security.UsuarioLogado;
import br.com.fiap.mercadoexpress.service.ProdutoService;
import br.com.fiap.mercadoexpress.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final ProdutoService produtoService;
    private final UsuarioService usuarioService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalProdutos", produtoService.contar());
        model.addAttribute("valorEstoque", produtoService.valorEmEstoque());
        model.addAttribute("estoqueBaixo", produtoService.estoqueBaixo());
        model.addAttribute("porCategoria", produtoService.contagemPorCategoria());
        model.addAttribute("totalClientes", usuarioService.contar(Perfil.CLIENTE));
        model.addAttribute("totalAdmins", usuarioService.contar(Perfil.ADMIN));
        return "admin/dashboard";
    }

    @GetMapping("/usuarios")
    public String usuarios(Model model) {
        model.addAttribute("usuarios", usuarioService.listar());
        model.addAttribute("perfis", Perfil.values());
        return "admin/usuarios";
    }

    @PostMapping("/usuarios/{id}/perfil")
    public String alterarPerfil(@PathVariable Long id, @RequestParam Perfil perfil,
                                @AuthenticationPrincipal UsuarioLogado logado,
                                RedirectAttributes redirect) {
        if (logado.getId().equals(id)) {
            redirect.addFlashAttribute("erro", "Você não pode alterar o seu próprio perfil.");
            return "redirect:/admin/usuarios";
        }
        var usuario = usuarioService.alterarPerfil(id, perfil);
        redirect.addFlashAttribute("sucesso", usuario.getNome() + " agora é " + perfil.getDescricao() + ".");
        return "redirect:/admin/usuarios";
    }
}
