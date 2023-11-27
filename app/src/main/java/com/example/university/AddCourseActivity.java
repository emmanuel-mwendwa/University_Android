package com.example.university;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.university.databinding.ActivityAddCourseBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class AddCourseActivity extends AdminDrawerActivity {

    ActivityAddCourseBinding activityAddCourseBinding;

    private Button addNewCourseBtn;
    private EditText inputCourseName, inputCourseCode;
    private ProgressDialog loadingBar;
    private Spinner spinnerYearSemester, spinnerLecturer;

    private DatabaseReference coursesRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAddCourseBinding = ActivityAddCourseBinding.inflate(getLayoutInflater());
        setContentView(activityAddCourseBinding.getRoot());
        allocateActivityTitle("New Course");

        addNewCourseBtn = findViewById(R.id.addNewCourse);
        inputCourseCode = findViewById(R.id.course_code);
        inputCourseName = findViewById(R.id.course_name);
//        inputLecturer = findViewById(R.id.lecturer_email);
        spinnerYearSemester = findViewById(R.id.spinnerYearSemester);
        spinnerLecturer = findViewById(R.id.spinnerLecturer);

//        String inputLecturer = spinnerLecturer.getSelectedItem().toString();

        spinnerLecturer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String inputLecturer = spinnerLecturer.getSelectedItem().toString();

                addNewCourseBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateLecturerAssignedCourse(inputLecturer, inputCourseCode.getText().toString());
                        Log.d("Selected Lecturer", String.valueOf(inputLecturer));
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        loadingBar = new ProgressDialog(this);

        coursesRef = FirebaseDatabase.getInstance().getReference().child("Courses");

        spinnerYearSemester.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedYearSemester = spinnerYearSemester.getSelectedItem().toString();

                populateLecturerSpinner(selectedYearSemester);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void populateLecturerSpinner(String selectedYearSemester) {
        // Query your database for lecturers based on the selected year and semester
        DatabaseReference lecturerRef = FirebaseDatabase.getInstance().getReference().child("Users");

        // Sample query - adjust based on your database structure
        Query query = lecturerRef.orderByChild("yearSemester").equalTo(selectedYearSemester);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> lecturerList = new ArrayList<>();

                for (DataSnapshot lecturerSnapshot : dataSnapshot.getChildren()) {
                    // Assuming each lecturer node has a 'name' field

                    String isLecturer = lecturerSnapshot.child("isLecturer").getValue(String.class);

                    if (isLecturer  != null ) {
                        String lecturerName = lecturerSnapshot.child("email").getValue(String.class);
                        lecturerList.add(lecturerName);
                    }
                }

                // Populate the spinner with the retrieved lecturers
                ArrayAdapter<String> lecturerAdapter = new ArrayAdapter<>(AddCourseActivity.this, android.R.layout.simple_spinner_item, lecturerList);
                lecturerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerLecturer.setAdapter(lecturerAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle onCancelled if needed
            }
        });
    }

    private void validateCourseData() {
        String courseName = inputCourseName.getText().toString();
        String courseCode = inputCourseCode.getText().toString();
        String lecturer = spinnerLecturer.getSelectedItem().toString();
        String selectedYearSemester = spinnerYearSemester.getSelectedItem().toString();

        if (TextUtils.isEmpty(courseName) || TextUtils.isEmpty(courseCode) || TextUtils.isEmpty(lecturer)) {
            Toast.makeText(this, "All fields must be filled", Toast.LENGTH_SHORT).show();
        } else {
            loadingBar.setTitle("Add New Course");
            loadingBar.setMessage("Please wait, while we add the course.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            storeCourseInformation(courseName, courseCode, lecturer, selectedYearSemester);
        }
    }

    private void storeCourseInformation(String courseName, String courseCode, String lecturer, String selectedYearSemester) {
        String saveCurrentDate, saveCurrentTime;
        String courseRandomKey;

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        courseRandomKey = saveCurrentDate + saveCurrentTime;

        Query lecturerQuery = coursesRef.orderByChild("lecturerEmail").equalTo(lecturer);

        lecturerQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot lecturerSnapshot) {
                if (!lecturerSnapshot.exists()) {
                    Query courseQuery = coursesRef.orderByChild("courseCode").equalTo(courseCode);

                    courseQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot courseSnapshot) {
                            if (!courseSnapshot.exists()) {
                                // Course Code is unique, and lecturer is not assigned to any course
                                HashMap<String, Object> courseMap = new HashMap<>();
                                courseMap.put("courseId", courseRandomKey);
                                courseMap.put("date", saveCurrentDate);
                                courseMap.put("time", saveCurrentTime);
                                courseMap.put("courseName", courseName);
                                courseMap.put("courseCode", courseCode);
                                courseMap.put("lecturerEmail", lecturer);
                                courseMap.put("yearSemester", selectedYearSemester);

                                coursesRef.child(selectedYearSemester).push().updateChildren(courseMap)
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
                            // Handle potential errors
                        }
                    });
                } else {
                    Toast.makeText(AddCourseActivity.this, "Lecturer already assigned to a course", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateLecturerAssignedCourse(String lecturerEmail, String leccourseId) {
        DatabaseReference lecturerRef = FirebaseDatabase.getInstance().getReference("Users");

        Query query = lecturerRef.orderByChild("email").equalTo(lecturerEmail);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot lecturerSnapshot) {
                if (lecturerSnapshot.exists()) {
                    for (DataSnapshot lecturerData : lecturerSnapshot.getChildren()) {
                        String currentAssignedCourse = lecturerData.child("assignedCourse").getValue(String.class);
                        if (!TextUtils.equals(currentAssignedCourse, leccourseId)) {
                            lecturerData.getRef().child("assignedCourse").setValue(leccourseId);

                            validateCourseData();
                        }
                    }
                } else {
                    Toast.makeText(AddCourseActivity.this, "This lecturer does not exist", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled if needed
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Includes navigate = new Includes().navigateTo(AddCourseActivity.this, Admin.class);
        finish();
    }
}