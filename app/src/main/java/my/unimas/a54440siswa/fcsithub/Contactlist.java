package my.unimas.a54440siswa.fcsithub;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class Contactlist extends AppCompatActivity {
    ImageView IVLogout;
    ImageView IVback;


    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

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

        List<Contact> lstContact ;
        IVback = (ImageView) findViewById(R.id.IVback);
        IVLogout = (ImageView) findViewById(R.id.IVLogout);

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


        lstContact = new ArrayList<>();
        lstContact.add(new Contact("Niloy","niloy@gmail.com","01921234072"));
        lstContact.add(new Contact("Ferdous","ferdous@gmail.com","0192322890"));
        lstContact.add(new Contact("Dipu","dipu@gmail.com","01721212098"));
        lstContact.add(new Contact("Mehedi","mehedi@gmail.com","01123456789"));
        lstContact.add(new Contact("Dean","dean@gmail.com","01123456078"));


        RecyclerView myrv = (RecyclerView) findViewById(R.id.recyclerview_id);
        ContactRecyclerViewAdapter myAdapter = new ContactRecyclerViewAdapter(this,lstContact);
        myrv.setLayoutManager(new LinearLayoutManager(this));
        myrv.setAdapter(myAdapter);

        }
}