package com.bruce.dice.travelapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class PersonalActivity extends AppCompatActivity {
    private ImageView singleProfilePhoto;
    private TextView singleUserName, singleDisplayName, singleComment;

    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseUsers;
    private FirebaseUser mCurrentUser;
    private Button deleteBtn;
    private FirebaseAuth mAuth;
    private String currentUserID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);

        singleProfilePhoto = findViewById(R.id.singleImageview);
        singleUserName = findViewById(R.id.singleTitle);
        singleDisplayName = findViewById(R.id.singleDesc);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();


        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());
        mDatabaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String userName = (String) dataSnapshot.child("Username").getValue();
                String displayName = (String) dataSnapshot.child("displayName").getValue();
                String profilePhoto = (String) dataSnapshot.child("profilePhoto").getValue();
                /*String post_uid = (String) dataSnapshot.child("uid").getValue();
                String post_comment = (String) dataSnapshot.child("comment").getValue();*/

                singleUserName.setText(userName);
                singleDisplayName.setText(displayName);
               // singleComment.setText(post_comment);
               // displayName.setText(profilePhoto);
                Picasso.with(PersonalActivity.this).load(profilePhoto).into(singleProfilePhoto);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
