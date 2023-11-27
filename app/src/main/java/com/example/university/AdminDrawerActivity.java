package com.example.university;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.transition.Fade;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.material.navigation.NavigationView;

import io.paperdb.Paper;

public class AdminDrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;

    @Override
    public void setContentView(View view) {
        if (drawerLayout == null) {
            setupDrawerLayout(view);
        } else {
            updateContentView(view);
        }
    }

    private void setupDrawerLayout(View view) {
        drawerLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_admin_drawer, null);
        FrameLayout container = drawerLayout.findViewById(R.id.adminActivityContainer);
        container.addView(view);
        super.setContentView(drawerLayout);

        Toolbar toolbar = drawerLayout.findViewById(R.id.adminToolBar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = drawerLayout.findViewById(R.id.admin_nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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

    private void updateContentView(View view) {
        FrameLayout container = drawerLayout.findViewById(R.id.adminActivityContainer);
        container.removeAllViews();
        container.addView(view);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation item selection
        // ...
        if (item.getItemId() == R.id.nav_admin_home) {
                startActivity(new Intent(this, Admin.class));
                overridePendingTransition(0, 0);
        }

        else if (item.getItemId() == R.id.nav_add_course) {
            startActivity(new Intent(this, AddCourseActivity.class));
            overridePendingTransition(0, 0);
        }

        else if (item.getItemId() == R.id.nav_edit_marks) {
            startActivity(new Intent(this, AdminViewCoursesActivity.class));
            overridePendingTransition(0, 0);
        }

        else if (item.getItemId() == R.id.nav_add_account) {
            startActivity(new Intent(this, AdminSignUp.class));
            overridePendingTransition(0, 0);
        }

        else if (item.getItemId() == R.id.nav_admin_logout) {
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
