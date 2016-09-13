package com.example.bradleycooper.politicslive;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        HomePage.OnFragmentInteractionListener,
        HomePage.OnCommunicateActivityListener,
        UserProfile.OnFragmentInteractionListener,
        UsersList.OnFragmentInteractionListener,
        AllCandidates.OnFragmentInteractionListener,
        AllCandidates.OnCommunicateActivityListener,
        PollsPage.OnFragmentInteractionListener,
        SettingsFragment.OnFragmentInteractionListener,
        ListOfPoliticalParties.OnFragmentInteractionListener,
        ListOfEvents.OnFragmentInteractionListener,
        ListOfPolls.OnFragmentInteractionListener{

    private CharSequence mTitle;
    public final static String IS_LOGIN_USER = "pref_is_login";

    private String api_clinton_favorable = "http://elections.huffingtonpost.com/pollster/api/charts/hilary-clinton-favorable-rating.json";
    private String api_sanders_favorable = "http://elections.huffingtonpost.com/pollster/api/charts/bernie-sanders-favorable-rating.json";
    private String api_trump_favorable = "http://elections.huffingtonpost.com/pollster/api/charts/donald-trump-favorable-rating.json";
    private String api_cruz_favorable = "http://elections.huffingtonpost.com/pollster/api/charts/ted-cruz-favorable-rating.json";
    private String api_kasich_favorable = "http://elections.huffingtonpost.com/pollster/api/charts/john-kasich-favorable-rating.json";
    private  String api_republicans = "http://elections.huffingtonpost.com/pollster/api/charts/2016-national-gop-primary.json";
    private  String api_democrats = "http://elections.huffingtonpost.com/pollster/api/charts/2016-national-democratic-primary.json";

    public int NUMBER_OF_DATABASES_TO_POPULATE = 3;
    ProgressDialog progressBar;
    int progressBarProgress = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //getSupportActionBar().setIcon(R.drawable.cnn_60);
        //getSupportActionBar().setIcon(R.drawable.pl2);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();



        mTitle = getTitle();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        String userName = preferences.getString("Username", "");

        Menu menu = navigationView.getMenu();
        MenuItem nav_user = menu.findItem(R.id.nav_user);
        if(userName.equalsIgnoreCase("") || userName == null) {
            nav_user.setTitle("Login");
        }

        // Check to see if user has chosen to skip logging in, if not, redirect to login page
        String skip = preferences.getString("Skip", "");
        if(userName.equalsIgnoreCase("") || userName==null)
        {
            if(skip == null || skip.equalsIgnoreCase("")) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("TEST", "First time opening app");
                startActivity(intent);
            }
            else {

                Fragment fragment;
                FragmentManager fragmentManager = getFragmentManager();
                fragment = new HomePage();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, fragment)
                        .commit();
            }
        }

    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        restoreActionBar();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Fragment fragment;
        FragmentManager fragmentManager = getFragmentManager();
        int id = item.getItemId();
        fragment = new HomePage();
        if (id == R.id.nav_home) {
            fragment = new HomePage();
            mTitle = "Politics Live!";
        } else if (id == R.id.nav_all_candidates) {
            fragment = new AllCandidates();
            mTitle = "Presidential Candidates" +
                    "";
        }
        else if (id == R.id.nav_settings) {
            fragment = new SettingsFragment();
            mTitle = "Settings" +
                    "";
        }else if (id == R.id.nav_users_list) {
            fragment = new UsersList();
            mTitle = "Explore Users of the App" +
                    "";
        }else if (id == R.id.nav_polls) {
            fragment = new ListOfPolls();
            mTitle = "Polls and Graphs" +
                    "";
        }  else if (id == R.id.nav_calendar) {
            fragment = new ListOfEvents();
            mTitle = "Upcoming Events" +
                    "";
        } else if (id == R.id.nav_political_party) {
            fragment = new ListOfPoliticalParties();
            mTitle = "Political Parties" +
                    "";
        } else if (id == R.id.nav_user) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            String userName = preferences.getString("Username", "");
            if (userName.equalsIgnoreCase("") || userName == null) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
            else {
                fragment = new UserProfile();
                mTitle = userName +
                        "";
            }
        }
        else if (id == R.id.nav_settings) {
            Intent intent = new Intent(MainActivity.this, AppSettings.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        restoreActionBar();

        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public boolean onNavigationItemSelected(String fragmentToLoad) {
        // Handle navigation view item clicks here.
        Fragment fragment;
        FragmentManager fragmentManager = getFragmentManager();
        fragment = new HomePage();
        if (fragmentToLoad.equalsIgnoreCase("home")) {
            fragment = new HomePage();
            mTitle = "Race for the White House";
        }  else if (fragmentToLoad.equalsIgnoreCase("logout")) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            String userName = preferences.getString("Username", "");
            if(userName.equalsIgnoreCase("") || userName == null) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }else{
                AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                builder1.setMessage("Are you sure you want to logout?");
                builder1.setCancelable(true);

                builder1.setNegativeButton(
                        "Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                builder1.setPositiveButton(
                        "Logout",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                clearUserPreferences();
                                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }

        } else if (fragmentToLoad.equalsIgnoreCase("userslist")){
            fragment = new UsersList();
            mTitle = "Browse Users";
        }

        else if (fragmentToLoad.equalsIgnoreCase("settings")) {
            fragment = new SettingsFragment();
            mTitle = "Settings";
        }
        else if (fragmentToLoad.equalsIgnoreCase("user")) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            String userName = preferences.getString("Username", "");
            if(userName.equalsIgnoreCase("") || userName == null) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                builder1.setMessage("You must be logged in to access this page.");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Login",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                        });

                builder1.setNegativeButton(
                        "Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();

            }
            else {
                fragment = new UserProfile();
                mTitle = userName +
                        "";
            }
        }
        restoreActionBar();

        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void restoreActionBar () {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }
    public byte[] convertBitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream(bitmap.getWidth() * bitmap.getHeight());
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, buffer);
        return buffer.toByteArray();
    }
    public void clearUserPreferences(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Username", "");
        editor.putString("Password", "");
        editor.apply();
    }

    public void setUserPreferences(boolean isLogin){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(IS_LOGIN_USER, isLogin);
        editor.commit();
    }

    public boolean getIsLogin(){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        return pref.getBoolean(IS_LOGIN_USER, false);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void passDataToActivity(int nevID) {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Menu menu = navigationView.getMenu();
        if(nevID == R.id.nav_home){
            mTitle = "Politics Live!";
            MenuItem nav_camara = menu.findItem(R.id.nav_home);
            nav_camara.setChecked(true);
            restoreActionBar();
        }else if(nevID == R.id.nav_all_candidates){
            mTitle = "All Candidates";
            MenuItem nav_camara = menu.findItem(R.id.nav_all_candidates);
            nav_camara.setChecked(true);
            restoreActionBar();
        }
        else if (nevID == R.id.nav_political_party) {
            mTitle = "Political Parties";
            MenuItem nav_camara = menu.findItem(R.id.nav_political_party);
            nav_camara.setChecked(true);
            restoreActionBar();
        }
        else if(nevID == R.id.nav_user){
            mTitle = "Your Profile";
            MenuItem nav_camara = menu.findItem(R.id.nav_user);
            nav_camara.setChecked(true);
            restoreActionBar();
        }
        else if(nevID == R.id.nav_settings){
            /* mTitle = "Browse Users";
            MenuItem nav_camara = menu.findItem(R.id.nav_users_list);
            nav_camara.setChecked(true);
            restoreActionBar(); */
        }


    }

}
