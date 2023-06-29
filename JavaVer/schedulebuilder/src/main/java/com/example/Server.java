package com.example;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;

public class Server {
    public static void main(String[] args) throws Exception {
        try (ServerSocket serverSocket = new ServerSocket(8080)) {

            while (true) {
                try (Socket client = serverSocket.accept()) {
                    InputStreamReader reader = new InputStreamReader(client.getInputStream());
                    BufferedReader br = new BufferedReader(reader);
                    StringBuilder request = new StringBuilder();

                    String line;
                    line = br.readLine();
                    String firstLine = line;
                    int contentLength = 0;
                    String resource = line.split(" ")[1]; // get aany resources the html needs

                    // get the entire reqeust
                    while (!line.isBlank()) {
                        request.append(line + "\r\n");
                        if (line.startsWith("Content-Length")) {
                            contentLength = Integer.valueOf(line.split(" ")[1]);
                        }
                        line = br.readLine();
                    }

                    System.out.println(request);

                    OutputStream clientOutput = client.getOutputStream();
                    // handle get requests
                    if (firstLine.startsWith("GET")) {
                        if (resource.equals("/")) {
                            FileInputStream html = new FileInputStream("HomePage.html");
                            FileInputStream img = new FileInputStream("sadie.png");

                            clientOutput.write(("HTTP/1.1 200 OK\r\n").getBytes());
                            clientOutput.write(("\r\n").getBytes());
                            clientOutput.write(html.readAllBytes());
                            clientOutput.flush();

                            html.close();
                            img.close();
                        }
                        else if (resource.equals("/sadie.png")) { 
                            FileInputStream img = new FileInputStream("sadie.png");
                            clientOutput.write(("HTTP/1.1 200 OK\r\n").getBytes());
                            clientOutput.write(("\r\n").getBytes());
                            clientOutput.write(img.readAllBytes());
                            img.close();
                        }
                        else if (resource.equals("/favicon.ico")) {
                            FileInputStream img = new FileInputStream("favicon.png");
                            clientOutput.write(("HTTP/1.1 200 OK\r\n").getBytes());
                            clientOutput.write(("\r\n").getBytes());
                            clientOutput.write(img.readAllBytes());
                            img.close();
                        }
                    }
                    // handle post requests
                    else if (firstLine.startsWith("POST")) {
                        byte[] buffer = new byte[contentLength];
                        String data = "";
                        //line = br.readLine();
                        System.out.println(line);
                    }

                    client.close();
                }
                catch (Exception f) {
                    System.out.println(f);
                }
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }
}
