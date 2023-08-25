var earliestHour = 8;
var earliestMin = 0;
var latestHour = -1;
var latestMin = -1;
var maxTR = -1;
var maxMWF = -1;
var minSeats = -1;
var virtual = true;
var honors = true;
var credExam = true;
var json = null;
var instructors = [];

var scheduleList = []; // array of schedules
var scheduleStartPoint = 0; // index of presented schedules
var callNumList = []; // array of call numbers

document.getElementById("applyFilter").addEventListener("click", applyFilter);

fetch('./courses.json') // fetch the offered courses and then handle the data
    .then(res => {
        return res.json();
    })
    .then(data => handleData(data));

window.onscroll = function() { // set listener for scroll action that loads new content once the bottom of the page is reached
    if ((window.innerHeight + Math.round(window.scrollY)) >= document.body.offsetHeight) {
        presentSchedules(schedulesList);
    }
};

function copyCallNums(id) {

    console.log("click");
}

function applyFilter() { // function that gets all filter values and handles the data

    scheduleStartPoint = 0;
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

    credExam = document.getElementById("allowCreditExams").checked; // get credit exams boolean

    handleData(json);
}

function handleData(data) { // function that takes json data of offered courses, and completely handles all schedule building and presenting

    json = data;

    console.log(json);

    table = document.getElementById("coursesTb");

    keys = Object.keys(data);

    nullCourses = []; // get each course with no offerings
    for (i in keys) {
        if (data[keys[i]] === "Null") {
            nullCourses.push(keys[i]);
        }
    }
    if (nullCourses.length > 0) { // if course has no offerings, show the user
        showHeader(0);
        presentNullCourses(nullCourses);
        return;
    }

    schedulesList = getSchedules(0, [], data, []); // build schedules

    showHeader(schedulesList.length); // show the amount of schedules

    if (data["No classes"] === true || schedulesList.length === 0) { // if no possible schedules
        presentNothing();
        return;
    }

    presentSchedules(schedulesList); // add schedules to html
}

function showHeader(numSchedules) { // function that changes the main header to show amount of schedule possibilities

    div = document.getElementById("centerBanner"); // reset header
    header = document.getElementById("centerHeader");
    header.remove();
    header = document.createElement("h1");
    header.setAttribute("id", "centerHeader");

    if (numSchedules > 1 || numSchedules === 0) { // plural cases
        header.appendChild(document.createTextNode(numSchedules + " Schedule Possibilities"));
    }
    else { // singular case
        header.appendChild(document.createTextNode(numSchedules + " Schedule Possibility"));
    }
    div.appendChild(header);
}

function presentNullCourses(nullCourses) { // function to show if course has no offerings

    // remove elements from page
    document.getElementById("tableHeaderBody").remove();
    document.getElementById("filtersDiv").remove();
    tableBody = document.getElementById("tableDataBody");
    if (tableBody != null) {
        tableBody.remove();
    }

    table = document.getElementById("coursesTb"); // create new table body
    tableDataBody = document.createElement("tbody");
    tableDataBody.setAttribute("id", "tableDataBody");

    headerRow = document.createElement("th");
    h2 = document.createElement("h2");
    h2.appendChild(document.createTextNode("The Following Course(s) Have No Offerings"));
    headerRow.appendChild(h2);
    tableDataBody.appendChild(headerRow);

    tr = document.createElement("tr");
    td = document.createElement("td");
    td.setAttribute("colspan", "8");
    h3 = document.createElement("h3");
    ul = document.createElement("ul");

    for (i in nullCourses) { // show list of courses with no offerings
        li = document.createElement("li")
        text = document.createTextNode(nullCourses[i])
        li.appendChild(text);
        ul.appendChild(li);
    }

    h3.appendChild(ul);
    td.appendChild(h3);
    tr.appendChild(td);
    tableDataBody.appendChild(tr);

    table.appendChild(tableDataBody);
}

function presentNothing() { // function to present when there are no schedule possibilities

    tableBody = document.getElementById("tableDataBody"); // reset table body
    if (tableBody != null) {
        tableBody.remove();
    }

    table = document.getElementById("coursesTb");
    tableDataBody = document.createElement("tbody");
    tableDataBody.setAttribute("id", "tableDataBody");

    row = document.createElement("tr");
    col = document.createElement("td");
    col.setAttribute("colspan", "8");

    h2 = document.createElement("h2");
    h2.appendChild(document.createTextNode("No Schedule Possibilities"));
    col.appendChild(h2);
    row.appendChild(col);
    tableDataBody.appendChild(row);
    table.appendChild(tableDataBody);

}

function presentSchedules(schedulesList) { // takes schedule list and adds needed schedules to html, will look at "scheduleStartPoint" to know what index of schedules to present

    tableDataBody = document.getElementById("tableDataBody"); // ensure there is a table body
    if (tableDataBody == null) {
        table = document.getElementById("coursesTb");
        tableDataBody = document.createElement("tbody");
        tableDataBody.setAttribute("id", "tableDataBody");
    }

    i = scheduleStartPoint;

    while (i < scheduleStartPoint + 100) { // for each schedule in scheduleList starting at scheduleStartPoint and ending at scheduleStartPoint + 100
        
        if (i >= schedulesList.length) { // if reached the end of scheduleList, break
            break;
        }

        newLable = document.createElement("tr"); // add schedule number and copy call number button to table
        newHeader = document.createElement("th");
        newHeader.setAttribute("rowspan", "" + (schedulesList[i].length + 1));
        newTextHeader = document.createElement("h2");
        text = document.createTextNode("#" + (i + 1));
        newTextHeader.appendChild(text);
        button = document.createElement("button");
        button.setAttribute("type", "button");
        button.setAttribute("class", "callNumsButton");
        buttonText = document.createTextNode("Copy Call Numbers");
        button.appendChild(buttonText);
        newTextHeader.appendChild(document.createElement("br"));
        newTextHeader.appendChild(button);
        newHeader.appendChild(newTextHeader);
        newLable.appendChild(newHeader);
        tableDataBody.appendChild(newLable);

        callNumList[i] = "";

        for (j in schedulesList[i]) { // for each course in schedule

            course = schedulesList[i][j];

            button.setAttribute("id", "callNums" + i);

            callNumList[i] += course["callNum"] + " ";

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
        i++;
    }

    scheduleStartPoint = i; // set new scheduleStartPoint
    table.appendChild(tableDataBody);

    var callNumButtons = document.getElementsByClassName("callNumsButton");

    for (i = 0; i < callNumButtons.length; i++) { // set event listener for all call number copy buttons
        callNumButtons[i].addEventListener("click", function() {
            num = parseInt(this.id.substring(8));
            navigator.clipboard.writeText(callNumList[num]); // set clipboard
        });
    }   
}

function getTime(course, currSet) { // function that takes a course and the current setting number, and returns time string to present on table

    time = course["days" + currSet] + " "; // add days to time text

    extraAfterZero = "";
    extraBeforeZero = "";
    if (course["startMin" + currSet] === 0) { // determine if an extra zero is needed after the start minute
        extraAfterZero = "0";
    }
    else if (course["startMin" + currSet] < 10) {
        extraBeforeZero = "0";
    }

    if (course["startHour" + currSet] > 12) { // for any time after 12 PM
        time += (course["startHour" + currSet] - 12) + ":" + extraBeforeZero + course["startMin" + currSet] + extraAfterZero + "PM - ";
    }
    else if (course["startHour" + currSet] === 12) { // for noon
        time += (course["startHour" + currSet]) + ":" + extraBeforeZero + course["startMin" + currSet] + extraAfterZero + "PM - ";
    }
    else { // for any time before 12 PM
        time += (course["startHour" + currSet]) + ":" + extraBeforeZero + course["startMin" + currSet] + extraAfterZero + "AM - ";
    }

    extraAfterZero = "";
    extraBeforeZero = "";
    if (course["endMin" + currSet] === 0) { // determine if extra zero is needed for end minute
        extraAfterZero = "0";
    }
    else if (course["endMin" + currSet] < 10) {
        extraBeforeZero = "0";
    }

    if (course["endHour" + currSet] > 12) {
        time += (course["endHour" + currSet] - 12) + ":" + extraBeforeZero + course["endMin" + currSet] + extraAfterZero + "PM";
    }
    else if (course["endHour" + currSet] === 12) {
        time += (course["endHour" + currSet]) + ":" + extraBeforeZero + course["endMin" + currSet] + extraAfterZero + "PM";
    }
    else {
        time += (course["endHour" + currSet]) + ":" + extraBeforeZero + course["endMin" + currSet] + extraAfterZero + "AM";
    }

    return time;
}

function getSchedules(i, schedule, coursesList, schedules) { // a recursive function that takes an int 0, an empty shedule array, the 2D array of courses, and an empty schedules array, and returns a built schedules list

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

    if (!credExam) {
        if (course["section"].includes("E") || course["restrictions"].includes("CREDIT EXAM")) {
            return false;
        }
    }

    if (minSeats != -1) { // check for minimum open seats
        if (course["openSeats"] < minSeats) {
            return false;
        }
    }

    if (!instructors.includes(course["instructor"])) { // add instructors 
        instructors.push(course["instructor"]);
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

function isRequiredSetting(course, schedule) { // function takes course JSON object, and current schedule, and returns false if settings conflict

    currMWF = 0; // keep track of number of classes per day
    currTR = 0;

    cNumSet = 0; // keep track of number of class times for the current course
    cDays = ""; // current course days

    while (cNumSet < course["numSettings"]) { // loop through each class time in the current course

        cDays = course["days" + cNumSet]; // get days course is offered per setting

        if (cDays === "") { // if no days are listed yet
            return true;
        }

        for (i in schedule) { // loop through each course in schedule

            if (schedule[i]["modality"].includes("Online")) { // continue if course is virtual
                continue;
            }
    
            sNumSet = 0; // keep track of number of class times for the current schedule course

            while (sNumSet < schedule[i]["numSettings"]) { // loop through each class time in the schedule course

                sDays = schedule[i]["days" + sNumSet]; // get days schedule course is offered per setting

                if (sDays === undefined) { // break if the course doesnt have days listed
                    break;
                }

                if (sDays.includes("M") || sDays.includes("W") || sDays.includes("F")) { // increment days counters
                    currMWF++;
                }
                else if (sDays.includes("T") || sDays.includes("R")) {
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
    
    // check the amount of classes per day
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
