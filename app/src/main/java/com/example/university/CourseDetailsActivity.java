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

        registerCourse = findViewById(R.id.register_course_button);
        txtcourseName = findViewById(R.id.course_name_details);
        txtcourseCode = findViewById(R.id.course_code_details);
        txtcourseLecturer = findViewById(R.id.course_lecturer_details);

        getCourseDetails(courseId);

        registerCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCourseBeforeAdding();
            }
        });
    }

    private void checkCourseBeforeAdding() {
        DatabaseReference usersReference = FirebaseDatabase.getInstance().getReference("Users").child(Prevalent.currentOnlineUser.getReg_no());

        usersReference.child("registered_courses")
                .child(courseId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists()) {
                            // Course is not in the cart list
                            checkCourseLimit();
                        } else {
                            // Course is already in the cart list
                            Toast.makeText(CourseDetailsActivity.this, "You have already registered this course", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle potential errors
                    }
                });
    }

    private void checkCourseLimit() {
        DatabaseReference usersReference = FirebaseDatabase.getInstance().getReference("Users");

        usersReference.child(Prevalent.currentOnlineUser.getReg_no())
                .child("registered_courses")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getChildrenCount() < 5) {
                            // The user has not reached the maximum limit (5 courses), proceed to add the course
                            addingToRegisteredCoursesList();
                        } else {
                            // The user has already registered the maximum allowed number of courses
                            Toast.makeText(CourseDetailsActivity.this, "Maximum limit of 5 courses reached", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle potential errors
                    }
                });
    }

    private void addingToRegisteredCoursesList() {
        String saveCurrentTime, saveCurrentDate;

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        final DatabaseReference usersReference = FirebaseDatabase.getInstance().getReference("Users").child(Prevalent.currentOnlineUser.getReg_no());
        DatabaseReference coursesReference = FirebaseDatabase.getInstance().getReference("Courses");


        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("courseId", courseId);
        cartMap.put("courseName", txtcourseName.getText().toString());
        cartMap.put("courseCode", txtcourseCode.getText().toString());
        cartMap.put("courseLecturer", txtcourseLecturer.getText().toString());
        cartMap.put("date", saveCurrentDate);
        cartMap.put("time", saveCurrentTime);

        final HashMap<String, Object> studentMap = new HashMap<>();
        studentMap.put("studentRegNo", Prevalent.currentOnlineUser.getReg_no());
        studentMap.put("studentName", Prevalent.currentOnlineUser.getName());
        studentMap.put("studentEmail", Prevalent.currentOnlineUser.getEmail());
        studentMap.put("date", saveCurrentDate);
        studentMap.put("time", saveCurrentTime);
        studentMap.put("studentMarksStatus", "pending");

        usersReference.child("registered_courses")
                .child(courseId)
                .updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            coursesReference.child(courseId)
                                    .child("students")
                                    .child(Prevalent.currentOnlineUser.getReg_no())
                                    .updateChildren(studentMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(CourseDetailsActivity.this, "Course Registered Successfully.", Toast.LENGTH_SHORT).show();
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
                // Handle potential errors
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Includes navigate = new Includes().navigateTo(CourseDetailsActivity.this, Student.class);
        finish();
    }
}
