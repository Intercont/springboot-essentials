package br.com.devdojo.springbootessentials.javaclient;

import br.com.devdojo.springbootessentials.model.Student;

import java.util.List;

public class JavaSpringClientTest {
    public static void main(String[] args) {

        Student student = new Student();
//        student.setName("Don Igone");
//        student.setEmail("igone@bigbig.com");

        //Objeto que interatua com a API
        JavaClientDAO dao = new JavaClientDAO();

        //buscando um id
//        System.out.println(dao.findById(1));

        //Listando todos os estudantes presentes
        List<Student> students = dao.listAll();
        System.out.println(students);

        //Persistindo o novo estudante
//        System.out.println(dao.save(student));
//        System.out.println(dao.listAll());

        //Atualizando o estudante
//        student.setName("Don Igone Penne");
//        student.setEmail("igone.penne@bigbig.com");
//        student.setId(12L);
//
//        dao.update(student);

        //Deletando um valor
//        dao.delete(12L);









    }


}
