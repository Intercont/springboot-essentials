package br.com.devdojo.springbootessentials.endpoint;

import br.com.devdojo.springbootessentials.error.ResourceNotFoundException;
import br.com.devdojo.springbootessentials.model.Student;
import br.com.devdojo.springbootessentials.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("v1")
public class StudentEndpoint {

    private final StudentRepository studentDAO;

    @Autowired
    public StudentEndpoint(StudentRepository studentDAO) {
        this.studentDAO = studentDAO;
    }

    @GetMapping(path = "protected/students")
    public ResponseEntity<?> listAll(Pageable pageable) { //pageable para paginar os resultados
        //exemplo de chamada sem alterar o padrão de 20: http://localhost:8080/students?page=3&size=5
        return new ResponseEntity<>(studentDAO.findAll(pageable), HttpStatus.OK);
    }

    @GetMapping(path = "protected/students/{id}")
    public ResponseEntity<?> getStudentById(@PathVariable("id") Long id,
                                            @AuthenticationPrincipal UserDetails userDetails) {
        System.out.println(userDetails);
        verifyIfStudentExists(id);
        //sucesso, foi encontrado, vamos retornar o objeto
        return new ResponseEntity<>(studentDAO.findById(id).get(), HttpStatus.OK);
    }

    @GetMapping(path = "protected/students/findByName/{username}")
    public ResponseEntity<?> findStudentsByUsername(@PathVariable String username){
        return new ResponseEntity<>(studentDAO.findByUsernameIgnoreCaseContaining(username), HttpStatus.OK);
    }

    //criar algo no servidor, ou inserir algo no banco de dados - POST
    @PostMapping(path = "admin/students")
    @Transactional //indispensavel para que o rollback seja executado em caso de erro e utilizar a engine InnoDB para MySQL
    @PreAuthorize("hasRole('ADMIN')") //apenas usuários com perfil ADMIN podem inserir dados pela API
//    @Transactional(rollbackFor = Exception.class) //Se quiser trabalhar com exceções do tipo Checked (tratar dentro de um try catch), especificar como RollbackFor. Se atentar que possivelmente não fará o rollback automático no DB
    public ResponseEntity<?> save(@Valid @RequestBody Student student) {
        /* TESTE DE ROLLBACK FORÇANDO UMA EXCEÇÃO RUNTIME
        studentDAO.save(student);
        studentDAO.save(student);
        try {
            if (true) {
                throw new RuntimeException("Testando a bodega");
            }
        } catch (Exception e){
            System.out.printf("Caiu na exceção");
        }
        studentDAO.save(student);*/
        return new ResponseEntity<>(studentDAO.save(student), HttpStatus.CREATED);
    }

    //REMOVER algo do servidor - DELETE
    @DeleteMapping(path = "admin/students/{id}")
    @Transactional
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        verifyIfStudentExists(id);
        studentDAO.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //atualização nos dados do servidor - PUT
    @PutMapping(path = "admin/students")
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> update(@RequestBody Student student) {
        verifyIfStudentExists(student.getId());
        studentDAO.save(student);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //caso não exista, já interrompo a execução
    private void verifyIfStudentExists(Long id){
        Optional<Student> studentOptional = studentDAO.findById(id);

        if(!studentOptional.isPresent()){
            //erro, nada foi encontrado
            throw new ResourceNotFoundException("Student not found for ID: " + id);
        }
    }
}
