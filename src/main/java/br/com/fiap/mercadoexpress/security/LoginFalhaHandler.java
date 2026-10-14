package br.com.fiap.mercadoexpress.security;

import br.com.fiap.mercadoexpress.repository.UsuarioRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class LoginFalhaHandler implements AuthenticationFailureHandler {

    private final UsuarioRepository usuarioRepository;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        String contexto = request.getContextPath();
        String email = request.getParameter("email");

        if (exception instanceof DisabledException) {
            response.sendRedirect(contexto + "/login?inativo");
            return;
        }

        if (email != null && !email.isBlank() && !usuarioRepository.existsByEmailIgnoreCase(email.trim())) {
            String emailCodificado = URLEncoder.encode(email.trim(), StandardCharsets.UTF_8);
            response.sendRedirect(contexto + "/cadastro?naoCadastrado&email=" + emailCodificado);
            return;
        }

        response.sendRedirect(contexto + "/login?erro");
    }
}
