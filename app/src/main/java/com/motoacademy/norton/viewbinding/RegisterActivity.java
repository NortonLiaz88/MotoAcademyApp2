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
import com.motoacademy.norton.viewbinding.databinding.ActivityHomeBinding;
import com.motoacademy.norton.viewbinding.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        setContentView(view);
        setupNavigateRegister();
        setupHandleRegister();
        binding.progressCircular.setVisibility(View.INVISIBLE);
    }

    private void setupNavigateRegister() {
        binding.logNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("DEBUG", "NAVIGATE");
                Intent intent;
                intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void setupHandleRegister() {
        binding.signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email, password, confirmPassword;
                email = binding.emailInput.getText().toString();
                password = binding.passwordInput.getText().toString();
                confirmPassword = binding.confirmPasswordInput.getText().toString();

                if(TextUtils.isEmpty(email)) {
                    Toast.makeText(RegisterActivity.this, "O email não pode estar vazio.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(RegisterActivity.this, "A senha não pode estar vazia.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(confirmPassword))  {
                    Toast.makeText(RegisterActivity.this, "A confirmação de senha não pode estart vazia.", Toast.LENGTH_SHORT).show();
                    return;
                }
                binding.progressCircular.setVisibility(View.VISIBLE);

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("SUCCESS", "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w("FAILED", "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                                binding.progressCircular.setVisibility(View.GONE);
                            }
                        });
            }
        });
    }
}