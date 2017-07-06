package com.example.mahmoudheshmat.missing.Activites;


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mahmoudheshmat.missing.Fragments.HomeFragmentUnknown;
import com.example.mahmoudheshmat.missing.Fragments.HomeFragmentknown;
import com.example.mahmoudheshmat.missing.Fragments.Notification_Fragment;
import com.example.mahmoudheshmat.missing.R;
import com.example.mahmoudheshmat.missing.Fragments.chat_Fragment;
import com.example.mahmoudheshmat.missing.Fragments.languageFragment;
import com.example.mahmoudheshmat.missing.notify;
import com.example.mahmoudheshmat.missing.utils.GPSTracker;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.R.attr.windowSoftInputMode;

public class Home extends AppCompatActivity implements languageFragment.Communictor {

    Context context;

    //Internet Connection
    boolean isConnected;
    Locale myLocale;


    //permission Dialog
    public static final int GalleryPermission_id = 1;
    public static final int CameraPermission_id = 2;
    Boolean checkPermission = false;
    Boolean cameraCheckPermission = false;

    //Gallery
    private int PICK_IMAGE_REQUEST = 1;
    static final int REQUEST_IMAGE_CAPTURE = 2;
    private Bitmap bitmap = null;
    Uri imageurl = null;

    static String user_type = null;

    //Drawer
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;

    //Fragment
    FragmentTransaction fragmentTransaction;

    //SharedPreferences
    SharedPreferences sharedPreferences;
    SharedPreferences languageprefernce;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String MyLANGUAGEPREFERENCES = "MyLangPrefs" ;

    MenuItem search_item ;
    MenuItem filter_item ;

    FloatingActionButton fab;

    List<Fragment> activeCenterFragments = new ArrayList<Fragment>();

    Fragment currentFragment = null;

    GPSTracker gpsTracker;

    // Tabbed Layout
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.home));

        context = this;



        try {
            createImageFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //SharedPreferences
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        languageprefernce = getSharedPreferences(MyLANGUAGEPREFERENCES, Context.MODE_PRIVATE);
        String headerUserName = sharedPreferences.getString("User_username", null);

        // Bottom Navigation Drawer
        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);
        bottomNavigationView.setItemIconTintList(null);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.chat:
                                if(internetConnection()){

                                    currentFragment = getSupportFragmentManager().findFragmentByTag("notify");
                                    if(currentFragment == null){
                                        currentFragment = getSupportFragmentManager().findFragmentByTag("chat");
                                        if (currentFragment == null){
                                            chat_Fragment chat = new chat_Fragment();
                                            //HomeFragmentknown home = new HomeFragmentknown();
                                            addCenterFragments(chat, "chat");
                                        }
                                    }else{
                                        Notification_Fragment notification_fragment = new Notification_Fragment();
                                        chat_Fragment chat = new chat_Fragment();

                                        add_delete(chat, notification_fragment, "chat");
                                    }

                                }else{
                                    Toast.makeText(getApplicationContext(), "Please Connent To Intennet", Toast.LENGTH_LONG).show();
                                }
                                break;
                            case R.id.notification:
                                /*if(internetConnection()){
                                    currentFragment = getSupportFragmentManager().findFragmentByTag("chat");
                                    if(currentFragment == null){
                                        currentFragment = getSupportFragmentManager().findFragmentByTag("notify");
                                        if (currentFragment == null){
                                            Notification_Fragment notification_fragment = new Notification_Fragment();
                                            addCenterFragments(notification_fragment, "notify");
                                        }
                                    }else{
                                        Notification_Fragment notification_fragment = new Notification_Fragment();
                                        chat_Fragment chat = new chat_Fragment();

                                        add_delete(notification_fragment, chat, "chat");
                                    }

                                }else{
                                    Toast.makeText(getApplicationContext(), "Please Connent To Intennet", Toast.LENGTH_LONG).show();
                                }*/

                                startActivity(new Intent(context, notify.class));

                                break;
                            case R.id.home_page:
                                if(internetConnection()){
                                    Fragment fragment = getSupportFragmentManager().findFragmentByTag("filter");
                                    if(fragment != null)
                                        getSupportFragmentManager().beginTransaction().remove(fragment).commit();

                                    Fragment fragment2 = getSupportFragmentManager().findFragmentByTag("filtered_data");
                                    if(fragment2 != null)
                                        getSupportFragmentManager().beginTransaction().remove(fragment).commit();

                                    removeActiveCenterFragments();
                                }else{
                                    Toast.makeText(getApplicationContext(), "Please Connent To Intennet", Toast.LENGTH_LONG).show();
                                }

                                break;
                        }
                        return false;
                    }
                });


        //Navigation Drawer
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,R.string.drawer_open, R.string.drawer_close );
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        navigationView = (NavigationView) findViewById(R.id.navigationView);
        View headerView =  navigationView.getHeaderView(0);
        TextView headerText = (TextView) headerView.findViewById(R.id.header);
        headerText.setText(headerUserName);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.homeDrawer:
                        if(internetConnection()){

                            drawerLayout.closeDrawers();
                        }else{
                            Toast.makeText(getApplicationContext(), "Please Connent To Intennet", Toast.LENGTH_LONG).show();
                        }
                        break;

                    case R.id.Add:
                        if(internetConnection()){
                            final CharSequence[] items = { getResources().getString(R.string.parent),
                                    getResources().getString(R.string.helper), getResources().getString(R.string.cancel)};

                            AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
                            builder.setTitle(R.string.add_photo);
                            builder.setItems(items, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int item) {

                                    if (items[item].equals(getResources().getString(R.string.parent))) {
                                        add_alert("parent");
                                    } else if (items[item].equals(getResources().getString(R.string.helper))) {
                                        add_alert("user");
                                    } else if (items[item].equals(getResources().getString(R.string.cancel))) {
                                        dialog.dismiss();
                                    }
                                }
                            });
                            builder.show();

                            drawerLayout.closeDrawers();
                        }else{
                            Toast.makeText(getApplicationContext(), "Please Connent To Intennet", Toast.LENGTH_LONG).show();
                        }
                        break;

                    case R.id.profile:
                        if(internetConnection()){
                            startActivity(new Intent(getApplicationContext(), profile.class));
                            drawerLayout.closeDrawers();
                        }else{
                            Toast.makeText(getApplicationContext(), "Please Connent To Intennet", Toast.LENGTH_LONG).show();
                        }

                        break;

                    case R.id.logout:

                        if(internetConnection()){
                            sharedPreferences.edit().clear().commit();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                        }else{
                            Toast.makeText(getApplicationContext(), "Please Connent To Intennet", Toast.LENGTH_LONG).show();
                        }

                        break;

                    case R.id.language:
                        if(internetConnection()){
                            FragmentManager manager = getFragmentManager();
                            languageFragment myDialog = new languageFragment();
                            myDialog.show(manager, "language");
                            drawerLayout.closeDrawers();
                        }else{
                            Toast.makeText(getApplicationContext(), "Please Connect To Internet", Toast.LENGTH_LONG).show();
                        }
                        break;

                }

                return false;
            }
        });


        // Floating action button
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CharSequence[] items = { getResources().getString(R.string.parent),
                        getResources().getString(R.string.helper), getResources().getString(R.string.cancel)};

                AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
                builder.setTitle(R.string.add_photo);
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {

                        if (items[item].equals(getResources().getString(R.string.parent))) {
                            add_alert("parent");
                        } else if (items[item].equals(getResources().getString(R.string.helper))) {
                            add_alert("user");
                        } else if (items[item].equals(getResources().getString(R.string.cancel))) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            }
        });



        //Tabbed Layout
        viewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
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

    // Function get language selection from language dialog
    @Override
    public void DialogMessage(String messgae) {
        languageprefernce.edit().remove("language").commit();
        if(messgae == "ar"){
            languageprefernce.edit().putString("language", "ar").commit();
            setLocale("ar");
        }
        if(messgae == "en"){
            languageprefernce.edit().putString("language", "en").commit();
            setLocale("en");
        }

    }

    // Function change the language of app
    public void setLocale(String lang) {

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

    //permissions
    public void Gallerypermission(){
        if ((int) Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                // Should we show an explanation?
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE )) {
                        requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},GalleryPermission_id);
                    }else {
                        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                            alertBuilder.setCancelable(true);
                            alertBuilder.setTitle("Permission necessary");
                            alertBuilder.setMessage("Gallery permission is necessary");
                            alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                                            GalleryPermission_id);
                                }
                            });
                            AlertDialog alert = alertBuilder.create();
                            alert.show();
                        }
                    }
                }
                return;
            }else{
                checkPermission = true;
            }
        }else{
            showFileChooser();
        }
    }

    public void Camerapermission(){
        if ((int) Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                // Should we show an explanation?
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA )) {
                        requestPermissions(new String[]{android.Manifest.permission.CAMERA},CameraPermission_id);
                    }else {
                        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, android.Manifest.permission.CAMERA)) {
                            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                            alertBuilder.setCancelable(true);
                            alertBuilder.setTitle("Permission necessary");
                            alertBuilder.setMessage("Camera permission is necessary");
                            alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.CAMERA},
                                            CameraPermission_id);
                                }
                            });
                            AlertDialog alert = alertBuilder.create();
                            alert.show();
                        }
                    }
                }
                return;
            }else{
                cameraCheckPermission = true;
            }
        }else{
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    public void onRequestPermissionsResult(int requestCode,String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode)
        {
            case GalleryPermission_id:
                // If request is cancelled, the result arrays are empty.
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    checkPermission = true;
                    showFileChooser();
                }
                break;

            case  CameraPermission_id:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    cameraCheckPermission = true;
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    }
                }
                break;

        }
    }

    // Gallery
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            imageurl = filePath;
            if(user_type.equals("user")){
                Intent intent = new Intent(getApplicationContext(), FaceRecognition.class);
                intent.putExtra("owner", "user");
                intent.putExtra("childImage", imageurl);
                startActivity(intent);
            }else if(user_type.equals("parent")){
                Intent intent = new Intent(getApplicationContext(), childData.class);
                intent.putExtra("key", "parent");
                intent.putExtra("childImage", imageurl);
                startActivity(intent);
            }
        }

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            bitmap = imageBitmap;
            imageurl = getImageUri(context, bitmap);
            if(user_type.equals("user")){
                Intent intent = new Intent(getApplicationContext(), FaceRecognition.class);
                intent.putExtra("owner", "user");
                intent.putExtra("childImage", imageurl);
                startActivity(intent);
            }else if(user_type.equals("parent")){
                Intent intent = new Intent(getApplicationContext(), childData.class);
                intent.putExtra("key", "parent");
                intent.putExtra("childImage", imageurl);
                startActivity(intent);
            }
        }
    }

    //Function display alert dialog about take picture or gallery
    public void add_alert(final String userType){

        user_type = userType;

        final CharSequence[] items = { getResources().getString(R.string.take_photo),
                getResources().getString(R.string.choose_from_lib), getResources().getString(R.string.cancel)};

        AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
        builder.setTitle(getResources().getString(R.string.add_photo));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals(getResources().getString(R.string.take_photo))) {
                    Camerapermission();
                    if(cameraCheckPermission){
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        // Ensure that there's a camera activity to handle the intent
                        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                        }
                    }
                } else if (items[item].equals(getResources().getString(R.string.choose_from_lib))) {
                    Gallerypermission();
                    if(checkPermission){
                        showFileChooser();
                    }

                } else if (items[item].equals(getResources().getString(R.string.cancel))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    //View pager of tabbed layout
    // Tabbed Host and view Pager
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new HomeFragmentknown(), getString(R.string.parentGallery));
        adapter.addFragment(new HomeFragmentUnknown(), getString(R.string.userGallery));
        //adapter.addFragment(new chat_Fragment(), getString(R.string.userGallery));
        viewPager.setAdapter(adapter);
    }
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(android.support.v4.app.FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


    // Menu of search and filter
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.gallery,menu);
        search_item = menu.findItem(R.id.action_search);
        filter_item = menu.findItem(R.id.filter);

        search_item.setVisible(true);
        filter_item.setVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    // Add Fragment in home
    private void addCenterFragments(Fragment fragment, String tag) {
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.main, fragment, tag);
        fragmentTransaction.commit();
        activeCenterFragments.add(fragment);
    }

    // Remove all fragments
    private void removeActiveCenterFragments() {
        if (activeCenterFragments.size() > 0) {
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            for (Fragment activeFragment : activeCenterFragments) {
                fragmentTransaction.remove(activeFragment);
            }
            activeCenterFragments.clear();
            fragmentTransaction.commit();
        }
    }

    // Remove previous fragment and add new one
    public void add_delete(Fragment AddedFragment, Fragment RemovedFragment, String add_tag){

        fragmentTransaction = getSupportFragmentManager().beginTransaction();

        if (activeCenterFragments.size() > 0) {
            fragmentTransaction.remove(RemovedFragment);
            fragmentTransaction.commit();

            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.main, AddedFragment, add_tag);
            fragmentTransaction.commit();
            activeCenterFragments.add(AddedFragment);
        }
    }

    String mCurrentPhotoPath;
    // Make Dircetory of image
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    //convert bitmap to uri
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}

