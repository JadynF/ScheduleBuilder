package com.example;
import java.util.ArrayList;
import java.io.*;
import org.json.JSONObject;

public class Main {
    public static String main(String year, String quarter, String[] courses, String[] courseNums) throws IOException{ // take desired courses, scrape data, and create JSON file with data

        long start = System.nanoTime();

        ArrayList<ArrayList<Courses>> coursesList = new ArrayList<ArrayList<Courses>>(); // 2d array of every course being offered

        for (int i = 0; i < courses.length; i++) {
            coursesList.add(Scraper.scrape(year, quarter, courses[i], courseNums[i]));
        }

        long end = System.nanoTime();
        double timeElapsed = ((end - start) / 1000000000.0);
        System.out.println(timeElapsed + " seconds taken");

        String json = buildJson(coursesList);
        BufferedWriter jsonWriter = new BufferedWriter(new FileWriter("courses.json"));
        jsonWriter.write(json);
        jsonWriter.close();
        return json;
    }

    public static String buildJson(ArrayList<ArrayList<Courses>> coursesList) { // function that takes 2d array of courses being offered, and returns those courses as a JSON object

        JSONObject json = new JSONObject();
        for (int i = 0; i < coursesList.size(); i++) {
            if (coursesList.get(i).get(0).getNullCourse()) { // if no offerings for course
                json.put(coursesList.get(i).get(0).getSubject() + "-" + coursesList.get(i).get(0).getCourse(), "Null");
                continue;
            }
            JSONObject coursesJson = new JSONObject(); // new object for each individual course
            json.put(coursesList.get(i).get(0).getSubject() + coursesList.get(i).get(0).getCourse(), coursesJson);
            for (int j = 0; j < coursesList.get(i).size(); j++) { // add key and values to new course object
                Courses course = coursesList.get(i).get(j);
                JSONObject courseJson = new JSONObject()
                    .put("callNum", course.getCallNum())
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
                    .put("name", course.getSubject() + "-" + course.getCourse())
                    .put("numSettings", course.getNumTimes())
                    .put("prerequisites", course.getPrereqs())
                    .put("session", course.getSession());
                for (int k = 0; k < course.getNumTimes(); k++) { // put every offering time in object
                    courseJson.put("days" + k, course.getDays().get(k));
                    courseJson.put("startHour" + k, course.getHourStart().get(k));
                    courseJson.put("endHour" + k, course.getHourEnd().get(k));
                    courseJson.put("startMin" + k, course.getMinuteStart().get(k));
                    courseJson.put("endMin" + k, course.getMinuteEnd().get(k));
                }
                coursesJson.put(course.getSection(), courseJson);
            }
        }
        String jsonString = json.toString();
        return jsonString;
    }
}