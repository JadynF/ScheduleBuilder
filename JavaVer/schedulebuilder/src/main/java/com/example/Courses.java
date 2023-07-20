package com.example;

public class Courses {
    
    private String course;
    private String subject;
    private String section;
    private String callNum;
    private Boolean open = false;
    private int openSeats;
    private int maxSeats;
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

    public Boolean isHonors() {
        return this.honors;
    }

    /////////////////////////////////////////////////////////////////////////////////

    public void print() {
        if (this.cancelled) {
            System.out.println("CANCELLED");
            return;
        }
        System.out.println("Course: " + this.subject + " " + this.course + " " + this.section);
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
        if (!this.restrictions.equals("")) {
            System.out.println("Restrictions: " + this.restrictions);
        }
    }

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
        result += "Days: " + this.days + "\n";
        result += "Time: " + this.time + "\n";
        result += this.hourStart + " " + this.minuteStart + " " + this.hourEnd + " " + this.minuteEnd + "\n";
        result += "Location: " + this.location + "\n";
        result += "Instructor: " + this.instructor + "\n";
        if (!this.restrictions.equals("")) {
            result += "Restrictions: " + this.restrictions + "\n";
        }
        return result;
    }

    public String toHTML() {
        String result = "";
        if (this.cancelled) {
            return "<p>CANCELLED</p>";
        }
        result += "<p>Course: " + this.subject + " " + this.course + " " + this.section + "</p>";
        result += "<p>Call number: " + this.callNum + "</p>";
        if (this.open) {
            result += "<p>" + this.openSeats + "/" + this.maxSeats + " seats are open</p>";
        }
        result += "<p>Modality: " + this.modality + "</p>";
        result += "<p>Days: " + this.days + "</p>";
        result += "<p>Time: " + this.time + "</p>";
        result += "<p>" + this.hourStart + " " + this.minuteStart + " " + this.hourEnd + " " + this.minuteEnd + "</p>";
        result += "<p>Location: " + this.location + "</p>";
        result += "<p>Instructor: " + this.instructor + "</p>";
        if (!this.restrictions.equals("")) {
            result += "<p>Restrictions: " + this.restrictions + "</p>";
        }
        return result;
    }
}
