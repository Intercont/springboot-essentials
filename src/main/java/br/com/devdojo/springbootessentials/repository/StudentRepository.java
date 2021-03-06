package br.com.devdojo.springbootessentials.repository;

import br.com.devdojo.springbootessentials.model.Student;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface StudentRepository extends PagingAndSortingRepository<Student, Long> { //para paginar os resultados do GET
    //temos varios tipos de consultas já existentes no CrudRepository
    //Mas também podemos adicionar as que quisermos acrescentar aqui e o próprio Spring se encarregará de realizar as queries
    List<Student> findByUsernameIgnoreCaseContaining(String name);
    //neste caso é mandatório que o nome do atributo a ser encontrado coincida com o nome do metodo
    //ao colocar IgnoreCase ele já automáticamente ignora o Case Sensitive
    //Ao colocar Containing no nome do método, ele passa a utilizar o LIKE na query construída no lugar de igual
}
