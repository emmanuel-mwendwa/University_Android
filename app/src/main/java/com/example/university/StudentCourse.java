package com.example.university;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.university.Model.Courses;
import com.example.university.Model.RegisteredStudents;
import com.example.university.Model.Users;
import com.example.university.ViewHolder.StudentViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StudentCourse extends AppCompatActivity {

    private DatabaseReference coursesRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_course);

        coursesRef = FirebaseDatabase.getInstance().getReference("Users");
        recyclerView = (RecyclerView) findViewById(R.id.student_recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onStart() {

        super.onStart();

        Intent intent = getIntent();
        String courseCode = intent.getStringExtra("courseCode");

        FirebaseRecyclerOptions<Courses> options =
                new FirebaseRecyclerOptions.Builder<Courses>()
                        .setQuery(coursesRef.child("registered_courses"), Courses.class)
                        .build();

        FirebaseRecyclerAdapter<Courses, StudentViewHolder> adapter =
                new FirebaseRecyclerAdapter<Courses, StudentViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull StudentViewHolder holder, int position, @NonNull Courses model) {


                        holder.studentName.setText(model.getCourseName());
                        holder.studentRegNo.setText(model.getCourseCode());
                        holder.studentMarksStatus.setText("Pending");
                    }

                    @NonNull
                    @Override
                    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.students_course_layout, parent, false);
                        StudentViewHolder holder = new StudentViewHolder(view);

                        return holder;
                    }
                };

        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Includes navigate = new Includes().navigateTo(StudentCourse.this, Lecturer.class);
        finish();
    }
}