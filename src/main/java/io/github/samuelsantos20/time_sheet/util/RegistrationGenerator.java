package io.github.samuelsantos20.time_sheet.util;

import java.util.Random;

public class RegistrationGenerator {

    public String Generator() {

        Random random = new Random();

        int year = random.nextInt(24) + 2000;
        int sequence = random.nextInt(9000000) + 1000000;
        return year + String.valueOf(sequence);


    }


}
