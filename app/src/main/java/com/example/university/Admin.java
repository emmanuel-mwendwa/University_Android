package com.example.university;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.university.databinding.ActivityAdminBinding;

import io.paperdb.Paper;

public class Admin extends AdminDrawerActivity {

    ActivityAdminBinding activityAdminBinding;

    private Button addCourse, addAccount, generateReport, editMarks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAdminBinding = ActivityAdminBinding.inflate(getLayoutInflater());
        setContentView(activityAdminBinding.getRoot());
        allocateActivityTitle("Dashboard");

        addCourse = (Button) findViewById(R.id.addNewCourseActivity);
        addAccount = (Button) findViewById(R.id.addNewAccountActivity);
        generateReport = (Button) findViewById(R.id.generateReportsActivity);
        editMarks = (Button) findViewById(R.id.editMarksActivity);

        addAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Includes().navigateTo(Admin.this, AdminSignUp.class);
            }
        });

        addCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Includes().navigateTo(Admin.this, AddCourseActivity.class);
            }
        });

        editMarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Includes().navigateTo(Admin.this, AdminViewCoursesActivity.class);
            }
        });

        generateReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Includes().navigateTo(Admin.this, ReportTypeActivity.class);
            }
        });

    }
}