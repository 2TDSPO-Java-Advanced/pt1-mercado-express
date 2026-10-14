package br.com.fiap.mercadoexpress.service;

public class EmailJaCadastradoException extends RuntimeException {
    public EmailJaCadastradoException(String email) {
        super("Já existe uma conta com o e-mail " + email + ".");
    }
}
