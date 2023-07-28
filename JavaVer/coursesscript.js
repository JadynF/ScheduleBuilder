var earliestHour = 8;
var earliestMin = 0;
var latestHour = -1;
var latestMin = -1;
var maxTR = -1;
var maxMWF = -1;
var requiredC = null;
var requiredI = null;
var prohibitedI = null;
var minSeats = -1;
var virtual = true;
var honors = true;
var json = null;
var instructors = [];

document.getElementById("applyFilter").addEventListener("click", applyFilter);

fetch('./courses.json')
    .then(res => {
        return res.json();
    })
    .then(data => handleData(data));

function applyFilter() {
    document.getElementById("tableDataBody").remove(); // reset table

    earliestTime = document.getElementById("earliestHour").value.split(":"); // get earliest time filter
    earliestHour = parseInt(earliestTime[0]);
    earliestMin = parseInt(earliestTime[1]);
    if (isNaN(earliestHour))
        earliestHour = 8;
    if (isNaN(earliestMin))
        earliestMin = 0;

    latestTime = document.getElementById("latestHour").value.split(":"); // get latest time filter
    latestHour = parseInt(latestTime[0]);
    latestMin = parseInt(latestTime[1]);
    if (isNaN(latestHour))
        latestHour = -1;
    if (isNaN(latestMin))
        latestMin = -1;

    maxTR = parseInt(document.getElementById("maxTR").value); // get max amount of TR classes
    if (isNaN(maxTR))
        maxTR = -1;

    maxMWF = parseInt(document.getElementById("maxMWF").value); // get max amount of MWF classes
    if (isNaN(maxMWF))
        maxMWF = -1;

    minSeats = parseInt(document.getElementById("minSeats").value);
    if (isNaN(minSeats))
        minSeats = -1;

    virtual = document.getElementById("allowVirtual").checked; // get virtual boolean

    honors = document.getElementById("allowHonors").checked; // get honors boolean

    handleData(json);
}

function handleData(data) {

    console.log(data);

    json = data;

    schedulesList = getSchedules(0, [], data, []); // build schedules

    console.log(instructors);

    presentSchedules(schedulesList); // add schedules to html
}

function presentSchedules(schedulesList) { // takes schedule list and adds to html

    table = document.getElementById("coursesTb");
    tableDataBody = document.createElement("tbody");
    tableDataBody.setAttribute("id", "tableDataBody");

    count = 1;
    for (i in schedulesList) { // for each schedule in scheduleList

        newLable = document.createElement("tr"); // add schedule number to table
        newHeader = document.createElement("th");
        newHeader.setAttribute("rowspan", "" + (schedulesList[i].length + 1));
        newTextHeader = document.createElement("h2");
        text = document.createTextNode("#" + count++);
        newTextHeader.appendChild(text);
        newHeader.appendChild(newTextHeader);
        newLable.appendChild(newHeader);
        tableDataBody.appendChild(newLable);

        for (j in schedulesList[i]) { // for each course in schedule

            course = schedulesList[i][j];

            newDataRow = document.createElement("tr");

            nameData = document.createElement("td"); // add course name to table
            text = document.createTextNode(course["name"] + "-" + course["section"]);
            nameData.appendChild(text);

            callData = document.createElement("td"); // add call number to table
            text = document.createTextNode(course["callNum"]);
            callData.appendChild(text);

            k = 0;
            timeData = document.createElement("td");
            while (k < course["numSettings"]) { // loop through each time for course
                if (k != 0) { // create border between times
                    timeData.appendChild(document.createElement("br")); 
                }
                time = getTime(course, k); // build time text
                text = document.createTextNode(time);
                timeData.appendChild(text);
                k++;
            }

            locationData = document.createElement("td"); // add modality
            cModality = course["modality"];
            text = document.createTextNode(cModality);
            locationData.appendChild(text);
            if (course["modality"] != "Asynchronous Online") { // add location, if it has one
                locationData.appendChild(document.createElement("br"));
                cLocation = course["location"];
                text = document.createTextNode(cLocation);
                locationData.appendChild(text);
            }

            instructorData = document.createElement("td"); // add instructor
            instructor = course["instructor"];
            if (instructor === "STAFF T") {
                instructor = "TBA";
            }
            text = document.createTextNode(instructor);
            instructorData.appendChild(text);

            seatData = document.createElement("td"); // add status
            text = document.createTextNode("Closed");
            if (course["open"] == true) {
                text = document.createTextNode(course["openSeats"] + "/" + course["maxSeats"] + " Seats Open");
                if (course["maxSeats"] === 0) { // if no seats listed yet
                    text = document.createTextNode("Open");
                }
            }
            seatData.appendChild(text);

            restrictionsData = document.createElement("td"); // add restrictions
            text = document.createTextNode(course["restrictions"]);
            restrictionsData.appendChild(text);

            newDataRow.appendChild(nameData);
            newDataRow.appendChild(callData);
            newDataRow.appendChild(timeData);
            newDataRow.appendChild(locationData);
            newDataRow.appendChild(instructorData);
            newDataRow.appendChild(seatData);
            newDataRow.appendChild(restrictionsData);
            tableDataBody.appendChild(newDataRow);
        }
        dividerRow = document.createElement("tr"); // border between schedule
        dividerData = document.createElement("td");
        dividerData.setAttribute("colspan", "8");
        divider = document.createElement("hr");
        dividerData.appendChild(divider);
        dividerRow.appendChild(dividerData);
        tableDataBody.appendChild(dividerRow);
    }
    table.appendChild(tableDataBody);
}

function getTime(course, currSet) { // function that takes a course and the current setting number, and returns time string to present on table

    time = course["days" + currSet] + " "; // add days to time text

    extraZero = "";
    if (course["startMin" + currSet] === 0) { // determine if an extra zero is needed for the start minute
        extraZero = "0";
    }

    if (course["startHour" + currSet] > 12) { // for any time after 12 PM
        time += (course["startHour" + currSet] - 12) + ":" + course["startMin" + currSet] + extraZero + "PM - ";
    }
    else if (course["startHour" + currSet] === 12) { // for noon
        time += (course["startHour" + currSet]) + ":" + course["startMin" + currSet] + extraZero + "PM - ";
    }
    else { // for any time before 12 PM
        time += (course["startHour" + currSet]) + ":" + course["startMin" + currSet] + extraZero + "AM - ";
    }

    extraZero = "";
    if (course["endMin" + currSet] === 0) { // determine if extra zero is needed for end minute
        extraZero = "0";
    }

    if (course["endHour" + currSet] > 12) {
        time += (course["endHour" + currSet] - 12) + ":" + course["endMin" + currSet] + extraZero + "PM";
    }
    else if (course["endHour" + currSet] === 12) {
        time += (course["endHour" + currSet]) + ":" + course["endMin" + currSet] + extraZero + "PM";
    }
    else {
        time += (course["endHour" + currSet]) + ":" + course["endMin" + currSet] + extraZero + "AM";
    }

    return time;
}

function getSchedules(i, schedule, coursesList, schedules) { // function that takes an int 0, an empty shedule array, the 2D array of courses, and an empty schedules array, and returns a built schedules list

    if (i == Object.keys(coursesList).length) { // if i === the length of the coursesList, then all the desired classes have been added
        newSchedule = JSON.parse(JSON.stringify(schedule));
        schedules.push(newSchedule); // push current schedule to schedules list
        return; // go back to last course and continue
    }
    else {
        for (let j = 0; j < Object.keys(coursesList[Object.keys(coursesList)[i]]).length; j++) { // loop through amount of classes for each offered course
            if (compatable(coursesList[Object.keys(coursesList)[i]][Object.keys(coursesList[Object.keys(coursesList)[i]])[j]], schedule)) { // check if the current course is compatable with current schedule
                course = coursesList[Object.keys(coursesList)[i]][Object.keys(coursesList[Object.keys(coursesList)[i]])[j]];
                schedule.push(course); // add current course to schedule
                getSchedules(i + 1, schedule, coursesList, schedules); // move on to next class type
                schedule.splice(i, 1); // remove current course from schedule 
            }
        }
        if (i == 0) { // if finished looping through first set, then all possibilities have been found
            return schedules;
        }
        return; // when finished with course loop, go back to parent course and continue
    }
}

function compatable(course, schedule) { // a function that takes a course as JSON object, and the current schedule, and returns true if they are compatable

    if (minSeats != -1) {
        if (course["openSeats"] < minSeats) {
            return false;
        }
    }

    if (!instructors.includes(course["instructor"])) {
        instructors.push(course["instructor"]);
    }

    if (requiredC != null) { // check for required courses
        if (!isRequiredSection(course)) {
            return false;
        }
    }

    if (requiredI != null) { // check for required instructors
        if (!isRequiredInstructor(course)) {
            return false;
        }
    }

    if (prohibitedI != null) { // check for prohibited instructors
        if (isProhibitedInstructor(course)) {
            return false;
        }
    }

    if (!honors) { // check honors filter
        if (course["honors"]) {
            return false;
        }
    }

    if (course["modality"] === "Asynchronous Online") { // check for virtual filter
        if (virtual) {
            return true; // no need to check times if course is virtual and virtual courses are allowed
        }
        else {
            return false;
        }
    }

    i = 0;
    while (i < course["numSettings"]) { // check for earliest hour
        startHour = course["startHour" + i];
        if (startHour < 8) {
            startHour += 12;
        }
        if (startHour < earliestHour) { 
            return false;
        }
        else if (startHour == earliestHour && course["startMin" + i] < earliestMin) {
            return false;
        }
        i++;
    }

    i = 0;
    while (i < course["numSettings"]) { // check for latest hour
        endHour = course["endHour" + i];
        if (endHour < 8) {
            endHour += 12;
        }
        if (latestHour != -1) {
            if (endHour > latestHour) {
                return false;
            }
            else if (endHour == latestHour && course["endMin" + i] > latestMin) {
                return false;
            }
        }
        i++;
    }

    if (!isRequiredSetting(course, schedule)) { // check for conflicting times in courses, and max days filters
        return false;
    }

    return true;
}

function isRequiredSection(course) {

}

function isRequiredInstructor(course) {

}

function isProhibitedInstructor(course) {

}

function isRequiredSetting(course, schedule) { // function takes course JSON object, and current schedule, and returns false if settings conflict

    currMWF = 0; // keep track of number of classes per day
    currTR = 0;

    cNumSet = 0;
    cDays = "";

    while (cNumSet < course["numSettings"]) { // loop through each setting in the course

        cDays = course["days" + cNumSet]; // get days course is offered per setting

        if (cDays === "") { // if no days are listed yet
            return true;
        }

        for (i in schedule) { // loop through each course in schedule

            if (schedule[i]["modality"].includes("Online")) { // continue if course is virtual
                continue;
            }
    
            sNumSet = 0;
            while (sNumSet < schedule[i]["numSettings"]) { // loop through each setting in the schedule course

                sDays = schedule[i]["days" + sNumSet]; // get days schedule course is offered per setting

                if (sDays === undefined) { // break if the course doesnt have days listed
                    break;
                }

                if (sDays.includes("M") || sDays.includes("W") || sDays.includes("F")) { // increment days counters
                    currMWF++;
                }
                else if (sDays.includes("T") || sDays.contains("R")) {
                    currTR++;
                }

                shareDays = false;
                for (j in cDays) { // see if the 2 courses share days
                    for (k in sDays) {
                        if (cDays[j] === sDays[k]) {
                            shareDays = true;
                        }
                    }
                }
    
                if (shareDays) { // if they share days, check for conflicting times

                    currStartHour = course["startHour" + cNumSet];
                    currStartMin = course["startMin" + cNumSet];
                    currEndHour = course["endHour" + cNumSet];
                    currEndMin = course["endMin" + cNumSet];

                    schedStartHour = schedule[i]["startHour" + sNumSet];
                    schedStartMin = schedule[i]["startMin" + sNumSet];
                    schedEndHour = schedule[i]["endHour" + sNumSet];
                    schedEndMin = schedule[i]["endMin" + sNumSet];

                    if (currStartHour > schedStartHour && currStartHour <= schedEndHour) {
                        if (currStartHour === schedEndHour && currStartMin > schedEndMin) {
                            sNumSet++;
                            continue;
                        }
                        return false;
                    }
                    else if (schedStartHour > currStartHour && schedStartHour <= currEndHour) {
                        if (schedStartHour === currEndHour && currEndMin < schedStartMin) {
                            sNumSet++;
                            continue;
                        }
                        return false;
                    }
                    else if (currStartHour === schedStartHour) {
                        return false;
                    }
                }
                sNumSet++;
            }
        }
        cNumSet++;
    }
    
    if (maxMWF != -1) {
        if ((cDays.includes("M") || cDays.includes("W") || cDays.includes("F")) && (currMWF >= maxMWF)) {
            return false;
        }
    }
    if (maxTR != -1) {
        if ((cDays.includes("T") || cDays.includes("R")) && (currTR >= maxTR)) {
            return false;
        }
    }

    return true;
}