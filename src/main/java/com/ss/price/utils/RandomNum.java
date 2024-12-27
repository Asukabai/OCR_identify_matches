package com.ss.price.utils;

import java.math.BigDecimal;
import java.util.Random;
import java.util.UUID;

public class RandomNum {
    private static final char[] digits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

    public RandomNum() {
    }

    public static String uuidNoSplit() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static String _12() {
        return randomBySize(12);
    }

    private static String randomBySize(int size) {
        Random random = new Random();
        char[] cs = new char[size];

        for(int i = 0; i < cs.length; ++i) {
            cs[i] = digits[random.nextInt(digits.length)];
        }

        return new String(cs);
    }

    public static BigDecimal makeRandom(double max, double min, int scale) {
        BigDecimal cha = new BigDecimal(Math.random() * (max - min) + min);
        return cha.setScale(scale, 4);
    }
}