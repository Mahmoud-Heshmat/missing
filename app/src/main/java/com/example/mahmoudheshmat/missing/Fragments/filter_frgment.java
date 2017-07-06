package com.example.mahmoudheshmat.missing.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.mahmoudheshmat.missing.R;

public class filter_frgment extends Fragment implements View.OnClickListener{

    RadioGroup ageGroup;
    RadioButton ageRadio;

    RadioGroup addressGroup;
    RadioButton addressRadio;

    Button Btnfilter;

    String age = null;
    String city = null;

    Toolbar mToolbar;

    String first_age;
    String second_age;
    String choice_city;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.filter_frgment, container, false);

        setHasOptionsMenu(true);

        mToolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        if (mToolbar != null) {
            ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        }
        mToolbar.setTitle(getString(R.string.filter_dailog));


        ageGroup = (RadioGroup) rootView.findViewById(R.id.ageRadio);
        addressGroup = (RadioGroup) rootView.findViewById(R.id.cityRadio);

        Btnfilter = (Button) rootView.findViewById(R.id.filterButton);

        Btnfilter.setOnClickListener(this);


        ageGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int s = ageGroup.getCheckedRadioButtonId();
                ageRadio = (RadioButton) rootView.findViewById(s);
                age = ageRadio.getText().toString();
            }
        });

        addressGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int ss = addressGroup.getCheckedRadioButtonId();
                addressRadio = (RadioButton) rootView.findViewById(ss);
                city = addressRadio.getText().toString();
                if(city.equals("القاهرة")){
                    city = "cairo";
                }else if (city.equals("الاسكندرية")){
                    city = "alexandria";
                }
                else if (city.equals("الفيوم")){
                    city = "fayoium";
                }
                else if (city.equals("الجيزة")){
                    city = "giza";
                }
            }
        });

        return rootView;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.filterButton){

            if(age == null && city == null){
                Toast.makeText(getContext(), "Please Choose Value OR click Home", Toast.LENGTH_LONG).show();
            }else{
                if(age == null && city != null){
                    first_age = "0";
                    second_age = "0";
                    choice_city = city;

                    //Filtered();
                }else if(age != null && city != null){
                    if(age.equals(getResources().getString(R.string.age_from_2_8))){
                        first_age = "2";
                        second_age = "8";
                    }else if(age.equals(getResources().getString(R.string.age_from_9_15))){
                        first_age = "9";
                        second_age = "15";
                    }else{
                        first_age = "15";
                    }
                    choice_city = city;

                   // Filtered();
                }else if(age != null && city == null){
                    String firstAge = "0";
                    String secondAge = "0";
                    if(age.equals(getResources().getString(R.string.age_from_2_8))){
                        first_age = "2";
                        second_age = "8";
                    }else if(age.equals(getResources().getString(R.string.age_from_9_15))){
                        first_age = "9";
                        second_age = "15";
                    }else{
                        first_age = "15";
                    }

                    choice_city = "null";
                    //Filtered();
                }

                Bundle bundle = new Bundle();
                bundle.putString("key", "parent");
                bundle.putString("city", choice_city);
                bundle.putString("first_age", first_age);
                bundle.putString("second_age", second_age);
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                Filtered_data filtered_data = new Filtered_data();
                filtered_data.setArguments(bundle);

                fragmentTransaction.replace(R.id.main, filtered_data, "filtered_data");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }

        }
    }

    @Override
    public void onCreateOptionsMenu (final Menu menu, MenuInflater inflater) {

        //inflater.inflate(R.menu.gallery, menu); // removed to not double the menu items
        MenuItem item = menu.findItem(R.id.action_search);
        MenuItem filter_item = menu.findItem(R.id.filter);
        filter_item.setVisible(false);
        item.setVisible(false);
        super.onCreateOptionsMenu(menu,inflater);
    }

    public void Filtered(){
        Bundle bundle = new Bundle();
        bundle.getString("key", "parent");

        bundle.putString("city", choice_city);

        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        Filtered_data filtered_data = new Filtered_data();
        filtered_data.setArguments(bundle);

        fragmentTransaction.replace(R.id.main, filtered_data, "filtered_data");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
