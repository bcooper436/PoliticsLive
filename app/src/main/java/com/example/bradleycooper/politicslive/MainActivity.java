package com.example.bradleycooper.politicslive;

import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        RepublicansList.OnFragmentInteractionListener,
        DemocratsList.OnFragmentInteractionListener,
        HomePage.OnFragmentInteractionListener,
        HomePage.OnCommunicateActivityListener,
        UserProfile.OnFragmentInteractionListener,
        UsersList.OnFragmentInteractionListener,
        AllCandidates.OnFragmentInteractionListener,
        AllCandidates.OnCommunicateActivityListener {

    private CharSequence mTitle;
    public final static String IS_LOGIN_USER = "pref_is_login";
    public int resize = 150;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Ballot.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        mTitle = getTitle();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String userName = preferences.getString("Username", "");
        Menu menu = navigationView.getMenu();
        MenuItem nav_camara = menu.findItem(R.id.logout);
        if(userName.equalsIgnoreCase("") || userName == null) {
            nav_camara.setTitle("Login");
        }


        Fragment fragment;
        FragmentManager fragmentManager = getFragmentManager();
        fragment = new HomePage();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();

//        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
//        String userName = preferences.getString("Username", "");
        String skip = preferences.getString("Skip", "");
        if(userName.equalsIgnoreCase("") || userName==null)
        {
            if(skip == null || skip.equalsIgnoreCase("")) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }

        /*
        SharedPreferences preferences = getSharedPreferences("com.example.bradleycooper.politicslive", 0);
        String value = preferences.getString("userNameKey", null);
        if (value == null) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else {
            // handle the value
        }*/

        /* Check to see if database is storing any objects.  If not, populate with candidate information */
        CandidateDataSource ds = new CandidateDataSource(MainActivity.this);
        ds.open();
        if(ds.getLastCandidateId() < 1) {
            initializeDB();
        }

    }
    private static boolean doesDataBaseExist(Context context, String dbName){
        File dbFile = context.getDatabasePath(dbName);
        return dbFile.exists();
    }
    private Candidate currentCandidate;
    private User user;
    private void initializeDB(){
        Bitmap bitmapSquareBernie = BitmapFactory.decodeResource(getResources(),R.drawable.bernie_square);
        Bitmap bitmapSquareBernieResized = Bitmap.createScaledBitmap(bitmapSquareBernie, resize, resize, true);
        byte[] byteArraySquareBernie = convertBitmapToByteArray(bitmapSquareBernieResized);
        Bitmap bitmapSquareHilary = BitmapFactory.decodeResource(getResources(), R.drawable.hilary_square);
        Bitmap bitmapSquareHilaryResized = Bitmap.createScaledBitmap(bitmapSquareHilary, resize, resize, true);
        byte[] byteArraySquareHilary = convertBitmapToByteArray(bitmapSquareHilaryResized);
        Bitmap bitmapSquareTrump = BitmapFactory.decodeResource(getResources(), R.drawable.trump_square);
        Bitmap bitmapSquareTrumpResized = Bitmap.createScaledBitmap(bitmapSquareTrump, resize, resize, true);
        byte[] byteArraySquareTrump = convertBitmapToByteArray(bitmapSquareTrumpResized);
        Bitmap bitmapSquareCruz = BitmapFactory.decodeResource(getResources(), R.drawable.cruz_square);
        Bitmap bitmapSquareCruzResized = Bitmap.createScaledBitmap(bitmapSquareCruz, resize, resize, true);
        byte[] byteArraySquareCruz = convertBitmapToByteArray(bitmapSquareCruzResized);
        Bitmap bitmapSquareKasich = BitmapFactory.decodeResource(getResources(), R.drawable.kasich_square);
        Bitmap bitmapSquareKasichResized = Bitmap.createScaledBitmap(bitmapSquareKasich, resize, resize, true);
        byte[] byteArraySquareKasich = convertBitmapToByteArray(bitmapSquareKasichResized);

        Bitmap bitmapWideBernie = BitmapFactory.decodeResource(getResources(), R.drawable.bernie);
        byte[] byteArrayWideBernie = convertBitmapToByteArray(bitmapWideBernie);
        Bitmap bitmapWideHilary = BitmapFactory.decodeResource(getResources(), R.drawable.hilary);
        byte[] byteArrayWideHilary = convertBitmapToByteArray(bitmapWideHilary);
        Bitmap bitmapWideTrump = BitmapFactory.decodeResource(getResources(), R.drawable.trump);
        byte[] byteArrayWideTrump = convertBitmapToByteArray(bitmapWideTrump);
        Bitmap bitmapWideCruz = BitmapFactory.decodeResource(getResources(), R.drawable.cruz);
        byte[] byteArrayWideCruz = convertBitmapToByteArray(bitmapWideCruz);
        Bitmap bitmapWideKasich = BitmapFactory.decodeResource(getResources(), R.drawable.kasich);
        byte[] byteArrayWideKasich = convertBitmapToByteArray(bitmapWideKasich);


        /* Initialize table to  hold candidate objects */
        generateCandidates("Bernie Sanders", "Self-proclaimed democratic socialist from Vermont.", byteArraySquareBernie, byteArrayWideBernie, "DNC", 5);
        generateCandidates("Hilary Clinton", "Former Secretary of State, many years in politics.",byteArraySquareHilary, byteArrayWideHilary, "DNC", 6);
        generateCandidates("Donald Trump", "Successful Business man from New York City.", byteArraySquareTrump, byteArrayWideTrump,"GOP", 6);
        generateCandidates("Ted Cruz", "Reported Zodiac killer, wants to turn abolish secularism.",byteArraySquareCruz, byteArrayWideCruz,"GOP", 4);
        generateCandidates("John Kasich", "What am I still doing in this race anyways...?", byteArraySquareKasich, byteArrayWideKasich, "GOP", 1);

        /* Initialize the user table with 30 fake user accounts, to demonstrate the graphs and demographics potential */
        generateUsers("Bradley Cooper", "bcooper436", "zun3ukit", "Democrat", 22, "Male", "Bernie Sanders", "Donald Trump");
        generateUsers("Linden Marshall", "welcomehome8", "zun3ukit", "Republican", 31, "Male", "Bernie Sanders", "John Kasich");
        generateUsers("Rhonda Boley", "duneiversity", "zun3ukit", "Independent", 62, "Female", "Hilary Clinton", "Donald Trump");
        generateUsers("Rubisha Louqie", "SparklesGems80", "zun3ukit", "Democrat", 47, "Female", "Hilary Clinton", "Donald Trump");
        generateUsers("Mark Simmons", "marksimms", "zun3ukit", "Democrat", 50, "Male", "Hilary Clinton", "Ted Cruz");

        generateUsers("Willis Lesia", "williscool40", "zun3ukit", "Republican", 35, "Male", "Bernie Sanders", "Ted Cruz");
        generateUsers("Johnie Kiersten", "JoHnIeK", "zun3ukit", "Republican", 31, "Male", "Hilary Clinton", "Donald Trump");
        generateUsers("Biance Glade", "bglade11", "zun3ukit", "Democrat", 22, "Female", "Bernie Sanders", "Donald Trump");
        generateUsers("Melva Denis", "Mel0192", "zun3ukit", "Republican", 55, "Male", "Bernie Sanders", "Donald Trump");
        generateUsers("Trina Bryan", "BryTri92", "zun3ukit", "Democrat", 17, "Female", "Hilary Clinton", "Ted Cruz");
        generateUsers("Oswald Webster", "theBookWasBetter", "zun3ukit", "Independent", 85, "Male", "Hilary Clinton", "Ted Cruz");


    }
    public void generateUsers(String displayName, String username, String password, String party, int age, String gender, String chosenDemocrat, String chosenRepublican){
        boolean wasSuccessful;

        user = new User();
        user.setDisplayName(displayName);
        user.setUserName(username);
        user.setPassword(password);
        user.setPartyAffiliation(party);
        user.setAge(age);
        user.setGender(gender);
        //user.setChosenDemocrat(chosenDemocrat);
        //user.setChosenRepublican(chosenRepublican);
        UserDataSource us = new UserDataSource(MainActivity.this);
        us.open();
        if(user.getUserID() == -1){
            wasSuccessful = us.insertUser(user);
            int newId = us.getLastUserId();
            user.setUserID(newId);
        }
        else{
            wasSuccessful = us.updateUser(user);
        }
        us.close();

        UserDataSource userDataSource = new UserDataSource(MainActivity.this);
        userDataSource.open();
        user = userDataSource.getSpecificUserFromLoginInfo(user.getUserName(),"zun3ukit");
        user.setChosenDemocrat(chosenDemocrat);
        user.setChosenRepublican(chosenRepublican);
        userDataSource.updateUser(user);
        userDataSource.close();

        if (wasSuccessful) {
            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
            alertDialog.setTitle("Success");
            alertDialog.setMessage("Candidate " + currentCandidate.getCandidateName() + " was added as a member of the " + currentCandidate.getParty() + " party. Id is " + currentCandidate.getCandidateID());
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",

                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }

    }
    public void generateCandidates(String candidateName, String candidateDescription, byte[] byteArraySquare, byte[] byteArrayWide ,String party, int numberOfVotes){
        currentCandidate = new Candidate();
        currentCandidate.setCandidateName(candidateName);
        currentCandidate.setCandidateDescription(candidateDescription);
        currentCandidate.setSquarePicture(byteArraySquare);
        currentCandidate.setWidePicture(byteArrayWide);
        currentCandidate.setParty(party);
        currentCandidate.setNumberOfVotes(numberOfVotes);
        CandidateDataSource ds = new CandidateDataSource(MainActivity.this);
        ds.open();

        boolean wasSuccessful = false;
        if(currentCandidate.getCandidateID() == -1) {
            wasSuccessful = ds.insertCandidate(currentCandidate);
            int newId = ds.getLastCandidateId();
            currentCandidate.setCandidateID(newId);
        }
        else{
            wasSuccessful = ds.updateCandidate(currentCandidate);
        }
        ds.close();
        if (wasSuccessful) {
            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
            alertDialog.setTitle("Success");
            alertDialog.setMessage("Candidate " + currentCandidate.getCandidateName() + " was added as a member of the " + currentCandidate.getParty() + " party. Id is " + currentCandidate.getCandidateID());
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",

                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
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
        } else if (id == R.id.nav_GOP) {
            fragment = new RepublicansList();
            mTitle = "Republican Party" +
                    "";

        } else if (id == R.id.nav_Candidates) {
            fragment = new AllCandidates();
            mTitle = "All Candidates" +
                    "";
        } else if (id == R.id.logout) {
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

        } else if (id == R.id.nav_DNC) {
            fragment = new DemocratsList();
            mTitle = "Democratic Party";
        } else if (id == R.id.nav_users_list){
            fragment = new UsersList();
            mTitle = "Browse Users";
        }

        else if (id == R.id.nav_settings) {
            Intent intent = new Intent(MainActivity.this, AppSettings.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        else if (id == R.id.nav_user) {
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
                                dialog.dismiss();
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

    public boolean onNavigationItemSelected(String fragmentToLoad) {
        // Handle navigation view item clicks here.
        Fragment fragment;
        FragmentManager fragmentManager = getFragmentManager();
        fragment = new HomePage();
        if (fragmentToLoad.equalsIgnoreCase("home")) {
            fragment = new HomePage();
            mTitle = "Politics Live!";
        } else if (fragmentToLoad.equalsIgnoreCase("republicanslist")) {
            fragment = new RepublicansList();
            mTitle = "Republican Party" +
                    "";

        } else if (fragmentToLoad.equalsIgnoreCase("allcandidates")) {
            fragment = new AllCandidates();
            mTitle = "All Candidates" +
                    "";
        } else if (fragmentToLoad.equalsIgnoreCase("logout")) {
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

        } else if (fragmentToLoad.equalsIgnoreCase("democratslist")) {
            fragment = new DemocratsList();
            mTitle = "Democratic Party";
        } else if (fragmentToLoad.equalsIgnoreCase("userslist")){
            fragment = new UsersList();
            mTitle = "Browse Users";
        }

        else if (fragmentToLoad.equalsIgnoreCase("settings")) {
            Intent intent = new Intent(MainActivity.this, AppSettings.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
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
        if(nevID == R.id.nav_DNC){
            mTitle = "Democratic Party";
            MenuItem nav_camara = menu.findItem(R.id.nav_DNC);
            nav_camara.setChecked(true);
            restoreActionBar();
        }else if(nevID == R.id.nav_GOP){
            mTitle = "Republican Party" + "";
            MenuItem nav_camara = menu.findItem(R.id.nav_GOP);
            nav_camara.setChecked(true);
            restoreActionBar();
        }

    }
    public FloatingActionButton getFloatingActionButton (){
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        return fab;
    }
    public void showFloatingActionButton() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.show();
    };

    public void hideFloatingActionButton() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.hide();
    };
}
