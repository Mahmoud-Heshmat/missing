package com.example.mahmoudheshmat.missing.Adapters;


import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mahmoudheshmat.missing.Activites.Notification_Details;
import com.example.mahmoudheshmat.missing.Models.parent_items;
import com.example.mahmoudheshmat.missing.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NotificationAdapter   extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private Context context;
    private ArrayList<parent_items> items;


    public NotificationAdapter(Context context, ArrayList<parent_items> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_row, parent, false);
        return new NotificationAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NotificationAdapter.ViewHolder holder, final int position) {

        String image =items.get(position).getImagePath2();
        String missingName= items.get(position).getChildName();
        String matchingTime = items.get(position).getTime();
        Picasso.with(context)
                .load(image)
                .into(holder.userImage);

        holder.username.setText(missingName);
        holder.Rtime.setText(matchingTime);

        holder.userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Notification_Details.class);
                intent.putExtra("id", items.get(position).getNotify_id());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView userImage;
        TextView username;
        TextView Rtime;
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);

            userImage = (ImageView) itemView.findViewById(R.id.childImage);
            username = (TextView) itemView.findViewById(R.id.username);
            Rtime = (TextView) itemView.findViewById(R.id.timestamp);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
        }
    }
}