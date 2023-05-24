package com.example;
import java.util.ArrayList;

public class Schedule {
    private ArrayList<ArrayList<Courses>> schedules;

    public Schedule() {
        schedules = new ArrayList<ArrayList<Courses>>();
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
                if (schedule.size() == 0 || compatable(coursesList.get(i).get(j), schedule)) { // if schedule is empty, create new schedule, if not empty, check if course at (i, j) is compatable with courses present in schedule
                    schedule.add(coursesList.get(i).get(j)); 
                    getSchedules(i + 1, schedule, coursesList); // go on to next course list in courseList
                    schedule.remove(0); // remove course from schedule once done
                }
            }
            return;
        }
    }

    public Boolean compatable(Courses c, ArrayList<Courses> schedule) { // when passed a Courses object and an array of Courses, will return true if their times dont overlap, false if they do overlap
        for (int i = 0; i < schedule.size(); i++) { // loop through each course in current schedule
            String cDays = c.getDays();
            String sDays = schedule.get(i).getDays();
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
        return true;
    }
}
