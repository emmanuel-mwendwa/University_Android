package com.example.university;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.university.Model.Users;
import com.example.university.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    private Button joinNowButton, loginButton;

    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        joinNowButton = (Button) findViewById(R.id.main_join_now_btn);
        loginButton = (Button) findViewById(R.id.main_login_btn);

        loadingBar = new ProgressDialog(this);

        Paper.init(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
                finish();
            }
        });

        joinNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Signup.class);
                startActivity(intent);
                finish();
            }
        });
        
        String userEmail = Paper.book().read(Prevalent.userEmail);
        String userPasswordKey = Paper.book().read(Prevalent.userPasswordKey);
        
        if (userEmail != "" && userPasswordKey != "") {
            if (!TextUtils.isEmpty(userEmail) && !TextUtils.isEmpty(userPasswordKey)) {
                AllowAccess(userEmail, userPasswordKey);

                loadingBar.setTitle("Already Logged In");
                loadingBar.setMessage("Please wait... ");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
            }
        }
    }

    private void AllowAccess(final String email, final String password) {
        final DatabaseReference RootRef;

        RootRef = FirebaseDatabase.getInstance().getReference().child("Users");

        RootRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        Users usersData = snapshot1.getValue(Users.class);
                        if (usersData != null && usersData.getEmail().equals(email)) {
                            if (usersData.getPassword().equals(password)) {
                                Toast.makeText(MainActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                                if (usersData.getIsLecturer() != null && usersData.getIsLecturer().equals("1")) {
                                    Intent  intent = new Intent(MainActivity.this, MainActivity.class);
                                    startActivity(intent);
                                }
                                else if (usersData.getIsStudent() != null && usersData.getIsStudent().equals("1")) {
                                    Intent  intent = new Intent(MainActivity.this, Student.class);
                                    startActivity(intent);
                                }
                                else if (usersData.getIsAdmin() != null && usersData.getIsAdmin().equals("1")) {
                                    Intent  intent = new Intent(MainActivity.this, MainActivity.class);
                                    startActivity(intent);
                                }
                            }
                            else {
                                Toast.makeText(MainActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
                else {
                    Toast.makeText(MainActivity.this, "Account does not exist", Toast.LENGTH_SHORT).show();
                }
                loadingBar.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}