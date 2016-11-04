package com.example.bradleycooper.politicslive.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bradleycooper.politicslive.R;
import com.example.bradleycooper.politicslive.adapters.UserAdapter;
import com.example.bradleycooper.politicslive.adapters.CandidateAdapterRanking;
import com.example.bradleycooper.politicslive.dataobjects.Candidate;
import com.example.bradleycooper.politicslive.dataobjects.User;
import com.example.bradleycooper.politicslive.datasources.CandidateDataSource;
import com.example.bradleycooper.politicslive.datasources.ExitPollDataSource;
import com.example.bradleycooper.politicslive.datasources.UserDataSource;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;

public class PollDetailActivity extends AppCompatActivity {

    private int democratColor, colorDown, circleDrawable, colorUp, colorToThemeWith, colorToThemeWithLight, colorToThemeWithDark, colorToThemeWithPressed;
    Drawable colorToThemeWithDrawable, roundedRectangleLight;


    ArrayList<Candidate> arrayListCandidates;
    CandidateAdapterRanking adapter;
    ArrayList<User> arrayListPartyUsers;
    UserAdapter adapterUser;

    ArrayList<User> arrayListUsers;

    private boolean registeredUsersCreated = false;
    private boolean totalVotesForAllCandidatesCreated = false;
    private boolean averageAgeofVoterCreated = false;
    private boolean genderBreakdownCreated = false;

    private String currentPollTitle, currentParty, currentPartyALT;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poll_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //ImageView imageViewPollIcon = (ImageView)findViewById(R.id.imageViewPollIcon);
        int democratIcon = R.drawable.ic_action_dnc_blue2;
        int republicanIcon = R.drawable.ic_action_gop_color2;

        /* Set title of page to the poll title */
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            currentPollTitle = extras.getString("POLL_TITLE");
        }
        if(currentPollTitle.equalsIgnoreCase("2016 Democratic Primary")){
            currentParty = "Democrat";
            currentPartyALT = "DNC";
            //imageViewPollIcon.setImageResource(democratIcon);
            colorToThemeWith = R.color.colorPrimary;
            colorToThemeWithDark = R.color.colorBlue;
            colorToThemeWithLight = R.color.colorBlueLight;
            colorToThemeWithPressed = R.color.colorMotionPressDNC;
            colorToThemeWithDrawable = getResources().getDrawable(R.color.colorPrimary);
            circleDrawable = R.drawable.circle_dnc;
            roundedRectangleLight = getResources().getDrawable(R.drawable.rectangle_rounded_blue_light);
            getSupportActionBar().setIcon(R.drawable.ic_action_dnc_white_50);
        }
        else if(currentPollTitle.equalsIgnoreCase("2016 Republican Primary")) {
            currentParty = "Republican";
            currentPartyALT = "GOP";
            colorToThemeWithLight = R.color.colorRedLight;
            //imageViewPollIcon.setImageResource(republicanIcon);
            colorToThemeWith = R.color.colorRedDark;
            colorToThemeWithDark = R.color.colorRedDarker;
            colorToThemeWithPressed = R.color.colorMotionPressGOP;
            circleDrawable = R.drawable.circle_gop;
            colorToThemeWithDrawable = getResources().getDrawable(R.color.colorRedDark);
            roundedRectangleLight = getResources().getDrawable(R.drawable.rectangle_rounded_red_light);
            getSupportActionBar().setIcon(R.drawable.ic_action_gop_white_50);
        }
        getSupportActionBar().setTitle(currentPollTitle);
        getSupportActionBar().setBackgroundDrawable(colorToThemeWithDrawable);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(colorToThemeWithDark));
        }

        inflateLinearLayoutPartyRanking();
        inflateLinearLayoutRegisteredUsers();
        inflateResources();

        Spinner spinnerFilter = (Spinner)findViewById(R.id.spinnerElectorate);
        ArrayAdapter<CharSequence> adapterFilter = ArrayAdapter.createFromResource(PollDetailActivity.this, R.array.data_array_alt, R.layout.spinner_item_2);
        adapterFilter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilter.setAdapter(adapterFilter);

        spinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                inflateVoterDemographics(parent.getItemAtPosition(position).toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


    }

    public void inflateLinearLayoutPartyRanking(){
        TextView textViewDataSource = (TextView)findViewById(R.id.layoutTopDataSource);
        textViewDataSource.setText("National Results (Huff)");

        LinearLayout linearLayoutTop = (LinearLayout)findViewById(R.id.layoutTop);
        linearLayoutTop.setBackgroundResource(colorToThemeWith);

        final ImageButton imageButton = (ImageButton)findViewById(R.id.layoutTopImageViewTriangle);
        if(currentParty.equalsIgnoreCase("Democrat")){
            imageButton.setImageResource(R.drawable.down_triangle_dnc);
        }
        else if(currentParty.equalsIgnoreCase("Republican")) {
            imageButton.setImageResource(R.drawable.down_triangle_gop);
        }
        imageButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        imageButton.setBackground(getResources().getDrawable(R.drawable.circle_back));
                        break;
                    case MotionEvent.ACTION_UP:

                        imageButton.setBackground(getResources().getDrawable(R.drawable.circle_white));
                        /*((MainActivity) getActivity()).onNavigationItemSelected("home");
                        activityCommunicator.passDataToActivity(R.id.nav_home); */
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        imageButton.setBackground(getResources().getDrawable(R.drawable.circle_white));
                        break;
                }
                return true;
            }
        });

        CandidateDataSource dataSource = new CandidateDataSource(PollDetailActivity.this);
        dataSource.open();
        arrayListCandidates = dataSource.getCandidatesInOrderOfVotes(currentPartyALT);
        dataSource.close();

        adapter = new CandidateAdapterRanking(PollDetailActivity.this,arrayListCandidates);
        LinearLayout linearLayoutCandidateRankings = (LinearLayout)findViewById(R.id.linearLayoutCandidateRankings);
        final int adapterCount = adapter.getCount();
        for(int i = 0 ; i < adapterCount ; i++) {
            final int iFinal = i;
            View item = adapter.getView(i, null, null);
            linearLayoutCandidateRankings.addView(item);
        }
    }

    private void inflateVoterDemographics(String dataSource){
        TextView textViewElectoratePoliticsLive = (TextView)findViewById(R.id.textViewElectoratePoliticsLive);
        TextView textViewTotalRespondentsPoliticsLive = (TextView)findViewById(R.id.textViewTotalRespondentsPoliticsLive);
        View cardViewPoliticsLive = (View)findViewById(R.id.cardViewPoliticsLive);

        TextView textViewElectorateCNN = (TextView)findViewById(R.id.textViewElectorateCNN);
        TextView textViewTotalRespondentsCNN = (TextView)findViewById(R.id.textViewTotalRespondentsCNN);
        View cardViewCNN = (View)findViewById(R.id.card_cnn);

        if(dataSource.equalsIgnoreCase("CNN Politics")){
            textViewElectoratePoliticsLive.setVisibility(View.GONE);
            textViewTotalRespondentsPoliticsLive.setVisibility(View.GONE);
            cardViewPoliticsLive.setVisibility(View.GONE);

            textViewElectorateCNN.setVisibility(View.VISIBLE);
            textViewTotalRespondentsCNN.setVisibility(View.VISIBLE);
            cardViewCNN.setVisibility(View.VISIBLE);

            inflateVoterDemographicsCNN();
        }
        else if(dataSource.equalsIgnoreCase("Politics Live Users")){
            textViewElectoratePoliticsLive.setVisibility(View.VISIBLE);
            textViewTotalRespondentsPoliticsLive.setVisibility(View.VISIBLE);
            cardViewPoliticsLive.setVisibility(View.VISIBLE);

            textViewElectorateCNN.setVisibility(View.GONE);
            textViewTotalRespondentsCNN.setVisibility(View.GONE);
            cardViewCNN.setVisibility(View.GONE);

            inflateVoterDemographicsPoliticsLive();
        }
    }
    public void inflateVoterDemographicsCNN() {
        TextView textViewTotalRespondentsCNN = (TextView)findViewById(R.id.textViewTotalRespondentsCNN);
        //textViewTotalRespondentsCNN.setBackground(roundedRectangleLight);

        TextView textViewElectorateCNN = (TextView)findViewById(R.id.textViewElectorateCNN);
        switch (currentParty){
            case "Democrat":
                textViewElectorateCNN.setText("Democratic Electorate");
                break;
            case "Republican":
                textViewElectorateCNN.setText("Republican Electorate");
                break;
        }

        final RelativeLayout relativeLayoutAgeCNN = (RelativeLayout)findViewById(R.id.relativeLayoutAgeCNN);
        final RelativeLayout relativeLayoutGenderCNN = (RelativeLayout)findViewById(R.id.relativeLayoutGenderCNN);
        final RelativeLayout relativeLayoutRaceCNN = (RelativeLayout)findViewById(R.id.relativeLayoutRaceCNN);
        final RelativeLayout relativeLayoutEducationCNN = (RelativeLayout)findViewById(R.id.relativeLayoutEducationCNN);
        final RelativeLayout relativeLayoutIncomeCNN = (RelativeLayout)findViewById(R.id.relativeLayoutIncomeCNN);
        final RelativeLayout relativeLayoutMarriageStatusCNN = (RelativeLayout)findViewById(R.id.relativeLayoutMarriageStatusCNN);
        final RelativeLayout relativeLayoutReligionCNN = (RelativeLayout)findViewById(R.id.relativeLayoutReligionCNN);
        final RelativeLayout relativeLayoutRegionCNN = (RelativeLayout)findViewById(R.id.relativeLayoutRegionCNN);

        ExitPollDataSource ds = new ExitPollDataSource(PollDetailActivity.this);
        ds.open();

        String partySpecialFormat;
        if(currentParty.equalsIgnoreCase("Democrat")){
            partySpecialFormat = "Democratic Electorate";
        }
        else {
            partySpecialFormat = "Republican Electorate";
        }

        final TextView textViewAge17To29CNN = (TextView)findViewById(R.id.textViewAge17To29CNN);
        final TextView textViewAge30To44CNN = (TextView)findViewById(R.id.textViewAge30To44CNN);
        final TextView textViewAge45To64CNN = (TextView)findViewById(R.id.textView45To64CNN);
        final TextView textViewAge65PlusCNN = (TextView)findViewById(R.id.textViewAge65PlusCNN);

        textViewAge17To29CNN.setText(ds.getExitPollsAverage(partySpecialFormat, "17_to_29"));
        textViewAge30To44CNN.setText(ds.getExitPollsAverage(partySpecialFormat, "30_to_44"));
        textViewAge45To64CNN.setText(ds.getExitPollsAverage(partySpecialFormat, "45_to_64"));
        textViewAge65PlusCNN.setText(ds.getExitPollsAverage(partySpecialFormat, "65_plus"));
        textViewAge17To29CNN.setBackgroundResource(circleDrawable);
        textViewAge30To44CNN.setBackgroundResource(circleDrawable);
        textViewAge45To64CNN.setBackgroundResource(circleDrawable);
        textViewAge65PlusCNN.setBackgroundResource(circleDrawable);


        final TextView textViewGenderMaleCNN = (TextView)findViewById(R.id.textViewMaleCNN);
        final TextView textViewGenderFemaleCNN = (TextView)findViewById(R.id.textViewFemaleCNN);
        textViewGenderMaleCNN.setText(ds.getExitPollsAverage(partySpecialFormat, "male"));
        textViewGenderFemaleCNN.setText(ds.getExitPollsAverage(partySpecialFormat, "female"));
        textViewGenderMaleCNN.setBackgroundResource(circleDrawable);
        textViewGenderFemaleCNN.setBackgroundResource(circleDrawable);


        final TextView textViewWhiteCNN = (TextView)findViewById(R.id.textViewWhiteCNN);
        final TextView textViewBlackCNN = (TextView)findViewById(R.id.textViewBlackCNN);
        final TextView textViewLatinoCNN = (TextView)findViewById(R.id.textViewLatinoCNN);
        final TextView textViewOtherRaceCNN = (TextView)findViewById(R.id.textViewOtherRaceCNN);
        textViewWhiteCNN.setText(ds.getExitPollsAverage(partySpecialFormat, "white"));
        textViewBlackCNN.setText(ds.getExitPollsAverage(partySpecialFormat, "black"));
        textViewLatinoCNN.setText(ds.getExitPollsAverage(partySpecialFormat, "latino"));
        textViewOtherRaceCNN.setText(ds.getExitPollsAverage(partySpecialFormat, "other"));
        textViewWhiteCNN.setBackgroundResource(circleDrawable);
        textViewBlackCNN.setBackgroundResource(circleDrawable);
        textViewLatinoCNN.setBackgroundResource(circleDrawable);
        textViewOtherRaceCNN.setBackgroundResource(circleDrawable);



        final TextView textViewHighSchoolCNN = (TextView)findViewById(R.id.textViewHighSchoolCNN);
        final TextView textViewCollegeCNN = (TextView)findViewById(R.id.textViewCollegeCNN);
        final TextView textViewPostgraduateCNN = (TextView)findViewById(R.id.textViewPostGraduateCNN);
        textViewHighSchoolCNN.setText(ds.getExitPollsAverage(partySpecialFormat, "highschool_or_less"));
        textViewCollegeCNN.setText(ds.getExitPollsAverage(partySpecialFormat, "some_college"));
        textViewPostgraduateCNN.setText(ds.getExitPollsAverage(partySpecialFormat, "postgraduate"));
        textViewHighSchoolCNN.setBackgroundResource(circleDrawable);
        textViewCollegeCNN.setBackgroundResource(circleDrawable);
        textViewPostgraduateCNN.setBackgroundResource(circleDrawable);



        final TextView textViewLessThan50KCNN = (TextView)findViewById(R.id.textViewLessThan50KCNN);
        final TextView textView50KTo100KCNN = (TextView)findViewById(R.id.textView50KTo100KCNN);
        final TextView textViewGreaterThan150KCNN = (TextView)findViewById(R.id.textViewGreaterThan150KCNN);
        textViewLessThan50KCNN.setText(ds.getExitPollsAverage(partySpecialFormat, "less_than_50k"));
        textView50KTo100KCNN.setText(ds.getExitPollsAverage(partySpecialFormat, "50k_to_100k"));
        textViewGreaterThan150KCNN.setText(ds.getExitPollsAverage(partySpecialFormat, "greater_than_100k"));
        textViewLessThan50KCNN.setBackgroundResource(circleDrawable);
        textView50KTo100KCNN.setBackgroundResource(circleDrawable);
        textViewGreaterThan150KCNN.setBackgroundResource(circleDrawable);


        final TextView textViewMarriedCNN = (TextView)findViewById(R.id.textViewMarriedCNN);
        final TextView textViewUnmarriedCNN = (TextView)findViewById(R.id.textViewUnmarriedCNN);
        textViewMarriedCNN.setText(ds.getExitPollsAverage(partySpecialFormat, "married"));
        textViewUnmarriedCNN.setText(ds.getExitPollsAverage(partySpecialFormat, "single"));
        textViewMarriedCNN.setBackgroundResource(circleDrawable);
        textViewUnmarriedCNN.setBackgroundResource(circleDrawable);


        final TextView textViewEvangelicalCNN = (TextView)findViewById(R.id.textViewEvangelicalCNN);
        final TextView textViewNotReligiousCNN = (TextView)findViewById(R.id.textViewNotReligiousCNN);
        textViewEvangelicalCNN.setText(ds.getExitPollsAverage(partySpecialFormat, "evangelical"));
        textViewNotReligiousCNN.setText(ds.getExitPollsAverage(partySpecialFormat, "not_evangelical"));
        textViewEvangelicalCNN.setBackgroundResource(circleDrawable);
        textViewNotReligiousCNN.setBackgroundResource(circleDrawable);


        final TextView textViewUrbanCNN = (TextView)findViewById(R.id.textViewUrbanCNN);
        final TextView textViewSuburbanCNN = (TextView)findViewById(R.id.textViewSuburbanCNN);
        final TextView textViewRuralCNN = (TextView)findViewById(R.id.textViewRuralCNN);
        textViewUrbanCNN.setText(ds.getExitPollsAverage(partySpecialFormat, "urban"));
        textViewSuburbanCNN.setText(ds.getExitPollsAverage(partySpecialFormat, "suburban"));
        textViewRuralCNN.setText(ds.getExitPollsAverage(partySpecialFormat, "rural"));
        textViewUrbanCNN.setBackgroundResource(circleDrawable);
        textViewSuburbanCNN.setBackgroundResource(circleDrawable);
        textViewUrbanCNN.setBackgroundResource(circleDrawable);


        final TextView textViewGOPCNN = (TextView)findViewById(R.id.textViewGOPCNN);
        final TextView textViewDNCCNN = (TextView)findViewById(R.id.textViewDNCCNN);
        final TextView textViewIndependentCNN = (TextView)findViewById(R.id.textViewIndependentCNN);
        textViewDNCCNN.setText(ds.getExitPollsAverage(partySpecialFormat, "democrats"));
        textViewGOPCNN.setText(ds.getExitPollsAverage(partySpecialFormat, "republicans"));
        textViewIndependentCNN.setText(ds.getExitPollsAverage(partySpecialFormat, "independents"));
        textViewDNCCNN.setBackgroundResource(circleDrawable);
        textViewGOPCNN.setBackgroundResource(circleDrawable);
        textViewIndependentCNN.setBackgroundResource(circleDrawable);


        final TextView textViewConservativeCNN = (TextView)findViewById(R.id.textViewConservativeCNN);
        final TextView textViewModerateCNN = (TextView)findViewById(R.id.textViewModerateCNN);
        final TextView textViewLiberalCNN = (TextView)findViewById(R.id.textViewLiberalCNN);
        textViewConservativeCNN.setText(ds.getExitPollsAverage(partySpecialFormat, "conservative"));
        textViewModerateCNN.setText(ds.getExitPollsAverage(partySpecialFormat, "moderate"));
        textViewLiberalCNN.setText(ds.getExitPollsAverage(partySpecialFormat, "liberal"));
        textViewConservativeCNN.setBackgroundResource(circleDrawable);
        textViewModerateCNN.setBackgroundResource(circleDrawable);
        textViewLiberalCNN.setBackgroundResource(circleDrawable);


        final TextView textViewExperiencedCNN = (TextView)findViewById(R.id.textViewExperiencedCNN);
        final TextView textViewOutsiderCNN = (TextView)findViewById(R.id.textViewOutsiderCNN);
        textViewExperiencedCNN.setText(ds.getExitPollsAverage(partySpecialFormat, "experienced"));
        textViewOutsiderCNN.setText(ds.getExitPollsAverage(partySpecialFormat, "outsider"));
        textViewExperiencedCNN.setBackgroundResource(circleDrawable);
        textViewOutsiderCNN.setBackgroundResource(circleDrawable);


        final TextView textViewEnthusiasticCNN = (TextView)findViewById(R.id.textViewEnthusiasticCNN);
        final TextView textViewSatisfiedCNN = (TextView)findViewById(R.id.textViewSatisfiedCNN);
        final TextView textViewUnsatisfiedCNN = (TextView)findViewById(R.id.textViewUnsatisfiedCNN);
        final TextView textViewAngryCNN = (TextView)findViewById(R.id.textViewAngryCNN);
        textViewEnthusiasticCNN.setText(ds.getExitPollsAverage(partySpecialFormat, "enthusiastic"));
        textViewSatisfiedCNN.setText(ds.getExitPollsAverage(partySpecialFormat, "satisfied"));
        textViewUnsatisfiedCNN.setText(ds.getExitPollsAverage(partySpecialFormat, "unsatisfied"));
        textViewAngryCNN.setText(ds.getExitPollsAverage(partySpecialFormat, "angry"));
        textViewEnthusiasticCNN.setBackgroundResource(circleDrawable);
        textViewSatisfiedCNN.setBackgroundResource(circleDrawable);
        textViewUnsatisfiedCNN.setBackgroundResource(circleDrawable);
        textViewAngryCNN.setBackgroundResource(circleDrawable);


        final TextView textViewImmigrationCNN = (TextView)findViewById(R.id.textViewImmigrationCNN);
        final TextView textViewEconomyCNN = (TextView)findViewById(R.id.textViewEconomyCNN);
        final TextView textViewTerrorismCNN = (TextView)findViewById(R.id.textViewTerrorismCNN);
        final TextView textViewGovSpendingCNN = (TextView)findViewById(R.id.textViewIssueLabel4);
        int heighestCurrentAverage = 0;
        textViewImmigrationCNN.setText(ds.getExitPollsAverage(partySpecialFormat, "immigration"));
        textViewEconomyCNN.setText(ds.getExitPollsAverage(partySpecialFormat, "economy"));
        textViewTerrorismCNN.setText(ds.getExitPollsAverage(partySpecialFormat, "terrorism"));
        textViewGovSpendingCNN.setText(ds.getExitPollsAverage(partySpecialFormat, "government_spending"));
        textViewImmigrationCNN.setBackgroundResource(circleDrawable);
        textViewEconomyCNN.setBackgroundResource(circleDrawable);
        textViewTerrorismCNN.setBackgroundResource(circleDrawable);
        textViewGovSpendingCNN.setBackgroundResource(circleDrawable);


        final RelativeLayout relativeLayoutPartyAffiliationCNN = (RelativeLayout)findViewById(R.id.relativeLayoutPartyAffiliationCNN);
        final RelativeLayout relativeLayoutIdealogyCNN = (RelativeLayout)findViewById(R.id.relativeLayoutIdealogyCNN);
        final RelativeLayout relativeLayoutVotingHistoryCNN = (RelativeLayout)findViewById(R.id.relativeLayoutVotingHistoryCNN);
        final RelativeLayout relativeLayoutExperienceCNN = (RelativeLayout)findViewById(R.id.relativeLayoutExperienceCNN);
        final RelativeLayout relativeLayoutFeelingsAboutCongressCNN = (RelativeLayout)findViewById(R.id.relativeLayoutFeelingsAboutCongressCNN);
        final RelativeLayout relativeLayoutMostImportantIssueCNN = (RelativeLayout)findViewById(R.id.relativeLayoutMostImportantIssueCNN);

        final ImageView imageViewArrowPersonalInformationCNN = (ImageView)findViewById(R.id.imageViewArrowEvent1);
        final RelativeLayout relativeLayoutPersonalInformationCNN = (RelativeLayout)findViewById(R.id.relativeLayoutPersonalInformationCNN);
        relativeLayoutPersonalInformationCNN.setBackgroundResource(colorToThemeWith);
        relativeLayoutPersonalInformationCNN.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        relativeLayoutPersonalInformationCNN.setBackgroundResource(colorToThemeWithPressed);
                        break;
                    case MotionEvent.ACTION_UP:
                        relativeLayoutPersonalInformationCNN.setBackgroundResource(colorToThemeWith);
                        if(relativeLayoutAgeCNN.getVisibility()==View.GONE){
                            relativeLayoutAgeCNN.setVisibility(View.VISIBLE);
                            relativeLayoutGenderCNN.setVisibility(View.VISIBLE);
                            relativeLayoutRaceCNN.setVisibility(View.VISIBLE);
                            relativeLayoutEducationCNN.setVisibility(View.VISIBLE);
                            relativeLayoutIncomeCNN.setVisibility(View.VISIBLE);
                            relativeLayoutMarriageStatusCNN.setVisibility(View.VISIBLE);
                            relativeLayoutReligionCNN.setVisibility(View.VISIBLE);
                            relativeLayoutRegionCNN.setVisibility(View.VISIBLE);

                            imageViewArrowPersonalInformationCNN.setImageResource(R.drawable.up_triangle_white);
                        }
                        else if(relativeLayoutAgeCNN.getVisibility()==View.VISIBLE){
                            relativeLayoutAgeCNN.setVisibility(View.GONE);
                            relativeLayoutGenderCNN.setVisibility(View.GONE);
                            relativeLayoutRaceCNN.setVisibility(View.GONE);
                            relativeLayoutEducationCNN.setVisibility(View.GONE);
                            relativeLayoutIncomeCNN.setVisibility(View.GONE);
                            relativeLayoutMarriageStatusCNN.setVisibility(View.GONE);
                            relativeLayoutReligionCNN.setVisibility(View.GONE);
                            relativeLayoutRegionCNN.setVisibility(View.GONE);

                            imageViewArrowPersonalInformationCNN.setImageResource(R.drawable.down_triangle_white);
                        }
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        relativeLayoutPersonalInformationCNN.setBackgroundResource(colorToThemeWith);
                        ;
                        break;
                }
                return true;
            }
        });
        final ImageView imageViewArrowPoliticalBackgroundCNN = (ImageView)findViewById(R.id.imageViewArrowPoliticalBackgroundCNN);
        final RelativeLayout relativeLayoutPoliticalBackgroundCNN = (RelativeLayout)findViewById(R.id.relativeLayoutPoliticalBackgroundCNN);
        relativeLayoutPoliticalBackgroundCNN.setBackgroundResource(colorToThemeWith);
        relativeLayoutPoliticalBackgroundCNN.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        relativeLayoutPoliticalBackgroundCNN.setBackgroundResource(colorToThemeWithPressed);
                        break;
                    case MotionEvent.ACTION_UP:
                        relativeLayoutPoliticalBackgroundCNN.setBackgroundResource(colorToThemeWith);
                        if(relativeLayoutPartyAffiliationCNN.getVisibility()==View.GONE){
                            relativeLayoutPartyAffiliationCNN.setVisibility(View.VISIBLE);
                            relativeLayoutIdealogyCNN.setVisibility(View.VISIBLE);
                            relativeLayoutVotingHistoryCNN.setVisibility(View.VISIBLE);
                            relativeLayoutExperienceCNN.setVisibility(View.VISIBLE);
                            relativeLayoutFeelingsAboutCongressCNN.setVisibility(View.VISIBLE);
                            relativeLayoutMostImportantIssueCNN.setVisibility(View.VISIBLE);

                            imageViewArrowPoliticalBackgroundCNN.setImageResource(R.drawable.up_triangle_white);
                        }
                        else if(relativeLayoutPartyAffiliationCNN.getVisibility()==View.VISIBLE){
                            relativeLayoutPartyAffiliationCNN.setVisibility(View.GONE);
                            relativeLayoutIdealogyCNN.setVisibility(View.GONE);
                            relativeLayoutVotingHistoryCNN.setVisibility(View.GONE);
                            relativeLayoutExperienceCNN.setVisibility(View.GONE);
                            relativeLayoutFeelingsAboutCongressCNN.setVisibility(View.GONE);
                            relativeLayoutMostImportantIssueCNN.setVisibility(View.GONE);

                            imageViewArrowPoliticalBackgroundCNN.setImageResource(R.drawable.down_triangle_white);
                        }

                        break;
                    case MotionEvent.ACTION_CANCEL:
                        relativeLayoutPoliticalBackgroundCNN.setBackgroundResource(colorToThemeWith);
                        ;
                        break;
                }
                return true;
            }
        });
    }
    public void inflateVoterDemographicsPoliticsLive(){
        TextView textViewTotalRespondentsPoliticsLive = (TextView)findViewById(R.id.textViewTotalRespondentsPoliticsLive);
        //textViewTotalRespondentsPoliticsLive.setBackground(roundedRectangleLight);

        TextView textViewElectoratePoliticsLive = (TextView)findViewById(R.id.textViewElectoratePoliticsLive);
        switch (currentParty){
            case "Democrat":
                textViewElectoratePoliticsLive.setText("Democratic Electorate");
                break;
            case "Republican":
                textViewElectoratePoliticsLive.setText("Republican Electorate");
                break;
        }

        final ImageView imageViewArrowPersonalInformationPoliticsLive = (ImageView)findViewById(R.id.imageViewArrowPersonalInformationPoliticsLive);
        final ImageView imageViewArrowPoliticalBackgroundPoliticsLive = (ImageView)findViewById(R.id.imageViewArrowPoliticalBackgroundPoliticsLive);
        final RelativeLayout relativeLayoutRegisteredParty = (RelativeLayout)findViewById(R.id.relativeLayoutRegisteredParty);
        final RelativeLayout relativeLayoutTotalVotes = (RelativeLayout)findViewById(R.id.relativeLayoutTotalVotes);
        final RelativeLayout relativeLayoutAverageAge = (RelativeLayout)findViewById(R.id.relativeLayoutAverageAge);
        final RelativeLayout relativeLayoutGenderAlt = (RelativeLayout)findViewById(R.id.relativeLayoutGenderAlt);

        final RelativeLayout relativeLayoutPersonalInformationPoliticsLive = (RelativeLayout)findViewById(R.id.relativeLayoutPersonalInformationPoliticsLive);
        relativeLayoutPersonalInformationPoliticsLive.setBackgroundResource(colorToThemeWith);
        relativeLayoutPersonalInformationPoliticsLive.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        relativeLayoutPersonalInformationPoliticsLive.setBackgroundResource(colorToThemeWithPressed);
                        break;
                    case MotionEvent.ACTION_UP:
                        relativeLayoutPersonalInformationPoliticsLive.setBackgroundResource(colorToThemeWith);
                        if(relativeLayoutRegisteredParty.getVisibility() == View.GONE) {
                            relativeLayoutRegisteredParty.setVisibility(View.VISIBLE);
                            relativeLayoutTotalVotes.setVisibility(View.VISIBLE);
                            relativeLayoutAverageAge.setVisibility(View.VISIBLE);
                            relativeLayoutGenderAlt.setVisibility(View.VISIBLE);
                            imageViewArrowPersonalInformationPoliticsLive.setImageResource(R.drawable.up_triangle_white);
                        }
                        else if(relativeLayoutRegisteredParty.getVisibility() == View.VISIBLE) {
                            relativeLayoutRegisteredParty.setVisibility(View.GONE);
                            relativeLayoutTotalVotes.setVisibility(View.GONE);
                            relativeLayoutAverageAge.setVisibility(View.GONE);
                            relativeLayoutGenderAlt.setVisibility(View.GONE);
                            imageViewArrowPersonalInformationPoliticsLive.setImageResource(R.drawable.down_triangle_white);
                        }
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        relativeLayoutPersonalInformationPoliticsLive.setBackgroundResource(colorToThemeWith);
                        ;
                        break;
                }
                return true;
            }
        });
        final RelativeLayout relativeLayoutPoliticalBackgroundPoliticsLive = (RelativeLayout)findViewById(R.id.relativeLayoutPoliticalBackgroundPoliticsLive);
        relativeLayoutPoliticalBackgroundPoliticsLive.setBackgroundResource(colorToThemeWith);
        relativeLayoutPoliticalBackgroundPoliticsLive.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        relativeLayoutPoliticalBackgroundPoliticsLive.setBackgroundResource(colorToThemeWithPressed);
                        break;
                    case MotionEvent.ACTION_UP:
                        relativeLayoutPoliticalBackgroundPoliticsLive.setBackgroundResource(colorToThemeWith);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        relativeLayoutPoliticalBackgroundPoliticsLive.setBackgroundResource(colorToThemeWith);
                        break;
                }
                return true;
            }
        });

        int circleDrawable;
        switch (currentParty){
            case "Democrat":
                circleDrawable = R.drawable.circle_dnc;
                break;
            case "Republican":
                circleDrawable = R.drawable.circle_gop;
                break;
            default:
                circleDrawable = R.drawable.circle_dnc;
                break;
        }

        UserDataSource userDataSource = new UserDataSource(PollDetailActivity.this);
        userDataSource.open();
        arrayListUsers = userDataSource.getUsersByParty(currentParty);

        TextView textViewRegisteredPartyMembers = (TextView) findViewById(R.id.textViewTotalUsersPoliticsLive);
        TextView textViewRegisteredPartyMembersLabel = (TextView)findViewById(R.id.textViewTotalVotersTitleLabel);
        TextView textViewTotalVotes = (TextView) findViewById(R.id.textViewTotalVotesPoliticsLive);
        TextView textViewTotalVotesLabel = (TextView) findViewById(R.id.textViewTotalVotesLabel);
        TextView textViewAverageAge = (TextView) findViewById(R.id.textViewAverageAgePoliticsLive);
        TextView textViewAverageAgeLabel = (TextView) findViewById(R.id.textViewGenderLabelCNN);
        TextView textViewMalePercentage = (TextView) findViewById(R.id.textViewMalePoliticsLive);
        TextView textViewFemalePercentage = (TextView) findViewById(R.id.textViewFemalePoliticsLive);

        if(arrayListUsers.size() > 0) {
            textViewRegisteredPartyMembers.setText(Integer.toString(userDataSource.getPartyMembers(currentParty)));
            textViewAverageAge.setText(Integer.toString(userDataSource.getAverageVoterAge(currentParty)));
            textViewMalePercentage.setText(Integer.toString(userDataSource.getGenderPercentage(currentParty, "Male")));
            textViewFemalePercentage.setText(Integer.toString(userDataSource.getGenderPercentage(currentParty, "Female")));
        }
        else {
            textViewRegisteredPartyMembers.setText("0");
            TextView textViewWarning = (TextView)findViewById(R.id.textViewWarning);
            textViewWarning.setVisibility(View.VISIBLE);
            TextView textViewWarning2 = (TextView)findViewById(R.id.textViewWarning2);
            textViewWarning2.setVisibility(View.VISIBLE);
            View viewDemographics = (View)findViewById(R.id.viewDemographics);
            viewDemographics.setVisibility(View.GONE);
            TextView textViewTapToExpand = (TextView)findViewById(R.id.textViewTapToExpand);
            textViewTapToExpand.setVisibility(View.GONE);
        }

        textViewRegisteredPartyMembers.setBackgroundResource(circleDrawable);
        textViewRegisteredPartyMembersLabel.setText("Registered party voters = ");
        textViewTotalVotes.setText(Integer.toString(getTotalVotes(arrayListCandidates)));
        textViewTotalVotes.setBackgroundResource(circleDrawable);
        textViewTotalVotesLabel.setText("Total votes for party = ");
        textViewAverageAge.setBackgroundResource(circleDrawable);
        textViewAverageAgeLabel.setText("Avgerage age of voter = ");
        textViewMalePercentage.setBackgroundResource(circleDrawable);
        textViewFemalePercentage.setBackgroundResource(circleDrawable);

        final int colorDown = ContextCompat.getColor(PollDetailActivity.this, R.color.colorLayoutPressed);
        final int colorDownLight = ContextCompat.getColor(PollDetailActivity.this, R.color.colorLayoutPressedLight);

        final int colorUp = ContextCompat.getColor(PollDetailActivity.this, R.color.colorBackgroundGrey);
        final int colorWhite = ContextCompat.getColor(PollDetailActivity.this, R.color.colorWhite);

        final RelativeLayout relativeLayoutRegisteredUsers = (RelativeLayout)findViewById(R.id.relativeLayoutRegisteredUsers);
        relativeLayoutRegisteredUsers.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        relativeLayoutRegisteredUsers.setBackgroundColor(colorDown);
                        break;
                    case MotionEvent.ACTION_UP:
                        relativeLayoutRegisteredUsers.setBackgroundColor(colorUp);
                        /* ((MainActivity) getFragmentManager()).onNavigationItemSelected("userslist");
                        activityCommunicator.passDataToActivity(R.id.nav_users_list); */
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        relativeLayoutRegisteredUsers.setBackgroundColor(colorUp);
                        break;
                }

                return true;
            }
        });


        //final RelativeLayout relativeLayoutRegisteredParty = (RelativeLayout)findViewById(R.id.relativeLayoutRegisteredParty);
        //final RelativeLayout relativeLayoutTotalVotes = (RelativeLayout)findViewById(R.id.relativeLayoutTotalVotes);
        //final RelativeLayout relativeLayoutAverageAge = (RelativeLayout)findViewById(R.id.relativeLayoutAverageAge);
    }


    public void inflateResources() {
        TextView textViewResources = (TextView)findViewById(R.id.textViewResources);
        final TextView textViewOfficalCampaignWebsite = (TextView)findViewById(R.id.textViewChangeVote);
        final TextView textViewEmailCandidate = (TextView)findViewById(R.id.textViewEmailCandidate);
        final TextView textViewTwitterPage = (TextView)findViewById(R.id.textViewTwitterPage);

        colorDown = ContextCompat.getColor(PollDetailActivity.this, R.color.colorLayoutPressed);
        colorUp = ContextCompat.getColor(PollDetailActivity.this, R.color.colorBackgroundGrey);

        switch(currentParty){
            case "Democrat":
                textViewResources.setText("DEMOCRATIC PARTY RESOURCES");
                textViewOfficalCampaignWebsite.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                textViewOfficalCampaignWebsite.setBackgroundColor(colorDown);
                                break;
                            case MotionEvent.ACTION_UP:
                                textViewOfficalCampaignWebsite.setBackgroundColor(0x00000000);
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.democrats.org/"));
                                startActivity(browserIntent);
                                break;
                            case MotionEvent.ACTION_CANCEL:
                                textViewOfficalCampaignWebsite.setBackgroundColor(0x00000000);
                                break;
                        }
                        return true;
                    }
                });
                textViewEmailCandidate.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                textViewEmailCandidate.setBackgroundColor(colorDown);
                                break;
                            case MotionEvent.ACTION_UP:
                                textViewEmailCandidate.setBackgroundColor(0x00000000);
                                textViewOfficalCampaignWebsite.setBackgroundColor(0x00000000);
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://my.democrats.org/page/s/contact-the-democrats"));
                                startActivity(browserIntent);
                                break;
                            case MotionEvent.ACTION_CANCEL:
                                textViewEmailCandidate.setBackgroundColor(0x00000000);
                                break;
                        }
                        return true;
                    }
                });
                textViewTwitterPage.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                textViewTwitterPage.setBackgroundColor(colorDown);
                                break;
                            case MotionEvent.ACTION_UP:
                                textViewTwitterPage.setBackgroundColor(0x00000000);
                                textViewOfficalCampaignWebsite.setBackgroundColor(0x00000000);
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/TheDemocrats?ref_src=twsrc%5Egoogle%7Ctwcamp%5Eserp%7Ctwgr%5Eauthor"));
                                startActivity(browserIntent);
                                break;
                            case MotionEvent.ACTION_CANCEL:
                                textViewTwitterPage.setBackgroundColor(0x00000000);
                                break;
                        }
                        return true;
                    }
                });
                break;


            case "Republican":
                textViewResources.setText("REPUBLICAN PARTY RESOURCES");
                textViewOfficalCampaignWebsite.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch (event.getAction()){
                            case MotionEvent.ACTION_DOWN:
                                textViewOfficalCampaignWebsite.setBackgroundColor(colorDown);
                                break;
                            case MotionEvent.ACTION_UP:
                                textViewOfficalCampaignWebsite.setBackgroundColor(0x00000000);
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.gop.com/"));
                                startActivity(browserIntent);
                                break;
                            case MotionEvent.ACTION_CANCEL:
                                textViewOfficalCampaignWebsite.setBackgroundColor(0x00000000);
                                break;
                        }
                        return true;
                    }
                });
                textViewEmailCandidate.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch (event.getAction()){
                            case MotionEvent.ACTION_DOWN:
                                textViewEmailCandidate.setBackgroundColor(colorDown);
                                break;
                            case MotionEvent.ACTION_UP:
                                textViewEmailCandidate.setBackgroundColor(0x00000000);

                                Intent i = new Intent(Intent.ACTION_SEND);
                                i.setType("message/rfc822");
                                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"ecampaign@gop.com"});
                                i.putExtra(Intent.EXTRA_SUBJECT, "About the 2016 Republican Primary...");
                                i.putExtra(Intent.EXTRA_TEXT, "Sent from Politics Live!");
                                try {
                                    startActivity(Intent.createChooser(i, "Send mail..."));
                                } catch (android.content.ActivityNotFoundException ex) {
                                    Toast.makeText(PollDetailActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case MotionEvent.ACTION_CANCEL:
                                textViewEmailCandidate.setBackgroundColor(0x00000000);
                                break;
                        }
                        return true;
                    }
                });
                textViewTwitterPage.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                textViewTwitterPage.setBackgroundColor(colorDown);
                                break;
                            case MotionEvent.ACTION_UP:
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/GOP?ref_src=twsrc%5Egoogle%7Ctwcamp%5Eserp%7Ctwgr%5Eauthor"));
                                startActivity(browserIntent);
                                textViewTwitterPage.setBackgroundColor(0x00000000);
                                break;
                            case MotionEvent.ACTION_CANCEL:
                                textViewTwitterPage.setBackgroundColor(0x00000000);
                                break;
                        }
                        return true;
                    }
                });
                break;
        }
    }

    public void inflateLinearLayoutRegisteredUsers(){
        UserDataSource userDataSource = new UserDataSource(PollDetailActivity.this);
        userDataSource.open();
        arrayListUsers = userDataSource.getUsersByParty(currentParty);
        userDataSource.close();

        adapterUser = new UserAdapter(PollDetailActivity.this, arrayListUsers);
        LinearLayout linearLayoutRegisteredUsers = (LinearLayout)findViewById(R.id.linearLayoutRegisteredUsers);
        final int adapterCount = adapterUser.getCount();
        for(int i = 0 ; i < adapterCount; i++) {
            final int iFinal = i;
            View item = adapterUser.getView(i, null, null);
            linearLayoutRegisteredUsers.addView(item);
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        }

        TextView textViewRegisteredVotersSeeAll = (TextView)findViewById(R.id.textViewRegisteredVotersSeeAll);
        TextView textViewRegisteredVotersTitle = (TextView)findViewById(R.id.textViewRegisteredVotersTitle);

        textViewRegisteredVotersTitle.setText(currentParty + " Users");
        textViewRegisteredVotersSeeAll.setTextColor(colorToThemeWith);
    }


    private void formatBarChart(BarChart b) {
        b.setDescription("");
        b.getLegend().setEnabled(false);
        b.setBackgroundColor(Color.WHITE);
        b.setDrawGridBackground(false);
        b.setDrawMarkerViews(false);
        b.setDrawBarShadow(false);
        b.setDrawBorders(false);
        b.getAxisLeft().setDrawLabels(false);
        b.getAxisRight().setDrawLabels(false);
        b.getAxisLeft().setDrawGridLines(false);
        b.getAxisRight().setDrawGridLines(false);
        b.getXAxis().setDrawGridLines(false);
        b.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        b.getXAxis().setDrawAxisLine(false);
        b.setDrawBorders(false);
        b.getAxisLeft().setDrawAxisLine(false);
        b.getAxisRight().setDrawAxisLine(false);
        b.animateXY(1000, 1000);
    }

    private void formatBarDataSet(BarDataSet d) {
        final int colorBlue1 = ContextCompat.getColor(PollDetailActivity.this, R.color.colorPrimary);
        final int colorBlue2 = ContextCompat.getColor(PollDetailActivity.this, R.color.colorMotionPressDNC);
        final int colorBlue3 = ContextCompat.getColor(PollDetailActivity.this, R.color.colorBlueLight);
        int[] democratColors = {colorBlue1, colorBlue2, colorBlue3};

        final int colorRed1 = ContextCompat.getColor(PollDetailActivity.this, R.color.colorRed);
        final int colorRed2 = ContextCompat.getColor(PollDetailActivity.this, R.color.colorMotionPressGOP);
        final int colorRed3 = ContextCompat.getColor(PollDetailActivity.this, R.color.colorRedLight);
        int[] republicanColors = {colorRed1,colorRed2,colorRed3};

        int[] colors = democratColors;
        switch(currentParty){
            case "Democrat":
                colors = democratColors;
                break;
            case "Republican":
                colors = republicanColors;
                break;
        }

        d.setColors(colors);
        d.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return String.format("%.0f", value);
            }
        });
        d.setValueTextSize(10f);
    }

    public int getTotalVotes(ArrayList<Candidate> arrayListCandidates){
        int totalVotes = 0;
        for(Candidate c : arrayListCandidates) {
            totalVotes += c.getNumberOfVotes();
        }
        return totalVotes;
    }
    public int getNumberOfRegisteredUsers(ArrayList<User> arrayListUsers) {
        int registered = 0;
        for(User u : arrayListUsers) {
            registered++;
        }
        return registered;
    }
}
