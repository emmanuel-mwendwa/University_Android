package com.example.university.Model;

public class RegisteredStudents {

    private String studentEmail, studentName, studentRegNo, studentMarksStatus, yearSemester;

    public RegisteredStudents(String studentEmail, String studentName, String studentRegNo, String studentMarksStatus, String yearSemester) {
        this.studentEmail = studentEmail;
        this.studentName = studentName;
        this.studentRegNo = studentRegNo;
        this.studentMarksStatus = studentMarksStatus;
        this.yearSemester = yearSemester;
    }

    public RegisteredStudents() {
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentRegNo() {
        return studentRegNo;
    }

    public void setStudentRegNo(String studentRegNo) {
        this.studentRegNo = studentRegNo;
    }

    public String getStudentMarksStatus() {
        return studentMarksStatus;
    }

    public void setStudentMarksStatus(String studentMarksStatus) {
        this.studentMarksStatus = studentMarksStatus;
    }

    public String getYearSemester() {
        return yearSemester;
    }

    public void setYearSemester(String yearSemester) {
        this.yearSemester = yearSemester;
    }
}
