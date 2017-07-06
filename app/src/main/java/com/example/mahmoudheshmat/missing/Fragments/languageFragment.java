package com.example.mahmoudheshmat.missing.Fragments;


import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.example.mahmoudheshmat.missing.R;


public class languageFragment extends DialogFragment implements View.OnClickListener{

    Button arabic;
    Button english;
    Communictor communictor;

    /*
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        communictor = (Communictor) context;
    }*/

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.communictor = (Communictor)activity;
        }
        catch (final ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement Communictor");
        }
    }

    @Override
    public void onStart() {
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        super.onStart();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.languagedialog, null);
        getDialog().setTitle("Languages");
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        arabic = (Button) view.findViewById(R.id.arabic);
        english = (Button) view.findViewById(R.id.english);
        arabic.setOnClickListener(this);
        english.setOnClickListener(this);
        //setCancelable(false);
        return view;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.arabic){
            communictor.DialogMessage("ar");
            dismiss();
        }
        if(v.getId() == R.id.english){
            communictor.DialogMessage("en");
            dismiss();
        }
    }


    public interface Communictor{
        void DialogMessage(String messgae);
    }
}
