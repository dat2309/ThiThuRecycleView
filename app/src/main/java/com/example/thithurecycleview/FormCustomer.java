package com.example.thithurecycleview;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class FormCustomer extends AppCompatActivity {
    private Customer customer;
    private Button btnSave,btnCancle;
    private EditText firstName, lastName, email;
    private String url = "https://60b08c541f26610017ffe5f1.mockapi.io/Cus";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_student);
        getSupportActionBar().hide();

        firstName = findViewById(R.id.form_fname);
        lastName = findViewById(R.id.form_lName);
        email = findViewById(R.id.form_email);
        btnSave = findViewById(R.id.form_btn_save);
        btnCancle= findViewById(R.id.form_btn_cancle);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            customer = (Customer) bundle.getSerializable("customer");
            firstName.setText(customer.getFirstName());
            lastName.setText(customer.getLastName());
            email.setText(customer.getEmail() );
        }

        if (customer.getId() == 0)
            btnSave.setText("ADD");
        else
            btnSave.setText("UPDATE");


        btnSave.setOnClickListener(v -> {
            if (customer.getId() == 0) {
                addCustomer();
            } else {
                updateCustomer();
            }
        });
        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FormCustomer.this, ManagerActivity.class));
                finish();
            }
        });
    }

    private void updateCustomer() {
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, url + "/" + customer.getId(),
                response -> {
                    startActivity(new Intent(FormCustomer.this, ManagerActivity.class));
                    finish();
                },
                error -> {
                    error.printStackTrace();
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("firstName", firstName.getText().toString().trim());
                map.put("lastName", lastName.getText().toString().trim());
                map.put("email", email.getText().toString().trim());
                return map;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, 1, 1));
        RequestQueue queue = Volley.newRequestQueue(FormCustomer.this);
        queue.add(stringRequest);
    }

    private void addCustomer() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    startActivity(new Intent(FormCustomer.this, ManagerActivity.class));
                    finish();
                },
                error -> {
                    error.printStackTrace();
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("firstName", firstName.getText().toString().trim());
                map.put("lastName", lastName.getText().toString().trim());
                map.put("email", email.getText().toString().trim());
                return map;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, 1, 1));
        RequestQueue queue = Volley.newRequestQueue(FormCustomer.this);
        queue.add(stringRequest);
    }
}
