package com.example.university;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Objects;

public class Signup extends AppCompatActivity {

    private Button createAccountButton;
    private EditText inputReg, inputName, inputEmail;

    private TextInputLayout inputPasswordLayout, inputConfirmPasswordLayout;

    private Spinner spinnerYearSemester;

    boolean valid = true;

    private ProgressDialog loadingBar;

    private CheckBox isStudentAccount, isLecturerAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        createAccountButton = (Button) findViewById(R.id.register_btn);
        inputReg = (EditText) findViewById(R.id.register_reg_input);
        inputName = (EditText) findViewById(R.id.register_name_input);
        inputEmail = (EditText) findViewById(R.id.register_email_input);
        inputPasswordLayout = (TextInputLayout) findViewById(R.id.register_password_input);
        inputConfirmPasswordLayout = (TextInputLayout) findViewById(R.id.register_confirm_password_input);
        isLecturerAccount = (CheckBox) findViewById(R.id.is_Lecturer);
        isStudentAccount = (CheckBox) findViewById(R.id.is_Student);
        spinnerYearSemester = (Spinner) findViewById(R.id.spinnerYearSemester);
        loadingBar = new ProgressDialog(this);

        isStudentAccount.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()) {
                    isLecturerAccount.setChecked(false);
                }
            }
        });

        isLecturerAccount.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()) {
                    isStudentAccount.setChecked(false);
                }
            }
        });

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAccount();
            }
        });
    }

    private void CreateAccount() {

        checkField(inputReg);
        checkField(inputName);
        checkField(inputEmail);
        checkField(inputPasswordLayout.getEditText());
        checkField(inputConfirmPasswordLayout.getEditText());

        // check validation
        if (!(isLecturerAccount.isChecked() || isStudentAccount.isChecked())) {
            Toast.makeText(Signup.this, "Select the Account Type", Toast.LENGTH_SHORT).show();
            return;
        }

        if (valid) {
            loadingBar.setTitle("Create Account");
            loadingBar.setMessage("Please wait, while we check the credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            String reg = inputReg.getText().toString();
            String name = inputName.getText().toString();
            String email = inputEmail.getText().toString();
            String password = inputPasswordLayout.getEditText().getText().toString();
            String confirmPassword = inputConfirmPasswordLayout.getEditText().getText().toString();
            String selectedYearSemester  = spinnerYearSemester.getSelectedItem().toString();

            if (TextUtils.isEmpty(selectedYearSemester)) {
                Toast.makeText(this, "Select Year and Semester", Toast.LENGTH_SHORT).show();
                return;
            }

            ValidateinputEmail(reg, name, email, password, confirmPassword, selectedYearSemester);
        }
    }

    private void ValidateinputEmail(String reg, String name, String email, String password, String confirmPassword,  String selectedYearSemester) {

        if (password.equals(confirmPassword)) {
            final DatabaseReference RootRef;

            RootRef = FirebaseDatabase.getInstance().getReference();

            RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (!(snapshot.child("Users").child(reg).exists())) {

                        Query query = RootRef.child("Users").orderByChild("email").equalTo(email);
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (!(snapshot.exists())) {
                                    HashMap<String, Object> userdataMap = new HashMap<>();

                                    userdataMap.put("reg_no", reg);
                                    userdataMap.put("name", name);
                                    userdataMap.put("email", email);
                                    userdataMap.put("password", password);
                                    userdataMap.put("yearSemester", selectedYearSemester);

                                    // specify the account type
                                    if (isLecturerAccount.isChecked()) {
                                        userdataMap.put("isLecturer", "1");
                                    }

                                    if (isStudentAccount.isChecked()) {
                                        userdataMap.put("isStudent", "1");
                                    }

                                    RootRef.child("Users").child(reg).updateChildren(userdataMap)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(Signup.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
                                                        loadingBar.dismiss();

                                                        new Includes().navigateTo(Signup.this, Login.class);
                                                    }
                                                    else {
                                                        loadingBar.dismiss();
                                                        Toast.makeText(Signup.this, "Network Error: Please try again", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                }
                                else {
                                    Toast.makeText(Signup.this, "Email already exists", Toast.LENGTH_SHORT).show();
                                    loadingBar.dismiss();
                                }

                            }


                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                    else {
                        Toast.makeText(Signup.this, "The registration number already exists", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else {
            loadingBar.dismiss();
            Toast.makeText(this, "Password does not match", Toast.LENGTH_SHORT).show();
        }

    }

    public boolean checkField(EditText textField) {
        if(textField.getText().toString().isEmpty()) {
            textField.setError("Error");
            valid = false;
        }
        else {
            valid = true;
        }

        return valid;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Includes navigate = new Includes().navigateTo(Signup.this, MainActivity.class);
        finish();
    }
}