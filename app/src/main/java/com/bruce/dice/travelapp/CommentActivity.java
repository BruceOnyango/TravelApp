package com.bruce.dice.travelapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CommentActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DatabaseReference likesRef,commentsRef;

    private FirebaseAuth mAuth;
    // private FirebaseUser mCurrentUser;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private FirebaseRecyclerAdapter adapter;
    String currentUserID =null;
    String post_key = null;
    AlertDialog.Builder comment_alert;
    LayoutInflater inflater;

    private ImageView singleImage;
    private EditText comment;
    private Button postBtn;
    private TextView singleComment, commentTime,commentDate,singleUID;

    private StorageReference mStorageRef;
    //Declare an instance of the database reference where we will save the post details
    private DatabaseReference databaseRef;

    //declare an instance of thr database reference where we have user details
    private DatabaseReference mDatabaseUsers;
    //Declare a instance of currently logged in user
    private FirebaseUser mCurrentUser;
    //declare and initialize the static code


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        Toolbar toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);


        singleComment = findViewById(R.id.indvcomment);

        singleImage = findViewById(R.id.singleImageview);
        singleUID = findViewById(R.id.uid);


        comment_alert = new AlertDialog.Builder(this);
        inflater = this.getLayoutInflater();
        post_key = getIntent().getExtras().getString("PostID");
        //get the post button
        postBtn = findViewById(R.id.postBtn);
        //initialize the storage reference
        mStorageRef = FirebaseStorage.getInstance().getReference();
        //initialize the database reference/node where you will be storing comments
        databaseRef = FirebaseDatabase.getInstance().getReference().child("Posts");
        //initialize an instance of Firebase Authentication
        mAuth = FirebaseAuth.getInstance();
        //Initialize the instance of the firebase user
        mCurrentUser = mAuth.getCurrentUser();
        //Get currently logged in user
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());

        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        //Reverse the layout so as to display the most recent post at the top
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        commentsRef = FirebaseDatabase.getInstance().getReference().child("Comments");
        //get an instance of firebase authentication
        mAuth = FirebaseAuth.getInstance();
        //get currently logged in user
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
        // if user is not logged in refer him/he ro the register activity
            Intent loginIntent = new Intent(CommentActivity.this, RegisterActivity.class);
            loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(loginIntent);
        }

        databaseRef.child(post_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String comment = (String) dataSnapshot.child("comment").getValue();
                String commentDate = (String) dataSnapshot.child("commentDate").getValue();
                String commentTime = (String) dataSnapshot.child("commentTime").getValue();
                String uid = (String) dataSnapshot.child("uid").getValue();
                String profile_photo = (String) dataSnapshot.child("profilePhoto").getValue();

                singleComment.setText(comment);
                singleUID.setText(uid);


                // singleComment.setText(post_comment);
               // singleDesc.setText(post_desc);
                Picasso.with(CommentActivity.this).load(profile_photo).into(singleImage);
                /*if (mAuth.getCurrentUser().getUid().equals(uid)){
                    deleteBtn.setVisibility(View.VISIBLE);
                }*/
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
}




