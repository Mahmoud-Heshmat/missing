package com.example.mahmoudheshmat.missing.Activites;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.mahmoudheshmat.missing.utils.Cryptography;
import com.example.mahmoudheshmat.missing.DatabaseBackground;
import com.example.mahmoudheshmat.missing.Models.DatabaseURL;
import com.example.mahmoudheshmat.missing.R;
import com.example.mahmoudheshmat.missing.utils.RegularExpression;
import com.example.mahmoudheshmat.missing.utils.Singleton;
import com.google.firebase.iid.FirebaseInstanceId;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class Registration extends AppCompatActivity {

    EditText userName;
    EditText email;
    EditText password;
    EditText phone;
    EditText address;

    Context context;

    //Continue Google and facebook sign in
    Button button;
    String usernameIntent;
    String userIDIntent;
    String userEmailIntent;

    //sharedPreferences
    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences.Editor editor;

    //Internet Connection
    boolean isConnected;

    private AwesomeValidation awesomeValidation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        context = this;

        userName = (EditText) findViewById(R.id.username);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        address = (EditText) findViewById(R.id.address);
        phone = (EditText) findViewById(R.id.phone);
        button = (Button) findViewById(R.id.button2);


        // Validation for user data entry
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this, R.id.username, RegularExpression.NAME_PATTERN, R.string.nameerror);
        awesomeValidation.addValidation(this, R.id.email, RegularExpression.EMAIL_PATTERN, R.string.emailerror);
        awesomeValidation.addValidation(this, R.id.phone, RegularExpression.Phone_PATERN, R.string.phoneerror);
        awesomeValidation.addValidation(this, R.id.password, RegularExpression.PASSWORD_PATTERN, R.string.passwroderror);
        awesomeValidation.addValidation(this, R.id.address, RegularExpression.ADDRESS_PATERN, R.string.addresserror);


        // Continue Google and facebook Sign In
        Bundle extra = getIntent().getExtras();
        if(extra != null){
            usernameIntent = extra.getString("username");
            userIDIntent = extra.getString("password");
            userEmailIntent = extra.getString("email");

            userName.setAlpha(0);
            password.setAlpha(0);
            email.setAlpha(0);

            context = this;
            button.setText(R.string.faceGoogle);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        DatabaseBackground background = new DatabaseBackground(context);
                        String type = "register";
                        String encryptPassword = Cryptography.encryptIt(userIDIntent);
                        if(background.execute(type, usernameIntent, userEmailIntent, encryptPassword,
                                address.getText().toString(),phone.getText().toString()).get().equals("success")){

                            editor.remove(MyPREFERENCES).commit();
                            editor.putString("User_id", userIDIntent);
                            editor.putString("User_username", usernameIntent);
                            editor.putString("User_email", userEmailIntent);
                            editor.commit();
                            startActivity(new Intent(getApplicationContext(), Home.class));

                        }else{
                            Toast.makeText(getApplicationContext(), "Something error Please check Data",Toast.LENGTH_LONG).show();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    /*
    public void register(final View view){
        if(internetConnection()){

            final String userEmail = email.getText().toString();
            final String userPassword = password.getText().toString();
            final String userUserName = userName.getText().toString();
            final String userPhoneNumber = phone.getText().toString();
            final String userAddress = address.getText().toString();


            if (awesomeValidation.validate()) {

                StringRequest stringRequest = new StringRequest(Request.Method.POST, DatabaseURL.checkUserEmail_url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("found")){
                            Toast.makeText(context, "Email is Registered before Please check again", Toast.LENGTH_LONG).show();
                        }else{
                            //FirebaseMessaging.getInstance().subscribeToTopic("test");
                            final String token = FirebaseInstanceId.getInstance().getToken();
                            //final String token = sharedPreferences.getString("token", null);
                            //Log.d("response", token);
                            final String encryptPassword = Cryptography.encryptIt(userPassword);

                            StringRequest stringRequest = new StringRequest(Request.Method.POST, DatabaseURL.register_url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if(response.equals("success")){
                                        startActivity(new Intent(getApplicationContext(), login.class));
                                    }else{
                                        Toast.makeText(context, "Error", Toast.LENGTH_LONG).show();
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
                                    params.put("email", userEmail);
                                    params.put("user_name", userUserName);
                                    params.put("password", encryptPassword);
                                    params.put("address", userAddress);
                                    params.put("phone", userPhoneNumber);
                                    params.put("token", token);
                                    return params;
                                }
                            };
                            Singleton.getInstance(context).addToRequestQueue(stringRequest);

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
                        params.put("email", userEmail);
                        return params;
                    }
                };
                Singleton.getInstance(context).addToRequestQueue(stringRequest);
            }
        }else{
            Toast.makeText(getApplicationContext(), "Please Connent To Intennet", Toast.LENGTH_LONG).show();
        }

    }
    */

    public void register(final View view){
        final String userEmail = email.getText().toString();
        final String userPassword = password.getText().toString();
        final String userUserName = userName.getText().toString();
        final String userPhoneNumber = phone.getText().toString();
        final String userAddress = address.getText().toString();


        if (awesomeValidation.validate()) {

            StringRequest stringRequest = new StringRequest(Request.Method.POST, DatabaseURL.checkUserEmail_url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if(response.equals("found")){
                        Toast.makeText(context, "Email is Registered before Please check again", Toast.LENGTH_LONG).show();
                    }else{
                        //FirebaseMessaging.getInstance().subscribeToTopic("test");
                        final String token = FirebaseInstanceId.getInstance().getToken();
                        //final String token = sharedPreferences.getString("token", null);
                        //Log.d("response", token);
                        final String encryptPassword = Cryptography.encryptIt(userPassword);

                        StringRequest stringRequest = new StringRequest(Request.Method.POST, DatabaseURL.register_url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if(response.equals("success")){
                                    startActivity(new Intent(getApplicationContext(), login.class));
                                }else{
                                    Toast.makeText(context, "Error", Toast.LENGTH_LONG).show();
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
                                params.put("email", userEmail);
                                params.put("user_name", userUserName);
                                params.put("password", encryptPassword);
                                params.put("address", userAddress);
                                params.put("phone", userPhoneNumber);
                                params.put("token", token);
                                return params;
                            }
                        };
                        Singleton.getInstance(context).addToRequestQueue(stringRequest);

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
                    params.put("email", userEmail);
                    return params;
                }
            };
            Singleton.getInstance(context).addToRequestQueue(stringRequest);
        }

    }


    //check internet connection
    public Boolean internetConnection(){
        Boolean check = false;
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if(isConnected){
            check = true;
        }
        return  check;
    }
}
