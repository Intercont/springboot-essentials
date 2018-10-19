package br.com.devdojo.springbootessentials.endpoint;

import br.com.devdojo.springbootessentials.error.ResourceNotFoundException;
import br.com.devdojo.springbootessentials.model.Student;
import br.com.devdojo.springbootessentials.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("students")
public class StudentEndpoint {

    private final StudentRepository studentDAO;

    @Autowired
    public StudentEndpoint(StudentRepository studentDAO) {
        this.studentDAO = studentDAO;
    }

    @GetMapping
    public ResponseEntity<?> listAll() {
        return new ResponseEntity<>(studentDAO.findAll(), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getStudentById(@PathVariable("id") Long id) {
        verifyIfStudentExists(id);
        //sucesso, foi encontrado, vamos retornar o objeto
        return new ResponseEntity<>(studentDAO.findById(id).get(), HttpStatus.OK);
    }

    @GetMapping(path = "/findByName/{name}")
    public ResponseEntity<?> findStudentsByName(@PathVariable String name){
        return new ResponseEntity<>(studentDAO.findByNameIgnoreCaseContaining(name), HttpStatus.OK);
    }

    //criar algo no servidor, ou inserir algo no banco de dados - POST
    @PostMapping
    public ResponseEntity<?> save(@RequestBody Student student) {
        return new ResponseEntity<>(studentDAO.save(student), HttpStatus.CREATED);
    }

    //REMOVER algo do servidor - DELETE
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        verifyIfStudentExists(id);
        studentDAO.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //atualização nos dados do servidor - PUT
    @PutMapping
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
