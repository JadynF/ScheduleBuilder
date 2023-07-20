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

    //console.log(data);
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

            time = getTime(course);
            timeData = document.createElement("td");
            text = document.createTextNode(time);
            timeData.appendChild(text);

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

function getTime(course) {
    if (course["days"] === undefined || course["startHour"] === 0 || course["modality"] === "Asynchronous Online") {
        return "No Time";
    }

    time = course["days"] + " ";
    extraZero = "";
    if (course["startMin"] === 0) {
        extraZero = "0";
    }

    if (course["startHour"] > 12) {
        time += (course["startHour"] - 12) + ":" + course["startMin"] + extraZero + "PM - ";
    }
    else if (course["startHour"] === 12) {
        time += (course["startHour"]) + ":" + course["startMin"] + extraZero + "PM - ";
    }
    else {
        time += (course["startHour"]) + ":" + course["startMin"] + extraZero + "AM - ";
    }

    extraZero = "";
    if (course["endMin"] === 0) {
        extraZero = "0";
    }

    if (course["endHour"] > 12) {
        time += (course["endHour"] - 12) + ":" + course["endMin"] + extraZero + "PM";
    }
    else if (course["endHour"] === 12) {
        time += (course["endHour"]) + ":" + course["endMin"] + extraZero + "PM";
    }
    else {
        time += (course["endHour"]) + ":" + course["endMin"] + extraZero + "AM";
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
            return false;
        }
    }

    if (course["modality"] === "Asynchronous Online") {
        if (virtual) {
            return true;
        }
        else {
            return false;
        }
    }

    if (course["startHour"] < earliestHour) {
        return false;
    }

    console.log(latestHour);
    console.log(course["endHour"]);
    if (latestHour != -1 && course["endHour"] > latestHour) {
        return false;
    }

    if (!isRequiredSetting(course, schedule)) {
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

    cDays = course["days"];
    for (i in schedule) {

        if (schedule[i]["modality"] === "Asynchronous Online") {
            continue;
        }

        sDays = schedule[i]["days"];
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
            if (course["startHour"] > schedule[i]["startHour"] && course["startHour"] <= schedule[i]["endHour"]) {
                //console.log(1);
                if (course["startHour"] === schedule[i]["endHour"] && course["startMin"] > schedule[i]["endMin"]) {
                    //console.log(2);
                    continue;
                }
                return false;
            }
            else if (schedule[i]["startHour"] > course["startHour"] && schedule[i]["startHour"] <= course["endHour"]) {
                //console.log(3);
                if (schedule[i]["startHour"] === course["endHour"] && course["endMin"] < schedule[i]["startMin"]) {
                    //console.log(4);
                    continue;
                }
                return false;
            }
            else if (course["startHour"] === schedule[i]["startHour"]) {
                //console.log(5);
                return false;
            }
        }
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
