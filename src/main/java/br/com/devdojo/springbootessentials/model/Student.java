package br.com.devdojo.springbootessentials.model;

import javax.persistence.Entity;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Entity
public class Student extends AbstractEntity {

    @NotEmpty(message = "Name field is mandatory") //validações, mensagem a ser retornada na resposta de erro
    private String username;

    @NotEmpty(message = "Email field is mandatory")
    @Email
    private String email;

    public Student() {
    }

    public Student(@NotEmpty Long id, @NotEmpty(message = "Name field is mandatory") String username, @Email @NotEmpty(message = "Email field is mandatory") String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

    public Student(@NotEmpty(message = "Name field is mandatory") String username, @Email @NotEmpty(message = "Email field is mandatory") String email) {
        this.username = username;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}