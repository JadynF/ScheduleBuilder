# ScheduleBuilder

## Description

A web application that helps Tech students create schedules for upcoming academic terms. 

The student can provide the courses they wish to take for the specified academic term through the client. Once the server recieves this data, it will fetch each class that Tech is offering of each course for that academic term. Server side data scraping and computation is done entirely in Java. This information is then sent back to the client, where it is processed, dates and times are compared for compatability on a single schedule, and each schedule possibility the student can take will be shown. Client side computation of JSON data is done in JavaScript. The student can also add their own filters to help customize their schedule possibilities.

## Tools

The Java portion of this project was created using Maven

The [com.sun.net.httpserver](https://docs.oracle.com/javase/8/docs/jre/api/net/httpserver/spec/com/sun/net/httpserver/package-summary.html) java package was used to create the HTTP server.

Webscraping done using [Apache HttpClient](https://hc.apache.org/httpcomponents-client-5.2.x/), HTML parsing done using [jsoup](https://jsoup.org/)

