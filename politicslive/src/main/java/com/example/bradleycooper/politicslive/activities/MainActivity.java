package com.example.bradleycooper.politicslive.activities;

import android.app.FragmentManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBar;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.bradleycooper.politicslive.dataobjects.Event;
import com.example.bradleycooper.politicslive.datasources.EventDataSource;
import com.example.bradleycooper.politicslive.fragments.HomePage;
import com.example.bradleycooper.politicslive.R;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        HomePage.OnFragmentInteractionListener {

    private CharSequence mTitle;
    public final static String IS_LOGIN_USER = "pref_is_login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();



        mTitle = getTitle();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        /* If notifications are not set, set event notifications */
        ArrayList<Event> arrayListEvents;
        EventDataSource dataSourceEvents = new EventDataSource(MainActivity.this);
        dataSourceEvents.open();
        arrayListEvents = dataSourceEvents.getEvents();
        dataSourceEvents.close();

        for(int i = 0; i < arrayListEvents.size(); i++){
            setNotifications(arrayListEvents.get(i));
        }
        checkIfAppHasBeenInitialized();
    }

    /* CHECK TO SEE IF APP HAS BEEN INITIALIZED (First method called when app is opened)
        ~YES - load HomePage Fragment and open app normally
        ~NO (user opened app for first time) - go to LoginActivity to initialize app */
    private void setNotifications(Event event){
        String notifTitle = event.getName();
        String notifDescription = "Hosted by CNN, watch tonight at 9:00pm EST";
        int notifPic = R.drawable.app_icon_for_notif;

        Calendar Calendar_Object  = Calendar.getInstance();
        Calendar_Object.set(Calendar.MONTH, 10);
        Calendar_Object.set(Calendar.YEAR, 2016);
        Calendar_Object.set(Calendar.DAY_OF_MONTH, 4);

        Calendar_Object.set(Calendar.HOUR_OF_DAY, 5);
        Calendar_Object.set(Calendar.MINUTE, 4);
        Calendar_Object.set(Calendar.SECOND, 0);

        PendingIntent pi = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);
        Resources r = getResources();
        Notification notification = new NotificationCompat.Builder(this)
                .setTicker(r.getString(R.string.app_name))
                .setSmallIcon(notifPic)
                .setContentTitle(notifTitle)
                .setContentText(notifDescription)
                .setContentIntent(pi)
                .setAutoCancel(true)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);

    }
    private void checkIfAppHasBeenInitialized() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        String userName = preferences.getString("Username", "");

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

        if (id == R.id.nav_settings) {
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
    public void restoreActionBar () {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }
    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
