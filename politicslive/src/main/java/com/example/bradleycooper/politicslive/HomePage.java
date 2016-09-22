package com.example.bradleycooper.politicslive;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ViewPortHandler;
import java.util.ArrayList;
import java.util.Map;


public class HomePage extends Fragment {
    private OnFragmentInteractionListener mListener;


    private Candidate currentCandidate;

    public HomePage() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_page, container, false);
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
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    @Override
    public void onStart() {
        super.onStart();
    }
    @Override
    public void onResume() {
        super.onResume();
        PieChart generalPieChart = (PieChart) getView().findViewById(R.id.chartNational);

        generalPieChart.highlightValue(-1,-1);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        /* Inflate all card views on home page */
        initClickableLayouts();
        initPoliticalParties();
        initEvents();
        initCandidates();
        createGraphsPollster();
    }

    private void initClickableLayouts(){
        /* Make the title area for each card view on the home page CLICKABLE to redirect to appropriate activity */
        final RelativeLayout relativeLayoutFeaturedPolls = (RelativeLayout)getView().findViewById(R.id.relativeLayoutFeaturedPolls);
        final RelativeLayout relativeLayoutPoliticalParties = (RelativeLayout)getView().findViewById(R.id.relativeLayoutPoliticalParties);
        final RelativeLayout relativeLayoutUpcomingEvents = (RelativeLayout)getView().findViewById(R.id.relativeLayoutUpcomingEvents);
        final RelativeLayout relativeLayoutCandidates = (RelativeLayout)getView().findViewById(R.id.relativeLayoutCandidates);

        final int colorDown = ContextCompat.getColor(getActivity(), R.color.colorLayoutPressed);
        final int colorUp = ContextCompat.getColor(getActivity(), R.color.colorBackgroundGrey);

        relativeLayoutFeaturedPolls.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        relativeLayoutFeaturedPolls.setBackgroundColor(colorDown);
                        break;
                    case MotionEvent.ACTION_UP:
                        relativeLayoutFeaturedPolls.setBackgroundColor(colorUp);
                        Intent intent = new Intent(getActivity(), AllPolls.class);
                        intent.putExtra("justupdated", "no");
                        startActivity(intent);
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
                        Intent intent = new Intent(getActivity(), AllPoliticalParties.class);
                        startActivity(intent);
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
                        Intent intent = new Intent(getActivity(), AllEvents.class);
                        startActivity(intent);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        relativeLayoutUpcomingEvents.setBackgroundColor(colorUp);
                        break;
                }
                return true;
            }
        });
        relativeLayoutCandidates.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        relativeLayoutCandidates.setBackgroundColor(colorDown);
                        break;
                    case MotionEvent.ACTION_UP:
                        relativeLayoutCandidates.setBackgroundColor(colorUp);
                        Intent intent = new Intent(getActivity(), AllPresidentialCandidates.class);
                        startActivity(intent);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        relativeLayoutCandidates.setBackgroundColor(colorUp);
                        break;
                }
                return true;
            }
        });

        /* Initialize Share button at the bottom of the homepage */
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
                        shareApp();
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        buttonShare.setBackgroundResource(R.drawable.button_border);
                        break;
                }
                return true;
            }
        });
    }

    private void initCandidates(){
        LinearLayout linearLayoutCandidates = (LinearLayout)getView().findViewById(R.id.linearLayoutCandidates);
        ArrayList<Candidate> arrayListCandidates;
        CandidateAdapterOther candidateAdapterOther;

        CandidateDataSource dataSourceCandidates = new CandidateDataSource(getActivity());
        dataSourceCandidates.open();
        arrayListCandidates = dataSourceCandidates.getCandidates();
        dataSourceCandidates.close();

        candidateAdapterOther = new CandidateAdapterOther(getActivity(),arrayListCandidates);
        final int adapterCount = candidateAdapterOther.getCount();
        for(int i = 0; i < adapterCount; i++){
            View item = candidateAdapterOther.getView(i, null, null);
            linearLayoutCandidates.addView(item);
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
        final int colorPrimary = ContextCompat.getColor(getActivity(), R.color.colorPrimary);
        final int colorRed1 = ContextCompat.getColor(getActivity(), R.color.colorRed);
        final int colorWhite = ContextCompat.getColor(getActivity(), R.color.colorWhite);
        int[] generalColorsPie = {colorPrimary, colorBlue1,colorRed1};

        Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);

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

        /* Initialize values in card view containing graph for each candidate's voting percentage */
        TextView textViewHillaryVotesGeneral = (TextView)getActivity().findViewById(R.id.textViewHillaryVotesGeneral);
        textViewHillaryVotesGeneral.setText(updateChartInformation("Hillary Clinton"));
        TextView textViewTrumpVotesGeneral = (TextView)getActivity().findViewById(R.id.textViewTrumpVotesGeneral);
        textViewTrumpVotesGeneral.setText(updateChartInformation("Donald Trump"));
        TextView textViewGaryVotesGeneral = (TextView)getActivity().findViewById(R.id.textViewGaryVotesGeneral);
        textViewGaryVotesGeneral.setText(updateChartInformation("Gary Johnson"));

        /* Make candidate images clickable and redirect to CandidateProfile activity */
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

        /* Populate "last_updated" textView */
        TextView text_view_update_api_2016_general_election = (TextView)getActivity().findViewById(R.id.text_view_update_api_2016_general_election);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String LAST_UPDATED_API_2016_GENERAL_ELECTION = preferences.getString("API_2016_GENERAL_ELECTION", "");
        if(LAST_UPDATED_API_2016_GENERAL_ELECTION != null) {
            text_view_update_api_2016_general_election.setText(LAST_UPDATED_API_2016_GENERAL_ELECTION);
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


    private void shareApp(){
        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Politics Live Download Link");
        String shareMessage = "Politics Live Download Link -> https://play.google.com/store/apps/details?id=com.tid.politicslive&hl=en";
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareMessage);

        //start the chooser for sharing
        startActivity(Intent.createChooser(shareIntent,
                "Share Link for Download"));
    }
}
