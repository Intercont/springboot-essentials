package br.com.devdojo.springbootessentials.javaclient;

import br.com.devdojo.springbootessentials.model.PageableResponse;
import br.com.devdojo.springbootessentials.model.Student;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class JavaClientDAO {

    private RestTemplate restTemplate = new RestTemplateBuilder()
            .rootUri("http://localhost:8080/v1/protected/students")
            .basicAuthorization("igor.fraga", "igor.api")
            .build();

    private RestTemplate restTemplateAdmin = new RestTemplateBuilder()
            .rootUri("http://localhost:8080/v1/admin/students")
            .basicAuthorization("igor.fraga", "igor.api")
            .build();

    public Student findById(long id) {
        return restTemplate.getForObject("/{id}", Student.class, id);
//        ResponseEntity<Student> forEntity = restTemplate.getForEntity("/{id}", Student.class, 2);
    }

    public List<Student> listAll() {
        ResponseEntity<PageableResponse<Student>> exchangeStudentList = restTemplate.exchange(
                "/", HttpMethod.GET,
                null,
                new ParameterizedTypeReference<PageableResponse<Student>>() {
                });

        return exchangeStudentList.getBody().getContent();
    }

    public Student save(Student student) {
        ResponseEntity<Student> exchangePost = restTemplateAdmin.exchange(
                "/",
                HttpMethod.POST,
                new HttpEntity<>(student, createJsonHeader()), //3º A entidade enviada é o valor a ser postado
                Student.class);
        return exchangePost.getBody();

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
