package br.com.fiap.mercadoexpress.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Entity
@Table(name = "tds_sec_mvc_tb_mercado")
@Getter
@Setter
@NoArgsConstructor
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Informe o nome do produto")
    @Size(max = 100, message = "O nome pode ter no máximo 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nome;

    @NotNull(message = "Selecione uma categoria")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Categoria categoria;

    @Size(max = 500, message = "A descrição pode ter no máximo 500 caracteres")
    @Column(length = 500)
    private String descricao;

    @NotNull(message = "Informe o preço de custo")
    @DecimalMin(value = "0.01", message = "O preço de custo deve ser maior que zero")
    @Digits(integer = 8, fraction = 2, message = "Use no máximo 2 casas decimais")
    @Column(name = "preco_custo", nullable = false, precision = 10, scale = 2)
    private BigDecimal precoCusto;

    @NotNull(message = "Informe o preço de venda")
    @DecimalMin(value = "0.01", message = "O preço de venda deve ser maior que zero")
    @Digits(integer = 8, fraction = 2, message = "Use no máximo 2 casas decimais")
    @Column(name = "preco_venda", nullable = false, precision = 10, scale = 2)
    private BigDecimal precoVenda;

    @NotNull(message = "Informe a quantidade em estoque")
    @Min(value = 0, message = "O estoque não pode ser negativo")
    @Column(name = "quantidade_estoque", nullable = false)
    private Integer quantidadeEstoque;

    @NotBlank(message = "Informe a unidade (un, kg, pacote...)")
    @Size(max = 20)
    @Column(nullable = false, length = 20)
    private String unidade;

    @Column(nullable = false)
    private Boolean ativo = true;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;

    @PrePersist
    void aoCriar() {
        criadoEm = LocalDateTime.now();
        atualizadoEm = criadoEm;
        if (ativo == null) {
            ativo = true;
        }
    }

    @PreUpdate
    void aoAtualizar() {
        atualizadoEm = LocalDateTime.now();
    }

    public BigDecimal getLucroUnitario() {
        if (precoVenda == null || precoCusto == null) {
            return BigDecimal.ZERO;
        }
        return precoVenda.subtract(precoCusto);
    }

    public BigDecimal getMargemPercentual() {
        if (precoVenda == null || precoVenda.signum() == 0) {
            return BigDecimal.ZERO;
        }
        return getLucroUnitario().multiply(BigDecimal.valueOf(100))
                .divide(precoVenda, 1, RoundingMode.HALF_UP);
    }

    public boolean isEstoqueBaixo() {
        return quantidadeEstoque != null && quantidadeEstoque <= 5;
    }
}
