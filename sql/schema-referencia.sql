CREATE TABLE IF NOT EXISTS tds_sec_mvc_tb_mercado (
    id                  BIGSERIAL PRIMARY KEY,
    nome                VARCHAR(100)   NOT NULL,
    categoria           VARCHAR(30)    NOT NULL,
    descricao           VARCHAR(500),
    preco_custo         NUMERIC(10,2)  NOT NULL,
    preco_venda         NUMERIC(10,2)  NOT NULL,
    quantidade_estoque  INTEGER        NOT NULL,
    unidade             VARCHAR(20)    NOT NULL,
    ativo               BOOLEAN        NOT NULL DEFAULT TRUE,
    criado_em           TIMESTAMP      NOT NULL,
    atualizado_em       TIMESTAMP
);

CREATE TABLE IF NOT EXISTS tds_users_mercado (
    id         BIGSERIAL PRIMARY KEY,
    nome       VARCHAR(100) NOT NULL,
    email      VARCHAR(120) NOT NULL UNIQUE,
    senha      VARCHAR(100) NOT NULL,
    telefone   VARCHAR(20),
    perfil     VARCHAR(20)  NOT NULL,
    ativo      BOOLEAN      NOT NULL DEFAULT TRUE,
    criado_em  TIMESTAMP    NOT NULL
);

