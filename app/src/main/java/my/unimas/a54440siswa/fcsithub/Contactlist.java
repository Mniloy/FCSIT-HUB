package my.unimas.a54440siswa.fcsithub;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Contactlist extends AppCompatActivity {
    String userID;
    ImageView IVLogout;
    ImageView IVback;

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseUser user;
    DatabaseReference userRef;
    ContactRecyclerViewAdapter contactAdapter;
    List<Contact> lstContact ;


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
        setContentView(R.layout.activity_contactlist);
        mAuth = FirebaseAuth.getInstance();


        IVback = (ImageView) findViewById(R.id.IVback);
        IVLogout = (ImageView) findViewById(R.id.IVLogout);
        final RecyclerView RVContact = findViewById(R.id.recyclerview_id);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    startActivity(new Intent(Contactlist.this, HomeActivity.class));
                }
            }
        };

        IVback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent homeactivity = new Intent (Contactlist.this, HomeActivity.class);
                startActivity(homeactivity);
            }
        });


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    startActivity(new Intent(Contactlist.this, SignIn.class));
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
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        userRef = rootRef.child("Users");
        /*----------------------------------------------------------------------------------------*/

        /*-------------------------------- Course List Fetch -------------------------------------*/


        lstContact = new ArrayList<>();

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String contactid[] = new String[10];
                String name[] = new String[10];
                String email[] = new String[10];
                String number[] = new String[10];

                lstContact.clear();
                if (dataSnapshot.exists()) {

                    int i = 1;
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.child("Contact").getChildren()) {
                        contactid[i]= dataSnapshot1.getKey();
                        name[i]=dataSnapshot.child("Contact").child(name[i]).getValue(String.class);
                        email[i]=dataSnapshot.child("Contact").child(email[i]).getValue(String.class);
                        number[i]=dataSnapshot.child("Contact").child(number[i]).getValue(String.class);
                        lstContact.add(new Contact(name[i],email[i],number[i]));
                        i++;
                    }
                }else{
                    RVContact.setVisibility(View.GONE);
                }
                contactAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Hello", "Failed to read value.", error.toException());
            }
        });

        contactAdapter = new ContactRecyclerViewAdapter(this,lstContact);
        RVContact.setLayoutManager(new LinearLayoutManager(this));
        RVContact.setAdapter(contactAdapter);

       }
}