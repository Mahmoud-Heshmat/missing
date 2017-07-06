package com.example.mahmoudheshmat.missing.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mahmoudheshmat.missing.Models.Message_type;
import com.example.mahmoudheshmat.missing.R;
import com.example.mahmoudheshmat.missing.Models.chat_items;
import com.squareup.picasso.Picasso;

import java.util.List;

public class chat_message_adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context;
    public List<chat_items> items;

    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    String userID;



    public chat_message_adapter(Context context, List<chat_items> items) {
        this.context = context;
        this.items = items;
        sharedPreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        userID = sharedPreferences.getString("User_id", null);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == Message_type.SENT_TEXT) {
            return new SentTextHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_send_message_text, parent, false));
        } else if (viewType == Message_type.SENT_IMAGE) {
            return new SentImageHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_send_message_img, parent, false));
        } else if (viewType == Message_type.RECEIVED_TEXT) {
            return new ReceivedMessageHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_received_message_text, parent, false));
        } else if (viewType == Message_type.RECEIVED_IMAGE) {
            return new ReceivedImageHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_received_message_img, parent, false));
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        //ViewHolder viewHolder = (ViewHolder) holder;

        int viewType = getItemViewType(position);

        if (viewType == Message_type.SENT_TEXT) {

            SentTextHolder sentTextHolder = (SentTextHolder) holder;
            sentTextHolder.time.setText(items.get(position).getTimeStamp());
            sentTextHolder.MessageContent.setText(items.get(position).getContent());

        } else if (viewType == Message_type.SENT_IMAGE) {

            SentImageHolder sentImageHolder = (SentImageHolder) holder;
            sentImageHolder.time.setText(items.get(position).getTimeStamp());
            Picasso.with(context)
                    .load(items.get(position).getContent())
                    .into(sentImageHolder.image);

        } else if (viewType == Message_type.RECEIVED_TEXT) {

            ReceivedMessageHolder receivedMessageHolder = (ReceivedMessageHolder) holder;
            receivedMessageHolder.time.setText(items.get(position).getTimeStamp());
            receivedMessageHolder.Username.setText(items.get(position).getUser_name());
            receivedMessageHolder.MessageContent.setText(items.get(position).getContent());

        } else if (viewType == Message_type.RECEIVED_IMAGE) {

            ReceivedImageHolder receivedImageHolder = (ReceivedImageHolder) holder;
            receivedImageHolder.time.setText(items.get(position).getTimeStamp());
            receivedImageHolder.Username.setText(items.get(position).getUser_name());
            Picasso.with(context)
                    .load(items.get(position).getContent())
                    .into(receivedImageHolder.image);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    @Override
    public int getItemViewType(int position) {


        if (Integer.valueOf(userID) == Integer.parseInt(items.get(position).getUser_id())) {

            if (items.get(position).getType().equals("1")) {
                return Message_type.SENT_TEXT;
            } else if (items.get(position).getType().equals("2")) {
                return Message_type.SENT_IMAGE;
            }
        } else {
            if (items.get(position).getType().equals("1")) {
                return Message_type.RECEIVED_TEXT;
            } else if (items.get(position).getType().equals("2")) {
                return Message_type.RECEIVED_IMAGE;
            }

        }
        return super.getItemViewType(position);
    }


    // sent message holders
    class SentTextHolder extends RecyclerView.ViewHolder {
        TextView time;
        TextView MessageContent;
        public SentTextHolder(View itemView) {
            super(itemView);
            time = (TextView) itemView.findViewById(R.id.tv_time);
            MessageContent = (TextView) itemView.findViewById(R.id.tv_message_content);
        }
    }

    // sent message with type image
    class SentImageHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView time;
        public SentImageHolder(View itemView) {
            super(itemView);
            time = (TextView) itemView.findViewById(R.id.tv_time);
            image = (ImageView) itemView.findViewById(R.id.img_msg);
        }
    }

    // received message holders
    class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        TextView Username;
        TextView time;
        TextView MessageContent;

        public ReceivedMessageHolder(View itemView) {
            super(itemView);
            Username = (TextView) itemView.findViewById(R.id.tv_username);
            time = (TextView) itemView.findViewById(R.id.tv_time);
            MessageContent = (TextView) itemView.findViewById(R.id.tv_message_content);
        }
    }

    // received message with type image
    class ReceivedImageHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView Username;
        TextView time;
        public ReceivedImageHolder(View itemView) {
            super(itemView);
            Username = (TextView) itemView.findViewById(R.id.tv_username);
            time = (TextView) itemView.findViewById(R.id.tv_time);
            image = (ImageView) itemView.findViewById(R.id.img_msg);
        }
    }


}