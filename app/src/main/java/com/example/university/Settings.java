package com.example.university;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.canhub.cropper.CropImage;
import com.example.university.Prevalent.Prevalent;
import com.example.university.databinding.ActivitySettingsBinding;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Settings extends StudentDrawerActivity {

    private ActivitySettingsBinding activitySettingsBinding;

    private CircleImageView profileImageView;
    private EditText fullnameEditText, emailEditText;
    private TextView profileChangeTextBtn, closeTextBtn, saveTextButton;

    private Uri imageUri;
    private String myUrl = "";
    private StorageTask uploadTask;
    private StorageReference storageProfilePictureRef;
    private String checker = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySettingsBinding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(activitySettingsBinding.getRoot());
        allocateActivityTitle("Settings");

        FirebaseApp.initializeApp(this);
        storageProfilePictureRef = FirebaseStorage.getInstance().getReference().child("Profile pictures");

        profileImageView = (CircleImageView) findViewById(R.id.settings_profile_image);
        fullnameEditText = (EditText) findViewById(R.id.settings_full_name);
        emailEditText = (EditText) findViewById(R.id.settings_email);
        profileChangeTextBtn = (TextView) findViewById(R.id.profile_image_change_btn);
        closeTextBtn = (TextView) findViewById(R.id.close_settings);
        saveTextButton = (TextView) findViewById(R.id.update_account_settings);

        userInfoDisplay(profileImageView, fullnameEditText, emailEditText);

        closeTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        saveTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checker.equals("clicked")) {
                    userInfoSaved();
                } else {
                    updateOnlyUserInfo();
                }
            }
        });

//        profileChangeTextBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                checker = "clicked";
//
//                // Update profile picture image
//                // Remember to download the Crop Image
////                CropImage.activity(imageUri)
////                        .setAspectRatio(1, 1)
////                        .start(Settings.this);
//            }
//        });
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
////
//        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
//            CropImage.ActivityResult result = CropImage.getActivityResult(data);
//            imageUri = result.getUri();
//
//            profileImageView.setImageURI(imageUri);
//        }
//        else {
//            Toast.makeText(this, "Error, Try Again", Toast.LENGTH_SHORT).show();
//
//            startActivity(new Intent(Settings.this, Settings.class));
//            finish();
//        }
//    }

        private void updateOnlyUserInfo () {

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

            HashMap<String, Object> userMap = new HashMap<>();
            userMap.put("name", fullnameEditText.getText().toString());
            userMap.put("email", emailEditText.getText().toString());
            ref.child(Prevalent.currentOnlineUser.getReg_no()).updateChildren(userMap);

            startActivity(new Intent(Settings.this, Student.class));
            Toast.makeText(Settings.this, "Account Information updated successfully", Toast.LENGTH_SHORT).show();
            finish();
        }

        private void userInfoSaved () {
            if (TextUtils.isEmpty(fullnameEditText.getText().toString())) {
                Toast.makeText(this, "Name is mandatory", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(emailEditText.getText().toString())) {
                Toast.makeText(this, "Email is mandatory", Toast.LENGTH_SHORT).show();
            } else if (checker.equals("clicked")) {
                uploadImage();
            }
        }

        private void uploadImage () {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Update Profile");
            progressDialog.setMessage("Please wait, while we are updating your account information");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            if (imageUri != null) {
                final StorageReference fileRef = storageProfilePictureRef
                        .child(Prevalent.currentOnlineUser.getReg_no() + ".jpg");

                uploadTask = fileRef.putFile(imageUri);

                uploadTask.continueWithTask(new Continuation() {
                    @Override
                    public Object then(@NonNull Task task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        return fileRef.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUrl = task.getResult();
                            myUrl = downloadUrl.toString();

                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

                            HashMap<String, Object> userMap = new HashMap<>();
                            userMap.put("name", fullnameEditText.getText().toString());
                            userMap.put("email", emailEditText.getText().toString());
                            userMap.put("image", myUrl);
                            ref.child(Prevalent.currentOnlineUser.getReg_no()).updateChildren(userMap);

                            progressDialog.dismiss();

                            startActivity(new Intent(Settings.this, Student.class));
                            Toast.makeText(Settings.this, "Account Information updated successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(Settings.this, "An error occured. Try Again", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
                Toast.makeText(this, "Image is not selected", Toast.LENGTH_SHORT).show();
            }
        }

        private void userInfoDisplay (CircleImageView profileImageView, EditText
        fullnameEditText, EditText emailEditText){

            DatabaseReference UsersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser.getReg_no());

            UsersRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        if (snapshot.child("image").exists()) {
                            String image = snapshot.child("image").getValue().toString();
                            String name = snapshot.child("name").getValue().toString();
                            String email = snapshot.child("email").getValue().toString();

                            Picasso.get().load(image).into(profileImageView);
                            fullnameEditText.setText(name);
                            emailEditText.setText(email);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Includes navigate = new Includes().navigateTo(Settings.this, Student.class);
        finish();
    }
    }
