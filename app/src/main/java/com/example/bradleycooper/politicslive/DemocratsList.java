package com.example.bradleycooper.politicslive;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DemocratsList.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DemocratsList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DemocratsList extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private int democratColor, colorDown, colorUp;

    private OnCommunicateActivityListener activityCommunicator;

    ArrayList<Candidate> arrayListCandidates;
    CandidateAdapterRanking adapter;
    ArrayList<User> arrayListDemocratUsers;
    UserAdapter adapterUser;

    ArrayList<User> arrayListUsers;

    private boolean registeredUsersCreated = false;
    private boolean totalVotesForAllCandidatesCreated = false;
    private boolean averageAgeofVoterCreated = false;
    private boolean genderBreakdownCreated = false;

    private OnFragmentInteractionListener mListener;

    public DemocratsList() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DemocratsList.
     */
    // TODO: Rename and change types and number of parameters
    public static DemocratsList newInstance(String param1, String param2) {
        DemocratsList fragment = new DemocratsList();
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

        return inflater.inflate(R.layout.fragment_democrats_list, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((MainActivity) getActivity()).hideFloatingActionButton();

        TextView textViewResources = (TextView)getView().findViewById(R.id.textViewResources);
        final TextView textViewOfficalCampaignWebsite = (TextView)getView().findViewById(R.id.textViewChangeVote);
        final TextView textViewEmailCandidate = (TextView)getView().findViewById(R.id.textViewEmailCandidate);
        final TextView textViewTwitterPage = (TextView)getView().findViewById(R.id.textViewTwitterPage);

        colorDown = ContextCompat.getColor(getActivity(), R.color.colorLayoutPressed);
        colorUp = ContextCompat.getColor(getActivity(), R.color.colorBackgroundGrey);


        textViewResources.setText("DEMOCRATIC PARTY RESOURCES");
        textViewOfficalCampaignWebsite.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
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


        inflateLinearLayoutDemocratRanking();
        inflateLinearLayoutRegisteredDemocrats();
        inflateVoterDemographics();
    }

    public void inflateLinearLayoutDemocratRanking(){
        CandidateDataSource dataSource = new CandidateDataSource(getActivity());
        dataSource.open();
        arrayListCandidates = dataSource.getCandidatesInOrderOfVotes("DNC");
        dataSource.close();
        adapter = new CandidateAdapterRanking(getActivity(),arrayListCandidates);
        LinearLayout linearLayoutDemocratRanking = (LinearLayout)getView().findViewById(R.id.linearLayoutDemocratRanking);
        final int adapterCount = adapter.getCount();
        for(int i = 0 ; i < adapterCount ; i++) {
            final int iFinal = i;
            View item = adapter.getView(i, null, null);
            linearLayoutDemocratRanking.addView(item);
        }
    }

    public void inflateLinearLayoutRegisteredDemocrats(){
        UserDataSource userDataSource = new UserDataSource(getActivity());
        userDataSource.open();
        arrayListDemocratUsers = userDataSource.getUsersByParty("Democrat");
        userDataSource.close();
        adapterUser = new UserAdapter(getActivity(), arrayListDemocratUsers);
        LinearLayout linearLayoutRegisteredDemocrats = (LinearLayout)getView().findViewById(R.id.linearLayoutRegisteredDemocrats);
        final int adapterCount2 = adapterUser.getCount();
        for(int i = 0 ; i < adapterCount2 ; i++) {
            final int iFinal = i;
            View item = adapterUser.getView(i, null, null);
            linearLayoutRegisteredDemocrats.addView(item);
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        }
    }

    public void inflateVoterDemographics(){
        UserDataSource userDataSource = new UserDataSource(getActivity());
        userDataSource.open();
        arrayListUsers = userDataSource.getUsersByParty("Democrat");



        democratColor = ContextCompat.getColor(getActivity(), R.color.colorPrimary);

        TextView textViewTotalVotes = (TextView) getView().findViewById(R.id.textViewTotalVotes);
        TextView textViewTotalVotesLabel = (TextView) getView().findViewById(R.id.textViewTotalVotesLabel);
        TextView textViewRegisteredPartyMembers = (TextView) getView().findViewById(R.id.textViewRegisteredPartyMembers);
        TextView textViewRegisteredPartyMembersLabel = (TextView)getView().findViewById(R.id.textViewRegisteredPartyMembersLabel);
        TextView textViewAverageAge = (TextView) getView().findViewById(R.id.textViewAverageAge);
        TextView textViewAverageAgeLabel = (TextView) getView().findViewById(R.id.textViewAverageAgeLabel);
        TextView textViewMalePercentage = (TextView) getView().findViewById(R.id.textViewMalePercentage);
        TextView textViewFemalePercentage = (TextView) getView().findViewById(R.id.textViewFemalePercentage);

        if(arrayListUsers.size() > 0) {
            textViewRegisteredPartyMembers.setText(Integer.toString(userDataSource.getPartyMembers("Democrat")));
            textViewAverageAge.setText(Integer.toString(userDataSource.getAverageVoterAge("Democrat")));
            textViewMalePercentage.setText(Integer.toString(userDataSource.getGenderPercentage("Democrat", "Male")));
            textViewFemalePercentage.setText(Integer.toString(userDataSource.getGenderPercentage("Democrat", "Female")));
        }
        else {
            textViewRegisteredPartyMembers.setText("0");
            TextView textViewWarning = (TextView)getView().findViewById(R.id.textViewWarning);
            textViewWarning.setVisibility(View.VISIBLE);
            TextView textViewWarning2 = (TextView)getView().findViewById(R.id.textViewWarning2);
            textViewWarning2.setVisibility(View.VISIBLE);
            View viewDemographics = (View)getView().findViewById(R.id.viewDemographics);
            viewDemographics.setVisibility(View.GONE);
            TextView textViewTapToExpand = (TextView)getView().findViewById(R.id.textViewTapToExpand);
            textViewTapToExpand.setVisibility(View.GONE);
        }

        textViewTotalVotes.setText(Integer.toString(getTotalVotes(arrayListCandidates)));
        textViewTotalVotes.setBackgroundResource(R.drawable.circle_dnc);
        textViewTotalVotesLabel.setText("Total votes for party = ");
        textViewRegisteredPartyMembers.setBackgroundResource(R.drawable.circle_dnc);
        textViewRegisteredPartyMembersLabel.setText("Registered democratic voters = ");
        textViewAverageAge.setBackgroundResource(R.drawable.circle_dnc);
        textViewAverageAgeLabel.setText("Avgerage age of voter = ");
        textViewMalePercentage.setBackgroundResource(R.drawable.circle_dnc);
        textViewFemalePercentage.setBackgroundResource(R.drawable.circle_dnc);

        final int colorDown = ContextCompat.getColor(getActivity(), R.color.colorLayoutPressed);
        final int colorDownLight = ContextCompat.getColor(getActivity(), R.color.colorLayoutPressedLight);

        final int colorUp = ContextCompat.getColor(getActivity(), R.color.colorBackgroundGrey);
        final int colorWhite = ContextCompat.getColor(getActivity(), R.color.colorWhite);


        final RelativeLayout relativeLayoutBrowseUsers = (RelativeLayout)getView().findViewById(R.id.relativeLayoutBrowseUsers);
        relativeLayoutBrowseUsers.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        relativeLayoutBrowseUsers.setBackgroundColor(colorDown);
                        break;
                    case MotionEvent.ACTION_UP:
                        relativeLayoutBrowseUsers.setBackgroundColor(colorUp);
                        ((MainActivity) getActivity()).onNavigationItemSelected("home");
                        activityCommunicator.passDataToActivity(R.id.nav_home);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        relativeLayoutBrowseUsers.setBackgroundColor(colorUp);
                        break;
                }

                return true;
            }
        });



        final RelativeLayout relativeLayoutRegisteredDemocrats = (RelativeLayout)getView().findViewById(R.id.relativeLayoutRegisteredDemocrats);
        relativeLayoutRegisteredDemocrats.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        relativeLayoutRegisteredDemocrats.setBackgroundColor(colorDown);
                        break;
                    case MotionEvent.ACTION_UP:
                        relativeLayoutRegisteredDemocrats.setBackgroundColor(colorUp);
                        ((MainActivity) getActivity()).onNavigationItemSelected("userslist");
                        activityCommunicator.passDataToActivity(R.id.nav_users_list);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        relativeLayoutRegisteredDemocrats.setBackgroundColor(colorUp);
                        break;
                }

                return true;
            }
        });


        final RelativeLayout relativeLayoutRegisteredParty = (RelativeLayout)getView().findViewById(R.id.relativeLayoutRegisteredParty);
        final RelativeLayout relativeLayoutRegisteredPartyExtended = (RelativeLayout)getView().findViewById(R.id.relativeLayoutRegisteredPartyExtended);
        final RelativeLayout relativeLayoutTotalVotes = (RelativeLayout)getView().findViewById(R.id.relativeLayoutTotalVotes);
        final RelativeLayout relativeLayoutTotalVotesExtended = (RelativeLayout)getView().findViewById(R.id.relativeLayoutTotalVotesExtended);
        final RelativeLayout relativeLayoutAverageAge = (RelativeLayout)getView().findViewById(R.id.relativeLayoutAverageAge);
        final RelativeLayout relativeLayoutAverageAgeExtended = (RelativeLayout)getView().findViewById(R.id.relativeLayoutAverageAgeExtended);
        final RelativeLayout relativeLayoutGender = (RelativeLayout)getView().findViewById(R.id.relativeLayoutGender);
        final RelativeLayout relativeLayoutGenderExtended = (RelativeLayout)getView().findViewById(R.id.relativeLayoutGenderExtended);

        relativeLayoutRegisteredParty.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        relativeLayoutRegisteredParty.setBackgroundColor(colorDownLight);
                        break;
                    case MotionEvent.ACTION_UP:
                        if(relativeLayoutRegisteredPartyExtended.getVisibility() == View.VISIBLE) {
                            makeAllExtendedInvisible("");
                        }
                        else {
                            makeAllExtendedInvisible("relativeLayoutRegisteredPartyExtended");
                            if(!registeredUsersCreated) {
                                createRegisteredUsersGraph();
                            }
                        }
                        relativeLayoutRegisteredParty.setBackgroundColor(colorWhite);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        relativeLayoutRegisteredParty.setBackgroundColor(colorWhite);
                        break;
                }
                return true;
            }
        });
        relativeLayoutTotalVotes.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        relativeLayoutTotalVotes.setBackgroundColor(colorDownLight);
                        break;
                    case MotionEvent.ACTION_UP:
                        if(relativeLayoutTotalVotesExtended.getVisibility() == View.VISIBLE) {
                            makeAllExtendedInvisible("");
                        }
                        else {
                            makeAllExtendedInvisible("relativeLayoutTotalVotesExtended");
                            if(!totalVotesForAllCandidatesCreated) {
                                createTotalVotesForAllCandidatesGraph();
                            }
                        }
                        relativeLayoutTotalVotes.setBackgroundColor(colorWhite);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        relativeLayoutTotalVotes.setBackgroundColor(colorWhite);
                        break;
                }
                return true;
            }
        });
        relativeLayoutAverageAge.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        relativeLayoutAverageAge.setBackgroundColor(colorDownLight);
                        break;
                    case MotionEvent.ACTION_UP:
                        if(relativeLayoutAverageAgeExtended.getVisibility() == View.VISIBLE) {
                            makeAllExtendedInvisible("");
                        }
                        else {
                            makeAllExtendedInvisible("relativeLayoutAverageAgeExtended");
                            if(!averageAgeofVoterCreated) {
                                createAverageAgeOfVoterGraph();
                            }
                        }
                        relativeLayoutAverageAge.setBackgroundColor(colorWhite);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        relativeLayoutAverageAge.setBackgroundColor(colorWhite);
                        break;
                }
                return true;
            }
        });
        relativeLayoutGender.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        relativeLayoutGender.setBackgroundColor(colorDownLight);
                        break;
                    case MotionEvent.ACTION_UP:
                        if(relativeLayoutGenderExtended.getVisibility() == View.VISIBLE) {
                            makeAllExtendedInvisible("");
                        }
                        else {
                            makeAllExtendedInvisible("relativeLayoutGenderExtended");
                            if(!genderBreakdownCreated) {
                                createGenderBreakdownGraph();
                            }
                        }
                        relativeLayoutGender.setBackgroundColor(colorWhite);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        relativeLayoutGender.setBackgroundColor(colorWhite);
                        break;
                }
                return true;
            }
        });
    }

    public void makeAllExtendedInvisible(String relativeLayoutName){
        RelativeLayout relativeLayoutTotalVotesExtended = (RelativeLayout)getView().findViewById(R.id.relativeLayoutTotalVotesExtended);
        relativeLayoutTotalVotesExtended.setVisibility(View.GONE);
        RelativeLayout relativeLayoutAverageAgeExtended = (RelativeLayout)getView().findViewById(R.id.relativeLayoutAverageAgeExtended);
        relativeLayoutAverageAgeExtended.setVisibility(View.GONE);
        RelativeLayout relativeLayoutGenderExtended = (RelativeLayout)getView().findViewById(R.id.relativeLayoutGenderExtended);
        relativeLayoutGenderExtended.setVisibility(View.GONE);
        RelativeLayout relativeLayoutRegisteredPartyExtended = (RelativeLayout)getView().findViewById(R.id.relativeLayoutRegisteredPartyExtended);
        relativeLayoutRegisteredPartyExtended.setVisibility(View.GONE);

        switch(relativeLayoutName){
            case "relativeLayoutTotalVotesExtended":
                relativeLayoutTotalVotesExtended.setVisibility(View.VISIBLE);
                break;
            case "relativeLayoutAverageAgeExtended":
                relativeLayoutAverageAgeExtended.setVisibility(View.VISIBLE);
                break;
            case "relativeLayoutGenderExtended":
                relativeLayoutGenderExtended.setVisibility(View.VISIBLE);
                break;
            case "relativeLayoutRegisteredPartyExtended":
                relativeLayoutRegisteredPartyExtended.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
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
        b.animateXY(1000,1000);
    }

    private void formatBarDataSet(BarDataSet d) {
        d.setColors(ColorTemplate.COLORFUL_COLORS);
        d.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return String.format("%.0f", value);
            }
        });
        d.setValueTextSize(10f);
    }

    private void createRegisteredUsersGraph() {
        BarChart barChart = (BarChart) getView().findViewById(R.id.chartRegisteredUsers);
        formatBarChart(barChart);
        UserDataSource ds = new UserDataSource(getActivity());
        ArrayList<String> parties = new ArrayList<>();
        ArrayList<BarEntry> values = new ArrayList<>();
        parties.add("DNC");
        ds.open();
        values.add(new BarEntry(getNumberOfRegisteredDemocrats(ds.getUsersByParty("Democrat")), 0));
        ds.close();
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
            if (!u.getPartyAffiliation().equals("Democrat")) {
                continue;
            }
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
        int other = 0;

        ds.open();
        for (User u : ds.getUsers()) {
            if (!u.getPartyAffiliation().equals("Democrat")) {
                continue;
            }
            if (u.getGender().equals("Male")) {
                male++;
                continue;
            }
            if (u.getGender().equals("Female")) {
                female++;
                continue;
            }
            other++;
        }
        ds.close();
        ArrayList<String> genders = new ArrayList<>();
        genders.add("Male");
        genders.add("Female");
        genders.add("Undisclosed");
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(male, 0));
        entries.add(new BarEntry(female, 1));
        entries.add(new BarEntry(other, 2));
        BarDataSet barDataSet = new BarDataSet(entries, "genders");
        formatBarDataSet(barDataSet);
        BarData barData = new BarData(genders, barDataSet);
        barChart.setData(barData);
        genderBreakdownCreated = true;
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

        activityCommunicator = (OnCommunicateActivityListener)getActivity();
    }
    public int getTotalVotes(ArrayList<Candidate> arrayListCandidates){
        int totalVotes = 0;
        for(Candidate c : arrayListCandidates) {
            totalVotes += c.getNumberOfVotes();
        }
        return totalVotes;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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

    public int getNumberOfRegisteredDemocrats(ArrayList<User> arrayListUsers) {
        int registered = 0;
        for(User u : arrayListUsers) {
            registered++;
        }
        return registered;
    }

    public interface OnCommunicateActivityListener{
        void passDataToActivity(int nevID);
    }

}
