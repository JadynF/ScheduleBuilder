package com.example;
import java.util.ArrayList;
import java.io.*;
import org.json.JSONObject;

public class Main {
    public static void main(String year, String quarter, String[] courses, String[] courseNums) throws IOException{

        ///////////////////////////////////////// BEGIN SCRAPE //////////////////////////////////////////////////
        long start = System.nanoTime();
        Scraper webScraper = new Scraper();

        ArrayList<ArrayList<Courses>> coursesList = new ArrayList<ArrayList<Courses>>();

        for (int i = 0; i < courses.length; i++) {
            System.out.println(year + ":" + quarter + ":" + courses[i] + ":" + courseNums[i]);
            coursesList.add(webScraper.scrape(year, quarter, courses[i], courseNums[i]));
        }

        long end = System.nanoTime();
        double timeElapsed = ((end - start) / 1000000000.0);
        System.out.println(timeElapsed);

        String json = buildJson(coursesList);
        BufferedWriter jsonWriter = new BufferedWriter(new FileWriter("courses.json"));
        jsonWriter.write(json);
        jsonWriter.close();

        //webScraper.closeDriver();

        //coursesList.add(webScraper.getCourseData("2023", "fall", "CSC", "130"));
        //coursesList.add(webScraper.getCourseData("2023", "fall", "MATH", "240"));
        //coursesList.add(webScraper.getCourseData("2023", "fall", "ENGL", "101"));
        //coursesList.add(webScraper.getCourseData("2023", "fall", "BISC", "130"));

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

    public static String buildJson(ArrayList<ArrayList<Courses>> coursesList) {
        JSONObject json = new JSONObject();
        for (int i = 0; i < coursesList.size(); i++) {
            JSONObject courseJson = new JSONObject();
            json.put(coursesList.get(i).get(0).getSubject() + coursesList.get(i).get(0).getCourse(), courseJson);
            for (int j = 0; j < coursesList.get(i).size(); j++) {
                Courses course = coursesList.get(i).get(j);
                courseJson.put(course.getSection(), new JSONObject()
                    .put("callNum", course.getCallNum())
                    .put("days", course.getDays())
                    .put("startHour", course.getHourStart())
                    .put("endHour", course.getHourEnd())
                    .put("startMin", course.getMinuteStart())
                    .put("endMin", course.getMinuteEnd())
                    .put("modality", course.getModality())
                    .put("name", course.getSubject() + course.getCourse())
                    .put("open", course.getOpen())
                    .put("openSeats", course.getOpenSeats())
                    .put("maxSeats", course.getMaxSeats())
                    .put("location", course.getLocation())
                    .put("instructor", course.getInstructor())
                    .put("restrictions", course.getRestrictions())
                    .put("honors", course.isHonors())
                    .put("cancelled", course.getCancelled())
                    .put("section", course.getSection())
                    .put("name", course.getSubject() + "-" + course.getCourse()));
            }
        }
        String jsonString = json.toString();
        return jsonString;
    }
}