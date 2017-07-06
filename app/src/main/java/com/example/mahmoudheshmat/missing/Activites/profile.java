package com.example.mahmoudheshmat.missing.Activites;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.example.mahmoudheshmat.missing.Models.DatabaseURL;
import com.example.mahmoudheshmat.missing.Fragments.imageFragment;
import com.example.mahmoudheshmat.missing.Fragments.imageFragmentUnknown;
import com.example.mahmoudheshmat.missing.R;
import com.example.mahmoudheshmat.missing.utils.RegularExpression;
import com.example.mahmoudheshmat.missing.utils.Singleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class profile extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

    EditText userName;
    EditText emailAddress;
    EditText phone;
    EditText address;

    // Json Attributes sores data retrieved drom DB
    String json_username = null;
    String json_email = null;
    String json_phone = null;
    String json_address = null;

    // SharedPreferences to get user id
    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    String userID;

    // Menu Items
    MenuItem close_item;
    MenuItem done_item;
    MenuItem edit_item;

    Context context;

    // KeyListener make Edittext uneditable
    KeyListener nameVar, emailVar, phoneVar, addressVar;

    // Validation of updated user data
    private AwesomeValidation awesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.my_profile));
        /*toolbar.setNavigationIcon(R.drawable.ic_action_navigation_arrow_back_inverted);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        */
        ActionBar ab = getSupportActionBar();
        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);


        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);


        // About profile Data

        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        userID = sharedPreferences.getString("User_id", null);

        userName = (EditText) findViewById(R.id.userNameP);
        emailAddress = (EditText) findViewById(R.id.Email);
        phone = (EditText) findViewById(R.id.PhoneP);
        address = (EditText) findViewById(R.id.AddressP);
        context = this;

        // Validation for user data entry
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this, R.id.userNameP, RegularExpression.NAME_PATTERN, R.string.nameerror);
        awesomeValidation.addValidation(this, R.id.Email, RegularExpression.EMAIL_PATTERN, R.string.emailerror);
        awesomeValidation.addValidation(this, R.id.PhoneP, RegularExpression.Phone_PATERN, R.string.phoneerror);
        awesomeValidation.addValidation(this, R.id.AddressP, RegularExpression.ADDRESS_PATERN, R.string.addresserror);

        // Make Edit text Non Editable

        nameVar = userName.getKeyListener();
        emailVar = emailAddress.getKeyListener();
        phoneVar = phone.getKeyListener();
        addressVar = address.getKeyListener();

        userName.setKeyListener(null);
        emailAddress.setKeyListener(null);
        phone.setKeyListener(null);
        address.setKeyListener(null);

        getProfileData();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        close_item = menu.findItem(R.id.close);
        done_item = menu.findItem(R.id.done);
        edit_item = menu.findItem(R.id.edit);
        close_item.setVisible(false);
        done_item.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.edit) {

            edit_item.setVisible(false);
            close_item.setVisible(true);
            done_item.setVisible(true);

            userName.setKeyListener(nameVar);
            emailAddress.setKeyListener(emailVar);
            phone.setKeyListener(phoneVar);
            address.setKeyListener(addressVar);

            userName.setTextColor(getResources().getColor(R.color.orange));
            emailAddress.setTextColor(getResources().getColor(R.color.orange));
            phone.setTextColor(getResources().getColor(R.color.orange));
            address.setTextColor(getResources().getColor(R.color.orange));
        }

        if (id == R.id.close) {

            edit_item.setVisible(true);
            close_item.setVisible(false);
            done_item.setVisible(false);

            userName.setKeyListener(null);
            emailAddress.setKeyListener(null);
            phone.setKeyListener(null);
            address.setKeyListener(null);

            userName.setTextColor(getResources().getColor(R.color.white));
            emailAddress.setTextColor(getResources().getColor(R.color.white));
            phone.setTextColor(getResources().getColor(R.color.white));
            address.setTextColor(getResources().getColor(R.color.white));

            userName.setText(userName.getText().toString());
            emailAddress.setText(emailAddress.getText().toString());
            phone.setText(phone.getText().toString());
            address.setText(address.getText().toString());
        }
        if (id == R.id.done) {
            if (awesomeValidation.validate()) {
                edit_item.setVisible(true);
                close_item.setVisible(false);
                done_item.setVisible(false);

                userName.setKeyListener(null);
                emailAddress.setKeyListener(null);
                phone.setKeyListener(null);
                address.setKeyListener(null);

                updateProfile();

                userName.setTextColor(getResources().getColor(R.color.white));
                emailAddress.setTextColor(getResources().getColor(R.color.white));
                phone.setTextColor(getResources().getColor(R.color.white));
                address.setTextColor(getResources().getColor(R.color.white));
            }
        }
        return super.onOptionsItemSelected(item);
    }

    // Function return the data of account owner
    private void getProfileData(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DatabaseURL.userProfile_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("nofound")){

                }else{
                    JSONObject jsonObject = null;
                    JSONArray jsonArray = null;
                    JSONObject json = null;
                    try {
                        jsonObject = new JSONObject(response);
                        jsonArray = jsonObject.getJSONArray("result");
                        json = jsonArray.getJSONObject(0);
                        json_username = json.getString("userName");
                        json_email = json.getString("email");
                        json_phone = json.getString("phone");
                        json_address = json.getString("address");

                        userName.setText(json_username);
                        emailAddress.setText(json_email);
                        phone.setText(json_phone);
                        address.setText(json_address);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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
                params.put("user_id", userID);
                return params;
            }
        };
        Singleton.getInstance(context).addToRequestQueue(stringRequest);
    }

    // Function update the data of account owner
    private void updateProfile(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DatabaseURL.updateProfile_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("success")){
                    userName.setText(userName.getText().toString());
                    emailAddress.setText(emailAddress.getText().toString());
                    phone.setText(phone.getText().toString());
                    address.setText(address.getText().toString());
                }else{

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
                params.put("user_name", userName.getText().toString());
                params.put("email", emailAddress.getText().toString());
                params.put("address", address.getText().toString());
                params.put("phone", phone.getText().toString());
                params.put("user_id", userID);

                return params;
            }
        };
        Singleton.getInstance(context).addToRequestQueue(stringRequest);
    }

    // Tabbed activity
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    imageFragment imageKnown =new imageFragment();
                    return  imageKnown;
                case 1:
                    imageFragmentUnknown ImagesUnknown =new imageFragmentUnknown();
                    return  ImagesUnknown;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.parentGallery);
                case 1:
                    return getString(R.string.userGallery);
            }
            return null;
        }
    }



}
