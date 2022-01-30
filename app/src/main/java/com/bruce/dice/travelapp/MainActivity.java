package com.bruce.dice.travelapp;




import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DatabaseReference likesRef,commentsRef;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private FirebaseAuth mAuth;
    // private FirebaseUser mCurrentUser;
    private FirebaseAuth.AuthStateListener mAuthListener;
    Boolean likeChecker =false;
    private FirebaseRecyclerAdapter adapter;
    String currentUserID =null;

    AlertDialog.Builder comment_alert;
    LayoutInflater inflater;
    private DrawerLayout mDrawerLayout;

    //boolean likeChecker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//inflate the tool bar
        Toolbar toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        //set up the navigation drawer



        comment_alert = new AlertDialog.Builder(this);
        inflater = this.getLayoutInflater();


//initialize recyclerview
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//Reverse the layout so as to display the most recent post at the top
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
//get the database reference where you will fetch posts
// mDatabase = FirebaseDatabase.getInstance().getReference().child("Posts");
//Initialize the database reference where you will store likes
        likesRef = FirebaseDatabase.getInstance().getReference().child("Likes");
        // initialize the database where you will store comments
        commentsRef = FirebaseDatabase.getInstance().getReference().child("Comments");
//get an instance of firebase authentication
        mAuth = FirebaseAuth.getInstance();
//get currently logged in user
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
// if user is not logged in refer him/he ro the register activity
            Intent loginIntent = new Intent(MainActivity.this, RegisterActivity.class);
            loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(loginIntent);
        }
        //set up the navigation drawer
        mDrawerLayout  = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle drawertoggle  = new ActionBarDrawerToggle(this, mDrawerLayout,toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(drawertoggle);
        drawertoggle.syncState();
        //make the navigation drawer icon always appear on the action bar
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //set up the navigation view to listen for the menu item selection
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.kq:
                        Intent kq = new Intent(MainActivity.this, KQActivity.class);
                        startActivity(kq);
                        break;
                    case R.id.jumbojet:
                        Intent jumbojet = new Intent(MainActivity.this, JumboJetActivity.class);
                        startActivity(jumbojet);
                        break;
                    case R.id.fly540:
                        Intent fly540 = new Intent (MainActivity.this, Fly540Activity.class);
                        startActivity(fly540);
                }
                return true;
            }
        });

    }
    @Override
    public void onBackPressed() {

        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)){
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    protected void onStart() {
//
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
//check to see if the user is logged in
        if (currentUser != null) {
//if user is logged in populate the Ui With card views
            updateUI(currentUser);
// Listen to the events on the adapter
            adapter.startListening();
        }
    }
    private void updateUI(final FirebaseUser currentUser) {
//create and initialize an instance of Query that retrieves all posts uploaded
        Query query = FirebaseDatabase.getInstance().getReference().child("Posts");
        Query comments = FirebaseDatabase.getInstance().getReference().child("Comments");
// Create and initialize an instance of Recycler Options passing in your model clas
       /* FirebaseRecyclerOptions<Attics> options1 = new FirebaseRecyclerOptions.Builder<Attic>().
                setQuery(comments, new SnapshotParser<Attic>() {
                    @NonNull
                    @Override

//Create a snapshot of your model

                    public Attics parseSnapshot(@NonNull DataSnapshot snapshot) {
                        return new Attic(snapshot.child("post_comment").getValue().toString());
                               // snapshot.child("desc").getValue().toString(),
                               // snapshot.child("postImage").getValue().toString(),
                               // snapshot.child("desc").getValue().toString(),//set back to displayName
                                // snapshot.child("comment").getValue().toString(),
                             //   snapshot.child("profilePhoto").getValue().toString(),
                              //  snapshot.child("time").getValue().toString(),
                               // snapshot.child("date").getValue().toString());
                    }
                })
                .build();*/
        FirebaseRecyclerOptions<Attic> options = new FirebaseRecyclerOptions.Builder<Attic>().
                setQuery(query, new SnapshotParser<Attic>() {
                    @NonNull
                    @Override

//Create a snapshot of your model

                    public Attic parseSnapshot(@NonNull DataSnapshot dataSnapshot) {
                        return new Attic(
                                dataSnapshot.child("title").getValue().toString(),
                                dataSnapshot.child("desc").getValue().toString(),
                                dataSnapshot.child("postImage").getValue().toString(),
                                dataSnapshot.child("displayName").getValue().toString(),
                                dataSnapshot.child("profilePhoto").getValue().toString(),
                                dataSnapshot.child("time").getValue().toString(),
                                dataSnapshot.child("date").getValue().toString());


                    }
                })
                .build();

// crate a fire base adapter passing in the model, and a View holder
// Create a new ViewHolder as a public inner class that extends RecyclerView.Holder,
        // outside the create , start and update the Ui methods.
        // /Then implement the methods onCreateViewHolder and onBindViewHolder
//Complete all the steps in the AtticViewHolder before proceeding to the methods
        // onCreateViewHolder, and onBindViewHolder
        adapter = new FirebaseRecyclerAdapter<Attic, AtticViewHolder>(options) {
            @NonNull
            @Override
            public AtticViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int
                    viewType) {
//inflate the layout where you have the card view items
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_items, parent,
                        false);
                return new AtticViewHolder(view);
            }
            @Override //change back to protected later at line 130
            public void onBindViewHolder(@NonNull AtticViewHolder holder, int position, @NonNull
                    Attic model) {
// very important for you to get the post key since we will use this to set likes and
                //delete a particular post
                final String post_key = getRef(position).getKey();

//populate the card views with data
                // holder.setCommentPostButton(model.getComment());
                holder.setTitle(model.getTitle());
                holder.setDesc(model.getDesc());
                holder.setPostImage(getApplicationContext(), model.getPostImage());
                holder.setUserName(model.getDisplayName());
                holder.setProfilePhoto(getApplicationContext(),
                        model.getProfilePhoto());
                holder.setTime(model.getTime());
                holder.setDate(model.getDate());
//set a like on a particular post
                holder.setLikeButtonStatus(post_key);
//add on click listener on the a particular post to allow opening this post on a
                // different screen

                holder.post_layout.setOnClickListener(new View.OnClickListener() {
                    @Override

                    public void onClick(View view) {

//launch the screen single post activity on clicking a particular cardview item
//create this activity using the empty activity template

                        Intent singleActivity = new Intent(MainActivity.this, SinglePostActivity.class);
                        // Intent commentIntent = new Intent(MainActivity.this, CommentActivity.class);
                        singleActivity.putExtra("PostID", post_key);
                        // commentIntent.putExtra("PostID", post_key);
                        startActivity(singleActivity);
                    }
                });
// set the onclick listener on the button for liking a post
                holder.likePostButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

// initialize the like checker to true, we are using this boolean variable to determine
                        //      if a post has been liked or dislike
// we declared this variable on to of our activity class
                        likeChecker = true;
//check the currently logged in user using his/her ID
                        FirebaseUser user =
                                FirebaseAuth.getInstance().getCurrentUser();
                        if (user != null) {
                            currentUserID=user.getUid();
                        } else {
                            Toast.makeText(MainActivity.this,"please login",Toast.LENGTH_SHORT).show();
                        }
//Listen to changes in the likes database reference
                        likesRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot
                                                             dataSnapshot) {
                                if (likeChecker.equals(true)) {
// if the current post has a like, associated to the current logged and the user
                                    //  clicks on it again, remove the like, basically this means the user is disliking the post
                                    if (dataSnapshot.child(post_key).hasChild(currentUserID)) {
                                        likesRef.child(post_key).child(currentUserID).removeValue();
                                        likeChecker = false;
                                    } else {
//here the user is liking, set value on the like
                                        likesRef.child(post_key).child(currentUserID).setValue(true);
                                        likeChecker = false;
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });


                    }
                });
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
    @Override
    protected void onStop() {
        super.onStop();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            adapter.stopListening();
        }
    }

    public void navigation(View view) {
        Intent navigation = new Intent(MainActivity.this,NavigationActivity.class);
        startActivity(navigation);
    }


    public class AtticViewHolder extends RecyclerView.ViewHolder{
        //Declare the view objects in the card view
        public TextView post_title;
        public TextView post_desc;
        public ImageView post_image;
        public TextView postUserName;
        public ImageView user_image;
        public TextView post_comment_txtview;
        public TextView postTime;
        public TextView postDate;
        public LinearLayout post_layout;
        public ImageButton likePostButton, commentPostButton;
        public TextView displayLikes;
        //Declare an int variable to hold the count of likes
        int countLikes;
        //Declare a string variable to hold the user ID of currently logged in user
        String currentUserID;
        //Declare an instance of firebase authentication
        FirebaseAuth mAuth;
        //Declare a database reference where you are saving the likes
        DatabaseReference likesRef;
        //create constructor matching super
        public AtticViewHolder(@NonNull View itemView) {
            super(itemView);
//Initialize the card view item objects
            post_title = itemView.findViewById(R.id.post_title_txtview);
            post_desc = itemView.findViewById(R.id.post_desc_txtview);
            post_image = itemView.findViewById(R.id.post_image);
            postUserName = itemView.findViewById(R.id.post_user);
            user_image = itemView.findViewById(R.id.userImage);
            postTime = itemView.findViewById(R.id.time);
            postDate = itemView.findViewById(R.id.date);
            post_layout = itemView.findViewById(R.id.linear_layout_post);
            likePostButton = itemView.findViewById(R.id.like_button);
            post_comment_txtview = itemView.findViewById(R.id.post_comment_txtview);
            commentPostButton = itemView.findViewById(R.id.comment);
            displayLikes = itemView.findViewById(R.id.likes_display);
//Initialize a database reference where you will store the likes
            likesRef = FirebaseDatabase.getInstance().getReference().child("Likes");
        }
        // create yos setters, you will use this setter in you onBindViewHolder method
        // public void setComment(String comment) {commentPostButton.setText(comment); }
        public void setTitle(String title){
            post_title.setText(title);
        }
        public void setDesc(String desc){
            post_desc.setText(desc);
        }
        public void setPostImage(Context ctx, String postImage){
            Picasso.with(ctx).load(postImage).into(post_image);
        }
        public void setUserName(String userName){
            postUserName.setText(userName);
        }
        public void setProfilePhoto(Context context,String profilePhoto){
            Picasso.with(context).load(profilePhoto).into(user_image);
        }
        public void setTime(String time) {
            postTime.setText(time);
        }
        public void setCommentPostButton( String comment) { post_comment_txtview.setText(comment);}

        public void setDate(String date) {
            postDate.setText(date);
        }
        public void setLikeButtonStatus(final String post_key){
//we want to know who has like a particular post, so let's get the user using
            //their user_ID
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                currentUserID = user.getUid();
            } else {

                Toast.makeText(MainActivity.this,"please login",Toast.LENGTH_SHORT).show();
            }
// Listen to changes in the database reference of Likes
            likesRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//define post_key in the in the onBindViewHolder method
//check if a particular post has been liked
                    if(dataSnapshot.child(post_key).hasChild(currentUserID)){
//if liked get the number of likes
                        countLikes=(int) dataSnapshot.child(post_key).getChildrenCount();
//check the image from initial dislike to like
                        likePostButton.setImageResource(R.drawable.like);

// count the like and display them in the textView for likes
                        displayLikes.setText(Integer.toString(countLikes));
                    }else {
//If disliked, get the current number of likes
                        countLikes=(int) dataSnapshot.child(post_key).getChildrenCount();
// set the image resource as disliked

                        likePostButton.setImageResource(R.drawable.dislike);

//display the current number of likes
                        displayLikes.setText(Integer.toString(countLikes));
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
// Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
//implement the functionality of the add icon, so that the user on clicking it
        // launches the post activity
        else if (id == R.id.action_add) {
            Intent postIntent=new Intent(this,PostActivity.class);
            startActivity(postIntent);
// on clicking logout, log the user out
        } else if (id == R.id.logout){
            mAuth.signOut();
            Intent logouIntent = new Intent(MainActivity.this, RegisterActivity.class);
            logouIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(logouIntent);
        }
        return super.onOptionsItemSelected(item);
    }
}
