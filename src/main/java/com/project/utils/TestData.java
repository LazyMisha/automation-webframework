package com.project.utils;

import com.github.javafaker.Faker;

import java.util.Date;

public class TestData {

    private static Faker faker = new Faker();

    public static String getFakeFirstName() {
        return faker.firstName();
    }

    public static String getFakeCountry(){
        return faker.country();
    }

    public static String getFakeCity(){
        return faker.cityPrefix() + " " + faker.citySuffix();
    }

    public static String getPassword() {
        return faker.zipCode() + new Date().getTime();
    }

    public static String getFakeEmail() {
        return faker.name() + "@" + faker.streetName() + "." + faker.cityPrefix();
    }


    public static int getRandomDigit(int min, int max) {
        return (int) (Math.random() * ((max - min) + 1)) + min;
    }
}
