package com.example.university.Model;

public class Users {

    private String email, name, password, reg_no, image, isLecturer, isStudent, isAdmin, assignedCourse;

    public Users() {

    }

    public Users(String email, String name, String password, String reg_no, String image, String isLecturer, String isStudent, String isAdmin, String assignedCourse) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.reg_no = reg_no;
        this.image = image;
        this.isLecturer = isLecturer;
        this.isStudent = isStudent;
        this.isAdmin = isAdmin;
        this.assignedCourse = assignedCourse;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getIsLecturer() {
        return isLecturer;
    }

    public void setIsLecturer(String isLecturer) {
        this.isLecturer = isLecturer;
    }

    public String getIsStudent() {
        return isStudent;
    }

    public void setIsStudent(String isStudent) {
        this.isStudent = isStudent;
    }

    public String getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(String isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getAssignedCourse() {
        return assignedCourse;
    }

    public void setAssignedCourse(String assignedCourse) {
        this.assignedCourse = assignedCourse;
    }
}
