package my.unimas.a54440siswa.fcsithub;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;


public class ContactRecyclerViewAdapter extends RecyclerView.Adapter<ContactRecyclerViewAdapter.MyViewHolder> {

    private Context mContext ;
    private List<Contact> mData ;


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
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.tv_contact_name.setText(mData.get(position).getName());
        holder.tv_contact_email.setText(mData.get(position).getEmail());
        holder.tv_contact_number.setText(mData.get(position).getNumber());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_contact_name;
        TextView tv_contact_email;
        TextView tv_contact_number;

        public MyViewHolder(View itemView) {
            super(itemView);

            tv_contact_name = (TextView) itemView.findViewById(R.id.contact_name) ;
            tv_contact_email = (TextView) itemView.findViewById(R.id.contact_email);
            tv_contact_number = (TextView) itemView.findViewById(R.id.contact_number);


        }
    }


}
