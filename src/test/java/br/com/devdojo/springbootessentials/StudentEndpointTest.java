package br.com.devdojo.springbootessentials;

import br.com.devdojo.springbootessentials.model.Student;
import br.com.devdojo.springbootessentials.repository.StudentRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.when;

/**
 * Classe de testes dos Endpoints da aplicação
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) //para que ao rodar os testes, ele inicie com uma porta aleatória, para que não falhe caso o serviço esteja rodando na 8080
@AutoConfigureMockMvc //específica para ao mockMvc
public class StudentEndpointTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    //apenas para visualização da porta utilizada, pois a configuração é feita automáticamente pela anotação SpringBootTest
    private int port;

    @MockBean //Mockito, para mockar os dados sem alterar do DB
    private StudentRepository studentRepository;

    @Autowired
    private MockMvc mockMvc; //alternativa ao restTemplate para testar

    //criação de classe estática pra configurar e autenticar o RestTemplate, pois os Endpoints estão protegidos para acesso
    //esta anotação já inputa automáticamente a autenticação no restTemplate criado acima
    @TestConfiguration
    static class Config{
        @Bean
        public RestTemplateBuilder restTemplateBuilder(){
            return new RestTemplateBuilder().basicAuthorization("igor_fraga", "api");
        }
    }


    @Test
    public void listStudentsWhenUsernameAndPasswordAreIncorrectShouldReturnStatusCode401(){
        restTemplate = restTemplate.withBasicAuth("1", "1");
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:8080/v1/protected/students/", String.class);
        Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(401);
    }

    @Test
    public void getStudentsByIdWhenUsernameAndPasswordAreIncorrectShouldReturnStatusCode401(){
        System.out.println(port);
        restTemplate = restTemplate.withBasicAuth("1", "1");
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:8080/v1/protected/students/1", String.class);
        Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(401);
    }

    @Test
    public void listStudentsWhenUsernameAndPasswordAreCorrectShouldReturnStatusCode200(){
        //Mockito - adicionando uma lista de estudantes com o Mockito
        List<Student> studentList = asList(new Student(1L, "Igor", "igor.mock@gmail.com"),
                new Student(2L, "Legolas", "legolas.mock@lotr.com"));

        BDDMockito.when(studentRepository.findAll()).thenReturn(studentList); //ao chamar o findAll através do endpoint, esta lista de Mock será carregada
//        restTemplate = restTemplate.withBasicAuth("igor", "api");
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:8080/v1/protected/students/", String.class);
        System.out.println(response.getBody());
        Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }



}
