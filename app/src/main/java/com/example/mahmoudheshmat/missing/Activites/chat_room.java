package com.example.mahmoudheshmat.missing.Activites;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.mahmoudheshmat.missing.Adapters.chat_adapter;
import com.example.mahmoudheshmat.missing.Models.DatabaseURL;
import com.example.mahmoudheshmat.missing.R;
import com.example.mahmoudheshmat.missing.utils.Singleton;
import com.example.mahmoudheshmat.missing.Models.chat_items;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class chat_room extends AppCompatActivity{

    RecyclerView recyclerView;
    private chat_adapter adapter;
    Context context;
    private List<chat_items> data_list;

    String user_id;
    String room_id;

    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    Toolbar toolbar;
    MaterialSearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(getResources().getString(R.string.chat_room));
        toolbar.setNavigationIcon(R.drawable.ic_action_navigation_arrow_back_inverted); // your drawable
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Implemented by activity
            }
        });


        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        user_id =sharedPreferences.getString("User_id", null);

        searchView = (MaterialSearchView) findViewById(R.id.search_view);

        context =this;
        recyclerView = (RecyclerView) findViewById(R.id.room_recycle_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);

        data_list = new ArrayList<>();

        JsonData();


        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new chat_adapter(context, data_list, recyclerView);
        recyclerView.setAdapter(adapter);




        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(1000);
        itemAnimator.setRemoveDuration(1000);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(itemAnimator);


        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }

        });


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
        Singleton.getInstance(context).addToRequestQueue(jsObjRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.gallery,menu);
        MenuItem item = menu.findItem(R.id.action_search);
        MenuItem filter = menu.findItem(R.id.filter);
        filter.setVisible(false);
        searchView.setMenuItem(item);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
