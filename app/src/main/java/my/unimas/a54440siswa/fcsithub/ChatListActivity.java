package my.unimas.a54440siswa.fcsithub;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatListActivity extends AppCompatActivity {

    String UserId;
    ImageView IVLogout, IVProfile, IVSearch ;
    ImageView IVback;
    TextView UserName;
    Toolbar toolbar;

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseUser user;
    ChatRecyclerViewAdapter chatAdapter;
    UsersRecyclerViewAdapter usersAdapter;
    List<Chat> lstChat ;
    List<Users> lstUsers ;


    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mAuthListener != null)
            mAuth.removeAuthStateListener(mAuthListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatlist);
        mAuth = FirebaseAuth.getInstance();

        toolbar= findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        IVback =  findViewById(R.id.IVback);
        IVLogout =  findViewById(R.id.IVLogout);
        IVLogout.setVisibility(View.INVISIBLE);
        IVProfile =  findViewById(R.id.IVProfile);
        IVProfile.setVisibility(View.INVISIBLE);
        IVSearch =  findViewById(R.id.IVsearch);
        IVSearch.setVisibility(View.INVISIBLE);

        UserName= findViewById(R.id.username);


        final RecyclerView RVUsers = findViewById(R.id.recyclerview_users);
        final RecyclerView RVChat = findViewById(R.id.recyclerview_chat);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    startActivity(new Intent(ChatListActivity.this, HomeActivity.class));
                }
            }
        };

        IVback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent homeactivity = new Intent (ChatListActivity.this, HomeActivity.class);
                startActivity(homeactivity);
            }
        });


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    startActivity(new Intent(ChatListActivity.this, SignIn.class));
                }
            }
        };

        IVLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
            }
        });

        /* ------------------------------ Firebase Elements --------------------------------------*/
        user = mAuth.getCurrentUser();
        UserId= user.getUid();
        DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference();


        /*----------------------------------------------------------------------------------------*/

        /*-------------------------------- Course List Fetch -------------------------------------*/


        lstChat = new ArrayList<>();

        chatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String userName = dataSnapshot.child("Users").child(UserId).child("userName").getValue(String.class);
                UserName.setText(userName);

                String chatid[] = new String[30];
                String chatuserid[] = new String[30];
                String chatusername[] = new String[30];


                lstChat.clear();
                lstUsers.clear();
                if (dataSnapshot.exists()) {
                    RVChat.setVisibility(View.VISIBLE);
                    int i = 1;
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.child("Users").child(UserId).child("Chat").getChildren()) {
                        chatid[i]= dataSnapshot1.getKey();
                        chatuserid[i]=dataSnapshot.child("Users").child(UserId).child("Chat").child(chatid[i]).child("userId").getValue(String.class);
                        chatusername[i]= dataSnapshot.child("Users").child(UserId).child("Chat").child(chatid[i]).child("userName").getValue(String.class);

                        lstChat.add(new Chat(chatid[i],chatuserid[i],chatusername[i]));
                        i++;
                    }

                }else{
                    RVChat.setVisibility(View.GONE);
                }
                chatAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Hello", "Failed to read value.", error.toException());
            }
        });



        lstUsers = new ArrayList<>();
        chatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String userName = dataSnapshot.child("Users").child(UserId).child("userName").getValue(String.class);
                UserName.setText(userName);

                String chatid[] = new String[30];
                String chatuserid[] = new String[30];
                String chatusername[] = new String[30];

                lstUsers.clear();
                if (dataSnapshot.exists()) {

                    int i = 1;
                    for (DataSnapshot dataSnapshot2 : dataSnapshot.child("Users").getChildren()) {
                        chatuserid[i]= dataSnapshot2.getKey();
                        chatusername[i]=dataSnapshot.child("Users").child(chatuserid[i]).child("userName").getValue(String.class);
                        lstUsers.add(new Users(chatuserid[i],chatusername[i]));
                        i++;
                    }

                }else{
                    RVUsers.setVisibility(View.GONE);
                }
                usersAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Hello", "Failed to read value.", error.toException());
            }
        });



        usersAdapter = new UsersRecyclerViewAdapter(this,lstUsers);
        RVUsers.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
        RVUsers.setAdapter(usersAdapter);


        chatAdapter = new ChatRecyclerViewAdapter(this,lstChat);
        RVChat.setLayoutManager(new LinearLayoutManager(this));
        RVChat.setAdapter(chatAdapter);




    }





}