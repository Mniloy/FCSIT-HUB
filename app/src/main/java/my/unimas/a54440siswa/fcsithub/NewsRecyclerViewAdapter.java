package my.unimas.a54440siswa.fcsithub;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


public class NewsRecyclerViewAdapter extends RecyclerView.Adapter<NewsRecyclerViewAdapter.MyViewHolder> {

    private Context mContext ;
    private List<News> mData ;


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
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.TVPost.setText(mData.get(position).getPost());
        holder.TVUserName.setText(mData.get(position).getPostUserName());
        holder.TVPostTime.setText(mData.get(position).getPostTime());
        holder.TVPostDate.setText(mData.get(position).getPostDate());


    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView TVPost, TVUserName,TVPostTime, TVPostDate;
        ImageView IVDelete;

        public MyViewHolder(View itemView) {
            super(itemView);

            TVPost = (TextView) itemView.findViewById(R.id.TVNPost) ;
            TVUserName = (TextView) itemView.findViewById(R.id.TVNUserName) ;
            TVPostTime = (TextView) itemView.findViewById(R.id.TVNPostTime) ;
            TVPostDate = (TextView) itemView.findViewById(R.id.TVNPostDate) ;
            IVDelete = itemView.findViewById(R.id.IVDelete);



        }
    }


}
