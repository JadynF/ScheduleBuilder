package com.example;
import java.io.*;

public class MainTest {
    public static void main(String[] args) throws IOException{
        String year = "2023";
        String quarter = "fall";
        String[] courses = {"ANSC"};
        String[] courseNums = {"309"};
        Main.main(year, quarter, courses, courseNums);
    }
}
