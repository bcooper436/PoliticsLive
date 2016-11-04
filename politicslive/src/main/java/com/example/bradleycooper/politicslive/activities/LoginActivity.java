package com.example.bradleycooper.politicslive.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.bradleycooper.politicslive.dataobjects.Candidate;
import com.example.bradleycooper.politicslive.datasources.CandidateDataSource;
import com.example.bradleycooper.politicslive.dataobjects.Event;
import com.example.bradleycooper.politicslive.datasources.EventDataSource;
import com.example.bradleycooper.politicslive.dataobjects.ExitPoll;
import com.example.bradleycooper.politicslive.datasources.ExitPollDataSource;
import com.example.bradleycooper.politicslive.dataobjects.PoliticalParty;
import com.example.bradleycooper.politicslive.datasources.PoliticalPartyDataSource;
import com.example.bradleycooper.politicslive.R;
import com.example.bradleycooper.politicslive.dataobjects.User;
import com.example.bradleycooper.politicslive.datasources.UserDataSource;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class LoginActivity extends AppCompatActivity {

    UserDataSource userDataSource;
    User currentUser;

    /* Specify how many items need to be process, set max for progress bar so that once all are processed, it goes away */
    public int NUMBER_OF_EXIT_POLLS = 1555;
    public int NUMBER_OF_USERS = 23;
    public int NUMBER_OF_CANDIDATES = 6;
    public int NUMBER_OF_POLITICAL_PARTIES = 4;
    public int NUMBER_OF_EVENTS = 5;
    public int NUMBER_OF_APIS = 6;
    //Total amount of items to process
    public int NUMBER_OF_DATABASES_TO_POPULATE = NUMBER_OF_EXIT_POLLS + NUMBER_OF_USERS + NUMBER_OF_CANDIDATES + NUMBER_OF_POLITICAL_PARTIES + NUMBER_OF_EVENTS + NUMBER_OF_APIS;

    /* Other fields to help populate database */
    int votesForHilary = 0, votesForBernie = 0, votesForTrump = 0, votesForCruz = 0, votesForKasich = 0, votesForGary = 0;
    private Candidate currentCandidate;
    private PoliticalParty currentPoliticalParty;
    private Event currentEvent;
    private User user;

    String[][] arrayOfVoterPercentages;

    int numberOfCandidatesToGenerate = 6;
    int numberOfCandidatesGenerated = 0;

    public String API_2016_NATIONAL_GOP_PRIMARY;
    public String API_2016_NATIONAL_DEMOCRATIC_PRIMARY;
    public String API_REPUBLICAN_PARTY_FAVORABLE_RATING;
    public String API_DEMOCRATIC_PARTY_FAVORABLE_RATING;

    public String API_2016_GENERAL_ELECTION;
    public String API_2016_NATIONAL_HOUSE_RACE;

    public String API_HILLARY_CLINTON_FAVORABLE_RATING;
    public String API_BERNIE_SANDERS_FAVORABLE_RATING;
    public String API_DONALD_TRUMP_FAVORABLE_RATING;
    public String API_TED_CRUZ_FAVORABLE_RATING;
    public String API_JOHN_KASICH_FAVORABLE_RATING;

    public String API_PARTY_IDENTIFICATION;
    public String API_US_SATISFACTION;
    public String API_US_RIGHT_DIRECTION_WRONG_TRACK;
    public String API_CONGRESS_JOB_APPROVAL;

    /* Create string variables to hold the url's for different APIs */
    public String currentAPI;
    public String currentAPILabel;
            //Boolean Values to check progress of downloading APIs
    public Boolean API_2016_NATIONAL_GOP_PRIMARY_DONE = false,
            API_2016_NATIONAL_DEMOCRATIC_PRIMARY_DONE = false,
            API_REPUBLICAN_PARTY_FAVORABLE_RATING_DONE = false,
            API_DEMOCRATIC_PARTY_FAVORABLE_RATING_DONE = false,
            API_HILLARY_CLINTON_FAVORABLE_RATING_DONE = false,
            API_BERNIE_SANDERS_FAVORABLE_RATING_DONE = false,
            API_DONALD_TRUMP_FAVORABLE_RATING_DONE = false,
            API_TED_CRUZ_FAVORABLE_RATING_DONE = false,
            API_JOHN_KASICH_FAVORABLE_RATING_DONE = false,
            API_PARTY_IDENTIFICATION_DONE = false,
            API_US_SATISFACTION_DONE = false,
            API_US_RIGHT_DIRECTION_WRONG_TRACK_DONE = false,
            API_CONGRESS_JOB_APPROVAL_DONE = false,
            API_2016_GENERAL_ELECTION_DONE = false,
            API_2016_NATIONAL_HOUSE_RACE_DONE = false;



    ProgressDialog progressBar;
    int progressBarProgress = 0;
    Candidate candidate;
    CandidateDataSource ds;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        changeBackgroundColor();

        /* Check to see if user has opened app.  If not, initialize app */
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.get("TEST").equals("First time opening app")) {
                progressBar = new ProgressDialog(LoginActivity.this);
                progressBar.setCancelable(false);
                progressBar.setMessage("Initializing for the first time, please be patient, this may take up to one minute.");
                progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressBar.setProgress(0);
                progressBar.setMax(NUMBER_OF_DATABASES_TO_POPULATE);
                Drawable customDrawable = (Drawable) getResources().getDrawable(R.drawable.custom_progressbar);
                progressBar.setProgressDrawable(customDrawable);
                progressBar.show();

                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        initializeAppFirstTimeUse();
                    }
                }, 1);
            }
        }

        /* Theme status bar */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorBlueDark));
        }

        final EditText editTextUserName = (EditText)findViewById(R.id.editText_user_name);
        final EditText editTextPassword = (EditText) findViewById(R.id.editText_password);


        /* USE CASE : Logging In */
        Button loginButton = (Button) findViewById(R.id.buttonLogIn);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextUserName.length() > 0 && editTextPassword.length() > 0) {
                    /* Validation User Information (DOES NOT CHECK PASSWORD FOR EASY DEBUGGING PURPOSES)
                        Access local database of users
                        Check if information matches and respond yes or no
                         */
                    userDataSource = new UserDataSource(LoginActivity.this);
                    userDataSource.open();
                    currentUser = userDataSource.getSpecificUserFromLoginInfo(editTextUserName.getText().toString(), editTextPassword.getText().toString());
                    userDataSource.close();
                    // login successfull
                    if (currentUser.getUserName() != null) {
                        storeUserPreferences(editTextUserName.getText().toString(), editTextPassword.getText().toString());
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    } else {
                        AlertDialog alertDialog2 = new AlertDialog.Builder(LoginActivity.this).create();
                        alertDialog2.setMessage("Your username or password is incorrect, try again.");
                        alertDialog2.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog2.show();

                    }
                } else {
                    editTextPassword.setHintTextColor(Color.parseColor("#F07D7D"));
                    editTextUserName.setHintTextColor(Color.parseColor("#F07D7D"));
                }
            }
        });
        API_2016_NATIONAL_GOP_PRIMARY =getResources().getString(R.string.api_2016_national_gop_primary);
        API_2016_NATIONAL_DEMOCRATIC_PRIMARY = getResources().getString(R.string.api_2016_national_democratic_primary);
        API_REPUBLICAN_PARTY_FAVORABLE_RATING = getResources().getString(R.string.api_republican_party_favorable_rating);
        API_DEMOCRATIC_PARTY_FAVORABLE_RATING = getResources().getString(R.string.api_democratic_party_favorable_rating);

        API_2016_GENERAL_ELECTION = getResources().getString(R.string.api_2016_general_election);
        API_2016_NATIONAL_HOUSE_RACE = getResources().getString(R.string.api_2016_national_house_race);

        API_HILLARY_CLINTON_FAVORABLE_RATING = getResources().getString(R.string.api_hillary_clinton_favorable_rating);
        API_BERNIE_SANDERS_FAVORABLE_RATING = getResources().getString(R.string.api_bernie_sanders_favorable_rating);
        API_DONALD_TRUMP_FAVORABLE_RATING = getResources().getString(R.string.api_donald_trump_favorable_rating);
        API_TED_CRUZ_FAVORABLE_RATING = getResources().getString(R.string.api_ted_cruz_favorable_rating);
        API_JOHN_KASICH_FAVORABLE_RATING = getResources().getString(R.string.api_john_kasich_favorable_rating);

        API_PARTY_IDENTIFICATION = getResources().getString(R.string.api_party_identification);
        API_US_SATISFACTION = getResources().getString(R.string.api_us_satisfaction);
        API_US_RIGHT_DIRECTION_WRONG_TRACK = getResources().getString(R.string.api_us_right_direction_wrong_track);
        API_CONGRESS_JOB_APPROVAL = getResources().getString(R.string.api_congress_job_approval);
    }
    public void onBackPressed() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Skip","Yes");
        editor.apply();

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


    public void changeBackgroundColor(){
    }
    public void displayWelcomeText(){
        TextView welcome = (TextView) findViewById(R.id.welcomeText);
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        welcome.startAnimation(fadeIn);
    }

    public void Skip(View view){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Skip","Yes");
        editor.apply();

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    public void CreateNewAccount(View view){
        Intent intent = new Intent(LoginActivity.this, CreateUser.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    public void storeUserPreferences(String username, String password){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Username",username);
        editor.putString("Password", password);
        editor.putBoolean(MainActivity.IS_LOGIN_USER, true);
        editor.apply();
    }
    private void initializeAppFirstTimeUse(){

        /* INITIALIZE APP WHILE PROGRESS BAR DISPLAYS PROGRESS */
        initializeDBPoliticalParties();
        initializeEvents();
        initializeDBCandidates();
    }
    private void initializeEvents(){
        /* Access String resources to populate information about events */
        String eventOneName = getResources().getString(R.string.eventOneName);
        String eventOneDescription = getResources().getString(R.string.eventOneDescription);
        String eventOneDate = getResources().getString(R.string.eventOneDate);
        String eventOneTime = getResources().getString(R.string.eventOneTime);
        String eventOneChannel = getResources().getString(R.string.eventOneChannel);
        String eventOneModerator = getResources().getString(R.string.eventOneModerator);
        String eventOneSite = getResources().getString(R.string.eventOneSite);
        String eventOneLocation = getResources().getString(R.string.eventOneLocation);

        String eventTwoName = getResources().getString(R.string.eventTwoName);
        String eventTwoDescription = getResources().getString(R.string.eventTwoDescription);
        String eventTwoDate = getResources().getString(R.string.eventTwoDate);
        String eventTwoTime = getResources().getString(R.string.eventTwoTime);
        String eventTwoChannel = getResources().getString(R.string.eventTwoChannel);
        String eventTwoModerator = getResources().getString(R.string.eventTwoModerator);
        String eventTwoSite = getResources().getString(R.string.eventTwoSite);
        String eventTwoLocation = getResources().getString(R.string.eventTwoLocation);

        String eventThreeName = getResources().getString(R.string.eventThreeName);
        String eventThreeDescription = getResources().getString(R.string.eventThreeDescription);
        String eventThreeDate = getResources().getString(R.string.eventThreeDate);
        String eventThreeTime = getResources().getString(R.string.eventThreeTime);
        String eventThreeChannel = getResources().getString(R.string.eventThreeChannel);
        String eventThreeModerator = getResources().getString(R.string.eventThreeModerator);
        String eventThreeSite = getResources().getString(R.string.eventThreeSite);
        String eventThreeLocation = getResources().getString(R.string.eventThreeLocation);

        String eventFourName = getResources().getString(R.string.eventFourName);
        String eventFourDescription = getResources().getString(R.string.eventFourDescription);
        String eventFourDate = getResources().getString(R.string.eventFourDate);
        String eventFourTime = getResources().getString(R.string.eventFourTime);
        String eventFourChannel = getResources().getString(R.string.eventFourChannel);
        String eventFourModerator = getResources().getString(R.string.eventFourModerator);
        String eventFourSite = getResources().getString(R.string.eventFourSite);
        String eventFourLocation = getResources().getString(R.string.eventFourLocation);

        String eventFiveName = getResources().getString(R.string.eventFiveName);
        String eventFiveDescription = getResources().getString(R.string.eventFiveDescription);
        String eventFiveDate = getResources().getString(R.string.eventFiveDate);
        String eventFiveTime = getResources().getString(R.string.eventFiveTime);
        String eventFiveChannel = getResources().getString(R.string.eventFiveChannel);
        String eventFiveModerator = getResources().getString(R.string.eventFiveModerator);
        String eventFiveSite = getResources().getString(R.string.eventFiveSite);
        String eventFiveLocation = getResources().getString(R.string.eventFiveLocation);

        /* Generate byte arrays of images to store in database and retrieve as blobs */
        Bitmap bitmapCNN = BitmapFactory.decodeResource(getResources(), R.drawable.cnn_60);
        Bitmap bitmapNBC = BitmapFactory.decodeResource(getResources(), R.drawable.nbc);
        Bitmap bitmapCBS = BitmapFactory.decodeResource(getResources(), R.drawable.cbs);
        Bitmap bitmapFOX = BitmapFactory.decodeResource(getResources(), R.drawable.fox);
        Bitmap bitmapWhiteHouse = BitmapFactory.decodeResource(getResources(), R.drawable.whitehouse);

        Bitmap bitmapEventOneModerator = BitmapFactory.decodeResource(getResources(), R.drawable.lester);
        Bitmap bitmapEventTwoModerator = BitmapFactory.decodeResource(getResources(), R.drawable.elaine);
        Bitmap bitmapEventThreeModerator = BitmapFactory.decodeResource(getResources(), R.drawable.anderson);
        Bitmap bitmapEventFourModerator = BitmapFactory.decodeResource(getResources(), R.drawable.chris);
        Bitmap bitmapEventFiveModerator = BitmapFactory.decodeResource(getResources(), R.drawable.votingbooth);


        /* Initialize table to  hold political party objects */
        generateEvents(eventOneName, eventOneDescription, eventOneDate, eventOneTime, eventOneChannel, bitmapCNN, "bitmapcnn.jpg", eventOneModerator, bitmapEventOneModerator, "bitmapmoderator1.jpg", eventOneSite, eventOneLocation  );
        generateEvents(eventTwoName, eventTwoDescription, eventTwoDate, eventTwoTime, eventTwoChannel, bitmapNBC, "bitmapnbc.jpg", eventTwoModerator, bitmapEventTwoModerator, "bitmapmoderator2.jpg", eventTwoSite, eventTwoLocation  );
        generateEvents(eventThreeName, eventThreeDescription, eventThreeDate, eventThreeTime, eventThreeChannel, bitmapCBS, "bitmapcbs.jpg",eventThreeModerator, bitmapEventThreeModerator, "bitmapmoderator3.jpg", eventThreeSite, eventThreeLocation  );
        generateEvents(eventFourName, eventFourDescription, eventFourDate, eventFourTime, eventFourChannel, bitmapFOX, "bitmapfox.jpg",eventFourModerator, bitmapEventFourModerator, "bitmapmoderator4.jpg", eventFourSite, eventFourLocation  );
        generateEvents(eventFiveName, eventFiveDescription, eventFiveDate, eventFiveTime, eventFiveChannel, bitmapWhiteHouse,"bitmapwhitehouse.jpg", eventFiveModerator, bitmapEventFiveModerator, "bitmapmoderator5.jpg", eventFiveSite, eventFiveLocation  );
    }

    private void generateEvents(String eventName, String eventDescription, String date, String time, String channel, Bitmap channelPic, String channelPicName, String moderator, Bitmap moderatorPic, String moderatorPicName, String site, String location){
        currentEvent = new Event();

        currentEvent.setName(eventName);
        currentEvent.setDescription(eventDescription);
        currentEvent.setDate(date);
        currentEvent.setTime(time);
        currentEvent.setChannel(channel);
        try {
            currentEvent.setChannelPic(saveToInternalStorage(channelPic, channelPicName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            currentEvent.setModeratorPic(saveToInternalStorage(moderatorPic, moderatorPicName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        currentEvent.setModerator(moderator);
        currentEvent.setSite(site);
        currentEvent.setLocation(location);

        EventDataSource eds = new EventDataSource(LoginActivity.this);
        eds.open();
        boolean wasSuccessful = false;
        if(currentEvent.getEventID() == -1) {
            wasSuccessful = eds.insertEvent(currentEvent);
            int newId = eds.getLastEventId();
            currentEvent.setEventID(newId);
        }
        else{
            wasSuccessful = eds.updateEvent(currentEvent);
        }
        eds.close();
        if (wasSuccessful) {
            progressBarProgress++;
            progressBar.setProgress(progressBarProgress);
        }

    }
    private void initializeDBPoliticalParties(){
        /* Access String resources to populate information about political parties */
        String republicanPartyName = getResources().getString(R.string.republican_party_name);
        String republicanPartyDescription = getResources().getString(R.string.republican_party_description);
        String republicanPartyHouseSeatsString = getResources().getString(R.string.republican_party_number_of_house_seats);
        int republicanPartyHouseSeats = Integer.parseInt(republicanPartyHouseSeatsString);
        String republicanPartySenateSeatsString = getResources().getString(R.string.republican_party_number_of_senate_seats);
        int republicanPartySenateSeats = Integer.parseInt(republicanPartySenateSeatsString);
        String republicanPartySite = getResources().getString(R.string.republican_party_site);
        String republicanPartyEmail = getResources().getString(R.string.republican_party_email);
        String republicanPartyTwitter = getResources().getString(R.string.republican_party_twitter);

        String democraticPartyName = getResources().getString(R.string.democratic_party_name);
        String democraticPartyDescription = getResources().getString(R.string.democratic_party_description);
        String democraticPartyHouseSeatsString = getResources().getString(R.string.democratic_party_number_of_house_seats);
        int democraticPartyHouseSeats = Integer.parseInt(democraticPartyHouseSeatsString);
        String democraticPartySenateSeatsString = getResources().getString(R.string.democratic_party_number_of_senate_seats);
        int democraticPartySenateSeats = Integer.parseInt(democraticPartySenateSeatsString);
        String democraticPartySite = getResources().getString(R.string.democratic_party_site);
        String democraticPartyEmail = getResources().getString(R.string.democratic_party_email);
        String democraticPartyTwitter = getResources().getString(R.string.democratic_party_twitter);

        String libertarianPartyName = getResources().getString(R.string.libertarian_party_name);
        String libertarianPartyDescription = getResources().getString(R.string.libertarian_party_description);
        String libertarianPartyHouseSeatsString = getResources().getString(R.string.libertarian_party_number_of_house_seats);
        int libertarianPartyHouseSeats = Integer.parseInt(libertarianPartyHouseSeatsString);
        String libertarianPartySenateSeatsString = getResources().getString(R.string.libertarian_party_number_of_senate_seats);
        int libertarianPartySenateSeats = Integer.parseInt(libertarianPartySenateSeatsString);
        String libertarianPartySite = getResources().getString(R.string.libertarian_party_site);
        String libertarianPartyEmail = getResources().getString(R.string.libertarian_party_email);
        String libertarianPartyTwitter = getResources().getString(R.string.libertarian_party_twitter);

        String greenPartyName = getResources().getString(R.string.green_party_name);
        String greenPartyDescription = getResources().getString(R.string.green_party_description);
        String greenPartyHouseSeatsString = getResources().getString(R.string.green_party_number_of_house_seats);
        int greenPartyHouseSeats = Integer.parseInt(greenPartyHouseSeatsString);
        String greenPartySenateSeatsString = getResources().getString(R.string.green_party_number_of_senate_seats);
        int greenPartySenateSeats = Integer.parseInt(greenPartySenateSeatsString);
        String greenPartySite = getResources().getString(R.string.green_party_site);
        String greenPartyEmail = getResources().getString(R.string.green_party_email);
        String greenPartyTwitter = getResources().getString(R.string.green_party_twitter);

        /* Create byte array for square icon and wide picture for each party*/
        Bitmap bitmapSquareRepublicanParty = BitmapFactory.decodeResource(getResources(), R.drawable.republican_party_square);
        Bitmap bitmapSquareDemocraticParty = BitmapFactory.decodeResource(getResources(), R.drawable.democratic_party_sqaure);
        Bitmap bitmapSquareLibertarianParty = BitmapFactory.decodeResource(getResources(), R.drawable.libertarian_party_sqaure);
        Bitmap bitmapSquareGreenParty = BitmapFactory.decodeResource(getResources(), R.drawable.green_party_square);

        Bitmap bitmapWideRepublicanParty = BitmapFactory.decodeResource(getResources(), R.drawable.republican_party_wide);
        Bitmap bitmapWideDemocraticParty = BitmapFactory.decodeResource(getResources(), R.drawable.democratic_party_wide);
        Bitmap bitmapWideLibertarianParty = BitmapFactory.decodeResource(getResources(), R.drawable.libertarian_party_wide);
        Bitmap bitmapWideGreenParty = BitmapFactory.decodeResource(getResources(), R.drawable.green_party_wide);

        /* Initialize table to  hold political party objects */
        generatePoliticalParties(republicanPartyName, republicanPartyDescription, bitmapSquareRepublicanParty, "republican.jpg", bitmapWideRepublicanParty, "republicanwide.jpg",republicanPartyHouseSeats, republicanPartySenateSeats, republicanPartySite, republicanPartyEmail, republicanPartyTwitter);
        generatePoliticalParties(democraticPartyName, democraticPartyDescription, bitmapSquareDemocraticParty, "democrat.jpg", bitmapWideDemocraticParty,"democratwide.jpg", democraticPartyHouseSeats, democraticPartySenateSeats, democraticPartySite, democraticPartyEmail, democraticPartyTwitter);
        generatePoliticalParties(libertarianPartyName, libertarianPartyDescription, bitmapSquareLibertarianParty, "libertarian.jpg", bitmapWideLibertarianParty, "libertarianwide.jpg", libertarianPartyHouseSeats, libertarianPartySenateSeats, libertarianPartySite, libertarianPartyEmail, libertarianPartyTwitter);
        generatePoliticalParties(greenPartyName, greenPartyDescription, bitmapSquareGreenParty, "green.jpg", bitmapWideGreenParty,"greenwide.jpg", greenPartyHouseSeats, greenPartySenateSeats, greenPartySite,greenPartyEmail, greenPartyTwitter);

    }
    private void generatePoliticalParties(String partyName, String partyDescription, Bitmap bitmapSquare, String bitmapSquareName, Bitmap bitmapWide, String bitmapWideName, int numberOfHouseSeats, int numberOfSenateSeats, String site, String email, String twitter){
        currentPoliticalParty = new PoliticalParty();
        currentPoliticalParty.setName(partyName);
        currentPoliticalParty.setDescription(partyDescription);
        try {
            currentPoliticalParty.setSquarePicture(saveToInternalStorage(bitmapSquare, bitmapSquareName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            currentPoliticalParty.setWidePicture(saveToInternalStorage(bitmapWide, bitmapWideName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        currentPoliticalParty.setNumberOfHouseSeats(numberOfHouseSeats);
        currentPoliticalParty.setNumberOfSenateSeats(numberOfSenateSeats);
        currentPoliticalParty.setSite(site);
        currentPoliticalParty.setEmail(email);
        currentPoliticalParty.setTwitter(twitter);

        PoliticalPartyDataSource ppds = new PoliticalPartyDataSource(LoginActivity.this);
        ppds.open();
        boolean wasSuccessful = false;
        if(currentPoliticalParty.getPoliticalPartyID() == -1) {
            wasSuccessful = ppds.insertPoliticalParty(currentPoliticalParty);
            int newId = ppds.getLastPoliticalPartyID();
            currentPoliticalParty.setPoliticalPartyID(newId);
        }
        else{
            wasSuccessful = ppds.updatePoliticalParty(currentPoliticalParty);
        }
        ppds.close();
        if (wasSuccessful) {
            progressBarProgress++;
            progressBar.setProgress(progressBarProgress);
        }
    }
    private void initializeDBCandidates() {
        Bitmap bitmapSquareBernie = BitmapFactory.decodeResource(getResources(), R.drawable.bernie_square);
        Bitmap bitmapSquareHilary = BitmapFactory.decodeResource(getResources(), R.drawable.hilary_square);
        Bitmap bitmapSquareTrump = BitmapFactory.decodeResource(getResources(), R.drawable.trump_square);
        Bitmap bitmapSquareCruz = BitmapFactory.decodeResource(getResources(), R.drawable.cruz_square);
        Bitmap bitmapSquareKasich = BitmapFactory.decodeResource(getResources(), R.drawable.kasich_square);
        Bitmap bitmapSquareGary = BitmapFactory.decodeResource(getResources(), R.drawable.gary_square);

        Bitmap bitmapWideBernie = BitmapFactory.decodeResource(getResources(), R.drawable.bernie);
        Bitmap bitmapWideHilary = BitmapFactory.decodeResource(getResources(), R.drawable.hilary);
        Bitmap bitmapWideTrump = BitmapFactory.decodeResource(getResources(), R.drawable.trump);
        Bitmap bitmapWideCruz = BitmapFactory.decodeResource(getResources(), R.drawable.cruz);
        Bitmap bitmapWideKasich = BitmapFactory.decodeResource(getResources(), R.drawable.kasich);
        Bitmap bitmapWideGary = BitmapFactory.decodeResource(getResources(), R.drawable.garyjohnson);

        /* Initialize table to  hold candidate objects */
        generateCandidates("Bernie Sanders", "Self-proclaimed democratic socialist from Vermont.", bitmapSquareBernie, "berniesquare.jpg", bitmapWideBernie,"berniewide.jpg", "DNC", votesForBernie, 1941, "normal");
        generateCandidates("Hillary Clinton", "Former Secretary of State, many years in politics.", bitmapSquareHilary, "hillarysquare.jpg", bitmapWideHilary, "hillarywide.jpg","DNC", votesForHilary, 1191, "general");
        generateCandidates("Donald Trump", "Successful Business man from New York City.", bitmapSquareTrump, "trumpsquare.jpg", bitmapWideTrump, "trumpwide.jpg","GOP", votesForTrump, 845, "general");
        generateCandidates("Ted Cruz", "Reported Zodiac killer, wants to turn abolish secularism.", bitmapSquareCruz, "cruzsquare.jpg", bitmapWideCruz,"cruzwide.jpg", "GOP", votesForCruz, 559, "normal" );
        generateCandidates("John Kasich", "What am I still doing in this race anyways...?", bitmapSquareKasich, "kasichsquare.jpg", bitmapWideKasich,"kasichwide.jpg", "GOP", votesForKasich, 148, "normal");
        generateCandidates("Gary Johnson", "Popular candidate from the Libertarian Party.", bitmapSquareGary, "garysquare.jpg", bitmapWideGary,"garywide.jpg", "Libertarian", votesForGary, 200, "general");
    }
    public void generateCandidates(String candidateName, String candidateDescription, Bitmap bitmapSquare, String bitmapSquareName, Bitmap bitmapWide, String bitmapWideName, String party, int numberOfVotes, int delegateCount, String electionType){
        currentCandidate = new Candidate();
        currentCandidate.setCandidateName(candidateName);
        currentCandidate.setCandidateDescription(candidateDescription);
        try {
            currentCandidate.setSquarePicture(saveToInternalStorage(bitmapSquare, bitmapSquareName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            currentCandidate.setWidePicture(saveToInternalStorage(bitmapWide, bitmapWideName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        currentCandidate.setParty(party);
        currentCandidate.setNumberOfVotes(numberOfVotes);
        currentCandidate.setDelegateCount(delegateCount);
        currentCandidate.setElectionType(electionType);
        if(isInGeneralElection(currentCandidate.getCandidateName())){
        }
        else {
        }
        CandidateDataSource ds = new CandidateDataSource(LoginActivity.this);
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
            progressBarProgress++;
            progressBar.setProgress(progressBarProgress);
            numberOfCandidatesGenerated++;
            if(numberOfCandidatesGenerated >= numberOfCandidatesToGenerate){
                /* init methods which are dependent on the database of candidate */
                initializeDBUsers();
                initializeExitPolls();
                loadAPIs();
            }
        }
    }
    /* Save bitmap to internal storage and return Strin of the image's path.
            The path will be stored in the SQL database for each image, rather than the image themselves (too large for SQL)
     */
    private String saveToInternalStorage(Bitmap bitmapImage, String fileName) throws IOException {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,fileName);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            fos.close();
        }
        StringBuilder s = new StringBuilder();
        s.append(directory.getAbsolutePath());
        s.append("/");
        s.append(fileName);
        return s.toString();
    }


    private boolean isInGeneralElection(String candidateName){
        switch(candidateName){
            case "Hillary Clinton":
                return true;
            case "Donald Trump":
                return true;
            case "Gary Johnson":
                return true;
            default:
                return false;
        }
    }

    private void initializeDBUsers(){
        /* Initialize the user table with 30 fake user accounts, to demonstrate the graphs and demographics potential */
        generateUsers("Bradley Cooper", "bcooper436", "zun3ukit", getRandomParty(), getRandomAge(), getRandomGender(), getRandomDemocraticCandidate(), getRandomRepublicanCandidate());
        generateUsers("Linden Marshall", "welcomehome8", "zun3ukit",  getRandomParty(), getRandomAge(), getRandomGender(), getRandomDemocraticCandidate(), getRandomRepublicanCandidate());
        generateUsers("Rhonda Bowley", "duneiversity", "zun3ukit",  getRandomParty(), getRandomAge(), getRandomGender(), getRandomDemocraticCandidate(), getRandomRepublicanCandidate());
        generateUsers("Rubisha Louqie", "SparklesGems80", "zun3ukit",  getRandomParty(), getRandomAge(), getRandomGender(), getRandomDemocraticCandidate(), getRandomRepublicanCandidate());
        generateUsers("Mark Simmons", "marksimms", "zun3ukit",  getRandomParty(), getRandomAge(), getRandomGender(), getRandomDemocraticCandidate(), getRandomRepublicanCandidate());

        generateUsers("Willis Lesia", "williscool40", "zun3ukit",  getRandomParty(), getRandomAge(), getRandomGender(), getRandomDemocraticCandidate(), getRandomRepublicanCandidate());
        generateUsers("Johnie Kiersten", "JoHnIeK", "zun3ukit",  getRandomParty(), getRandomAge(), getRandomGender(), getRandomDemocraticCandidate(), getRandomRepublicanCandidate());
        generateUsers("Biance Glade", "bglade11", "zun3ukit",  getRandomParty(), getRandomAge(), getRandomGender(), getRandomDemocraticCandidate(), getRandomRepublicanCandidate());
        generateUsers("Melva Denis", "Mel0192", "zun3ukit",  getRandomParty(), getRandomAge(), getRandomGender(), getRandomDemocraticCandidate(), getRandomRepublicanCandidate());
        generateUsers("Trina Bryan", "BryTri92", "zun3ukit", getRandomParty(), getRandomAge(), getRandomGender(), getRandomDemocraticCandidate(), getRandomRepublicanCandidate());
        generateUsers("Oswald Webster", "theBookWasBetter", "zun3ukit", getRandomParty(), getRandomAge(), getRandomGender(), getRandomDemocraticCandidate(), getRandomRepublicanCandidate());

        generateUsers("Vinny Archie", "va86", "zun3ukit", getRandomParty(), getRandomAge(), getRandomGender(), getRandomDemocraticCandidate(), getRandomRepublicanCandidate());
        generateUsers("Suzanne Hazel", "zanny101", "zun3ukit", getRandomParty(), getRandomAge(), getRandomGender(), getRandomDemocraticCandidate(), getRandomRepublicanCandidate());
        generateUsers("Darian Reid", "reidsbooks", "zun3ukit", getRandomParty(), getRandomAge(), getRandomGender(), getRandomDemocraticCandidate(), getRandomRepublicanCandidate());
        generateUsers("Madyson Femie", "maddawg", "zun3ukit", getRandomParty(), getRandomAge(), getRandomGender(), getRandomDemocraticCandidate(), getRandomRepublicanCandidate());
        generateUsers("Dolly Valerie", "dval", "zun3ukit", getRandomParty(), getRandomAge(), getRandomGender(), getRandomDemocraticCandidate(), getRandomRepublicanCandidate());
        generateUsers("Braelyn Shantelle", "shantytown", "zun3ukit", getRandomParty(), getRandomAge(), getRandomGender(), getRandomDemocraticCandidate(), getRandomRepublicanCandidate());

        generateUsers("Lysette Paula", "LysolKills", "zun3ukit", getRandomParty(), getRandomAge(), getRandomGender(), getRandomDemocraticCandidate(), getRandomRepublicanCandidate());
        generateUsers("Theresa Audley", "audibles", "zun3ukit", getRandomParty(), getRandomAge(), getRandomGender(), getRandomDemocraticCandidate(), getRandomRepublicanCandidate());
        generateUsers("Conner Makayla", "conword", "zun3ukit", getRandomParty(), getRandomAge(), getRandomGender(), getRandomDemocraticCandidate(), getRandomRepublicanCandidate());
        generateUsers("Devereux Kierra", "tryAndPronounceIt", "zun3ukit", getRandomParty(), getRandomAge(), getRandomGender(), getRandomDemocraticCandidate(), getRandomRepublicanCandidate());
        generateUsers("Todd Chrissy", "oddManToddMan", "zun3ukit", getRandomParty(), getRandomAge(), getRandomGender(), getRandomDemocraticCandidate(), getRandomRepublicanCandidate());
        generateUsers("Alise Jaye", "BlessAllPeople", "zun3ukit", getRandomParty(), getRandomAge(), getRandomGender(), getRandomDemocraticCandidate(), getRandomRepublicanCandidate());
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
        user.setChosenDemocrat(chosenDemocrat);
        user.setChosenRepublican(chosenRepublican);
        UserDataSource us = new UserDataSource(LoginActivity.this);
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


        UserDataSource userDataSource = new UserDataSource(LoginActivity.this);
        userDataSource.open();
        user = userDataSource.getSpecificUserFromLoginInfo(user.getUserName(),"zun3ukit");
        user.setChosenDemocrat(chosenDemocrat);
        user.setChosenRepublican(chosenRepublican);
        userDataSource.updateUser(user);
        userDataSource.close();

        if (wasSuccessful) {
            progressBarProgress++;
            progressBar.setProgress(progressBarProgress);
        }
    }

    public byte[] convertBitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream(bitmap.getWidth() * bitmap.getHeight());
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, buffer);
        return buffer.toByteArray();
    }
    public String getRandomGender(){
        final int min = 0;
        final int max = 99;
        Random r = new Random();
        final int random = r.nextInt((max - min) + 1) + min;

        if(random <= 50) {
            return "Male";
        }
        else {
            return "Female";
        }
    }
    public int getRandomAge(){
        final int min = 18;
        final int max = 65;
        Random r = new Random();
        final int random = r.nextInt((max - min) + 1) + min;

        return random;
    }
    public String getRandomParty(){
        final int min = 0;
        final int max = 4;
        Random r = new Random();
        final int random = r.nextInt((max - min) + 1) + min;

        if(random <= 1) {
            return "Democrat";
        }
        else if(random <= 3) {
            return "Republican";
        }
        else {
            return "Independent";
        }
    }
    public String getRandomDemocraticCandidate(){
        final int min = 0;
        final int max = 6;
        Random r = new Random();
        final int random = r.nextInt((max - min) + 1) + min;

        if(random <= 3){
            votesForHilary++;
            return "Hillary Clinton";
        }
        else {
            votesForBernie++;
            return "Bernie Sanders";
        }
    }
    public String getRandomRepublicanCandidate(){
        final int min = 0;
        final int max = 6;
        Random r = new Random();
        final int random = r.nextInt((max - min) + 1) + min;

        if(random <= 3){
            votesForTrump++;
            return "Donald Trump";
        }
        else if (random <= 5){
            votesForCruz++;
            return "Ted Cruz";
        }
        else {
            votesForKasich++;
            return "John Kasich";
        }
    }
    private void initializeExitPolls(){
        try {


            XmlPullParser parser = getResources().getXml(R.xml.combined_primary_election);


            parseXML(parser);

        } catch (XmlPullParserException e) {

            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    private void parseXML(XmlPullParser parser) throws XmlPullParserException,IOException{

        ArrayList<ExitPoll> exitPolls = null;
        int eventType = parser.getEventType();
        ExitPoll currentExitPoll = null;
        String name;
        String currentName = "debugging";
        while(eventType != XmlPullParser.END_DOCUMENT){

            name = parser.getName();

            switch (eventType){
                case XmlPullParser.START_DOCUMENT:
                    exitPolls = new ArrayList<ExitPoll>();
                    break;

                case XmlPullParser.START_TAG:
                    currentName = name;
                    if("record".equalsIgnoreCase(currentName)){
                        currentExitPoll = new ExitPoll();
                    }
                    break;

                case XmlPullParser.TEXT:
                    if(currentExitPoll != null) {
                        if("state".equalsIgnoreCase(currentName)){
                            currentExitPoll.setState(parser.getText());
                        }
                        else if("total_responders".equalsIgnoreCase(currentName)){
                            currentExitPoll.setTotalResponders(Integer.parseInt(parser.getText()));
                        }
                        else if("candidate".equalsIgnoreCase(currentName)){
                            currentExitPoll.setCandidate(parser.getText());
                        }
                        else if("voter_group".equalsIgnoreCase(currentName)){
                            currentExitPoll.setVoterGroup(parser.getText());
                        }
                        else if("percentage_of_vote".equalsIgnoreCase(currentName)){
                            currentExitPoll.setPercentageOfVote(Integer.parseInt(parser.getText()));
                        }
                        else if("voter_group_label".equalsIgnoreCase(currentName)){
                            currentExitPoll.setVoterGroupLabel(parser.getText());
                        }
                    }
                    break;

                case XmlPullParser.END_TAG:

                    name = parser.getName();

                    if("record".equalsIgnoreCase(name) && currentExitPoll != null){
                        exitPolls.add(currentExitPoll);
                    }
                    break;
            }
            eventType = parser.next();
        }
        saveExitPolls(exitPolls);
    }
    private void saveExitPolls(ArrayList<ExitPoll> exitPolls){

        ExitPoll currentExitPoll;
        ExitPollDataSource ds = new ExitPollDataSource(LoginActivity.this);
        ds.open();

        for(int i = 0; i < exitPolls.size(); i++){
            progressBarProgress++;                             //Set progress bar++ for each exit poll processed
            progressBar.setProgress(progressBarProgress);
            currentExitPoll = exitPolls.get(i);
            if(currentExitPoll.getExitPollID() == -1){
                ds.insertExitPoll(currentExitPoll);
                int newId = ds.getLastExitPollId();
                currentExitPoll.setExitPollID(newId);
            }
            else {
                ds.updateExitPoll(currentExitPoll);
            }
        }

        ds.close();
        setTypicalVoterFields("Bernie Sanders");
        setTypicalVoterFields("Hillary Clinton");
        setTypicalVoterFields("Donald Trump");
        setTypicalVoterFields("John Kasich");
        setTypicalVoterFields("Ted Cruz");
    }

    public void setTypicalVoterFields(String candidateName){
        Candidate currentCandidate;
        CandidateDataSource dsc = new CandidateDataSource(LoginActivity.this);

        dsc.open();
        currentCandidate = dsc.getCandidateByName(candidateName);

        ExitPollDataSource eds = new ExitPollDataSource(LoginActivity.this);
        eds.open();
        currentCandidate.setTypeOfVoter(eds.getMostCommonTypeOfVoterByCandidate(currentCandidate.getCandidateName()));
        eds.close();
        dsc.updateCandidate(currentCandidate);
        dsc.close();
    }
    private void loadAPIs(){
        if(!API_2016_NATIONAL_GOP_PRIMARY_DONE){
            currentAPI = API_2016_NATIONAL_GOP_PRIMARY;
            currentAPILabel = "API_2016_NATIONAL_GOP_PRIMARY";
            new RetrieveFeedTask().execute();
        }
        else if(!API_2016_NATIONAL_DEMOCRATIC_PRIMARY_DONE) {
            currentAPI = API_2016_NATIONAL_DEMOCRATIC_PRIMARY;
            currentAPILabel = "API_2016_NATIONAL_DEMOCRATIC_PRIMARY";
            new RetrieveFeedTask().execute();
        } /*
        else if(!API_HILLARY_CLINTON_FAVORABLE_RATING_DONE) {
            currentAPI = API_HILLARY_CLINTON_FAVORABLE_RATING;
            currentAPILabel = "API_HILLARY_CLINTON_FAVORABLE_RATING";
            new RetrieveFeedTask().execute();
        }
        else if(!API_BERNIE_SANDERS_FAVORABLE_RATING_DONE) {
            currentAPI = API_BERNIE_SANDERS_FAVORABLE_RATING;
            currentAPILabel = "API_BERNIE_SANDERS_FAVORABLE_RATING";
            new RetrieveFeedTask().execute();
        }
        else if(!API_DONALD_TRUMP_FAVORABLE_RATING_DONE) {
            currentAPI = API_DONALD_TRUMP_FAVORABLE_RATING;
            currentAPILabel = "API_DONALD_TRUMP_FAVORABLE_RATING";
            new RetrieveFeedTask().execute();
        }
        else if(!API_TED_CRUZ_FAVORABLE_RATING_DONE) {
            currentAPI = API_TED_CRUZ_FAVORABLE_RATING;
            currentAPILabel = "API_TED_CRUZ_FAVORABLE_RATING";
            new RetrieveFeedTask().execute();
        }
        else if(!API_JOHN_KASICH_FAVORABLE_RATING_DONE) {
            currentAPI = API_JOHN_KASICH_FAVORABLE_RATING;
            currentAPILabel = "API_JOHN_KASICH_FAVORABLE_RATING";
            new RetrieveFeedTask().execute();
        } */
        else if(!API_2016_GENERAL_ELECTION_DONE) {
            currentAPI = API_2016_GENERAL_ELECTION;
            currentAPILabel = "API_2016_GENERAL_ELECTION";
            new RetrieveFeedTask().execute();
        }
        /*else if(!API_2016_NATIONAL_HOUSE_RACE_DONE){
            currentAPI = API_2016_NATIONAL_HOUSE_RACE;
            currentAPILabel = "API_2016_NATIONAL_HOUSE_RACE";
            new RetrieveFeedTask().execute();
        }*/
        /*
        else if(!API_2016_HOUSE_RACE_DONE) {
            currentAPI = API_2016_HOUSE_RACE;
            currentAPILabel = "API_2016_HOUSE_RACE";
            new RetrieveFeedTask().execute();
        }*/
        else {
            //progressBar.setVisibility(View.INVISIBLE);
            //textViewLoading.setVisibility(View.INVISIBLE);
            //textView.setVisibility(View.VISIBLE);
            //textViewPollTitle.setVisibility(View.VISIBLE);
        }
    }

    class RetrieveFeedTask extends AsyncTask<Void, Void, String> {
        protected void onPreExecute() {
        }
        protected String doInBackground(Void... urls) {
            try {
                URL url = new URL(currentAPI);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                } finally {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        protected void onPostExecute(String response) {
            if (response == null) {
                response = "THERE WAS AN ERROR";
            }
            Log.i("INFO", response);

            JSONObject object;
            String  pollValues, lastUpdated;
            JSONArray jr;

            try {
                switch(currentAPILabel){
                    case "API_2016_NATIONAL_GOP_PRIMARY":
                        arrayOfVoterPercentages = new String[5][2];
                        API_2016_NATIONAL_GOP_PRIMARY_DONE = true;

                        object = (JSONObject) new JSONTokener(response).nextValue();
                        pollValues = object.getString("estimates");
                        lastUpdated = object.getString("last_updated");

                        jr = new JSONArray(pollValues);
                        Log.d("Length of jr = ",Integer.toString(jr.length()));
                        for(int i = 0; i<5; i++ ){
                            JSONObject currentCandidate = jr.getJSONObject(i);

                            arrayOfVoterPercentages[i][0] = currentCandidate.getString("first_name") + " " + currentCandidate.getString("last_name");
                            arrayOfVoterPercentages[i][1] = currentCandidate.getString("value");
                        }
                        for(int j = 0; j < arrayOfVoterPercentages.length; j++) {
                            if (!arrayOfVoterPercentages[j][0].equalsIgnoreCase("null null")) {
                                ds = new CandidateDataSource(LoginActivity.this);
                                ds.open();
                                candidate = ds.getCandidateByName(arrayOfVoterPercentages[j][0]);
                                candidate.setHuffPercentageOfVote(Float.parseFloat(arrayOfVoterPercentages[j][1]));
                                ds.updateCandidate(candidate);
                                ds.close();
                            }
                        }
                        updateSharedPreferences("API_2016_NATIONAL_GOP_PRIMARY", lastUpdated);
                        break;

                    case "API_2016_NATIONAL_DEMOCRATIC_PRIMARY":
                        int NUMBER_OF_DEMOCRATIC_CANDIDATES_TO_CONSIDER = 2;

                        arrayOfVoterPercentages = new String[ NUMBER_OF_DEMOCRATIC_CANDIDATES_TO_CONSIDER][2];
                        API_2016_NATIONAL_DEMOCRATIC_PRIMARY_DONE = true;

                        object = (JSONObject) new JSONTokener(response).nextValue();
                        pollValues = object.getString("estimates");
                        lastUpdated = object.getString("last_updated");

                        jr = new JSONArray(pollValues);
                        for(int i = 0; i < NUMBER_OF_DEMOCRATIC_CANDIDATES_TO_CONSIDER; i++ ){
                            JSONObject currentCandidate = jr.getJSONObject(i);
                            arrayOfVoterPercentages[i][0] = currentCandidate.getString("first_name") + " " + currentCandidate.getString("last_name");
                            arrayOfVoterPercentages[i][1] = currentCandidate.getString("value");
                        }
                        for(int j = 0; j < arrayOfVoterPercentages.length; j++) {
                            if (!arrayOfVoterPercentages[j][0].equalsIgnoreCase("null null")) {
                                CandidateDataSource ds = new CandidateDataSource(LoginActivity.this);
                                ds.open();
                                candidate = ds.getCandidateByName(arrayOfVoterPercentages[j][0]);
                                candidate.setHuffPercentageOfVote(Float.parseFloat(arrayOfVoterPercentages[j][1]));
                                ds.updateCandidate(candidate);
                                ds.close();
                            }
                        }
                        updateSharedPreferences("API_2016_NATIONAL_DEMOCRATIC_PRIMARY", lastUpdated);
                        break;

                    case "API_2016_GENERAL_ELECTION":
                        int NUMBER_OF_GENERAL_ELECTION_CANDIDATES_TO_CONSIDER = 3;

                        arrayOfVoterPercentages = new String[NUMBER_OF_GENERAL_ELECTION_CANDIDATES_TO_CONSIDER][2];
                        API_2016_GENERAL_ELECTION_DONE = true;

                        object = (JSONObject) new JSONTokener(response).nextValue();
                        pollValues = object.getString("estimates");
                        lastUpdated = object.getString("last_updated");

                        jr = new JSONArray(pollValues);
                        for(int i = 0; i < NUMBER_OF_GENERAL_ELECTION_CANDIDATES_TO_CONSIDER; i++ ){
                            JSONObject currentCandidate = jr.getJSONObject(i);
                            arrayOfVoterPercentages[i][0] = currentCandidate.getString("first_name") + " " + currentCandidate.getString("last_name");
                            arrayOfVoterPercentages[i][1] = currentCandidate.getString("value");
                        }
                        for(int j = 0; j < arrayOfVoterPercentages.length; j++) {
                            if (!arrayOfVoterPercentages[j][0].equalsIgnoreCase("null null")) {
                                CandidateDataSource ds1 = new CandidateDataSource(LoginActivity.this);
                                ds1.open();
                                candidate = ds1.getCandidateByName(arrayOfVoterPercentages[j][0]);
                                Float f = Float.parseFloat(arrayOfVoterPercentages[j][1]);
                                candidate.setHuffPercentageOfVoteGeneral(f);
                                ds1.updateCandidate(candidate);
                                ds1.close();
                            }
                        }
                        updateSharedPreferences("API_2016_GENERAL_ELECTION", lastUpdated);
                        break;
                    /*
                    case "API_2016_NATIONAL_HOUSE_RACE":

                        arrayOfVoterPercentages = new String[2][2];
                        API_2016_NATIONAL_DEMOCRATIC_PRIMARY_DONE = true;

                        object = (JSONObject) new JSONTokener(response).nextValue();
                        pollTitle = object.getString("title");
                        pollValues = object.getString("estimates");
                        lastUpdated = object.getString("last_updated");

                        jr = new JSONArray(pollValues);
                        for(int i = 0; i < arrayOfVoterPercentages.length; i++ ){
                            JSONObject currentParty = jr.getJSONObject(i);

                            arrayOfVoterPercentages[i][0] = currentParty.getString("choice");
                            arrayOfVoterPercentages[i][1] = currentParty.getString("value");
                        }
                        for(int j = 0; j < arrayOfVoterPercentages.length; j++) {
                            if (!arrayOfVoterPercentages[j][0].equalsIgnoreCase("null null")) {
                                PoliticalPartyDataSource pds = new PoliticalPartyDataSource(LoginActivity.this);
                                pds.open();
                                PoliticalParty politicalParty = pds.getOPoliticalPartyByName(arrayOfVoterPercentages[j][0]);
                                Float f = Float.parseFloat(arrayOfVoterPercentages[j][1]);
                                politicalParty.setPercentageOfHouseVote(f);
                                pds.updatePoliticalParty(politicalParty);
                                pds.close();
                            }
                        }
                        updateSharedPreferences("API_2016_NATIONAL_HOUSE_RACE", lastUpdated);
                        break;

                    case "API_HILLARY_CLINTON_FAVORABLE_RATING":
                        API_HILLARY_CLINTON_FAVORABLE_RATING_DONE = true;

                        arrayFavorable = new String[3][2];

                        object = (JSONObject) new JSONTokener(response).nextValue();
                        pollTitle = object.getString("title");
                        pollValues = object.getString("estimates");
                        lastUpdated = object.getString("last_updated");

                        ds = new CandidateDataSource(LoginActivity.this);
                        ds.open();
                        candidateToUpdate = ds.getCandidateByName("Hillary Clinton");

                        jr = new JSONArray(pollValues);
                        for(int i = 0; i<jr.length(); i++ ){
                            JSONObject currentCandidate = jr.getJSONObject(i);
                            arrayFavorable[i][0] = currentCandidate.getString("choice");
                            arrayFavorable[i][1] = currentCandidate.getString("value");
                        }
                        candidateToUpdate.setHuffFavorableRating(Float.parseFloat(arrayFavorable[0][1]));
                        candidateToUpdate.setHuffUnfavorableRating(Float.parseFloat(arrayFavorable[1][1]));
                        ds.updateCandidate(candidateToUpdate);
                        ds.close();
                        updateSharedPreferences("API_HILLARY_CLINTON_FAVORABLE_RATING", lastUpdated);
                        break;

                    case "API_BERNIE_SANDERS_FAVORABLE_RATING":
                        API_BERNIE_SANDERS_FAVORABLE_RATING_DONE = true;

                        arrayFavorable = new String[3][2];

                        object = (JSONObject) new JSONTokener(response).nextValue();
                        pollTitle = object.getString("title");
                        pollValues = object.getString("estimates");
                        lastUpdated = object.getString("last_updated");

                        ds = new CandidateDataSource(LoginActivity.this);
                        ds.open();
                        candidateToUpdate = ds.getCandidateByName("Bernie Sanders");

                        jr = new JSONArray(pollValues);
                        for(int i = 0; i<jr.length(); i++ ){
                            JSONObject currentCandidate = jr.getJSONObject(i);
                            arrayFavorable[i][0] = currentCandidate.getString("choice");
                            arrayFavorable[i][1] = currentCandidate.getString("value");
                        }
                        candidateToUpdate.setHuffFavorableRating(Float.parseFloat(arrayFavorable[0][1]));
                        candidateToUpdate.setHuffUnfavorableRating(Float.parseFloat(arrayFavorable[1][1]));
                        ds.updateCandidate(candidateToUpdate);
                        ds.close();
                        updateSharedPreferences("API_BERNIE_SANDERS_FAVORABLE_RATING", lastUpdated);
                        break;

                    case "API_DONALD_TRUMP_FAVORABLE_RATING":
                        API_DONALD_TRUMP_FAVORABLE_RATING_DONE = true;

                        arrayFavorable = new String[3][2];

                        object = (JSONObject) new JSONTokener(response).nextValue();
                        pollTitle = object.getString("title");
                        pollValues = object.getString("estimates");
                        lastUpdated = object.getString("last_updated");

                        ds = new CandidateDataSource(LoginActivity.this);
                        ds.open();
                        candidateToUpdate = ds.getCandidateByName("Donald Trump");

                        jr = new JSONArray(pollValues);
                        for(int i = 0; i<jr.length(); i++ ){
                            JSONObject currentCandidate = jr.getJSONObject(i);
                            arrayFavorable[i][0] = currentCandidate.getString("choice");
                            arrayFavorable[i][1] = currentCandidate.getString("value");
                        }
                        candidateToUpdate.setHuffFavorableRating(Float.parseFloat(arrayFavorable[0][1]));
                        candidateToUpdate.setHuffUnfavorableRating(Float.parseFloat(arrayFavorable[1][1]));
                        ds.updateCandidate(candidateToUpdate);
                        ds.close();
                        updateSharedPreferences("API_DONALD_TRUMP_FAVORABLE_RATING", lastUpdated);
                        break;

                    case "API_TED_CRUZ_FAVORABLE_RATING":
                        API_TED_CRUZ_FAVORABLE_RATING_DONE = true;

                        arrayFavorable = new String[3][2];

                        object = (JSONObject) new JSONTokener(response).nextValue();
                        pollTitle = object.getString("title");
                        pollValues = object.getString("estimates");
                        lastUpdated = object.getString("last_updated");

                        ds = new CandidateDataSource(LoginActivity.this);
                        ds.open();
                        candidateToUpdate = ds.getCandidateByName("Ted Cruz");

                        jr = new JSONArray(pollValues);
                        for(int i = 0; i<jr.length(); i++ ){
                            JSONObject currentCandidate = jr.getJSONObject(i);
                            arrayFavorable[i][0] = currentCandidate.getString("choice");
                            arrayFavorable[i][1] = currentCandidate.getString("value");
                        }
                        candidateToUpdate.setHuffFavorableRating(Float.parseFloat(arrayFavorable[0][1]));
                        candidateToUpdate.setHuffUnfavorableRating(Float.parseFloat(arrayFavorable[1][1]));
                        ds.updateCandidate(candidateToUpdate);
                        ds.close();
                        updateSharedPreferences("API_TED_CRUZ_FAVORABLE_RATING", lastUpdated);
                        break;

                    case "API_JOHN_KASICH_FAVORABLE_RATING":
                        API_JOHN_KASICH_FAVORABLE_RATING_DONE = true;

                        arrayFavorable = new String[3][2];

                        object = (JSONObject) new JSONTokener(response).nextValue();
                        pollTitle = object.getString("title");
                        pollValues = object.getString("estimates");
                        lastUpdated = object.getString("last_updated");

                        ds = new CandidateDataSource(LoginActivity.this);
                        ds.open();
                        candidateToUpdate = ds.getCandidateByName("John Kasich");

                        jr = new JSONArray(pollValues);
                        for(int i = 0; i<jr.length(); i++ ){
                            JSONObject currentCandidate = jr.getJSONObject(i);
                            arrayFavorable[i][0] = currentCandidate.getString("choice");
                            arrayFavorable[i][1] = currentCandidate.getString("value");
                        }
                        candidateToUpdate.setHuffFavorableRating(Float.parseFloat(arrayFavorable[0][1]));
                        candidateToUpdate.setHuffUnfavorableRating(Float.parseFloat(arrayFavorable[1][1]));
                        ds.updateCandidate(candidateToUpdate);
                        ds.close();
                        updateSharedPreferences("API_JOHN_KASICH_FAVORABLE_RATING", lastUpdated);
                        break; */
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
            progressBarProgress++;
            progressBar.setProgress(progressBarProgress);

            if(progressBarProgress >= NUMBER_OF_DATABASES_TO_POPULATE) {
                displayWelcomeText();
                progressBar.hide();

                final Animation animationFadeIn = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.fade_in);
                TextView textViewWelcome1 = (TextView)findViewById(R.id.textViewWelcome1);
                TextView textViewWelcome2 = (TextView)findViewById(R.id.textViewWelcome2);
                textViewWelcome1.setVisibility(View.VISIBLE);
                textViewWelcome2.setVisibility(View.VISIBLE);
                textViewWelcome1.startAnimation(animationFadeIn);
                textViewWelcome2.startAnimation(animationFadeIn);
            }
            else {
                loadAPIs();
            }
        }
    }

    private void updateSharedPreferences(String apiUpdateLabel, String timeOfLastUpdate){

        String year = timeOfLastUpdate.substring(2,4);
        String month = timeOfLastUpdate.substring(5,7);
        String day = timeOfLastUpdate.substring(8,10);
        String AMOrPM;
        String hour = timeOfLastUpdate.substring(11, 13);
        int hourCalc = Integer.parseInt(hour);
        if(hourCalc > 12){
            hourCalc -= 12;
            AMOrPM = "PM";
            hour = String.valueOf(hourCalc);
        }
        else {
            AMOrPM = "AM";
        }

        String minutes = timeOfLastUpdate.substring(14,16);

        String finalTimeOfLastUpdate = "Updated at " + hour + ":" + minutes  + " " + AMOrPM + " on " + month + "/" + day + "/" + year;

        SharedPreferences preferencesNo = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
        SharedPreferences.Editor editor = preferencesNo.edit();
        editor.putString(apiUpdateLabel, finalTimeOfLastUpdate);
        editor.apply();

        progressBarProgress++;
        progressBar.setProgress(progressBarProgress);
    }














}
