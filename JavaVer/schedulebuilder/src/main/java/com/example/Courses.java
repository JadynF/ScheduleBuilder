package com.example;
import java.util.ArrayList;

public class Courses {
    
    private String course;
    private String subject;
    private String section;
    private String callNum;
    private Boolean open = false;
    private int openSeats;
    private int maxSeats;
    private String modality;
    private ArrayList<String> days = new ArrayList<String>();
    private ArrayList<String> time = new ArrayList<String>();
    private ArrayList<Integer> hourStart = new ArrayList<Integer>();
    private ArrayList<Integer> minuteStart = new ArrayList<Integer>();
    private ArrayList<Integer> hourEnd = new ArrayList<Integer>();
    private ArrayList<Integer> minuteEnd = new ArrayList<Integer>();
    private int numTimes = 0;
    private String location;
    private String instructor;
    private String restrictions;
    private Boolean honors = false;
    private Boolean cancelled = false;

    public Courses(String sec) {
        this.section = setSection(sec);
    }

    ////////////////////////////////////////// SETTERS ////////////////////////////////////////////////

    public String setSection(String sec) {
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
        if (this.callNum.contains("H"))
            this.honors = true;
    }

    public void setStatAndSeat(String sAndS) {
        String[] parsed = sAndS.split("\\s+");
        if (parsed.length == 0)
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
            this.modality = "Not Specified";
            return;
        }
        this.modality = mode;
    }

    public void setSetting(String setting) {

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

    public void setInstructor(String ins) {
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
