package com.example.thithurecycleview;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder {

    TextView firstName, lastName, email;
    Button btnEdit, btnDelete;


    public MyViewHolder(@NonNull View itemView) {
        super(itemView);

        firstName = itemView.findViewById(R.id.item_fname);
        lastName = itemView.findViewById(R.id.item_lname);
        email = itemView.findViewById(R.id.item_email);

        btnEdit = itemView.findViewById(R.id.item_btn_edit);
        btnDelete = itemView.findViewById(R.id.item_btn_delete);
    }
}
