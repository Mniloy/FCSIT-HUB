package my.unimas.a54440siswa.fcsithub;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Collections;
import java.util.List;


public class AnnouncementRecyclerViewAdapter extends RecyclerView.Adapter<AnnouncementRecyclerViewAdapter.MyViewHolder> {

    private Context mContext ;
    private List<Announcement> mData ;

    FirebaseAuth mAuth;
    FirebaseUser user;
    String UserID;



    public AnnouncementRecyclerViewAdapter(Context mContext, List<Announcement> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view ;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.announcement,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.TVPost.setText(mData.get(position).getPost());
        holder.TVUserName.setText(mData.get(position).getPostUserName());
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
                    ref.child("Announcement").child(mData.get(position).getAnnouncementID()).removeValue();

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

        TextView TVPost, TVUserName, TVPostTime, TVPostDate, TVAError;
        ImageView IVDelete;

        public MyViewHolder(View itemView) {
            super(itemView);

            TVPost = (TextView) itemView.findViewById(R.id.TVAPost) ;
            TVUserName = (TextView) itemView.findViewById(R.id.TVAUserName) ;
            TVPostTime = (TextView) itemView.findViewById(R.id.TVAPostTime) ;
            TVPostDate = (TextView) itemView.findViewById(R.id.TVAPostDate) ;
            IVDelete = itemView.findViewById(R.id.IVDelete);
            TVAError = itemView.findViewById(R.id.TVAError);



        }
    }


}
