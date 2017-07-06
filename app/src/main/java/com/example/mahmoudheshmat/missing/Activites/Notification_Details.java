package com.example.mahmoudheshmat.missing.Activites;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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

import java.util.HashMap;
import java.util.Map;

public class Notification_Details extends AppCompatActivity {

    public ImageView parentImage;
    public ImageView userImage;
    public TextView username;
    public TextView phone;
    public TextView Rtime;
    public TextView lastseen;
    public Button delete;

    String user_name ;
    String phonee ;
    String parentpath ;
    String time ;
    String userpath ;
    String longti ;
    String latit ;

    String notify_id;

    Context context;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification__details);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.Notification_Details));
        toolbar.setNavigationIcon(R.drawable.ic_action_navigation_arrow_back_inverted);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        context = this;

        parentImage = (ImageView) findViewById(R.id.parentImage);
        userImage = (ImageView) findViewById(R.id.userImage);
        username = (TextView) findViewById(R.id.RuserName);
        phone = (TextView) findViewById(R.id.Rphone);
        Rtime = (TextView) findViewById(R.id.Time);
        lastseen = (TextView) findViewById(R.id.lastseen);
        delete = (Button) findViewById(R.id.deleteNotify);

        Bundle extra = getIntent().getExtras();
        if(extra != null) {
            notify_id = extra.getString("id");
            notification();

        }

        lastseen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, MapsActivity.class);
                i.putExtra("longitude", longti);
                i.putExtra("latitude", latit);
                context.startActivity(i);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete Image");
                builder.setMessage("Are you sure to delete this image");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delete_notify(notify_id);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });

    }

    private void notification(){
        StringRequest jsObjRequest = new StringRequest
                (Request.Method.POST, DatabaseURL.Displaynotify_url, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        if (response.equals("nofound")) {

                        }else {
                            try {
                                JSONObject json = new JSONObject(response);
                                JSONArray jsonArray = json.getJSONArray("result");
                                for(int x =0; x<jsonArray.length(); x++){
                                    JSONObject jsonObject = jsonArray.getJSONObject(x);
                                    user_name = jsonObject.getString("username");
                                    phonee = jsonObject.getString("phone");
                                    parentpath = jsonObject.getString("parentImage");
                                    time = jsonObject.getString("timestamp");
                                    userpath = jsonObject.getString("userImage");
                                    longti = jsonObject.getString("longti");
                                    latit = jsonObject.getString("latit");
                                    notify_id = jsonObject.getString("notify_id");

                                    Picasso.with(context)
                                            .load(parentpath)
                                            .placeholder(R.drawable.common_google_signin_btn_icon_dark_pressed)
                                            .error(R.drawable.com_facebook_auth_dialog_background)
                                            .into(parentImage);

                                    Picasso.with(context)
                                            .load(userpath)
                                            .placeholder(R.drawable.common_google_signin_btn_icon_dark_pressed)
                                            .error(R.drawable.com_facebook_auth_dialog_background)
                                            .into(userImage);

                                    username.setText(user_name);
                                    phone.setText(phonee);
                                    Rtime.setText(time);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("response", error.toString());
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("notify_id", notify_id);
                return params;
            }
        };
        Singleton.getInstance(context).addToRequestQueue(jsObjRequest);
    }

    private void delete_notify(final String id){
        StringRequest jsObjRequest = new StringRequest
                (Request.Method.POST, DatabaseURL.DeleteNotify_url, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        if(response.equals("success")){
                            startActivity(new Intent(context, Home.class));
                        }
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("response", error.toString());
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("notify_id", id);
                return params;
            }
        };
        Singleton.getInstance(context).addToRequestQueue(jsObjRequest);
    }
}
