package com.example.university.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.university.Interface.ItemClickListener;
import com.example.university.R;

public class StudentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtStudentName, txtStudentRegNo, txtStudentMarksStatus;
    public ItemClickListener listener;
    public StudentViewHolder(@NonNull View itemView) {
        super(itemView);

        txtStudentName = (TextView) itemView.findViewById(R.id.student_name);
        txtStudentRegNo = (TextView) itemView.findViewById(R.id.student_reg_no);
        txtStudentMarksStatus = (TextView) itemView.findViewById(R.id.student_marks);
    }

    public void setItemClickListener(ItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        listener.onClick(v, getAdapterPosition(), false);
    }
}
