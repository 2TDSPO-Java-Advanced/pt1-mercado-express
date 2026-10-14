package br.com.fiap.mercadoexpress.controller;

import br.com.fiap.mercadoexpress.dto.CadastroForm;
import br.com.fiap.mercadoexpress.service.EmailJaCadastradoException;
import br.com.fiap.mercadoexpress.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioService usuarioService;

    @GetMapping("/cadastro")
    public String formulario(@RequestParam(required = false) String email, Model model) {
        CadastroForm form = new CadastroForm();
        form.setEmail(email);
        model.addAttribute("form", form);
        return "cadastro";
    }

    @PostMapping("/cadastro")
    public String cadastrar(@Valid @ModelAttribute("form") CadastroForm form, BindingResult result,
                            RedirectAttributes redirect) {
        if (form.getSenha() != null && !form.getSenha().equals(form.getConfirmarSenha())) {
            result.rejectValue("confirmarSenha", "senha.diferente", "As senhas não são iguais");
        }
        if (result.hasErrors()) {
            return "cadastro";
        }
        try {
            usuarioService.cadastrar(form);
        } catch (EmailJaCadastradoException e) {
            result.rejectValue("email", "email.duplicado", e.getMessage() + " Faça login.");
            return "cadastro";
        }
        redirect.addFlashAttribute("sucesso", "Conta criada. Entre com seu e-mail e senha.");
        redirect.addFlashAttribute("emailCadastrado", form.getEmail().trim().toLowerCase());
        return "redirect:/login";
    }
}
