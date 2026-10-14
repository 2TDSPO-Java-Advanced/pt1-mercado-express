package br.com.fiap.mercadoexpress.controller;

import br.com.fiap.mercadoexpress.model.Categoria;
import br.com.fiap.mercadoexpress.model.Produto;
import br.com.fiap.mercadoexpress.service.ProdutoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/produtos")
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoService service;

    @InitBinder("produto")
    public void configurarBinder(WebDataBinder binder) {
        binder.setDisallowedFields("id", "criadoEm", "atualizadoEm");
    }

    @ModelAttribute("categorias")
    public Categoria[] categorias() {
        return Categoria.values();
    }

    @GetMapping
    public String listar(@RequestParam(required = false) String busca,
                         @RequestParam(required = false) Categoria categoria,
                         Model model) {
        model.addAttribute("produtos", service.listar(busca, categoria));
        model.addAttribute("busca", busca);
        model.addAttribute("categoriaSelecionada", categoria);
        return "produtos/lista";
    }

    @GetMapping("/{id}")
    public String detalhar(@PathVariable Long id, Model model) {
        model.addAttribute("produto", service.buscarPorId(id));
        return "produtos/detalhe";
    }

    @GetMapping("/novo")
    @PreAuthorize("hasRole('ADMIN')")
    public String novo(Model model) {
        Produto produto = new Produto();
        produto.setUnidade("un");
        produto.setAtivo(true);
        produto.setQuantidadeEstoque(0);
        model.addAttribute("produto", produto);
        return "produtos/form";
    }

    @PostMapping("/novo")
    @PreAuthorize("hasRole('ADMIN')")
    public String criar(@Valid @ModelAttribute("produto") Produto produto, BindingResult result,
                        RedirectAttributes redirect) {
        if (result.hasErrors()) {
            return "produtos/form";
        }
        Produto salvo = service.criar(produto);
        redirect.addFlashAttribute("sucesso", "Produto \"" + salvo.getNome() + "\" cadastrado.");
        return "redirect:/produtos/" + salvo.getId();
    }

    @GetMapping("/{id}/editar")
    @PreAuthorize("hasRole('ADMIN')")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("produto", service.buscarPorId(id));
        return "produtos/form";
    }

    @PostMapping("/{id}/editar")
    @PreAuthorize("hasRole('ADMIN')")
    public String atualizar(@PathVariable Long id, @Valid @ModelAttribute("produto") Produto produto,
                            BindingResult result, RedirectAttributes redirect) {
        if (result.hasErrors()) {
            produto.setId(id);
            return "produtos/form";
        }
        Produto atualizado = service.atualizar(id, produto);
        redirect.addFlashAttribute("sucesso", "Alterações em \"" + atualizado.getNome() + "\" salvas.");
        return "redirect:/produtos/" + id;
    }

    @PostMapping("/{id}/excluir")
    @PreAuthorize("hasRole('ADMIN')")
    public String excluir(@PathVariable Long id, RedirectAttributes redirect) {
        Produto produto = service.buscarPorId(id);
        service.excluir(id);
        redirect.addFlashAttribute("sucesso", "Produto \"" + produto.getNome() + "\" excluído.");
        return "redirect:/produtos";
    }
}
