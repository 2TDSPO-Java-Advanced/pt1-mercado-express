package br.com.fiap.mercadoexpress.config;

import br.com.fiap.mercadoexpress.model.Categoria;
import br.com.fiap.mercadoexpress.model.Perfil;
import br.com.fiap.mercadoexpress.model.Produto;
import br.com.fiap.mercadoexpress.model.Usuario;
import br.com.fiap.mercadoexpress.repository.ProdutoRepository;
import br.com.fiap.mercadoexpress.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final ProdutoRepository produtoRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.nome}")
    private String adminNome;
    @Value("${app.admin.email}")
    private String adminEmail;
    @Value("${app.admin.senha}")
    private String adminSenha;
    @Value("${app.demo.criar-cliente:true}")
    private boolean criarClienteDemo;
    @Value("${app.demo.email}")
    private String clienteEmail;
    @Value("${app.demo.senha}")
    private String clienteSenha;

    @Override
    public void run(String... args) {
        if (!usuarioRepository.existsByEmailIgnoreCase(adminEmail)) {
            usuarioRepository.save(new Usuario(adminNome, adminEmail.toLowerCase(),
                    passwordEncoder.encode(adminSenha), Perfil.ADMIN));
            log.info("Usuario ADMIN criado: {}", adminEmail);
        }
        if (criarClienteDemo && !usuarioRepository.existsByEmailIgnoreCase(clienteEmail)) {
            usuarioRepository.save(new Usuario("Cliente Demonstração", clienteEmail,
                    passwordEncoder.encode(clienteSenha), Perfil.CLIENTE));
            log.info("Usuario CLIENTE de demonstracao criado: {}", clienteEmail);
        }
        if (produtoRepository.count() == 0) {
            produtoRepository.saveAll(produtosIniciais());
            log.info("Produtos de exemplo cadastrados em tds_sec_mvc_tb_mercado");
        }
    }

    private List<Produto> produtosIniciais() {
        return List.of(
            produto("Banana prata", Categoria.HORTIFRUTI, "Banana prata selecionada, vendida por quilo.", "3.20", "6.49", 60, "kg"),
            produto("Tomate italiano", Categoria.HORTIFRUTI, "Tomate italiano maduro, ideal para molhos.", "4.10", "8.99", 45, "kg"),
            produto("Arroz agulhinha tipo 1 5kg", Categoria.MERCEARIA, "Pacote de 5 kg de arroz branco tipo 1.", "19.90", "29.90", 30, "pacote"),
            produto("Feijão carioca 1kg", Categoria.MERCEARIA, "Feijão carioca tipo 1, safra nova.", "5.40", "8.79", 40, "pacote"),
            produto("Azeite extra virgem 500ml", Categoria.MERCEARIA, "Azeite português extra virgem, acidez máxima 0,5%.", "24.00", "39.90", 40, "garrafa"),
            produto("Café torrado e moído 500g", Categoria.MERCEARIA, "Café tradicional torrado e moído, embalado a vácuo.", "13.50", "21.90", 25, "pacote"),
            produto("Leite integral 1L", Categoria.LATICINIOS, "Leite UHT integral, caixa de 1 litro.", "3.60", "5.49", 80, "caixa"),
            produto("Queijo muçarela fatiado 200g", Categoria.LATICINIOS, "Muçarela fatiada, bandeja de 200 g.", "7.20", "12.90", 4, "bandeja"),
            produto("Pão francês", Categoria.PADARIA, "Pão francês assado na hora, vendido por quilo.", "9.00", "16.90", 20, "kg"),
            produto("Água mineral sem gás 1,5L", Categoria.BEBIDAS, "Água mineral natural sem gás.", "1.30", "2.99", 120, "garrafa"),
            produto("Suco de uva integral 1L", Categoria.BEBIDAS, "Suco de uva 100% integral, sem adição de açúcar.", "8.50", "14.90", 3, "garrafa"),
            produto("Detergente neutro 500ml", Categoria.LIMPEZA, "Detergente líquido neutro para louças.", "1.40", "2.79", 70, "un"),
            produto("Sabão em pó 1,6kg", Categoria.LIMPEZA, "Sabão em pó multiação para roupas.", "12.30", "19.90", 18, "caixa"),
            produto("Creme dental 90g", Categoria.HIGIENE, "Creme dental com flúor, menta refrescante.", "2.10", "4.49", 50, "un"),
            produto("Pizza congelada muçarela 460g", Categoria.CONGELADOS, "Pizza de muçarela pronta para assar.", "11.00", "18.90", 15, "un")
        );
    }

    private Produto produto(String nome, Categoria categoria, String descricao, String custo,
                            String venda, int estoque, String unidade) {
        Produto p = new Produto();
        p.setNome(nome);
        p.setCategoria(categoria);
        p.setDescricao(descricao);
        p.setPrecoCusto(new BigDecimal(custo));
        p.setPrecoVenda(new BigDecimal(venda));
        p.setQuantidadeEstoque(estoque);
        p.setUnidade(unidade);
        p.setAtivo(true);
        return p;
    }
}
