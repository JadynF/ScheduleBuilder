package com.example;
import java.util.ArrayList;
import java.util.HashMap;

public class Schedule {
    private ArrayList<ArrayList<Courses>> schedules;
    private int earliestClassHour;
    private int maxTR;
    private int maxMWF;
    //private HashMap<String, String> rCourses;

    public Schedule() {
        schedules = new ArrayList<ArrayList<Courses>>();
        this.earliestClassHour = 0;
        this.maxTR = -1;
        this.maxMWF = -1;
        //this.rCourses = null;
    }

    public Schedule(int earliestStartHour, int maxClassTR, int maxClassMWF) {//, HashMap<String, String> requiredCourses) {
        schedules = new ArrayList<ArrayList<Courses>>();
        this.earliestClassHour = earliestStartHour;
        this.maxTR = maxClassTR;
        this.maxMWF = maxClassMWF;
        //this.rCourses = requiredCourses;
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
        //System.out.println(c.getSubject());
        //System.out.println(c.getCourse());
        //System.out.println(c.getSection());
        //System.out.println(this.rCourses.containsValue(c.getSection()));
        //System.out.println(this.rCourses.containsKey(c.getSubject() + "-" + c.getCourse()));
        //String sAndC = c.getSubject() + "-" + c.getCourse();
        //System.out.println(this.rCourses.get(c.getCourse() + "-" + c.getCourse()));
        //if (this.rCourses.containsKey(c.getSubject() + "-" + c.getCourse())) {
        //    if (!this.rCourses.get(c.getCourse() + "-" + c.getCourse()).equals(c.getSection())) {
        //        return false;
        //    }
        //}
        int currMWF = 0;
        int currTR = 0;
        if (c.getHourStart() < this.earliestClassHour)
            return false;
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
