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


public class UsersRecyclerViewAdapter extends RecyclerView.Adapter<UsersRecyclerViewAdapter.MyViewHolder> {

    private Context mContext ;
    private List<Users> mData ;
    FirebaseAuth mAuth;
    FirebaseUser user;

    public UsersRecyclerViewAdapter(Context mContext, List<Users> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view ;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.users,parent,false);
        return new MyViewHolder(view);
    }

    StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        StorageReference mediaRef =storageReference.child("profilepic/" +mData.get(position).getUserID()+".jpg");

        holder.TVUserName.setText(mData.get(position).getUserName());


        GlideApp.with(mContext /* context */)
                .load(mediaRef)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.CVUserImage.setVisibility(View.GONE);
                        return false;
                    }
                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.CVUserImage.setVisibility(View.VISIBLE);
                        return false;
                    }
                })
                .into(holder.CVUserImage);


        holder.TVUserName.setText(mData.get(position).getUserName());
        holder.LVUsers.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ChatActivity.class);
                // passing data to the book activity
                intent.putExtra("ChatUserId", mData.get(position).getUserID());
                intent.putExtra("ChatUserName", mData.get(position).getUserName());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView TVUserName, TVMError;
        CircleImageView CVUserImage;
        LinearLayout LVUsers;

        public MyViewHolder(View itemView) {
            super(itemView);
            TVUserName =  itemView.findViewById(R.id.tvcusername) ;
            LVUsers =itemView.findViewById(R.id.lvcusers);
            CVUserImage = itemView.findViewById(R.id.cvcuserprofile);

        }
    }


}
