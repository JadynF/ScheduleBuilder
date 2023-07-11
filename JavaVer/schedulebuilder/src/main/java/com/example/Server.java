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
                    System.out.println(year);
                    String quarter = pairs.get("Quarter");
                    String[] courses = {pairs.get("CourseName")};
                    String[] courseNums = {pairs.get("CourseID")};
                    ScrapeThread t = new ScrapeThread(exchange, os, year, quarter, courses, courseNums);
                    new Thread(t).start();
                }
            }
        }
    }
}
