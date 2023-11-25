package com.example.university;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.university.Model.Users;
import com.example.university.Prevalent.Prevalent;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class Login extends AppCompatActivity {

    private EditText inputEmail;

    private TextInputLayout inputPasswordLayout;
    private Button loginButton;
    private ProgressDialog loadingBar;

    private CheckBox chxBoxRememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = (Button) findViewById(R.id.login_btn);
        inputEmail = (EditText) findViewById(R.id.login_email_input);
        inputPasswordLayout = (TextInputLayout) findViewById(R.id.login_password_input);
        loadingBar = new ProgressDialog(this);

        chxBoxRememberMe = (CheckBox) findViewById(R.id.remember_me);

        Paper.init(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

    }

    private void loginUser() {

        String email = inputEmail.getText().toString();
        String password = inputPasswordLayout.getEditText().getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Enter your email", Toast.LENGTH_SHORT).show();
        }

        else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Enter your password", Toast.LENGTH_SHORT).show();
        }

        else {
            loadingBar.setTitle("Logging In");
            loadingBar.setMessage("Please wait, while we check the credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            AllowAccessToAccount(email, password);
        }
    }

    private void AllowAccessToAccount(String email, String password) {

        if (chxBoxRememberMe.isChecked()) {
            Paper.book().write(Prevalent.userEmail, email);
            Paper.book().write(Prevalent.userPasswordKey, password);
        }

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
                                Toast.makeText(Login.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                                Prevalent.currentOnlineUser= usersData;
                                loadingBar.dismiss();

                                if (usersData.getIsLecturer() != null && usersData.getIsLecturer().equals("1")) {
                                    new Includes().navigateTo(Login.this, Lecturer.class);
                                }
                                else if (usersData.getIsStudent() != null && usersData.getIsStudent().equals("1")) {
                                    new Includes().navigateTo(Login.this, Student.class);
                                }
                                else if (usersData.getIsAdmin() != null && usersData.getIsAdmin().equals("1")) {
                                    new Includes().navigateTo(Login.this, Admin.class);
                                }
                            }
                            else {
                                Toast.makeText(Login.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
                else {
                    Toast.makeText(Login.this, "Account does not exist", Toast.LENGTH_SHORT).show();
                }
                loadingBar.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Includes navigate = new Includes().navigateTo(Login.this, MainActivity.class);
        finish();
    }
}