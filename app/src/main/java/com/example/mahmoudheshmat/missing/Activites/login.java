package com.example.mahmoudheshmat.missing.Activites;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.example.mahmoudheshmat.missing.utils.Cryptography;
import com.example.mahmoudheshmat.missing.DatabaseBackground;
import com.example.mahmoudheshmat.missing.Models.DatabaseURL;
import com.example.mahmoudheshmat.missing.R;
import com.example.mahmoudheshmat.missing.utils.RegularExpression;
import com.example.mahmoudheshmat.missing.utils.SendMail;
import com.example.mahmoudheshmat.missing.utils.Singleton;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class login extends AppCompatActivity {

    // Login
    EditText email;
    EditText password;
    CheckBox show_hide_password;
    TextView forget_password;
    Context context;
    EditText editTextConfirmOtp;
    AppCompatButton buttonConfirm;
    AppCompatButton buttonSendAgain;
    AppCompatButton buttonchangePassword;
    EditText new_password;
    EditText new_password2;

    //Shared Preferences
    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences.Editor editor;

    private AwesomeValidation awesomeValidation;


    //Google login
    GoogleApiClient mGoogleApiClient;
    Context googleContext;

    //Facebook Login
    LoginButton facebooklogin;
    CallbackManager callbackManager;
    String faceEmail;
    String faceFirstName;
    String faceLastName;
    String faceId ;

    //Internet Connection
    boolean isConnected;

    // result of otp create response
    static String result = null;
    static String check_result = null;
    static String check_Changepassword = null;

    // RegularExpression for check email and password
    RegularExpression validate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        // Validation for user data entry
        validate = new RegularExpression();
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this, R.id.email, RegularExpression.EMAIL_PATTERN, R.string.emailerror);


        context = this;
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        forget_password = (TextView) findViewById(R.id.forgot_password);

        show_hide_password = (CheckBox) findViewById(R.id.show_hide_password);
        show_hide_password.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton button,
                                         boolean isChecked) {
                if (isChecked) {

                    show_hide_password.setText("Hide Password");// change
                    password.setInputType(InputType.TYPE_CLASS_TEXT);
                    password.setTransformationMethod(HideReturnsTransformationMethod
                            .getInstance());// show password
                } else {
                    show_hide_password.setText("Show Password");// change
                    password.setInputType(InputType.TYPE_CLASS_TEXT
                            | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    password.setTransformationMethod(PasswordTransformationMethod
                            .getInstance());// hide password
                }
            }
        });


        //Google login
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(internetConnection()){
                    signIn();
                }else{
                    Toast.makeText(getApplicationContext(), "Please Connent To Intennet", Toast.LENGTH_LONG).show();
                }
            }
        });

        forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (awesomeValidation.validate()) {
                    sendOTP(email.getText().toString());
                    if (result == null){

                    }else if(result.equals("Failed")){

                    }else{
                        LayoutInflater li = LayoutInflater.from(context);
                        //Creating a view to get the dialog box
                        View confirmDialog = li.inflate(R.layout.dialog_confirm, null);
                        final AlertDialog.Builder alert = new AlertDialog.Builder(context);

                        buttonConfirm = (AppCompatButton) confirmDialog.findViewById(R.id.buttonConfirm);
                        buttonSendAgain = (AppCompatButton) confirmDialog.findViewById(R.id.buttonSendagain);
                        editTextConfirmOtp = (EditText) confirmDialog.findViewById(R.id.editTextOtp);

                        //Adding our dialog box to the view of alert dialog
                        alert.setView(confirmDialog);
                        //Creating an alert dialog
                        final AlertDialog alertDialog = alert.create();
                        //Displaying the alert dialog
                        alertDialog.show();

                        buttonConfirm.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(email.getText().toString().isEmpty()){
                                    Toast.makeText(context, "Enter the email address to send code", Toast.LENGTH_LONG).show();
                                }else{
                                    //Getting the user entered otp from edittext
                                    final ProgressDialog loading = ProgressDialog.show(login.this, "Authenticating", "Please wait while we check the entered code", false,false);
                                    final String otp = editTextConfirmOtp.getText().toString().trim();
                                    checkOTP(otp, email.getText().toString());
                                    if(check_result == null){
                                        loading.dismiss();
                                    }else if(check_result.equals("success")){
                                        loading.dismiss();
                                        alertDialog.dismiss();

                                        LayoutInflater layoutInflater = LayoutInflater.from(context);
                                        //Creating a view to get the dialog box
                                        View confirmDialog = layoutInflater.inflate(R.layout.dialog_confirm_password, null);
                                        AlertDialog.Builder alert = new AlertDialog.Builder(context);

                                        buttonchangePassword= (AppCompatButton) confirmDialog.findViewById(R.id.changePassword);
                                        new_password = (EditText) confirmDialog.findViewById(R.id.new_password);
                                        new_password2 = (EditText) confirmDialog.findViewById(R.id.confirm_new_password);

                                        //Adding our dialog box to the view of alert dialog
                                        alert.setView(confirmDialog);
                                        //Creating an alert dialog
                                        final AlertDialog alertDialog = alert.create();
                                        //Displaying the alert dialog
                                        alertDialog.show();

                                        buttonchangePassword.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                final ProgressDialog loading = ProgressDialog.show(login.this, "Change Password", "Please wait while password change", false,false);

                                                if(!validate.check(new_password.getText().toString(),"password") || !validate.check(new_password2.getText().toString(),"password")){
                                                    loading.dismiss();
                                                    Toast.makeText(context, "Password Must be at least 8 Letter", Toast.LENGTH_LONG).show();
                                                }else{
                                                    loading.dismiss();
                                                    if(new_password.getText().toString().equals(new_password2.getText().toString())){
                                                        changePassword(new_password.getText().toString() ,email.getText().toString());
                                                        if(check_Changepassword == null){

                                                        }else if(check_Changepassword.equals("success")){
                                                            alertDialog.dismiss();
                                                        }else if (check_Changepassword.equals("Failed")){

                                                        }
                                                    }else{
                                                        Toast.makeText(context, "Password Must be Identical", Toast.LENGTH_LONG).show();
                                                    }
                                                }

                                            }
                                        });

                                    }else if (check_result.equals("nofound")){
                                        loading.dismiss();
                                        Toast.makeText(context, "The code is wrong Please check", Toast.LENGTH_LONG).show();
                                    }

                                }
                            }
                        });

                        buttonSendAgain.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                sendOTP(email.getText().toString());
                            }
                        });
                    }
                }

            }
        });


        //Facebook Login
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        callbackManager= CallbackManager.Factory.create();
        facebooklogin=(LoginButton) findViewById(R.id.login_button);
        facebooklogin.setReadPermissions(Arrays.asList("public_profile","email","user_friends","user_location"));

        facebooklogin.registerCallback(callbackManager,new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess (LoginResult loginResult){
                graphRequest(loginResult.getAccessToken());
            }

            @Override
            public void onCancel () {
                Toast.makeText(getApplicationContext(), "Cancel", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError (FacebookException error){

            }
        });



    }

    public void login(View view){
        if(internetConnection()){
            awesomeValidation.addValidation(this, R.id.password, RegularExpression.PASSWORD_PATTERN, R.string.passwroderror);
            if (awesomeValidation.validate()) {
               // checkvervication();
                checkvervication();
            }

        }else{
            Toast.makeText(getApplicationContext(), "Please Connent To Intennet", Toast.LENGTH_LONG).show();
        }
    }


    public void checkvervication() {
        String type = "login";
        DatabaseBackground background = new DatabaseBackground(context);
        String check = null;
        try {
            String DecryptPassword = Cryptography.encryptIt(password.getText().toString());
            check = background.execute(type, email.getText().toString(), DecryptPassword).get();
            if(check.equals("nofound")) {
                Toast.makeText(context, "Email or Password incorrect Please Check", Toast.LENGTH_SHORT).show();
            }else{
                editor.remove(MyPREFERENCES).commit();
                JSONObject jsonObject = new JSONObject(check);
                JSONArray jsonArray = jsonObject.getJSONArray("result");
                JSONObject json = jsonArray.getJSONObject(0);

                String User_id = json.getString("id");
                String User_username = json.getString("userName");
                String User_email = json.getString("email");
                String User_userType = json.getString("userTypeID");

                editor.putString("User_id", User_id);
                editor.putString("User_username", User_username);
                editor.putString("User_email", User_email);
                editor.putString("User_userType", User_userType);
                editor.commit();
                startActivity(new Intent(getApplicationContext(), Home.class));
                finish();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //Google Login
    int RC_SIGN_IN ;
    private void signIn() {
        RC_SIGN_IN = 10;
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }else{
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();

            String googleEmails = acct.getEmail();
            String checkGoogleEmail;
            googleContext = this;
            String login = "login";
            DatabaseBackground background = new DatabaseBackground(googleContext);
            try {
                String DecryptPassword = Cryptography.encryptIt(acct.getId());
                checkGoogleEmail = background.execute(login, googleEmails, DecryptPassword).get();
                if(!checkGoogleEmail.equals("nofound")) {
                    editor.remove(MyPREFERENCES).commit();
                    JSONObject jsonObject = new JSONObject(checkGoogleEmail);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    JSONObject json = jsonArray.getJSONObject(0);

                    String User_id = json.getString("id");
                    String User_username = json.getString("userName");
                    String User_email = json.getString("email");
                    String User_userType = json.getString("userTypeID");

                    editor.putString("User_id", User_id);
                    editor.putString("User_username", User_username);
                    editor.putString("User_email", User_email);
                    editor.putString("User_userType", User_userType);
                    editor.commit();
                    startActivity(new Intent(getApplicationContext(), Home.class));
                    finish();
                }else{
                    Intent i = new Intent(getApplicationContext(), Registration.class);
                    i.putExtra("username", acct.getDisplayName());
                    i.putExtra("email", acct.getEmail());
                    i.putExtra("password", acct.getId());
                    startActivity(i);
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            Toast.makeText(this, "Please follow instruction", Toast.LENGTH_LONG).show();
        }
    }

    //Facebook Login
    public void graphRequest(AccessToken token){
        GraphRequest request = GraphRequest.newMeRequest(token,new GraphRequest.GraphJSONObjectCallback(){

            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {

                try {
                    JSONObject jsonObject = new JSONObject(object.toString());

                    faceFirstName = jsonObject.getString("first_name");
                    faceLastName = jsonObject.getString("last_name");
                    faceEmail = jsonObject.getString("email");
                    faceId = jsonObject.getString("id");

                    String checkFacebookEmail;
                    String login = "login";
                    DatabaseBackground background = new DatabaseBackground(context);
                    String DecryptPassword = Cryptography.encryptIt(faceId);
                    checkFacebookEmail = background.execute(login, faceEmail, DecryptPassword).get();
                    if(!checkFacebookEmail.equals("nofound")) {
                        editor.remove(MyPREFERENCES).commit();
                        editor.putString("User_id", faceId);
                        editor.putString("User_username", faceFirstName + " "+ faceLastName);
                        editor.putString("User_email", faceEmail);
                        editor.commit();
                        startActivity(new Intent(getApplicationContext(), Home.class));
                        finish();
                    }else{
                        String faceusername = faceFirstName + " " + faceLastName;
                        Intent i= new Intent(getApplicationContext(), Registration.class);
                        i.putExtra("username", faceusername);
                        i.putExtra("email", faceEmail);
                        i.putExtra("password",faceId);
                        startActivity(i);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }


            }
        });

        Bundle b = new Bundle();
        b.putString("fields","id,email,first_name,last_name, location{location}");
        request.setParameters(b);
        request.executeAsync();

    }

    // Function check internet connection
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

    //Function send OTP to the user database
    public void sendOTP(final String email){

        StringRequest jsObjRequest = new StringRequest
                (Request.Method.POST, DatabaseURL.OTP_add_url, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        if(response.equals("Failed")){
                        }else{
                            SendMail sm = new SendMail(context, email, "Password verification", response);
                            sm.execute();
                        }
                        result = response;
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                return params;
            }
        };
        Singleton.getInstance(context).addToRequestQueue(jsObjRequest);
    }

    // Function compare the user otp enter with the database one
    public void checkOTP(final String otp, final String email){
        StringRequest jsObjRequest = new StringRequest
                (Request.Method.POST, DatabaseURL.OTP_check_url, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        check_result = response;
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        check_result = error.toString();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email",email);
                params.put("otp",otp);
                return params;
            }
        };
        Singleton.getInstance(context).addToRequestQueue(jsObjRequest);
    }

    public void changePassword(final String password ,final String email){
        final String encryptPassword = Cryptography.encryptIt(password);
        StringRequest jsObjRequest = new StringRequest
                (Request.Method.POST, DatabaseURL.change_password_url, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        check_Changepassword = response;
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        check_Changepassword = error.toString();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("password",encryptPassword);
                params.put("email",email);
                return params;
            }
        };
        Singleton.getInstance(context).addToRequestQueue(jsObjRequest);
    }
}



