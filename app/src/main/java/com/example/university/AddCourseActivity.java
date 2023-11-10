package com.example.university;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class AddCourseActivity extends AppCompatActivity {

    private Button addNewCourseBtn;
    private EditText inputCourseName, inputCourseCode, inputLecturer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        addNewCourseBtn = (Button) findViewById(R.id.addNewCourse);
        inputCourseCode = (EditText) findViewById(R.id.course_code);
        inputCourseName = (EditText) findViewById(R.id.course_name);
        inputLecturer = (EditText) findViewById(R.id.lecturer_email);
    }
}