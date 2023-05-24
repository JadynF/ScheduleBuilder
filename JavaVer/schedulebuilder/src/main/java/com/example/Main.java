package com.example;
import java.util.ArrayList;

public class Main {

    private static ArrayList<ArrayList<Courses>> schedules = new ArrayList<ArrayList<Courses>>();

    public static void main( String[] args ) {
        Scrape webScraper = new Scrape();
        //ArrayList<Courses> course = webScraper.getCourseData("2023", "fall", "CSC", "220");
        ArrayList<ArrayList<Courses>> coursesList = new ArrayList<ArrayList<Courses>>();
        coursesList.add(webScraper.getCourseData("2023", "fall", "CSC", "325"));
        coursesList.add(webScraper.getCourseData("2023", "fall", "MATH", "308"));
        //coursesList.add(webScraper.getCourseData("2023", "fall", "STAT", "405"));
        webScraper.closeDriver();
        for (ArrayList<Courses> clas : coursesList) {
            for (Courses c : clas) {
                c.print();
                System.out.println("***********************************************************************");
            }
            System.out.println("=======================================================================");
        }
        getSchedules(coursesList.size() - 1, new ArrayList<Courses>(), coursesList);
    }

    public static void getSchedules(int i, ArrayList<Courses> schedule, ArrayList<ArrayList<Courses>> coursesList) {
        for (Courses c : schedule) {
            c.print();
        }
        System.out.println(i);
        System.out.println("***********************************************************************");
        if (i == -1) {
            System.out.println("BUILT!");
            schedules.add(schedule);
            for (Courses c : schedule) {
                c.print();
            }
            System.out.println("***********************************************************************");
            return;
        }
        else {
            for (int j = 0; j < coursesList.get(i).size(); j++) {
                System.out.println(j);
                if (schedule.size() == 0) {
                    System.out.println("new schedule");
                    schedule.add(coursesList.get(i).get(j));
                    getSchedules(i--, schedule, coursesList);
                    schedule.remove(coursesList.get(i).get(j));
                }
                if (compatable(coursesList.get(i).get(j), schedule)) {
                    System.out.println("found compatable class");
                    schedule.add(coursesList.get(i).get(j));
                    getSchedules(i--, schedule, coursesList);
                    schedule.remove(coursesList.get(i).get(j));
                }
            }
        }
    }

    public static Boolean compatable(Courses c, ArrayList<Courses> schedule) {
        for (int i = 0; i < schedule.size(); i++) {
            if (c.getHourStart() > schedule.get(i).getHourStart() && c.getHourStart() < schedule.get(i).getHourEnd())
                return false;
            else if (schedule.get(i).getHourStart() > c.getHourStart() && schedule.get(i).getHourStart() < c.getHourEnd())
                return false;
        }
        return true;
    }
}
