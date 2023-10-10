package com.example;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;


public class Scraper {

    final static CloseableHttpClient httpClient = HttpClients.createDefault();

    public static ArrayList<Courses> scrape(String year, String quarter, String subject, String cNum) { // takes desired course year, quarter, subject, and number as strings, returns list of available classes for desired course
        String html = getCoursePage(year, quarter, subject, cNum);
        Document doc = Jsoup.parse(html);
        Elements tables = doc.select("table"); // select all tables
        ArrayList<Courses> courseList = new ArrayList<Courses>();
        for (Element table : tables) { // go through each table
            if (table.hasClass("datadisplaytable")) { // check for correct table
                Elements rows = table.select("tr"); // go through each row in table
                Courses currCourse = null;
                for (Element row : rows) {
                    Boolean sameCourse = false; // keeps check for notes and different times for same course
                    Elements rowHeaders = row.select("th");
                    if (rowHeaders.size() > 0) { // if header is present, then new class
                        currCourse = null;
                        continue;
                    }
                    Elements rowData = row.select("td");
                    if (rowData.size() != 0) {
                        if (rowData.get(0).hasAttr("colspan")) { // if colspan attribute is present, then it is course notes
                            currCourse.setPrereqs(rowData.get(0).text());
                            continue;
                        }
                        else if (currCourse != null) { // is same course, and current row will contain more information on other times
                            sameCourse = true;
                        }
                    }
                    else {
                        continue;
                    }
                    for (Element cellData : rowData) { // get each cell in row
                        if (sameCourse) { // if same course add separate time
                            if (cellData.attr("headers").equals("DaysTimeLocation")) {
                                currCourse.setSetting(cellData.text());
                                break;
                            }
                            continue;
                        }
                        if (cellData.hasAttr("headers")) {
                            String headerValue = cellData.attr("headers"); // add attribute to object where necessary
                            if (headerValue.equals("CourseID")) {
                                currCourse = new Courses(cellData.text());
                                courseList.add(currCourse); // make new course object
                            }
                            else if (headerValue.equals("CallNumber"))
                                currCourse.setCallNum(cellData.text());
                            else if (headerValue.equals("StatusAndSeats"))
                                currCourse.setStatAndSeat(cellData.text());
                            else if (headerValue.equals("Modality"))
                                currCourse.setModality(cellData.text());
                            else if (headerValue.equals("DaysTimeLocation"))
                                currCourse.setSetting(cellData.text());
                            else if (headerValue.equals("Instructor"))
                                currCourse.setInstructor(cellData.text());
                            else if (headerValue.equals("Session"))
                                currCourse.setSession(cellData.text());
                        }
                    }
                    // if object doesnt have adequate data, remove from list
                    if (courseList.size() > 0 && currCourse != null && (currCourse.getOpen() == false || currCourse.getCallNum() == null || currCourse.getCancelled())) {
                        courseList.remove(courseList.size() - 1);
                    }
                }
            }
        }
        if (courseList.size() == 0) { // if no classes found
            Courses course = new Courses(subject + "-" + cNum + "- null");
            course.setNullCourse();
            courseList.add(course);
        }
        return courseList;
    }

    public static String getCoursePage(String year, String quarter, String subject, String cNum) {
        try {
            // get main BOSS page
            HttpGet request = new HttpGet("https://boss.latech.edu/ia-bin/tsrvweb.cgi?&WID=W&tserve_tip_write=%7C%7CWID&ConfigName=rcrssecthp1&ReqNum=1&TransactionSource=H&tserve_trans_config=rcrssecthp1.cfg&tserve_host_code=HostZero&tserve_tiphost_code=TipZero");
            CloseableHttpResponse getResponse = httpClient.execute(request);
            getResponse.close();

            String postUrl = "https://boss.latech.edu/ia-bin/tsrvweb.cgi";

            String term;
            // create correct quarter value to submit
            if (quarter.equals("fall")) {
                int intYear = Integer.parseInt(year) + 1;
                year = Integer.toString(intYear);
                term = year + "1";
            }
            else if (quarter.equals("winter"))
                term = year + "2";
            else if (quarter.equals("spring"))
                term = year + "3";
            else if (quarter.equals("summer"))
                term = year + "4";
            else
                return null;

            HttpPost postTerms = new HttpPost(postUrl); // post request to send academic terms
            List<NameValuePair> urlTermParams = new ArrayList<>(); // add POST payload
            urlTermParams.add(new BasicNameValuePair("tserve_tip_read_destroy", ""));
            urlTermParams.add(new BasicNameValuePair("tserve_host_code", "HostZero"));
            urlTermParams.add(new BasicNameValuePair("tserve_tiphost_code", "TipZero"));
            urlTermParams.add(new BasicNameValuePair("tserve_trans_config", "RCRSSECTHP1.cfg"));
            urlTermParams.add(new BasicNameValuePair("tserve_tip_write", "||WID|SID|PIN|Term|Subject|CourseID|AppTerm|ConfigName"));
            urlTermParams.add(new BasicNameValuePair("TransactionSource", "H"));
            urlTermParams.add(new BasicNameValuePair("Term", term));
            postTerms.setEntity(new UrlEncodedFormEntity(urlTermParams));
            CloseableHttpResponse termResponse = httpClient.execute(postTerms); // send POST request
            termResponse.close();

            subject = subject.toUpperCase();

            HttpPost postSubject = new HttpPost(postUrl); // post request to send subject
            List<NameValuePair> urlSubjectParams = new ArrayList<>();
            urlSubjectParams.add(new BasicNameValuePair("tserve_tip_read_destroy", ""));
            urlSubjectParams.add(new BasicNameValuePair("tserve_host_code", "HostZero"));
            urlSubjectParams.add(new BasicNameValuePair("tserve_tiphost_code", "TipZero"));
            urlSubjectParams.add(new BasicNameValuePair("tserve_trans_config", "rcrssecthp2.cfg"));
            urlSubjectParams.add(new BasicNameValuePair("tserve_tip_write", "||WID|SID|PIN|Term|Subject|CourseID|AppTerm|ConfigName"));
            urlSubjectParams.add(new BasicNameValuePair("TransactionSource", "H"));
            urlSubjectParams.add(new BasicNameValuePair("ReqNum", "2"));
            urlSubjectParams.add(new BasicNameValuePair("Subject", subject));
            postSubject.setEntity(new UrlEncodedFormEntity(urlSubjectParams));
            CloseableHttpResponse subjectResponse = httpClient.execute(postSubject);
            subjectResponse.close();

            // create course id value for payload
            String courseID;
            if (subject.length() == 3)
                courseID = subject + " -" + cNum;
            else if (subject.length() == 2)
                courseID = subject + "  -" + cNum;
            else
                courseID = subject + "-" + cNum;

            HttpPost postSection = new HttpPost(postUrl); // post request to send section
            List<NameValuePair> urlSectionParams = new ArrayList<>();
            urlSectionParams.add(new BasicNameValuePair("tserve_tip_read_destroy", ""));
            urlSectionParams.add(new BasicNameValuePair("tserve_host_code", "HostZero"));
            urlSectionParams.add(new BasicNameValuePair("tserve_tiphost_code", "TipZero"));
            urlSectionParams.add(new BasicNameValuePair("tserve_trans_config", "rcrssecthp3.cfg"));
            urlSectionParams.add(new BasicNameValuePair("tserve_tip_write", "||WID|SID|PIN|Term|Subject|CourseID|AppTerm|ConfigName"));
            urlSectionParams.add(new BasicNameValuePair("TransactionSource", "H"));
            urlSectionParams.add(new BasicNameValuePair("ReqNum", "3"));
            urlSectionParams.add(new BasicNameValuePair("CourseID", courseID));
            postSection.setEntity(new UrlEncodedFormEntity(urlSectionParams));
            CloseableHttpResponse sectionResponse = httpClient.execute(postSection);
            String html = EntityUtils.toString(sectionResponse.getEntity()); // get html
            sectionResponse.close();
            return html;
        }
        catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }
}
