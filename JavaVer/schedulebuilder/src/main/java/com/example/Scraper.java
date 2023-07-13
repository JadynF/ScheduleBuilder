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
                for (Element row : rows) {
                    Elements rowData = row.select("td");
                    Courses currCourse = null;
                    for (Element cellData : rowData) { // get each cell in row
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
                        }
                    }
                    // if object doesnt have adequate data, remove from list
                    if (currCourse != null && (currCourse.getOpen() == false || (currCourse.getDays() == null && (!currCourse.getModality().equals("Asynchronous Online"))) || currCourse.getCallNum() == null || (currCourse.getTime() == null && (!currCourse.getModality().equals("Asynchronous Online"))) || currCourse.getCancelled())) {
                        courseList.remove(courseList.size() - 1);
                    }
                }
            }
        }
        return courseList;
    }

    public static String getCoursePage(String year, String quarter, String subject, String cNum) {
        try {
            // get main BOSS page
            HttpGet request = new HttpGet("https://boss.latech.edu/ia-bin/tsrvweb.cgi?&WID=W&tserve_tip_write=%7C%7CWID&ConfigName=rcrssecthp1&ReqNum=1&TransactionSource=H&tserve_trans_config=rcrssecthp1.cfg&tserve_host_code=HostZero&tserve_tiphost_code=TipZero");
            CloseableHttpResponse getResponse = httpClient.execute(request);
            //System.out.println(EntityUtils.toString(getResponse.getEntity()));
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

            HttpPost postTerms = new HttpPost(postUrl);
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
            //System.out.println(EntityUtils.toString(termResponse.getEntity()));
            termResponse.close();

            subject = subject.toUpperCase();

            HttpPost postSubject = new HttpPost(postUrl);
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
            //System.out.println(EntityUtils.toString(subjectResponse.getEntity()));
            subjectResponse.close();

            // create course id value for payload
            String courseID;
            if (subject.length() == 3)
                courseID = subject + " -" + cNum;
            else
                courseID = subject + "-" + cNum;

            HttpPost postSection = new HttpPost(postUrl);
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

    //public static void main( String[] args ) {
    //    ArrayList<Courses> courseList = scrape("2023", "fall", "CSC", "130");
//
    //    for (Courses c : courseList) {
    //        System.out.println(c.toString());
    //    }
    //}
//
    //    try {
    //        HttpGet request = new HttpGet("https://boss.latech.edu/ia-bin/tsrvweb.cgi?&WID=W&tserve_tip_write=%7C%7CWID&ConfigName=rcrssecthp1&ReqNum=1&TransactionSource=H&tserve_trans_config=rcrssecthp1.cfg&tserve_host_code=HostZero&tserve_tiphost_code=TipZero");
    //        CloseableHttpResponse getResponse = httpClient.execute(request);
    //        //System.out.println(EntityUtils.toString(getResponse.getEntity()));
    //        getResponse.close();
//
    //        String postUrl = "https://boss.latech.edu/ia-bin/tsrvweb.cgi";
//
    //        HttpPost postTerms = new HttpPost(postUrl);
    //        List<NameValuePair> urlTermParams = new ArrayList<>();
    //        urlTermParams.add(new BasicNameValuePair("tserve_tip_read_destroy", ""));
    //        urlTermParams.add(new BasicNameValuePair("tserve_host_code", "HostZero"));
    //        urlTermParams.add(new BasicNameValuePair("tserve_tiphost_code", "TipZero"));
    //        urlTermParams.add(new BasicNameValuePair("tserve_trans_config", "RCRSSECTHP1.cfg"));
    //        urlTermParams.add(new BasicNameValuePair("tserve_tip_write", "||WID|SID|PIN|Term|Subject|CourseID|AppTerm|ConfigName"));
    //        urlTermParams.add(new BasicNameValuePair("TransactionSource", "H"));
    //        urlTermParams.add(new BasicNameValuePair("Term", "20241"));
    //        postTerms.setEntity(new UrlEncodedFormEntity(urlTermParams));
    //        CloseableHttpResponse termResponse = httpClient.execute(postTerms);
    //        //System.out.println(EntityUtils.toString(termResponse.getEntity()));
    //        termResponse.close();
//
    //        HttpPost postSubject = new HttpPost(postUrl);
    //        List<NameValuePair> urlSubjectParams = new ArrayList<>();
    //        urlSubjectParams.add(new BasicNameValuePair("tserve_tip_read_destroy", ""));
    //        urlSubjectParams.add(new BasicNameValuePair("tserve_host_code", "HostZero"));
    //        urlSubjectParams.add(new BasicNameValuePair("tserve_tiphost_code", "TipZero"));
    //        urlSubjectParams.add(new BasicNameValuePair("tserve_trans_config", "rcrssecthp2.cfg"));
    //        urlSubjectParams.add(new BasicNameValuePair("tserve_tip_write", "||WID|SID|PIN|Term|Subject|CourseID|AppTerm|ConfigName"));
    //        urlSubjectParams.add(new BasicNameValuePair("TransactionSource", "H"));
    //        urlSubjectParams.add(new BasicNameValuePair("ReqNum", "2"));
    //        urlSubjectParams.add(new BasicNameValuePair("Subject", "ENGL"));
    //        postSubject.setEntity(new UrlEncodedFormEntity(urlSubjectParams));
    //        CloseableHttpResponse subjectResponse = httpClient.execute(postSubject);
    //        //System.out.println(EntityUtils.toString(subjectResponse.getEntity()));
    //        subjectResponse.close();
//
    //        HttpPost postSection = new HttpPost(postUrl);
    //        //postSection.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7");
    //        //postSection.addHeader("Accept-Encoding", "gzip, deflate, br");
    //        //postSection.addHeader("Cache-Control", "max-age=0");
    //        //postSection.addHeader("Content-Type", "application/x-www-form-urlencoded");
    //        //postSection.addHeader("Referer", "https://boss.latech.edu/ia-bin/tsrvweb.cgi");
    //        //postSection.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36");
    //        List<NameValuePair> urlSectionParams = new ArrayList<>();
    //        urlSectionParams.add(new BasicNameValuePair("tserve_tip_read_destroy", ""));
    //        urlSectionParams.add(new BasicNameValuePair("tserve_host_code", "HostZero"));
    //        urlSectionParams.add(new BasicNameValuePair("tserve_tiphost_code", "TipZero"));
    //        urlSectionParams.add(new BasicNameValuePair("tserve_trans_config", "rcrssecthp3.cfg"));
    //        urlSectionParams.add(new BasicNameValuePair("tserve_tip_write", "||WID|SID|PIN|Term|Subject|CourseID|AppTerm|ConfigName"));
    //        urlSectionParams.add(new BasicNameValuePair("TransactionSource", "H"));
    //        urlSectionParams.add(new BasicNameValuePair("ReqNum", "3"));
    //        urlSectionParams.add(new BasicNameValuePair("CourseID", "ENGL-101"));
    //        postSection.setEntity(new UrlEncodedFormEntity(urlSectionParams));
    //        CloseableHttpResponse sectionResponse = httpClient.execute(postSection);
    //        System.out.println(EntityUtils.toString(sectionResponse.getEntity()));
    //        sectionResponse.close();
    //    }
    //    catch (Exception e) {
    //        System.out.println(e);
    //    }
    //}
}
