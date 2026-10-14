package br.com.fiap.mercadoexpress.service;

import br.com.fiap.mercadoexpress.dto.CadastroForm;
import br.com.fiap.mercadoexpress.model.Perfil;
import br.com.fiap.mercadoexpress.model.Usuario;
import br.com.fiap.mercadoexpress.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Usuario cadastrar(CadastroForm form) {
        String email = form.getEmail().trim().toLowerCase();
        if (repository.existsByEmailIgnoreCase(email)) {
            throw new EmailJaCadastradoException(email);
        }
        Usuario usuario = new Usuario(form.getNome().trim(), email,
                passwordEncoder.encode(form.getSenha()), Perfil.CLIENTE);
        if (form.getTelefone() != null && !form.getTelefone().isBlank()) {
            usuario.setTelefone(form.getTelefone().trim());
        }
        return repository.save(usuario);
    }

    @Transactional(readOnly = true)
    public List<Usuario> listar() {
        return repository.findAllByOrderByCriadoEmDesc();
    }

    @Transactional(readOnly = true)
    public long contar(Perfil perfil) {
        return repository.countByPerfil(perfil);
    }

    @Transactional
    public Usuario alterarPerfil(Long id, Perfil novoPerfil) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
        usuario.setPerfil(novoPerfil);
        return usuario;
    }
}
