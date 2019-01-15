package br.com.devdojo.springbootessentials;

import br.com.devdojo.springbootessentials.model.Student;
import br.com.devdojo.springbootessentials.repository.StudentRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.*;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;

/**
 * Testes unitários do Repositório
 */
@RunWith(SpringRunner.class)
@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.AUTO_CONFIGURED)
public class StudentRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * Este teste valida se o save está persistindo os dados no DB corretamente
     */
    @Test
    public void createShouldPersistData(){
        Student student = new Student("Igor", "igor@masterintercont.com");

        this.studentRepository.save(student);
        assertThat(student.getId()).isNotNull();
        assertThat(student.getUsername()).isEqualTo("Igor");
        assertThat(student.getEmail()).isEqualTo("igor@masterintercont.com");
    }

    @Test
    public void deleteShouldRemoveData() {
        Student student = new Student("Igor", "igor@masterintercont.com");

        this.studentRepository.save(student);
        this.studentRepository.delete(student);

        assertThat(studentRepository.findById(student.getId())).isEmpty();
    }

    @Test
    public void updateShouldChangeAndPersistData() {
        Student student1 = new Student("Igor", "igor@masterintercont.com");

        this.studentRepository.save(student1);

        student1.setUsername("Igostoso");
        student1.setEmail("intercrack@masterintercont.com");

        //assim validamos o que vem do que foi persistido
        this.studentRepository.save(student1);
        student1 = this.studentRepository.findById(student1.getId()).get();

        assertThat(student1.getUsername()).isEqualTo("Igostoso");
        assertThat(student1.getEmail()).isEqualTo("intercrack@masterintercont.com");
    }

    @Test
    public void findByNameIgnoreCaseContainingShouldIgnoreCase() {
        Student student1 = new Student("Igor", "igor@masterintercont.com");
        Student student2 = new Student("igor", "lowercase@masterintercont.com");

        this.studentRepository.save(student1);
        this.studentRepository.save(student2);

        List<Student> studentList = this.studentRepository.findByUsernameIgnoreCaseContaining("igor");

        assertThat(studentList.size()).isEqualTo(2);

    }

    private Validator validator;

    @Before
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    /**
     * TODO - NOT WORKING - verificar
     */
    @Test
    public void createWhenNameIsNullShouldThrowConstraintViolationException() {
//        thrown.expect(HttpClientErrorException.class);
        thrown.expect(ConstraintViolationException.class);
        thrown.expectMessage("Name field is mandatory");

        Student student = new Student();
        this.studentRepository.save(student);

//        Set<ConstraintViolation<Student>> violations = validator.validate(student);
//        assertFalse(violations.isEmpty());


//        this.studentRepository.findById(student.getId());
    }

    /**
     * TODO - NOT WORKING - verificar
     */
    @Test
    public void createWhenEmailIsNullShouldThrowConstraintViolationException() {
        thrown.expect(ConstraintViolationException.class);

        Student student = new Student();
//        student.setUsername("Igor");
//        student.setEmail("igorsada@df.com");

        this.studentRepository.save(student);

    }


}
