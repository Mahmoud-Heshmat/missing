package com.example.mahmoudheshmat.missing.Fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
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
import android.widget.SearchView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.mahmoudheshmat.missing.Activites.Home;
import com.example.mahmoudheshmat.missing.Adapters.chat_adapter;
import com.example.mahmoudheshmat.missing.Models.DatabaseURL;
import com.example.mahmoudheshmat.missing.R;
import com.example.mahmoudheshmat.missing.utils.Singleton;
import com.example.mahmoudheshmat.missing.Models.chat_items;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class chat_Fragment extends android.support.v4.app.Fragment implements SearchView.OnQueryTextListener{

    RecyclerView recyclerView;
    private chat_adapter adapter;
    private List<chat_items> data_list;

    String user_id;
    String room_id;

    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    Toolbar mToolbar;

    MenuItem filter_item;

    LinearLayoutManager linearLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.chat_room, container, false);

        setHasOptionsMenu(true);

        mToolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        if (mToolbar != null) {
            ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        }
        mToolbar.setTitle(getResources().getString(R.string.chat_room));

        sharedPreferences = this.getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        user_id =sharedPreferences.getString("User_id", null);

        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycle_viewFragment);

        data_list = new ArrayList<>();

        JsonData();

        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new chat_adapter(getContext(), data_list, recyclerView);
        recyclerView.setAdapter(adapter);

        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(1000);
        itemAnimator.setRemoveDuration(1000);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(itemAnimator);

        return rootView;
    }


    private void JsonData() {
        StringRequest jsObjRequest = new StringRequest
                (Request.Method.POST, DatabaseURL.chatRooms_url, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        if(response.equals("nofound")){

                        }else if(response.equals("error")){

                        }else{
                            try {
                                JSONObject json = new JSONObject(response);
                                JSONArray jsonArray = json.getJSONArray("result");
                                for(int x =0; x<jsonArray.length(); x++){
                                    JSONObject jsonObject = jsonArray.getJSONObject(x);
                                    String room_id = jsonObject.getString("room_id");
                                    String names = jsonObject.getString("names");

                                    chat_items items = new chat_items(room_id, names);

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

        //inflater.inflate(R.menu.main, menu); // removed to not double the menu items
        final MenuItem item = menu.findItem(R.id.action_search);
        filter_item = menu.findItem(R.id.filter);
        filter_item.setVisible(false);
        item.setVisible(true);
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
