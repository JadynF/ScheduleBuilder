package com.example;
import java.util.ArrayList;

public class Courses {
    
    private String course; // course name
    private String subject; // course subject
    private String section; // course section
    private String callNum; // course call number
    private Boolean open = false; // if the course is open
    private int openSeats; // number of open seats in the class
    private int maxSeats; // max number of seats in the class
    private String modality; // modality of the class
    private ArrayList<String> days = new ArrayList<String>(); // array of days the course is offered, multiple listed if class is meeting more than once a day
    private ArrayList<String> time = new ArrayList<String>(); // array of times for each class days
    private ArrayList<Integer> hourStart = new ArrayList<Integer>(); // array of class start hours
    private ArrayList<Integer> minuteStart = new ArrayList<Integer>(); // array of class start minutes
    private ArrayList<Integer> hourEnd = new ArrayList<Integer>();  // array of class end hours
    private ArrayList<Integer> minuteEnd = new ArrayList<Integer>(); // array of class end minutes
    private int numTimes = 0; // number of times a day class takes place
    private String location; // location of class
    private String instructor; // instructor of class
    private String restrictions; // notes, can list required majors or honors requirements
    private Boolean honors = false; // if the class is honors
    private Boolean cancelled = false; // if the class has been listed as cancelled
    private String session; // the session the class is offered in during the quarter
    private String prereqs; // the prerequisites of the class

    private Boolean nullCourse = false; // set to true if no classes are found for this course

    public Courses(String sec) { // class constructor that takes the entire class section name
        this.section = setSection(sec);
    }

    ////////////////////////////////////////// SETTERS ////////////////////////////////////////////////

    public void setNullCourse() {
        this.nullCourse = true;
    }

    public String setSection(String sec) { // takes entire class section name, parses into the class subject, course, and returns the class section
        if (sec.contains("HONORS") || sec.contains("Honors"))
            this.honors = true;
        String[] parsed = sec.split("-");
        this.subject = parsed[0].replaceAll("\\s+", "");
        this.course = parsed[1].replaceAll("\\s+", "");
        parsed = parsed[2].split("\\s+");
        return parsed[0];
    }

    public void setCallNum(String newCNum) {
        if (newCNum.length() == 0)
            return;
        this.callNum = newCNum;
        if (this.callNum.contains("H")) // checks if the course is honors
            this.honors = true;
    }

    public void setPrereqs(String p) {
        this.prereqs = p;
    }

    public void setSession(String s) {
        this.session = s;
    }

    public void setStatAndSeat(String sAndS) {
        String[] parsed = sAndS.split("\\s+");
        if (parsed.length == 0) // if no seats stats are listed
            return;
        else if (parsed[0].equals("Open")) {
            this.open = true;
            if (parsed.length == 5) {
                this.openSeats = Integer.parseInt(parsed[2]);
                this.maxSeats = Integer.parseInt(parsed[4]);
            }
        }
    }

    public void setModality(String mode) {
        if (mode.length() == 0) {
            this.modality = "No Specified Modality";
            return;
        }
        this.modality = mode;
    }

    public void setSetting(String setting) { // takes entire class setting string, sets the location, the days, the times, hours, and minutes. NOTE: can be called more than once if the class meets more than once a day

        if (setting.equals("Main Campus")) {
            this.location = "Main Campus";
            return;
        }
        else if (setting.equals("To Be Arranged") || setting.startsWith("To Be Arranged")) {
            this.location = "TBA";
            return;
        }
        else if (setting.startsWith("CANCELLED")) {
            this.cancelled = true;
            return;
        }

        String[] parsed = setting.split("\\s+");
        this.days.add(parsed[0]);
        this.time.add(parsed[1]);

        this.hourStart.add(Integer.valueOf(parsed[1].substring(0,2)));
        if (this.hourStart.get(this.numTimes) < 8)
            this.hourStart.set(this.numTimes, this.hourStart.get(this.numTimes) + 12);
        this.minuteStart.add(Integer.valueOf(parsed[1].substring(3,5)));
        this.hourEnd.add(Integer.valueOf(parsed[1].substring(6,8)));
        if (this.hourEnd.get(this.numTimes) < 8)
            this.hourEnd.set(this.numTimes, this.hourEnd.get(this.numTimes) + 12);
        this.minuteEnd.add(Integer.valueOf(parsed[1].substring(9,11)));

        this.location = parsed[2] + " ";
        int i = 3;
        while (i < parsed.length) {
            this.location += parsed[i] + " ";
            i++;
        }
        this.numTimes++;
    }

    public void setInstructor(String ins) { // takes the entire instructor string, which will also contain any restrictions for the class
        String[] parsed = ins.split("\\s+");
        if (parsed.length <= 1)
            return;
        this.instructor = parsed[0] + " " + parsed[1].charAt(0);
        this.restrictions = "";
        for (int i = 2; i < parsed.length; i++) {
            this.restrictions += parsed[i] + " ";
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////// GETTERS /////////////////////////////////////////////

    public String getPrereqs() {
        return this.prereqs;
    }

    public String getSession() {
        return this.session;
    }

    public Boolean getNullCourse() {
        return this.nullCourse;
    }

    public String getCourse() {
        return this.course;
    }

    public int getNumTimes() {
        return this.numTimes;
    }

    public String getSubject() {
        return this.subject;
    }
    
    public String getSection() {
        return this.section;
    }
    
    public String getCallNum() {
        return this.callNum;
    }
    
    public Boolean getOpen() {
        return this.open;
    }
    
    public int getOpenSeats() {
        return this.openSeats;
    }
    
    public int getMaxSeats() {
        return this.maxSeats;
    }
    
    public String getModality() {
        return this.modality;
    }
    
    public ArrayList<String> getDays() {
        return this.days;
    }
    
    public ArrayList<String> getTime() {
        return this.time;
    }
    
    public ArrayList<Integer> getHourStart() {
        return this.hourStart;
    }
    
    public ArrayList<Integer> getMinuteStart() {
        return this.minuteStart;
    }
    
    public ArrayList<Integer> getHourEnd() {
        return this.hourEnd;
    }
    
    public ArrayList<Integer> getMinuteEnd() {
        return this.minuteEnd;
    }
    
    public String getLocation() {
        return this.location;
    }
    
    public String getInstructor() {
        return this.instructor;
    }
    
    public String getRestrictions() {
        return this.restrictions;
    }
    
    public Boolean getCancelled() {
        return this.cancelled;
    }

    public Boolean isHonors() {
        return this.honors;
    }

    /////////////////////////////////////////////////////////////////////////////////

    public String toString() {
        String result = "";
        if (this.cancelled) {
            return "CANCELLED";
        }
        result += "Course: " + this.subject + " " + this.course + " " + this.section + "\n";
        result += "Call number: " + this.callNum + "\n";
        if (this.open) {
            result += this.openSeats + "/" + this.maxSeats + " seats are open\n";
        }
        result += "Modality: " + this.modality + "\n";
        for (int i = 0; i < this.numTimes; i++) {
            result += "Days: " + this.days.get(i) + "\n";
            result += "Time: " + this.time.get(i) + "\n";
            result += this.hourStart.get(i) + " " + this.minuteStart.get(i) + " " + this.hourEnd.get(i) + " " + this.minuteEnd.get(i) + "\n";
        }
        result += "Location: " + this.location + "\n";
        result += "Instructor: " + this.instructor + "\n";
        if (!this.restrictions.equals("")) {
            result += "Restrictions: " + this.restrictions + "\n";
        }
        return result;
    }
}
