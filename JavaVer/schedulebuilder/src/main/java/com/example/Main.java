package com.example;
import java.util.ArrayList;

public class Main {

    public static void main( String[] args ) {
        Scrape webScraper = new Scrape();

        ArrayList<ArrayList<Courses>> coursesList = new ArrayList<ArrayList<Courses>>();
        coursesList.add(webScraper.getCourseData("2023", "fall", "CSC", "325"));
        //coursesList.add(webScraper.getCourseData("2023", "fall", "COMM", "101"));
        coursesList.add(webScraper.getCourseData("2023", "fall", "MATH", "308"));
        coursesList.add(webScraper.getCourseData("2023", "fall", "STAT", "405"));
        webScraper.closeDriver();
        Schedule scheduleObject = new Schedule();
        scheduleObject.getSchedules(0, new ArrayList<Courses>(), coursesList);
        ArrayList<ArrayList<Courses>> schedules = scheduleObject.getSchedules();

        int i = 0;
        for (ArrayList<Courses> cL : schedules) {
            System.out.println("Schedule " + ++i);
            System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
            for (Courses c : cL) {
                c.print();
                System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
            }
        }
    }
}
