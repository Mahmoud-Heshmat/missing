package com.example.mahmoudheshmat.missing.Activites;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.mahmoudheshmat.missing.Adapters.childIdentifierAdapter;
import com.example.mahmoudheshmat.missing.Adapters.identifiedAdapter;
import com.example.mahmoudheshmat.missing.DatabaseBackground;
import com.example.mahmoudheshmat.missing.Models.DatabaseURL;
import com.example.mahmoudheshmat.missing.Models.childItem;
import com.example.mahmoudheshmat.missing.R;
import com.example.mahmoudheshmat.missing.utils.Singleton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class FaceRecogintioned extends AppCompatActivity {

    RecyclerView recyclerView;
    private identifiedAdapter adapter;
    Context context;
    private List<childItem> data_list;

    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    ArrayList<String> BundleSubjectID;

    String datetime;
    String currentUserID;

    String owner;
    Uri IdentifiedimagePath;
    String Identifiedimage;
    String longtidue;
    String latitidue;

    Toolbar toolbar;

    ImageView identifiedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_recogintioned);
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
        getSupportActionBar().setTitle(getResources().getString(R.string.face_result_2));

        context =this;

        identifiedImage = (ImageView) findViewById(R.id.image);

        android.text.format.DateFormat df = new android.text.format.DateFormat();
        datetime = (String) df.format("yyyy-MM-dd hh:mm:ss", new java.util.Date());

        Bundle extra = getIntent().getExtras();
        if(extra != null){
            BundleSubjectID=extra.getStringArrayList("subject_id");
            owner = extra.getString("key");
            IdentifiedimagePath = extra.getParcelable("imagepath");
            longtidue = extra.getString("longtidue");
            latitidue = extra.getString("latidue");
        }

        try {
            Bitmap bit = MediaStore.Images.Media.getBitmap(getContentResolver(), IdentifiedimagePath);
            Identifiedimage = getStringImage(bit);
        } catch (IOException e) {
            e.printStackTrace();
        }


        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        currentUserID = sharedPreferences.getString("User_id" ,null);


        context =this;
        data_list = new ArrayList<>();
        JsonData();

        identifiedImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, missingDetails.class);
                intent.putExtra("type", "identified");
                intent.putExtra("bundle_id", data_list.get(0).getBundle_id());
                intent.putExtra("ImagePath", data_list.get(0).getBundle_id());
                intent.putExtra("user_id", data_list.get(0).getUser_id());
                startActivity(intent);

            }
        });
    }

    private void JsonData()                           {

        for (int i=0; i<BundleSubjectID.size();i++){
            final int finalI = i;
            StringRequest jsObjRequest = new StringRequest
                    (Request.Method.POST, DatabaseURL.getchildBysubjectID_url, new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {

                            if(response.equals("nofound")){

                            }else{
                                try {
                                    JSONObject jsonObj = new JSONObject(response);
                                    JSONArray list = jsonObj.getJSONArray("result");
                                    for(int x =0; x<list.length(); x++){
                                        JSONObject jsonObject = list.optJSONObject(x);
                                        String ImagePath = jsonObject.getString("ImagePath");
                                        String user_id = jsonObject.getString("user_id");

                                        getData(user_id);
                                        Log.d("test", user_id);

                                        childItem items = new childItem(ImagePath, user_id, BundleSubjectID.get(x));

                                        add_notify(user_id, BundleSubjectID.get(x), datetime, currentUserID, Identifiedimage, longtidue, latitidue);

                                        data_list.add(items);
                                    }

                                    Picasso.with(context)
                                            .load(data_list.get(0).getImagePath())
                                            .placeholder(R.drawable.common_google_signin_btn_icon_dark_pressed)
                                            .error(R.drawable.com_facebook_auth_dialog_background)
                                            .into(identifiedImage);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("response", "error : "+error.toString());
                        }
                    }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("subjectID", BundleSubjectID.get(finalI));
                    return params;
                }
            };
            Singleton.getInstance(context).addToRequestQueue(jsObjRequest);

        }

    }

    // Send notification to owner of image
    private void getData(final String user_id){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DatabaseURL.push_notification_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("test", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", user_id);
                return params;
            }
        };
        Singleton.getInstance(context).addToRequestQueue(stringRequest);
    }

    // Convert image bitmap into string
    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    //add notify
    public void add_notify(final String user_id, final String BundleSubjectID, final String datetime,
                           final String currentUserID, final String Identifiedimage, final String longtidue, final String latitidue){

        StringRequest jsObjRequest = new StringRequest
                (Request.Method.POST, DatabaseURL.notify_url, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        //Log.d("test", response);
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
                params.put("subject_id", BundleSubjectID);
                params.put("MatchingTime", datetime);
                params.put("userMatchID", currentUserID);
                params.put("image", Identifiedimage);
                params.put("longtidue", longtidue);
                params.put("latitdue", latitidue);
                return params;
            }
        };
        Singleton.getInstance(context).addToRequestQueue(jsObjRequest);

    }
}

