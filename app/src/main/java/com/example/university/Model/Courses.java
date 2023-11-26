package com.example.university.Model;

public class Courses {

    private String courseCode, courseName, lecturerEmail, courseId,  yearSemester;

    public Courses(String courseCode, String courseName, String lecturerEmail, String courseId, String yearSemester) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.lecturerEmail = lecturerEmail;
        this.courseId = courseId;
        this.yearSemester = yearSemester;
    }

    public Courses () {

    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getLecturerEmail() {
        return lecturerEmail;
    }

    public void setLecturerEmail(String lecturerEmail) {
        this.lecturerEmail = lecturerEmail;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getYearSemester() {
        return yearSemester;
    }

    public void setYearSemester(String yearSemester) {
        this.yearSemester = yearSemester;
    }
}
