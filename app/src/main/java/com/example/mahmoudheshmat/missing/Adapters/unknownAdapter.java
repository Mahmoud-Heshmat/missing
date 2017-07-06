package com.example.mahmoudheshmat.missing.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.mahmoudheshmat.missing.R;
import com.example.mahmoudheshmat.missing.utils.RecyclerViewAnimator;
import com.example.mahmoudheshmat.missing.Activites.childDetailed;
import com.example.mahmoudheshmat.missing.Models.childItem;
import com.squareup.picasso.Picasso;

import java.util.List;


public class unknownAdapter extends RecyclerView.Adapter<unknownAdapter.ViewHolder>{

    private Context context;
    public List<childItem> items;
    Context contexts;
    private RecyclerViewAnimator mAnimator;

    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    String userID;

    RecyclerView recyclerView;


    public unknownAdapter(Context context, List<childItem> items, RecyclerView recyclerView){
        this.context = context;
        this.items = items;
        mAnimator = new RecyclerViewAnimator(recyclerView);
        this.recyclerView = recyclerView;
        sharedPreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        userID = sharedPreferences.getString("User_id", null);
    }

    @Override
    public unknownAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.childrow, parent, false);
        mAnimator.onCreateViewHolder(view);
        return new unknownAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(unknownAdapter.ViewHolder holder, final int position) {

        Picasso.with(context)
                .load(items.get(position).getImagePath())
                .placeholder(R.drawable.common_google_signin_btn_icon_dark_pressed)
                .error(R.drawable.com_facebook_auth_dialog_background)
                .into(holder.childImage);


        final String ImagePathIntent = items.get(position).getImagePath();
        final String addressLineIntent = items.get(position).getAddressLine();
        final String cirtIntent = items.get(position).getCity();
        final String countryIntent = items.get(position).getCountry();
        final String user_idIntent = items.get(position).getUser_id();
        final String uploadDateIntent = items.get(position).getUploadDate();


        holder.childImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contexts = v.getContext();
                Intent intent = new Intent(contexts, childDetailed.class);
                intent.putExtra("ImagePath", ImagePathIntent);
                intent.putExtra("addressLine", addressLineIntent);
                intent.putExtra("city", cirtIntent);
                intent.putExtra("country", countryIntent);
                intent.putExtra("user_id", user_idIntent);
                intent.putExtra("upload", uploadDateIntent);
                contexts.startActivity(intent);
            }
        });

        mAnimator.onBindViewHolder(holder.itemView, position);
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
        public Button deleteImage;

        public ViewHolder(View itemView) {
            super(itemView);

            childImage = (ImageView) itemView.findViewById(R.id.childImage);

        }
    }

}

