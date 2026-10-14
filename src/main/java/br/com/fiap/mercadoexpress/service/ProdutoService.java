package br.com.fiap.mercadoexpress.service;

import br.com.fiap.mercadoexpress.model.Categoria;
import br.com.fiap.mercadoexpress.model.Produto;
import br.com.fiap.mercadoexpress.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    public static final int LIMITE_ESTOQUE_BAIXO = 5;

    private final ProdutoRepository repository;

    @Transactional(readOnly = true)
    public List<Produto> listar(String busca, Categoria categoria) {
        boolean temBusca = busca != null && !busca.isBlank();
        if (temBusca && categoria != null) {
            return repository.findByNomeContainingIgnoreCaseAndCategoriaOrderByNomeAsc(busca.trim(), categoria);
        }
        if (temBusca) {
            return repository.findByNomeContainingIgnoreCaseOrderByNomeAsc(busca.trim());
        }
        if (categoria != null) {
            return repository.findByCategoriaOrderByNomeAsc(categoria);
        }
        return repository.findAll(Sort.by("nome"));
    }

    @Transactional(readOnly = true)
    public Produto buscarPorId(Long id) {
        return repository.findById(id).orElseThrow(() -> new ProdutoNaoEncontradoException(id));
    }

    @Transactional
    public Produto criar(Produto produto) {
        produto.setId(null);
        return repository.save(produto);
    }

    @Transactional
    public Produto atualizar(Long id, Produto dados) {
        Produto produto = buscarPorId(id);
        produto.setNome(dados.getNome());
        produto.setCategoria(dados.getCategoria());
        produto.setDescricao(dados.getDescricao());
        produto.setPrecoCusto(dados.getPrecoCusto());
        produto.setPrecoVenda(dados.getPrecoVenda());
        produto.setQuantidadeEstoque(dados.getQuantidadeEstoque());
        produto.setUnidade(dados.getUnidade());
        produto.setAtivo(dados.getAtivo() != null ? dados.getAtivo() : Boolean.FALSE);
        return repository.save(produto);
    }

    @Transactional
    public void excluir(Long id) {
        repository.delete(buscarPorId(id));
    }

    @Transactional(readOnly = true)
    public long contar() {
        return repository.count();
    }

    @Transactional(readOnly = true)
    public List<Produto> estoqueBaixo() {
        return repository.findByQuantidadeEstoqueLessThanEqualOrderByQuantidadeEstoqueAsc(LIMITE_ESTOQUE_BAIXO);
    }

    @Transactional(readOnly = true)
    public BigDecimal valorEmEstoque() {
        return repository.findAll().stream()
                .map(p -> p.getPrecoCusto().multiply(BigDecimal.valueOf(p.getQuantidadeEstoque())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Transactional(readOnly = true)
    public Map<Categoria, Long> contagemPorCategoria() {
        Map<Categoria, Long> mapa = new LinkedHashMap<>();
        for (Categoria c : Categoria.values()) {
            mapa.put(c, repository.countByCategoria(c));
        }
        return mapa;
    }
}
