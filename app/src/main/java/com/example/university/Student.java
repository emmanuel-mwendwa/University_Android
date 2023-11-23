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

public class Student extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityStudentBinding binding;
    private DatabaseReference CoursesRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        setContentView(R.layout.activity_student);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        Paper.init(this);
        CoursesRef = FirebaseDatabase.getInstance().getReference().child("Courses");

        binding = ActivityStudentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarStudent.toolbar);
        binding.appBarStudent.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Includes includes = new Includes().navigateTo(Student.this, CartActivity.class);
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_course_registration, R.id.nav_results)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_student);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        View headerView = navigationView.getHeaderView(0);
        CircleImageView profileImageView = headerView.findViewById(R.id.user_profile_image);
        TextView userNameTextView = headerView.findViewById(R.id.user_profile_name);

        recyclerView = findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        if (Prevalent.currentOnlineUser != null) {
            userNameTextView.setText(Prevalent.currentOnlineUser.getName());
        }
        else {
            startActivity(new Intent(Student.this, Login.class));
            finish();
        }

        NavigationView navigationView1 = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if( id == R.id.nav_logout) {
                    logout();
                    return true;
                }
                else if (id == R.id.nav_settings) {
                    Intent intent = new Intent(Student.this, Settings.class);
                    startActivity(intent);
                    finish();
                    return true;
                }
                else if (id == R.id.nav_course_registration) {
                    Intent intent = new Intent(Student.this, CartActivity.class);
                    startActivity(intent);
                    finish();
                    return true;
                }

                return false;
            }
        });


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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("MenuItemClicked", "MenuItem clicked: "+ item.getItemId());

        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        Log.d("Logout", "Logout method called");
        Paper.book().destroy();

        Intent intent = new Intent(Student.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.student, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_student);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Includes navigate = new Includes().navigateTo(Student.this, Student.class);
        finish();
    }
}
