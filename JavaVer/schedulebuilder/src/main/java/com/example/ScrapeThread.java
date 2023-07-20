package com.example;

import java.io.FileInputStream;
import java.io.OutputStream;
import com.sun.net.httpserver.HttpExchange;

public class ScrapeThread implements Runnable{
    HttpExchange ex;
    OutputStream os;
    String year;
    String quarter;
    String[] courses;
    String[] courseNums;

    public ScrapeThread (HttpExchange exchange, OutputStream output, String year, String quarter, String[] courses, String[] courseNums) {
        this.ex = exchange;
        this.os = output;
        this.year = year;
        this.quarter = quarter;
        this.courses = courses;
        this.courseNums = courseNums;
    }

    public void run() {
        try {
            Main.main(year, quarter, courses, courseNums);
            FileInputStream html = new FileInputStream("CoursesPage.html");
            ex.sendResponseHeaders(200, 0);
            os.write(html.readAllBytes());
            html.close();
            os.close();
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }
}
