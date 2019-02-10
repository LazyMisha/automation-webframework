package com.project.utils;

import com.github.javafaker.Faker;

public class TestData {

    private static Faker faker = new Faker();

    public static String getFakeFirstName() {
        return faker.name().firstName();
    }

    public static String getFakeCountry(){
        return faker.address().country();
    }

    public static String getFakeCity(){
        return faker.address().city();
    }

    public static String getPassword(int min, int max) {
        return faker.lorem().characters(min, max);
    }

    public static String getFakeEmail() {
        return faker.internet().emailAddress();
    }


    public static int getRandomDigit(int min, int max) {
        return (int) (Math.random() * ((max - min) + 1)) + min;
    }
}
