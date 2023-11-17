package com.example.university;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.university.Model.Courses;
import com.example.university.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class CourseDetailsActivity extends AppCompatActivity {

    private Button registerCourse;
    private ImageView courseImage;
    private TextView txtcourseName, txtcourseCode, txtcourseLecturer;
    private String courseId = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);

        courseId = getIntent().getStringExtra("pid");

        registerCourse = (Button) findViewById(R.id.register_course_button);
        txtcourseName = (TextView) findViewById(R.id.course_name_details);
        txtcourseCode = (TextView) findViewById(R.id.course_code_details);
        txtcourseLecturer = (TextView) findViewById(R.id.course_lecturer_details);

        getCourseDetails(courseId);

        registerCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addingToRegisteredCoursesList();
            }
        });
    }

    private void addingToRegisteredCoursesList() {
        String saveCurrentTime, saveCurrentDate;

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentDate.format(calForDate.getTime());

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");

        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("courseId", courseId);
        cartMap.put("courseName", txtcourseName.getText().toString());
        cartMap.put("courseCode", txtcourseCode.getText().toString());
        cartMap.put("courseLecturer", txtcourseLecturer.getText().toString());
        cartMap.put("date", saveCurrentDate);
        cartMap.put("time", saveCurrentTime);

        cartListRef.child("User View").child(Prevalent.currentOnlineUser.getReg_no())
                .child("Courses").child(courseId)
                .updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            cartListRef.child("Admin View").child(Prevalent.currentOnlineUser.getReg_no())
                                    .child("Courses").child(courseId)
                                    .updateChildren(cartMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                          if (task.isSuccessful()) {
                                              Toast.makeText(CourseDetailsActivity.this, "Added to Courses List", Toast.LENGTH_SHORT).show();

                                              Includes includes = new Includes().navigateTo(CourseDetailsActivity.this, Student.class);
                                          }
                                        }
                                    });
                        }
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

            }
        });
    }

    @Override
    public void onBackPressed() {
        Includes navigate = new Includes().navigateTo(CourseDetailsActivity.this, Student.class);
        finish();
    }
}