package com.example.thithurecycleview;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.io.Serializable;

public class ManagerActivity extends AppCompatActivity {

    private MyAdapter adapter;
    private FirebaseAuth auth;
    private RecyclerView recyclerView;
    private Button btnAdd, btnSignout;
    private FirebaseAuth.AuthStateListener stateListener;
    private String url = "https://60b08c541f26610017ffe5f1.mockapi.io/Cus";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);
        getSupportActionBar().hide();

        recyclerView = findViewById(R.id.manager_recycler);
        btnAdd = findViewById(R.id.manager_btn_add);
        btnSignout = findViewById(R.id.manager_btn_signout);

        adapter = new MyAdapter(ManagerActivity.this);
        recyclerView.setLayoutManager(new GridLayoutManager(ManagerActivity.this, 1));
        recyclerView.setAdapter(adapter);

        auth = FirebaseAuth.getInstance();

        btnAdd.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("customer",  new Customer());
            Intent intent = new Intent(ManagerActivity.this, FormCustomer.class);
            intent.putExtras(bundle);
            startActivity(intent);
        });

        btnSignout.setOnClickListener(v -> {
            auth.signOut();
        });

        stateListener = auth -> {
            if (auth.getCurrentUser() == null) {
                startActivity(new Intent(ManagerActivity.this, MainActivity.class));
                finish();
            }
        };

    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(stateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (stateListener != null)
            auth.removeAuthStateListener(stateListener);
    }
}