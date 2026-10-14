package br.com.fiap.mercadoexpress.model;

public enum Perfil {
    ADMIN("Gerente"),
    CLIENTE("Cliente");

    private final String descricao;

    Perfil(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
