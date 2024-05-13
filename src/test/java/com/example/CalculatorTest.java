package com.example;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class CalculatorTest {

    @ParameterizedTest
    //@CsvFileSource(resources = "input.csv")
    @MethodSource("getValuesFromJSON")
    public void tryParameterizedTest(String inputString) throws Exception
    {
        String actualUppercase = inputString.toUpperCase();
        assertEquals(inputString.toUpperCase(), actualUppercase);
    }

    public static List<String> getValuesFromJSON()
    {
        List<String> list = new ArrayList<>();
        list.add("Pineapple");
        list.add("Apple");
        return list;
    }

}
