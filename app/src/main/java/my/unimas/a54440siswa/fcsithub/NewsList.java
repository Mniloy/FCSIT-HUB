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

public class NewsList extends AppCompatActivity {

    String UserId;
    ImageView IVLogout;
    ImageView IVback;
    TextView UserName;
    TextView PostTime;
    TextView PostDate;
    Toolbar toolbar;

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseUser user;
    NewsRecyclerViewAdapter newsAdapter;
    List<News> lstNews ;


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
        setContentView(R.layout.activity_newslist);
        mAuth = FirebaseAuth.getInstance();

        toolbar= findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        IVback =  findViewById(R.id.IVback);
        IVLogout = findViewById(R.id.IVLogout);
        UserName= findViewById(R.id.username);


        final RecyclerView RVNews = findViewById(R.id.recyclerview_news);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    startActivity(new Intent(NewsList.this, HomeActivity.class));
                }
            }
        };

        IVback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent homeactivity = new Intent (NewsList.this, HomeActivity.class);
                startActivity(homeactivity);
            }
        });


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    startActivity(new Intent(NewsList.this, SignIn.class));
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
        DatabaseReference newsRef = FirebaseDatabase.getInstance().getReference();


        /*----------------------------------------------------------------------------------------*/

        /*-------------------------------- Course List Fetch -------------------------------------*/


        lstNews = new ArrayList<>();

        newsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String userName = dataSnapshot.child("Users").child(UserId).child("userName").getValue(String.class);
                UserName.setText(userName);
                String newsid[] = new String[20];
                String postusername[] = new String[20];
                String post[] = new String[20];
                String posttime[] = new String[20];
                String postdate[] = new String[20];
                String userid[] = new String[30];

                lstNews.clear();
                if (dataSnapshot.exists()) {

                    int i = 1;
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.child("News").getChildren()) {
                        newsid[i]= dataSnapshot1.getKey();
                        postusername[i]=dataSnapshot.child("News").child(newsid[i]).child("PostUserName").getValue(String.class);
                        post[i]=dataSnapshot.child("News").child(newsid[i]).child("Post").getValue(String.class);
                        posttime[i]=dataSnapshot.child("News").child(newsid[i]).child("PostTime").getValue(String.class);
                        postdate[i]=dataSnapshot.child("News").child(newsid[i]).child("PostDate").getValue(String.class);
                        userid[i]=dataSnapshot.child("News").child(newsid[i]).child("PostUserId").getValue(String.class);

                        lstNews.add(new News(post[i],postusername[i],posttime[i],postdate[i], newsid[i], userid[i]));
                        i++;
                    }
                }else{
                    RVNews.setVisibility(View.GONE);
                }
                newsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Hello", "Failed to read value.", error.toException());
            }
        });

        newsAdapter = new NewsRecyclerViewAdapter(this,lstNews);
        RVNews.setLayoutManager(new LinearLayoutManager(this));
        RVNews.setAdapter(newsAdapter);


    }

}