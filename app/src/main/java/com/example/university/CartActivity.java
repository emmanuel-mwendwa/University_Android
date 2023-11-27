package com.example.university;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.university.Model.Cart;
import com.example.university.Prevalent.Prevalent;
import com.example.university.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button NextProcessBtn;
    private TextView txtTotalCourses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);


        recyclerView = findViewById(R.id.course_cart_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        txtTotalCourses = (TextView) findViewById(R.id.total_courses);

    }

    @Override
    protected void onStart() {
        super.onStart();

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference("Users")
                .child(Prevalent.currentOnlineUser.getReg_no())
                .child("registered_courses");

        FirebaseRecyclerOptions<Cart> options =
                new FirebaseRecyclerOptions.Builder<Cart>()
                        .setQuery(cartListRef, Cart.class)
                                .build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter
                = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull Cart model) {

                holder.txtCartCourseName.setText(model.getCourseName());
                holder.txtCartCourseCode.setText(model.getCourseCode());
                holder.txtCartLecturerEmail.setText(model.getCourseLecturer());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence options[] = new CharSequence[] {
                                "Deregister from Course"
                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                        builder.setTitle("Course Options:");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {

                                    cartListRef .child(model.getCourseId())
                                            .removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(CartActivity.this, "Course removed successfully.", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });

                                    String yearSemester = Prevalent.currentOnlineUser.getYearSemester();
                                    DatabaseReference coursesReference = FirebaseDatabase.getInstance().getReference("Courses").child(yearSemester);

                                    Query courseQuery = coursesReference.orderByChild("courseCode").equalTo(model.getCourseCode());
                                    courseQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.exists()) {
                                                // Iterate over each course found
                                                for (DataSnapshot courseSnapshot : snapshot.getChildren()) {
                                                    // Check if the "students" node exists for the course
                                                    if (courseSnapshot.hasChild("students")) {
                                                        // Get the specific student node based on registration number
                                                        DataSnapshot studentNode = courseSnapshot.child("students").child(Prevalent.currentOnlineUser.getReg_no());

                                                        if (studentNode.exists()) {
                                                            // Log the student data found
                                                            Log.d("StudentData", studentNode.toString());

                                                            // Remove the specific student's details
                                                            studentNode.getRef().removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {
                                                                        Toast.makeText(CartActivity.this, "You have deregistered from this course", Toast.LENGTH_SHORT).show();
                                                                    } else {
                                                                        Toast.makeText(CartActivity.this, "Failed to deregister you from this course", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            });
                                                        } else {
                                                            Log.e("StudentData", "Student not found for registration number: " + Prevalent.currentOnlineUser.getReg_no());
                                                        }
                                                    } else {
                                                        Log.e("StudentData", "No students node found for course: " + courseSnapshot.getKey());
                                                    }
                                                }
                                            } else {
                                                // Handle the case where the course does not exist
                                                Log.e("CourseQuery", "Course does not exist");
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            // Handle potential errors
                                            Log.e("CourseQuery", "Cancelled: " + error.getMessage());
                                        }
                                    });


                                    Intent intent = new Intent(CartActivity.this, CartActivity.class);
                                    intent.putExtra("pid", model.getCourseCode());
                                    Log.d("courseCodeSent", String.valueOf(model.getCourseId()));
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
                        builder.show();
                    }
                });
            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout, parent, false);
                CartViewHolder holder = new CartViewHolder(view);
                return holder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Includes navigate = new Includes().navigateTo(CartActivity.this, Student.class);
        finish();
    }
}