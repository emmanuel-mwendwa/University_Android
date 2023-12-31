package com.example.university;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.university.databinding.ActivityAdminEditMarksAcitivityBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class AdminEditMarksActivity extends AdminDrawerActivity {

    ActivityAdminEditMarksAcitivityBinding activityAdminEditMarksAcitivityBinding;

    TextView txtStudentName, txtStudentRegNo;
    EditText txtAssignment1, txtAssignment2, txtCat1, txtCat2, txtExam;
    Button addMarksButton;

    DatabaseReference studentsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAdminEditMarksAcitivityBinding = ActivityAdminEditMarksAcitivityBinding.inflate(getLayoutInflater());
        setContentView(activityAdminEditMarksAcitivityBinding.getRoot());

        Intent intent = getIntent();


        String studentName = intent.getStringExtra("studentName");
        String studentRegNo = intent.getStringExtra("studentRegNo");
        String studentYearSemester = intent.getStringExtra("studentYearSemester");

        txtStudentName = (TextView) findViewById(R.id.editTextStudentName);
        txtStudentRegNo = (TextView) findViewById(R.id.editTextStudentRegNo);
        txtAssignment1 = (EditText) findViewById(R.id.editTextAssignment1);
        txtAssignment2 = (EditText) findViewById(R.id.editTextAssignment2);
        txtCat1 = (EditText) findViewById(R.id.editTextCat1);
        txtCat2 = (EditText) findViewById(R.id.editTextCat2);
        txtExam = (EditText) findViewById(R.id.editTextExamMarks);

        addMarksButton = (Button) findViewById(R.id.btnAddMarks);

        txtStudentName.setText(studentName);
        txtStudentRegNo.setText(studentRegNo);

        allocateActivityTitle(txtStudentName.getText().toString());

        studentsRef = FirebaseDatabase.getInstance().getReference();

        addMarksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addStudentMarks();
            }
        });

    }

    private void addStudentMarks() {

        // Retrieve values from EditText fields
        String studentName = txtStudentName.getText().toString();
        String studentRegNo = txtStudentRegNo.getText().toString();
        double assignment1 = parseDoubleFromEditText(txtAssignment1);
        double assignment2 = parseDoubleFromEditText(txtAssignment2);
        double cat1 = parseDoubleFromEditText(txtCat1);
        double cat2 = parseDoubleFromEditText(txtCat2);
        double finalExam = parseDoubleFromEditText(txtExam);

        // Check if total assignment marks are not more than 10
        if (assignment1 + assignment2 > 10) {
            // Show an error or handle the situation (e.g., display a Toast)
            Toast.makeText(this, "Total assignment marks cannot be more than 10", Toast.LENGTH_SHORT).show();
            return; // Stop further processing
        }

// Check if total cat marks are not more than 20
        if (cat1 + cat2 > 20) {
            // Show an error or handle the situation (e.g., display a Toast)
            Toast.makeText(this, "Total cat marks cannot be more than 20", Toast.LENGTH_SHORT).show();
            return; // Stop further processing
        }

// Check if exam marks are not more than 70
        if (finalExam > 70) {
            // Show an error or handle the situation (e.g., display a Toast)
            Toast.makeText(this, "Exam marks cannot be more than 70", Toast.LENGTH_SHORT).show();
            return; // Stop further processing
        }

        // Calculate totalMarks
        double totalMarks = (assignment1 + assignment2 + cat1 + cat2 + finalExam);

        // Assign overall grade based on criteria
        String overallGrade = calculateOverallGrade(totalMarks);

        // Assign grade status
        String gradeStatus = calculateGradeStatus(overallGrade);

        Intent intent = getIntent();
        String courseCode = intent.getStringExtra("courseCode");
        String studentYearSemester = intent.getStringExtra("studentYearSemester");

        DatabaseReference coursesRef = FirebaseDatabase.getInstance().getReference("Courses").child(studentYearSemester);

        Query courseQuery = coursesRef.orderByChild("courseCode").equalTo(courseCode);

        courseQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    DataSnapshot courseSnapshot = snapshot.getChildren().iterator().next();
                    DataSnapshot studentsSnapshot  = courseSnapshot.child("students").child(studentRegNo);

                    // Marks are not available. Continue with adding the marks.

                    DataSnapshot gradesSnapshot = studentsSnapshot.child("grades");

                    // Create a HashMap to store the updated values
                    Map<String, Object> updateValues = new HashMap<>();
                    updateValues.put("assignment1", assignment1);
                    updateValues.put("assignment2", assignment2);
                    updateValues.put("cat1", cat1);
                    updateValues.put("cat2", cat2);
                    updateValues.put("finalExam", finalExam);
                    updateValues.put("overallGrade", overallGrade);
                    updateValues.put("gradeStatus", gradeStatus);

                    gradesSnapshot.getRef().updateChildren(updateValues);

                    // Update studentMarksStatus
                    DataSnapshot studentMarksStatusSnapshot = courseSnapshot.child("students").child(studentRegNo);
                    Map<String, Object> updateMarksStatus = new HashMap<>();
                    updateMarksStatus.put("studentMarksStatus", "available");
                    studentMarksStatusSnapshot.getRef().updateChildren(updateMarksStatus);

                    // Update the students courseGradeStatus
                    DatabaseReference studentRef = FirebaseDatabase.getInstance().getReference("Users").child(studentRegNo).child("registered_courses").child(courseCode);
                    Map<String, Object> updateGradeStatus = new HashMap<>();
                    updateGradeStatus.put("courseGradeStatus", gradeStatus);
                    studentRef.updateChildren(updateGradeStatus);

                    // Update the students semester overall grade
                    DatabaseReference studentsGradeRef = FirebaseDatabase.getInstance().getReference("Users").child(studentRegNo);
                    studentsGradeRef.child("registered_courses").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            boolean allPassed = true;
                            boolean anyPending = false;

                            // Check if all courses are passed
                            for (DataSnapshot courseGradeSnapshot : snapshot.getChildren()) {
                                String courseGradeStatus = courseGradeSnapshot.child("courseGradeStatus").getValue(String.class);

                                if (!"Pass".equals(courseGradeStatus)) {
                                    allPassed = false;
                                    if ("Pending".equals(courseGradeStatus)) {
                                        anyPending = true;
                                        break;
                                    }
                                }
                            }

                            // Update semesterGradeStatus based on the result
                            if (allPassed) {
                                studentsGradeRef.child("semesterGrade").child("semesterGradeStatus").setValue("Pass");
                            }
                            else if (anyPending) {
                                studentsGradeRef.child("semesterGrade").child("semesterGradeStatus").setValue("Pending");
                            }
                            else {
                                studentsGradeRef.child("semesterGrade").child("semesterGradeStatus").setValue("Fail");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    // Start the StudentCourse activity
                    Toast.makeText(AdminEditMarksActivity.this, "Marks added successfully", Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(AdminEditMarksActivity.this, AdminStudentCourse.class);
                    intent1.putExtra("courseCode", String.valueOf(courseCode));
                    intent1.putExtra("selectedYearSemester", String.valueOf(studentYearSemester));
                    startActivity(intent1);
                    finish();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private String calculateOverallGrade(double totalMarks) {
        if (totalMarks >= 70) {
            return "A";
        }
        else if (totalMarks >= 60) {
            return "B";
        }
        else if (totalMarks >= 50) {
            return "C";
        }
        else if (totalMarks >= 40) {
            return "D";
        }
        else {
            return "F";
        }
    }

    private String calculateGradeStatus(String overallGrade) {
        if (overallGrade.equals("F")) {
            return "Fail";
        }
        else {
            return "Pass";
        }
    }

    private boolean isEditTextEmpty(EditText editText) {
        String text = editText.getText().toString().trim();
        if (text.isEmpty()) {
            editText.setError("This field is required");
            return true;
        }
        return false;
    }

    private double parseDoubleFromEditText(EditText editText) {
        if (isEditTextEmpty(editText)) {
            return 0.0; // or any other default value, or you can choose to return a special value
        }

        String text = editText.getText().toString().trim();

        try {
            // Parse the double value from the EditText
            return Double.parseDouble(text);
        } catch (NumberFormatException e) {
            // Show a Toast message or highlight the error in the EditText
            Toast.makeText(this, "Invalid number format", Toast.LENGTH_SHORT).show();
            editText.setError("Invalid number format");
            return 0.0; // or any other default value, or you can choose to return a special value
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        String selectedYearSemester = getIntent().getStringExtra("selectedYearSemester");

        Intent intent = new Intent(AdminEditMarksActivity.this, AdminCourseDetailsActivity.class);
        intent.putExtra("selectedYearSemester", String.valueOf(selectedYearSemester));
        startActivity(intent);
        finish();
    }
}