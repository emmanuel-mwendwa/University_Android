package com.example.university;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.university.Model.Courses;
import com.example.university.Prevalent.Prevalent;
import com.example.university.ViewHolder.CourseViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.university.databinding.ActivityStudentBinding;
import com.google.firebase.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class Student extends StudentDrawerActivity {

    private ActivityStudentBinding activityStudentBinding;
    private DatabaseReference CoursesRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityStudentBinding = ActivityStudentBinding.inflate(getLayoutInflater());
        setContentView(activityStudentBinding.getRoot());

        Paper.init(this);

        String yearSemester = Prevalent.currentOnlineUser.getYearSemester();
        Log.d("currentYear", yearSemester);

        CoursesRef = FirebaseDatabase.getInstance().getReference().child("Courses").child(yearSemester);

//        CircleImageView profileImageView = headerView.findViewById(R.id.user_profile_image);
//        TextView userNameTextView = findViewById(R.id.user_profile_name);

        recyclerView = findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        if (Prevalent.currentOnlineUser != null) {
//            userNameTextView.setText(Prevalent.currentOnlineUser.getName());
        }
        else {
            startActivity(new Intent(Student.this, Login.class));
            finish();
        }


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
//                        Picasso.get().load(model.getImage()).into(holder.imageView);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Student.this, CourseDetailsActivity.class);
                                intent.putExtra("pid", model.getCourseCode());
                                Log.d("courseCodeSent", String.valueOf(model.getCourseCode()));
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Includes navigate = new Includes().navigateTo(Student.this, Student.class);
        finish();
    }
}