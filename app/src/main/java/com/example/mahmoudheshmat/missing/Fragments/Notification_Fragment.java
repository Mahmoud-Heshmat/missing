package com.example.mahmoudheshmat.missing.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.mahmoudheshmat.missing.Adapters.notifyAdapter;
import com.example.mahmoudheshmat.missing.Models.DatabaseURL;
import com.example.mahmoudheshmat.missing.R;
import com.example.mahmoudheshmat.missing.utils.Singleton;
import com.example.mahmoudheshmat.missing.Models.childItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Notification_Fragment extends android.support.v4.app.Fragment{

    RecyclerView recyclerView;
    private notifyAdapter nAdapter;
    private List<childItem> data_list;
    LinearLayoutManager linearLayoutManager;

    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    String user_id;
    ProgressDialog loading;

    Toolbar mToolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.chat_room, container, false);

        setHasOptionsMenu(true);

        mToolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        if (mToolbar != null) {
            ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        }
        mToolbar.setTitle(getResources().getString(R.string.notification));

        sharedPreferences = this.getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        user_id =sharedPreferences.getString("User_id", null);
        linearLayoutManager = new LinearLayoutManager(getContext());

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycle_viewFragment);

        data_list = new ArrayList<>();

        notification();
        recyclerView.setLayoutManager(linearLayoutManager);
        //nAdapter = new notifyAdapter(getContext(),data_list, recyclerView);
        recyclerView.setAdapter(nAdapter);

        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(1000);
        itemAnimator.setRemoveDuration(1000);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(itemAnimator);

        return rootView;
    }

    private void notification(){
        StringRequest jsObjRequest = new StringRequest
                (Request.Method.POST, DatabaseURL.Displaynotify_title_url, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("heshmat", response);
                            JSONObject json = new JSONObject(response);
                            JSONArray jsonArray = json.getJSONArray("result");
                            for(int x =0; x<jsonArray.length(); x++){
                                JSONObject jsonObject = jsonArray.getJSONObject(x);
                                String user_name = jsonObject.getString("username");
                                String time = jsonObject.getString("timestamp");
                                String userpath = jsonObject.getString("userImage");
                                String notify_id = jsonObject.getString("notify_id");

                                childItem items = new childItem(user_name, userpath, time, notify_id);
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
        Singleton.getInstance(getContext()).addToRequestQueue(jsObjRequest);
    }

    @Override
    public void onCreateOptionsMenu (final Menu menu, MenuInflater inflater) {

        //inflater.inflate(R.menu.gallery, menu); // removed to not double the menu items
        MenuItem item = menu.findItem(R.id.action_search);
        MenuItem filter_item = menu.findItem(R.id.filter);
        filter_item.setVisible(false);
        item.setVisible(false);
        super.onCreateOptionsMenu(menu,inflater);
    }
}
