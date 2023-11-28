package com.example.university.Model;

public class Cart {
    private String courseId, courseCode, courseName, courseLecturer, courseGradeStatus;

    public Cart() {
    }

    public Cart(String courseId, String courseCode, String courseName, String courseLecturer, String courseGradeStatus) {
        this.courseId = courseId;
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.courseLecturer = courseLecturer;
        this.courseGradeStatus = courseGradeStatus;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
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

    public String getCourseLecturer() {
        return courseLecturer;
    }

    public void setCourseLecturer(String courseLecturer) {
        this.courseLecturer = courseLecturer;
    }

    public String getCourseGradeStatus() {
        return courseGradeStatus;
    }

    public void setCourseGradeStatus(String courseGradeStatus) {
        this.courseGradeStatus = courseGradeStatus;
    }
}
