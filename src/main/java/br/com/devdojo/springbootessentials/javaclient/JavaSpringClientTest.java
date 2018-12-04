package br.com.devdojo.springbootessentials.javaclient;

import br.com.devdojo.springbootessentials.model.PageableResponse;
import br.com.devdojo.springbootessentials.model.Student;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
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

//        Podemos realizar as requisições com 3 métodos diferetes: getForObject, getForEntity e exchange
//        diferenças entre o getForObject e o get for Entity
        Student student = restTemplate.getForObject("/{id}", Student.class, 2);
        ResponseEntity<Student> forEntity = restTemplate.getForEntity("/{id}", Student.class, 2);
        System.out.println(student);
        System.out.println(forEntity);
        System.out.println(forEntity.getBody());

//        obtendo a lista de students
        Student[] students = restTemplate.getForObject("/", Student[].class);
        System.out.println(Arrays.toString(students));

        //obtendo em uma Lista Collection diretamente com o Exchange do Spring
        ResponseEntity<List<Student>> exchangeStudentList = restTemplate.exchange("/", HttpMethod.GET, null, new ParameterizedTypeReference<List<Student>>() {
        });
        System.out.println(exchangeStudentList.getBody());

        //capturando do Body para um ArrayList do Java e imprimindo os nomes
        List<Student> studentsList = new ArrayList<>(exchangeStudentList.getBody());

        for (Student s: studentsList) {
            System.out.println(s.getName());
        }

//        //GET - Rehabilitando a PAGINAÇÃO - obtendo em uma Lista Collection diretamente com o Exchange do Spring
        ResponseEntity<PageableResponse<Student>> exchangeStudentList = restTemplate.exchange(
                "/?sort=id,desc&sort=name,asc", HttpMethod.GET,
                null,
                new ParameterizedTypeReference<PageableResponse<Student>>() {
        });
//
        System.out.println(exchangeStudentList.getBody().getContent());

        //POST - postar conteúdo em uma API usando recursos do Spring
        //METODO EXCHANGE
        //1º - Vamos popular o objeto a ser postado
        Student studentPost = new Student();
        studentPost.setName("Igor Post Spring API");
        studentPost.setEmail("fuckyeah@masterintercont.com");
//
        //2º RestTemplate específico para a URL de POST com usuário Admin
        RestTemplate restTemplateAdmin = new RestTemplateBuilder()
                .rootUri("http://localhost:8080/v1/admin/students")
                .basicAuthorization("igor.fraga", "igor.api")
                .build();
//
        ResponseEntity<Student> exchangePost = restTemplateAdmin.exchange(
                "/",
                HttpMethod.POST,
                new HttpEntity<>(studentPost, createJsonHeader()), //3º A entidade enviada é o valor a ser postado
                Student.class);


        //MÉTODO POST FOR OBJECT
        studentPost.setName("Igor Post Spring API For Object");
        studentPost.setEmail("object@masterintercont.com");

        Student studentPostForObject = restTemplateAdmin.postForObject("/", studentPost, Student.class);
        System.out.println(studentPostForObject);

        //MÉTODO POST FOR ENTITY
        studentPost.setName("Igor Post Spring API For Entity");
        studentPost.setEmail("entity@masterintercont.com");

        ResponseEntity<Student> studentPostForEntity = restTemplateAdmin.postForEntity("/", studentPost, Student.class);
        System.out.println(studentPostForEntity);

    }

    /**
     * Para criar um Header genérico para o tipo de JSON
     * @return
     */
    private static HttpHeaders createJsonHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

}
