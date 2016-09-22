package com.example.bradleycooper.politicslive;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.IntegerRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ViewPortHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class AllPolls extends AppCompatActivity {
    String[][] arrayOfVoterPercentages;
    String[][] arrayFavorable;

    public int NUMBER_OF_DATABASES_TO_POPULATE = 1;

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
            API_2016_GENERAL_ELECTION_DONE = false;



    ProgressDialog progressBar;
    int progressBarProgress = 0;
    Candidate candidate;
    CandidateDataSource ds;
    Candidate candidateToUpdate;

    private Candidate currentCandidate;
    /* Fields for initializing the sqllite database */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_polls);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            final int primaryDark = ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark);
            window.setStatusBarColor(primaryDark);
        }
        Bundle extras = getIntent().getExtras();
        if(extras.getString("justupdated").equalsIgnoreCase("yes")){
            Context context = getApplicationContext();
            CharSequence text = "Success! The polls have been updated";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

        }
        API_2016_NATIONAL_GOP_PRIMARY =getResources().getString(R.string.api_2016_national_gop_primary);
        API_2016_NATIONAL_DEMOCRATIC_PRIMARY = getResources().getString(R.string.api_2016_national_democratic_primary);
        API_2016_GENERAL_ELECTION = getResources().getString(R.string.api_2016_general_election);

        TextView text_view_update_api_2016_national_gop_primary = (TextView)findViewById(R.id.text_view_update_api_2016_national_gop_primary);
        TextView text_view_update_api_2016_national_democratic_primary = (TextView)findViewById(R.id.text_view_update_api_2016_national_democratic_primary);
        TextView text_view_update_api_2016_general_election = (TextView)findViewById(R.id.text_view_update_api_2016_general_election);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(AllPolls.this);
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
        PieChart demPieChart = (PieChart)findViewById(R.id.chartDNC);
        PieChart repPieChart = (PieChart)findViewById(R.id.chartGOP);
        PieChart generalPieChart = (PieChart)findViewById(R.id.chartNational);
        Spinner spinnerDataGOP = (Spinner)findViewById(R.id.spinnerDataGOP);
        ArrayAdapter<CharSequence> adapterFilterGOP = ArrayAdapter.createFromResource(AllPolls.this,R.array.data_array, R.layout.spinner_item_2);
        adapterFilterGOP.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDataGOP.setAdapter(adapterFilterGOP);


        final int buttonNormal = ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary);
        final int buttonDown = ContextCompat.getColor(getApplicationContext(), R.color.colorBlueLight);

        spinnerDataGOP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                createGraphs(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        Spinner spinnerDataDNC = (Spinner)findViewById(R.id.spinnerDataDNC);
        ArrayAdapter<CharSequence> adapterFilterDNC = ArrayAdapter.createFromResource(AllPolls.this, R.array.data_array, R.layout.spinner_item_2);
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
        createGraphs("HuffPOST POLLSTER");

        Button buttonUpdate = (Button)findViewById(R.id.buttonUpdatePolls);

        buttonUpdate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                progressBar = new ProgressDialog(AllPolls.this);
                progressBar.setCancelable(false);
                progressBar.setMessage("Updating polls from HuffPost Pollster API...");
                progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressBar.setProgress(0);
                progressBar.setMax(NUMBER_OF_DATABASES_TO_POPULATE);
                Drawable customDrawable = (Drawable) getResources().getDrawable(R.drawable.custom_progressbar);
                progressBar.setProgressDrawable(customDrawable);
                progressBar.show();


                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        loadAPIs();
                    }
                }, 1);

                loadAPIs();
                return false;
            }
        });

        TextView textViewHillaryVotesGeneral = (TextView)findViewById(R.id.textViewHillaryVotesGeneral);
        textViewHillaryVotesGeneral.setText(updateChartInformation("Hillary Clinton"));

        TextView textViewTrumpVotesGeneral = (TextView)findViewById(R.id.textViewTrumpVotesGeneral);
        textViewTrumpVotesGeneral.setText(updateChartInformation("Donald Trump"));

        TextView textViewGaryVotesGeneral = (TextView)findViewById(R.id.textViewGaryVotesGeneral);
        textViewGaryVotesGeneral.setText(updateChartInformation("Gary Johnson"));

    }
    private String updateChartInformation(String candidateName){
        CandidateDataSource ds = new CandidateDataSource(AllPolls.this);
        ds.open();
        currentCandidate = ds.getCandidateByName(candidateName);
        ds.close();
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%.0f%c", currentCandidate.getHuffPercentageOfVoteGeneral(), '%'));
        return sb.toString();
    }
    private void loadAPIs(){
        /*if(!API_2016_NATIONAL_GOP_PRIMARY_DONE){
            currentAPI = API_2016_NATIONAL_GOP_PRIMARY;
            currentAPILabel = "API_2016_NATIONAL_GOP_PRIMARY";
            API_2016_GENERAL_ELECTION_DONE = true;
            new RetrieveFeedTask().execute();
        }
        else if(!API_2016_GENERAL_ELECTION_DONE) {
            currentAPI = API_2016_GENERAL_ELECTION;
            currentAPILabel = "API_2016_GENERAL_ELECTION";
            API_2016_GENERAL_ELECTION_DONE = true;
            new RetrieveFeedTask().execute();
        }
        else if(!API_2016_NATIONAL_DEMOCRATIC_PRIMARY_DONE) {
            currentAPI = API_2016_NATIONAL_DEMOCRATIC_PRIMARY;
            currentAPILabel = "API_2016_NATIONAL_DEMOCRATIC_PRIMARY";
            API_2016_NATIONAL_DEMOCRATIC_PRIMARY_DONE = true;
            new RetrieveFeedTask().execute();
        }*/
        if(!API_2016_GENERAL_ELECTION_DONE) {
            currentAPI = API_2016_GENERAL_ELECTION;
            currentAPILabel = "API_2016_GENERAL_ELECTION";
            API_2016_GENERAL_ELECTION_DONE = true;
            new RetrieveFeedTask().execute();
        }
    }
    class RetrieveFeedTask extends AsyncTask<Void, Void, String> {
        private Exception exception;
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
            String pollTitle, pollValues, lastUpdated;
            JSONArray jr;

            try {
                switch(currentAPILabel){
                    case "API_2016_NATIONAL_GOP_PRIMARY":
                        arrayOfVoterPercentages = new String[5][2];
                        API_2016_NATIONAL_GOP_PRIMARY_DONE = true;

                        object = (JSONObject) new JSONTokener(response).nextValue();
                        pollTitle = object.getString("title");
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
                                ds = new CandidateDataSource(AllPolls.this);
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
                        pollTitle = object.getString("title");
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
                                CandidateDataSource ds = new CandidateDataSource(AllPolls.this);
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
                        API_2016_NATIONAL_DEMOCRATIC_PRIMARY_DONE = true;

                        object = (JSONObject) new JSONTokener(response).nextValue();
                        pollTitle = object.getString("title");
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
                                CandidateDataSource ds1 = new CandidateDataSource(AllPolls.this);
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
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
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
        hourCalc = Integer.parseInt(hour);

        String minutes = timeOfLastUpdate.substring(14,16);

        String finalTimeOfLastUpdate = "Updated at " + hour + ":" + minutes  + " " + AMOrPM + " on " + month + "/" + day + "/" + year;

        SharedPreferences preferencesNo = PreferenceManager.getDefaultSharedPreferences(AllPolls.this);
        SharedPreferences.Editor editor = preferencesNo.edit();
        editor.putString(apiUpdateLabel, finalTimeOfLastUpdate);
        editor.apply();

        progressBarProgress++;
        progressBar.setProgress(progressBarProgress);


        if(progressBarProgress >= NUMBER_OF_DATABASES_TO_POPULATE) {
            Intent intent = new Intent(AllPolls.this, AllPolls.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("justupdated", "yes");
            startActivity(intent);

        }
        /*else {
            loadAPIs();
        }*/
    }

    private void createGraphs(String dataSource){
        TextView text_view_update_api_2016_national_gop_primary = (TextView)findViewById(R.id.text_view_update_api_2016_national_gop_primary);
        TextView text_view_update_api_2016_national_democratic_primary = (TextView)findViewById(R.id.text_view_update_api_2016_national_democratic_primary);
        TextView text_view_update_api_2016_general_election = (TextView)findViewById(R.id.text_view_update_api_2016_general_election);

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
        PieChart demPieChart = (PieChart)findViewById(R.id.chartDNC);
        PieChart repPieChart = (PieChart)findViewById(R.id.chartGOP);
        PieChart generalPieChart = (PieChart)findViewById(R.id.chartNational);

        final CandidateDataSource ds = new CandidateDataSource(AllPolls.this);

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


        final int colorBlue1 = ContextCompat.getColor(AllPolls.this, R.color.colorMotionPressDNC);
        final int colorBlue2 = ContextCompat.getColor(AllPolls.this, R.color.colorBlueLightish);
        final int colorPrimary = ContextCompat.getColor(AllPolls.this, R.color.colorPrimary);
        final int colorBlueDark = ContextCompat.getColor(AllPolls.this, R.color.colorBlueDark);

        int[] democratColors = {colorBlue1,colorBlue2};
        int[] democratColorsPie = {colorBlue1, colorBlueDark};

        final int colorRed1 = ContextCompat.getColor(AllPolls.this, R.color.colorRed);
        final int colorRed2 = ContextCompat.getColor(AllPolls.this, R.color.colorMotionPressGOP);
        final int colorRed3 = ContextCompat.getColor(AllPolls.this, R.color.colorRedLight);
        final int colorRedDark = ContextCompat.getColor(AllPolls.this, R.color.colorRedDark);


        final int colorMaterial700 = ContextCompat.getColor(AllPolls.this, R.color.colorMaterialGrey700);
        final int colorMaterial800 = ContextCompat.getColor(AllPolls.this, R.color.colorMaterialGrey800);
        final int colorMaterial900 = ContextCompat.getColor(AllPolls.this, R.color.colorMaterialGrey900);
        final int colorWhite = ContextCompat.getColor(AllPolls.this, R.color.colorWhite);

        Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);

        int[] republicanColors = {colorRed1,colorRed2,colorRed3};
        int[] republicanColorsPie = {colorRedDark,colorRed2,colorRed1};
        int[] bothColors = {colorBlue1, colorBlue2, colorRed1,colorRed2,colorRed3};

        final int colorPurple = ContextCompat.getColor(AllPolls.this, R.color.colorPurpleDark);

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
                Intent intent = new Intent(AllPolls.this, CandidateProfile.class);
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
                Intent intent = new Intent(AllPolls.this, CandidateProfile.class);
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
                Intent intent = new Intent(AllPolls.this, CandidateProfile.class);
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
        PieChart demPieChart = (PieChart)findViewById(R.id.chartNational);
        PieChart repPieChart = (PieChart)findViewById(R.id.chartNational);

        final CandidateDataSource ds = new CandidateDataSource(AllPolls.this);

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

        final int colorBlue1 = ContextCompat.getColor(AllPolls.this, R.color.colorMotionPressDNC);
        final int colorBlue2 = ContextCompat.getColor(AllPolls.this, R.color.colorBlueLight);
        final int colorPrimary = ContextCompat.getColor(AllPolls.this, R.color.colorPrimary);
        final int colorBlueDark = ContextCompat.getColor(AllPolls.this, R.color.colorBlueDark);


        int[] democratColors = {colorBlue1,colorBlue2};
        int[] democratColorsPie = {colorBlue1, colorBlueDark};

        final int colorRed1 = ContextCompat.getColor(AllPolls.this, R.color.colorRed);
        final int colorRed2 = ContextCompat.getColor(AllPolls.this, R.color.colorMotionPressGOP);
        final int colorRed3 = ContextCompat.getColor(AllPolls.this, R.color.colorRedLight);
        final int colorRedDark = ContextCompat.getColor(AllPolls.this, R.color.colorRedDark);


        final int colorMaterial700 = ContextCompat.getColor(AllPolls.this, R.color.colorMaterialGrey700);
        final int colorMaterial800 = ContextCompat.getColor(AllPolls.this, R.color.colorMaterialGrey800);
        final int colorMaterial900 = ContextCompat.getColor(AllPolls.this, R.color.colorMaterialGrey900);
        final int colorWhite = ContextCompat.getColor(AllPolls.this, R.color.colorWhite);

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
                Intent intent = new Intent(AllPolls.this, CandidateProfile.class);
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
                Intent intent = new Intent(AllPolls.this, CandidateProfile.class);
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
