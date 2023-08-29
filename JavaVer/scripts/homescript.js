var courseCount = 2 // keep count of the number of courses added
document.getElementById("newCourseButton").addEventListener("click", newCourse);
document.getElementById("deleteCourseButton").addEventListener("click", delCourse); 
document.getElementById("submit").addEventListener("click", load); 
document.getElementById("closeLoader").addEventListener("click", closeLoader); 

var loaderDiv = document.getElementById("loader");
currentYear = new Date().getFullYear();
addYearSelect(); // add the select options for the year input

function addYearSelect() { // funciton to add the years to the year input
    yearSelect = document.getElementById("Year");
    i = -1;
    while (i < 2) {
        year = currentYear + i; // add the current year, the year before, and the year after
        option = document.createElement("option");
        option.setAttribute("value", "" + year);
        option.appendChild(document.createTextNode("" + year));
        yearSelect.appendChild(option);
        i++;
    }

}

function load() { // function to bring up the loader
    if (noEmptyFields()) { // check if the form has been completed
        loaderDiv.style.display = "block"; // display the loader
    }
}

function noEmptyFields() { // function to check if the form has been completed
    elements = document.getElementById("coursesForm").getElementsByTagName("input");
    for (element in elements) {
        if (elements[element].id === "submit") // last input element, so exit
            return true;
        if (elements[element].value === "") { // if element is empty
            return false;
        }
    }
}

function closeLoader() { // function to hide the loader
    loaderDiv.style.display = "none";
}

function newCourse() { // function to add new course input fields

    coursediv = document.getElementById("courseTb"); // add new row and column to table
    newRow = document.createElement("tr");
    newRow.setAttribute("class", "tableInput");
    newDepartmentData = document.createElement("td");

    newCourse = document.createElement("input"); // add new department input
    newCourse.setAttribute("type", "text");
    newCourse.setAttribute("id", "CourseName" + courseCount);
    newCourse.setAttribute("name", "CourseName" + courseCount);
    newCourse.setAttribute("required", "");
    newDepartmentData.appendChild(newCourse);
    newRow.appendChild(newDepartmentData);

    newId = document.createElement("input"); // add new course id input
    newIdData = document.createElement("td");
    newId.setAttribute("type", "text");
    newId.setAttribute("id", "CourseID" + courseCount);
    newId.setAttribute("name", "CourseID" + courseCount);
    newId.setAttribute("required", "");
    newIdData.appendChild(newId);
    newRow.appendChild(newIdData);

    coursediv.appendChild(newRow);
    courseCount++;
}

function delCourse() { // function to delete a course

    if (courseCount === 2) { // do nothing if only 1 course left
        return;
    }

    var oldCourse = document.getElementById("CourseName" + (courseCount - 1));
    var tableData = oldCourse.parentElement;
    var tableRow = tableData.parentElement;
    tableRow.remove(); // remove entire row of course input
    courseCount--;
}