package com.example.mysignupapp;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {

    EditText edtfName, edtlName, edtEmail, edtPhone, edtPasswd, edtconfPasswd;
    Button btnRegister;
    TextView btn;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    NewUser newUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        edtfName = findViewById(R.id.fname);
        edtlName = findViewById(R.id.lname);
        edtEmail = findViewById(R.id.email);
        edtPhone = findViewById(R.id.phone);
        edtPasswd = findViewById(R.id.pass);
        edtconfPasswd = findViewById(R.id.confpass);
        btnRegister = findViewById(R.id.register);

        // Write a message to the database
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("user info");
        newUser = new NewUser();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Pass = edtPasswd.getText().toString();
                String confPass = edtconfPasswd.getText().toString();
                String fName = edtfName.getText().toString().trim();
                String lName = edtlName.getText().toString().trim();
                String Email = edtEmail.getText().toString().trim();
                String Phone = edtPhone.getText().toString().trim();

                if (fName.isEmpty() || lName.isEmpty() || Email.isEmpty() || Phone.isEmpty()
                        || Pass.isEmpty() || confPass.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Fill in the fields",
                            Toast.LENGTH_LONG).show();
                } else if (Pass.equals(confPass)) {
                        addNewUser(fName,lName,Email,Phone,Pass);
                        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(i);

                } else {
                    Toast.makeText(getApplicationContext(), "Password do not match.",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

    }
    public void addNewUser(String fName, String lName, String Email, String Phone, String Pass){
        newUser.setfName(fName);
        newUser.setlName(lName);
        newUser.setEmail(Email);
        newUser.setPhone(Phone);
        newUser.setPasswd(Pass);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(Phone)){
                    Toast.makeText(RegisterActivity.this, "User already exist!",
                            Toast.LENGTH_SHORT).show();
                }else {
                databaseReference.child(Phone).setValue(newUser);
                Toast.makeText(getApplicationContext(),"User registered successfully!",
                        Toast.LENGTH_SHORT).show();}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"Error! user registration failed!",
                        Toast.LENGTH_SHORT).show();

            }
        });
    }
}