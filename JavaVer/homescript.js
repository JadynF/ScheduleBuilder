var courseCount = 2
document.getElementById("newCourseButton").addEventListener("click", newCourse);
document.getElementById("deleteCourseButton").addEventListener("click", delCourse);

function newCourse() {
    var coursediv = document.getElementById("courseTb");
    var newRow = document.createElement("tr");
    newRow.setAttribute("class", "tableInput");
    var newCourse = document.createElement("input");
    var newDepartmentData = document.createElement("td");
    newCourse.setAttribute("type", "text");
    newCourse.setAttribute("id", "CourseName" + courseCount);
    newCourse.setAttribute("name", "CourseName" + courseCount);
    newDepartmentData.appendChild(newCourse);
    newRow.appendChild(newDepartmentData);
    var newId = document.createElement("input");
    var newIdData = document.createElement("td");
    newId.setAttribute("type", "text");
    newId.setAttribute("id", "CourseID" + courseCount);
    newId.setAttribute("name", "CourseID" + courseCount);
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