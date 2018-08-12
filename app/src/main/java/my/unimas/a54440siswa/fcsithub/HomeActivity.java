package my.unimas.a54440siswa.fcsithub;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class HomeActivity extends AppCompatActivity {

    Button BTNFacilities, BTNDirectory, BTNContact;
    ImageView IVLogout;
    String UserId;
    TextView UserName;

    FirebaseAuth mAuth;
    FirebaseUser user;
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
        setContentView(R.layout.activity_home);
        mAuth = FirebaseAuth.getInstance();

        BTNFacilities = (Button) findViewById(R.id.BTNfacilities);
        BTNDirectory = (Button) findViewById(R.id.BTNdirectory);
        BTNContact = (Button) findViewById(R.id.BTNContact);
        IVLogout = (ImageView) findViewById(R.id.IVLogout);

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        user = mAuth.getCurrentUser();
        UserId= user.getUid();
        UserName =findViewById(R.id.username);

        rootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String userName = dataSnapshot.child("Users").child(UserId).child("userName").getValue(String.class);
                UserName.setText(userName);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Hello", "Failed to read value.", error.toException());
            }
        });


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    startActivity(new Intent(HomeActivity.this, SignIn.class));
                }
            }
        };

        IVLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();

            }
        });

        BTNFacilities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent facilities = new Intent (HomeActivity.this, Facilities.class);
                startActivity(facilities);
            }
        });

        BTNDirectory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent directory = new Intent (HomeActivity.this, Directory.class);
                startActivity(directory);
            }
        });

        BTNContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent contacts = new Intent(HomeActivity.this, Contactlist.class);
                startActivity(contacts);
            }
        });


    }
}
