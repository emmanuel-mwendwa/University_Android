package com.example.university.Model;

public class SemesterGrade {
    private String semesterGradeStatus;

    // Required default constructor
    public SemesterGrade() {
        // Default constructor required for Firebase
    }

    // Other constructors, getters, setters...

    public String getSemesterGradeStatus() {
        return semesterGradeStatus;
    }

    public void setSemesterGradeStatus(String semesterGradeStatus) {
        this.semesterGradeStatus = semesterGradeStatus;
    }
}

