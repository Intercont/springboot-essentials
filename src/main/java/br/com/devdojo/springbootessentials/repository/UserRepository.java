package br.com.devdojo.springbootessentials.repository;

import br.com.devdojo.springbootessentials.model.User;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Repositório responsável pela busca dos usuários no DB
 */
public interface UserRepository extends PagingAndSortingRepository<User, Long> {

    User findByUsername(String username);

}
