package br.com.devdojo.springbootessentials.config;

import br.com.devdojo.springbootessentials.service.CustomUserDetailsService;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static br.com.devdojo.springbootessentials.config.SecurityConstants.*;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    private final CustomUserDetailsService customUserDetailsService;

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager, CustomUserDetailsService customUserDetailsService) {
        super(authenticationManager);
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader(HEADER_STRING);
        //VALIDAÇÃO DE INICIO DO REQUEST
        if (header == null || !header.startsWith(TOKEN_PREFIX)) {
            //ALGO DEU ERRADO, APENAS RETORNAR
            chain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken authenticationToken = getAuthenticationToken(request);
        //adicionar a autenticação no Contexto para poder utilizar nos Controllers
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        //devolvendo a requisição para a chain, para finalizar
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthenticationToken(HttpServletRequest request) {
        //obtendo o token (é o mesmo contido no Authentication, HEADER STRING, pois o Token é enviado no Header do Request)
        String token = request.getHeader(HEADER_STRING);
        if (token == null) {
            return null;
        }

        String username = Jwts.parser().setSigningKey(SECRET)
                .parseClaimsJws(token.replace(TOKEN_PREFIX, "")) //PARA REMOVER O Bearer E PASSAR APENAS A HASH DO TOKEN
                .getBody() //do Corpo
                .getSubject(); //recuperamos o Subject (o usuário que precisamos para verificar as permissões de acesso que o mesmo possui)

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
        //caso tenha usuário, retorno um novo usuário com a lista de permissões do mesmo
        return username != null ? new UsernamePasswordAuthenticationToken(
                userDetails, //passando um objeto no lugar da String, o mesmo é recebido no Authentication da chamada do Endpoint, no getStudentById
                null,
                userDetails.getAuthorities())
                : null;
    }
}
