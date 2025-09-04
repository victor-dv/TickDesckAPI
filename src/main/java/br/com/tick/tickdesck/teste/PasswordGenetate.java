package br.com.tick.tickdesck.teste;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGenetate {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "123456789";
        String encodedPassword = encoder.encode(rawPassword);
        System.out.println(encodedPassword);
    }
}
