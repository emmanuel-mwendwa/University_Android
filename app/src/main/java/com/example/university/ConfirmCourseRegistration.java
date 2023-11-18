package com.example.university;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.university.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ConfirmCourseRegistration extends AppCompatActivity {

    private Button confirmCourseRegistrationBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_course_registration);

        confirmCourseRegistrationBtn = (Button) findViewById(R.id.confirm_course_registration);

        confirmCourseRegistrationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmRegistration();
            }
        });
    }

    private void ConfirmRegistration() {
        final String saveCurrentDate, saveCurrentTime;

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        final DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference()
                .child("Confirmed")
                .child(Prevalent.currentOnlineUser.getReg_no());

        // Check if the course is already registered
        ordersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Course already registered
                    Toast.makeText(ConfirmCourseRegistration.this, "Courses already registered", Toast.LENGTH_SHORT).show();
                } else {
                    // Course not registered, proceed with registration
                    HashMap<String, Object> ordersMap = new HashMap<>();
                    ordersMap.put("studentemail", Prevalent.currentOnlineUser.getEmail());
                    ordersMap.put("date", saveCurrentDate);
                    ordersMap.put("time", saveCurrentTime);

                    ordersRef.updateChildren(ordersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ConfirmCourseRegistration.this, "Courses registered successfully", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(ConfirmCourseRegistration.this, Student.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(ConfirmCourseRegistration.this, "Failed to register courses", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
                Toast.makeText(ConfirmCourseRegistration.this, "Error checking registration status", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
