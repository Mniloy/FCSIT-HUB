package my.unimas.a54440siswa.fcsithub;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class ChatRecyclerViewAdapter extends RecyclerView.Adapter<ChatRecyclerViewAdapter.MyViewHolder> {

    private Context mContext ;
    private List<Chat> mData ;

    FirebaseAuth mAuth;
    FirebaseUser user;


    public ChatRecyclerViewAdapter(Context mContext, List<Chat> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view ;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.chatusers,parent,false);
        return new MyViewHolder(view);
    }

    StorageReference storageReference = FirebaseStorage.getInstance().getReference();


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        StorageReference mediaRef =storageReference.child("profilepic/" +mData.get(position).getChatUserId()+".jpg");
        holder.TVChatUserName.setText(mData.get(position).getChatUserName());


        GlideApp.with(mContext /* context */)
                .load(mediaRef)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.CVChatUser.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.CVChatUser.setVisibility(View.VISIBLE);
                        return false;
                    }
                })
                .into(holder.CVChatUser);

        holder.TVChatUserName.setText(mData.get(position).getChatUserName());
        holder.LVChatUsers.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ChatActivity.class);
                // passing data to the book activit
                intent.putExtra("ChatUserId", mData.get(position).getChatUserId());
                intent.putExtra("ChatUserName", mData.get(position).getChatUserName());
                mContext.startActivity(intent);
            }
        });
    }



    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView TVChatUserName;
        CircleImageView CVChatUser;
        LinearLayout LVChatUsers;


        public MyViewHolder(View itemView) {
            super(itemView);

            TVChatUserName = itemView.findViewById(R.id.tvchatusername) ;
            CVChatUser =  itemView.findViewById(R.id.cvchatuserprofile) ;
            LVChatUsers = itemView.findViewById(R.id.lvchatusers) ;



        }
    }


}
