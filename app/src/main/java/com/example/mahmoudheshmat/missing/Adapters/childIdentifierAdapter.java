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

import com.example.mahmoudheshmat.missing.Filters.CustomFilter;
import com.example.mahmoudheshmat.missing.R;
import com.example.mahmoudheshmat.missing.utils.RecyclerViewAnimator;
import com.example.mahmoudheshmat.missing.Activites.childDetailed;
import com.example.mahmoudheshmat.missing.Models.childItem;
import com.squareup.picasso.Picasso;

import java.util.List;


public class childIdentifierAdapter extends RecyclerView.Adapter<childIdentifierAdapter.ViewHolder> implements Filterable {

    private Context context;
    public List<childItem> items;
    Context contexts;
    private RecyclerViewAnimator mAnimator;

    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    String userID;

    List<childItem> filterList;
    CustomFilter filter;

    public childIdentifierAdapter(Context context, List<childItem> items, RecyclerView recyclerView){
        this.context = context;
        this.items = items;
        //mAnimator = new RecyclerViewAnimator(recyclerView);
        this.filterList = items;
        sharedPreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        userID = sharedPreferences.getString("User_id", null);
    }


    @Override
    public childIdentifierAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.identifyrow, parent, false);
        //mAnimator.onCreateViewHolder(view);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(childIdentifierAdapter.ViewHolder holder, final int position) {

        String image = items.get(position).getImagePath();

        Picasso.with(context)
                .load(image)
                .placeholder(R.drawable.common_google_signin_btn_icon_dark_pressed)
                .error(R.drawable.com_facebook_auth_dialog_background)
                .into(holder.childImage);


        final String childNameIntent = items.get(position).getChildName();
        final String ageIntent = items.get(position).getAge();
        final String skinColorIntent = items.get(position).getSkinColor();
        final String hairColorIntent = items.get(position).getHairColor();
        final String eyeColorIntent = items.get(position).getEyeColor();
        final String lengthIntent = items.get(position).getLength();
        final String LostdateIntent = items.get(position).getLostdate();
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
                intent.putExtra("childName", childNameIntent);
                intent.putExtra("age", ageIntent);
                intent.putExtra("skinColor", skinColorIntent);
                intent.putExtra("hairColor", hairColorIntent);
                intent.putExtra("eyeColor", eyeColorIntent);
                intent.putExtra("length", lengthIntent);
                intent.putExtra("Lostdate", LostdateIntent);
                intent.putExtra("ImagePath", ImagePathIntent);
                intent.putExtra("addressLine", addressLineIntent);
                intent.putExtra("city", cirtIntent);
                intent.putExtra("country", countryIntent);
                intent.putExtra("user_id", user_idIntent);
                intent.putExtra("upload", uploadDateIntent);
                contexts.startActivity(intent);
            }
        });

        //mAnimator.onBindViewHolder(holder.itemView, position);

        if(image != null){



        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public Filter getFilter() {
        if(filter==null)
        {
            filter=new CustomFilter(filterList, this);
        }
        return filter;
    }

    public void publishResult(List<childItem> items) {
        this.items = items;
        notifyDataSetChanged();
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

