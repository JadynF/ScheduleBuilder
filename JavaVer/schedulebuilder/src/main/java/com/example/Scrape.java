package com.example;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.util.ArrayList;

public class Scrape 
{
    private WebDriver driver;

    public Scrape() // initialize the WebDriver
    {
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        this.driver = new ChromeDriver(options);
    }

    public void closeDriver() {
        this.driver.close();
    }

    public ArrayList<Courses> getCourseData(String year, String quarter, String subject, String cNum) { // takes desired course year, quarter, subject, and number as strings, returns list of available classes for desired course
        String html = goToCoursePage(year, quarter, subject, cNum);
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
                    if (currCourse != null && (currCourse.getDays() == null || currCourse.getCallNum() == null || currCourse.getTime() == null  || currCourse.getCancelled()))
                        courseList.remove(courseList.size() - 1);
                }
            }
        }
        return courseList;
    }

    public String goToCoursePage(String year, String quarter, String subject, String cNum) { // navigate to the courses page, return the html document or return null if error
        try {
            this.driver.get("https://boss.latech.edu/ia-bin/tsrvweb.cgi?&WID=W&tserve_tip_write=||WID&ConfigName=rcrssecthp1&ReqNum=1&TransactionSource=H&tserve_trans_config=rcrssecthp1.cfg&tserve_host_code=HostZero&tserve_tiphost_code=TipZero#top");
            Select selectQuarter = new Select(this.driver.findElement(By.xpath("/html/body/div[3]/form/table/tbody/tr[2]/td[2]/select")));
            String selectValue;
            // create correct quarter value to submit to webpage
            if (quarter.equals("fall")) {
                int intYear = Integer.parseInt(year) + 1;
                year = Integer.toString(intYear);
                selectValue = year + "1";
            }
            else if (quarter.equals("winter"))
                selectValue = year + "2";
            else if (quarter.equals("spring"))
                selectValue = year + "3";
            else if (quarter.equals("summer"))
                selectValue = year + "4";
            else
                return null;
            selectQuarter.selectByValue(selectValue);
            // submit the quarter
            WebElement submitQuarter = this.driver.findElement(By.xpath("/html/body/div[3]/form/p/input"));
            submitQuarter.click();

            // select the subject
            Select selectSubject = new Select(this.driver.findElement(By.xpath("/html/body/div[3]/form/table/tbody/tr[2]/td[2]/select")));
            selectSubject.selectByValue(subject);
            // submit the subject
            WebElement submitSubject = this.driver.findElement(By.xpath("/html/body/div[3]/form/p/input[2]"));
            submitSubject.click();

            // select the course
            Select selectCourse = new Select(this.driver.findElement(By.xpath("/html/body/div[3]/form/table/tbody/tr[2]/td[2]/select")));
            if (subject.length() == 3)
                selectValue = subject + " -" + cNum;
            else
                selectValue = subject + "-" + cNum;
            selectCourse.selectByValue(selectValue);
            // submit the course
            WebElement submitCourse = this.driver.findElement(By.xpath("/html/body/div[3]/form/p[3]/input[2]"));
            submitCourse.click();

            // get and return html document
            String html = this.driver.getPageSource();
            return html;

        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }   
}
