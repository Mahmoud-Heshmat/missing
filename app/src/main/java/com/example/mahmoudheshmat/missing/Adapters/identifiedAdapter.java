package com.example.mahmoudheshmat.missing.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.mahmoudheshmat.missing.Activites.missingDetails;
import com.example.mahmoudheshmat.missing.R;
import com.example.mahmoudheshmat.missing.Activites.childDetailed;
import com.example.mahmoudheshmat.missing.Models.childItem;
import com.squareup.picasso.Picasso;

import java.util.List;


public class identifiedAdapter extends RecyclerView.Adapter<identifiedAdapter.ViewHolder>{

    private Context context;
    public List<childItem> items;
    Context contexts;

    public identifiedAdapter(Context context, List<childItem> items){
        this.context = context;
        this.items = items;
    }


    @Override
    public identifiedAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.identifyrow, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(identifiedAdapter.ViewHolder holder, final int position) {

        final String image = items.get(position).getImagePath();

        Picasso.with(context)
                .load(image)
                .placeholder(R.drawable.common_google_signin_btn_icon_dark_pressed)
                .error(R.drawable.com_facebook_auth_dialog_background)
                .into(holder.childImage);

        holder.childImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contexts = v.getContext();
                Intent intent = new Intent(contexts, missingDetails.class);
                intent.putExtra("type", "identified");
                intent.putExtra("bundle_id", items.get(position).getBundle_id());
                intent.putExtra("ImagePath", image);
                intent.putExtra("user_id", items.get(position).getUser_id());
                contexts.startActivity(intent);

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


    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView childImage;

        public ViewHolder(View itemView) {
            super(itemView);
            childImage = (ImageView) itemView.findViewById(R.id.childImage);
        }
    }
}

