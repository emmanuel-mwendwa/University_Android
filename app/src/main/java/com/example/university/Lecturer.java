package com.example.university;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.university.Model.Courses;
import com.example.university.Model.Users;
import com.example.university.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class Lecturer extends AppCompatActivity {

    private Button LogoutBtn;
    private TextView txtCourseName, txtCourseCode, txtLecturerEmail, txtLecturerDashboard;

    private RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecturer);

        LogoutBtn = (Button) findViewById(R.id.lecturerLogoutButton);
        txtLecturerDashboard = (TextView) findViewById(R.id.lecturer_dashboard);
        txtCourseName = (TextView) findViewById(R.id.lecturer_course_layout_name);
        txtCourseCode = (TextView) findViewById(R.id.lecturer_course_layout_code);
        txtLecturerEmail = (TextView) findViewById(R.id.lecturer_course_layout_lecturer);

        LogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Paper.book().destroy();

                Intent intent = new Intent(Lecturer.this, MainActivity.class);
                startActivity(intent);
            }
        });

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        String userId = Prevalent.currentOnlineUser.getReg_no();

        databaseReference.child("Users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users user = snapshot.getValue(Users.class);
                String assignCourse = user.getAssignedCourse();

                // Now, use the assigned course to retrieve the course details
                databaseReference.child("Courses").orderByChild("lecturerEmail").equalTo(user.getEmail()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot datasnapshot) {

                        if (datasnapshot.exists()) {
                            for (DataSnapshot dataSnapshot : datasnapshot.getChildren()) {
                                Courses course = dataSnapshot.getValue(Courses.class);

                                final String sendCourseCode = String.valueOf(course.getCourseCode());

                                txtLecturerDashboard.setText("");
                                txtCourseName.setText(course.getCourseName());
                                txtCourseCode.setText(course.getCourseCode());
                                txtLecturerEmail.setText(course.getLecturerEmail());

                                relativeLayout = (RelativeLayout) findViewById(R.id.relative_layout_2);
                                relativeLayout.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(Lecturer.this, StudentCourse.class);
                                        intent.putExtra("courseCode", sendCourseCode);
                                        startActivity(intent);
                                        finish();
                                    }
                                });

                            }
                        }
                        else {
                            Toast.makeText(Lecturer.this, "Course does not exist", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}