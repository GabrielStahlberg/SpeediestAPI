package com.hackathon.speediestapi.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UtilsImpl {

    public static Integer periodMinutes = 60;
    public static Integer downloadMinAcceptable = 8;
    public static Integer uploadMinAcceptable = 4;

    public static Double calculateGeneralAverage(List<Double> averages) {
        Double generalAverage = 0.0;
        for(Double average : averages) {
            generalAverage += average;
        }
        return getDoubleValueRounded((generalAverage / averages.size()), 1);
    }

    public static Date dateFormatter(Date date) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        String dateFormatted = formatter.format(date);
        return formatter.parse(dateFormatted);
    }

    public static int generateRandomInteger(int min, int max) {
        if(min >= max) {
            throw new IllegalArgumentException("Max must be greater than min");
        }

        Random random = new Random();
        return random.nextInt((max - min) + 1) + min;
    }

    public static double getDoubleValueRounded(double value, int scale) {
        return new BigDecimal(value).setScale(scale, RoundingMode.HALF_UP).doubleValue();
    }
}
