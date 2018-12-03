package br.com.devdojo.springbootessentials.service;

import br.com.devdojo.springbootessentials.model.User;
import br.com.devdojo.springbootessentials.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //consultamos se o usuário informado existe, caso não, lançamos exceção
        User user = Optional.ofNullable(userRepository.findByUsername(username))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        //criando uma lista de Authorities através da classe utilitária do Spring
        List<GrantedAuthority> authorityListAdmin = AuthorityUtils.createAuthorityList("ROLE_USER", "ROLE_ADMIN");
        List<GrantedAuthority> authorityListUser = AuthorityUtils.createAuthorityList("ROLE_USER");

        //retornando o usuário do Spring, com a authorityList correspondente para o Spring Security , que validará através do @PreAuthorize
        // (atenção pois é diferente do User criado como entidade)
        return new org.springframework.security.core.userdetails.User(
                user.getName(),
                user.getPassword(),
                user.isAdmin() ? authorityListAdmin : authorityListUser); //ternário, se for admin, envia a lista admin, se não, lista user


    }
}
