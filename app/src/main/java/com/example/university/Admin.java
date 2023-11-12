package com.example.university;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import io.paperdb.Paper;

public class Admin extends AppCompatActivity {

    private Button LogoutBtn, addCourse, addAccount, generateReport, editMarks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        LogoutBtn = (Button) findViewById(R.id.adminLogoutButton);
        addCourse = (Button) findViewById(R.id.addNewCourseActivity);
        addAccount = (Button) findViewById(R.id.addNewAccountActivity);
        generateReport = (Button) findViewById(R.id.generateReportsActivity);
        editMarks = (Button) findViewById(R.id.editMarksActivity);

        addCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Includes().navigateTo(Admin.this, AddCourseActivity.class);
            }
        });

        LogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Paper.book().destroy();

                new Includes().navigateTo(Admin.this, MainActivity.class);
            }
        });
    }
}