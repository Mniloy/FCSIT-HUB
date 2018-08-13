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

public class Contactlist extends AppCompatActivity {

    String UserId;
    ImageView IVLogout;
    ImageView IVback;
    TextView UserName;
    Toolbar toolbar;

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseUser user;
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

        toolbar= findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        IVback = (ImageView) findViewById(R.id.IVback);
        IVLogout = (ImageView) findViewById(R.id.IVLogout);
        UserName= findViewById(R.id.username);


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
        user = mAuth.getCurrentUser();
        UserId= user.getUid();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();


        /*----------------------------------------------------------------------------------------*/

        /*-------------------------------- Course List Fetch -------------------------------------*/


        lstContact = new ArrayList<>();

        rootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String userName = dataSnapshot.child("Users").child(UserId).child("userName").getValue(String.class);
                UserName.setText(userName);
                String contactid[] = new String[20];
                String name[] = new String[20];
                String email[] = new String[20];
                String number[] = new String[20];

                lstContact.clear();
                if (dataSnapshot.exists()) {

                    int i = 1;
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.child("Contact").getChildren()) {
                        contactid[i]= dataSnapshot1.getKey();
                        name[i]=dataSnapshot.child("Contact").child(contactid[i]).child("Name").getValue(String.class);
                        email[i]=dataSnapshot.child("Contact").child(contactid[i]).child("Email").getValue(String.class);
                        number[i]=dataSnapshot.child("Contact").child(contactid[i]).child("Number").getValue(String.class);
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

    public boolean onCreateOptionMenu (Menu menu){
        getMenuInflater().inflate(R.menu.searchview, menu);
        return true;
    }




}