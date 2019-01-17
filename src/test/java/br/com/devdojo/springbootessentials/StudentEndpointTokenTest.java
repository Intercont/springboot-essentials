package br.com.devdojo.springbootessentials;

import br.com.devdojo.springbootessentials.model.Student;
import br.com.devdojo.springbootessentials.repository.StudentRepository;
import org.apache.http.impl.client.HttpClients;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.client.RestTemplate;

/**
 * Classe de testes dos Endpoints da aplicação
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//para que ao rodar os testes, ele inicie com uma porta aleatória, para que não falhe caso o serviço esteja rodando na 8080
@AutoConfigureMockMvc //específica para ao mockMvc
public class StudentEndpointTokenTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    //apenas para visualização da porta utilizada, pois a configuração é feita automáticamente pela anotação SpringBootTest
    private int port;

    @MockBean //Mockito, para mockar os dados sem alterar do DB
    private StudentRepository studentRepository;

    @Autowired
    private MockMvc mockMvc; //alternativa ao restTemplate para testar

    private HttpEntity<Void> protectedHeader;
    private HttpHeaders protectedHeaderHttp;
    private HttpEntity<Void> adminHeader;
    private HttpEntity<Void> wrongHeader;

    @Before
    public void configProtectedHeaders(){
        //{"username":"cara_comum","password":"api"}
        String str = "{\"username\":\"cara_comum\",\"password\":\"api\"}";
//        HttpHeaders headers = restTemplate.postForEntity("/login", str, String.class).getHeaders();
        protectedHeaderHttp = restTemplate.postForEntity("/login", str, String.class).getHeaders();
//        this.protectedHeader = new HttpEntity<>(headers);
    }

    @Before
    public void configAdminHeaders(){
        //{"username":"pitufos_malucos","password":"api"}
        String str = "{\"username\":\"pitufos_malucos\",\"password\":\"api\"}";
        //Workaround do erro de output streaming with server authentication
//        ClientHttpRequestFactory requestFactory = new
//                HttpComponentsClientHttpRequestFactory(HttpClients.createDefault());
//        restTemplate.getRestTemplate().setRequestFactory(requestFactory);

        HttpHeaders headers = restTemplate.postForEntity("/login", str, String.class).getHeaders();
        this.adminHeader = new HttpEntity<>(headers);
    }
    @Before
    public void configWrongHeaders(){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "11111");
        this.wrongHeader = new HttpEntity<>(headers);
    }

    @Before
    public void setup() {
        Student student = new Student(2L, "Legolas", "legolas@lotr.com");
        BDDMockito.when(studentRepository.findById(student.getId())).thenReturn(java.util.Optional.of(student));
    }

    @Test
    public void listStudentsWhenTokenIsIncorrectShouldReturnStatusCode403() {
        ResponseEntity<String> response = restTemplate.exchange("/v1/protected/students/", HttpMethod.GET, wrongHeader, String.class);
        Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(403);
    }

    @Test
    public void getStudentsByIdWhenTokenIsIncorrectShouldReturnStatusCode403() {
        ResponseEntity<String> response = restTemplate.exchange("/v1/protected/students/1", HttpMethod.GET, wrongHeader, String.class);
        Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(403);
    }

    @Test
    public void listStudentsWhenTokenIsCorrectShouldReturnStatusCode200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/protected/students/").headers(protectedHeaderHttp)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
//        ResponseEntity<String> response = restTemplate.exchange("/v1/protected/students/", HttpMethod.GET, protectedHeader, String.class);
//        Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    @WithMockUser(username = "xpto", password = "xpto", roles = {"USER", "ADMIN"})
    public void getStudentsByIdWhenUsernameAndPasswordAreCorrectShouldReturnStatusCode200() throws Exception {
//        Student student = new Student(2L, "Legolas", "legolas.mock@lotr.com");
//        BDDMockito.when(studentRepository.findById(student.getId())).thenReturn(java.util.Optional.of(student));
//        BDDMockito.given(studentRepository.findById(student.getId())).willReturn(java.util.Optional.of(student));
//        ResponseEntity<Student> response = restTemplate.getForEntity("http://localhost:8080/v1/protected/students/{id}", Student.class, 1);
//        ResponseEntity<Student> response = restTemplate.getForEntity("/v1/protected/students/{id}", Student.class, student.getId());
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/protected/students/{id}", 2L))
                .andExpect(MockMvcResultMatchers.status().isOk());
//        System.out.println(response.getBody());
//        Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    @WithMockUser(username = "xpto", password = "xpto", roles = {"USER", "ADMIN"})
    public void deleteWhenUserHasRoleAdminAndStudentExistsShouldReturnStatusCode200() throws Exception {
        Student student = new Student(1L, "Legolas", "legolas.mock@lotr.com");
//        BDDMockito.when(studentRepository.findById(student.getId())).thenReturn(java.util.Optional.of(student));
//        Student student = new Student();
        student.setId(1L);
        BDDMockito.doNothing().when(studentRepository).delete(student);
//        ResponseEntity<String> exchange = restTemplate.exchange("/v1/protected/students/{id}", HttpMethod.DELETE, null, String.class, 1L);
//        Assertions.assertThat(exchange.getStatusCodeValue()).isEqualTo(200);
        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/protected/students/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    @WithMockUser(username = "xpto", password = "xpto", roles = {"USER", "ADMIN"})
    public void deleteWhenUserHasRoleAdminAndStudentDoesNotExistsShouldReturnStatusCode404() throws Exception {
//        Student student = new Student(2L, "Legolas", "legolas.mock@lotr.com");
//        BDDMockito.when(studentRepository.findById(student.getId())).thenReturn(java.util.Optional.of(student));
        Student student = new Student();
        student.setId(1L);
        BDDMockito.doNothing().when(studentRepository).delete(student);
//        ResponseEntity<String> exchange = restTemplate.exchange("http://localhost:8080/v1/protected/students/{id}", HttpMethod.DELETE, null, String.class, 1L);
//        Assertions.assertThat(exchange.getStatusCodeValue()).isEqualTo(404);
        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/protected/students/{id}", -1L))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
