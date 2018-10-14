package br.com.devdojo.springbootessentials.endpoint;

import br.com.devdojo.springbootessentials.error.CustomErrorType;
import br.com.devdojo.springbootessentials.model.Student;
import br.com.devdojo.springbootessentials.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.util.Arrays.asList;

@RestController
@RequestMapping("students")
public class StudentEndpoint {

    @Autowired
    private DateUtil dateUtil;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> listAll() {
        return new ResponseEntity<>(Student.studentList, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    public ResponseEntity<?> getStudentById(@PathVariable("id") int id) {
        Student student = new Student();
        student.setId(id);

        int index = Student.studentList.indexOf(student);

        //se o index não foi encontrado, então -1
        if (index == -1) {
            return new ResponseEntity<>(new CustomErrorType("Student not found"), HttpStatus.NOT_FOUND);
        }
        //caso sim seja encontrado, retornamos o objeto da lista estática em studentList
        return new ResponseEntity<>(Student.studentList.get(index), HttpStatus.OK);
    }

    //criar algo no servidor, ou inserir algo no banco de dados - POST
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> save(@RequestBody Student student) {
        Student.studentList.add(student);
        return new ResponseEntity<>(student, HttpStatus.OK);
    }

    /*//atualização nos dados do servidor - PUT
    @RequestMapping(method = RequestMethod.PUT, path = "/{id}")
    public ResponseEntity<?> getStudentById(@PathVariable("id") int id) {

    }

    //REMOVER algo do servidor - DELETE
    @RequestMapping(method = RequestMethod.DELETE, path = "/{id}")
    public ResponseEntity<?> getStudentById(@PathVariable("id") int id) {

    }*/

}
