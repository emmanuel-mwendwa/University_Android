package com.example.university;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.university.Model.Courses;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminCourseDetailsActivity extends AppCompatActivity {

    private Button registerCourse;
    private ImageView courseImage;
    private TextView txtcourseName, txtcourseCode, txtcourseLecturer;
    private String courseId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_course_details);

        courseId = getIntent().getStringExtra("pid");

        registerCourse = findViewById(R.id.admin_register_course_button);
        txtcourseName = findViewById(R.id.admin_course_name_details);
        txtcourseCode = findViewById(R.id.admin_course_code_details);
        txtcourseLecturer = findViewById(R.id.admin_course_lecturer_details);

        getCourseDetails(courseId);

        registerCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCourseDetailsActivity.this, AdminStudentCourse.class);
                intent.putExtra("courseCode", String.valueOf(txtcourseCode.getText()));
                startActivity(intent);
                finish();
            }
        });
    }

    private void getCourseDetails(String courseId) {
        DatabaseReference coursesRef = FirebaseDatabase.getInstance().getReference().child("Courses");

        coursesRef.child(courseId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Courses courses = snapshot.getValue(Courses.class);
                    txtcourseName.setText(courses.getCourseName());
                    txtcourseCode.setText(courses.getCourseCode());
                    txtcourseLecturer.setText(courses.getLecturerEmail());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle potential errors
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Includes navigate = new Includes().navigateTo(AdminCourseDetailsActivity.this, AdminViewCoursesActivity.class);
        finish();
    }
}