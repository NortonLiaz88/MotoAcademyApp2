package com.motoacademy.norton.viewbinding;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.motoacademy.norton.viewbinding.databinding.ActivityFormBinding;
import com.motoacademy.norton.viewbinding.databinding.ActivityHomeBinding;
import com.motoacademy.norton.viewbinding.domain.Person;

public class FormActivity extends AppCompatActivity {

    private ActivityFormBinding binding;
    private FirebaseAuth mAuth;

    private DatabaseHelper db;
    FirebaseUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFormBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        db = DatabaseHelper.getInstance(FormActivity.this);
        setupSaveButton();
        setupShowButton();
    }

    private void setupSaveButton() {
        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String firstName, lastName;
                firstName = binding.inputFirstName.getText().toString();
                lastName = binding.inputLastName.getText().toString();
                if(TextUtils.isEmpty(firstName)) {
                    Toast.makeText(FormActivity.this, "O primeiro nome não pode estar vazio.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(lastName)){
                    Toast.makeText(FormActivity.this, "O segundo nome não pode estar vazia.", Toast.LENGTH_SHORT).show();
                    return;
                }
                try{
                    Person person = new Person(firstName, lastName, currentUser.getUid());
                    db.addPerson(person);
                } catch (Exception e) {
                    Toast.makeText(FormActivity.this, "Não foi possível salvar os dados", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setupShowButton() {
        binding.btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPerson();
            }
        });
    }

    private void showPerson() {
        binding.btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Person person = db.fetchPerson(currentUser.getUid());
                Log.i("DB PERSON", ""+ person);
//                binding.inputFirstName.setText(person.getFirstName());
//                binding.inputLastName.setText(person.getLastName());
            }
        });

    }
}