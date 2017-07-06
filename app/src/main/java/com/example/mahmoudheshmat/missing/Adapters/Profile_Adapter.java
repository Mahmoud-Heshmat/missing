package com.example.mahmoudheshmat.missing.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mahmoudheshmat.missing.Activites.childDetailed;
import com.example.mahmoudheshmat.missing.DatabaseBackground;
import com.example.mahmoudheshmat.missing.Models.DatabaseURL;
import com.example.mahmoudheshmat.missing.R;
import com.example.mahmoudheshmat.missing.utils.RecyclerViewAnimator;
import com.example.mahmoudheshmat.missing.Models.childItem;
import com.kairos.Kairos;
import com.kairos.KairosListener;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class Profile_Adapter extends RecyclerView.Adapter<Profile_Adapter.ViewHolder>{

    private Context context;
    public List<childItem> items;
    Context contexts;
    private RecyclerViewAnimator mAnimator;

    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    String userID;

    Kairos myKairos;
    KairosListener listener;
    static int pos;

    public Profile_Adapter(Context context, List<childItem> items, RecyclerView recyclerView){
        this.context = context;
        this.items = items;
        mAnimator = new RecyclerViewAnimator(recyclerView);
        sharedPreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        userID = sharedPreferences.getString("User_id", null);
    }

    @Override
    public Profile_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_row, parent, false);
        mAnimator.onCreateViewHolder(view);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Profile_Adapter.ViewHolder holder, final int position) {

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

        holder.location.setText(addressLineIntent + " " +cirtIntent + " "+ countryIntent);

        final String child_id = items.get(position).getChild_id();
        final String image_id = items.get(position).getImage_id();
        final String location_id = items.get(position).getLocation_id();
        final String subject_id = items.get(position).getSubject_id();
        final String key = items.get(position).getKey();

        if(user_idIntent.equals(userID) && key != null){
            holder.deleteImage.setVisibility(View.VISIBLE);
        }else{
            holder.deleteImage.setVisibility(View.GONE);
        }


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

        holder.deleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(R.string.delete_image_title);
                builder.setMessage(R.string.delete_image_sub);
                builder.setPositiveButton(R.string.delete_image_pos, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        pos = position;
                        String type = "deleteimage";
                        DatabaseBackground db = new DatabaseBackground(context);
                        try {
                            String result = db.execute(type, child_id, image_id, location_id, subject_id).get();
                            if(result.equals("Failed")){
                                Log.d("response", result);
                            }else{
                                DeleteSubject(subject_id);
                            }
                        } catch (InterruptedException | ExecutionException e) {
                            e.printStackTrace();
                        }
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });

        mAnimator.onBindViewHolder(holder.itemView, position);

        if(image != null){



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


    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView childImage;
        public TextView deleteImage;
        public TextView location;

        public ViewHolder(View itemView) {
            super(itemView);
            childImage = (ImageView) itemView.findViewById(R.id.childImage);
            deleteImage = (TextView) itemView.findViewById(R.id.deleteImage);
            location = (TextView) itemView.findViewById(R.id.location);
        }
    }

    public void DeleteSubject(String id){
        myKairos = new Kairos();
        myKairos.setAuthentication(context, DatabaseURL.app_id, DatabaseURL.api_key);
        listener = new KairosListener() {
            @Override
            public void onSuccess(String response) {
                items.remove(pos);
                notifyItemRemoved(pos);
                notifyItemRangeChanged(pos,items.size());
            }
            @Override
            public void onFail(String response) {
            }
        };

        try {
            String galleryId = "missingPeople";
            String DeletedSubject = id;
            myKairos.deleteSubject(DeletedSubject, galleryId, listener);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}

