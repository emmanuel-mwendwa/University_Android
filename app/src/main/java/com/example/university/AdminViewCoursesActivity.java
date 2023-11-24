package com.example.university;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.university.Model.Courses;
import com.example.university.ViewHolder.CourseViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class AdminViewCoursesActivity extends AppCompatActivity {

    private DatabaseReference CoursesRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    private Button addMarksButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_courses);

//        RecyclerView recyclerView = findViewById(R.id.recycler_admin_view_courses);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        CoursesRef = FirebaseDatabase.getInstance().getReference().child("Courses");

        addMarksButton = (Button) findViewById(R.id.register_course_button) ;

        recyclerView = findViewById(R.id.recycler_admin_view_courses);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

    }

    @Override
    protected void onStart() {

        super.onStart();

        FirebaseRecyclerOptions<Courses> options =
                new FirebaseRecyclerOptions.Builder<Courses>()
                        .setQuery(CoursesRef, Courses.class)
                        .build();

        FirebaseRecyclerAdapter<Courses, CourseViewHolder> adapter =
                new FirebaseRecyclerAdapter<Courses, CourseViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull CourseViewHolder holder, int position, @NonNull Courses model) {
                        holder.txtCourseName.setText(model.getCourseName());
                        holder.txtCourseCode.setText(model.getCourseCode());
                        holder.txtLecturerEmail.setText(model.getLecturerEmail());

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent intent = new Intent(AdminViewCoursesActivity.this, AdminCourseDetailsActivity.class);
                                intent.putExtra("pid", model.getCourseId());
                                startActivity(intent);
                                finish();
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.courses_items_layout, parent, false);
                        CourseViewHolder holder = new CourseViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}