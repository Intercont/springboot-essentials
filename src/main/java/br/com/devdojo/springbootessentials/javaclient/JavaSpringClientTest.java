package br.com.devdojo.springbootessentials.javaclient;

import br.com.devdojo.springbootessentials.model.PageableResponse;
import br.com.devdojo.springbootessentials.model.Student;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JavaSpringClientTest {
    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplateBuilder()
                .rootUri("http://localhost:8080/v1/protected/students")
                .basicAuthorization("igor.fraga", "igor.api")
                .build();

        //Podemos realizar as requisições com 3 métodos diferetes: getForObject, getForEntity e exchange
        //diferenças entre o getForObject e o get for Entity
//        Student student = restTemplate.getForObject("/{id}", Student.class, 2);
//        ResponseEntity<Student> forEntity = restTemplate.getForEntity("/{id}", Student.class, 2);
//        System.out.println(student);
//        System.out.println(forEntity);
//        System.out.println(forEntity.getBody());

        //obtendo a lista de students
//        Student[] students = restTemplate.getForObject("/", Student[].class);
//        System.out.println(Arrays.toString(students));
//
//        //obtendo em uma Lista Collection diretamente com o Exchange do Spring
//        ResponseEntity<List<Student>> exchangeStudentList = restTemplate.exchange("/", HttpMethod.GET, null, new ParameterizedTypeReference<List<Student>>() {
//        });
//        System.out.println(exchangeStudentList.getBody());
//
//        //capturando do Body para um ArrayList do Java e imprimindo os nomes
//        List<Student> studentsList = new ArrayList<>(exchangeStudentList.getBody());
//
//        for (Student s: studentsList) {
//            System.out.println(s.getName());
//        }

        //Rehabilitando a PAGINAÇÃO - obtendo em uma Lista Collection diretamente com o Exchange do Spring
        ResponseEntity<PageableResponse<Student>> exchangeStudentList = restTemplate.exchange(
                "/?sort=id,desc&sort=name,asc", HttpMethod.GET,
                null,
                new ParameterizedTypeReference<PageableResponse<Student>>() {
        });

        System.out.println(exchangeStudentList.getBody().getContent());

    }
}
