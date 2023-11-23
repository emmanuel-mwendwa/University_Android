package com.example.university.Model;

public class StudentClass {

    private String studentName, studentRegNo;

    private double assignment1, assignment2, cat1, cat2, finalExam;

    private String overallGrade, gradeStatus;

    public StudentClass() {
    }

    public StudentClass(String studentName, String studentRegNo, String studentMarksStatus, double assignment1, double assignment2, double cat1, double cat2, double finalExam) {
        this.studentName = studentName;
        this.studentRegNo = studentRegNo;
        this.assignment1 = assignment1;
        this.assignment2 = assignment2;
        this.cat1 = cat1;
        this.cat2 = cat2;
        this.finalExam = finalExam;
        this.overallGrade = calculateOverallGrade();
        this.gradeStatus = calculateGradeStatus();
    }

    private String calculateOverallGrade() {

        double totalMarks = (assignment1 + assignment2 + cat1 + cat2 + finalExam);

        if (totalMarks >= 70) {
            return "A";
        }
        else if (totalMarks >= 60) {
            return "B";
        }
        else if (totalMarks >= 50) {
            return "C";
        }
        else if (totalMarks >= 40) {
            return "D";
        }
        else {
            return "F";
        }
    }

    public String getOverallGrade() {
        return overallGrade;
    }

    public void setOverallGrade(String overallGrade) {
        this.overallGrade = overallGrade;
    }

    public String getGradeStatus() {
        return gradeStatus;
    }

    public void setGradeStatus(String gradeStatus) {
        this.gradeStatus = gradeStatus;
    }

    private String calculateGradeStatus() {
        if (overallGrade.equals("F")) {
            return "Fail";
        }
        else {
            return "Pass";
        }
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
