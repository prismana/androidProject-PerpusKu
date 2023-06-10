package com.prismana.corporation.perpusku;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    TextInputEditText daftarEmail, daftarPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        daftarEmail = findViewById(R.id.RegisterEmail);
        daftarPassword = findViewById(R.id.RegisterPassword);

        mAuth = FirebaseAuth.getInstance();

        Button tombolDaftar = findViewById(R.id.RegisterButton);
        //click listerner tombol register
        tombolDaftar.setOnClickListener(v -> CreateUser());

        TextView menujuLogin = findViewById(R.id.RegisterToLogin);
        menujuLogin.setOnClickListener(v -> startActivity(new Intent(RegisterActivity.this, LoginActivity.class)));
    }

    private void CreateUser() {
        String Email, Password;
        Email = Objects.requireNonNull(daftarEmail.getText()).toString();
        Password = Objects.requireNonNull(daftarPassword.getText()).toString();

        if (TextUtils.isEmpty(Email))
        {
            daftarEmail.setError("Email tidak boleh kosong");
            daftarEmail.requestFocus();
        } else if (TextUtils.isEmpty(Password))
        {
            daftarPassword.setError("Password tidak boleh kosong");
            daftarPassword.requestFocus();
        } else
        {
            mAuth.createUserWithEmailAndPassword(Email, Password).addOnCompleteListener(task -> {
                if (task.isSuccessful())
                {
                    Toast.makeText(RegisterActivity.this, "Pendaftaran Berhasil!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                } else
                {
                    Toast.makeText(RegisterActivity.this, "Register error: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}