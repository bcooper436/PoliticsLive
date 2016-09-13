package com.example.bradleycooper.politicslive;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

//import com.github.mikephil.charting.charts.BarChart;
//import com.github.mikephil.charting.data.BarData;
//import com.github.mikephil.charting.data.BarDataSet;
//import com.github.mikephil.charting.data.BarEntry;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomePage.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomePage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomePage extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    /* Fields for initializing the sqllite database */
    public int NUMBER_OF_DATABASES_TO_POPULATE = 3;
    ProgressDialog progressBar;
    int progressBarProgress = 0;


    private int democratColor, colorGrey600 = R.color.colorMaterialGrey600, colorGrey800 = R.color.colorMaterialGrey800, colorGrey700 = R.color.colorMaterialGrey700, colorDown, colorUp, colorToThemeWith = R.color.colorPrimary, colorToThemeWithLight, colorToThemeWithPressed = R.color.colorMotionPressDNC;

    private OnFragmentInteractionListener mListener;

    private boolean registeredUsersCreated = false;
    private boolean totalVotesForAllCandidatesCreated = false;
    private boolean averageAgeofVoterCreated = false;
    private boolean genderBreakdownCreated = false;

    public String questionOneAnswer = "", questionTwoAnswer = "", questionThreeAnswer = "";

    public int resize = 150;
    public int numberOfRandomlyGeneratedUsers = 0;
    int votesForHilary = 0, votesForBernie = 0, votesForTrump = 0, votesForCruz = 0, votesForKasich = 0, votesForGary = 0;
    private Candidate currentCandidate;
    private User user;

    public HomePage() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomePage.
     */
    // TODO: Rename and change types and number of parameters
    public static HomePage newInstance(String param1, String param2) {
        HomePage fragment = new HomePage();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_page, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        final OnCommunicateActivityListener activityCommunicator = (OnCommunicateActivityListener) getActivity();
        super.onActivityCreated(savedInstanceState);
        initPoliticalParties();
        initEvents();
        initPoliticalProfile();
        createGraphs("Huff Post");
        final RelativeLayout relativeLayoutFeaturedPolls = (RelativeLayout)getView().findViewById(R.id.relativeLayoutFeaturedPolls);
        final RelativeLayout relativeLayoutPoliticalParties = (RelativeLayout)getView().findViewById(R.id.relativeLayoutPoliticalParties);
        final RelativeLayout relativeLayoutUpcomingEvents = (RelativeLayout)getView().findViewById(R.id.relativeLayoutUpcomingEvents);
        final RelativeLayout relativeLayoutPoliticalProfile = (RelativeLayout)getView().findViewById(R.id.relativeLayoutPoliticalProfile);

        final int colorDownLight = ContextCompat.getColor(getActivity(), R.color.colorLayoutPressedLight);
        final int colorWhite = ContextCompat.getColor(getActivity(), R.color.colorWhite);


        colorDown = ContextCompat.getColor(getActivity(), R.color.colorLayoutPressed);
        colorUp = ContextCompat.getColor(getActivity(), R.color.colorBackgroundGrey);
        ImageView imageViewSettings = (ImageView)getView().findViewById(R.id.imageViewSettings);
        imageViewSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout linearLayoutQuestionOne = (LinearLayout)getView().findViewById(R.id.linearLayoutQuestionOne);
                LinearLayout linearLayoutQuestionTwo = (LinearLayout)getView().findViewById(R.id.linearLayoutQuestionTwo);
                LinearLayout linearLayoutQuestionThree = (LinearLayout)getView().findViewById(R.id.linearLayoutQuestionThree);

                View viewQuestionOne = (View)getView().findViewById(R.id.viewQuestionOne);
                View viewQuestionTwo = (View)getView().findViewById(R.id.viewQuestionTwo);
                View viewQuestionThree = (View)getView().findViewById(R.id.viewQuestionThree);

                viewQuestionOne.setVisibility(View.VISIBLE);
                viewQuestionTwo.setVisibility(View.VISIBLE);
                viewQuestionThree.setVisibility(View.VISIBLE);

                LinearLayout linearLayoutStrip = (LinearLayout)getView().findViewById(R.id.linearLayoutStrip);
                View viewStrip = (View)getView().findViewById(R.id.viewStrip);

                viewStrip.setVisibility(View.INVISIBLE);
            }
        });
        final Button buttonShare = (Button)getView().findViewById(R.id.buttonShare);
        buttonShare.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        buttonShare.setBackgroundResource(R.drawable.button_border_pressed);
                        break;
                    case MotionEvent.ACTION_UP:
                        buttonShare.setBackgroundResource(R.drawable.button_border);
                        final Intent emailIntent = new Intent(Intent.ACTION_SEND);
                        emailIntent.setType("plain/text");

                        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                                "Email Subject");

                        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,
                                "Sent from 'Politics In America' Android Application");

                        startActivity(Intent.createChooser(
                                emailIntent, "Send mail..."));
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        buttonShare.setBackgroundResource(R.drawable.button_border);
                        break;
                }
                return true;
            }
        });

        /*
        relativeLayoutFeaturedPolls.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        relativeLayoutFeaturedPolls.setBackgroundColor(colorDown);
                        break;
                    case MotionEvent.ACTION_UP:
                        relativeLayoutFeaturedPolls.setBackgroundColor(colorUp);
                        Fragment fragment;
                        FragmentManager fragmentManager = getFragmentManager();
                        fragment = new ListOfPolls();
                        fragmentManager.beginTransaction()
                                .replace(R.id.container, fragment)
                                .commit();
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        relativeLayoutFeaturedPolls.setBackgroundColor(colorUp);
                        break;
                }
                return true;
            }
        });


        relativeLayoutPoliticalParties.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        relativeLayoutPoliticalParties.setBackgroundColor(colorDown);
                        break;
                    case MotionEvent.ACTION_UP:
                        relativeLayoutPoliticalParties.setBackgroundColor(colorUp);
                        Fragment fragment;
                        FragmentManager fragmentManager = getFragmentManager();
                        fragment = new ListOfPoliticalParties();
                        fragmentManager.beginTransaction()
                                .replace(R.id.container, fragment)
                                .commit();
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        relativeLayoutPoliticalParties.setBackgroundColor(colorUp);
                        break;
                }
                return true;
            }
        });
        relativeLayoutUpcomingEvents.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        relativeLayoutUpcomingEvents.setBackgroundColor(colorDown);
                        break;
                    case MotionEvent.ACTION_UP:
                        relativeLayoutUpcomingEvents.setBackgroundColor(colorUp);
                        Fragment fragment;
                        FragmentManager fragmentManager = getFragmentManager();
                        fragment = new ListOfEvents();
                        fragmentManager.beginTransaction()
                                .replace(R.id.container, fragment)
                                .commit();
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        relativeLayoutUpcomingEvents.setBackgroundColor(colorUp);
                        break;
                }
                return true;
            }
        });
        relativeLayoutPoliticalProfile.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        relativeLayoutPoliticalProfile.setBackgroundColor(colorDown);
                        break;
                    case MotionEvent.ACTION_UP:
                        relativeLayoutPoliticalProfile.setBackgroundColor(colorUp);
                        Fragment fragment;
                        FragmentManager fragmentManager = getFragmentManager();
                        fragment = new UserProfile();
                        fragmentManager.beginTransaction()
                                .replace(R.id.container, fragment)
                                .commit();
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        relativeLayoutPoliticalProfile.setBackgroundColor(colorUp);
                        break;
                }
                return true;
            }
        });
        /*Spinner spinnerDataGeneral = (Spinner) getView().findViewById(R.id.spinnerDataGeneral);
        ArrayAdapter<CharSequence> adapterFilterGeneral = ArrayAdapter.createFromResource(getActivity(), R.array.data_array, R.layout.spinner_item_2);
        adapterFilterGeneral.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDataGeneral.setAdapter(adapterFilterGeneral);

        spinnerDataGeneral.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                createGraphs(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        }); */

        /*Spinner spinnerElectorate = (Spinner)getActivity().findViewById(R.id.spinnerElectorate);
        ArrayAdapter<CharSequence> adapterFilterElectorate = ArrayAdapter.createFromResource(getActivity(), R.array.data_array_alt, R.layout.spinner_item_2);
        adapterFilterElectorate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerElectorate.setAdapter(adapterFilterElectorate);

        spinnerElectorate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //inflateVoterDemographics(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });*/



        TextView textViewHillaryVotesGeneral = (TextView)getActivity().findViewById(R.id.textViewHillaryVotesGeneral);
        textViewHillaryVotesGeneral.setText(updateChartInformation("Hillary Clinton"));

        TextView textViewTrumpVotesGeneral = (TextView)getActivity().findViewById(R.id.textViewTrumpVotesGeneral);
        textViewTrumpVotesGeneral.setText(updateChartInformation("Donald Trump"));

        TextView textViewGaryVotesGeneral = (TextView)getActivity().findViewById(R.id.textViewGaryVotesGeneral);
        textViewGaryVotesGeneral.setText(updateChartInformation("Gary Johnson"));


        final CandidateDataSource ds5 = new CandidateDataSource(getActivity());


        ImageView imageHillary = (ImageView)getActivity().findViewById(R.id.imageViewHillary);
        ImageView imageTrump = (ImageView)getActivity().findViewById(R.id.imageViewTrump);
        ImageView imageGary = (ImageView)getActivity().findViewById(R.id.imageViewGary);

        imageHillary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CandidateProfile.class);
                ds5.open();
                intent.putExtra("candidateId",
                        ds5.getCandidateByName("Hillary Clinton").getCandidateID());
                ds5.close();
                startActivity(intent);

            }
        });
        imageTrump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CandidateProfile.class);
                ds5.open();
                intent.putExtra("candidateId",
                        ds5.getCandidateByName("Donald Trump").getCandidateID());
                ds5.close();
                startActivity(intent);

            }
        });
        imageGary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CandidateProfile.class);
                ds5.open();
                intent.putExtra("candidateId",
                        ds5.getCandidateByName("Gary Johnson").getCandidateID());
                ds5.close();
                startActivity(intent);

            }
        });
    }
    private void initPoliticalProfile(){

        final TextView textViewQuestionOneYes = (TextView)getActivity().findViewById(R.id.textViewQuestionOneYes);
        final TextView textViewQuestionOneNo = (TextView)getActivity().findViewById(R.id.textViewQuestionOneNo);
        final TextView textViewQuestionTwoDemocrat = (TextView)getActivity().findViewById(R.id.textViewQuestionTwoDemocrat);
        final TextView textViewQuestionTwoRepublican = (TextView)getActivity().findViewById(R.id.textViewQuestionTwoRepublican);
        final TextView textViewQuestionTwoLibertarian = (TextView)getActivity().findViewById(R.id.textViewQuestionTwoLibertarian);
        final TextView textViewQuestionTwoGreen = (TextView)getActivity().findViewById(R.id.textViewQuestionTwoGreen);
        final TextView textViewQuestionThreeClinton = (TextView)getActivity().findViewById(R.id.textViewQuestionThreeClinton);
        final TextView textViewQuestionThreeTrump = (TextView)getActivity().findViewById(R.id.textViewQuestionThreeTrump);
        final TextView textViewQuestionThreeJohnson = (TextView)getActivity().findViewById(R.id.textViewQuestionThreeJohnson);
        final TextView textViewQuestionThreeStein = (TextView)getActivity().findViewById(R.id.textViewQuestionThreeStein);

        final int colorDownLight = ContextCompat.getColor(getActivity(), R.color.colorMaterialGrey300);
        final int colorWhite = ContextCompat.getColor(getActivity(), R.color.colorWhite);


        textViewQuestionOneYes.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        textViewQuestionOneYes.setBackgroundColor(colorDownLight);
                        break;
                    case MotionEvent.ACTION_UP:
                        textViewQuestionOneNo.setBackgroundColor(colorWhite);
                        if(questionOneAnswer.equalsIgnoreCase("yes")) {
                            textViewQuestionOneYes.setBackgroundColor(colorWhite);
                            questionOneAnswer = "";
                        }else {
                            textViewQuestionOneYes.setBackgroundColor(colorDownLight);
                            questionOneAnswer = String.valueOf(textViewQuestionOneYes.getText());
                            checkIfQuestionsAreFinished();
                        }
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        textViewQuestionOneYes.setBackgroundColor(colorWhite);
                        break;
                }
                return true;
            }
        });
        textViewQuestionOneNo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        textViewQuestionOneNo.setBackgroundColor(colorDownLight);
                        break;
                    case MotionEvent.ACTION_UP:
                        textViewQuestionOneYes.setBackgroundColor(colorWhite);

                        if(questionOneAnswer.equalsIgnoreCase("no")) {
                            textViewQuestionOneNo.setBackgroundColor(colorWhite);
                            questionOneAnswer = "";
                        }else {
                            textViewQuestionOneNo.setBackgroundColor(colorDownLight);
                            questionOneAnswer = String.valueOf(textViewQuestionOneNo.getText());
                            checkIfQuestionsAreFinished();
                        }

                        break;
                    case MotionEvent.ACTION_CANCEL:
                        textViewQuestionOneNo.setBackgroundColor(colorWhite);
                        break;
                }
                return true;
            }
        });
        textViewQuestionTwoDemocrat.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        textViewQuestionTwoDemocrat.setBackgroundColor(colorDownLight);
                        break;
                    case MotionEvent.ACTION_UP:
                        textViewQuestionTwoRepublican.setBackgroundColor(colorWhite);
                        textViewQuestionTwoLibertarian.setBackgroundColor(colorWhite);
                        textViewQuestionTwoGreen.setBackgroundColor(colorWhite);

                        if(questionTwoAnswer.equalsIgnoreCase("democrat")) {
                            textViewQuestionTwoDemocrat.setBackgroundColor(colorWhite);
                            questionTwoAnswer = "";
                        }else {
                            textViewQuestionTwoDemocrat.setBackgroundColor(colorDownLight);
                            questionTwoAnswer = String.valueOf(textViewQuestionTwoDemocrat.getText());
                            checkIfQuestionsAreFinished();
                        }

                        break;
                    case MotionEvent.ACTION_CANCEL:
                        textViewQuestionTwoDemocrat.setBackgroundColor(colorWhite);
                        break;
                }
                return true;
            }
        });
        textViewQuestionTwoRepublican.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        textViewQuestionTwoRepublican.setBackgroundColor(colorDownLight);
                        break;
                    case MotionEvent.ACTION_UP:
                        textViewQuestionTwoDemocrat.setBackgroundColor(colorWhite);
                        textViewQuestionTwoLibertarian.setBackgroundColor(colorWhite);
                        textViewQuestionTwoGreen.setBackgroundColor(colorWhite);

                        if(questionTwoAnswer.equalsIgnoreCase("republican")) {
                            textViewQuestionTwoRepublican.setBackgroundColor(colorWhite);
                            questionTwoAnswer = "";
                        }else {
                            textViewQuestionTwoRepublican.setBackgroundColor(colorDownLight);
                            questionTwoAnswer = String.valueOf(textViewQuestionTwoRepublican.getText());
                            checkIfQuestionsAreFinished();
                        }

                        break;
                    case MotionEvent.ACTION_CANCEL:
                        textViewQuestionTwoRepublican.setBackgroundColor(colorWhite);
                        break;
                }
                return true;
            }
        });
        textViewQuestionTwoLibertarian.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        textViewQuestionTwoLibertarian.setBackgroundColor(colorDownLight);
                        break;
                    case MotionEvent.ACTION_UP:
                        textViewQuestionTwoDemocrat.setBackgroundColor(colorWhite);
                        textViewQuestionTwoRepublican.setBackgroundColor(colorWhite);
                        textViewQuestionTwoGreen.setBackgroundColor(colorWhite);

                        if(questionTwoAnswer.equalsIgnoreCase("libertarian")) {
                            textViewQuestionTwoLibertarian.setBackgroundColor(colorWhite);
                            questionTwoAnswer = "";
                        }else {
                            textViewQuestionTwoLibertarian.setBackgroundColor(colorDownLight);
                            questionTwoAnswer = String.valueOf(textViewQuestionTwoLibertarian.getText());
                            checkIfQuestionsAreFinished();
                        }

                        break;
                    case MotionEvent.ACTION_CANCEL:
                        textViewQuestionTwoLibertarian.setBackgroundColor(colorWhite);
                        break;
                }
                return true;
            }
        });
        textViewQuestionTwoGreen.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        textViewQuestionTwoGreen.setBackgroundColor(colorDownLight);
                        break;
                    case MotionEvent.ACTION_UP:
                        textViewQuestionTwoDemocrat.setBackgroundColor(colorWhite);
                        textViewQuestionTwoRepublican.setBackgroundColor(colorWhite);
                        textViewQuestionTwoLibertarian.setBackgroundColor(colorWhite);

                        if(questionTwoAnswer.equalsIgnoreCase("green")) {
                            textViewQuestionTwoGreen.setBackgroundColor(colorWhite);
                            questionTwoAnswer = "";
                        }else {
                            textViewQuestionTwoGreen.setBackgroundColor(colorDownLight);
                            questionTwoAnswer = String.valueOf(textViewQuestionTwoGreen.getText());
                            checkIfQuestionsAreFinished();
                        }
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        textViewQuestionTwoGreen.setBackgroundColor(colorWhite);
                        break;
                }
                return true;
            }
        });
        textViewQuestionThreeClinton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        textViewQuestionThreeClinton.setBackgroundColor(colorDownLight);
                        break;
                    case MotionEvent.ACTION_UP:
                        textViewQuestionThreeTrump.setBackgroundColor(colorWhite);
                        textViewQuestionThreeJohnson.setBackgroundColor(colorWhite);
                        textViewQuestionThreeStein.setBackgroundColor(colorWhite);

                        if(questionThreeAnswer.equalsIgnoreCase("hillary clinton")) {
                            textViewQuestionThreeClinton.setBackgroundColor(colorWhite);
                            questionThreeAnswer = "";
                        }else {
                            textViewQuestionThreeClinton.setBackgroundColor(colorDownLight);
                            questionThreeAnswer = String.valueOf(textViewQuestionThreeClinton.getText());
                            checkIfQuestionsAreFinished();
                        }

                        break;
                    case MotionEvent.ACTION_CANCEL:
                        textViewQuestionThreeClinton.setBackgroundColor(colorWhite);
                        break;
                }
                return true;
            }
        });
        textViewQuestionThreeTrump.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        textViewQuestionThreeTrump.setBackgroundColor(colorDownLight);
                        break;
                    case MotionEvent.ACTION_UP:
                        textViewQuestionThreeClinton.setBackgroundColor(colorWhite);
                        textViewQuestionThreeJohnson.setBackgroundColor(colorWhite);
                        textViewQuestionThreeStein.setBackgroundColor(colorWhite);

                        if(questionThreeAnswer.equalsIgnoreCase("donald trump")) {
                            textViewQuestionThreeTrump.setBackgroundColor(colorWhite);
                            questionThreeAnswer = "";
                        }else {
                            textViewQuestionThreeTrump.setBackgroundColor(colorDownLight);
                            questionThreeAnswer = String.valueOf(textViewQuestionThreeTrump.getText());
                            checkIfQuestionsAreFinished();
                        }

                        break;
                    case MotionEvent.ACTION_CANCEL:
                        textViewQuestionThreeTrump.setBackgroundColor(colorWhite);
                        break;
                }
                return true;
            }
        });
        textViewQuestionThreeJohnson.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        textViewQuestionThreeJohnson.setBackgroundColor(colorDownLight);
                        break;
                    case MotionEvent.ACTION_UP:
                        textViewQuestionThreeClinton.setBackgroundColor(colorWhite);
                        textViewQuestionThreeTrump.setBackgroundColor(colorWhite);
                        textViewQuestionThreeStein.setBackgroundColor(colorWhite);

                        if(questionThreeAnswer.equalsIgnoreCase("gary johnson")) {
                            textViewQuestionThreeJohnson.setBackgroundColor(colorWhite);
                            questionThreeAnswer = "";
                        }else {
                            textViewQuestionThreeJohnson.setBackgroundColor(colorDownLight);
                            questionThreeAnswer = String.valueOf(textViewQuestionThreeJohnson.getText());
                            checkIfQuestionsAreFinished();
                        }

                        break;
                    case MotionEvent.ACTION_CANCEL:
                        textViewQuestionThreeJohnson.setBackgroundColor(colorWhite);
                        break;
                }
                return true;
            }
        });
        textViewQuestionThreeStein.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        textViewQuestionThreeStein.setBackgroundColor(colorDownLight);
                        break;
                    case MotionEvent.ACTION_UP:
                        textViewQuestionThreeClinton.setBackgroundColor(colorWhite);
                        textViewQuestionThreeTrump.setBackgroundColor(colorWhite);
                        textViewQuestionThreeJohnson.setBackgroundColor(colorWhite);

                        if(questionThreeAnswer.equalsIgnoreCase("jill stein")) {
                            textViewQuestionThreeStein.setBackgroundColor(colorWhite);
                            questionThreeAnswer = "";
                        }else {
                            textViewQuestionThreeStein.setBackgroundColor(colorDownLight);
                            questionThreeAnswer = String.valueOf(textViewQuestionThreeStein.getText());
                            checkIfQuestionsAreFinished();
                        }

                        break;
                    case MotionEvent.ACTION_CANCEL:
                        textViewQuestionThreeStein.setBackgroundColor(colorWhite);
                        break;
                }
                return true;
            }
        });
    }
    private void checkIfQuestionsAreFinished(){
        LinearLayout linearLayoutQuestionOne = (LinearLayout)getView().findViewById(R.id.linearLayoutQuestionOne);
        LinearLayout linearLayoutQuestionTwo = (LinearLayout)getView().findViewById(R.id.linearLayoutQuestionTwo);
        LinearLayout linearLayoutQuestionThree = (LinearLayout)getView().findViewById(R.id.linearLayoutQuestionThree);

        View viewQuestionOne = (View)getView().findViewById(R.id.viewQuestionOne);
        View viewQuestionTwo = (View)getView().findViewById(R.id.viewQuestionTwo);
        View viewQuestionThree = (View)getView().findViewById(R.id.viewQuestionThree);

        if(!questionOneAnswer.equals("") && !questionTwoAnswer.equals("") && !questionThreeAnswer.equals("")){
            viewQuestionOne.setVisibility(View.INVISIBLE);
            viewQuestionTwo.setVisibility(View.INVISIBLE);
            viewQuestionThree.setVisibility(View.INVISIBLE);

            View viewStrip = (View)getView().findViewById(R.id.viewStrip);

            viewStrip.setVisibility(View.VISIBLE);
        }
    }
    private void initEvents(){
        int NUMBER_OF_FEATURED_EVENTS = 2;
        LinearLayout linearLayoutEventsSmall = (LinearLayout)getView().findViewById(R.id.linearLayoutEventsSmall);
        ArrayList<Event> arrayListEvents;
        EventAdapterSmall adapterEventsSmall;

        EventDataSource dataSourceEvents = new EventDataSource(getActivity());
        dataSourceEvents.open();
        arrayListEvents = dataSourceEvents.getEvents();
        dataSourceEvents.close();

        adapterEventsSmall = new EventAdapterSmall(getActivity(),arrayListEvents);
        final int adapterCount = adapterEventsSmall.getCount();
        for(int i = 0; i < NUMBER_OF_FEATURED_EVENTS; i++){
            View item = adapterEventsSmall.getView(i, null, null);
            linearLayoutEventsSmall.addView(item);
        }
    }
    private void initPoliticalParties(){
        LinearLayout linearLayoutPoliticalPartiesSmall = (LinearLayout)getView().findViewById(R.id.linearLayoutPoliticalPartiesSmall);
        ArrayList<PoliticalParty> arrayListPoliticalParties;
        PoliticalPartyAdapterSmall adapterPoliticalPartiesSmall;

        PoliticalPartyDataSource dataSource = new PoliticalPartyDataSource(getActivity());
        dataSource.open();
        arrayListPoliticalParties = dataSource.getPoliticalParties();
        dataSource.close();

        adapterPoliticalPartiesSmall = new PoliticalPartyAdapterSmall(getActivity(),arrayListPoliticalParties);
        final int adapterCount = adapterPoliticalPartiesSmall.getCount();
        for(int i = 0; i < adapterCount; i++){
            View item = adapterPoliticalPartiesSmall.getView(i, null, null);
            linearLayoutPoliticalPartiesSmall.addView(item);
        }
    }
    private String updateChartInformation(String candidateName){
        CandidateDataSource ds = new CandidateDataSource(getActivity());
        ds.open();
        currentCandidate = ds.getCandidateByName(candidateName);
        ds.close();
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%.0f%c", currentCandidate.getHuffPercentageOfVoteGeneral(), '%'));
        return sb.toString();
    }

    @Override
    public void onStart() {
        super.onStart();
        //createGraphs();
    }

    @Override
    public void onResume() {
        super.onResume();
        PieChart generalPieChart = (PieChart) getView().findViewById(R.id.chartNational);

        generalPieChart.highlightValue(-1,-1);

        registeredUsersCreated = false;
        totalVotesForAllCandidatesCreated = false;
        averageAgeofVoterCreated = false;
        genderBreakdownCreated = false;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void createGraphs(String dataSource){
        TextView text_view_update_api_2016_general_election = (TextView)getActivity().findViewById(R.id.text_view_update_api_2016_general_election);

        if(dataSource.equalsIgnoreCase("Huff Post")){
            createGraphsPollster();
            text_view_update_api_2016_general_election.setVisibility(View.VISIBLE);
        }
        else if(dataSource.equalsIgnoreCase("Politics Live Users")){
            createGraphsCommunity();
            text_view_update_api_2016_general_election.setVisibility(View.INVISIBLE);
        }
    }
    private void createGraphsPollster() {
        PieChart generalPieChart = (PieChart) getView().findViewById(R.id.chartNational);

        final CandidateDataSource ds = new CandidateDataSource(getActivity());

        ds.open();

        if(ds.getLastCandidateId() < 1) {
            return;
        }
        Map<String, Float> candidatesInGeneral = ds.getGeneralPollsterVotes();
        ds.close();


        final ArrayList<String> generalCandidates = new ArrayList<>();
        ArrayList<Entry> generalVotes = new ArrayList<>();
        int k = 0;
        for (Map.Entry<String, Float> entry : candidatesInGeneral.entrySet()) {
            generalCandidates.add(entry.getKey());
            generalVotes.add(new Entry(entry.getValue(), k));
            k++;
        }


        final int colorBlue1 = ContextCompat.getColor(getActivity(), R.color.colorMotionPressDNC);
        final int colorBlue2 = ContextCompat.getColor(getActivity(), R.color.colorBlueLightish);
        final int colorPrimary = ContextCompat.getColor(getActivity(), R.color.colorPrimary);
        final int colorBlueDark = ContextCompat.getColor(getActivity(), R.color.colorBlueDark);

        int[] democratColors = {colorBlue1,colorBlue2};
        int[] democratColorsPie = {colorBlue1, colorBlueDark};

        final int colorRed1 = ContextCompat.getColor(getActivity(), R.color.colorRed);
        final int colorRed2 = ContextCompat.getColor(getActivity(), R.color.colorMotionPressGOP);
        final int colorRed3 = ContextCompat.getColor(getActivity(), R.color.colorRedLight);
        final int colorRedDark = ContextCompat.getColor(getActivity(), R.color.colorRedDark);


        final int colorMaterial700 = ContextCompat.getColor(getActivity(), R.color.colorMaterialGrey700);
        final int colorMaterial800 = ContextCompat.getColor(getActivity(), R.color.colorMaterialGrey800);
        final int colorMaterial900 = ContextCompat.getColor(getActivity(), R.color.colorMaterialGrey900);
        final int colorWhite = ContextCompat.getColor(getActivity(), R.color.colorWhite);

        Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);

        int[] republicanColors = {colorRed1,colorRed2,colorRed3};
        int[] republicanColorsPie = {colorRedDark,colorRed2,colorRed1};
        int[] bothColors = {colorBlue1, colorBlue2, colorRed1,colorRed2,colorRed3};

        final int colorPurple = ContextCompat.getColor(getActivity(), R.color.colorPurpleDark);

        int[] generalColors = {colorBlue1,colorRed2,colorPurple};
        int[] generalColorsPie = {colorPrimary, colorBlue1,colorRed1};



        PieDataSet generalDataSet = new PieDataSet(generalVotes, "Votes");
        generalDataSet.setColors(generalColorsPie);
        PieData generalData = new PieData(generalCandidates, generalDataSet);
        generalData.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return String.format("%.0f%c", value, '%');
            }
        });
        generalData.setValueTextSize(11f);
        generalData.setValueTypeface(boldTypeface);
        generalData.setValueTextColor(colorWhite);
        generalPieChart.setData(generalData);
        generalPieChart.setDescription("");
        generalPieChart.setHoleRadius(20f);
        generalPieChart.setTransparentCircleRadius(0f);
        generalPieChart.getLegend().setEnabled(false);

        generalPieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                Intent intent = new Intent(getActivity(), CandidateProfile.class);
                ds.open();
                intent.putExtra("candidateId",
                        ds.getCandidateByName(generalCandidates.get(e.getXIndex())).getCandidateID());
                ds.close();
                startActivity(intent);
            }

            @Override
            public void onNothingSelected() {

            }
        });

        generalPieChart.animateXY(2500, 2500);
    }

    private void createGraphsCommunity() {
        PieChart demPieChart = (PieChart) getView().findViewById(R.id.chartNational);
        PieChart repPieChart = (PieChart) getView().findViewById(R.id.chartNational);

        final CandidateDataSource ds = new CandidateDataSource(getActivity());

        ds.open();

        if(ds.getLastCandidateId() < 1) {
            return;
        }
        Map<String, Integer> democrats = ds.getDemocratVotes();
        Map<String, Integer> republicans = ds.getRepublicanVotes();
        ds.close();

        int totalDemocratVotes = democrats.get("TOTAL");
        democrats.remove("TOTAL");
        int totalRepublicanVotes = republicans.get("TOTAL");
        republicans.remove("TOTAL");

        final ArrayList<String> demCandidates = new ArrayList<>();
        ArrayList<Entry> demVotes = new ArrayList<>();
        if (totalDemocratVotes > 0) {
            int i = 0;
            for (Map.Entry<String, Integer> entry : democrats.entrySet()) {
                demCandidates.add(entry.getKey());
                demVotes.add(new Entry(((float) entry.getValue() / (float) totalDemocratVotes) * 100f, i));
                i++;
            }
        }

        final ArrayList<String> repCandidates = new ArrayList<>();
        ArrayList<Entry> repVotes = new ArrayList<>();
        if (totalRepublicanVotes > 0) {
            int i = 0;
            for (Map.Entry<String, Integer> entry : republicans.entrySet()) {
                repCandidates.add(entry.getKey());
                repVotes.add(new Entry(((float) entry.getValue() / (float) totalRepublicanVotes) * 100f, i));
                i++;
            }
        }

        final int colorBlue1 = ContextCompat.getColor(getActivity(), R.color.colorMotionPressDNC);
        final int colorBlue2 = ContextCompat.getColor(getActivity(), R.color.colorBlueLight);
        final int colorPrimary = ContextCompat.getColor(getActivity(), R.color.colorPrimary);
        final int colorBlueDark = ContextCompat.getColor(getActivity(), R.color.colorBlueDark);


        int[] democratColors = {colorBlue1,colorBlue2};
        int[] democratColorsPie = {colorBlue1, colorBlueDark};

        final int colorRed1 = ContextCompat.getColor(getActivity(), R.color.colorRed);
        final int colorRed2 = ContextCompat.getColor(getActivity(), R.color.colorMotionPressGOP);
        final int colorRed3 = ContextCompat.getColor(getActivity(), R.color.colorRedLight);
        final int colorRedDark = ContextCompat.getColor(getActivity(), R.color.colorRedDark);


        final int colorMaterial700 = ContextCompat.getColor(getActivity(), R.color.colorMaterialGrey700);
        final int colorMaterial800 = ContextCompat.getColor(getActivity(), R.color.colorMaterialGrey800);
        final int colorMaterial900 = ContextCompat.getColor(getActivity(), R.color.colorMaterialGrey900);
        final int colorWhite = ContextCompat.getColor(getActivity(), R.color.colorWhite);

        int[] republicanColors = {colorRed1,colorRed2,colorRed3};
        int[] republicanColorsPie = {colorRedDark,colorRed2,colorRed1};


        int[] bothColors = {colorBlue1, colorBlue2, colorRed1,colorRed2,colorRed3};



        PieDataSet repDataSet = new PieDataSet(repVotes, "% of " +totalRepublicanVotes +" Votes");
        //repDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        repDataSet.setColors(republicanColorsPie);
        PieData repData = new PieData(repCandidates, repDataSet);
        repData.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return String.format("%.2f%c", value, '%');
            }
        });



        repData.setValueTextSize(9f);
        Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
        repData.setValueTypeface(boldTypeface);
        repData.setValueTextColor(colorWhite);
        repPieChart.setData(repData);
        repPieChart.setDescription("");
        repPieChart.setHoleRadius(20f);
        repPieChart.setTransparentCircleRadius(0f);
        repPieChart.getLegend().setEnabled(false);

        repPieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                Intent intent = new Intent(getActivity(), CandidateProfile.class);
                ds.open();
                intent.putExtra("candidateId",
                        ds.getCandidateByName(repCandidates.get(e.getXIndex())).getCandidateID());
                ds.close();
                startActivity(intent);
            }

            @Override
            public void onNothingSelected() {

            }
        });

        repPieChart.animateXY(2500, 2500);

        PieDataSet demDataSet = new PieDataSet(demVotes, "% of " +totalDemocratVotes +" Votes");
        //demDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        demDataSet.setColors(democratColorsPie);
        PieData demData = new PieData(demCandidates, demDataSet);
        demData.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return String.format("%.2f%c", value, '%');
            }
        });
        demData.setValueTextSize(9f);
        demData.setValueTypeface(boldTypeface);
        demData.setValueTextColor(colorWhite);
        demPieChart.setData(demData);
        demPieChart.setDescription("");
        demPieChart.setHoleRadius(20f);
        demPieChart.setTransparentCircleRadius(0f);
        demPieChart.getLegend().setEnabled(false);



        demPieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                Intent intent = new Intent(getActivity(), CandidateProfile.class);
                ds.open();
                intent.putExtra("candidateId",
                        ds.getCandidateByName(demCandidates.get(e.getXIndex())).getCandidateID());
                ds.close();
                startActivity(intent);
            }

            @Override
            public void onNothingSelected() {

            }
        });

        demPieChart.animateXY(2500, 2500);
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
        final int colorBlue1 = ContextCompat.getColor(getActivity(), R.color.colorPrimary);
        final int colorBlue2 = ContextCompat.getColor(getActivity(), R.color.colorMotionPressDNC);
        final int colorBlue3 = ContextCompat.getColor(getActivity(), R.color.colorBlueLight);
        final int colorRed1 = ContextCompat.getColor(getActivity(), R.color.colorRedLight);
        final int colorRed2 = ContextCompat.getColor(getActivity(), R.color.colorRed);

        int[] bothColors = {colorBlue1, colorBlue2, colorBlue3,colorRed1,colorRed2};

        d.setColors(bothColors);
        d.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return String.format("%.0f", value);
            }
        });
        d.setValueTextSize(10f);
    }

    /*
    private void createRegisteredUsersGraph() {
        BarChart barChart = (BarChart) getView().findViewById(R.id.chartRegisteredUsers);
        formatBarChart(barChart);
        UserDataSource ds = new UserDataSource(getActivity());
        int gop = 0;
        int dnc = 0;
        int ind = 0;

        ds.open();
        for(User u : ds.getUsers()) {
            if(u.getPartyAffiliation().equals("Democrat")) {
                dnc++;
            } else if (u.getPartyAffiliation().equals("Republican")) {
                gop++;
            } else {
                ind++;
            }
        }
        ds.close();
        ArrayList<String> parties = new ArrayList<>();
        ArrayList<BarEntry> values = new ArrayList<>();
        parties.add("DNC");
        parties.add("GOP");
        parties.add("IND");
        values.add(new BarEntry(dnc, 0));
        values.add(new BarEntry(gop, 1));
        values.add(new BarEntry(ind, 2));
        BarDataSet barDataSet = new BarDataSet(values, "User Affiliation");
        formatBarDataSet(barDataSet);
        BarData barData = new BarData(parties, barDataSet);
        barChart.setData(barData);
        registeredUsersCreated = true;
    }

    private void createTotalVotesForAllCandidatesGraph() {
        BarChart barChart = (BarChart) getView().findViewById(R.id.chartTotalVotesForAllCandidates);
        formatBarChart(barChart);
        CandidateDataSource ds = new CandidateDataSource(getActivity());
        ArrayList<String> candidates = new ArrayList<>();
        ArrayList<BarEntry> votes = new ArrayList<>();
        int i = 0;

        ds.open();
        for(Map.Entry<String, Integer> entry : ds.getRepublicanVotes().entrySet()) {
            if(entry.getKey().equals("TOTAL")) {
                continue;
            }
            candidates.add(entry.getKey().substring(entry.getKey().indexOf(' ')));
            votes.add(new BarEntry(entry.getValue(), i));
            i++;
        }
        for(Map.Entry<String, Integer> entry : ds.getDemocratVotes().entrySet()) {
            if(entry.getKey().equals("TOTAL")) {
                continue;
            }
            candidates.add(entry.getKey().substring(entry.getKey().indexOf(' ')));
            votes.add(new BarEntry(entry.getValue(), i));
            i++;
        }
        ds.close();
        barChart.getXAxis().setTextSize(5f);
        BarDataSet barDataSet = new BarDataSet(votes, "Candidate Votes");
        formatBarDataSet(barDataSet);
        BarData barData = new BarData(candidates, barDataSet);
        barChart.setData(barData);
        totalVotesForAllCandidatesCreated = true;
    }

    private void createAverageAgeOfVoterGraph() {
        BarChart barChart = (BarChart) getView().findViewById(R.id.chartAverageAgeOfVoter);
        formatBarChart(barChart);
        UserDataSource ds = new UserDataSource(getActivity());
        int lessThanTwentyFive = 0;
        int twentyFiveToThrityFive = 0;
        int thirtyFiveToFourtyFive = 0;
        int fourtyFiveToFiftyFive = 0;
        int fiftyFivePlus = 0;

        ds.open();
        for(User u : ds.getUsers()) {
            if (u.getAge() < 25) {
                lessThanTwentyFive++;
                continue;
            }
            if (u.getAge() < 35) {
                twentyFiveToThrityFive++;
                continue;
            }
            if (u.getAge() < 45) {
                thirtyFiveToFourtyFive++;
                continue;
            }
            if (u.getAge() < 55) {
                fourtyFiveToFiftyFive++;
                continue;
            }
            fiftyFivePlus++;
        }
        ds.close();
        ArrayList<String> ages = new ArrayList<>();
        ages.add("<25");
        ages.add("25-34");
        ages.add("35-44");
        ages.add("45-54");
        ages.add("55+");
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(lessThanTwentyFive, 0));
        entries.add(new BarEntry(twentyFiveToThrityFive, 1));
        entries.add(new BarEntry(thirtyFiveToFourtyFive, 2));
        entries.add(new BarEntry(fourtyFiveToFiftyFive, 3));
        entries.add(new BarEntry(fiftyFivePlus, 4));
        BarDataSet barDataSet = new BarDataSet(entries, "ages");
        formatBarDataSet(barDataSet);
        BarData barData = new BarData(ages, barDataSet);
        barChart.setData(barData);
        averageAgeofVoterCreated = true;



    }

    private void createGenderBreakdownGraph() {
        BarChart barChart = (BarChart) getView().findViewById(R.id.chartGenderBreakdown);
        formatBarChart(barChart);
        UserDataSource ds = new UserDataSource(getActivity());
        int male = 0;
        int female = 0;

        ds.open();
        for (User u : ds.getUsers()) {
            if (u.getGender().equals("Male")) {
                male++;
                continue;
            }
            if (u.getGender().equals("Female")) {
                female++;
                continue;
            }
        }
        ds.close();
        ArrayList<String> genders = new ArrayList<>();
        genders.add("Male");
        genders.add("Female");
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(male, 0));
        entries.add(new BarEntry(female, 1));
        BarDataSet barDataSet = new BarDataSet(entries, "genders");
        formatBarDataSet(barDataSet);
        BarData barData = new BarData(genders, barDataSet);
        barChart.setData(barData);
        genderBreakdownCreated = true;
    }
    */
    public interface OnCommunicateActivityListener{
        void passDataToActivity(int nevID);
    }
    /*
    public void inflateVoterDemographics(){
        //LinearLayout linearLayoutDemographicsPoliticsLive = (LinearLayout)getActivity().findViewById(R.id.linearLayoutDemographicsPoliticsLive);
        //linearLayoutDemographicsPoliticsLive.setVisibility(View.VISIBLE);

        UserDataSource userDataSource = new UserDataSource(getActivity());
        userDataSource.open();
        ArrayList<User> arrayListUsers = userDataSource.getUsers();



        int colorfulColor = ContextCompat.getColor(getActivity(), R.color.colorPurple);

        TextView textViewTotalVotes = (TextView) getView().findViewById(R.id.textViewTotalVotesPoliticsLive);
        TextView textViewTotalVotesLabel = (TextView) getView().findViewById(R.id.textViewTotalVotesLabel);
        TextView textViewRegisteredPartyMembers = (TextView) getView().findViewById(R.id.textViewTotalUsersPoliticsLive);
        TextView textViewRegisteredPartyMembersLabel = (TextView)getView().findViewById(R.id.textViewTotalVotersTitleLabel);
        TextView textViewAverageAge = (TextView) getView().findViewById(R.id.textViewAverageAgePoliticsLive);
        TextView textViewAverageAgeLabel = (TextView) getView().findViewById(R.id.textViewGenderLabelCNN);
        TextView textViewMalePercentage = (TextView) getView().findViewById(R.id.textViewMalePoliticsLive);
        TextView textViewFemalePercentage = (TextView) getView().findViewById(R.id.textViewFemalePoliticsLive);

        textViewRegisteredPartyMembersLabel.setText("Registered users = ");
        textViewTotalVotesLabel.setText("Total votes for all candidates = ");
        textViewAverageAgeLabel.setText("Average age of voter = ");

        if(arrayListUsers.size() > 0) {
            textViewRegisteredPartyMembers.setText(Integer.toString(userDataSource.getNumberOfUsers()));
            textViewAverageAge.setText(Integer.toString(userDataSource.getAverageVoterAge()));
            textViewMalePercentage.setText(Integer.toString(userDataSource.getGenderPercentage("Male")));
            textViewFemalePercentage.setText(Integer.toString(userDataSource.getGenderPercentage("Female")));
        }
        else {
            textViewRegisteredPartyMembers.setText("0");
            TextView textViewWarning = (TextView)getView().findViewById(R.id.textViewWarning);
            textViewWarning.setVisibility(View.VISIBLE);
            View viewDemographics = (View)getView().findViewById(R.id.viewDemographics);
            viewDemographics.setVisibility(View.GONE);
        }
        userDataSource.close();

        CandidateDataSource candidateDataSource = new CandidateDataSource(getActivity());
        candidateDataSource.open();
        ArrayList<Candidate> arrayListCandidates = candidateDataSource.getCandidates();
        candidateDataSource.close();

        textViewTotalVotes.setText(Integer.toString(getTotalVotes(arrayListCandidates)));
        //textViewTotalVotes.setBackgroundResource(R.drawable.circle_gop);
        //textViewRegisteredPartyMembers.setBackgroundResource(R.drawable.circle_gop);
        //textViewAverageAge.setBackgroundResource(R.drawable.circle_gop);
        //textViewMalePercentage.setBackgroundResource(R.drawable.circle_gop);
        //textViewFemalePercentage.setBackgroundResource(R.drawable.circle_gop);

        final int colorDown = ContextCompat.getColor(getActivity(), R.color.colorLayoutPressed);
        final int colorDownLight = ContextCompat.getColor(getActivity(), R.color.colorLayoutPressedLight);

        final int colorUp = ContextCompat.getColor(getActivity(), R.color.colorBackgroundGrey);
        final int colorWhite = ContextCompat.getColor(getActivity(), R.color.colorWhite);

        final RelativeLayout relativeLayoutRegisteredParty = (RelativeLayout)getView().findViewById(R.id.relativeLayoutRegisteredParty);
        final RelativeLayout relativeLayoutTotalVotes = (RelativeLayout)getView().findViewById(R.id.relativeLayoutTotalVotes);
        final RelativeLayout relativeLayoutAverageAge = (RelativeLayout)getView().findViewById(R.id.relativeLayoutAverageAge);
        final RelativeLayout relativeLayoutGender = (RelativeLayout)getView().findViewById(R.id.relativeLayoutGenderAlt);


    }*/
    /*
    private void inflateEvents(){
        final RelativeLayout relativeLayoutEvent1 = (RelativeLayout)getActivity().findViewById(R.id.relativeLayoutEvent1);
        final RelativeLayout relativeLayoutEvent2 = (RelativeLayout)getActivity().findViewById(R.id.relativeLayoutEvent2);
        final RelativeLayout relativeLayoutEvent3 = (RelativeLayout)getActivity().findViewById(R.id.relativeLayoutEvent3);

        final RelativeLayout relativeLayoutEvent1Details = (RelativeLayout)getActivity().findViewById(R.id.relativeLayoutEvent1Details);
        final RelativeLayout relativeLayoutEvent2Details = (RelativeLayout)getActivity().findViewById(R.id.relativeLayoutEvent2Details);
        final RelativeLayout relativeLayoutEvent3Details = (RelativeLayout)getActivity().findViewById(R.id.relativeLayoutEvent3Details);

        final ImageView imageViewArrowEvent1 = (ImageView)getActivity().findViewById(R.id.imageViewArrowEvent1);
        final ImageView imageViewArrowEvent2 = (ImageView)getActivity().findViewById(R.id.imageViewArrowEvent2);
        final ImageView imageViewArrowEvent3 = (ImageView)getActivity().findViewById(R.id.imageViewArrowEvent3);

        relativeLayoutEvent1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        relativeLayoutEvent1.setBackgroundResource(colorGrey600);
                        break;
                    case MotionEvent.ACTION_UP:
                        relativeLayoutEvent1.setBackgroundResource(colorGrey700);
                        if(relativeLayoutEvent1Details.getVisibility()==View.GONE){
                            relativeLayoutEvent1Details.setVisibility(View.VISIBLE);

                            imageViewArrowEvent1.setImageResource(R.drawable.up_triangle_white);
                        }
                        else if(relativeLayoutEvent1Details.getVisibility()==View.VISIBLE){
                            relativeLayoutEvent1Details.setVisibility(View.GONE);

                            imageViewArrowEvent1.setImageResource(R.drawable.down_triangle_white);
                        }
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        relativeLayoutEvent1.setBackgroundResource(colorGrey700);
                        ;
                        break;
                }
                return true;
            }
        });
        relativeLayoutEvent2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        relativeLayoutEvent2.setBackgroundResource(colorGrey600);
                        break;
                    case MotionEvent.ACTION_UP:
                        relativeLayoutEvent2.setBackgroundResource(colorGrey700);
                        if(relativeLayoutEvent2Details.getVisibility()==View.GONE){
                            relativeLayoutEvent2Details.setVisibility(View.VISIBLE);

                            imageViewArrowEvent2.setImageResource(R.drawable.up_triangle_white);
                        }
                        else if(relativeLayoutEvent2Details.getVisibility()==View.VISIBLE){
                            relativeLayoutEvent2Details.setVisibility(View.GONE);

                            imageViewArrowEvent2.setImageResource(R.drawable.down_triangle_white);
                        }
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        relativeLayoutEvent2.setBackgroundResource(colorGrey700);
                        break;
                }
                return true;
            }
        });
        relativeLayoutEvent3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        relativeLayoutEvent3.setBackgroundResource(colorGrey600 );
                        break;
                    case MotionEvent.ACTION_UP:
                        relativeLayoutEvent3.setBackgroundResource(colorGrey700);
                        if(relativeLayoutEvent3Details.getVisibility()==View.GONE){
                            relativeLayoutEvent3Details.setVisibility(View.VISIBLE);

                            imageViewArrowEvent3.setImageResource(R.drawable.up_triangle_white);
                        }
                        else if(relativeLayoutEvent3Details.getVisibility()==View.VISIBLE){
                            relativeLayoutEvent3Details.setVisibility(View.GONE);

                            imageViewArrowEvent3.setImageResource(R.drawable.down_triangle_white);
                        }
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        relativeLayoutEvent3.setBackgroundResource(colorGrey700);
                        break;
                }
                return true;
            }
        });

    }
    /*
    private void inflateVoterDemographics(String dataSource){

        TextView textViewElectoratePoliticsLive = (TextView)getActivity().findViewById(R.id.textViewElectoratePoliticsLive);
        TextView textViewTotalRespondentsPoliticsLive = (TextView)getActivity().findViewById(R.id.textViewTotalRespondentsPoliticsLive);
        View cardViewPoliticsLive = (View)getActivity().findViewById(R.id.cardViewPoliticsLive);

        TextView textViewElectorateCNN = (TextView)getActivity().findViewById(R.id.textViewElectorateCNN);
        TextView textViewTotalRespondentsCNN = (TextView)getActivity().findViewById(R.id.textViewTotalRespondentsCNN);
        View cardViewCNN = (View)getActivity().findViewById(R.id.card_cnn);

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
        TextView textViewTotalRespondentsCNN = (TextView)getActivity().findViewById(R.id.textViewTotalRespondentsCNN);
        //textViewTotalRespondentsCNN.setBackground(roundedRectangleLight);


        final RelativeLayout relativeLayoutAgeCNN = (RelativeLayout)getActivity().findViewById(R.id.relativeLayoutAgeCNN);
        final RelativeLayout relativeLayoutGenderCNN = (RelativeLayout)getActivity().findViewById(R.id.relativeLayoutGenderCNN);
        final RelativeLayout relativeLayoutRaceCNN = (RelativeLayout)getActivity().findViewById(R.id.relativeLayoutRaceCNN);
        final RelativeLayout relativeLayoutEducationCNN = (RelativeLayout)getActivity().findViewById(R.id.relativeLayoutEducationCNN);
        final RelativeLayout relativeLayoutIncomeCNN = (RelativeLayout)getActivity().findViewById(R.id.relativeLayoutIncomeCNN);
        final RelativeLayout relativeLayoutMarriageStatusCNN = (RelativeLayout)getActivity().findViewById(R.id.relativeLayoutMarriageStatusCNN);
        final RelativeLayout relativeLayoutReligionCNN = (RelativeLayout)getActivity().findViewById(R.id.relativeLayoutReligionCNN);
        final RelativeLayout relativeLayoutRegionCNN = (RelativeLayout)getActivity().findViewById(R.id.relativeLayoutRegionCNN);

        final RelativeLayout relativeLayoutPartyAffiliationCNN = (RelativeLayout)getActivity().findViewById(R.id.relativeLayoutPartyAffiliationCNN);
        final RelativeLayout relativeLayoutIdealogyCNN = (RelativeLayout)getActivity().findViewById(R.id.relativeLayoutIdealogyCNN);
        final RelativeLayout relativeLayoutVotingHistoryCNN = (RelativeLayout)getActivity().findViewById(R.id.relativeLayoutVotingHistoryCNN);
        final RelativeLayout relativeLayoutExperienceCNN = (RelativeLayout)getActivity().findViewById(R.id.relativeLayoutExperienceCNN);
        final RelativeLayout relativeLayoutFeelingsAboutCongressCNN = (RelativeLayout)getActivity().findViewById(R.id.relativeLayoutFeelingsAboutCongressCNN);
        final RelativeLayout relativeLayoutMostImportantIssueCNN = (RelativeLayout)getActivity().findViewById(R.id.relativeLayoutMostImportantIssueCNN);

        final ImageView imageViewArrowPersonalInformationCNN = (ImageView)getActivity().findViewById(R.id.imageViewArrowPersonalInformationCNN);
        final RelativeLayout relativeLayoutPersonalInformationCNN = (RelativeLayout)getActivity().findViewById(R.id.relativeLayoutPersonalInformationCNN);
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
        final ImageView imageViewArrowPoliticalBackgroundCNN = (ImageView)getActivity().findViewById(R.id.imageViewArrowPoliticalBackgroundCNN);
        final RelativeLayout relativeLayoutPoliticalBackgroundCNN = (RelativeLayout)getActivity().findViewById(R.id.relativeLayoutPoliticalBackgroundCNN);
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
        TextView textViewTotalRespondentsPoliticsLive = (TextView) getActivity().findViewById(R.id.textViewTotalRespondentsPoliticsLive);
        //textViewTotalRespondentsPoliticsLive.setBackground(roundedRectangleLight);

        TextView textViewElectoratePoliticsLive = (TextView) getActivity().findViewById(R.id.textViewElectoratePoliticsLive);

        final ImageView imageViewArrowPersonalInformationPoliticsLive = (ImageView) getActivity().findViewById(R.id.imageViewArrowPersonalInformationPoliticsLive);
        final ImageView imageViewArrowPoliticalBackgroundPoliticsLive = (ImageView) getActivity().findViewById(R.id.imageViewArrowPoliticalBackgroundPoliticsLive);
        final RelativeLayout relativeLayoutRegisteredParty = (RelativeLayout) getActivity().findViewById(R.id.relativeLayoutRegisteredParty);
        final RelativeLayout relativeLayoutTotalVotes = (RelativeLayout) getActivity().findViewById(R.id.relativeLayoutTotalVotes);
        final RelativeLayout relativeLayoutAverageAge = (RelativeLayout) getActivity().findViewById(R.id.relativeLayoutAverageAge);
        final RelativeLayout relativeLayoutGenderAlt = (RelativeLayout) getActivity().findViewById(R.id.relativeLayoutGenderAlt);

        final RelativeLayout relativeLayoutPersonalInformationPoliticsLive = (RelativeLayout) getActivity().findViewById(R.id.relativeLayoutPersonalInformationPoliticsLive);
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
        final RelativeLayout relativeLayoutPoliticalBackgroundPoliticsLive = (RelativeLayout) getActivity().findViewById(R.id.relativeLayoutPoliticalBackgroundPoliticsLive);
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

        UserDataSource userDataSource = new UserDataSource( getActivity());
        userDataSource.open();
        ArrayList arrayListUsers = userDataSource.getUsers();

        TextView textViewRegisteredPartyMembers = (TextView) getActivity().findViewById(R.id.textViewTotalUsersPoliticsLive);
        TextView textViewRegisteredPartyMembersLabel = (TextView) getActivity().findViewById(R.id.textViewTotalVotersTitleLabel);
        TextView textViewTotalVotes = (TextView) getActivity().findViewById(R.id.textViewTotalVotesPoliticsLive);
        TextView textViewTotalVotesLabel = (TextView)  getActivity().findViewById(R.id.textViewTotalVotesLabel);
        TextView textViewAverageAge = (TextView)  getActivity().findViewById(R.id.textViewAverageAgePoliticsLive);
        TextView textViewAverageAgeLabel = (TextView)  getActivity().findViewById(R.id.textViewGenderLabelCNN);
        TextView textViewMalePercentage = (TextView)  getActivity().findViewById(R.id.textViewMalePoliticsLive);
        TextView textViewFemalePercentage = (TextView)  getActivity().findViewById(R.id.textViewFemalePoliticsLive);

        if(arrayListUsers.size() > 0) {
            textViewRegisteredPartyMembers.setText(Integer.toString(userDataSource.getNumberOfUsers()));
            textViewAverageAge.setText(Integer.toString(userDataSource.getAverageVoterAge()));
            textViewMalePercentage.setText(Integer.toString(userDataSource.getGenderPercentage("Male")));
            textViewFemalePercentage.setText(Integer.toString(userDataSource.getGenderPercentage("Female")));
        }
        else {
            textViewRegisteredPartyMembers.setText("0");
            TextView textViewWarning = (TextView)getActivity().findViewById(R.id.textViewWarning);
            textViewWarning.setVisibility(View.VISIBLE);
            TextView textViewWarning2 = (TextView)getActivity().findViewById(R.id.textViewWarning2);
            textViewWarning2.setVisibility(View.VISIBLE);
            View viewDemographics = (View)getActivity().findViewById(R.id.viewDemographics);
            viewDemographics.setVisibility(View.GONE);
            TextView textViewTapToExpand = (TextView)getActivity().findViewById(R.id.textViewTapToExpand);
            textViewTapToExpand.setVisibility(View.GONE);
        }
        CandidateDataSource candidateDataSource = new CandidateDataSource(getActivity());
        candidateDataSource.open();
        ArrayList<Candidate> arrayListCandidates = candidateDataSource.getCandidates();
        candidateDataSource.close();

        textViewRegisteredPartyMembersLabel.setText("Registered party voters = ");
        textViewTotalVotes.setText(Integer.toString(getTotalVotes(arrayListCandidates)));
        textViewTotalVotesLabel.setText("Total votes for party = ");
        textViewAverageAgeLabel.setText("Average age of voter = ");

        final int colorDown = ContextCompat.getColor(getActivity(), R.color.colorLayoutPressed);
        final int colorDownLight = ContextCompat.getColor(getActivity(), R.color.colorLayoutPressedLight);

        final int colorUp = ContextCompat.getColor(getActivity(), R.color.colorBackgroundGrey);
        final int colorWhite = ContextCompat.getColor(getActivity(), R.color.colorWhite);
    } */

    public int getTotalVotes(ArrayList<Candidate> arrayListCandidates){
        int totalVotes = 0;
        for(Candidate c : arrayListCandidates) {
            totalVotes += c.getNumberOfVotes();
        }
        return totalVotes;
    }

    private void shareApp(){
        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Politics Live Download Link");
        String shareMessage = "Politics Live Download Link -> www.politicslive.com";
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareMessage);

        //start the chooser for sharing
        startActivity(Intent.createChooser(shareIntent,
                "Share Link for Download"));
    }
}
