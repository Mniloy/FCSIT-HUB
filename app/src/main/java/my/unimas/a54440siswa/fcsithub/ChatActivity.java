package my.unimas.a54440siswa.fcsithub;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class ChatActivity extends AppCompatActivity {

    ImageView IVLogout, IVBack, IVProfile, IVAttachment, IVbtnMessage;
    EditText ETMessage;

    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseAuth.AuthStateListener mAuthListener;

    ChattingRecyclerViewAdapter chatMessageAdapter;

    String ChatPartnerUserId, ChatPartnerUserName;
    List<ChatMessage> lstChatMessage ;


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
        IVBack =findViewById(R.id.IVback);

        /*------------------------- Receive data From Previous Intent ----------------------------*/
        Intent intent = getIntent();

        ChatPartnerUserId = intent.getExtras().getString("ChatUserId");
        ChatPartnerUserName = intent.getExtras().getString("ChatUserName");

        /*----------------------------------------------------------------------------------------*/

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

        user = mAuth.getCurrentUser();
        final String UserId= user.getUid();
        final String UserName =user.getDisplayName();
        final TextView TVUserName =findViewById(R.id.username);

        StorageReference storageReference = FirebaseStorage.getInstance().getReference();

        final CircleImageView CVChatUser = findViewById(R.id.cvchatuser);
        final TextView TVChatUserName = findViewById(R.id.tvchatusername);
        final RecyclerView RVChatMessage = findViewById(R.id.rvchatmessage);

        ETMessage =findViewById(R.id.etmessage);
        IVbtnMessage = findViewById(R.id.ivbtnmessage);

        TVChatUserName.setText(ChatPartnerUserName);
        StorageReference mediaRef =storageReference.child("profilepic/" +ChatPartnerUserId+".jpg");
        DatabaseReference chatMessageRef = FirebaseDatabase.getInstance().getReference();



        IVbtnMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ETMessage.getText().toString().length() == 0) {
                    ETMessage.setError("Type Something");
                }else{

                    String post = ETMessage.getText().toString().trim();
                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(UserId).child("Chat").child(ChatPartnerUserId).push();
                    userRef.child("message").setValue(post);
                    userRef.child("senderId").setValue(UserId);
                    userRef.child("time").setValue(getCurrentTime());

                    DatabaseReference partnerRef = FirebaseDatabase.getInstance().getReference().child("Users").child(ChatPartnerUserId).child("Chat").child(UserId).push();
                    partnerRef.child("message").setValue(post);
                    partnerRef.child("senderId").setValue(UserId);
                    partnerRef.child("time").setValue(getCurrentTime());
                    ETMessage.setText("");

                }

            }

            });
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



        lstChatMessage = new ArrayList<>();

        chatMessageRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String userName = dataSnapshot.child("Users").child(UserId).child("userName").getValue(String.class);
                TVUserName.setText(userName);

                String chatmessageid[] = new String[30];
                String senderId[] = new String[30];
                String message[] = new String[100];
                String time[] =new String[100];


                lstChatMessage.clear();
                lstChatMessage.clear();
                if (dataSnapshot.exists()) {
                    RVChatMessage.setVisibility(View.VISIBLE);
                    int i = 1;
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.child("Users").child(UserId).child("Chat").child(ChatPartnerUserId).getChildren()) {
                        chatmessageid[i]= dataSnapshot1.getKey();
                        senderId[i]= dataSnapshot.child("Users").child(UserId).child("Chat").child(ChatPartnerUserId).child(chatmessageid[i]).child("senderId").getValue(String.class);
                        message[i]= dataSnapshot.child("Users").child(UserId).child("Chat").child(ChatPartnerUserId).child(chatmessageid[i]).child("message").getValue(String.class);
                        time[i]= dataSnapshot.child("Users").child(UserId).child("Chat").child(ChatPartnerUserId).child(chatmessageid[i]).child("time").getValue(String.class);

                        if(chatmessageid[i] != null && senderId[i] !=null && time[i] !=null ) {
                            lstChatMessage.add(new ChatMessage(ChatPartnerUserId, chatmessageid[i], senderId[i], message[i], time[i]));
                        }
                        i++;
                    }

                }else{
                    RVChatMessage.setVisibility(View.GONE);
                }
                chatMessageAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Hello", "Failed to read value.", error.toException());
            }
        });

        chatMessageAdapter = new ChattingRecyclerViewAdapter(this,lstChatMessage);
        RVChatMessage.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,true));
        RVChatMessage.setAdapter(chatMessageAdapter);






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

        IVBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent homeactivity = new Intent (ChatActivity.this, ChatListActivity.class);
                startActivity(homeactivity);
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

    public String getCurrentTime() {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

}
