package io.github.samuelsantos20.time_sheet.util;

import java.util.Random;

public class PasswordGenerator {

    private final static int LENGTH = 8;

    public String Generator() {

        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()";

        Random random = new Random();

        StringBuilder password = new StringBuilder();

        for (int i = 0; i < LENGTH; i++) {


            int index = random.nextInt(characters.length());

            password.append(index);

        }

        return password.toString();

    }


}
