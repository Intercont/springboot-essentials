package br.com.devdojo.springbootessentials.model;

import javax.persistence.Entity;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
public class Student extends AbstractEntity {

    @NotEmpty(message = "Name field is mandatory") //validações, mensagem a ser retornada na resposta de erro
    private String name;

    @NotEmpty(message = "Email field is mandatory")
    @Email
    private String email;

    public Student() {
    }

    public Student(@NotEmpty(message = "Name field is mandatory") String name, @Email @NotEmpty(message = "Email field is mandatory") String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}