package com.hackathon.speediestapi.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Random;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UtilsImpl {

    public static Integer periodMinutes = 2;

    public static int generateRandomInteger(int min, int max) {
        if(min >= max) {
            throw new IllegalArgumentException("Max must be greater than min");
        }

        Random random = new Random();
        return random.nextInt((max - min) + 1) + min;
    }
}
