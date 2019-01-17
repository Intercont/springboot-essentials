package br.com.devdojo.springbootessentials.config;

import br.com.devdojo.springbootessentials.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;

import static br.com.devdojo.springbootessentials.config.SecurityConstants.SIGN_IN_URL;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) //para que funcionem as ROLES através da anotação @PreAuthorize
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests() //autorizar
////                .anyRequest().authenticated() //qualquer requisição autenticada
//                .antMatchers("/*/protected/**").hasRole("USER") //VALIDAÇÃO DAS URLS POR PERFIS DE USUÁRIO
//                .antMatchers("/*/admin/**").hasRole("ADMIN")
//                .and()
//                .httpBasic() //por httpBasic (Basic no Header) (tem outros, formLogin e tal)
//                .and()
//                .csrf().disable(); //desabilitar a proteção cross site reference forward para testes locais
//
//        //Permitir X-Frame Options para usar o console do H2DB no navegador
//        http.headers().frameOptions().disable();
//    }

    /**
     * Trabalhando com segurança por Token JWT
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues()) //desbloqueio geral de Cors (Cross-Origin Request, quando as requisições tem origem em domínios diferentes)
                .and().csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, SIGN_IN_URL).permitAll() //desabilitar a validação Cross-Site
                .antMatchers("/*/protected/**").hasRole("USER") //VALIDAÇÃO DAS URLS POR PERFIS DE USUÁRIO
                .antMatchers("/*/admin/**").hasRole("ADMIN")
                .and()
                .addFilter(new JWTAuthenticationFilter(authenticationManager()))
                .addFilter(new JWTAuthorizationFilter(authenticationManager(), customUserDetailsService));

        //Permitir X-Frame Options para usar o console do H2DB no navegador
        http.headers().frameOptions().disable();
    }


    /**
     * Método para autenticação através dos usuários armazenados no DB, usando o customUserDetailsService, e desencriptando
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }

    /**
     * Método encarregado da autenticação antes de acessar o endpoint, quanto estava testando com a autenticação em memória
     * Usado apenas para testes de desenvolvimento
     * @param auth
     */
//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication()
//                .withUser("igor").password("{noop}api").roles("USER")
//                .and()
//                .withUser("admin").password("{noop}admin").roles("USER","ADMIN");

//        User.withDefaultPasswordEncoder().username("igor").password("{noop}api").roles("USER").build();
//        User.withDefaultPasswordEncoder().username("admin").password("{noop}admin").roles("USER","ADMIN").build();

//    }
}
