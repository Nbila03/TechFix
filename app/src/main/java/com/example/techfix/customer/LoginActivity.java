package com.example.techfix.customer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.techfix.R;

public class LoginActivity extends AppCompatActivity {

    private EditText edtEmail;
    private EditText edtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);

        Button btnLogin = findViewById(R.id.btnLogin);
        TextView txtCreateAccount = findViewById(R.id.txtCreateAccount);

        btnLogin.setOnClickListener(v -> validateLogin());

        txtCreateAccount.setOnClickListener(v -> {
            Intent intent = new Intent(
                    LoginActivity.this,
                    RegisterActivity.class
            );
            startActivity(intent);
        });
    }

    private void validateLogin() {

        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString();

        if (email.isEmpty()) {
            edtEmail.setError("Email is required");
            edtEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.setError("Enter a valid email");
            edtEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            edtPassword.setError("Password is required");
            edtPassword.requestFocus();
            return;
        }

        // Temporary until backend API is connected
        Intent intent = new Intent(
                LoginActivity.this,
                HomeActivity.class
        );

        startActivity(intent);
        finish();
    }
}
