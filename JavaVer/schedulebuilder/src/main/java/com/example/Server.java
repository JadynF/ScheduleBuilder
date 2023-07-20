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
        HttpServer sv = HttpServer.create(new InetSocketAddress(8080), 0);
        sv.createContext("/", new Handler());
        sv.setExecutor(null);
        sv.start();
    }

    static class Handler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            System.out.println(exchange.getRequestMethod());
            System.out.println(exchange.getRequestHeaders());
            System.out.println(exchange.getRequestBody());
            System.out.println(exchange.getRequestURI());
            OutputStream os = exchange.getResponseBody();
            String resource = exchange.getRequestURI().toString();
            if (exchange.getRequestMethod().equals("GET")) {
                if (resource.equals("/")) {
                    FileInputStream html = new FileInputStream("HomePage.html");
                    exchange.sendResponseHeaders(200, 0);
                    os.write(html.readAllBytes());
                    html.close();
                    os.close();
                }
                else if (resource.equals("/sadie.png")) {
                    FileInputStream img = new FileInputStream("sadie.png");
                    exchange.sendResponseHeaders(200, 0);
                    os.write(img.readAllBytes());
                    img.close();
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
            }
            else if (exchange.getRequestMethod().equals("POST")) {
                String data = "";
                InputStream in = exchange.getRequestBody();
                int bytes;
                while ((bytes = in.read()) != -1) {
                    data += (char) bytes;
                }
                String[] q = data.split("&");
                Map<String, String> pairs = new HashMap<String, String>();
                for (String query : q) {
                    String[] parts = query.split("=");
                    pairs.put(parts[0], parts[1]);
                }
                System.out.println(pairs.toString());
                if (resource.equals("/submit")) {
                    String year = pairs.get("Year");
                    String quarter = pairs.get("Quarter");
                    int numCourses = (pairs.size() / 2) - 1;
                    String[] courses = new String[numCourses];
                    String[] courseNums = new String[numCourses];
                    for (int i = 0; i < numCourses; i++) {
                        courses[i] = pairs.get("CourseName" + (i + 1));
                        courseNums[i] = pairs.get("CourseID" + (i + 1));
                    }
                    ScrapeThread t = new ScrapeThread(exchange, os, year, quarter, courses, courseNums);
                    new Thread(t).start();
                }
            }
        }
    }
}
