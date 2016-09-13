package com.example.bradleycooper.politicslive;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListOfPolls.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListOfPolls#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListOfPolls extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Candidate currentCandidate;
    private OnFragmentInteractionListener mListener;

    public ListOfPolls() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListOfPolls.
     */
    // TODO: Rename and change types and number of parameters
    public static ListOfPolls newInstance(String param1, String param2) {
        ListOfPolls fragment = new ListOfPolls();
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
        return inflater.inflate(R.layout.fragment_list_of_polls, container, false);
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
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView text_view_update_api_2016_national_gop_primary = (TextView)getActivity().findViewById(R.id.text_view_update_api_2016_national_gop_primary);
        TextView text_view_update_api_2016_national_democratic_primary = (TextView)getActivity().findViewById(R.id.text_view_update_api_2016_national_democratic_primary);
        TextView text_view_update_api_2016_general_election = (TextView)getActivity().findViewById(R.id.text_view_update_api_2016_general_election);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String LAST_UPDATED_API_2016_NATIONAL_GOP_PRIMARY = preferences.getString("API_2016_NATIONAL_GOP_PRIMARY", "");
        String LAST_UPDATED_API_2016_NATIONAL_DEMOCRATIC_PRIMARY = preferences.getString("API_2016_NATIONAL_DEMOCRATIC_PRIMARY", "");
        String LAST_UPDATED_API_2016_GENERAL_ELECTION = preferences.getString("API_2016_GENERAL_ELECTION", "");

        if(LAST_UPDATED_API_2016_NATIONAL_DEMOCRATIC_PRIMARY != null) {
            text_view_update_api_2016_national_democratic_primary.setText(LAST_UPDATED_API_2016_NATIONAL_DEMOCRATIC_PRIMARY);
        }
        if(LAST_UPDATED_API_2016_NATIONAL_GOP_PRIMARY != null) {
            text_view_update_api_2016_national_gop_primary.setText(LAST_UPDATED_API_2016_NATIONAL_GOP_PRIMARY);
        }
        if(LAST_UPDATED_API_2016_NATIONAL_GOP_PRIMARY != null) {
            text_view_update_api_2016_general_election.setText(LAST_UPDATED_API_2016_GENERAL_ELECTION);
        }
        PieChart demPieChart = (PieChart) getView().findViewById(R.id.chartDNC);
        PieChart repPieChart = (PieChart) getView().findViewById(R.id.chartGOP);
        PieChart generalPieChart = (PieChart) getView().findViewById(R.id.chartNational);
        Spinner spinnerDataGOP = (Spinner) getView().findViewById(R.id.spinnerDataGOP);
        ArrayAdapter<CharSequence> adapterFilterGOP = ArrayAdapter.createFromResource(getActivity(), R.array.data_array, R.layout.spinner_item_2);
        adapterFilterGOP.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDataGOP.setAdapter(adapterFilterGOP);

        spinnerDataGOP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                createGraphs(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        Spinner spinnerDataDNC = (Spinner) getView().findViewById(R.id.spinnerDataDNC);
        ArrayAdapter<CharSequence> adapterFilterDNC = ArrayAdapter.createFromResource(getActivity(), R.array.data_array, R.layout.spinner_item_2);
        adapterFilterDNC.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDataDNC.setAdapter(adapterFilterDNC);

        spinnerDataDNC.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                createGraphs(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
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
    private String updateChartInformation(String candidateName){
        CandidateDataSource ds = new CandidateDataSource(getActivity());
        ds.open();
        currentCandidate = ds.getCandidateByName(candidateName);
        ds.close();
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%.0f%c", currentCandidate.getHuffPercentageOfVoteGeneral(), '%'));
        return sb.toString();
    }
    private void createGraphs(String dataSource){
        TextView text_view_update_api_2016_national_gop_primary = (TextView)getActivity().findViewById(R.id.text_view_update_api_2016_national_gop_primary);
        TextView text_view_update_api_2016_national_democratic_primary = (TextView)getActivity().findViewById(R.id.text_view_update_api_2016_national_democratic_primary);
        TextView text_view_update_api_2016_general_election = (TextView)getActivity().findViewById(R.id.text_view_update_api_2016_general_election);

        if(dataSource.equalsIgnoreCase("HuffPOST POLLSTER")){
            createGraphsPollster();
            text_view_update_api_2016_national_gop_primary.setVisibility(View.VISIBLE);
            text_view_update_api_2016_national_democratic_primary.setVisibility(View.VISIBLE);
            text_view_update_api_2016_general_election.setVisibility(View.VISIBLE);
        }
        else if(dataSource.equalsIgnoreCase("Politics Live Users")){
            createGraphsCommunity();
            text_view_update_api_2016_national_gop_primary.setVisibility(View.INVISIBLE);
            text_view_update_api_2016_national_democratic_primary.setVisibility(View.INVISIBLE);
            text_view_update_api_2016_general_election.setVisibility(View.INVISIBLE);
        }
    }
    private void createGraphsPollster() {
        PieChart demPieChart = (PieChart) getView().findViewById(R.id.chartDNC);
        PieChart repPieChart = (PieChart) getView().findViewById(R.id.chartGOP);
        PieChart generalPieChart = (PieChart) getView().findViewById(R.id.chartNational);

        final CandidateDataSource ds = new CandidateDataSource(getActivity());

        ds.open();

        if(ds.getLastCandidateId() < 1) {
            return;
        }
        Map<String, Float> democrats = ds.getDemocratPollsterVotes();
        Map<String, Float> republicans = ds.getRepublicanPollsterVotes();
        Map<String, Float> candidatesInGeneral = ds.getGeneralPollsterVotes();
        ds.close();


        final ArrayList<String> demCandidates = new ArrayList<>();
        ArrayList<Entry> demVotes = new ArrayList<>();
        int i = 0;
        for (Map.Entry<String, Float> entry : democrats.entrySet()) {
            demCandidates.add(entry.getKey());
            demVotes.add(new Entry(entry.getValue(), i));

            i++;
        }


        final ArrayList<String> repCandidates = new ArrayList<>();
        ArrayList<Entry> repVotes = new ArrayList<>();
        int j = 0;
        for (Map.Entry<String, Float> entry : republicans.entrySet()) {
            repCandidates.add(entry.getKey());
            repVotes.add(new Entry(entry.getValue(), j));
            j++;
        }

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

        PieDataSet repDataSet = new PieDataSet(repVotes, "Votes");
        //repDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        repDataSet.setColors(republicanColorsPie);
        PieData repData = new PieData(repCandidates, repDataSet);
        repData.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return String.format("%.1f%c", value, '%');
            }
        });
        repData.setValueTextSize(9f);
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

        PieDataSet demDataSet = new PieDataSet(demVotes, "Votes");
        demDataSet.setColors(democratColorsPie);
        PieData demData = new PieData(demCandidates, demDataSet);
        demData.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return String.format("%.1f%c", value, '%');
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

}
