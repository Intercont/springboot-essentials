package br.com.devdojo.springbootessentials.config;

import br.com.devdojo.springbootessentials.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

import static br.com.devdojo.springbootessentials.config.SecurityConstants.*;

/*
 * Classe de Autenticação do Usuário, geração do Token a partir do User e Password
 */
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        //parse do User que vem no JSON, pois precisamos do username e pass para gerar o Token
        try {
            User user = new ObjectMapper().readValue(request.getInputStream(), User.class);
            //Tentamos realizar a Autenticação, com os dados do usuário recebido
            return this.authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getUsername(), user.getPassword()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        //para obter o nome de usuário que está vindo no Authresult - TODO - Refactor nome da classe do Model de User para outro nome
        String username = ((org.springframework.security.core.userdetails.User) authResult.getPrincipal()).getUsername();
        //geração do token
        String token = Jwts.builder()
                .setSubject(username) //usuário
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) //VALIDADE duração do Token = Data atual em Milisegundos + o periodo de UM DIA em Milisegundos
                .signWith(SignatureAlgorithm.HS512, SECRET) //criptografa com o HS512 e a senha que definimos
                .compact();
        response.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
    }
}
