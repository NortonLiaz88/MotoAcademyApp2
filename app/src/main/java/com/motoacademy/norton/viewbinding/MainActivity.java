package com.motoacademy.norton.viewbinding;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.motoacademy.norton.viewbinding.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        setContentView(view);
        setupNavigationButton();
        setupNavigateRegister();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }


    private void setupNavigationButton() {
        binding.signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email, password, confirmPassword;
                email = binding.emailInput.getText().toString();
                password = binding.passwordInput.getText().toString();

                if(TextUtils.isEmpty(email)) {
                    Toast.makeText(MainActivity.this, "O email não pode estar vazio.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(MainActivity.this, "A senha não pode estar vazia.", Toast.LENGTH_SHORT).show();
                    return;
                }
                binding.progressCircular.setVisibility(View.VISIBLE);

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("SUCCESS", "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Intent intent;
                                    intent = new Intent(MainActivity.this, HomeActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.i("CREDENTIALS", email + password);
                                    Log.w("FAILED", "signInWithEmail:failure", task.getException());
                                    Toast.makeText(MainActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                                binding.progressCircular.setVisibility(View.GONE);

                            }
                        });

            }
        });
    }
    private void setupNavigateRegister() {
        binding.regNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("DEBUG", "NAVIGATE");
                Intent intent;
                intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}