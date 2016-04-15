package com.example.bradleycooper.politicslive;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

//import com.github.mikephil.charting.charts.BarChart;
//import com.github.mikephil.charting.data.BarData;
//import com.github.mikephil.charting.data.BarDataSet;
//import com.github.mikephil.charting.data.BarEntry;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;
import java.util.HashMap;
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

    private OnCommunicateActivityListener activityCommunicator;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private OnFragmentInteractionListener mListener;

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
        activityCommunicator = (OnCommunicateActivityListener)context;

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FloatingActionButton floatingActionButton = ((MainActivity) getActivity()).getFloatingActionButton();
        if(floatingActionButton != null){
            floatingActionButton.show();
        }
        final RelativeLayout relativeLayoutGraphDNC = (RelativeLayout)getView().findViewById(R.id.relativeLayoutGraphDNC);
        final RelativeLayout relativeLayoutGraphGOP = (RelativeLayout)getView().findViewById(R.id.relativeLayoutGraphGOP);

        final int colorDownLight = ContextCompat.getColor(getActivity(), R.color.colorLayoutPressedLight);
        final int colorWhite = ContextCompat.getColor(getActivity(), R.color.colorWhite);

        relativeLayoutGraphDNC.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        relativeLayoutGraphDNC.setBackgroundColor(colorDownLight);
                        break;
                    case MotionEvent.ACTION_UP:
                        Fragment fragment;
                        FragmentManager fragmentManager = getFragmentManager();
                        fragment = new DemocratsList();
                        fragmentManager.beginTransaction()
                                .replace(R.id.container, fragment)
                                .commit();
                        activityCommunicator.passDataToActivity(R.id.nav_DNC);
                        relativeLayoutGraphDNC.setBackgroundColor(colorWhite);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        relativeLayoutGraphDNC.setBackgroundColor(colorWhite);
                        break;
                }
                return true;
            }
        });
        relativeLayoutGraphGOP.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        relativeLayoutGraphGOP.setBackgroundColor(colorDownLight);
                        break;
                    case MotionEvent.ACTION_UP:
                        Fragment fragment;
                        FragmentManager fragmentManager = getFragmentManager();
                        fragment = new RepublicansList();
                        fragmentManager.beginTransaction()
                                .replace(R.id.container, fragment)
                                .commit();
                        activityCommunicator.passDataToActivity(R.id.nav_GOP);
                        relativeLayoutGraphGOP.setBackgroundColor(colorWhite);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        relativeLayoutGraphGOP.setBackgroundColor(colorWhite);
                        break;
                }
                return true;
            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();
        //createGraphs();
    }

    @Override
    public void onResume() {
        super.onResume();
        PieChart demPieChart = (PieChart) getView().findViewById(R.id.chartDem);
        PieChart repPieChart = (PieChart) getView().findViewById(R.id.chartRep);
        demPieChart.highlightValue(-1,-1);
        repPieChart.highlightValue(-1,-1);
        //Not sure if this should be here or in activity created or on start...
        createGraphs();
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


    private void createGraphs() {
        PieChart demPieChart = (PieChart) getView().findViewById(R.id.chartDem);
        PieChart repPieChart = (PieChart) getView().findViewById(R.id.chartRep);
        TextView demChartLabel = (TextView) getView().findViewById(R.id.textViewDemChartLabel);
        TextView repChartLabel = (TextView) getView().findViewById(R.id.textViewRepChartLabel);

        final CandidateDataSource ds = new CandidateDataSource(getActivity());

        ds.open();
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

        PieDataSet repDataSet = new PieDataSet(repVotes, "% of " +totalRepublicanVotes +" Votes");
        repDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData repData = new PieData(repCandidates, repDataSet);
        repData.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return String.format("%.2f%c", value, '%');
            }
        });
        repData.setValueTextSize(8f);
        repPieChart.setData(repData);
        repPieChart.setDescription("");
        repPieChart.setHoleRadius(38f);
        repPieChart.setTransparentCircleRadius(43f);
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

        repPieChart.animateXY(2500,2500);
        repChartLabel.setText("Votes cast = " + totalRepublicanVotes);

        PieDataSet demDataSet = new PieDataSet(demVotes, "% of " +totalDemocratVotes +" Votes");
        demDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        PieData demData = new PieData(demCandidates, demDataSet);
        demData.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return String.format("%.2f%c", value, '%');
            }
        });
        demData.setValueTextSize(8f);
        demPieChart.setData(demData);
        demPieChart.setDescription("");
        demPieChart.setHoleRadius(38f);
        demPieChart.setTransparentCircleRadius(43f);
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

        demPieChart.animateXY(2500,2500);
        demChartLabel.setText("Votes cast = " +totalDemocratVotes);

        inflateVoterDemographics();

    }

    public void createAverageAgeGraph() {

    }

    public void createGenderGraph() {

    }

    public void createRegisteredPartyGraph(){

    }

    public void createTotalVotesForPartyGraph() {
        BarChart totalVotes = (BarChart) getView().findViewById(R.id.chartTotalVotesByParty);
        CandidateDataSource ds = new CandidateDataSource(getActivity());
        int gop = 0;
        int dnc = 0;

        ds.open();
        for(Candidate c : ds.getCandidates()) {
            if(c.getParty().equals("DNC")) {
                dnc += c.getNumberOfVotes();
            } else {
                gop += c.getNumberOfVotes();
            }
        }
        ds.close();
        ArrayList<String> parties = new ArrayList<>();
        ArrayList<BarEntry> values = new ArrayList<>();
        parties.add("DNC");
        parties.add("GOP");
        values.add(new BarEntry(dnc, 0));
        values.add(new BarEntry(gop, 1));
        BarDataSet barDataSet = new BarDataSet(values, "Party Votes");
        BarData barData = new BarData(parties, barDataSet);
        totalVotes.setData(barData);
    }

    public void createVotesForCandidateGraph() {

    }

    public interface OnCommunicateActivityListener{
        void passDataToActivity(int nevID);
    }
    public void inflateVoterDemographics(){
        UserDataSource userDataSource = new UserDataSource(getActivity());
        userDataSource.open();
        ArrayList<User> arrayListUsers = userDataSource.getUsers();



        int colorfulColor = ContextCompat.getColor(getActivity(), R.color.colorPurple);

        TextView textViewTotalVotes = (TextView) getView().findViewById(R.id.textViewTotalVotes);
        TextView textViewTotalVotesLabel = (TextView) getView().findViewById(R.id.textViewTotalVotesLabel);
        TextView textViewRegisteredPartyMembers = (TextView) getView().findViewById(R.id.textViewRegisteredPartyMembers);
        TextView textViewRegisteredPartyMembersLabel = (TextView)getView().findViewById(R.id.textViewRegisteredPartyMembersLabel);
        TextView textViewAverageAge = (TextView) getView().findViewById(R.id.textViewAverageAge);
        TextView textViewAverageAgeLabel = (TextView) getView().findViewById(R.id.textViewAverageAgeLabel);
        TextView textViewMalePercentage = (TextView) getView().findViewById(R.id.textViewMalePercentage);
        TextView textViewFemalePercentage = (TextView) getView().findViewById(R.id.textViewFemalePercentage);

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
                            createTotalVotesForPartyGraph();
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
    public int getTotalVotes(ArrayList<Candidate> arrayListCandidates){
        int totalVotes = 0;
        for(Candidate c : arrayListCandidates) {
            totalVotes += c.getNumberOfVotes();
        }
        return totalVotes;
    }
}
