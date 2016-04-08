package com.example.bradleycooper.politicslive;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

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
    }

    @Override
    public void onResume() {
        super.onResume();
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
        BarChart demBarChart = (BarChart) getView().findViewById(R.id.chartDem);
        BarChart repBarChart = (BarChart) getView().findViewById(R.id.chartRep);

        CandidateDataSource ds = new CandidateDataSource(thisContext);
        int totalDemocratVotes;
        int totalRepublicanVotes;

        ds.open();
        Map<String, Integer> democrats = ds.getDemocratVotes();
        Map<String, Integer> republicans = ds.getRepublicanVotes();
        ds.close();

        totalDemocratVotes = democrats.get("TOTAL");
        democrats.remove("TOTAL");
        totalRepublicanVotes = republicans.get("TOTAL");
        republicans.remove("TOTAL");

        ArrayList<String> demCandidates = new ArrayList<>();
        ArrayList<BarEntry> demVotes = new ArrayList<>();
        if (totalDemocratVotes > 0) {
            int i = 0;
            for (Map.Entry<String, Integer> entry : democrats.entrySet()) {
                demCandidates.add(entry.getKey());
                demVotes.add(new BarEntry((float) entry.getValue() / (float) totalDemocratVotes, i));
                i++;
            }
        }

        ArrayList<String> repCandidates = new ArrayList<>();
        ArrayList<BarEntry> repVotes = new ArrayList<>();
        if (totalRepublicanVotes > 0) {
            int i = 0;
            for (Map.Entry<String, Integer> entry : republicans.entrySet()) {
                repCandidates.add(entry.getKey());
                repVotes.add(new BarEntry((float) entry.getValue() / (float) totalRepublicanVotes, i));
                i++;
            }
        }

        BarDataSet repDataSet = new BarDataSet(repVotes, "Percent of Votes");
        BarData repData = new BarData(repCandidates, repDataSet);
        repBarChart.setData(repData);

        BarDataSet demDataSet = new BarDataSet(demVotes, "Percent of Votes");
        BarData demData = new BarData(demCandidates, demDataSet);
        demBarChart.setData(demData);

    }


}
