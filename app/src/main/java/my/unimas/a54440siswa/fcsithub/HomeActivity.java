package my.unimas.a54440siswa.fcsithub;

import android.content.Intent;
import android.net.Uri;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class HomeActivity extends AppCompatActivity {

    Button BTNFacilities, BTNDirectory, BTNContact, BTNEleap, BTNMessage, BTNPost, BTNNews, BTNAnnouncement;
    ImageButton IVDelete;

    ImageView IVLogout, IVBack, IVProfile, IVSearch;
    String UserId;
    String postusername;
    String password;
    TextView UserName;
    LinearLayout layout;
    EditText ETpost;
    RadioButton Rnews, Rannouncement, Rmedia;


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

        IVBack =findViewById(R.id.IVback);
        IVBack.setVisibility(View.INVISIBLE);
        IVSearch =findViewById(R.id.IVsearch);
        IVSearch.setVisibility(View.INVISIBLE);
        BTNFacilities = (Button) findViewById(R.id.BTNfacilities);
        BTNDirectory = (Button) findViewById(R.id.BTNdirectory);
        BTNContact = (Button) findViewById(R.id.BTNContact);
        BTNEleap = (Button) findViewById(R.id.BTNEleap);
        BTNMessage=(Button) findViewById(R.id.BTNMessage);
        IVLogout = (ImageView) findViewById(R.id.IVLogout);
        ETpost= findViewById(R.id.ETpost);
        BTNPost = findViewById(R.id.BTNpost);
        BTNNews = findViewById(R.id.BTNnews);
        BTNAnnouncement=(Button) findViewById(R.id.BTNAnnouncement);
        Rnews = findViewById(R.id.radio_news);
        Rannouncement = findViewById(R.id.radio_announcement);
        Rmedia = findViewById(R.id.radio_media);
        IVDelete=findViewById(R.id.IVDelete);
        IVProfile= findViewById(R.id.IVProfile);


        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        user = mAuth.getCurrentUser();
        UserId= user.getUid();
        postusername =user.getDisplayName();
        UserName =findViewById(R.id.username);


        BTNPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ETpost.getText().toString().length() == 0) {
                    ETpost.setError("Type Something");
                } else if (ETpost.getText().toString().length() >= 300) {
                    ETpost.setError("Note should be less than 300 characters");
                } else {
                    if (Rnews.isChecked()){

                        String post = ETpost.getText().toString().trim();
                        DatabaseReference postRef = FirebaseDatabase.getInstance().getReference().child("News").push();
                        postRef.child("PostUserId").setValue(UserId);
                        postRef.child("PostUserName").setValue(postusername);
                        postRef.child("Post").setValue(post);
                        postRef.child("PostTime").setValue(getCurrentTime());
                        postRef.child("PostDate").setValue(getCurrentDate());
                        Toast.makeText(HomeActivity.this, "Post Saved", Toast.LENGTH_LONG).show();
                        ETpost.setText("");


                    }else if (Rannouncement.isChecked()){

                        String post = ETpost.getText().toString().trim();
                        DatabaseReference postRef = FirebaseDatabase.getInstance().getReference().child("Announcement").push();
                        postRef.child("PostUserId").setValue(UserId);
                        postRef.child("PostUserName").setValue(postusername);
                        postRef.child("Post").setValue(post);
                        postRef.child("PostTime").setValue(getCurrentTime());
                        postRef.child("PostDate").setValue(getCurrentDate());
                        Toast.makeText(HomeActivity.this, "Post Saved", Toast.LENGTH_LONG).show();
                        ETpost.setText("");
                    }else{
                        Toast.makeText(HomeActivity.this, "Select the Category of your Post", Toast.LENGTH_LONG).show();
                        ETpost.setText("");

                    }



                }
            }
        });



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

        IVProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent userprofile = new Intent (HomeActivity.this, Userprofile.class);
                startActivity(userprofile);
            }
        });

        BTNDirectory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("http://www.unimas.my/student/unimas-map"));
                startActivity(browserIntent);
            }
        });

        BTNContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent contacts = new Intent(HomeActivity.this, Contactlist.class);
                startActivity(contacts);
            }
        });

        BTNEleap.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent browserIntent = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://eleap.unimas.my/"));
                startActivity(browserIntent);
            }
        });

        BTNNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent news = new Intent(HomeActivity.this, NewsList.class);
                startActivity(news);
            }
        });
        BTNAnnouncement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent announcement = new Intent(HomeActivity.this, AnnouncementList.class);
                startActivity(announcement);
            }
        });



    }

    public String getCurrentDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public String getCurrentTime() {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }





}
