package com.example.university;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CourseDetailsActivity extends AppCompatActivity {

    private FloatingActionButton registerCourse;
    private ImageView courseImage;
    private TextView courseName, courseCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);

        registerCourse = (FloatingActionButton) findViewById(R.id.register_course);
        courseName = (TextView) findViewById(R.id.course_name_details);
        courseName = (TextView) findViewById(R.id.course_name_details);
    }
}