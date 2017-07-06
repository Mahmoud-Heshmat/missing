package com.example.mahmoudheshmat.missing.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mahmoudheshmat.missing.Activites.Notification_Details;
import com.example.mahmoudheshmat.missing.R;
import com.example.mahmoudheshmat.missing.utils.RecyclerViewAnimator;
import com.example.mahmoudheshmat.missing.Models.childItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class notifyAdapter extends RecyclerView.Adapter<notifyAdapter.ViewHolder>{

    private Context context;
    public List<childItem> items;
    private RecyclerViewAnimator mAnimator;


    public notifyAdapter(Context context, List<childItem> items) {
        this.context = context;
        this.items = items;
        //mAnimator = new RecyclerViewAnimator(recyclerView);
    }

    @Override
    public notifyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_row, parent, false);
        //mAnimator.onCreateViewHolder(view);
        return new notifyAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(notifyAdapter.ViewHolder holder, final int position) {

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
        //mAnimator.onBindViewHolder(holder.itemView, position);

    }



    @Override
    public int getItemCount() {
        return items.size();
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void updateNotifcation(ArrayList list){
        this.items = list;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView userImage;
        public TextView username;
        public TextView Rtime;
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