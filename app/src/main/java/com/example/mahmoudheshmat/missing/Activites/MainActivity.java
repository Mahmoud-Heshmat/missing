package com.example.mahmoudheshmat.missing.Activites;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

import com.example.mahmoudheshmat.missing.R;
import com.example.mahmoudheshmat.missing.Fragments.languageFragment;

import java.util.Locale;


public class MainActivity extends AppCompatActivity implements languageFragment.Communictor {

    SharedPreferences LangsharedPreferences;
    public static final String MyLangPrefs = "MyLangPrefs" ;
    SharedPreferences.Editor editor;

    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;

    // Language
    Locale myLocale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LangsharedPreferences = getSharedPreferences(MyLangPrefs, Context.MODE_PRIVATE);
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        //sharedPreferences.edit().clear().commit();
        //LangsharedPreferences.edit().clear().commit();
        String language = LangsharedPreferences.getString("language",null);

        String emails = sharedPreferences.getString("User_email",null);
        if(emails != null && language != null){
            setHomeLocale(language);
            finish();
        }else if(emails == null && language == null){
            FragmentManager manager = getFragmentManager();
            languageFragment myDialog = new languageFragment();
            myDialog.show(manager, "language");
        }
    }

    public void register(View view){
        startActivity(new Intent(getApplicationContext(), Registration.class));
    }

    public void signin(View view){
        startActivity(new Intent(getApplicationContext(), login.class));
    }

    // Data backed from language dialog fragment
    @Override
    public void DialogMessage(String messgae) {
        editor = LangsharedPreferences.edit();
        if(messgae == "ar"){
            editor.putString("language", "ar");
            setLocale("ar");
        }
        if(messgae == "en"){
            editor.putString("language", "en");
            setLocale("en");
        }
        editor.commit();

    }

    public void setLocale(String lang) {

        myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(this, MainActivity.class);
        startActivity(refresh);
        finish();
    }

    public void setHomeLocale(String lang) {

        myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(this, Home.class);
        startActivity(refresh);
        finish();
    }
}
