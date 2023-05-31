package com.example;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    public static void main( String[] args ) {
        Scrape webScraper = new Scrape();

        ArrayList<ArrayList<Courses>> coursesList = new ArrayList<ArrayList<Courses>>();
        coursesList.add(webScraper.getCourseData("2023", "fall", "CSC", "130"));
        coursesList.add(webScraper.getCourseData("2023", "fall", "HNRS", "100"));
        coursesList.add(webScraper.getCourseData("2023", "fall", "MATH", "240"));
        coursesList.add(webScraper.getCourseData("2023", "fall", "ENGL", "101"));
        webScraper.closeDriver();
        
        String[][] requiredCourses = null;//{{"HNRS", "100", "H03"}};
        String[][] requiredInstructor = {{"CSC130", "CHERRY K"}, {"MATH240", "BOYET C"}};

        Schedule scheduleObject = new Schedule(-1, -1, -1, -1, null, null, true, true);
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
