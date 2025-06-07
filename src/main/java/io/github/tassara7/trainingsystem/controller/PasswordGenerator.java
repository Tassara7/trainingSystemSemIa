package io.github.tassara7.trainingsystem.controller;

import java.security.SecureRandom;

// Gerador de senha segura
public class PasswordGenerator {
    private static final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%&*_";
    private static final SecureRandom random = new SecureRandom();

    public static String generate(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(CHARS.length());
            sb.append(CHARS.charAt(index));
        }
        return sb.toString();
    }

    public static long countPossibilities(int length) {
        return (long) Math.pow(CHARS.length(), length);
    }

    public static void main(String[] args) {
        int length = 10;
        String password = generate(length);
        long possibilities = countPossibilities(length);

        System.out.println("Senha gerada: " + password);
        System.out.println("Total de possibilidades: " + possibilities);
    }
}
