package com.example.university;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.SimpleTimeZone;

public class AddCourseActivity extends AppCompatActivity {

    private Button addNewCourseBtn;
    private EditText inputCourseName, inputCourseCode, inputLecturer;
    boolean valid = true;
    String courseName, courseCode, lecturer, saveCurrentDate, saveCurrentTime, courseRandomKey;

    private DatabaseReference CoursesRef;

    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        addNewCourseBtn = (Button) findViewById(R.id.addNewCourse);
        inputCourseCode = (EditText) findViewById(R.id.course_code);
        inputCourseName = (EditText) findViewById(R.id.course_name);
        inputLecturer = (EditText) findViewById(R.id.lecturer_email);

        loadingBar = new ProgressDialog(this);

        CoursesRef = FirebaseDatabase.getInstance().getReference().child("Courses");

        addNewCourseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidateCourseData();
            }
        });

    }

    private void ValidateCourseData() {
        courseName = inputCourseName.getText().toString();
        courseCode = inputCourseCode.getText().toString();
        lecturer = inputLecturer.getText().toString();

        new Includes().checkField(inputCourseName);
        new Includes().checkField(inputCourseCode);
        new Includes().checkField(inputLecturer);

        if (TextUtils.isEmpty(courseName)) {
            Toast.makeText(this, "Enter Course Name", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(courseCode)) {
            Toast.makeText(this, "Enter Course Code", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(lecturer)) {
            Toast.makeText(this, "Enter Lecturer Email", Toast.LENGTH_SHORT).show();
        }
        else {
            loadingBar.setTitle("Add New Course");
            loadingBar.setMessage("Please wait, while we add the course.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            StoreCourseInformation();
        }
    }

    private void StoreCourseInformation() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        courseRandomKey = saveCurrentDate + saveCurrentTime;

        CoursesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.child("courseId").exists()) {

                    Query query = CoursesRef.orderByChild("courseCode").equalTo(courseCode);

                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (!(snapshot.exists())) {
                                HashMap<String, Object> courseMap = new HashMap<>();
                                courseMap.put("courseId", courseRandomKey);
                                courseMap.put("date", saveCurrentDate);
                                courseMap.put("time", saveCurrentTime);
                                courseMap.put("courseName", courseName);
                                courseMap.put("courseCode", courseCode);
                                courseMap.put("lecturerEmail", lecturer);

                                CoursesRef.child(courseRandomKey).updateChildren(courseMap)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(AddCourseActivity.this, "Course added successfully!", Toast.LENGTH_SHORT).show();
                                                    loadingBar.dismiss();

                                                    new Includes().navigateTo(AddCourseActivity.this, Admin.class);
                                                } else {
                                                    String message = task.getException().toString();
                                                    Toast.makeText(AddCourseActivity.this, "Error " + message, Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            } else {
                                Toast.makeText(AddCourseActivity.this, "Course Code already exists", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}