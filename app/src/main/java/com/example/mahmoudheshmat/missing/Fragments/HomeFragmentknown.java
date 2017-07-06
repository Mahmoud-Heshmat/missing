package com.example.mahmoudheshmat.missing.Fragments;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
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
import android.widget.SearchView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.mahmoudheshmat.missing.Activites.Home;
import com.example.mahmoudheshmat.missing.Activites.childData;
import com.example.mahmoudheshmat.missing.Adapters.childIdentifierAdapter;
import com.example.mahmoudheshmat.missing.Adapters.parentAdapter;
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

public class HomeFragmentknown extends android.support.v4.app.Fragment implements SearchView.OnQueryTextListener{

    RecyclerView recyclerView;
    private parentAdapter adapter;
    private List<childItem> data_list;

    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    String userID;

    //Endless RecycleView
    private int KnownCount = 1;
    private static String result_known = null;
    String checkDataBacked = null;

    GridLayoutManager gridLayoutManager;

    ProgressDialog loading;
    MenuItem filter_item;

    Context mContext;

    FragmentTransaction fragmentTransaction;
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.image_fragment, container, false);

        setHasOptionsMenu(true);

        sharedPreferences = this.getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        userID = sharedPreferences.getString("User_id", null);

        data_list = new ArrayList<>();
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycle_viewFragment);
        gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        loading = ProgressDialog.show(this.getContext(),"Please wait...","uploading",false,false);
        JsonDataKnown(KnownCount);
        adapter = new parentAdapter(getContext(),data_list);
        recyclerView.setAdapter(adapter);

        //recyclerView.setOnScrollChangeListener(this);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(!result_known.equals("nofound")){
                    KnownCount++;
                    JsonDataKnown(KnownCount);
                }
            }
        });
        loading.dismiss();

        mContext = getActivity();

        return rootView;
    }

    private void JsonDataKnown(final int knownCount){
        StringRequest jsObjRequest = new StringRequest
                (Request.Method.POST, DatabaseURL.getGallery_url, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            result_known = response;
                            checkDataBacked = response;
                            JSONObject json = new JSONObject(response);
                            JSONArray list = json.getJSONArray("result");
                            for(int x =0; x<list.length(); x++){
                                JSONObject jsonObject = list.optJSONObject(x);
                                String child_name = jsonObject.getString("child_name");
                                String ImagePath = jsonObject.getString("ImagePath");
                                String subject_id = jsonObject.getString("subjectID");

                                childItem items = new childItem(ImagePath, child_name, subject_id);

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
                                    startActivity(new Intent(getContext(), childData.class));
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
                params.put("page",String.valueOf(knownCount));
                return params;
            }
        };
        Singleton.getInstance(getContext()).addToRequestQueue(jsObjRequest);
    }

    @Override
    public void onCreateOptionsMenu (final Menu menu, MenuInflater inflater) {

        //inflater.inflate(R.menu.main, menu); // removed to not double the menu items
        final MenuItem item = menu.findItem(R.id.action_search);
        filter_item = menu.findItem(R.id.filter);
        SearchView sv = new SearchView(((Home) getActivity()).getSupportActionBar().getThemedContext());
        MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
        MenuItemCompat.setActionView(item, sv);
        sv.setOnQueryTextListener(this);
        sv.setIconifiedByDefault(true);

        sv.onActionViewExpanded();
        sv.onActionViewCollapsed();

        MenuItemCompat.setOnActionExpandListener(item, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                return true;  // Return true to collapse action view
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {

                return true;  // Return true to expand action view
            }
        });


        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_search) {
            //filter_item.setVisible(false);
        }
        if (id == R.id.filter){

            filter_frgment filter = new filter_frgment();
            fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.main, filter, "filter");
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.getFilter().filter(newText);
        return false;
    }

}
