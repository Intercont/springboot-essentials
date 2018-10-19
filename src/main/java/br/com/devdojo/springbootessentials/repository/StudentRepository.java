package br.com.devdojo.springbootessentials.repository;

import br.com.devdojo.springbootessentials.model.Student;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StudentRepository extends CrudRepository<Student, Long> {
    //temos varios tipos de consultas já existentes no CrudRepository
    //Mas também podemos adicionar as que quisermos acrescentar aqui e o próprio Spring se encarregará de realizar as queries
    List<Student> findByName(String name); //nestecaso é mandatório que o nome do atributo a ser encontrado coincida com o nome do metodo
}
