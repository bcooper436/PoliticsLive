package com.example.bradleycooper.politicslive;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

//import com.github.mikephil.charting.charts.BarChart;
//import com.github.mikephil.charting.data.BarData;
//import com.github.mikephil.charting.data.BarDataSet;
//import com.github.mikephil.charting.data.BarEntry;

import com.github.mikephil.charting.charts.PieChart;
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

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Context thisContext;

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
        thisContext = context;
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

        final CandidateDataSource ds = new CandidateDataSource(thisContext);

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
        repPieChart.setCenterText("Republican\nCandidates");
        repPieChart.setCenterTextSize(10f);
        repPieChart.setHoleRadius(38f);
        repPieChart.setTransparentCircleRadius(43f);
        repPieChart.getLegend().setEnabled(false);

        repPieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                Intent intent = new Intent(thisContext, CandidateProfile.class);
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
        repChartLabel.setText("" +totalRepublicanVotes +" Votes Cast");

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
        demPieChart.setCenterText("Democrat\nCandidates");
        demPieChart.setCenterTextSize(10f);
        demPieChart.setHoleRadius(38f);
        demPieChart.setTransparentCircleRadius(43f);
        demPieChart.getLegend().setEnabled(false);

        demPieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                Intent intent = new Intent(thisContext, CandidateProfile.class);
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
        demChartLabel.setText("" +totalDemocratVotes +" Votes Cast");

    }


}
