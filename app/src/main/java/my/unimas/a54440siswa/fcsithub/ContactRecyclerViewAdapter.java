package my.unimas.a54440siswa.fcsithub;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


public class ContactRecyclerViewAdapter extends RecyclerView.Adapter<ContactRecyclerViewAdapter.MyViewHolder> {

    private Context mContext ;
    private List<Contact> mData ;
    FirebaseAuth mAuth;
    FirebaseUser user;
    String UserId;


    public ContactRecyclerViewAdapter(Context mContext, List<Contact> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view ;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.contact,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference mediaRef =storageReference.child("profilepic/" +mData.get(position).getUserId()+".jpg");
        mAuth = FirebaseAuth.getInstance();


        user = mAuth.getCurrentUser();
       // UserId= user.getUid();

        holder.tv_contact_name.setText(mData.get(position).getName());
        holder.tv_contact_email.setText(mData.get(position).getEmail());
        holder.tv_contact_number.setText(mData.get(position).getNumber());

        GlideApp.with(mContext /* context */)
                .load(mediaRef)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.circleImageView.setVisibility(View.GONE);
                        return false;
                    }
                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.circleImageView.setVisibility(View.VISIBLE);
                        return false;
                    }
                })
                .into(holder.circleImageView);


    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void filterList(List<Contact> filteredList) {
        mData = filteredList;
        notifyDataSetChanged();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_contact_name;
        TextView tv_contact_email;
        TextView tv_contact_number;
        CircleImageView circleImageView;

        public MyViewHolder(View itemView) {
            super(itemView);

            circleImageView = itemView.findViewById(R.id.cvcontact);
            tv_contact_name = (TextView) itemView.findViewById(R.id.contact_name) ;
            tv_contact_email = (TextView) itemView.findViewById(R.id.contact_email);
            tv_contact_number = (TextView) itemView.findViewById(R.id.contact_number);


        }
    }


}
