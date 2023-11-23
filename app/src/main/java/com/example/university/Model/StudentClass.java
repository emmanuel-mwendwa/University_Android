package com.example.university.Model;

public class StudentClass {

    private String studentName, studentRegNo;

    private double assignment1, assignment2, cat1, cat2, finalExam;

    public StudentClass() {
    }

    public StudentClass(String studentName, String studentRegNo, double assignment1, double assignment2, double cat1, double cat2, double finalExam) {
        this.studentName = studentName;
        this.studentRegNo = studentRegNo;
        this.assignment1 = assignment1;
        this.assignment2 = assignment2;
        this.cat1 = cat1;
        this.cat2 = cat2;
        this.finalExam = finalExam;
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

    public double getAssignment1() {
        return assignment1;
    }

    public void setAssignment1(double assignment1) {
        this.assignment1 = assignment1;
    }

    public double getAssignment2() {
        return assignment2;
    }

    public void setAssignment2(double assignment2) {
        this.assignment2 = assignment2;
    }

    public double getCat1() {
        return cat1;
    }

    public void setCat1(double cat1) {
        this.cat1 = cat1;
    }

    public double getCat2() {
        return cat2;
    }

    public void setCat2(double cat2) {
        this.cat2 = cat2;
    }

    public double getFinalExam() {
        return finalExam;
    }

    public void setFinalExam(double finalExam) {
        this.finalExam = finalExam;
    }
}
