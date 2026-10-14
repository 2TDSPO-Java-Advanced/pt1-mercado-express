package br.com.fiap.mercadoexpress;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MercadoExpressSecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(MercadoExpressSecurityApplication.class, args);
        System.out.println("Mercado Express (Spring Security MVC) rodando em http://localhost:8080");
    }
}
