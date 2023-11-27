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
import android.widget.TextView;
import android.widget.Toast;

import com.example.university.Model.RegisteredStudents;
import com.example.university.ViewHolder.StudentViewHolder;
import com.example.university.databinding.ActivityAdminStudentCourseBinding;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class AdminStudentCourse extends AdminDrawerActivity {

    ActivityAdminStudentCourseBinding activityAdminStudentCourseBinding;
    private DatabaseReference coursesRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    TextView txtCourseCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAdminStudentCourseBinding = ActivityAdminStudentCourseBinding.inflate(getLayoutInflater());
        setContentView(activityAdminStudentCourseBinding.getRoot());
        allocateActivityTitle("Registered Students");

        txtCourseCode = (TextView) findViewById(R.id.admin_recycler_course_name);

        Intent intent = getIntent();
        String courseCode = intent.getStringExtra("courseCode");
        String selectedYearSemester = intent.getStringExtra("selectedYearSemester");
        Log.d("CourseCode", String.valueOf(courseCode));

        txtCourseCode.setText(courseCode);

        coursesRef = FirebaseDatabase.getInstance().getReference("Courses").child(selectedYearSemester);

        recyclerView = (RecyclerView) findViewById(R.id.admin_student_recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onStart() {

        super.onStart();

        Intent intent = getIntent();
        String courseCode = intent.getStringExtra("courseCode");
        String selectedYearSemester = intent.getStringExtra("selectedYearSemester");

        Query courseQuery = coursesRef.orderByChild("courseCode").equalTo(courseCode);
        courseQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    DataSnapshot courseSnapshot = snapshot.getChildren().iterator().next();
                    DataSnapshot studentsSnapshot = courseSnapshot.child("students");

                    Query studentsQuery = studentsSnapshot.getRef(); // Convert DataSnapshot to Query

                    FirebaseRecyclerOptions<RegisteredStudents> options =
                            new FirebaseRecyclerOptions.Builder<RegisteredStudents>()
                                    .setQuery(studentsQuery, RegisteredStudents.class)
                                    .build();


                    FirebaseRecyclerAdapter<RegisteredStudents, StudentViewHolder> adapter =
                            new FirebaseRecyclerAdapter<RegisteredStudents, StudentViewHolder>(options) {
                                @Override
                                protected void onBindViewHolder(@NonNull StudentViewHolder holder, int position, @NonNull RegisteredStudents model) {


                                    holder.txtStudentName.setText("Name: " + model.getStudentName());
                                    holder.txtStudentRegNo.setText("Reg No: " + model.getStudentRegNo());
                                    holder.txtStudentMarksStatus.setText("Marks Status: " + model.getStudentMarksStatus());

                                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            String selectedYearSemester = intent.getStringExtra("selectedYearSemester");
                                            Log.d("selectedYearSemester", selectedYearSemester);
                                            DatabaseReference coursesRef = FirebaseDatabase.getInstance().getReference("Courses").child(selectedYearSemester);
                                            Query courseQuery = coursesRef.orderByChild("courseCode").equalTo(courseCode);

                                            courseQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    if (snapshot.exists()) {
                                                        DataSnapshot coursesSnapshot = snapshot.getChildren().iterator().next();
                                                        DataSnapshot studentMarksStatus = coursesSnapshot.child("students")
                                                                .child(model.getStudentRegNo())
                                                                .child("studentMarksStatus");

                                                            Intent intent1 = new Intent(getApplicationContext(), AdminEditMarksActivity.class);
                                                            intent1.putExtra("courseCode", String.valueOf(courseCode));
                                                            intent1.putExtra("studentName", String.valueOf(model.getStudentName()));
                                                            intent1.putExtra("studentRegNo", String.valueOf(model.getStudentRegNo()));
                                                            intent1.putExtra("studentYearSemester", String.valueOf(model.getYearSemester()));
                                                            startActivity(intent1);
                                                            finish();
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });
                                        }
                                    });

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
                else {
                    Toast.makeText(AdminStudentCourse.this, "There are no registered students in this course", Toast.LENGTH_SHORT).show();
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

        String selectedYearSemester = getIntent().getStringExtra("selectedYearSemester");

        Intent intent = new Intent(AdminStudentCourse.this, AdminCourseDetailsActivity.class);
        intent.putExtra("selectedYearSemester", String.valueOf(selectedYearSemester));
        startActivity(intent);
        finish();
    }
}