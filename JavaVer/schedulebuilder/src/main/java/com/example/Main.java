package com.example;
import java.util.ArrayList;
import java.io.*;

public class Main {

    public static void main(String year, String quarter, String[] courses, String[] courseNums) throws IOException{

        ///////////////////////////////////////// BEGIN SCRAPE //////////////////////////////////////////////////

        Scrape webScraper = new Scrape();

        ArrayList<ArrayList<Courses>> coursesList = new ArrayList<ArrayList<Courses>>();

        for (int i = 0; i < courses.length; i++) {
            System.out.println(year + ":" + quarter + ":" + courses[i] + ":" + courseNums[i]);
            coursesList.add(webScraper.getCourseData(year, quarter, courses[i], courseNums[i]));
        }

        //coursesList.add(webScraper.getCourseData("2023", "fall", "CSC", "130"));
        //coursesList.add(webScraper.getCourseData("2023", "fall", "MATH", "240"));
        //coursesList.add(webScraper.getCourseData("2023", "fall", "ENGL", "101"));
        webScraper.closeDriver();

        String[][] requiredCourses = null;//{{"HNRS", "100", "H03"}};
        String[][] requiredInstructor = null;//{{"ENGL101", "RUFLETH E"}};
        String[][] prohibitedInstructor = null;//{{"CSC130", "CHERRY K"}};

        Schedule scheduleObject = new Schedule(8, -1, -1, -1, requiredCourses, requiredInstructor, prohibitedInstructor, true, true);
        scheduleObject.calculateSchedules(0, new ArrayList<Courses>(), coursesList);
        ArrayList<ArrayList<Courses>> schedules = scheduleObject.getSchedules();

        BufferedWriter writer = new BufferedWriter(new FileWriter("courses.html"));
        String html = "<!DOCTYPE html><html><body>";

        int i = 0;
        for (ArrayList<Courses> cL : schedules) {
            ++i;
            html += "<h1>Shedule " + i + "</h1>";
            html += "<h2>^^^^^^^^^^^^^^^^^^^^^^^^^^^^^</h2>";
            //System.out.println("Schedule " + ++i);
            //System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
            for (Courses c : cL) {
                //c.print();
                html += c.toHTML();
                html += "<h2>^^^^^^^^^^^^^^^^^^^^^^^^^^^^^</h2>";
                //System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
            }
        }
        html += "</body></html>";
        writer.write(html);
        writer.close();
    }
}