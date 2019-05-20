package my.unimas.a54440siswa.fcsithub;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Contactlist extends AppCompatActivity {

    String UserId;
    ImageView IVLogout,IVSearch;
    ImageView IVback;
    TextView UserName;
    Toolbar toolbar;
    EditText ETSearch;

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseUser user;
    ContactRecyclerViewAdapter contactAdapter;
    List<Contact> lstContact ;
    DatabaseReference rootRef;

    RecyclerView RVContact;
    Animation RightToLeft;

    CircleImageView CVProfileImage;


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

        RightToLeft = AnimationUtils.loadAnimation(this,R.anim.rightleft);

        IVback =  findViewById(R.id.IVback);
        IVLogout= findViewById(R.id.IVLogout);
        UserName= findViewById(R.id.username);
        IVSearch= findViewById(R.id.IVsearch);
        CVProfileImage = findViewById(R.id.CVProfile);


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

        IVSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ETSearch.setAnimation(RightToLeft);
                ETSearch.setVisibility(View.VISIBLE);
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
        rootRef = FirebaseDatabase.getInstance().getReference();
        createContactList();
        buildRecyclerView();

        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference mediaRef =storageReference.child("profilepic/" +UserId+".jpg");
        /*----------------------------------------------------------------------------------------*/


        GlideApp.with(this /* context */)
                .load(mediaRef)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        CVProfileImage.setVisibility(View.GONE);
                        return false;
                    }
                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        CVProfileImage.setVisibility(View.VISIBLE);
                        return false;
                    }
                })
                .into(CVProfileImage);


        /*----------------------------------------------------------------------------------------*/

        ETSearch = findViewById(R.id.edittext);
        ETSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

    }



    private void createContactList() {
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

                        lstContact.add(new Contact(userid[i],name[i], email[i], number[i]));
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

    }

    private void buildRecyclerView() {
        RVContact = findViewById(R.id.recyclerview_id);
       // RVContact.setHasFixedSize(true);
        contactAdapter = new ContactRecyclerViewAdapter(this,lstContact);
        RVContact.setLayoutManager(new LinearLayoutManager(this));
        RVContact.setAdapter(contactAdapter);
    }


    public void filter(final String text ) {
        final List <Contact> newlstContact = new ArrayList<>();
        rootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String userName = dataSnapshot.child("Users").child(UserId).child("userName").getValue(String.class);
                UserName.setText(userName);
                String userid[] = new String[20];
                String name[] = new String[20];
                String email[] = new String[20];
                String number[] = new String[20];

                newlstContact.clear();
                if (dataSnapshot.exists()) {
                    int i = 1;
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.child("Users").getChildren()) {
                        userid[i]= dataSnapshot1.getKey();
                        name[i]=dataSnapshot.child("Users").child(userid[i]).child("userName").getValue(String.class);
                        email[i]=dataSnapshot.child("Users").child(userid[i]).child("userEmail").getValue(String.class);
                        number[i]=dataSnapshot.child("Users").child(userid[i]).child("userNumber").getValue(String.class);
                  //      search(text,userid[i], name[i], email[i], number[i]);

                        if (name[i].toLowerCase().contains(text.toLowerCase())) {
                            Log.w("new", name[i]);
                            newlstContact.add(new Contact(userid[i],name[i],email[i],number[i]));

                            Log.w("new", newlstContact.toString());
                        }
                        i++;
                    }
                }else{
                  //  RVContact.setVisibility(View.GONE);
                }
                contactAdapter.filterList(newlstContact);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Hello", "Failed to read value.", error.toException());
            }
        });
    }

}