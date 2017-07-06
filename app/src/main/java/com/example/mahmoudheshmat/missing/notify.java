package com.example.mahmoudheshmat.missing;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.mahmoudheshmat.missing.Adapters.NotificationAdapter;
import com.example.mahmoudheshmat.missing.Adapters.notifyAdapter;
import com.example.mahmoudheshmat.missing.Models.DatabaseURL;
import com.example.mahmoudheshmat.missing.Models.childItem;
import com.example.mahmoudheshmat.missing.Models.parent_items;
import com.example.mahmoudheshmat.missing.utils.Singleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class notify extends AppCompatActivity {

    Toolbar toolbar;

    RecyclerView recyclerView;
    private NotificationAdapter nAdapter;
    private ArrayList<parent_items> data_list;
    LinearLayoutManager linearLayoutManager;

    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    String user_id;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setNavigationIcon(R.drawable.ic_action_navigation_arrow_back_inverted); // your drawable
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Implemented by activity
            }
        });
        getSupportActionBar().setTitle(getResources().getString(R.string.notification));

        context =this;

        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        user_id =sharedPreferences.getString("User_id", null);

        linearLayoutManager = new LinearLayoutManager(context);

        data_list = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recycle_viewFragment);

        notification();

        nAdapter = new NotificationAdapter(context, data_list);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(nAdapter);

    }

    private void notification(){
        StringRequest jsObjRequest = new StringRequest
                (Request.Method.POST, DatabaseURL.Displaynotify_title_url, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("test", response);
                        try {
                            JSONObject json = new JSONObject(response);
                            JSONArray jsonArray = json.getJSONArray("result");
                            for(int x =0; x<jsonArray.length(); x++){
                                JSONObject jsonObject = jsonArray.getJSONObject(x);
                                String user_name = jsonObject.getString("username");
                                String time = jsonObject.getString("timestamp");
                                String userpath = jsonObject.getString("userImage");
                                String notify_id = jsonObject.getString("notify_id");
                                parent_items items = new parent_items(user_name, userpath, time, notify_id);
                                data_list.add(items);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
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
                params.put("user_id", user_id);
                return params;
            }
        };
        Singleton.getInstance(context).addToRequestQueue(jsObjRequest);
    }

}
