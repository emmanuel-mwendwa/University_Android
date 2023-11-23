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

import com.example.university.Model.StudentClass;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class AddMarksActivity extends AppCompatActivity {

    TextView txtStudentName, txtStudentRegNo;
    EditText txtAssignment1, txtAssignment2, txtCat1, txtCat2, txtExam;
    Button addMarksButton;

    DatabaseReference studentsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_marks);

        Intent intent = getIntent();


        String studentName = intent.getStringExtra("studentName");
        String studentRegNo = intent.getStringExtra("studentRegNo");

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
        double assignment1 = Double.parseDouble(txtAssignment1.getText().toString());
        double assignment2 = Double.parseDouble(txtAssignment2.getText().toString());
        double cat1 = Double.parseDouble(txtCat1.getText().toString());
        double cat2 = Double.parseDouble(txtCat2.getText().toString());
        double finalExam = Double.parseDouble(txtExam.getText().toString());

        // Calculate totalMarks
        double totalMarks = (assignment1 + assignment2 + cat1 + cat2 + finalExam);

        // Assign overall grade based on criteria
        String overallGrade = calculateOverallGrade(totalMarks);

        // Assign grade status
        String gradeStatus = calculateGradeStatus(overallGrade);

//        StudentClass student = new StudentClass(studentName, studentRegNo, assignment1, assignment2, cat1, cat2, finalExam);

        Intent intent = getIntent();
        String courseCode = intent.getStringExtra("courseCode");

        DatabaseReference coursesRef = FirebaseDatabase.getInstance().getReference("Courses");

        Query courseQuery = coursesRef.orderByChild("courseCode").equalTo(courseCode);

        courseQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    DataSnapshot courseSnapshot = snapshot.getChildren().iterator().next();
                    DataSnapshot studentsSnapshot  = courseSnapshot.child("students").child(studentRegNo).child("grades");

                    // Create a HashMap to store the updated values
                    Map<String, Object> updateValues = new HashMap<>();
                    updateValues.put("assignment1", assignment1);
                    updateValues.put("assignment2", assignment2);
                    updateValues.put("cat1", cat1);
                    updateValues.put("cat2", cat2);
                    updateValues.put("finalExam", finalExam);
                    updateValues.put("overallGrade", overallGrade);
                    updateValues.put("gradeStatus", gradeStatus);

                    studentsSnapshot.getRef().updateChildren(updateValues);

                    Toast.makeText(AddMarksActivity.this, "Marks added successfully", Toast.LENGTH_SHORT).show();
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


}