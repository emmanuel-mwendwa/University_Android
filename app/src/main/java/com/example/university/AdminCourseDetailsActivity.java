package com.example.university;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.university.Model.Courses;
import com.example.university.databinding.ActivityAdminCourseDetailsBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class AdminCourseDetailsActivity extends AdminDrawerActivity {

    ActivityAdminCourseDetailsBinding activityAdminCourseDetailsBinding;
    private Button registerCourse;
    private ImageView courseImage;
    private TextView txtcourseName, txtcourseCode, txtcourseLecturer;
    private String courseId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAdminCourseDetailsBinding = ActivityAdminCourseDetailsBinding.inflate(getLayoutInflater());
        setContentView(activityAdminCourseDetailsBinding.getRoot());
        allocateActivityTitle("Course");

        courseId = getIntent().getStringExtra("pid");
        String selectedYearSemester = getIntent().getStringExtra("selectedYearSemester");

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
                intent.putExtra("selectedYearSemester", String.valueOf(selectedYearSemester));
                startActivity(intent);
                finish();
            }
        });
    }

    private void getCourseDetails(String courseId) {

        String selectedYearSemester = getIntent().getStringExtra("selectedYearSemester");

        DatabaseReference coursesRef = FirebaseDatabase.getInstance().getReference().child("Courses").child(selectedYearSemester);

        Query courseQuery = coursesRef.orderByChild("courseCode").equalTo(courseId);

        courseQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    DataSnapshot courseSnapshot = snapshot.getChildren().iterator().next();
                    Courses courses = courseSnapshot.getValue(Courses.class);
                    Log.d("coursesValuesFetched", String.valueOf(courseSnapshot));
                    if (courses != null) {
                        txtcourseName.setText(courses.getCourseName());
                        txtcourseCode.setText(courses.getCourseCode());
                        txtcourseLecturer.setText(courses.getLecturerEmail());
                    }
                    else {
                        Toast.makeText(AdminCourseDetailsActivity.this, "Error retrieving course details", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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