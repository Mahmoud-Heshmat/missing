package com.example.mahmoudheshmat.missing.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.mahmoudheshmat.missing.R;

public class filter_fragmentU extends Fragment implements View.OnClickListener{

    RadioGroup addressGroup;
    RadioButton addressRadio;

    Button Btnfilter;

    String city = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.filter_fragmentu, container, false);

        addressGroup = (RadioGroup) rootView.findViewById(R.id.cityRadio);

        Btnfilter = (Button) rootView.findViewById(R.id.filterButton);

        Btnfilter.setOnClickListener(this);

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

            if(city == null){
                Toast.makeText(getContext(), "Please Choose Value OR click back", Toast.LENGTH_LONG).show();
            }else{
                Bundle bundle = new Bundle();
                bundle.putString("key", "user");
                bundle.putString("city", city);

                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                Filtered_data filtered_data = new Filtered_data();
                filtered_data.setArguments(bundle);

                fragmentTransaction.replace(R.id.main, filtered_data, "filtered_data");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        }
    }

}
