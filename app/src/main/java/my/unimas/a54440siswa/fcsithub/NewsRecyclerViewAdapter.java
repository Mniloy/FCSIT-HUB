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

import java.util.List;


public class NewsRecyclerViewAdapter extends RecyclerView.Adapter<NewsRecyclerViewAdapter.MyViewHolder> {

    private Context mContext ;
    private List<News> mData ;

    FirebaseAuth mAuth;
    FirebaseUser user;
    String UserID;



    public NewsRecyclerViewAdapter(Context mContext, List<News> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view ;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.news,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

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
                    ref.child("News").child(mData.get(position).getNewsID()).removeValue();

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
    public void filterList(List<News> filteredList) {
        mData = filteredList;
        notifyDataSetChanged();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView TVPost, TVUserName,TVPostTime, TVPostDate, TVNError;
        ImageView IVDelete;

        public MyViewHolder(View itemView) {
            super(itemView);

            TVPost = (TextView) itemView.findViewById(R.id.TVNPost) ;
            TVUserName = (TextView) itemView.findViewById(R.id.TVNUserName) ;
            TVPostTime = (TextView) itemView.findViewById(R.id.TVNPostTime) ;
            TVPostDate = (TextView) itemView.findViewById(R.id.TVNPostDate) ;
            IVDelete = itemView.findViewById(R.id.IVDelete);
            TVNError = itemView.findViewById(R.id.TVNError);


        }
    }


}
