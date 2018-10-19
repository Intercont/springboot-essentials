package br.com.devdojo.springbootessentials.endpoint;

import br.com.devdojo.springbootessentials.error.CustomErrorType;
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
        Optional<Student> studentOptional = studentDAO.findById(id);

        if(studentOptional.isPresent()){
            //sucesso, foi encontrado, vamos retornar o objeto
            return new ResponseEntity<>(studentOptional.get(), HttpStatus.OK);
        } else {
            //erro, nada foi encontrado
            return new ResponseEntity<>(new CustomErrorType("Student not found"), HttpStatus.NOT_FOUND);
        }
    }

    //criar algo no servidor, ou inserir algo no banco de dados - POST
    @PostMapping
    public ResponseEntity<?> save(@RequestBody Student student) {
        return new ResponseEntity<>(studentDAO.save(student), HttpStatus.OK);
    }

    //REMOVER algo do servidor - DELETE
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        studentDAO.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //atualização nos dados do servidor - PUT
    @PutMapping
    public ResponseEntity<?> update(@RequestBody Student student) {
        studentDAO.save(student);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
