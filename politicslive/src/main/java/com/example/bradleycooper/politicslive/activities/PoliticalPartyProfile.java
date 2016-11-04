package com.example.bradleycooper.politicslive.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.bradleycooper.politicslive.dataobjects.PoliticalParty;
import com.example.bradleycooper.politicslive.datasources.PoliticalPartyDataSource;
import com.example.bradleycooper.politicslive.R;
import com.example.bradleycooper.politicslive.dataobjects.User;
import com.example.bradleycooper.politicslive.adapters.UserAdapter;
import com.example.bradleycooper.politicslive.datasources.UserDataSource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class PoliticalPartyProfile extends AppCompatActivity {


    PoliticalParty currentPoliticalParty;
    private int idDNC, colorDown, colorUp, greenColor, greenColorLight, independentColor, independentColorLight, colorPrimaryColor, colorMotionPress, colorMotionPressDNC, colorMotionPressGOP, democratColor, democratColorLight, democratColorDark, democratColorDarker, idGOP, republicanColor, republicanColorLight, republicanColorDarker;
    private String candidateParty, candidateName;

    private String partyName, partyDescription, huffFavorableRating, huffUnfavorableRating, site, email, twitter;
    private int numberOfHouseSeats, numberOfSenateSeats;
    private Toolbar toolbar;
    private ImageView imgView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();

        democratColor = ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary);
        democratColorDark = ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark);
        democratColorDarker = ContextCompat.getColor(getApplicationContext(), R.color.colorBlueDarker);

        democratColorLight = ContextCompat.getColor(getApplicationContext(), R.color.colorBlueLight);
        republicanColor = ContextCompat.getColor(getApplicationContext(), R.color.colorRed);
        republicanColorLight = ContextCompat.getColor(getApplicationContext(), R.color.colorRedLight);
        republicanColorDarker = ContextCompat.getColor(getApplicationContext(), R.color.colorRedDarker);
        idGOP = getResources().getIdentifier("com.example.bradleycooper.politicslive:drawable/ic_action_gop_red_light", null, null);
        idDNC = getResources().getIdentifier("com.example.bradleycooper.politicslive:drawable/ic_action_dnc_blue_light", null, null);

        greenColor = ContextCompat.getColor(getApplicationContext(), R.color.colorGreenParty);
        independentColor = ContextCompat.getColor(getApplicationContext(), R.color.colorPurple);

        PoliticalPartyDataSource ppds = new PoliticalPartyDataSource(PoliticalPartyProfile.this);
        ppds.open();
        currentPoliticalParty = ppds.getSpecificPoliticalParty(extras.getInt("politicalPartyId"));
        ppds.close();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.BLACK);
        }

        setContentView(R.layout.activity_political_party_profile);


        imgView = (ImageView)findViewById(R.id.imageViewBackButton);
        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        initProfileResources();
        initPoliticalParty(extras.getInt("politicalPartyId"));
    }

    private void initProfileResources(){
        final TextView textViewSite = (TextView)findViewById(R.id.textViewSite);
        final TextView textViewEmail = (TextView)findViewById(R.id.textViewEmail);
        final TextView textViewTwitter = (TextView)findViewById(R.id.textViewTwitter);

        colorDown = ContextCompat.getColor(PoliticalPartyProfile.this, R.color.colorLayoutPressed);
        colorUp = ContextCompat.getColor(PoliticalPartyProfile.this, R.color.colorBackgroundGrey);

        textViewSite.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        textViewSite.setBackgroundColor(colorDown);
                        break;
                    case MotionEvent.ACTION_UP:
                        textViewSite.setBackgroundColor(0x00000000);
                        Intent browserIntentSite = new Intent(Intent.ACTION_VIEW, Uri.parse(currentPoliticalParty.getSite()));
                        startActivity(browserIntentSite);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        textViewSite.setBackgroundColor(0x00000000);
                        break;
                }
                return true;
            }
        });
        textViewEmail.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        textViewEmail.setBackgroundColor(colorDown);
                        break;
                    case MotionEvent.ACTION_UP:
                        textViewEmail.setBackgroundColor(0x00000000);
                        /* Check to see if email is an actual email address of a contact form of a web page */
                        if(currentPoliticalParty.getEmail().contains("@")){
                            final Intent emailIntent = new Intent(Intent.ACTION_SEND);
                            emailIntent.setType("plain/text");
                            emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
                                    new String[] { currentPoliticalParty.getEmail() });

                            emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                                    "Email Subject");

                            emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,
                                    "Sent from 'Politics In America' Android Application");

                            startActivity(Intent.createChooser(
                                    emailIntent, "Send mail..."));

                        }
                        else {
                            Intent browserIntentEmail = new Intent(Intent.ACTION_VIEW, Uri.parse(currentPoliticalParty.getEmail()));
                            startActivity(browserIntentEmail);
                        }
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        textViewEmail.setBackgroundColor(0x00000000);
                        break;
                }
                return true;
            }
        });
        textViewTwitter.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        textViewTwitter.setBackgroundColor(colorDown);
                        break;
                    case MotionEvent.ACTION_UP:
                        textViewTwitter.setBackgroundColor(0x00000000);
                        Intent browserIntentTwitter = new Intent(Intent.ACTION_VIEW, Uri.parse(currentPoliticalParty.getTwitter()));
                        startActivity(browserIntentTwitter);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        textViewTwitter.setBackgroundColor(0x00000000);
                        break;
                }
                return true;
            }
        });
    }
    private void initPoliticalParty(int id) {
        partyName = currentPoliticalParty.getName();
        partyDescription = currentPoliticalParty.getDescription();
        numberOfHouseSeats = currentPoliticalParty.getNumberOfHouseSeats();
        numberOfSenateSeats = currentPoliticalParty.getNumberOfSenateSeats();
        site = currentPoliticalParty.getSite();
        email = currentPoliticalParty.getEmail();
        twitter = currentPoliticalParty.getTwitter();



        TextView textViewName = (TextView)findViewById(R.id.textViewName);
        TextView textViewDescription = (TextView)findViewById(R.id.textViewDescription);
        ImageView imageViewWide = (ImageView)findViewById(R.id.imageViewWidePicture);
        TextView textViewSenateSeats = (TextView)findViewById(R.id.textViewSeatsInSenate);
        TextView textViewHouseSeats = (TextView)findViewById(R.id.textViewSeatsInHouse);
        RelativeLayout relativeLayoutPersonalInformation = (RelativeLayout)findViewById(R.id.relativeLayoutPersonalInformationCNN);
        RelativeLayout relativeLayoutPoliticalBackground = (RelativeLayout)findViewById(R.id.relativeLayoutPoliticalBackgroundCNN);

        textViewName.setText(currentPoliticalParty.getName());
        textViewDescription.setText(currentPoliticalParty.getDescription());
        textViewSenateSeats.setText(String.valueOf(currentPoliticalParty.getNumberOfSenateSeats()));
        textViewHouseSeats.setText(String.valueOf(currentPoliticalParty.getNumberOfHouseSeats()));

        if(partyName.equalsIgnoreCase("Republican Party")){
            textViewHouseSeats.setBackgroundResource(R.drawable.circle_gop);
            textViewSenateSeats.setBackgroundResource(R.drawable.circle_gop);
            relativeLayoutPersonalInformation.setBackgroundColor(republicanColor);
            relativeLayoutPoliticalBackground.setBackgroundColor(republicanColor);
        }

        else if(partyName.equalsIgnoreCase("Green Party")){
            textViewHouseSeats.setBackgroundResource(R.drawable.circle_green);
            textViewSenateSeats.setBackgroundResource(R.drawable.circle_green);
            relativeLayoutPersonalInformation.setBackgroundColor(greenColor);
            relativeLayoutPoliticalBackground.setBackgroundColor(greenColor);
        }
        else if(partyName.equalsIgnoreCase("Libertarian Party")){
            textViewHouseSeats.setBackgroundResource(R.drawable.circle_lib);
            textViewSenateSeats.setBackgroundResource(R.drawable.circle_lib);
            relativeLayoutPersonalInformation.setBackgroundColor(independentColor);
            relativeLayoutPoliticalBackground.setBackgroundColor(independentColor);
        }

        imageViewWide.setImageBitmap(loadImageFromStorage(currentPoliticalParty.getWidePicture()));

        /*
        UserDataSource userDataSource = new UserDataSource(CandidateProfile.this);
        userDataSource.open();
        if(candidateParty.equalsIgnoreCase("DNC")) {
            textView_total_votes.setBackgroundResource(R.drawable.circle_dnc);
            textView_average_age.setBackgroundResource(R.drawable.circle_dnc);
            textView_male_percentage.setBackgroundResource(R.drawable.circle_dnc);
            textView_female_percentage.setBackgroundResource(R.drawable.circle_dnc);

            relativeLayoutImmigration.setBackgroundColor(democratColor);
            relativeLayoutMarriage.setBackgroundColor(democratColor);
            relativeLayoutEducation.setBackgroundColor(democratColor);
            relativeLayoutGuns.setBackgroundColor(democratColor);
            relativeLayoutEconomy.setBackgroundColor(democratColor);
            relativeLayoutHealthCare.setBackgroundColor(democratColor);
            relativeLayoutEnviornment.setBackgroundColor(democratColor);
            relativeLayoutForeignPolicy.setBackgroundColor(democratColor);
        }
        else{
            textView_total_votes.setBackgroundResource(R.drawable.circle_gop);
            textView_average_age.setBackgroundResource(R.drawable.circle_gop);
            textView_male_percentage.setBackgroundResource(R.drawable.circle_gop);
            textView_female_percentage.setBackgroundResource(R.drawable.circle_gop);

            relativeLayoutImmigration.setBackgroundColor(republicanColor);
            relativeLayoutMarriage.setBackgroundColor(republicanColor);
            relativeLayoutEducation.setBackgroundColor(republicanColor);
            relativeLayoutGuns.setBackgroundColor(republicanColor);
            relativeLayoutEconomy.setBackgroundColor(republicanColor);
            relativeLayoutHealthCare.setBackgroundColor(republicanColor);
            relativeLayoutEnviornment.setBackgroundColor(republicanColor);
            relativeLayoutForeignPolicy.setBackgroundColor(republicanColor);
        }
        textView_total_votes.setText(Integer.toString(currentCandidate.getNumberOfVotes()));

        ArrayList<User> userArrayList = new ArrayList<User>();
        userArrayList = userDataSource.getUsersByCandidate(currentCandidate.getCandidateName(), currentCandidate.getParty());



        if(userArrayList.size() > 0) {
            UserAdapter userAdapter = new UserAdapter(this, userArrayList);

            LinearLayout linearLayoutRegisteredVoters = (LinearLayout)findViewById(R.id.linearLayoutRegisteredVoters);
            final int adapterCount = userAdapter.getCount();
            for(int i = 0 ; i < adapterCount ; i++) {
                final int iFinal = i;
                View item = userAdapter.getView(i, null, null);
                linearLayoutRegisteredVoters.addView(item);
                item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });
            }



            textView_male_percentage.setText(Integer.toString(userDataSource.getGenderPercentageByCandidate(currentCandidate.getCandidateName(), currentCandidate.getParty(), "Male")));
            textView_female_percentage.setText(Integer.toString(userDataSource.getGenderPercentageByCandidate(currentCandidate.getCandidateName(), currentCandidate.getParty(), "Female")));
            textView_average_age.setText(Integer.toString(userDataSource.getAverageVoterAgeByCandidate(currentCandidate.getCandidateName(), currentCandidate.getParty())));
        }
        else {
            TextView textViewWarning = (TextView)findViewById(R.id.textViewWarning);
            textViewWarning.setVisibility(View.VISIBLE);
        }
        userDataSource.close();
        byte[] byteArray = currentCandidate.getWidePicture();
        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        imageViewWide.setImageBitmap(bmp);
        ds.close();


        final RelativeLayout relativeLayoutImmigration = (RelativeLayout)findViewById(R.id.relativeLayoutImmigration);
        final RelativeLayout relativeLayoutImmigrationExtended = (RelativeLayout)findViewById(R.id.relativeLayoutImmigrationExtended);
        final RelativeLayout relativeLayoutMarriage = (RelativeLayout)findViewById(R.id.relativeLayoutMarriage);
        final RelativeLayout relativeLayoutMarriageExtended = (RelativeLayout)findViewById(R.id.relativeLayoutMarriageExtended);
        final RelativeLayout relativeLayoutEducation = (RelativeLayout)findViewById(R.id.relativeLayoutEducation);
        final RelativeLayout relativeLayoutEducationExtended = (RelativeLayout)findViewById(R.id.relativeLayoutEducationExtended);
        final RelativeLayout relativeLayoutGuns = (RelativeLayout)findViewById(R.id.relativeLayoutGuns);
        final RelativeLayout relativeLayoutGunsExtended = (RelativeLayout)findViewById(R.id.relativeLayoutGunsExtended);
        final RelativeLayout relativeLayoutHealthCare = (RelativeLayout)findViewById(R.id.relativeLayoutHealthCare);
        final RelativeLayout relativeLayoutHealthCareExtended = (RelativeLayout)findViewById(R.id.relativeLayoutHealthCareExtended);
        final RelativeLayout relativeLayoutEconomy = (RelativeLayout)findViewById(R.id.relativeLayoutEconomy);
        final RelativeLayout relativeLayoutEconomyExtended = (RelativeLayout)findViewById(R.id.relativeLayoutEconomyExtended);
        final RelativeLayout relativeLayoutEnviornment = (RelativeLayout)findViewById(R.id.relativeLayoutEnviornment);
        final RelativeLayout relativeLayoutEnviornmentExtended = (RelativeLayout)findViewById(R.id.relativeLayoutEnviornmentExtended);
        final RelativeLayout relativeLayoutForeignPolicy = (RelativeLayout)findViewById(R.id.relativeLayoutForeignPolicy);
        final RelativeLayout relativeLayoutForeignPolicyExtended = (RelativeLayout)findViewById(R.id.relativeLayoutForeignPolicyExtended);

        colorMotionPressDNC = ContextCompat.getColor(CandidateProfile.this, R.color.colorMotionPressDNC);
        colorMotionPressGOP = ContextCompat.getColor(CandidateProfile.this, R.color.colorMotionPressGOP);

        if(currentCandidate.getParty().equalsIgnoreCase("DNC")){
            colorMotionPress = colorMotionPressDNC;
            colorPrimaryColor = democratColor;
        }
        else {
            colorMotionPress = colorMotionPressGOP;
            colorPrimaryColor = republicanColor;
        }

        relativeLayoutImmigration.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        relativeLayoutImmigration.setBackgroundColor(colorMotionPress);
                        break;
                    case MotionEvent.ACTION_UP:
                        if(relativeLayoutImmigrationExtended.getVisibility() == View.VISIBLE) {
                            makeAllExtendedInvisible("");
                        }
                        else {
                            makeAllExtendedInvisible("relativeLayoutImmigrationExtended");
                        }
                        relativeLayoutImmigration.setBackgroundColor(colorPrimaryColor);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        relativeLayoutImmigration.setBackgroundColor(colorPrimaryColor);
                        break;
                }
                return true;
            }
        });

        relativeLayoutMarriage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        relativeLayoutMarriage.setBackgroundColor(colorMotionPress);
                        break;
                    case MotionEvent.ACTION_UP:
                        if(relativeLayoutMarriageExtended.getVisibility() == View.VISIBLE) {
                            makeAllExtendedInvisible("");
                        }
                        else {
                            makeAllExtendedInvisible("relativeLayoutMarriageExtended");
                        }
                        relativeLayoutMarriage.setBackgroundColor(colorPrimaryColor);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        relativeLayoutMarriage.setBackgroundColor(colorPrimaryColor);
                        break;
                }
                return true;

            }
        });

        relativeLayoutEducation.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        relativeLayoutEducation.setBackgroundColor(colorMotionPress);
                        break;
                    case MotionEvent.ACTION_UP:
                        if(relativeLayoutEducationExtended.getVisibility() == View.VISIBLE) {
                            makeAllExtendedInvisible("");
                        }
                        else {
                            makeAllExtendedInvisible("relativeLayoutEducationExtended");
                        }
                        relativeLayoutEducation.setBackgroundColor(colorPrimaryColor);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        relativeLayoutEducation.setBackgroundColor(colorPrimaryColor);
                        break;
                }
                return true;

            }
        });
        relativeLayoutGuns.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        relativeLayoutGuns.setBackgroundColor(colorMotionPress);
                        break;
                    case MotionEvent.ACTION_UP:
                        if(relativeLayoutGunsExtended.getVisibility() == View.VISIBLE) {
                            makeAllExtendedInvisible("");
                        }
                        else {
                            makeAllExtendedInvisible("relativeLayoutGunsExtended");
                        }
                        relativeLayoutGuns.setBackgroundColor(colorPrimaryColor);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        relativeLayoutGuns.setBackgroundColor(colorPrimaryColor);
                        break;
                }
                return true;

            }
        });
        relativeLayoutHealthCare.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        relativeLayoutHealthCare.setBackgroundColor(colorMotionPress);
                        break;
                    case MotionEvent.ACTION_UP:
                        if(relativeLayoutHealthCareExtended.getVisibility() == View.VISIBLE) {
                            makeAllExtendedInvisible("");
                        }
                        else {
                            makeAllExtendedInvisible("relativeLayoutHealthCareExtended");
                        }
                        relativeLayoutHealthCare.setBackgroundColor(colorPrimaryColor);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        relativeLayoutHealthCare.setBackgroundColor(colorPrimaryColor);
                        break;
                }
                return true;

            }
        });
        relativeLayoutEconomy.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        relativeLayoutEconomy.setBackgroundColor(colorMotionPress);
                        break;
                    case MotionEvent.ACTION_UP:
                        if(relativeLayoutEconomyExtended.getVisibility() == View.VISIBLE) {
                            makeAllExtendedInvisible("");
                        }
                        else {
                            makeAllExtendedInvisible("relativeLayoutEconomyExtended");
                        }
                        relativeLayoutEconomy.setBackgroundColor(colorPrimaryColor);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        relativeLayoutEconomy.setBackgroundColor(colorPrimaryColor);
                        break;
                }
                return true;

            }
        });
        relativeLayoutEnviornment.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        relativeLayoutEnviornment.setBackgroundColor(colorMotionPress);
                        break;
                    case MotionEvent.ACTION_UP:
                        if(relativeLayoutEnviornmentExtended.getVisibility() == View.VISIBLE) {
                            makeAllExtendedInvisible("");
                        }
                        else {
                            makeAllExtendedInvisible("relativeLayoutEnviornmentExtended");
                        }
                        relativeLayoutEnviornment.setBackgroundColor(colorPrimaryColor);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        relativeLayoutEnviornment.setBackgroundColor(colorPrimaryColor);
                        break;
                }
                return true;

            }
        });
        relativeLayoutForeignPolicy.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        relativeLayoutForeignPolicy.setBackgroundColor(colorMotionPress);
                        break;
                    case MotionEvent.ACTION_UP:
                        if(relativeLayoutForeignPolicyExtended.getVisibility() == View.VISIBLE) {
                            makeAllExtendedInvisible("");
                        }
                        else {
                            makeAllExtendedInvisible("relativeLayoutForeignPolicyExtended");
                        }
                        relativeLayoutForeignPolicy.setBackgroundColor(colorPrimaryColor);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        relativeLayoutForeignPolicy.setBackgroundColor(colorPrimaryColor);
                        break;
                }
                return true;

            }
        }); */


    }
    private Bitmap loadImageFromStorage(String path) {
        {
            try {
                File f = new File(path);
                Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
                //ImageView img = (ImageView) getActivity().findViewById(R.id.imageViewTesting);
                //img.setImageBitmap(b);
                return b;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public void inflateLinearLayoutRegisteredDemocrats(){
        UserDataSource userDataSource = new UserDataSource(this);
        userDataSource.open();
        ArrayList<User> arrayListDemocratUsers = userDataSource.getUsersByParty("Democrat");
        userDataSource.close();
        UserAdapter adapterUser = new UserAdapter(this, arrayListDemocratUsers);
    }

    public void makeAllExtendedInvisible(String relativeLayoutName) {
        final RelativeLayout relativeLayoutImmigrationExtended = (RelativeLayout)findViewById(R.id.relativeLayoutImmigrationExtended);
        final RelativeLayout relativeLayoutMarriageExtended = (RelativeLayout)findViewById(R.id.relativeLayoutMarriageExtended);
        final RelativeLayout relativeLayoutEducationExtended = (RelativeLayout)findViewById(R.id.relativeLayoutEducationExtended);
        final RelativeLayout relativeLayoutGunsExtended = (RelativeLayout)findViewById(R.id.relativeLayoutGunsExtended);
        final RelativeLayout relativeLayoutHealthCareExtended = (RelativeLayout)findViewById(R.id.relativeLayoutHealthCareExtended);
        final RelativeLayout relativeLayoutEconomyExtended = (RelativeLayout)findViewById(R.id.relativeLayoutEconomyExtended);
        final RelativeLayout relativeLayoutEnviornmentExtended = (RelativeLayout)findViewById(R.id.relativeLayoutEnviornmentExtended);
        final RelativeLayout relativeLayoutForeignPolicyExtended = (RelativeLayout)findViewById(R.id.relativeLayoutForeignPolicyExtended);


        relativeLayoutImmigrationExtended.setVisibility(View.GONE);
        relativeLayoutMarriageExtended.setVisibility(View.GONE);;
        relativeLayoutEducationExtended.setVisibility(View.GONE);
        relativeLayoutGunsExtended.setVisibility(View.GONE);
        relativeLayoutEconomyExtended.setVisibility(View.GONE);
        relativeLayoutHealthCareExtended.setVisibility(View.GONE);
        relativeLayoutEnviornmentExtended.setVisibility(View.GONE);
        relativeLayoutForeignPolicyExtended.setVisibility(View.GONE);

        switch(relativeLayoutName){
            case "relativeLayoutImmigrationExtended":
                relativeLayoutImmigrationExtended.setVisibility(View.VISIBLE);
                break;
            case "relativeLayoutMarriageExtended":
                relativeLayoutMarriageExtended.setVisibility(View.VISIBLE);
                break;
            case "relativeLayoutEducationExtended":
                relativeLayoutEducationExtended.setVisibility(View.VISIBLE);
                break;
            case "relativeLayoutGunsExtended":
                relativeLayoutGunsExtended.setVisibility(View.VISIBLE);
                break;
            case "relativeLayoutEconomyExtended":
                relativeLayoutEconomyExtended.setVisibility(View.VISIBLE);
                break;
            case "relativeLayoutHealthCareExtended":
                relativeLayoutHealthCareExtended.setVisibility(View.VISIBLE);
                break;
            case "relativeLayoutEnviornmentExtended":
                relativeLayoutEnviornmentExtended.setVisibility(View.VISIBLE);
                break;
            case "relativeLayoutForeignPolicyExtended":
                relativeLayoutForeignPolicyExtended.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    public void initIssues(String candidateName){
        TextView textViewImmigrationExtended = (TextView)findViewById(R.id.textViewImmigrationExtended);
        TextView textViewSameSexMarriageExtended = (TextView)findViewById(R.id.textViewSameSexMarriageExtended);
        TextView textViewEducationExtended = (TextView) findViewById(R.id.textViewEducationExtended);
        TextView textViewGunControlExtended = (TextView) findViewById(R.id.textViewGunControlExtended);
        TextView textViewEconomyExtended = (TextView) findViewById(R.id.textViewEconomyExtended);
        TextView textViewHealthCareExtended = (TextView) findViewById(R.id.textViewHealthCareExtended);
        TextView textViewEnviornmentExtended = (TextView) findViewById(R.id.textViewEnviornmentExtended);
        TextView textViewForeignPolicyExtended = (TextView) findViewById(R.id.textViewForeignPolicyExtended);

        switch(candidateName) {
            case "Ted Cruz":
                String[] ted_cruz = getResources().getStringArray(R.array.issues_ted_cruz);

                textViewImmigrationExtended.setText(ted_cruz[0]);
                textViewSameSexMarriageExtended.setText(ted_cruz[1]);
                textViewEducationExtended.setText(ted_cruz[2]);
                textViewGunControlExtended.setText(ted_cruz[3]);
                textViewEconomyExtended.setText(ted_cruz[4]);
                textViewHealthCareExtended.setText(ted_cruz[5]);
                textViewEnviornmentExtended.setText(ted_cruz[6]);
                textViewForeignPolicyExtended.setText(ted_cruz[7]);
                break;
            case "Donald Trump":
                String[] donald_trump = getResources().getStringArray(R.array.issues_donald_trump);

                textViewImmigrationExtended.setText(donald_trump[0]);
                textViewSameSexMarriageExtended.setText(donald_trump[1]);
                textViewEducationExtended.setText(donald_trump[2]);
                textViewGunControlExtended.setText(donald_trump[3]);
                textViewEconomyExtended.setText(donald_trump[4]);
                textViewHealthCareExtended.setText(donald_trump[5]);
                textViewEnviornmentExtended.setText(donald_trump[6]);
                textViewForeignPolicyExtended.setText(donald_trump[7]);
                break;
            case "John Kasich":
                String[] john_kasich = getResources().getStringArray(R.array.issues_john_kasich);

                textViewImmigrationExtended.setText(john_kasich[0]);
                textViewSameSexMarriageExtended.setText(john_kasich[1]);
                textViewEducationExtended.setText(john_kasich[2]);
                textViewGunControlExtended.setText(john_kasich[3]);
                textViewEconomyExtended.setText(john_kasich[4]);
                textViewHealthCareExtended.setText(john_kasich[5]);
                textViewEnviornmentExtended.setText(john_kasich[6]);
                textViewForeignPolicyExtended.setText(john_kasich[7]);
                break;
            case "Hilary Clinton":
                String[] hillary_clinton = getResources().getStringArray(R.array.issues_hillary_clinton);

                textViewImmigrationExtended.setText(hillary_clinton[0]);
                textViewSameSexMarriageExtended.setText(hillary_clinton[1]);
                textViewEducationExtended.setText(hillary_clinton[2]);
                textViewGunControlExtended.setText(hillary_clinton[3]);
                textViewEconomyExtended.setText(hillary_clinton[4]);
                textViewHealthCareExtended.setText(hillary_clinton[5]);
                textViewEnviornmentExtended.setText(hillary_clinton[6]);
                textViewForeignPolicyExtended.setText(hillary_clinton[7]);
                break;
            case "Bernie Sanders":
                String[] bernie_sanders = getResources().getStringArray(R.array.issues_bernie_sanders);

                textViewImmigrationExtended.setText(bernie_sanders[0]);
                textViewSameSexMarriageExtended.setText(bernie_sanders[1]);
                textViewEducationExtended.setText(bernie_sanders[2]);
                textViewGunControlExtended.setText(bernie_sanders[3]);
                textViewEconomyExtended.setText(bernie_sanders[4]);
                textViewHealthCareExtended.setText(bernie_sanders[5]);
                textViewEnviornmentExtended.setText(bernie_sanders[6]);
                textViewForeignPolicyExtended.setText(bernie_sanders[7]);
                break;
        }
    }

}
