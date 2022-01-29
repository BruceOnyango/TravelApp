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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ProfileActivity extends AppCompatActivity {

    //Declare instances of views
    private EditText profUserName;
    private ImageButton imageButton;
    private Button doneBtn;
    //Declare a firebase instance
    private FirebaseAuth mAuth;
    //declare a database reference
    private DatabaseReference mDatabaseUser;
    //Declare an instance of the storage reference of the uploaded photo
    private StorageReference mStorageRef;
    //Declare an instance of URI for getting the image from our phone then initialize it to null
    private Uri profileImageUri = null;

    /*create an implicit intent for getting images, declare and initialize the private static
     int that will serve as the request code
     */
    private final static int GALLERY_REQ = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
//inflate the tool bar
        Toolbar toolbar = findViewById(R.id.tool_bar);
        // setSupportActionBar(toolbar);

        //Initialize instances of the views
        profUserName = findViewById(R.id.profUserName);
        imageButton = findViewById(R.id.imagebutton);
        doneBtn = findViewById(R.id.doneBtn);
        //Initialize the instance of firebase authenticaion
        mAuth = FirebaseAuth.getInstance();
        //get the current users Id and pass to a variable
        final String userID = mAuth.getCurrentUser().getUid();
        //initialize database reference and get specific user
        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);
        //initialize firebase storage reference to store profile photo images
        mStorageRef = FirebaseStorage.getInstance().getReference().child("profile_images");
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create an implicit intent for getting the images
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                //set the type to images only
                galleryIntent.setType("image/*");
                //since we need results pass intent and request code to the startactivitforresult() method
                startActivityForResult(galleryIntent, GALLERY_REQ);


            }
        });
        /*after clicking the images we want we want to get the name and the profile photo then save later on
        a database reference
         */

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get the custom display name entered by the user
                final String name = profUserName.getText().toString().trim();
                //validate to ensure that the name and profile image are not null
               if (!TextUtils.isEmpty(name) && profileImageUri !=null) {
                    /*create storage reference node inside the profile_image storage refernce
                   where you will save the profile
                   */
                   StorageReference profileImagePath = mStorageRef.child("profile_images").child(profileImageUri.getLastPathSegment());
                   profileImagePath.putFile(profileImageUri ).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                       @Override
                       public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                           //if upload was successful then download the url
                           if(taskSnapshot.getMetadata()!=null) {
                               if(taskSnapshot.getMetadata().getReference()!=null) {
                                   //get download url
                                   Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                   //call the method add onsuccesslistener to determine if we got the download url
                                   result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                       @Override
                                       public void onSuccess(Uri uri) {
                                           //convert the uri to a string on success
                                           final String profileImage = uri.toString();
                                           //call the method push() to add values to the database of a specific user
                                           mDatabaseUser.push();
                                           //call the method addValueEventListener to publish the additions in the database reference of a specific user
                                           mDatabaseUser.addValueEventListener(new ValueEventListener() {
                                               @Override
                                               public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                   //add profile photo and displayName for the current user
                                                   mDatabaseUser.child("displayName").setValue(name);
                                                   //mDatabaseUser.child("profilePhoto").setValue(profileImage).addOnCompleteListener(new onCompleteListener<Void>() {
                                                   mDatabaseUser.child("profilePhoto").setValue(profileImage).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                       @Override
                                                       public void onComplete(@NonNull Task<Void> task) {
                                                           if(task.isSuccessful()) {
                                                               //show a toast to indicate profile updated
                                                               Toast.makeText(ProfileActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                                                               //launch the login activity
                                                               Intent login = new Intent(ProfileActivity.this, LoginActivity.class);
                                                               startActivity(login);

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
  //  override this method to set the profile image int the image button view
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQ && resultCode == RESULT_OK){
            //get the image selected by the user
            profileImageUri  = data.getData();
            //set in the image button view
            imageButton.setImageURI(profileImageUri);
        }
    }
}
