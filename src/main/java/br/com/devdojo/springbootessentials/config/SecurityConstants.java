package br.com.devdojo.springbootessentials.config;

import java.util.concurrent.TimeUnit;

public class SecurityConstants {
    //estrutura do Header do Token: Authorization Bearer <tokengeradosemaschaves>
    static final String SECRET = "IgorTokenTeste";
    static final String TOKEN_PREFIX = "Bearer ";
    static final String HEADER_STRING = "Authorization";
    static final String SIGN_IN_URL = "/users/sign-in";
    static final long EXPIRATION_TIME = 86400000L; //VALIDADE DE 1 dia em milis

    //validade do Tokem em Milisegundos
    public static void main(String[] args) {
        //token válido por 1 dia, em Milisegundos, usando a classe TimeUnit para converter
        System.out.println(TimeUnit.MILLISECONDS.convert(1L, TimeUnit.DAYS));
    }


}
