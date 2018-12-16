package my.unimas.a54440siswa.fcsithub;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
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
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;


public class HomeActivity extends AppCompatActivity {

    CardView CVFacilities, CVDirectory, CVContact, CVEleap, CVMessage, CVNews, CVAnnouncement, CVMedia;
    Button BTNPost;
    ImageButton IVDelete;
    CircleImageView CVProfileImage;
    ImageView IVLogout, IVBack, IVSearch, IVAttachment;
    String UserId;
    String postusername;
    TextView UserName, TVAttachmentName;
    LinearLayout layout;
    EditText ETpost;
    RadioButton Rnews, Rannouncement, Rmedia;
    ProgressBar mProgressBar;

    private Uri mImageUri;


    private static final int PICK_IMAGE_REQUEST = 1;


    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseAuth.AuthStateListener mAuthListener;

    private StorageTask mUploadTask;


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

        CVEleap =findViewById(R.id.CVeleap);
        CVFacilities =  findViewById(R.id.CVfacilities);
        CVDirectory = findViewById(R.id.CVdirectory);
        CVContact =  findViewById(R.id.CVcontact);
        CVAnnouncement=findViewById(R.id.CVannouncement);
        CVMessage= findViewById(R.id.CVmessage);
        CVNews = findViewById(R.id.CVnews);
        CVMedia= findViewById(R.id.CVMedia);
        IVAttachment=findViewById(R.id.IVAttachment);
        mProgressBar =findViewById(R.id.pmupb);
        TVAttachmentName =findViewById(R.id.TVAttachmentName);

        CVProfileImage = findViewById(R.id.CVProfile);

        IVLogout =  findViewById(R.id.IVLogout);
        ETpost= findViewById(R.id.ETpost);
        BTNPost = findViewById(R.id.BTNpost);


        Rnews = findViewById(R.id.radio_news);
        Rannouncement = findViewById(R.id.radio_announcement);
        Rmedia = findViewById(R.id.radio_media);
        IVDelete=findViewById(R.id.IVDelete);


        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

        user = mAuth.getCurrentUser();
        UserId= user.getUid();
        postusername =user.getDisplayName();
        UserName =findViewById(R.id.username);

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



        BTNPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ETpost.getText().toString().length() == 0) {
                    ETpost.setError("Type Something");
                } else if (ETpost.getText().toString().length() >= 300) {
                    ETpost.setError("Note should be less than 300 characters");
                } else
                    {
                        if (Rnews.isChecked()) {

                            String post = ETpost.getText().toString().trim();
                            DatabaseReference postRef = FirebaseDatabase.getInstance().getReference().child("News").push();
                            postRef.child("PostUserId").setValue(UserId);
                            postRef.child("PostUserName").setValue(postusername);
                            postRef.child("Post").setValue(post);
                            postRef.child("PostTime").setValue(getCurrentTime());
                            postRef.child("PostDate").setValue(getCurrentDate());
                            Toast.makeText(HomeActivity.this, "Post Saved", Toast.LENGTH_LONG).show();
                            ETpost.setText("");


                        } else if (Rannouncement.isChecked()) {

                            String post = ETpost.getText().toString().trim();
                            DatabaseReference postRef = FirebaseDatabase.getInstance().getReference().child("Announcement").push();
                            postRef.child("PostUserId").setValue(UserId);
                            postRef.child("PostUserName").setValue(postusername);
                            postRef.child("Post").setValue(post);
                            postRef.child("PostTime").setValue(getCurrentTime());
                            postRef.child("PostDate").setValue(getCurrentDate());
                            Toast.makeText(HomeActivity.this, "Post Saved", Toast.LENGTH_LONG).show();
                            ETpost.setText("");
                        } else if (Rmedia.isChecked()) {
                             DatabaseReference postRef = FirebaseDatabase.getInstance().getReference().child("Media").push();
                             String postid= postRef.getKey();
                             uploadFile(postRef, postid);
                             }else
                            {
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

        CVFacilities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent facilities = new Intent (HomeActivity.this, Facilities.class);
                startActivity(facilities);
            }
        });

        CVProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent userprofile = new Intent (HomeActivity.this, Userprofile.class);
                startActivity(userprofile);
            }
        });

        CVDirectory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("http://www.unimas.my/student/unimas-map"));
                startActivity(browserIntent);
            }
        });

        CVContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent contacts = new Intent(HomeActivity.this, Contactlist.class);
                startActivity(contacts);
            }
        });

        CVMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent media = new Intent(HomeActivity.this, MediaList.class);
                startActivity(media);
            }
        });

        CVEleap.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent browserIntent = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://eleap.unimas.my/"));
                startActivity(browserIntent);
            }
        });

        CVNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent news = new Intent(HomeActivity.this, NewsList.class);
                startActivity(news);
            }
        });

        IVAttachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            openFileChooser();
            }
        });
        CVAnnouncement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent announcement = new Intent(HomeActivity.this, AnnouncementList.class);
                startActivity(announcement);
            }
        });

        CVMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent message = new Intent(HomeActivity.this, ChatListActivity.class);
                startActivity(message);
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


    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
        TVAttachmentName.setText("Attachment Added for Uploading");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();

           // Picasso.with(this).load(mImageUri).into(mImageView);
        }
    }


    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile(final DatabaseReference postRef, final String postid) {
        if (mImageUri != null) {

            StorageReference mStorageRef = FirebaseStorage.getInstance().getReference().child("Media/").child(postid);

            mUploadTask = mStorageRef.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mProgressBar.setProgress(0);
                                }
                            }, 500);

                            String post = ETpost.getText().toString().trim();
                            postRef.child("PostUserId").setValue(UserId);
                            postRef.child("PostUserName").setValue(postusername);
                            postRef.child("Post").setValue(post);
                            postRef.child("PostTime").setValue(getCurrentTime());
                            postRef.child("PostDate").setValue(getCurrentDate());
                            postRef.child("mediaRef").setValue(postid);
                            Toast.makeText(HomeActivity.this, "Media Upload successful", Toast.LENGTH_LONG).show();
                            Toast.makeText(HomeActivity.this, "Post Saved", Toast.LENGTH_LONG).show();
                            ETpost.setText("");

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(HomeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            mProgressBar.setProgress((int) progress);
                        }
                    });
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

}
