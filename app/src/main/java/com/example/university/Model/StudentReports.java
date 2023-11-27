package com.example.university.Model;

public class StudentReports {
    private String email, name, password, reg_no, yearSemester, semesterGradeStatus;

    public StudentReports() {

    }

    public StudentReports(String email, String name, String password, String reg_no, String yearSemester, String semesterGradeStatus) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.reg_no = reg_no;
        this.yearSemester = yearSemester;
        this.semesterGradeStatus = semesterGradeStatus;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getReg_no() {
        return reg_no;
    }

    public void setReg_no(String reg_no) {
        this.reg_no = reg_no;
    }


    public String getYearSemester() {
        return yearSemester;
    }

    public void setYearSemester(String yearSemester) {
        this.yearSemester = yearSemester;
    }

    public String getSemesterGradeStatus() {
        return semesterGradeStatus;
    }

    public void setSemesterGradeStatus(String semesterGradeStatus) {
        this.semesterGradeStatus = semesterGradeStatus;
    }
}
