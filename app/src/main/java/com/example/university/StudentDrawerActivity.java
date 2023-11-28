package com.example.university;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.transition.Fade;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.university.Model.Cart;
import com.example.university.Prevalent.Prevalent;
import com.google.android.material.navigation.NavigationView;

import io.paperdb.Paper;


public class StudentDrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;

    @Override
    public void setContentView(View view) {
        drawerLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_student_drawer, null);
        FrameLayout container = drawerLayout.findViewById(R.id.studentActivityContainer);
        container.addView(view);
        super.setContentView(drawerLayout);

        Toolbar toolbar = drawerLayout.findViewById(R.id.studentToolBar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = drawerLayout.findViewById(R.id.student_nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView  = navigationView.getHeaderView(0);
        TextView userProfileName = headerView.findViewById(R.id.user_profile_name);

        userProfileName.setText(Prevalent.currentOnlineUser.getName());

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.admin_menu_drawer_open,
                R.string.admin_menu_drawer_close
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.nav_student_home) {
            startActivity(new Intent(this, Student.class));
            overridePendingTransition(0, 0);
        }

        else if (item.getItemId() == R.id.nav_registered_courses) {
            startActivity(new Intent(this, CartActivity.class));
            overridePendingTransition(0, 0);
        }

        else if (item.getItemId() == R.id.nav_student_settings) {
            startActivity(new Intent(this, Settings.class));
            overridePendingTransition(0, 0);
        }

        else if (item.getItemId() == R.id.nav_student_logout) {
            showLogoutConfirmationDialog();
        }

        // Close the drawer
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    protected void allocateActivityTitle(String titleString) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(titleString);
        }
    }

    private void showLogoutConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to log out?");

        // Add buttons
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                performLogout();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                dialog.dismiss();
            }
        });

        // Create and show the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void performLogout() {
        Paper.book().destroy();

        // Create a fade-out transition
        Fade fadeOut = new Fade();
        fadeOut.setDuration(9000); // You can adjust the duration as needed
        getWindow().setExitTransition(fadeOut);

        // Start MainActivity with the specified transition
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());

        // Override pending transition to avoid animation conflict
        overridePendingTransition(0, 0);
    }
}