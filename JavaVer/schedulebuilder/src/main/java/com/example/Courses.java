package com.example;
public class Courses {
    

    private String course;
    private String section;
    private String callNum;
    private Boolean open = false;
    private String openSeats;
    private String maxSeats;
    private String modality;
    private String days;
    private String time;
    private int hourStart;
    private int minuteStart;
    private int hourEnd;
    private int minuteEnd;
    private String location;
    private String instructor;
    private String restrictions;
    private Boolean cancelled = false;

    public Courses(String sec, String sub) {
        this.section = setSection(sec, sub);

    }

    public String setSection(String sec, String sub) {
        String[] parsed = sec.split("-");
        this.course = parsed[0] + "-" + parsed[1];
        parsed = parsed[2].split("\\s+");
        return "-" + parsed[0];
    }

    public void setCallNum(String newCNum) {
        if (newCNum.length() == 0)
            return;
        this.callNum = newCNum;
    }

    public void setStatAndSeat(String sAndS) {
        String[] parsed = sAndS.split("\\s+");
        if (parsed.length == 0)
            return;
        else if (parsed[0].equals("Open")) {
            this.open = true;
            if (parsed.length == 5) {
                this.openSeats = parsed[2];
                this.maxSeats = parsed[4];
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
        this.days = parsed[0];
        this.time = parsed[1];

        this.hourStart = Integer.valueOf(parsed[1].substring(0,2));
        if (hourStart < 8)
            hourStart += 12;
        this.minuteStart = Integer.valueOf(parsed[1].substring(3,5));
        this.hourEnd = Integer.valueOf(parsed[1].substring(6,8));
        if (hourEnd < 8)
            hourEnd += 12;
        this.minuteEnd = Integer.valueOf(parsed[1].substring(9,11));

        this.location = parsed[2] + " ";
        int i = 3;
        while (i < parsed.length) {
            this.location += parsed[i] + " ";
            i++;
        }
    }

    public void setInstructor(String ins) {
        String[] parsed = ins.split("\\s+");
        if (parsed.length <= 1)
            return;
        this.instructor = parsed[0] + " " + parsed[1].charAt(0);
    }

    public void print() {
        if (this.cancelled) {
            System.out.println("CANCELLED");
            return;
        }
        System.out.println("Course: " + this.course + this.section);
        System.out.println("Call number: " + this.callNum);
        if (this.open) {
            System.out.println(this.openSeats + "/" + this.maxSeats + " seats are open");
        }
        System.out.println("Modality: " + this.modality);
        System.out.println("Days: " + this.days);
        System.out.println("Time: " + this.time);
        System.out.println(this.hourStart + " " + this.minuteStart + " " + this.hourEnd + " " + this.minuteEnd);
        System.out.println("Location: " + this.location);
        System.out.println("Instructor: " + this.instructor);
    }

    //////////////////////////////////// GETTERS /////////////////////////////////////////////

    public String getCourse() {
        return this.course;
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
    
    public String getOpenSeats() {
        return this.openSeats;
    }
    
    public String getMaxSeats() {
        return this.maxSeats;
    }
    
    public String getModality() {
        return this.modality;
    }
    
    public String getDays() {
        return this.days;
    }
    
    public String getTime() {
        return this.time;
    }
    
    public int getHourStart() {
        return this.hourStart;
    }
    
    public int getMinuteStart() {
        return this.minuteStart;
    }
    
    public int getHourEnd() {
        return this.hourEnd;
    }
    
    public int getMinuteEnd() {
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
}
