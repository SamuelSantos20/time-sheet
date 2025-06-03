package io.github.samuelsantos20.time_sheet.util;

import java.util.Random;
// Valores gerados para User: Matricula: 5892420, Senha: GQqL^3d, Role: EMPLOYEE
public class PasswordGenerator {

    private static final String CARACTERES_PERMITIDOS =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                    "abcdefghijklmnopqrstuvwxyz" +
                    "0123456789" +
                    "!@#$%^&*()";

    public String Generator() {
        Random random = new Random();
        StringBuilder senha = new StringBuilder();


        for (int i = 0; i < 7; i++) {
            int index = random.nextInt(CARACTERES_PERMITIDOS.length());
            senha.append(CARACTERES_PERMITIDOS.charAt(index));
        }

        return senha.toString();
    }



}
