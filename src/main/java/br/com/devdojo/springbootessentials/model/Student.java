package br.com.devdojo.springbootessentials.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.Arrays.asList;

public class Student {

    private String name;
    private int id;
    public static List<Student> studentList;

    static {
        studentRepository();
    }

    public Student(String name, int id) {
        this(name); //usando o construtor abaixo para atribuir o valor de name
        this.id = id;
    }

    public Student(String name) {
        this.name = name;
    }

    public Student() {
    }

    private static void studentRepository(){
        studentList = new ArrayList<>(asList(new Student("Igão Fofino", 1), new Student("Fernanda Fofina", 2)));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    //não irá funcionar para o comparativo de ids sem estes dois pois o objeto enviado para a requisição não se necontra na lista
    //porém o valor do id do objeto sim se encontra na lista
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return id == student.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
