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

public class ChattingRecyclerViewAdapter extends RecyclerView.Adapter {

    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    FirebaseAuth mAuth;
    FirebaseUser user;
    String UserId;
    StorageReference mediaRef;


    String chatpartneruserid, senderId, message, time;

    private Context mContext;
    private List<ChatMessage> mMessageList;

    public ChattingRecyclerViewAdapter(Context context, List<ChatMessage> messageList) {
        mContext = context;
        mMessageList = messageList;
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

    // Determines the appropriate ViewType according to the sender of the message.
    @Override
    public int getItemViewType(int position) {
        chatpartneruserid = mMessageList.get(position).getChatPartnerUserId();
        senderId = mMessageList.get(position).getSenderId();
        message = mMessageList.get(position).getMessage();
        time = mMessageList.get(position).getTime();

        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        mediaRef =storageReference.child("profilepic/" +senderId+".jpg");

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        UserId= user.getUid();

        if (senderId.equals(chatpartneruserid)) {
            // If the current user is the sender of the message
            return VIEW_TYPE_MESSAGE_RECEIVED;
        } else {
            // If some other user sent the message
            return VIEW_TYPE_MESSAGE_SENT;
        }
    }

    // Inflates the appropriate layout according to the ViewType.
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.sentmessage, parent, false);
            return new SentMessageHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.receivedmessage, parent, false);
            return new ReceivedMessageHolder(view);
        }

        return null;
    }


    // Passes the message object to a ViewHolder so that the contents can be bound to UI.
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        String senderId = mMessageList.get(position).getSenderId();

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                ((SentMessageHolder) holder).bind(senderId);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceivedMessageHolder) holder).bind(senderId);
        }
    }

    private class SentMessageHolder extends RecyclerView.ViewHolder {
        TextView TVMessage, TVTime;
        CircleImageView CVUserImage;

        SentMessageHolder(View itemView) {
            super(itemView);
            TVMessage = itemView.findViewById(R.id.tvusermessage);
            TVTime = itemView.findViewById(R.id.tvusertime);
            CVUserImage = itemView.findViewById(R.id.cvuserimage);
        }

        void bind(String senderId) {
            TVMessage.setText(message);
            TVTime.setText(time);

            GlideApp.with(mContext /* context */)
                    .load(mediaRef)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            CVUserImage.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            CVUserImage.setVisibility(View.VISIBLE);
                            return false;
                        }
                    })
                    .into(CVUserImage);

        }

    }

    private class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        TextView TVMessage, TVTime;
        CircleImageView CVSenderImage;

        ReceivedMessageHolder(View itemView) {
            super(itemView);

            TVMessage = itemView.findViewById(R.id.tvsendermessage);
            TVTime = itemView.findViewById(R.id.tvsendertime);
            CVSenderImage = itemView.findViewById(R.id.cvsenderimage);
        }

        void bind(String senderId) {
            TVMessage.setText(message);
            TVTime.setText(time);


            GlideApp.with(mContext /* context */)
                    .load(mediaRef)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            CVSenderImage.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            CVSenderImage.setVisibility(View.VISIBLE);
                            return false;
                        }
                    })
                    .into(CVSenderImage);
        }
    }
}
