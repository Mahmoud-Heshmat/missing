package com.example.mahmoudheshmat.missing.Activites;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.mahmoudheshmat.missing.Adapters.chat_message_adapter;
import com.example.mahmoudheshmat.missing.Models.DatabaseURL;
import com.example.mahmoudheshmat.missing.R;
import com.example.mahmoudheshmat.missing.RequestHandler;
import com.example.mahmoudheshmat.missing.utils.Singleton;
import com.example.mahmoudheshmat.missing.Models.chat_items;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiresApi(api = Build.VERSION_CODES.M)
public class chat_message extends AppCompatActivity implements RecyclerView.OnScrollChangeListener{

    RecyclerView recyclerView;
    private chat_message_adapter adapter;
    Context context;
    private List<chat_items> data_list;

    String user_id;
    String room_id;
    String user_name;
    String datetime;
    String reciever_name;

    //Activity Attribute
    ImageView img_send;
    ImageView img_attach;
    EditText message;

    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs";

    //Gallery
    private int PICK_IMAGE_REQUEST = 1;
    private Bitmap bitmap = null;

    private int requestCount = 1;

    LinearLayoutManager linearLayoutManager;

    ProgressBar progressBar;
    Toolbar toolbar;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_message);
        toolbar = (Toolbar) findViewById(R.id.tool);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_navigation_arrow_back_inverted); // your drawable
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Implemented by activity
            }
        });

        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        user_id =sharedPreferences.getString("User_id", null);
        user_name = sharedPreferences.getString("User_username", null);

        //Clicked image to send
        img_send = (ImageView) findViewById(R.id.img_send);
        img_attach = (ImageView) findViewById(R.id.img_attachment);
        message = (EditText) findViewById(R.id.et_message);
        progressBar = (ProgressBar) findViewById(R.id.progressBar1);

        Bundle extra = getIntent().getExtras();
        if(extra != null){
            room_id = extra.getString("room_id");
            reciever_name = extra.getString("name");
            getSupportActionBar().setTitle(reciever_name);

            context =this;
            recyclerView = (RecyclerView) findViewById(R.id.recycler_chat);
            linearLayoutManager = new LinearLayoutManager(context);

            data_list = new ArrayList<>();

            JsonData(requestCount);

            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            linearLayoutManager.setReverseLayout(true);
            linearLayoutManager.setStackFromEnd(true);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setScrollContainer(true);
            recyclerView.setLayoutAnimation(null);
            recyclerView.setItemAnimator(null);
            adapter = new chat_message_adapter(context, data_list);
            recyclerView.setAdapter(adapter);

            recyclerView.setOnScrollChangeListener(this);

            RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
            itemAnimator.setAddDuration(1000);
            itemAnimator.setRemoveDuration(1000);
            recyclerView.setHasFixedSize(true);
            recyclerView.setItemAnimator(itemAnimator);

        }else{
            Log.d("response", "No room id sent");
        }

        img_attach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        message.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                Boolean handeled = false;
                if(actionId == EditorInfo.IME_ACTION_SEND){
                    if(!message.getText().toString().isEmpty()){
                        android.text.format.DateFormat df = new android.text.format.DateFormat();
                        datetime = (String) df.format("yyyy-MM-dd hh:mm:ss", new java.util.Date());
                        String message_send = message.getText().toString().trim();
                        message.setText("");
                        addMessage(room_id, user_id, user_name, "1", message_send, datetime);
                        message_send = "";

                        handeled = true;
                    }
                }
                return handeled;
            }
        });


        img_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!message.getText().toString().isEmpty()){
                    android.text.format.DateFormat df = new android.text.format.DateFormat();
                    datetime = (String) df.format("yyyy-MM-dd hh:mm:ss", new java.util.Date());
                    String message_send = message.getText().toString().trim();
                    message.setText("");
                    addMessage(room_id, user_id, user_name, "1", message_send, datetime);
                    message_send = "";
                }
            }
        });

        FirebaseMessaging.getInstance().subscribeToTopic("room" + room_id);

    }

    //Function add message to the server
    public void addMessage(final String Mroom_id, final String Muser_id, final String Muser_name, final String Mtype, final String Mcontent, final String MtimeStamp){

        StringRequest jsObjRequest = new StringRequest
                (Request.Method.POST, DatabaseURL.messageText_url, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        if(response.equals("success")){

                            chat_items items = new chat_items(room_id, user_id, user_name, "1", Mcontent, datetime);
                            data_list.add(0, items);
                            adapter.notifyItemInserted(0);
                            recyclerView.smoothScrollToPosition(0);

                        }
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("room_id", Mroom_id);
                params.put("user_id", Muser_id);
                params.put("user_name", Muser_name);
                params.put("type", Mtype);
                params.put("content", Mcontent);
                params.put("timeStamp", MtimeStamp);
                return params;
            }
        };
        Singleton.getInstance(context).addToRequestQueue(jsObjRequest);
    }

    //Function returned all the messages from the server
    private void JsonData(final int requestCount) {

        progressBar.setVisibility(View.VISIBLE);
        setProgressBarIndeterminateVisibility(true);

        StringRequest jsObjRequest = new StringRequest
                (Request.Method.POST, DatabaseURL.getMessages_url, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("response", response);
                        try {
                            if(response.equals("over")){

                            }else if( response.equals("nofound")){
                                progressBar.setVisibility(View.GONE);
                            }
                            else{
                                JSONObject json = new JSONObject(response);
                                JSONArray jsonArray = json.getJSONArray("result");
                                for(int x =0; x<jsonArray.length(); x++){
                                    JSONObject jsonObject = jsonArray.getJSONObject(x);
                                    String message_id = jsonObject.getString("id");
                                    String room_id = jsonObject.getString("room_id");
                                    String user_id = jsonObject.getString("user_id");
                                    String user_name = jsonObject.getString("user_name");
                                    String type = jsonObject.getString("type");
                                    String content = jsonObject.getString("content");
                                    String timeStamp = jsonObject.getString("timeStamp");

                                    chat_items items = new chat_items(message_id, room_id, user_id, user_name, type, content, timeStamp);

                                    data_list.add(items);

                                    adapter.notifyDataSetChanged();
                                    recyclerView.smoothScrollToPosition(0);
                                }
                            }

                            progressBar.setVisibility(View.GONE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("room_id", room_id);
                params.put("page", String.valueOf(requestCount));
                return params;
            }
        };
        Singleton.getInstance(context).addToRequestQueue(jsObjRequest);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(messageReceiver, new IntentFilter("UpdateChateActivity"));
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(messageReceiver);
    }

    // Function used to get image from gallery
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                if(bitmap != null){
                    String imageString = getStringImage(bitmap);
                    android.text.format.DateFormat df = new android.text.format.DateFormat();
                    datetime = (String) df.format("yyyy-MM-dd hh:mm:ss", new java.util.Date());
                    uploadImage(room_id, user_id, user_name, "2", imageString, datetime);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // that's function convert bitmap into string
    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    // Function upload message had image

    public void uploadImage(final String Mroom_id, final String Muser_id, final String Muser_name, final String Mtype, final String Mcontent, final String MtimeStamp){

        class UploadImage extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                HashMap<String,String> param = new HashMap<String,String>();
                param.put("room_id", Mroom_id);
                param.put("user_id", Muser_id);
                param.put("user_name", Muser_name);
                param.put("type", Mtype);
                param.put("content", Mcontent);
                param.put("timeStamp", MtimeStamp);
                String result = rh.sendPostRequest(DatabaseURL.messageImage_url, param);
                return result;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if(!s.equals("Failed")){
                    Log.d("test", s);
                    chat_items items = new chat_items(room_id, user_id, user_name, "2", s, datetime);
                    data_list.add(0,items);
                    adapter.notifyItemInserted(0);
                    recyclerView.smoothScrollToPosition(0);
                    message.setText("");
                }
            }
        }
        UploadImage u = new UploadImage();
        u.execute();
    }

/*
    public void uploadImage(final String Mroom_id, final String Muser_id, final String Muser_name, final String Mtype, final String Mcontent, final String MtimeStamp){

        StringRequest jsObjRequest = new StringRequest
                (Request.Method.POST, DatabaseURL.add_message_url, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        chat_items items = new chat_items(room_id, user_id, user_name, "2", response, datetime);
                        data_list.add(0,items);
                        adapter.notifyItemInserted(0);
                        recyclerView.smoothScrollToPosition(0);
                        message.setText("");
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("room_id", Mroom_id);
                params.put("user_id", Muser_id);
                params.put("user_name", Muser_name);
                params.put("type", Mtype);
                params.put("content", Mcontent);
                params.put("timeStamp", MtimeStamp);
                return params;
            }
        };
        Singleton.getInstance(context).addToRequestQueue(jsObjRequest);

    }
 */
    // Broadcast Receive message from fcm on Receive function
    private BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            // get message sent from broadcast
            Bundle message = intent.getExtras();
            if(message != null){

                String messageContent = message.getString("messageContent");
                String roomId = message.getString("roomId");
                String userId = message.getString("userId");
                String username = message.getString("username");
                String messageType = message.getString("messageType");
                String timestamp = message.getString("timestamp");


                chat_items items = new chat_items(roomId, userId, username, messageType, messageContent, timestamp);

                data_list.add(0, items);
                adapter.notifyItemInserted(0);
                recyclerView.smoothScrollToPosition(0);
            }

        }
    };

    //This method would check that the recyclerview scroll has reached the bottom or not
    private boolean isLastItemDisplaying(RecyclerView recyclerView) {
        if (recyclerView.getAdapter().getItemCount() != 0) {
            int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
            if (lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition == recyclerView.getAdapter().getItemCount() - 1)
                return true;
        }
        return false;
    }

    @Override
    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        //Ifscrolled at last then
        if (isLastItemDisplaying(recyclerView)) {
            JsonData(requestCount);
            requestCount++;
        }
    }
}
