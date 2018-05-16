package com.tch.kuwanx.bean;

public class TestSin {
    private static TestSin instance;

    private TestSin() {
    }

    public static TestSin getInstance() {
        if (instance == null) {
            instance = new TestSin();
        }
        return instance;
    }
}