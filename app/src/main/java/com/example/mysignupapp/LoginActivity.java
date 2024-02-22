package com.example.mysignupapp;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class LoginActivity extends AppCompatActivity {
    Button btn_login;
    TextView create_acc,forget_pass;
    EditText phone,passwd;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btn_login = findViewById(R.id.log_btn);
        phone = findViewById(R.id.phone);
        passwd = findViewById(R.id.pass);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("user info");
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               String Phone = phone.getText().toString().trim();
               String Passwd = passwd.getText().toString().trim();

               if (Phone.isEmpty() || Passwd.isEmpty()){
                   Toast.makeText(LoginActivity.this, "Please fill in all fields",
                           Toast.LENGTH_SHORT).show();
               } else {
                   // Read from the database
                   databaseReference.addValueEventListener(new ValueEventListener() {
                       @Override
                       public void onDataChange(DataSnapshot dataSnapshot) {
                           // This method is called once with the initial value and again
                           // whenever data at this location is updated.
                           if (dataSnapshot.hasChild(Phone)){
                               String pass = dataSnapshot.child(Phone).child("passwd").getValue(String.class);
                               if (Passwd.equals(pass)){
                                   Intent i = new Intent(getApplicationContext(),WelcomePage.class);
                                   startActivity(i);
                               } else{
                                   Toast.makeText(LoginActivity.this, "Invalid input!",
                                           Toast.LENGTH_SHORT).show();
                               }

                           } else{
                               Toast.makeText(LoginActivity.this, "Invalid input!",
                                       Toast.LENGTH_SHORT).show();

                           }
                       }

                       @Override
                       public void onCancelled(DatabaseError error) {
                           // Failed to read value
                           Log.w(TAG, "Failed to read value.", error.toException());
                       }
                   });
               }
            }
        });
        forget_pass = findViewById(R.id.forgot_pass);


        create_acc=findViewById(R.id.acc_register);
        create_acc.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this,RegisterActivity.class)));
    }
}