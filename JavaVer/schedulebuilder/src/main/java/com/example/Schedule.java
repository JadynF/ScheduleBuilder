package com.example;
import java.util.ArrayList;

public class Schedule {
    private ArrayList<ArrayList<Courses>> schedules;
    private int earliestClassHour;
    private int latestClassHour;
    private int maxTR;
    private int maxMWF;
    private String[][] rCourses;
    private String[][] rInstructor;

    public Schedule() {
        schedules = new ArrayList<ArrayList<Courses>>();
        this.earliestClassHour = 0;
        this.latestClassHour = -1;
        this.maxTR = -1;
        this.maxMWF = -1;
        this.rCourses = null;
        this.rInstructor = null;
    }

    public Schedule(int earliestStartHour, int latestEndHour, int maxClassTR, int maxClassMWF, String[][] requiredCourses, String[][] requiredInstructor) {
        schedules = new ArrayList<ArrayList<Courses>>();
        this.earliestClassHour = earliestStartHour;
        this.latestClassHour = latestEndHour;
        this.maxTR = maxClassTR;
        this.maxMWF = maxClassMWF;
        this.rCourses = requiredCourses;
        this.rInstructor = requiredInstructor;
    }

    public ArrayList<ArrayList<Courses>> getSchedules() {
        return this.schedules;
    }

    public void getSchedules(int i, ArrayList<Courses> schedule, ArrayList<ArrayList<Courses>> coursesList) { // passed an integer for number of unique courses, an empty list of courses, and the list of course lists, will build schedules
        if (i == coursesList.size()) {  // if looped through each unique course, then all desired courses are present on schedule
            ArrayList<Courses> newSchedule = new ArrayList<Courses>(); // copy schedule
            for (Courses c : schedule) {
                newSchedule.add(c);
            }
            this.schedules.add(newSchedule);
            return;
        }
        else {
            for (int j = 0; j < coursesList.get(i).size(); j++) { // loop through the "i"th course
                if (compatable(coursesList.get(i).get(j), schedule)) { // if schedule is empty, create new schedule, if not empty, check if course at (i, j) is compatable with courses present in schedule
                    schedule.add(coursesList.get(i).get(j)); 
                    getSchedules(i + 1, schedule, coursesList); // go on to next course list in courseList
                    schedule.remove(i); // remove course from schedule once done
                }
            }
            return;
        }
    }

    public Boolean compatable(Courses c, ArrayList<Courses> schedule) { // when passed a Courses object and an array of Courses, will return true if their times dont overlap, false if they do overlap
        
        // loop through required courses, return false for courses with same subject, course, but different section
        if (this.rCourses != null) {
            if (!isRequiredSection(c))
                return false;
        }
        
        // check required instructor
        if (this.rInstructor != null) {
            if (!isRequiredInstructor(c))
                return false;
        }

        // check earliest start time
        if (c.getHourStart() < this.earliestClassHour)
            return false;

        // check latest end time
        if (this.latestClassHour != -1 && c.getHourEnd() > this.latestClassHour)
            return false;

        if (!isRequiredSetting(c, schedule))
            return false;
        
        return true;
    }

    private boolean isRequiredSection(Courses c) {
        for (int i = 0; i < this.rCourses.length; i++) {
            if ((this.rCourses[i][0] + this.rCourses[i][1]).equals(c.getSubject() + c.getCourse())) {
                if (!this.rCourses[i][2].equals(c.getSection()))
                    return false;
            }
        }
        return true;
    }

    private boolean isRequiredInstructor(Courses c) {
        for (int i = 0; i < this.rInstructor.length; i++) {
            if (this.rInstructor[i][0].equals(c.getSubject() + c.getCourse())) {
                if (!this.rInstructor[i][1].equals(c.getInstructor()))
                    return false;
            }
        }
        return true;
    }

    private boolean isRequiredSetting(Courses c, ArrayList<Courses> schedule) {
        int currMWF = 0;
        int currTR = 0;

        // check time compatability
        String cDays = c.getDays();
        for (int i = 0; i < schedule.size(); i++) { // loop through each course in current schedule
            String sDays = schedule.get(i).getDays();
            if (sDays.contains("M") || sDays.contains("W") || sDays.contains("F"))
                currMWF++;
            else if (sDays.contains("T") || sDays.contains("R"))
                currTR++;
            Boolean shareDays = false; // check to see if the two courses share the same day
            for (int j = 0; j < cDays.length(); j++) {
                for (int k = 0; k < sDays.length(); k++) {
                    if (cDays.charAt(j) == sDays.charAt(k)) 
                        shareDays = true;
                }
            }
            if (shareDays) { // if on same day
                if (c.getHourStart() > schedule.get(i).getHourStart() && c.getHourStart() <= schedule.get(i).getHourEnd()) { // check to see if new course starts during current course hour
                    if (c.getHourStart() == schedule.get(i).getHourEnd() && c.getMinuteStart() > schedule.get(i).getMinuteEnd()) // check to see, if on same hour, if new course starts after current minute
                        continue;
                    return false;
                }
                else if (schedule.get(i).getHourStart() > c.getHourStart() && schedule.get(i).getHourStart() <= c.getHourEnd()) { // check to see if current course starts during new course hour
                    if (schedule.get(i).getHourStart() == c.getHourEnd() && c.getMinuteEnd() < schedule.get(i).getMinuteStart()) // if on same hour, if current course starts after new course minute
                        continue;
                    return false;
                }
                else if (c.getHourStart() == schedule.get(i).getHourStart()) { // if starting at same time
                    return false;
                }
            }
        }

        // check if day is already full
        if (this.maxMWF != -1) {
            if ((cDays.contains("M") || cDays.contains("W") || cDays.contains("F")) && (currMWF >= this.maxMWF))
                return false;
        }
        if (this.maxTR != -1) {
            if ((cDays.contains("T") || cDays.contains("R")) && (currTR >= this.maxTR))
                return false;
        }
        return true;
    }
}
