package com.bruce.dice.travelapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PostActivity extends AppCompatActivity {
    //Declare the view objects
    private ImageButton imageBtn;
    private EditText textTitle;
    private EditText textDesc;
    private Button postBtn;

    //Declare an instance of the Storage reference where we will upload the post photo
    private StorageReference mStorageRef;
    //Declare an instance of the database reference where we will save the post details
    private DatabaseReference databaseRef;
    //Declare an instance of firebase authentication
    private FirebaseAuth mAuth;
    //declare an instance of thr database reference where we have user details
    private DatabaseReference mDatabaseUsers;
    //Declare a instance of currently logged in user
    private FirebaseUser mCurrentUser;
    //declare and initialize the static code
    private static final int GALLERY_REQUEST_CODE = 2;
    //Declare an Instance of URI for getting images from the phone and initialize to null
    private Uri uri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        //inflate the tool bar
        Toolbar toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        //initialize view objects
        postBtn = findViewById(R.id.postBtn);
        textDesc = findViewById(R.id.textDesc);
        textTitle = findViewById(R.id.textTitle);
        //initialize the storage reference
        mStorageRef = FirebaseStorage.getInstance().getReference();
        //initialize the database reference/node where you will be storing posts
        databaseRef = FirebaseDatabase.getInstance().getReference().child("Posts");
        //initialize an instance of Firebase Authentication
        mAuth = FirebaseAuth.getInstance();
        //Initialize the instance of the firebase user
        mCurrentUser = mAuth.getCurrentUser();
        //Get currently logged in user
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());
        //initialize the image button
        imageBtn = findViewById(R.id.imgBtn);
        //picking image from gallery
        imageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);

            }
        });

        //posting to firebase
        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PostActivity.this, "POSTING...", Toast.LENGTH_SHORT).show();
                //get title and desc from the edit texts
                final String PostTitle = textTitle.getText().toString().trim();
                final String PostDesc = textDesc.getText().toString().trim();
                //get the date and time of the post

                java.util.Calendar calendar = Calendar.getInstance();
                SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
                final String saveCurrentDate = currentDate.format(calendar.getTime());
                java.util.Calendar calendar1 = Calendar.getInstance();
                SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
                final String saveCurrentTime = currentTime.format(calendar1.getTime());
                //check for empty fields

                if (!TextUtils.isEmpty(PostDesc) && !TextUtils.isEmpty(PostTitle)) {
                    //create storage reference node, inside post_image storage reference where you will save the post image
                    StorageReference filepath = mStorageRef.child("post_images").child(uri.getLastPathSegment());

                    /*call the putfile() method and pass the post image
                    then check if the upload was successful

                     */

                    filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //if the upload of the post image was successful get the download uri
                                if (taskSnapshot.getMetadata() != null) {
                                    if (taskSnapshot.getMetadata().getReference() != null) {
                                        //get the download url from your storage use the methods getStorage() and getDownlaodUrl()
                                    Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                    //call the method addOnSuccessListener to determine if we got the download url
                                        result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                //convert the uri to a string on success
                                                final String imageUrl = uri.toString();
                                                Toast.makeText(getApplicationContext(), "Successfully Uploaded", Toast.LENGTH_SHORT).show();

                                                //call the method push() to publish the values on the database reference
                                                final DatabaseReference newPost = databaseRef.push();
                                                //adding post contents to database reference, call addValueEventListener so as to set the value
                                                mDatabaseUsers.addValueEventListener(new ValueEventListener() {

                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                      newPost.child("title").setValue(PostTitle);
                                                      newPost.child("desc").setValue(PostDesc);
                                                      newPost.child("postImage").setValue(imageUrl);
                                                      newPost.child("uid").setValue(mCurrentUser.getUid());
                                                      newPost.child("time").setValue(saveCurrentTime);
                                                      newPost.child("date").setValue(saveCurrentDate);
                                                      //get the profile photo and display name of the person posting
                                                       // newPost.child("displayName").setValue(dataSnapshot.child("displayName").getValue());
                                                        newPost.child("profilePhoto").setValue(dataSnapshot.child("profilePhoto").getValue());
                                                        newPost.child("displayName").setValue(dataSnapshot.child("displayName").getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(Task<Void> task) {
                                                                if( task.isSuccessful()){
                                                            //launch the main activity
                                                                    Intent intent = new Intent(PostActivity.this, MainActivity.class);
                                                                    startActivity(intent);

                                                                }
                                                            }
                                                        });


                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });

                                            }
                                        });
                                    }
                                }
                        }

                    });
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK) {
            //get the image selected by the user
            uri = data.getData();
            //set the image
            imageBtn.setImageURI(uri);
        }
    }
}
