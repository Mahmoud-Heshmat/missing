package com.example.mahmoudheshmat.missing.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.mahmoudheshmat.missing.Models.DatabaseURL;
import com.example.mahmoudheshmat.missing.Adapters.Profile_unAdapter;
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

public class imageFragmentUnknown extends Fragment {

    RecyclerView recyclerView;
    private Profile_unAdapter adapter;
    private List<childItem> data_list;

    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    String userID;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.image_fragment, container, false);

        sharedPreferences = this.getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        userID = sharedPreferences.getString("User_id", null);

        data_list = new ArrayList<>();
        JsonData();

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycle_viewFragment);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new Profile_unAdapter(this.getContext(), data_list, recyclerView);
        recyclerView.setAdapter(adapter);

        return rootView;
    }

    private void JsonData(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DatabaseURL.imageprofile2_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("nofound")){

                }else{
                    JSONObject jsonObj = null;
                    try {
                        jsonObj = new JSONObject(response);
                        JSONArray list = jsonObj.getJSONArray("result");
                        for(int x =0; x<list.length(); x++){
                            JSONObject jsonObject = list.optJSONObject(x);
                            String ImagePath = jsonObject.getString("ImagePath");
                            String addressLine = jsonObject.getString("addressLine");
                            String city = jsonObject.getString("city");
                            String country = jsonObject.getString("country");
                            String uploadDate = jsonObject.getString("upload");
                            String user_id = jsonObject.getString("user_id");
                            String image_id = jsonObject.getString("image_id");
                            String subject_id = jsonObject.getString("subject_id");
                            String location_id = jsonObject.getString("location_id");
                            String key = "profile";

                            childItem items = new childItem( ImagePath, addressLine, city, country,user_id, uploadDate, image_id, subject_id, location_id, key);
                            data_list.add(items);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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
                params.put("user_id", userID);
                return params;
            }
        };
        Singleton.getInstance(getContext()).addToRequestQueue(stringRequest);
    }
}

