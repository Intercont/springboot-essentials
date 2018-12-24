package br.com.devdojo.springbootessentials.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Classe com o intuíto exclusivo de encodar as senhas em BCrypt
 */
public class PasswordEncoder {

    public static void main(String[] args) {
        //MD5, SHA1 e SHA2 estão depreciados, o que se usa neste momento é o BCrypt
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        System.out.println(passwordEncoder.encode("api"));
    }
}
