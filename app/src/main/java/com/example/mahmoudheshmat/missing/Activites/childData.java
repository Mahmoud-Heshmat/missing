package com.example.mahmoudheshmat.missing.Activites;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.mahmoudheshmat.missing.R;

import java.io.IOException;
import java.util.Calendar;

public class childData extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    ImageView childImage;
    EditText childName;
    TextView  childAge;
    TextView childSkinColor;
    TextView  childHairColor;
    TextView  childEyeColor;
    TextView  childLength;
    TextView childLostDate;
    Button btn;

    Context context ;

    private Bitmap bitmap = null;

    Uri imageurl = null;
    String key = null;

    // Skin, hair and eye color Array
    final String[] childSkinColorPicker= {"White","Black"};
    final String[] childHairColorPicker= {"Black","yellow", "Brown"};
    final String[] childEyeColorPicker= {"Black","Brown", "Blue", "Green"};


    // Values of picker stored in Strings
    String agePicker;
    String skniColorPicker;
    String hairColorPicker;
    String eyeColorPicker;
    String lengthPicker;
    String lostdatePicker;

    // Date picker Attribute
    DatePickerDialog datePickerDialog;
    private Calendar calendar;
    private int year, month, day;

    // Pop up menu
    private PopupWindow pw;
    TextView firstInfo ;
    TextView secondInfo ;
    TextView thirdInfo ;
    TextView fourthInfo ;
    TextView fifthInfo ;
    ImageView cancelimage ;

    //Shared Preferences
    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences.Editor editor;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_data);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.add_information));
        toolbar.setNavigationIcon(R.drawable.ic_action_navigation_arrow_back_inverted); // your drawable
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Implemented by activity
            }
        });

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        context = this;
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        String checkPopDisplay = sharedPreferences.getString("childDataPopUp", null);

        if(checkPopDisplay == null){
            editor.putString("childDataPopUp", "Displayed").commit();
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
            builder.setTitle(R.string.pop_title);
            builder.setMessage(R.string.popup_message);
            builder.setPositiveButton(R.string.popup_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    initiatePopupWindow();
                }
            });
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();
        }

        childImage = (ImageView) findViewById(R.id.missingImage);
        childName = (EditText) findViewById(R.id.missingName);
        childAge = (TextView) findViewById(R.id.missingAge);
        childSkinColor = (TextView) findViewById(R.id.missingSkin);
        childHairColor = (TextView) findViewById(R.id.missingHair);
        childEyeColor = (TextView) findViewById(R.id.missingEye);
        childLength = (TextView) findViewById(R.id.missingLength);
        childLostDate = (TextView) findViewById(R.id.missingLost);
        btn = (Button) findViewById(R.id.btnChildData);


        // Number and Date Picker
        childAge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final NumberPicker aNumberPicker = new NumberPicker(context);
                aNumberPicker.setMinValue(1);
                aNumberPicker.setMaxValue(30);
                aNumberPicker.setWrapSelectorWheel(true);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setTitle("Age of missing");
                alertDialogBuilder.setView(aNumberPicker);
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        agePicker = String.valueOf(aNumberPicker.getValue());
                                        childAge.setText("Age : " + agePicker);
                                        childAge.setTextColor(getResources().getColor(R.color.orange));
                                    }
                                })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        childLength.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final NumberPicker aNumberPicker = new NumberPicker(context);
                aNumberPicker.setMinValue(30);
                aNumberPicker.setMaxValue(200);
                aNumberPicker.setWrapSelectorWheel(true);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setTitle("Length of missing");
                alertDialogBuilder.setView(aNumberPicker);
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        lengthPicker = String.valueOf(aNumberPicker.getValue());
                                        childLength.setText("Length : " + lengthPicker);
                                        childLength.setTextColor(getResources().getColor(R.color.orange));
                                    }
                                })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        childSkinColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final NumberPicker aNumberPicker = new NumberPicker(context);
                aNumberPicker.setMinValue(0);
                aNumberPicker.setMaxValue(childSkinColorPicker.length-1);
                aNumberPicker.setDisplayedValues(childSkinColorPicker);
                aNumberPicker.setWrapSelectorWheel(true);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setTitle("Skin Color");
                alertDialogBuilder.setView(aNumberPicker);
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        skniColorPicker = childSkinColorPicker[aNumberPicker.getValue()];
                                        childSkinColor.setText("Skin color : "+skniColorPicker);
                                        childSkinColor.setTextColor(getResources().getColor(R.color.orange));
                                    }
                                })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        childHairColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final NumberPicker aNumberPicker = new NumberPicker(context);
                aNumberPicker.setMinValue(0);
                aNumberPicker.setMaxValue(childHairColorPicker.length-1);
                aNumberPicker.setDisplayedValues(childHairColorPicker);
                aNumberPicker.setWrapSelectorWheel(true);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setTitle("Hair color");
                alertDialogBuilder.setView(aNumberPicker);
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        hairColorPicker = childHairColorPicker[aNumberPicker.getValue()];
                                        childHairColor.setText("Hair color : "+hairColorPicker);
                                        childHairColor.setTextColor(getResources().getColor(R.color.orange));
                                    }
                                })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        childEyeColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final NumberPicker aNumberPicker = new NumberPicker(context);
                aNumberPicker.setMinValue(0);
                aNumberPicker.setMaxValue(childEyeColorPicker.length-1);
                aNumberPicker.setDisplayedValues(childEyeColorPicker);
                aNumberPicker.setWrapSelectorWheel(true);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setTitle("Eye color");
                alertDialogBuilder.setView(aNumberPicker);
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        eyeColorPicker = childEyeColorPicker[aNumberPicker.getValue()];
                                        childEyeColor.setText("Eye color : "+eyeColorPicker);
                                        childEyeColor.setTextColor(getResources().getColor(R.color.orange));
                                    }
                                })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(
                context, childData.this, year, month, day);

        childLostDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });


        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            key = extra.getString("key");

            if(key.equals("parent")){
                imageurl = extra.getParcelable("childImage");
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imageurl);
                    childImage.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }else if(key.equals("retake")){
                childName.setText(extra.getString("childName"));
                childEyeColor.setText("Eye color : "+extra.getString("childEyeColor"));
                childHairColor.setText("Hair color : "+extra.getString("childHairColor"));
                childSkinColor.setText("Skin color : "+extra.getString("childSkinColor"));
                childLength.setText("Length : " + extra.getString("childLength"));
                childAge.setText("Age : " + extra.getString("childAge"));
                childLostDate.setText(extra.getString("childLostDate"));
            }
        }

    }

    public void btnSave(View view){

        if(imageurl != null && childName.getText() != null && agePicker != null && skniColorPicker != null
                && hairColorPicker != null && eyeColorPicker != null && lengthPicker != null && lostdatePicker != null){
            Intent intent = new Intent(getApplicationContext(), FaceRecognition.class);
            intent.putExtra("owner", "parent");
            intent.putExtra("childImage", imageurl);
            intent.putExtra("childName",childName.getText().toString());
            intent.putExtra("childAge",agePicker);
            intent.putExtra("childSkinColor", skniColorPicker);
            intent.putExtra("childHairColor", hairColorPicker);
            intent.putExtra("childEyeColor", eyeColorPicker);
            intent.putExtra("childLength", lengthPicker);
            intent.putExtra("childLostDate", lostdatePicker);
            startActivity(intent);
        }
        if(imageurl == null){
            Snackbar.make(view,"Please click on image and choose one" ,Snackbar.LENGTH_LONG).show();
        }
        if(childName.getText() == null){
            Snackbar.make(view,"Please click on missing Name and choose one" ,Snackbar.LENGTH_LONG).show();
        }
        if(agePicker == null){
            Snackbar.make(view,"Please click on Age and Set missing age" ,Snackbar.LENGTH_LONG).show();
        }
        if(skniColorPicker == null){
            Snackbar.make(view,"Please click on Skin color" ,Snackbar.LENGTH_LONG).show();
        }
        if(hairColorPicker == null){
            Snackbar.make(view,"Please click on child Name" ,Snackbar.LENGTH_LONG).show();
        }
        if(eyeColorPicker == null){
            Snackbar.make(view,"Please click on eye Color" ,Snackbar.LENGTH_LONG).show();
        }
        if(lengthPicker == null){
            Snackbar.make(view,"Please click on Length" ,Snackbar.LENGTH_LONG).show();
        }
        if(lostdatePicker == null){
            Snackbar.make(view,"Please click on Lost Date" ,Snackbar.LENGTH_LONG).show();
        }

    }

    // Date Picker
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        lostdatePicker = String.valueOf(dayOfMonth)+"-"+String.valueOf(month)+"-"+String.valueOf(year);
        childLostDate.setText(lostdatePicker);
        childLostDate.setTextColor(getResources().getColor(R.color.orange));
    }

    // Pop up menu show the first time to user
    private void initiatePopupWindow() {
        try {
            LayoutInflater inflater = (LayoutInflater) childData.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View layout = inflater.inflate(R.layout.pop_up, (ViewGroup) findViewById(R.id.popRelative));

            pw = new PopupWindow(layout, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
            // display the popup in the center
            pw.setAnimationStyle(R.style.animationName);
            pw.showAtLocation(layout, Gravity.CENTER, 0, 0);


            if(key.equals("parent")){
                firstInfo = (TextView) layout.findViewById(R.id.firstInfo);
                secondInfo = (TextView) layout.findViewById(R.id.secondInfo);
                thirdInfo = (TextView) layout.findViewById(R.id.thirdInfo);
                fourthInfo = (TextView) layout.findViewById(R.id.fourthInfo);
                fifthInfo = (TextView) layout.findViewById(R.id.fifthInfo);
                cancelimage = (ImageView) layout.findViewById(R.id.popUpImage);

                firstInfo.setText("1) Click on the image and choose from gallery or Capture from camera");
                secondInfo.setText("2) Click on the age and choose age of missing");
                thirdInfo.setText("3) Click on skin, Hair and eye color on missing");
                fourthInfo.setText("4) Click save button");
                fifthInfo.setVisibility(View.GONE);
                cancelimage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pw.dismiss();
                    }
                });

            }else{
                firstInfo = (TextView) layout.findViewById(R.id.firstInfo);
                secondInfo = (TextView) layout.findViewById(R.id.secondInfo);
                thirdInfo = (TextView) layout.findViewById(R.id.thirdInfo);
                fourthInfo = (TextView) layout.findViewById(R.id.fourthInfo);
                fifthInfo = (TextView) layout.findViewById(R.id.fifthInfo);
                cancelimage = (ImageView) layout.findViewById(R.id.popUpImage);


                firstInfo.setText("1) Click on the image and choose from gallery or Capture from camera");
                secondInfo.setText("4) Click save button");
                thirdInfo.setVisibility(View.GONE);
                fourthInfo.setVisibility(View.GONE);
                fifthInfo.setVisibility(View.GONE);

                cancelimage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pw.dismiss();
                    }
                });
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}