package com.example.socialmedia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.socialmedia.databinding.ActivitySignUpBinding;
import com.example.socialmedia.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {
    ActivitySignUpBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });


        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();


        binding.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.createUserWithEmailAndPassword(binding.Email.getText().toString(), binding.pass.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    UserModel userModel;
                                    String name=binding.name.getText().toString();
                                    String Email=binding.Email.getText().toString();
                                    String pass=binding.pass.getText().toString();
                                    String profesion=binding.profesion.getText().toString();
                                userModel=new UserModel(name,Email,pass,profesion);

                                String UserID=task.getResult().getUser().getUid();

                                    Toast.makeText(SignUpActivity.this,name+"  " +Email+"  "+pass+"  "+profesion , Toast.LENGTH_SHORT).show();

                                    database.getReference().child("Users").child(UserID).setValue(userModel);

                                    startActivity(new Intent(SignUpActivity.this,MainActivity.class));
                                } else {

                                    Toast.makeText(SignUpActivity.this, "not crated", Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
            }
        });

    }
}