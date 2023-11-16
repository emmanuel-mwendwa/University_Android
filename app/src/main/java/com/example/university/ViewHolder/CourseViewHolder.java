package com.example.university.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.university.R;

public class CourseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtCourseName, txtCourseCode, txtLecturerEmail;
    public ImageView imageView;

    public CourseViewHolder(@NonNull View itemView) {
        super(itemView);

        imageView = (ImageView) itemView.findViewById(R.id.course_layout_image);
        txtCourseName = (TextView) itemView.findViewById(R.id.course_layout_name);
        txtCourseCode = (TextView) itemView.findViewById(R.id.course_layout_code);
        txtLecturerEmail = (TextView) itemView.findViewById(R.id.course_layout_lecturer);
    }

    @Override
    public void onClick(View v) {

    }
}
