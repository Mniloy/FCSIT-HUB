package my.unimas.a54440siswa.fcsithub;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.List;


public class MediaRecyclerViewAdapter extends RecyclerView.Adapter<MediaRecyclerViewAdapter.MyViewHolder> {

    private Context mContext ;
    private List<Media> mData ;

    FirebaseAuth mAuth;
    FirebaseUser user;
    String UserID;



    public MediaRecyclerViewAdapter(Context mContext, List<Media> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view ;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.media,parent,false);
        return new MyViewHolder(view);
    }

    StorageReference storageReference = FirebaseStorage.getInstance().getReference();





    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        StorageReference mediaRef =storageReference.child("Media/" +mData.get(position).getMediaID());

        holder.TVPost.setText(mData.get(position).getPost());
        holder.TVUserName.setText(mData.get(position).getPostUserName());


        GlideApp.with(mContext /* context */)
                .load(mediaRef)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.ImageLoading.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.ImageLoading.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(holder.IVMediaItem);


        holder.TVPostTime.setText(mData.get(position).getPostTime());
        holder.TVPostDate.setText(mData.get(position).getPostDate());
        holder.IVDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth = FirebaseAuth.getInstance();
                FirebaseUser user = mAuth.getCurrentUser();
                UserID = user.getUid();
                if(UserID.equals(mData.get(position).getUserID())){
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                    ref.child("Media").child(mData.get(position).getMediaID()).removeValue();

                }else{
                    Toast.makeText(mContext, "You are not Allowed to Delete this Post", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView TVPost, TVUserName, TVPostTime, TVPostDate, TVMError;
        ImageView IVDelete, IVMediaItem;
        ProgressBar ImageLoading;

        public MyViewHolder(View itemView) {
            super(itemView);

            TVPost = itemView.findViewById(R.id.TVMPost) ;
            TVUserName =  itemView.findViewById(R.id.TVMUserName) ;
            TVPostTime = itemView.findViewById(R.id.TVMPostTime) ;
            TVPostDate =  itemView.findViewById(R.id.TVMPostDate) ;
            IVDelete = itemView.findViewById(R.id.IVDelete);
            TVMError = itemView.findViewById(R.id.TVMError);
            IVMediaItem =itemView.findViewById(R.id.IVmediaitem);
            ImageLoading =itemView.findViewById(R.id.progress_circle);



        }
    }


}
