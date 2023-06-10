package com.prismana.corporation.perpusku;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    TextInputEditText loginEmail, loginPassword;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginEmail = findViewById(R.id.LoginEmail);
        loginPassword = findViewById(R.id.LoginPassword);

        mAuth = FirebaseAuth.getInstance();

        //tombol login buat login
        Button tombolLogin = findViewById(R.id.LoginButton);
        tombolLogin.setOnClickListener(v ->
                //startActivity(new Intent(LoginActivity.this, MainActivity.class)));
                LoginUser());

        TextView menujuDaftar = findViewById(R.id.LoginToRegister);
        menujuDaftar.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));
    }

    private void LoginUser() {
        String Email, Password;
        Email = Objects.requireNonNull(loginEmail.getText()).toString();
        Password = Objects.requireNonNull(loginPassword.getText()).toString();

        if (TextUtils.isEmpty(Email)) {
            loginEmail.setError("Email tidak boleh kosong");
            loginEmail.requestFocus();
        } else if (TextUtils.isEmpty(Password)) {
            loginPassword.setError("Password tidak boleh kosong");
            loginPassword.requestFocus();
        } else {
            mAuth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "Berhasil Login", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    } else {
                        Toast.makeText(LoginActivity.this, "Login Error:" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}