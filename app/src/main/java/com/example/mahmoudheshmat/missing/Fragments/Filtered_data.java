package com.example.mahmoudheshmat.missing.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.mahmoudheshmat.missing.Activites.Home;
import com.example.mahmoudheshmat.missing.Adapters.childIdentifierAdapter;
import com.example.mahmoudheshmat.missing.Adapters.unknownAdapter;
import com.example.mahmoudheshmat.missing.Models.DatabaseURL;
import com.example.mahmoudheshmat.missing.Models.childItem;
import com.example.mahmoudheshmat.missing.R;
import com.example.mahmoudheshmat.missing.utils.Singleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Filtered_data extends android.support.v4.app.Fragment implements SearchView.OnQueryTextListener{

    RecyclerView recyclerView;
    private childIdentifierAdapter adapter;
    private unknownAdapter unAdapter;
    private List<childItem> data_list;

    GridLayoutManager gridLayoutManager;

    ProgressDialog loading;

    Toolbar mToolbar;

    String key = null;


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.chat_room, container, false);

        mToolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        if (mToolbar != null) {
            ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        }
        mToolbar.setTitle(getString(R.string.filter_dailog));


        setHasOptionsMenu(true);

        data_list = new ArrayList<>();
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycle_viewFragment);
        gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        loading = ProgressDialog.show(this.getContext(),"Please wait...","uploading",false,false);

        Bundle bundle = getArguments();
        key = bundle.getString("key");

        if(key == null){

        } else if(key.equals("parent")){
            String first_age = bundle.getString("first_age");
            String second_age = bundle.getString("second_age");
            String city = bundle.getString("city");
            Log.d("response", first_age + " : " + second_age + " : " + city);

            JsonDataKnown(first_age,second_age, city);

            adapter = new childIdentifierAdapter(getContext(),data_list, recyclerView);
            recyclerView.setAdapter(adapter);

        }else if (key.equals("user")){
            String city = bundle.getString("city");
            JsonData(city);
            Log.d("response", key);
            unAdapter = new unknownAdapter(getContext(),data_list, recyclerView);
            recyclerView.setAdapter(unAdapter);
        }

        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(1000);
        itemAnimator.setRemoveDuration(1000);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(itemAnimator);
        loading.dismiss();

        return rootView;
    }

    private void JsonDataKnown(final String first_age, final String second_age, final String city){
        StringRequest jsObjRequest = new StringRequest
                (Request.Method.POST, DatabaseURL.filterKnown_url, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("response",response);
                        if(response.equals("nofound")){
                            Toast.makeText(getContext(), "Not found", Toast.LENGTH_LONG).show();
                        }else{
                            try {
                                JSONObject json = new JSONObject(response);
                                JSONArray list = json.getJSONArray("result");
                                for(int x =0; x<list.length(); x++){
                                    JSONObject jsonObject = list.optJSONObject(x);
                                    String child_name = jsonObject.getString("child_name");
                                    String age = jsonObject.getString("age");
                                    String skinColor = jsonObject.getString("skinColor");
                                    String hairColor = jsonObject.getString("hairColor");
                                    String eyeColor = jsonObject.getString("eyeColor");
                                    String length = jsonObject.getString("length");
                                    String Lostdate = jsonObject.getString("Lostdate");
                                    String ImagePath = jsonObject.getString("ImagePath");
                                    String addressLine = jsonObject.getString("addressLine");
                                    String city = jsonObject.getString("city");
                                    String country = jsonObject.getString("country");
                                    String user_id = jsonObject.getString("user_id");
                                    String uploadDate = jsonObject.getString("upload");

                                    childItem items = new childItem(child_name, age, skinColor, hairColor, eyeColor, length, Lostdate, ImagePath,
                                            addressLine, city, country,user_id, uploadDate);

                                    data_list.add(items);

                                    adapter.notifyDataSetChanged();
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
                params.put("first_age", first_age);
                params.put("second_age", second_age);
                params.put("city", city);
                return params;
            }
        };
        Singleton.getInstance(getContext()).addToRequestQueue(jsObjRequest);
    }

    private void JsonData(final String city){
        StringRequest jsObjRequest = new StringRequest
                (Request.Method.POST, DatabaseURL.filterunKnown_url, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("responsee", response);
                        if(response.equals("nofound")){
                            Toast.makeText(getContext(), "Not found", Toast.LENGTH_LONG).show();
                        }else{
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

                                    unAdapter.notifyDataSetChanged();
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
                params.put("city", city);
                return params;
            }
        };
        Singleton.getInstance(getContext()).addToRequestQueue(jsObjRequest);
    }

    @Override
    public void onCreateOptionsMenu (final Menu menu, MenuInflater inflater) {

        final MenuItem item = menu.findItem(R.id.action_search);
        MenuItem filter_item = menu.findItem(R.id.filter);

        if(key == null){

        }else if(key.equals("parent")){

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

        }else if(key.equals("user")){
            filter_item.setVisible(false);
            item.setVisible(false);
        }


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
        if(key.equals("parent")){
            adapter.getFilter().filter(newText);
        }
        return false;
    }
}
