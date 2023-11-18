package com.example.university.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.university.Interface.ItemClickListener;
import com.example.university.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtCartCourseName, txtCartCourseCode, txtCartLecturerEmail;
    private ItemClickListener itemClickListener;

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);

        txtCartCourseName = (TextView)  itemView.findViewById(R.id.cart_course_name);
        txtCartCourseCode = (TextView) itemView.findViewById(R.id.cart_course_code);
        txtCartLecturerEmail = (TextView) itemView.findViewById(R.id.cart_course_lecturer);
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
