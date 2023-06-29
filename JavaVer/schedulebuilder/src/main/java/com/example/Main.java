package com.example;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.SwingUtilities;

public class Main {

    public static void main( String[] args ) {

        ///////////////////////////////////////// BEGIN SCRAPE //////////////////////////////////////////////////
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                GuiMain frame = new GuiMain();
                frame.run();
            }
        });


        Scrape webScraper = new Scrape();

        ArrayList<ArrayList<Courses>> coursesList = new ArrayList<ArrayList<Courses>>();

        coursesList.add(webScraper.getCourseData("2023", "fall", "CSC", "130"));
        coursesList.add(webScraper.getCourseData("2023", "fall", "HNRS", "100"));
        coursesList.add(webScraper.getCourseData("2023", "fall", "MATH", "240"));
        coursesList.add(webScraper.getCourseData("2023", "fall", "ENGL", "101"));
        webScraper.closeDriver();

        String[][] requiredCourses = null;//{{"HNRS", "100", "H03"}};
        String[][] requiredInstructor = {{"CSC130", "CHERRY K"}};
        String[][] prohibitedInstructor = {{"CSC130", "CHERRY K"}};

        Schedule scheduleObject = new Schedule(-1, -1, -1, -1, requiredCourses, requiredInstructor, null, true, true);
        scheduleObject.calculateSchedules(0, new ArrayList<Courses>(), coursesList);
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