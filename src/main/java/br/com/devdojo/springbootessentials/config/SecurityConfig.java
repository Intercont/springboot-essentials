package br.com.devdojo.springbootessentials.config;

import br.com.devdojo.springbootessentials.service.CustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) //para que funcionem as ROLES através da anotação @PreAuthorize
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomUserDetailService customUserDetailService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests() //autorizar
                .anyRequest().authenticated() //qualquer requisição autenticada
                .and()
                .httpBasic() //por httpBasic (Basic no Header) (tem outros, formLogin e tal)
                .and()
                .csrf().disable(); //desabilitar a proteção cross site reference forward para testes locais
    }

    /**
     * Método para autenticação através dos usuários armazenados no DB, usando o customUserDetailService, e desencriptando
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailService).passwordEncoder(new BCryptPasswordEncoder());
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
