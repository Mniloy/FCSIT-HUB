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
import android.util.Log;
import android.view.View;
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

public class AnnouncementList extends AppCompatActivity {

    String UserId;
    ImageView IVLogout, IVSearch ;
    ImageView IVback;
    TextView UserName;
    Toolbar toolbar;

    CircleImageView CVProfileImage;

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseUser user;
    AnnouncementRecyclerViewAdapter announcementAdapter;
    List<Announcement> lstAnnouncement ;


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
        setContentView(R.layout.announcementlist);
        mAuth = FirebaseAuth.getInstance();

        toolbar= findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        IVback =  findViewById(R.id.IVback);
        IVLogout =  findViewById(R.id.IVLogout);
        CVProfileImage = findViewById(R.id.CVProfile);
        IVSearch =  findViewById(R.id.IVsearch);
        IVSearch.setVisibility(View.INVISIBLE);

        UserName= findViewById(R.id.username);


        final RecyclerView RVAnnouncement = findViewById(R.id.recyclerview_announcement);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    startActivity(new Intent(AnnouncementList.this, HomeActivity.class));
                }
            }
        };

        IVback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent homeactivity = new Intent (AnnouncementList.this, HomeActivity.class);
                startActivity(homeactivity);
            }
        });


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    startActivity(new Intent(AnnouncementList.this, SignIn.class));
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
        DatabaseReference announcementRef = FirebaseDatabase.getInstance().getReference();

        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference mediaRef2 =storageReference.child("profilepic/" +UserId+".jpg");
        /*----------------------------------------------------------------------------------------*/


        GlideApp.with(this /* context */)
                .load(mediaRef2)
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

        lstAnnouncement = new ArrayList<>();

        announcementRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String userName = dataSnapshot.child("Users").child(UserId).child("userName").getValue(String.class);
                UserName.setText(userName);
                String announcementid[] = new String[20];
                String postusername[] = new String[20];
                String post[] = new String[20];
                String posttime[] = new String[20];
                String postdate[] = new String[20];
                String userid[] = new String[30];

                lstAnnouncement.clear();
                if (dataSnapshot.exists()) {

                    int i = 1;
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.child("Announcement").getChildren()) {
                        announcementid[i]= dataSnapshot1.getKey();
                        postusername[i]=dataSnapshot.child("Announcement").child(announcementid[i]).child("PostUserName").getValue(String.class);
                        post[i]=dataSnapshot.child("Announcement").child(announcementid[i]).child("Post").getValue(String.class);
                        posttime[i]=dataSnapshot.child("Announcement").child(announcementid[i]).child("PostTime").getValue(String.class);
                        postdate[i]=dataSnapshot.child("Announcement").child(announcementid[i]).child("PostDate").getValue(String.class);
                        userid[i]=dataSnapshot.child("Announcement").child(announcementid[i]).child("PostUserId").getValue(String.class);

                        lstAnnouncement.add(new Announcement(post[i],postusername[i],posttime[i],postdate[i], announcementid[i], userid[i]));
                        i++;
                    }
                }else{
                    RVAnnouncement.setVisibility(View.GONE);
                }
                announcementAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Hello", "Failed to read value.", error.toException());
            }
        });

        announcementAdapter = new AnnouncementRecyclerViewAdapter(this,lstAnnouncement);
        RVAnnouncement.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,true));
        RVAnnouncement.setAdapter(announcementAdapter);


    }





}