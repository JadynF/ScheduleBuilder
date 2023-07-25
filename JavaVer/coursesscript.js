var earliestHour = 8;
var latestHour = -1;
var maxTR = -1;
var maxMWF = -1;
var requiredC = null;
var requiredI = null;
var prohibitedI = null;
var virtual = true;
var honors = true;
var json = null;

document.getElementById("applyFilter").addEventListener("click", applyFilter);

fetch('./courses.json')
    .then(res => {
        return res.json();
    })
    .then(data => handleData(data));

function applyFilter() {
    document.getElementById("tableDataBody").remove();
    earliestHour = document.getElementById("earliestHour").value;
    earliestHour = parseInt(earliestHour);
    latestHour = document.getElementById("latestHour").value;
    latestHour = parseInt(latestHour);
    if (latestHour === "") {
        latestHour = -1;
    }
    else if (latestHour < 8) {
        latestHour += 12;
    }
    maxTR = document.getElementById("maxTR").value;
    maxTR = parseInt(maxTR);
    if (maxTR === "") {
        maxTR = -1;
    }
    maxMWF = document.getElementById("maxMWF").value;
    maxMWF = parseInt(maxMWF);
    if (maxMWF === "") {
        maxMWF = -1;
    }
    virtual = document.getElementById("allowVirtual").checked;
    honors = document.getElementById("allowHonors").checked;
    handleData(json);
}

function handleData(data) {

    json = data;
    console.log(data);
    schedulesList = getSchedules(0, [], data, []);
    //for (i in schedulesList) {
    //    console.log("___________________________________")
    //    schedule = schedulesList[i];
    //    for (course in schedule) {
    //        console.log(schedule[course]);
    //    }
    //}
    presentSchedules(schedulesList);
}

function presentSchedules(schedulesList) {
    table = document.getElementById("coursesTb");
    tableDataBody = document.createElement("tbody");
    tableDataBody.setAttribute("id", "tableDataBody");

    count = 1;
    for (i in schedulesList) {
        newLable = document.createElement("tr");
        newHeader = document.createElement("th");
        newHeader.setAttribute("rowspan", "" + (schedulesList[i].length + 1));
        newTextHeader = document.createElement("h2");
        text = document.createTextNode("#" + count++);
        newTextHeader.appendChild(text);
        newHeader.appendChild(newTextHeader);
        newLable.appendChild(newHeader);
        tableDataBody.appendChild(newLable);

        for (j in schedulesList[i]) {
            course = schedulesList[i][j];

            newDataRow = document.createElement("tr");

            nameData = document.createElement("td");
            text = document.createTextNode(course["name"] + "-" + course["section"]);
            nameData.appendChild(text);

            callData = document.createElement("td");
            text = document.createTextNode(course["callNum"]);
            callData.appendChild(text);

            k = 0;
            timeData = document.createElement("td");
            while (k < course["numSettings"]) {
                if (k != 0) {
                    timeData.appendChild(document.createElement("br"));
                }
                time = getTime(course, k);
                text = document.createTextNode(time);
                timeData.appendChild(text);
                k++;
            }

            locationData = document.createElement("td");
            cModality = course["modality"];
            text = document.createTextNode(cModality);
            locationData.appendChild(text);
            if (course["modality"] != "Asynchronous Online") {
                locationData.appendChild(document.createElement("br"));
                cLocation = course["location"];
                text = document.createTextNode(cLocation);
                locationData.appendChild(text);
            }

            instructorData = document.createElement("td");
            instructor = course["instructor"];
            if (instructor === "STAFF T") {
                instructor = "TBA";
            }
            text = document.createTextNode(instructor);
            instructorData.appendChild(text);

            seatData = document.createElement("td");
            text = document.createTextNode("Closed");
            if (course["open"] == true) {
                text = document.createTextNode(course["openSeats"] + "/" + course["maxSeats"] + " Seats Open");
            }
            if (course["maxSeats"] === 0) {
                text = document.createTextNode("TBA");
            }
            seatData.appendChild(text);

            restrictionsData = document.createElement("td");
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
        dividerRow = document.createElement("tr");
        dividerData = document.createElement("td");
        dividerData.setAttribute("colspan", "8");
        divider = document.createElement("hr");
        dividerData.appendChild(divider);
        dividerRow.appendChild(dividerData);
        tableDataBody.appendChild(dividerRow);
    }
    table.appendChild(tableDataBody);
}

function getTime(course, num) {

    time = course["days" + num] + " ";
    extraZero = "";
    if (course["startMin" + num] === 0) {
        extraZero = "0";
    }

    if (course["startHour" + num] > 12) {
        time += (course["startHour" + num] - 12) + ":" + course["startMin" + num] + extraZero + "PM - ";
    }
    else if (course["startHour" + num] === 12) {
        time += (course["startHour" + num]) + ":" + course["startMin" + num] + extraZero + "PM - ";
    }
    else {
        time += (course["startHour" + num]) + ":" + course["startMin" + num] + extraZero + "AM - ";
    }

    extraZero = "";
    if (course["endMin" + num] === 0) {
        extraZero = "0";
    }

    if (course["endHour" + num] > 12) {
        time += (course["endHour" + num] - 12) + ":" + course["endMin" + num] + extraZero + "PM";
    }
    else if (course["endHour" + num] === 12) {
        time += (course["endHour" + num]) + ":" + course["endMin" + num] + extraZero + "PM";
    }
    else {
        time += (course["endHour" + num]) + ":" + course["endMin" + num] + extraZero + "AM";
    }

    return time;
}

function getSchedules(i, schedule, coursesList, schedules) { // function that builds schedules, takes integer 0, an empty shedule array, and the 2D array of courses
    if (i == Object.keys(coursesList).length) {
        newSchedule = JSON.parse(JSON.stringify(schedule));
        schedules.push(newSchedule);
        return;
    }
    else {
        for (let j = 0; j < Object.keys(coursesList[Object.keys(coursesList)[i]]).length; j++) { // loop through amount of classes for each offered course
            if (compatable(coursesList[Object.keys(coursesList)[i]][Object.keys(coursesList[Object.keys(coursesList)[i]])[j]], schedule)) {
                course = coursesList[Object.keys(coursesList)[i]][Object.keys(coursesList[Object.keys(coursesList)[i]])[j]];
                schedule.push(course);
                getSchedules(i + 1, schedule, coursesList, schedules); // move on to next class type
                schedule.splice(i, 1);
            }
        }
        if (i == 0) { // if finished looping through first set, then all possibilities have been found
            return schedules;
        }
        return;
    }
}

function compatable(course, schedule) {
    if (requiredC != null) {
        if (!isRequiredSection(course)) {
            return false;
        }
    }

    if (requiredI != null) {
        if (!isRequiredInstructor(course)) {
            return false;
        }
    }

    if (prohibitedI != null) {
        if (isProhibitedInstructor(course)) {
            return false;
        }
    }

    if (!honors) {
        if (course["honors"]) {
            //console.log("1");
            return false;
        }
    }

    if (course["modality"] === "Asynchronous Online") {
        if (virtual) {
            return true;
        }
        else {
            //console.log("2");
            return false;
        }
    }

    i = 0;
    while (i < course["numSettings"]) {
        if (course["startHour" + i] < earliestHour) {
            //console.log("3");
            return false;
        }
        i++;
    }

    i = 0;
    while (i < course["numSettings"]) {
        if (latestHour != -1 && course["endHour" + i] > latestHour) {
            //console.log("4");
            return false;
        }
        i++;
    }

    if (!isRequiredSetting(course, schedule)) {
        //console.log("5");
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

function isRequiredSetting(course, schedule) {

    //console.log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    //console.log(course);
    //console.log("===================");
    //for (i in schedule) {
    //    console.log(schedule[i]);
    //}

    currMWF = 0;
    currTR = 0;

    cNumSet = 0;
    cDays = "";

    while (cNumSet < course["numSettings"]) {
        cDays = course["days" + cNumSet];
        if (cDays === "") {
            console.log("empty");
            return true;
        }

        for (i in schedule) {

            if (schedule[i]["modality"].includes("Online")) {
                continue;
            }
    
            sNumSet = 0;
            while (sNumSet < schedule[i]["numSettings"]) {
                sDays = schedule[i]["days" + sNumSet];
                if (sDays === undefined) {
                    break;
                }

                if (sDays.includes("M") || sDays.includes("W") || sDays.includes("F")) {
                    currMWF++;
                }
                else if (sDays.includes("T") || sDays.contains("R")) {
                    currTR++;
                }
                shareDays = false;
                for (j in cDays) {
                    for (k in sDays) {
                        if (cDays[j] === sDays[k]) {
                            shareDays = true;
                        }
                    }
                }
    
                if (shareDays) {
                    if (course["startHour" + cNumSet] > schedule[i]["startHour" + sNumSet] && course["startHour" + cNumSet] <= schedule[i]["endHour" + sNumSet]) {
                        //console.log(1);
                        if (course["startHour" + cNumSet] === schedule[i]["endHour" + sNumSet] && course["startMin" + cNumSet] > schedule[i]["endMin" + sNumSet]) {
                            //console.log(2);
                            continue;
                        }
                        return false;
                    }
                    else if (schedule[i]["startHour" + sNumSet] > course["startHour" + cNumSet] && schedule[i]["startHour" + sNumSet] <= course["endHour" + cNumSet]) {
                        //console.log(3);
                        if (schedule[i]["startHour" + sNumSet] === course["endHour" + cNumSet] && course["endMin" + cNumSet] < schedule[i]["startMin" + sNumSet]) {
                            //console.log(4);
                            continue;
                        }
                        return false;
                    }
                    else if (course["startHour" + cNumSet] === schedule[i]["startHour" + sNumSet]) {
                        //console.log(5);
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
