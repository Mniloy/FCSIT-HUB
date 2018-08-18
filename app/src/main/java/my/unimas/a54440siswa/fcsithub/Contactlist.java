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

public class Contactlist extends AppCompatActivity {

    String UserId;
    ImageView IVLogout,IVSearch, IVProfile;
    ImageView IVback;
    TextView UserName;
    Toolbar toolbar;
    EditText ETSearch;

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



        IVback =  findViewById(R.id.IVback);
        IVLogout= findViewById(R.id.IVLogout);
        UserName= findViewById(R.id.username);
        IVSearch= findViewById(R.id.IVsearch);
        IVProfile=findViewById(R.id.IVProfile);
        IVProfile.setVisibility(View.INVISIBLE);
        IVLogout.setVisibility(View.INVISIBLE);




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

       /* mAuthListener = new FirebaseAuth.AuthStateListener() {
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
        });*/

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
                String userid[] = new String[20];
                String name[] = new String[20];
                String email[] = new String[20];
                String number[] = new String[20];

                lstContact.clear();
                if (dataSnapshot.exists()) {

                    int i = 1;
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.child("Users").getChildren()) {
                        userid[i]= dataSnapshot1.getKey();
                        name[i]=dataSnapshot.child("Users").child(userid[i]).child("userName").getValue(String.class);
                        email[i]=dataSnapshot.child("Users").child(userid[i]).child("userEmail").getValue(String.class);
                        number[i]=dataSnapshot.child("Users").child(userid[i]).child("userNumber").getValue(String.class);

                     //   if(name[i].contains(ETSearch.getText().toString().trim())) {
                            lstContact.add(new Contact(name[i], email[i], number[i]));
                     //   }
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