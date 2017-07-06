package com.example.mahmoudheshmat.missing.Fragments;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.example.mahmoudheshmat.missing.Activites.childData;
import com.example.mahmoudheshmat.missing.Adapters.unknownAdapter;
import com.example.mahmoudheshmat.missing.Models.DatabaseURL;
import com.example.mahmoudheshmat.missing.R;
import com.example.mahmoudheshmat.missing.utils.Singleton;
import com.example.mahmoudheshmat.missing.Models.childItem;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragmentUnknown extends android.support.v4.app.Fragment {

    public static RecyclerView recyclerView;
    public static unknownAdapter adapter;
    public static List<childItem> data_list;
    GridLayoutManager gridLayoutManager;
    ProgressDialog loading;

    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    String userID;

    //Endless RecycleView
    private int unKnowntCount = 1;
    private static String result_Unknown = null;
    String checkDataBacked = null;

    // Search
    MaterialSearchView searchView;


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.image_fragment, container, false);

        sharedPreferences = this.getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        userID = sharedPreferences.getString("User_id", null);

        setHasOptionsMenu(true);

        data_list = new ArrayList<>();

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycle_viewFragment);
        gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        loading = ProgressDialog.show(this.getContext(),"Please wait...","uploading",false,false);
        JsonDataUnknown(unKnowntCount);
        recyclerView.setLayoutManager(gridLayoutManager);
        adapter = new unknownAdapter(getContext(),data_list, recyclerView);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(!result_Unknown.equals("nofound")){
                    unKnowntCount++;
                    JsonDataUnknown(unKnowntCount);
                }
            }
        });
        loading.dismiss();

        return rootView;
    }


    private void JsonDataUnknown(final int unKnowntCount){
        StringRequest jsObjRequest = new StringRequest
                (Request.Method.POST, DatabaseURL.getunknownGallery_url, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        result_Unknown = response;
                        checkDataBacked = response;
                        try {
                            JSONObject json = new JSONObject(response);
                            JSONArray list = json.getJSONArray("result");
                            for(int x =0; x<list.length(); x++){
                                JSONObject jsonObject = list.optJSONObject(x);
                                String ImagePath = jsonObject.getString("ImagePath");
                                String addressLine = jsonObject.getString("addressLine");
                                String city = jsonObject.getString("city");
                                String country = jsonObject.getString("country");
                                String user_id = jsonObject.getString("user_id");
                                String uploadDate = jsonObject.getString("upload");

                                childItem items = new childItem( ImagePath, addressLine, city, country,user_id, uploadDate);
                                data_list.add(items);

                                adapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(error.toString() != null){
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setTitle(R.string.parentGallery);
                            builder.setMessage(R.string.alert_checkIamgeExist);
                            builder.setPositiveButton(R.string.alert_postive, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent i = new Intent(getContext(), childData.class);
                                    i.putExtra("key","user");
                                    startActivity(i);
                                }
                            });
                            builder.setNegativeButton(R.string.alert_cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            builder.show();
                        }
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("page",String.valueOf(unKnowntCount));
                return params;
            }
        };
        Singleton.getInstance(getContext()).addToRequestQueue(jsObjRequest);
    }


    @Override
    public void onCreateOptionsMenu (final Menu menu, MenuInflater inflater) {

        //inflater.inflate(R.menu.main, menu); // removed to not double the menu items
        final MenuItem item = menu.findItem(R.id.action_search);
        //MenuItem filter_item = menu.findItem(R.id.filter);
        item.setVisible(false);
        super.onCreateOptionsMenu(menu,inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.filter){

            filter_fragmentU filter = new filter_fragmentU();
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.main, filter, "filter");
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
