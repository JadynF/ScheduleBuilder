package com.example;

public class Main {
    public static void main( String[] args ) {
        Scrape webScraper = new Scrape();
        Courses html[] = webScraper.getCourseData("2023", "fall", "ENGR", "101");
        //System.out.println(html);
    }
}
