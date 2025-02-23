package io.github.samuelsantos20.time_sheet.util;

import java.util.Random;

public class RegistrationGenerator {

    public String Generator() {

        Random random = new Random();
        int sequence = random.nextInt(9000000) + 1000000; // Gera um número entre 1.000.000 e 9.999.999

        System.out.println(sequence);

        return String.valueOf(sequence);
    }




}
