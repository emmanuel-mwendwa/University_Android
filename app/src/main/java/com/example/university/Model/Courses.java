package com.example.university.Model;

public class Courses {

    private String courseCode, courseName, lecturerEmail;

    public Courses(String courseCode, String courseName, String lecturerEmail) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.lecturerEmail = lecturerEmail;
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
}
