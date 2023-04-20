package com.motoacademy.norton.viewbinding;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.motoacademy.norton.viewbinding.databinding.ActivityHomeBinding;
import com.motoacademy.norton.viewbinding.databinding.ActivityMainBinding;

public class HomeActivity extends AppCompatActivity {
    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        setupLoggoutButton();
        setupFormNavigation();
    }

    private void setupLoggoutButton() {
        binding.exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent;
                intent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void setupFormNavigation() {
        binding.formNavigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("DEBUG", "NAVIGATE");
                Intent intent;
                intent = new Intent(HomeActivity.this, FormActivity.class);
                startActivity(intent);
            }
        });
    }
}