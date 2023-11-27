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
import android.widget.AdapterView;
import android.widget.Spinner;

import com.example.university.Model.Courses;
import com.example.university.Model.StudentReports;
import com.example.university.ViewHolder.CourseViewHolder;
import com.example.university.ViewHolder.ReportsViewHolder;
import com.example.university.databinding.ActivityReportTypeBinding;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportTypeActivity extends AdminDrawerActivity {

    ActivityReportTypeBinding activityReportTypeBinding;

    private DatabaseReference StudentsResultsRef;

    private RecyclerView recyclerView;

    RecyclerView.LayoutManager layoutManager;

    private Spinner spinnerYearSemester, spinnerReportType;

    private Map<String, StudentReports> studentDataMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityReportTypeBinding = ActivityReportTypeBinding.inflate(getLayoutInflater());
        setContentView(activityReportTypeBinding.getRoot());
        allocateActivityTitle("Reports");

        StudentsResultsRef = FirebaseDatabase.getInstance().getReference("Users");

        recyclerView = findViewById(R.id.recycler_admin_view_reports);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        spinnerYearSemester = findViewById(R.id.spinnerYearSemester);
        spinnerReportType = findViewById(R.id.spinnerReportType);

        studentDataMap = new HashMap<>();

        spinnerYearSemester.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerReportType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        displayReports();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void displayReports() {

        String selectedYearSemester = spinnerYearSemester.getSelectedItem().toString();
        String selectedReportType = spinnerReportType.getSelectedItem().toString();

        Query studentsOnlyRef = StudentsResultsRef.orderByChild("semesterGradeStatus").equalTo(selectedReportType);

        //         Set up the FirebaseRecyclerAdapter
        FirebaseRecyclerOptions<StudentReports> options = new FirebaseRecyclerOptions.Builder<StudentReports>()
                .setQuery(studentsOnlyRef, StudentReports.class)
                .build();

        FirebaseRecyclerAdapter<StudentReports, ReportsViewHolder> adapter =
                new FirebaseRecyclerAdapter<StudentReports, ReportsViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ReportsViewHolder holder, int position, @NonNull StudentReports model) {
                        // Display students based on the selected report type and their grade status
                        holder.txtStudentName.setText(model.getName());
                        holder.txtStudentRegNo.setText(model.getReg_no());
                    }

                    @NonNull
                    @Override
                    public ReportsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.generated_report_layout, parent, false);
                        ReportsViewHolder holder = new ReportsViewHolder(view);
                        return holder;
                    }
                };

        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }
}