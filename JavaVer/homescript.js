var courseCount = 2
document.getElementById("newCourseButton").addEventListener("click", newCourse);
document.getElementById("deleteCourseButton").addEventListener("click", delCourse);

var coursediv = document.getElementById("courseInput");

function newCourse() {
    var newParagraph = document.createElement("p");
    var newCourse = document.createElement("input");
    newCourse.setAttribute("type", "text");
    newCourse.setAttribute("id", "CourseName" + courseCount);
    newCourse.setAttribute("name", "CourseName" + courseCount);
    newParagraph.appendChild(newCourse);
    var newId = document.createElement("input");
    newId.setAttribute("type", "text");
    newId.setAttribute("id", "CourseID" + courseCount);
    newId.setAttribute("name", "CourseID" + courseCount);
    newParagraph.appendChild(newId);
    coursediv.appendChild(newParagraph);
    courseCount ++;
}

function delCourse() {
    if (courseCount === 2) {
        return;
    }
    console.log("CourseName" + (courseCount - 1));
    var oldCourse = document.getElementById("CourseName" + (courseCount - 1));
    var oldId = document.getElementById("CourseID" + (courseCount - 1));
    oldCourse.remove();
    oldId.remove();
    courseCount --;
}