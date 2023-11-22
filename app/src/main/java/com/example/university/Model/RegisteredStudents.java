package com.example.university.Model;

public class RegisteredStudents {

    private String studentEmail, studentName, studentRegNo;

    public RegisteredStudents(String studentEmail, String studentName, String studentRegNo) {
        this.studentEmail = studentEmail;
        this.studentName = studentName;
        this.studentRegNo = studentRegNo;
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
}
