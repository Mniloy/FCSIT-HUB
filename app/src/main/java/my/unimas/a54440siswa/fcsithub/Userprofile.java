package my.unimas.a54440siswa.fcsithub;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class Userprofile extends AppCompatActivity {

    String UserId;
    private ImageView IVLogout,IVSearch, IVProfile;
    ImageView IVback;
    ImageView imageView;
    private Button BTNUpload, BTNChoose;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;
    TextView UserName, UserPName, UserEmail, UserNumber, UserDesignation, UserProgram;
    Toolbar toolbar;
    CircleImageView CVProfileImage;

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseUser user;
    FirebaseStorage storage;
    StorageReference storageReference;




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

        StorageReference storageReference ;
        StorageReference profileImageReference;


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
        BTNChoose= findViewById(R.id.BTNChoose);
        BTNUpload=findViewById(R.id.BTNUpload);
        CVProfileImage = findViewById(R.id.CVProfile);

        IVback.setVisibility(View.INVISIBLE);
        IVSearch=findViewById(R.id.IVsearch);
        IVSearch.setVisibility(View.INVISIBLE);

        imageView = findViewById(R.id.IVProfilepic);

        user = mAuth.getCurrentUser();
        String userId = user.getUid();

        storageReference = FirebaseStorage.getInstance().getReference();
        profileImageReference =storageReference.child("profilepic/" +userId+".jpg");

        GlideApp.with(this /* context */)
                .load(profileImageReference)
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


        GlideApp.with(this /* context */)
                .load(profileImageReference)
                .into(imageView);



        BTNChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // chooseImage();
            }
        });


        BTNUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });


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


    private void uploadImage() {

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(Userprofile.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(Userprofile.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }


}
