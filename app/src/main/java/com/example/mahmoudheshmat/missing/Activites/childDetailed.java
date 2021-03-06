package com.example.mahmoudheshmat.missing.Activites;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.mahmoudheshmat.missing.Models.DatabaseURL;
import com.example.mahmoudheshmat.missing.R;
import com.example.mahmoudheshmat.missing.utils.Singleton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class childDetailed extends AppCompatActivity {

    ImageView childImage;
    TextView childName;
    TextView childage;
    TextView childSkinColor;
    TextView childHairColor;
    TextView childEyeColor;
    TextView childlength;
    TextView childLostdate;
    TextView childTimer;
    TextView Address;
    ImageView chat_image;

    Context context;

    LinearLayout linearLayout;


    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;

    String reciver_id ;
    String sender_id ;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_detailed);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.child_details));
        toolbar.setNavigationIcon(R.drawable.ic_action_navigation_arrow_back_inverted); // your drawable
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Implemented by activity
            }
        });

        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String chat_userid = sharedPreferences.getString("User_id", null);

        context = this;

        childImage = (ImageView) findViewById(R.id.DchildImage);
        childName = (TextView) findViewById(R.id.DchildName);
        childage = (TextView) findViewById(R.id.DAge);
        childSkinColor = (TextView) findViewById(R.id.DSkincolor);
        childHairColor = (TextView) findViewById(R.id.DHaircolor);
        childEyeColor = (TextView) findViewById(R.id.DEyecolor);
        childlength = (TextView) findViewById(R.id.DLength);
        childLostdate = (TextView) findViewById(R.id.DLostDate);
        childTimer = (TextView) findViewById(R.id.DTimer);
        Address = (TextView) findViewById(R.id.Daddress);
        chat_image = (ImageView) findViewById(R.id.image_chat);
        linearLayout = (LinearLayout) findViewById(R.id.LinearChildData);


        String child_name = null;
        String age = null;
        String skinColor = null;
        String hairColor = null;
        String eyeColor  = null;
        String length  = null;
        String Lostdate  = null;
        String Timer  = null;
        String ImagePath = null;
        String addressLine  = null;
        String city  = null;
        String country  = null;
        String user_id  = null;

        Bundle extra = getIntent().getExtras();
        if(extra != null){
            child_name = extra.getString("childName");
            age = extra.getString("age");
            skinColor = extra.getString("skinColor");
            hairColor = extra.getString("hairColor");
            eyeColor = extra.getString("eyeColor");
            length = extra.getString("length");
            Lostdate = extra.getString("Lostdate");
            Timer = extra.getString("upload");
            ImagePath = extra.getString("ImagePath");
            addressLine = extra.getString("addressLine");
            city = extra.getString("city");
            country = extra.getString("country");
            user_id = extra.getString("user_id");
        }

        Picasso.with(context)
                .load(ImagePath)
                .placeholder(R.drawable.common_google_signin_btn_icon_dark_pressed)
                .error(R.drawable.com_facebook_auth_dialog_background)
                .into(childImage);

        /*java.util.Date date = Calendar.getInstance().getTime();
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String today = formatter.format(date);

        long difference = getDateDiffString(Timer, today);

        if(difference == 0){
            childTimer.setText("Uploaded Today : " + Timer);
        }else if(difference <30){
            childTimer.setText("Uploaded " + difference + " day ago");
        }else if(difference == 30){
            childTimer.setText("Uploaded month ago");
        }*/

        if(child_name == null){
            linearLayout.setVisibility(View.GONE);
        }else{
            childName.setText(getResources().getString(R.string.childName )+"   : " +child_name);
            childage.setText(getResources().getString(R.string.childAge)+"   : " +age);
            childSkinColor.setText(getResources().getString(R.string.childskin )+"    : " +skinColor);
            childHairColor.setText(getResources().getString(R.string.childhair )+"    : " +hairColor);
            childEyeColor.setText(getResources().getString(R.string.childEyeColor)+"    : " +eyeColor);
            childlength.setText(getResources().getString(R.string.childLength)+"    "  +length);
            childLostdate.setText(getResources().getString(R.string.lostDate)+"    : " +Lostdate);
        }
        Address.setText(getResources().getString(R.string.address) + "  "+addressLine + "  "+ city + "  "+ country);

        if(chat_userid.equals(user_id)){
            chat_image.setVisibility(View.GONE);
        }

        reciver_id = user_id;
        sender_id = chat_userid;

        chat_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_room(sender_id, reciver_id);
            }
        });

    }

    public long getDateDiffString(String One, String Two)
    {
        Date dateOne = Date.valueOf(One);
        Date dateTwo= Date.valueOf(Two);

        long timeOne = dateOne.getTime();
        long timeTwo = dateTwo.getTime();
        long oneDay = 1000 * 60 * 60 * 24;
        long delta = (timeTwo - timeOne) / oneDay;

        if (delta > 0) {
            return delta ;
        }
        else {
            delta *= -1;
            return delta ;
        }

    }

    public void add_room(String s_id, String r_id){

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);

        alertBuilder.setCancelable(true);
        alertBuilder.setTitle(R.string.chat_builder);
        alertBuilder.setMessage(R.string.chat_message);
        alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            public void onClick(DialogInterface dialog, int which) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, DatabaseURL.addRoom_url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.contains("result")){
                            try {
                                JSONObject json = new JSONObject(response);
                                JSONArray jsonArray = json.getJSONArray("result");
                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                String room_id = jsonObject.getString("room_id");
                                Intent intent = new Intent(context, chat_message.class);
                                intent.putExtra("room_id", room_id);
                                context.startActivity(intent);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }else if(response.equals("Failed")){
                            Toast.makeText(context, R.string.chat_room_error, Toast.LENGTH_LONG).show();
                        }else{
                            Intent intent = new Intent(context, chat_message.class);
                            intent.putExtra("room_id", response);
                            context.startActivity(intent);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("sender_id", sender_id);
                        params.put("reciver_id", reciver_id);
                        return params;
                    }
                };
                Singleton.getInstance(context).addToRequestQueue(stringRequest);
            }
        });
        alertBuilder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        final AlertDialog alert = alertBuilder.create();
        alert.show();
    }

}
