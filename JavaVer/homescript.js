var courseCount = 2
document.getElementById("newCourseButton").addEventListener("click", newCourse);
document.getElementById("deleteCourseButton").addEventListener("click", delCourse);
document.getElementById("submit").addEventListener("click", load);
document.getElementById("closeLoader").addEventListener("click", closeLoader);

yearInput = document.getElementById("Year");
var loaderDiv = document.getElementById("loader");
currentYear = new Date().getFullYear();
yearInput.setAttribute("min", currentYear - 1);
yearInput.setAttribute("max", currentYear + 1);

function load() {
    if (noEmptyFields()) {
        loaderDiv.style.display = "block";
    }
}

function noEmptyFields() {
    elements = document.getElementById("coursesForm").getElementsByTagName("input");
    for (element in elements) {
        if (elements[element].id === "submit") 
            return true;
        if (elements[element].value === "") {
            return false;
        }
    }
}

function closeLoader() {
    loaderDiv.style.display = "none";
}

function newCourse() {
    var coursediv = document.getElementById("courseTb");
    var newRow = document.createElement("tr");
    newRow.setAttribute("class", "tableInput");
    var newCourse = document.createElement("input");
    var newDepartmentData = document.createElement("td");
    newCourse.setAttribute("type", "text");
    newCourse.setAttribute("id", "CourseName" + courseCount);
    newCourse.setAttribute("name", "CourseName" + courseCount);
    newCourse.setAttribute("required", "");
    newDepartmentData.appendChild(newCourse);
    newRow.appendChild(newDepartmentData);
    var newId = document.createElement("input");
    var newIdData = document.createElement("td");
    newId.setAttribute("type", "text");
    newId.setAttribute("id", "CourseID" + courseCount);
    newId.setAttribute("name", "CourseID" + courseCount);
    newId.setAttribute("required", "");
    newIdData.appendChild(newId);
    newRow.appendChild(newIdData);
    coursediv.appendChild(newRow);
    courseCount ++;
}

function delCourse() {
    if (courseCount === 2) {
        return;
    }
    var oldCourse = document.getElementById("CourseName" + (courseCount - 1));
    var tableData = oldCourse.parentElement;
    var tableRow = tableData.parentElement;
    tableRow.remove();
    courseCount --;
}