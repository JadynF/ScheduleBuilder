package com.example;

import java.io.*;
import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.HashMap;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

public class Server {
    public static void main(String[] args) throws Exception {
        HttpServer sv = HttpServer.create(new InetSocketAddress(8080), 50); // create server
        sv.createContext("/", new Handler()); // set context handler
        sv.setExecutor(null);
        sv.start();
    }

    static class Handler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            OutputStream os = exchange.getResponseBody();
            String resource = exchange.getRequestURI().toString(); // get the requested resource
            System.out.println(resource);
            if (exchange.getRequestMethod().equals("GET")) { // handle GET requests
                if (resource.equals("/")) {
                    FileInputStream html = new FileInputStream("HomePage.html"); 
                    exchange.sendResponseHeaders(200, 0);
                    os.write(html.readAllBytes());
                    html.close();
                    os.close();
                }
                else if (resource.equals("/homescript.js")) {
                    FileInputStream js = new FileInputStream("homescript.js");
                    exchange.sendResponseHeaders(200, 0);
                    os.write(js.readAllBytes());
                    js.close();
                    os.close();
                }
                else if (resource.equals("/coursesscript.js")) {
                    FileInputStream js = new FileInputStream("coursesscript.js");
                    exchange.sendResponseHeaders(200, 0);
                    os.write(js.readAllBytes());
                    js.close();
                    os.close();
                }
                else if (resource.equals("/courses.json")) {
                    FileInputStream json = new FileInputStream("courses.json");
                    exchange.sendResponseHeaders(200, 0);
                    os.write(json.readAllBytes());
                    json.close();
                    os.close();
                }
                else if (resource.equals("/homestyle.css")) {
                    FileInputStream css = new FileInputStream("homestyle.css");
                    exchange.sendResponseHeaders(200, 0);
                    os.write(css.readAllBytes());
                    css.close();
                    os.close();
                }
                else if (resource.equals("/coursestyle.css")) {
                    FileInputStream css = new FileInputStream("coursestyle.css");
                    exchange.sendResponseHeaders(200, 0);
                    os.write(css.readAllBytes());
                    css.close();
                    os.close();
                }
            }
            else if (exchange.getRequestMethod().equals("POST")) { // handle POST requests
                String data = "";
                InputStream in = exchange.getRequestBody();
                int bytes;
                while ((bytes = in.read()) != -1) { // get each character in bytes
                    data += (char) bytes;
                }

                System.out.println(data);

                String[] q = data.split("&");
                Map<String, String> pairs = new HashMap<String, String>();
                for (String query : q) { // map each key value in payload
                    String[] parts = query.split("=");
                    pairs.put(parts[0], parts[1]);
                }
                if (resource.equals("/submit")) { // when user submits desired courses
                    String year = pairs.get("Year");
                    String quarter = pairs.get("Quarter");
                    int numCourses = (pairs.size() / 2) - 1;
                    String[] courses = new String[numCourses];
                    String[] courseNums = new String[numCourses];
                    for (int i = 0; i < numCourses; i++) { // get each course and course number
                        courses[i] = pairs.get("CourseName" + (i + 1)).replaceAll("\\+", "");
                        courseNums[i] = pairs.get("CourseID" + (i + 1)).replaceAll("\\+", "");
                    }
                    ScrapeThread t = new ScrapeThread(exchange, os, year, quarter, courses, courseNums); // start new thread to scrape data and send response back
                    new Thread(t).start();
                }
            }
        }
    }
}
