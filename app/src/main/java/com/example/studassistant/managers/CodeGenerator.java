package com.example.studassistant.managers;

import java.util.Random;

public class CodeGenerator {
    public static final String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final int MAX_NUM = 10;
    public static final Random NUM_GENERATOR = new Random(System.currentTimeMillis());

    public static String generateCode(int length){
        StringBuilder generatedCode = new StringBuilder();

        for (int i = 0; i < length; ++i){
            int num = NUM_GENERATOR.nextInt(MAX_NUM);
            char letter = LETTERS.charAt(NUM_GENERATOR.nextInt(LETTERS.length()));

            int numDistance = Math.abs(MAX_NUM / 2 - num);
            int letterDistance = Math.abs(LETTERS.length() / 2 - LETTERS.indexOf(letter));

            if (numDistance == letterDistance){
                int decision = NUM_GENERATOR.nextInt(2);
                generatedCode.append((decision % 2 == 0)? num : letter);
            }
            else if (numDistance > letterDistance)
                generatedCode.append(num);
            else
                generatedCode.append(letter);
        }

        return generatedCode.toString();
    }
}
