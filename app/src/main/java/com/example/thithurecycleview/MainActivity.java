package com.example.thithurecycleview;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private EditText inputEmail, inputPassword;
    private FirebaseAuth.AuthStateListener stateListener;
    private Button btnSignin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();

        stateListener = firebaseAuth -> {
            if (firebaseAuth.getCurrentUser() != null) {
                startActivity(new Intent(MainActivity.this, ManagerActivity.class));
                finish();
            }
        };
        inputEmail = findViewById(R.id.editEmail);
        inputPassword = findViewById(R.id.editPass);
        btnSignin= findViewById(R.id.btnSign2);
        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (valid()) {
                    String email = inputEmail.getText().toString();
                    final String password = inputPassword.getText().toString();
                    //authenticate user
                    auth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    // If sign in fails, display a message to the user. If sign in succeeds
                                    // the auth state listener will be notified and logic to handle the
                                    // signed in user can be handled in the listener.
                                    if (!task.isSuccessful()) {
                                        // there was an error
                                        if (password.length() < 6) {
                                            inputPassword.setError(getString(R.string.minimum_password));
                                        } else {
                                            Toast.makeText(MainActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        Intent intent = new Intent(MainActivity.this, ManagerActivity.class);
                                        intent.putExtra("email", email);
                                        intent.putExtra("password", password);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                }
            }
        });


    }
    @Override
    protected void onStop() {
        super.onStop();
        if (stateListener != null)
            auth.removeAuthStateListener(stateListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(stateListener);
    }
        private boolean valid() {
            final  String email = inputEmail.getText().toString().trim();
            final String password = inputPassword.getText().toString().trim();

            if (email.isEmpty()) {
                inputEmail.setError("Email không được để trống");
                return false;
            }
            if (password.isEmpty()) {
                inputPassword.setError("Vui lòng nhập mật khẩu");
                return false;
            }
            return true;
        }
}