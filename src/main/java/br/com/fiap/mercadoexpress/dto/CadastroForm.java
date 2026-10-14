package br.com.fiap.mercadoexpress.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CadastroForm {

    @NotBlank(message = "Informe seu nome")
    @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres")
    private String nome;

    @NotBlank(message = "Informe seu e-mail")
    @Email(message = "Digite um e-mail válido, como nome@exemplo.com")
    private String email;

    @Pattern(regexp = "^$|^[0-9()+\\-\\s]{8,20}$", message = "Telefone inválido")
    private String telefone;

    @NotBlank(message = "Crie uma senha")
    @Size(min = 6, max = 60, message = "A senha deve ter pelo menos 6 caracteres")
    private String senha;

    @NotBlank(message = "Repita a senha")
    private String confirmarSenha;
}
