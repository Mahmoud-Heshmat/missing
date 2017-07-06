package com.example.mahmoudheshmat.missing.Activites;

import com.example.mahmoudheshmat.missing.DatabaseBackground;
import com.example.mahmoudheshmat.missing.utils.GPSTracker;
import com.example.mahmoudheshmat.missing.Models.DatabaseURL;
import com.example.mahmoudheshmat.missing.R;
import com.example.mahmoudheshmat.missing.RequestHandler;
import com.kairos.*;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ExecutionException;


public class FaceRecognition extends AppCompatActivity {

    //SharedPreferences
    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences.Editor editor;
    public static final int GPSPermission_id = 1;
    Boolean GPSCheckPermission = false;
    Boolean checkPermission = false;

    //progress Dialog
    ProgressDialog progress;


    //Child Data
    Uri childImage;
    String childName;
    String childAge;
    String childSkinColor;
    String childHairColor;
    String childEyeColor;
    String childLength;
    String childLostDate;
    Bitmap childbitmap = null;


    //Activity Attribute
    ImageView imageView;
    Context context;

    //Gps Location
    LocationManager locationManager;


    //Face Recognition
    Kairos myKairos;
    KairosListener listener;
    static String statue;
    String subject_id;
    int randomInt ;

    String user_id;

    int randomID = 0;
    GPSTracker gpsTracker ;

    String owner = null;
    Toolbar toolbar;

    ProgressDialog loading;

    // Pop up menu
    private PopupWindow pw;
    TextView firstInfo ;
    TextView secondInfo ;
    TextView thirdInfo ;
    TextView fourthInfo ;
    TextView fifthInfo ;
    ImageView cancelimage ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_recognition);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.face_recognition));
        toolbar.setNavigationIcon(R.drawable.ic_action_navigation_arrow_back_inverted); // your drawable
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Implemented by activity
            }
        });
        context = this;

        // check pop up window to the user for the first time
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        user_id = sharedPreferences.getString("User_id", null);
        editor = sharedPreferences.edit();

        String checkPopDisplay = sharedPreferences.getString("FaceRecognitionPopUp", null);

        if(checkPopDisplay == null){
            editor.putString("FaceRecognitionPopUp", "Displayed").commit();
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
            builder.setTitle("Information");
            builder.setMessage("That is first time for you please click Ok to see what will do or cancel it");
            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    initiatePopupWindow();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();
        }

        // Data from childData class about the missing person
        imageView = (ImageView) findViewById(R.id.imageView);

        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            owner = extra.getString("owner");
            if(owner.equals("user")){
                childImage = extra.getParcelable("childImage");
            }else if(owner.equals("parent")){
                childImage = extra.getParcelable("childImage");
                childName = extra.getString("childName");
                childAge = extra.getString("childAge");
                childSkinColor = extra.getString("childSkinColor");
                childHairColor = extra.getString("childHairColor");
                childEyeColor = extra.getString("childEyeColor");
                childLength = extra.getString("childLength");
                childLostDate = extra.getString("childLostDate");
                //Log.d("response", childName + " : "+ childAge + " : "+childSkinColor + " : "+childHairColor + " : "+childEyeColor + " : "+childLostDate + " : "+ childLength);
            }

            try {
                childbitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),childImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        imageView.setImageBitmap(childbitmap);

        // Gps permission check
        gpsTracker = new GPSTracker(context);

        //DeleteGallery();

    }

    public void faceR(View view){
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){

            GPSpermission();
            if(checkPermission){
                MakeFaceRecognition();
            }
        }else{
            gpsTracker.showSettingsAlert();
        }

    }

    // this function add photo to face recognition library
    public void AddToGallery(){

        myKairos = new Kairos();
        myKairos.setAuthentication(this, DatabaseURL.app_id, DatabaseURL.api_key);
        listener = new KairosListener() {
            @Override
            public void onSuccess(String response) {
                try {
                    if(response.contains("Errors")){
                        loading.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Add Photo!");
                        builder.setMessage("Picture isn't clear Please take another Picture");
                        builder.setPositiveButton("Take again", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(owner.equals("parent")) {
                                    Intent intent = new Intent(getApplicationContext(), childData.class);
                                    intent.putExtra("key","retake");
                                    intent.putExtra("childName",childName);
                                    intent.putExtra("childAge",childAge);
                                    intent.putExtra("childSkinColor",childSkinColor);
                                    intent.putExtra("childHairColor",childHairColor);
                                    intent.putExtra("childEyeColor",childEyeColor);
                                    intent.putExtra("childLength",childLength);
                                    intent.putExtra("childLostDate",childLostDate);
                                    startActivity(intent);
                                }else{
                                    Intent intent = new Intent(getApplicationContext(), Home.class);
                                    startActivity(intent);
                                }

                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(getApplicationContext(), Home.class);
                                startActivity(intent);
                            }
                        });
                        builder.show();
                    }else{
                        loading.dismiss();
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("images");
                        JSONObject json = jsonArray.getJSONObject(0);
                        JSONObject t = json.getJSONObject("transaction");
                        statue = t.getString("status");
                        subject_id = t.getString("subject_id");
                        DB(String.valueOf(randomID));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFail(String response) {
                loading.dismiss();
            }
        };

        loading = ProgressDialog.show(context,"Please wait...","Face Recognition",false,false);
        randomID = getRandom();

        Bitmap scaledImage = Bitmap.createScaledBitmap(childbitmap, 250, 250, true);
        String images = getStringImage(childbitmap);
        String galleryId = "missingPeople";
        String subjectId = String.valueOf(randomID);
        String selector = "FULL";
        String multipleFaces = "false";
        String minHeadScale = "0.25";
        try {
            myKairos.enroll(scaledImage,
                    subjectId,
                    galleryId,
                    selector,
                    multipleFaces,
                    minHeadScale,
                    listener);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    // this function make face recognition with the images found on system
    public void MakeFaceRecognition(){

        myKairos = new Kairos();
        myKairos.setAuthentication(this, DatabaseURL.app_id, DatabaseURL.api_key);
        listener = new KairosListener() {
            @Override
            public void onSuccess(String response) {
                if(response.contains("Errors") || response.contains("failure")){
                    loading.dismiss();
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle(R.string.face_result_title);
                    builder.setMessage(R.string.face_result);
                    builder.setPositiveButton(getResources().getString(R.string.alert_postive), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AddToGallery();
                        }
                    });
                    builder.setNegativeButton(getResources().getString(R.string.alert_cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(getApplicationContext(), Home.class);
                            startActivity(intent);
                        }
                    });
                    builder.show();
                }else{
                    loading.dismiss();
                    String RecognitionStatus = null;
                    ArrayList faceRegnition = new ArrayList();
                    JSONObject jsonObject = null;
                    try {

                        jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("images");
                        JSONObject json = jsonArray.getJSONObject(0);
                        JSONObject statuedata = json.optJSONObject("transaction");
                        JSONArray candidatesArray = json.getJSONArray("candidates");
                        for (int i = 0; i<candidatesArray.length(); i++){
                            JSONObject j = candidatesArray.getJSONObject(i);
                            faceRegnition.add(j.getString("subject_id"));
                        }

                        String imagePath = getStringImage(childbitmap);
                        //Intent i = new Intent(context, identifiedimages.class);
                        Intent i = new Intent(context, FaceRecogintioned.class);
                        if(owner.equals("user")){
                            i.putExtra("key","user");
                        }else{
                            i.putExtra("key", "parent");
                        }

                        GPSTracker gpsTracker = new GPSTracker(context);
                        String stringLatitude = String.valueOf(gpsTracker.latitude);
                        String stringLongitude = String.valueOf(gpsTracker.longitude);

                        i.putStringArrayListExtra("subject_id",faceRegnition);
                        i.putExtra("imagepath", childImage);
                        i.putExtra("longtidue", stringLatitude);
                        i.putExtra("latidue", stringLongitude);
                        startActivity(i);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFail(String response) {
                Toast.makeText(context, response, Toast.LENGTH_LONG).show();
            }
        };

        loading = ProgressDialog.show(context,"Please wait...","Face Recognition",false,false);

        Bitmap scaledBitmapFace = Bitmap.createScaledBitmap(childbitmap, 250, 250, true);
        String galleryId = "missingPeople";
        String selector = "FULL";
        String threshold = "0.75";
        String minHeadScale = "0.25";
        String maxNumResults = "25";
        try {
            myKairos.recognize(scaledBitmapFace,
                    galleryId,
                    selector,
                    threshold,
                    minHeadScale,
                    maxNumResults,
                    listener);
        } catch (JSONException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    // this function convert bitmap of image into string to send into database
    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    // this function delete the library
    public void DeleteGallery(){
        myKairos = new Kairos();
        myKairos.setAuthentication(this, DatabaseURL.app_id, DatabaseURL.api_key);
        listener = new KairosListener() {
            @Override
            public void onSuccess(String response) {
                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
            }
            @Override
            public void onFail(String response) {
            }
        };

        String galleryId = "missingPeople";
        try {
            myKairos.deleteGallery(galleryId, listener);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    // this function delete image from the library
    public void DeleteSubject(String id){
        myKairos = new Kairos();
        myKairos.setAuthentication(this, DatabaseURL.app_id, DatabaseURL.api_key);
        listener = new KairosListener() {
            @Override
            public void onSuccess(String response) {
                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
            }
            @Override
            public void onFail(String response) {
            }
        };

        try {
            String galleryId = "missingPeople";
            String DeletedSubject = id;
            myKairos.deleteSubject(DeletedSubject, galleryId, listener);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    // this function display all the library's images
    public void GalleryList(){
        myKairos = new Kairos();
        myKairos.setAuthentication(this, DatabaseURL.app_id, DatabaseURL.api_key);
        listener = new KairosListener() {
            @Override
            public void onSuccess(String response) {
                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
            }
            @Override
            public void onFail(String response) {
            }
        };

        try {
            myKairos.listGalleries(listener);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    // this function display image by its id
    public void SubjectList(String id){
        myKairos = new Kairos();
        myKairos.setAuthentication(this, DatabaseURL.app_id, DatabaseURL.api_key);
        listener = new KairosListener() {
            @Override
            public void onSuccess(String response) {
                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
            }
            @Override
            public void onFail(String response) {
            }
        };

        try {
            String ListgalleryId = id;
            myKairos.listSubjectsForGallery(ListgalleryId, listener);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    // this function gps permission
    public void GPSpermission(){

        if ((int) Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // Should we show an explanation?
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION )) {
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},GPSPermission_id);
                    }else {
                        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.ACCESS_FINE_LOCATION)) {
                            android.support.v7.app.AlertDialog.Builder alertBuilder = new android.support.v7.app.AlertDialog.Builder(context);
                            alertBuilder.setCancelable(true);
                            alertBuilder.setTitle("Permission necessary");
                            alertBuilder.setMessage("GPS permission is necessary");
                            alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                            GPSPermission_id);
                                }
                            });
                            android.support.v7.app.AlertDialog alert = alertBuilder.create();
                            alert.show();
                        }
                    }
                }
                return;
            }else{
                checkPermission = true;
            }
        }else{
            MakeFaceRecognition();
        }
    }

    public void onRequestPermissionsResult(int requestCode,String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode)
        {
            case GPSPermission_id:
                // If request is cancelled, the result arrays are empty.
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    checkPermission = true;
                }
                break;

        }
    }

    // this function get all attribute and put in upload image to add in database
    public void DB(String randomNumber){

        GPSTracker gpsTracker = new GPSTracker(context);
        String stringLatitude = String.valueOf(gpsTracker.latitude);
        String stringLongitude = String.valueOf(gpsTracker.longitude);
        String addressLine = gpsTracker.getAddressLine(context);
        String city = gpsTracker.getLocality(context);
        String country = gpsTracker.getCountryName(context);
        String imageString = getStringImage(childbitmap);
        if(city == null){
            city = "helwan";
        }
        if(addressLine == null){
            city = "Elmsakn";
        }
        if(country == null){
            city = "Egypt";
        }


        int galleryId = 1;
        if(owner.equals("parent")){
            galleryId = 1;
            uploadImage(childName, childAge, childSkinColor, childHairColor, childEyeColor, childLength , childLostDate,  stringLongitude,
                    stringLatitude,addressLine, city, country ,imageString, user_id , randomNumber, String.valueOf(galleryId));
        }else{
            galleryId = 2;
            uploadImage(stringLongitude, stringLatitude,addressLine, city, country ,imageString, user_id , randomNumber, String.valueOf(galleryId));
        }

    }

    // this function get random number of subject id for image
    public int getRandom(){
        String check = null;
        Random randomGenerator = new Random();
        randomInt = randomGenerator.nextInt(1000);
        String type = "checkImageID";
        DatabaseBackground db = new DatabaseBackground(context);
        try {
            check = db.execute(type, String.valueOf(randomInt)).get();
            while (check.equals("found")){
                randomInt = randomGenerator.nextInt(1000);
                check = db.execute(type, String.valueOf(randomInt)).get();
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return randomInt;
    }

    // this function put in known gallery with have all info of missing including child data
    public void uploadImage(final String childName, final String childAge, final String childSkinColor, final String childHairColor,
                            final String childEyeColor, final String childLength , final String Lostdate, final String longtidue,
                            final String latitude, final String addressLine, final String city, final String country,
                            final String imageString, final String user_id , final String subjectId, final String galleryType){

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
                param.put(DatabaseURL.KEY_childName,childName);
                param.put(DatabaseURL.KEY_childAge,childAge);
                param.put(DatabaseURL.KEY_childSkinColor,childSkinColor);
                param.put(DatabaseURL.KEY_childHairColor,childHairColor);
                param.put(DatabaseURL.KEY_childEyeColor, childEyeColor);
                param.put(DatabaseURL.KEY_childLength,childLength);
                param.put(DatabaseURL.KEY_childLostDate,Lostdate);
                param.put(DatabaseURL.KEY_longtidue,longtidue);
                param.put(DatabaseURL.KEY_latitude,latitude);
                param.put(DatabaseURL.KEY_addressLine,addressLine);
                param.put(DatabaseURL.KEY_city,city);
                param.put(DatabaseURL.KEY_country,country);
                param.put(DatabaseURL.KEY_image,imageString);
                param.put(DatabaseURL.KEY_subjectId,subjectId);
                String result = rh.sendPostRequest(DatabaseURL.Gallery_url, param);
                return result;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if(s.equals("All queries were executed successfully")){
                    DatabaseBackground db = new DatabaseBackground(context);
                    try {
                        String type = "Gallery";

                        Date date = Calendar.getInstance().getTime();
                        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                        String today = formatter.format(date);

                        String result = db.execute(type, user_id, galleryType, today).get();
                        if(result.equals("success")){
                            startActivity(new Intent(context, Home.class));
                        }else{
                            String Dtype ="deleteFromGallery";
                            DatabaseBackground deleteAddes = new DatabaseBackground(context);
                            String r = deleteAddes.execute(Dtype, user_id, galleryType).get();
                            if(!r.equals("Failed")){
                                startActivity(new Intent(context, Home.class));
                                finish();
                            }
                        }
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(context, "Failed To add in DataBase", Toast.LENGTH_LONG).show();
                    DeleteSubject(String.valueOf(randomID));
                }
            }
        }
        UploadImage u = new UploadImage();
        u.execute();
    }

    // this function put in known gallery with have all info of missing except child data
    public void uploadImage(final String longtidue, final String latitude, final String addressLine, final String city, final String country,
                            final String imageString, final String user_id , final String subjectId, final String galleryType){

        //Log.d("response",longtidue+ " : "+latitude+" : "+addressLine+ " : "+ city+ ":" + country + " : " + user_id+ " : " + subjectId+ " : " +galleryType);

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
                param.put(DatabaseURL.KEY_longtidue,longtidue);
                param.put(DatabaseURL.KEY_latitude,latitude);
                param.put(DatabaseURL.KEY_addressLine,addressLine);
                param.put(DatabaseURL.KEY_city,city);
                param.put(DatabaseURL.KEY_country,country);
                param.put(DatabaseURL.KEY_image,imageString);
                param.put(DatabaseURL.KEY_subjectId,subjectId);
                String result = rh.sendPostRequest(DatabaseURL.addunknownMissing_url, param);
                return result;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if(s.equals("All queries were executed successfully")){
                    DatabaseBackground db = new DatabaseBackground(context);
                    try {
                        String type = "addGalleryMissing";

                        Date date = Calendar.getInstance().getTime();
                        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                        String today = formatter.format(date);

                        String result = db.execute(type, user_id, galleryType, today).get();
                        if(result.equals("success")){
                            startActivity(new Intent(context, Home.class));
                        }else{
                            String Dtype ="deleteFromGallery";
                            DatabaseBackground deleteAddes = new DatabaseBackground(context);
                            String r = deleteAddes.execute(Dtype, user_id, galleryType).get();
                            if(!r.equals("Failed")){
                                startActivity(new Intent(context, Home.class));
                                finish();
                            }
                        }
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(context, "Failed To add in DataBase", Toast.LENGTH_LONG).show();
                    DeleteSubject(String.valueOf(randomID));
                }
            }
        }
        UploadImage u = new UploadImage();
        u.execute();
    }

    // this function display pop up menu
    private void initiatePopupWindow() {
        try {
            LayoutInflater inflater = (LayoutInflater) FaceRecognition.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View layout = inflater.inflate(R.layout.pop_up, (ViewGroup) findViewById(R.id.popRelative));

            pw = new PopupWindow(layout, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
            // display the popup in the center
            pw.setAnimationStyle(R.style.animationName);
            pw.showAtLocation(layout, Gravity.CENTER, 0, 0);

            firstInfo = (TextView) layout.findViewById(R.id.firstInfo);
            secondInfo = (TextView) layout.findViewById(R.id.secondInfo);
            thirdInfo = (TextView) layout.findViewById(R.id.thirdInfo);
            fourthInfo = (TextView) layout.findViewById(R.id.fourthInfo);
            fifthInfo = (TextView) layout.findViewById(R.id.fifthInfo);
            cancelimage = (ImageView) layout.findViewById(R.id.popUpImage);


            firstInfo.setText("1) Click on Face recognition button the app will match your image with all the " +
                    "missing images on system");
            secondInfo.setVisibility(View.GONE);
            thirdInfo.setVisibility(View.GONE);
            fourthInfo.setVisibility(View.GONE);
            fifthInfo.setVisibility(View.GONE);

            cancelimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pw.dismiss();
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
