package com.example.mahmoudheshmat.missing.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.mahmoudheshmat.missing.Filters.CustomFilter_chat;
import com.example.mahmoudheshmat.missing.Models.DatabaseURL;
import com.example.mahmoudheshmat.missing.R;
import com.example.mahmoudheshmat.missing.utils.RecyclerViewAnimator;
import com.example.mahmoudheshmat.missing.utils.Singleton;
import com.example.mahmoudheshmat.missing.Models.chat_items;
import com.example.mahmoudheshmat.missing.Activites.chat_message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class chat_adapter extends RecyclerView.Adapter<chat_adapter.ViewHolder> implements Filterable{

    private Context context;
    public List<chat_items> items;
    Context contexts;
    private RecyclerViewAnimator mAnimator;

    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    String userID;

    List<chat_items> filterList;
    CustomFilter_chat filter;

    RecyclerView recyclerView;

    public chat_adapter(Context context, List<chat_items> items, RecyclerView recyclerView){
        this.context = context;
        this.items = items;
        mAnimator = new RecyclerViewAnimator(recyclerView);
        this.recyclerView = recyclerView;
        this.filterList = items;
        sharedPreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        userID = sharedPreferences.getString("User_id", null);
    }


    @Override
    public chat_adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_room_raw, parent, false);
        mAnimator.onCreateViewHolder(view);
        return new chat_adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(chat_adapter.ViewHolder holder, final int position) {


        holder.chat_userName.setText(items.get(position).getUser_name());

        final String room_id = items.get(position).getRoom_id();
        final String name = items.get(position).getUser_name();

        holder.chat_userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contexts = v.getContext();
                Intent intent = new Intent(contexts, chat_message.class);
                intent.putExtra("room_id", room_id);
                intent.putExtra("name", name);
                contexts.startActivity(intent);
            }
        });

        holder.chat_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final CharSequence[] items = { "Delete", "Clear chat", "cancel"};

                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
                builder.setTitle("More ....");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {

                        if (items[item] == "Delete") {
                            delete_chat(room_id, position);
                           // Toast.makeText(context, items[item], Toast.LENGTH_LONG).show();
                        } else if (items[item]== "Clear chat") {
                            clear_chat(room_id, position);
                           // Toast.makeText(context, items[item], Toast.LENGTH_LONG).show();
                        } else if (items[item].equals("cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            }
        });


        mAnimator.onBindViewHolder(holder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public Filter getFilter() {
        if(filter==null)
        {
            filter=new CustomFilter_chat(filterList, this);
        }
        return filter;
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView chat_more;
        public TextView chat_userName;

        public ViewHolder(View itemView) {
            super(itemView);

            chat_more = (ImageView) itemView.findViewById(R.id.more);
            chat_userName = (TextView) itemView.findViewById(R.id.chat_room_name);

        }
    }


    // Function delete the chat with messages for only one user
    public void delete_chat(final String room_id, final int position){
        StringRequest jsObjRequest = new StringRequest
                (Request.Method.POST, DatabaseURL.chat_delete_url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responses", response);
                        if(response.equals("success")){
                            items.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position,items.size());
                        }else{
                            Toast.makeText(context, R.string.chat_delete, Toast.LENGTH_LONG).show();
                        }
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("responses", error.toString());
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("room_id", room_id);
                return params;
            }
        };
        Singleton.getInstance(context).addToRequestQueue(jsObjRequest);
    }

    public void clear_chat(final String room_id, final int position){
        StringRequest jsObjRequest = new StringRequest
                (Request.Method.POST, DatabaseURL.chat_clear_url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responses", response);
                        if(response.equals("failed")){
                            Toast.makeText(context, R.string.chat_delete, Toast.LENGTH_LONG).show();
                        }
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("responses", error.toString());
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("room_id", room_id);
                return params;
            }
        };
        Singleton.getInstance(context).addToRequestQueue(jsObjRequest);
    }

}

