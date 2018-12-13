package my.unimas.a54440siswa.fcsithub;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;


public class ChatActivity extends AppCompatActivity {

    ImageView IVLogout, IVBack, IVProfile, IVSearch, IVAttachment;

    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseAuth.AuthStateListener mAuthListener;

    String ChatUserId, ChatUserName;


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
        setContentView(R.layout.activity_chat);
        mAuth = FirebaseAuth.getInstance();

        IVLogout =  findViewById(R.id.IVLogout);
        IVProfile= findViewById(R.id.IVProfile);

        /*------------------------- Receive data From Previous Intent ----------------------------*/
        Intent intent = getIntent();

        ChatUserId = intent.getExtras().getString("ChatUserId");
        ChatUserName = intent.getExtras().getString("ChatUserName");

        /*----------------------------------------------------------------------------------------*/

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

        user = mAuth.getCurrentUser();
        String UserId= user.getUid();
        String postusername =user.getDisplayName();
        TextView UserName =findViewById(R.id.username);

        StorageReference storageReference = FirebaseStorage.getInstance().getReference();

        final CircleImageView CVChatUser = findViewById(R.id.cvchatuser);
        TextView TVChatUserName = findViewById(R.id.tvchatusername);

        TVChatUserName.setText(ChatUserName);
        StorageReference mediaRef =storageReference.child("profilepic/" +ChatUserId+".jpg");



        GlideApp.with( this)
                .load(mediaRef)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                       CVChatUser.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        CVChatUser.setVisibility(View.VISIBLE);
                        return false;
                    }
                })
                .into(CVChatUser);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    startActivity(new Intent(ChatActivity.this, SignIn.class));
                }
            }
        };

        IVLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();

            }
        });


        IVProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent userprofile = new Intent (ChatActivity.this, Userprofile.class);
                startActivity(userprofile);
            }
        });



    }

}
