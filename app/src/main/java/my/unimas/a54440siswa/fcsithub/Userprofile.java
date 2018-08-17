package my.unimas.a54440siswa.fcsithub;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class Userprofile extends AppCompatActivity {

    String UserId;
    ImageView IVLogout,IVSearch, IVProfile;
    ImageView IVback;
    TextView UserName, UserPName, UserEmail, UserNumber, UserDesignation, UserProgram;
    Toolbar toolbar;


    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseUser user;




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
        setContentView(R.layout.activity_userprofile);
        mAuth = FirebaseAuth.getInstance();

        toolbar= findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        IVback =  findViewById(R.id.IVback);
        IVLogout = findViewById(R.id.IVLogout);
        UserName= findViewById(R.id.username);
        UserPName= findViewById(R.id.TVpusername);
        UserEmail= findViewById(R.id.TVpuseremail);
        UserDesignation= findViewById(R.id.TVpuserdesignation);
        UserNumber= findViewById(R.id.TVpusernumber);
        UserProgram= findViewById(R.id.TVpuserprogram);

        IVback.setVisibility(View.INVISIBLE);
        IVProfile=findViewById(R.id.IVProfile);
        IVProfile.setVisibility(View.INVISIBLE);
        IVSearch=findViewById(R.id.IVsearch);
        IVSearch.setVisibility(View.INVISIBLE);


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    startActivity(new Intent(Userprofile.this, HomeActivity.class));
                }
            }
        };



        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    startActivity(new Intent(Userprofile.this, SignIn.class));
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
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();





        rootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String userName = dataSnapshot.child("Users").child(UserId).child("userName").getValue(String.class);
                UserName.setText(userName);
                UserPName.setText(userName);
                String userEmail = dataSnapshot.child("Users").child(UserId).child("userEmail").getValue(String.class);
                UserEmail.setText(userEmail);
                String userNumber = dataSnapshot.child("Users").child(UserId).child("userNumber").getValue(String.class);
                UserNumber.setText(userNumber);
                String userDesignation = dataSnapshot.child("Users").child(UserId).child("userDesignation").getValue(String.class);
                UserDesignation.setText(userDesignation);
                String userProgram = dataSnapshot.child("Users").child(UserId).child("userProgram").getValue(String.class);
                UserProgram.setText(userProgram);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Hello", "Failed to read value.", error.toException());
            }
        });



    }






}
