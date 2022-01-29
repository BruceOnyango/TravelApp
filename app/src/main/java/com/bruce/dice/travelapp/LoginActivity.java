package com.bruce.dice.travelapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private EditText loginEmail, loginPass;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseUsers;
    private Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = findViewById(R.id.tool_bar);
        //initialize the views
        loginBtn = findViewById(R.id.loginBtn);
        loginEmail = findViewById(R.id.login_email);
        loginPass = findViewById(R.id.login_password);
        //initialize the firebase authentication instance

        mAuth = FirebaseAuth.getInstance();
        //initalize the firebase instance where there are the child node users
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        //set on click listener on the login button
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "PROCESSING....", Toast.LENGTH_LONG).show();
                //get the email and password entered by the user
                String email = loginEmail.getText().toString().trim();
                String password = loginPass.getText().toString().trim();

                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                    /* use firebase authentication instance you create and call the method
signInWithEmailAndPassword method passing the email and password you got from the views
//Further call the addOnCompleteListener() method to handle the
Authentication result*/
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                //create a method that will check if the user exists in our database reference
                                checkUserExistence();

                            } else {
                                Toast.makeText(LoginActivity.this, "Couldn't login, User not found", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                } else {
                    Toast.makeText(LoginActivity.this, "Complete all Fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
                //check if user exists
                public void checkUserExistence(){
                    //check the user existence using the user id in the users database reference
                    final String user_id = mAuth.getCurrentUser().getUid();
                    //call the the addValueEventListener on the database reference to determine if current user exists
                    mDatabaseUsers.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot datasnapshot) {
                            if(datasnapshot.hasChild(user_id)){
                                //if the users exist direct the user to the mainactivity
                                Intent mainPage = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(mainPage);

                            }else {
                                //if the user doesn't exist show a toast
                                Toast.makeText(LoginActivity.this, "User not registered!", Toast.LENGTH_SHORT).show();
                            }


                        }

                        @Override
                        public void onCancelled( DatabaseError databaseError) {

                        }
                    });
                }
            }



