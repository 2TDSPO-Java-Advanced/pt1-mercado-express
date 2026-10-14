package br.com.fiap.mercadoexpress.service;

public class ProdutoNaoEncontradoException extends RuntimeException {
    public ProdutoNaoEncontradoException(Long id) {
        super("Produto com código " + id + " não foi encontrado.");
    }
}
