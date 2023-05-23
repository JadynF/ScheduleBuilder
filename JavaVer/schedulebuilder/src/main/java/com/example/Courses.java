package com.example;
public class Courses {
    

    private String course;
    private String section;
    private String callNum;
    private Boolean open;
    private String openSeats;
    private String maxSeats;
    private String modality;
    private String days;
    private String time;
    private String location;
    private String instructor;
    private String restrictions;

    public Courses(String sec, String sub) {
        this.section = setSection(sec, sub);

    }

    public String setSection(String sec, String sub) {
        String[] parsed = sec.split("\\s+");
        if (sub.length() == 3) {
            this.course = parsed[0];
            return parsed[1];
        }
        else {
            this.course = parsed[0] + parsed[1];
            return parsed[2];
        }
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
        System.out.println(setting);
        if (setting.charAt(0) == ' ') {
            this.location = "Main Campus";
            return;
        }
        else if (setting.equals("To Be Arranged")) {
            this.location = "TBA";
            return;
        }

        String[] parsed = setting.split("\\s+");
        this.days = parsed[0];
        this.time = parsed[1];

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
        System.out.println(this.course);
        System.out.println(this.section);
        System.out.println(this.callNum);
        System.out.println(this.open);
        System.out.println(this.openSeats);
        System.out.println(this.maxSeats);
        System.out.println(this.modality);
        System.out.println(this.days);
        System.out.println(this.time);
        System.out.println(this.location);
        System.out.println(this.instructor);
    }
}
