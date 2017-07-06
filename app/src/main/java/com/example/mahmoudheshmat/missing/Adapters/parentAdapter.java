package com.example.mahmoudheshmat.missing.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;

import com.example.mahmoudheshmat.missing.Activites.missingDetails;
import com.example.mahmoudheshmat.missing.Filters.CustomFilter;
import com.example.mahmoudheshmat.missing.Filters.CustomFilter_Parent;
import com.example.mahmoudheshmat.missing.R;
import com.example.mahmoudheshmat.missing.utils.RecyclerViewAnimator;
import com.example.mahmoudheshmat.missing.Activites.childDetailed;
import com.example.mahmoudheshmat.missing.Models.childItem;
import com.squareup.picasso.Picasso;

import java.util.List;


public class parentAdapter extends RecyclerView.Adapter<parentAdapter.ViewHolder> implements Filterable {

    private Context context;
    public List<childItem> items;
    Context contexts;

    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    String userID;

    List<childItem> filterList;
    CustomFilter_Parent filter;

    public parentAdapter(Context context, List<childItem> items){
        this.context = context;
        this.items = items;
        this.filterList = items;
        sharedPreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        userID = sharedPreferences.getString("User_id", null);
    }


    @Override
    public parentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.identifyrow, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(parentAdapter.ViewHolder holder, final int position) {

        String image = items.get(position).getImagePath();

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
                intent.putExtra("type", "parent");
                intent.putExtra("subject_id", items.get(position).getBundle_id());
                contexts.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }



    public void publishResult(List<childItem> items) {
        this.items = items;
        notifyDataSetChanged();
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public Filter getFilter() {
        if(filter==null)
        {
            filter=new CustomFilter_Parent(filterList, this);
        }
        return filter;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView childImage;

        public ViewHolder(View itemView) {
            super(itemView);
            childImage = (ImageView) itemView.findViewById(R.id.childImage);
        }
    }
}

