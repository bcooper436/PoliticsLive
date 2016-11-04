package com.example.bradleycooper.politicslive.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bradleycooper.politicslive.R;
import com.example.bradleycooper.politicslive.dataobjects.Candidate;
import com.example.bradleycooper.politicslive.datasources.CandidateDataSource;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PollsPage.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PollsPage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PollsPage extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    String[][] arrayOfVoterPercentages;
    String[][] arrayFavorable;

    private OnFragmentInteractionListener mListener;

    public String API_2016_NATIONAL_GOP_PRIMARY;
    public String API_2016_NATIONAL_DEMOCRATIC_PRIMARY;
    public String API_REPUBLICAN_PARTY_FAVORABLE_RATING;
    public String API_DEMOCRATIC_PARTY_FAVORABLE_RATING;

    public String API_HILLARY_CLINTON_FAVORABLE_RATING;
    public String API_BERNIE_SANDERS_FAVORABLE_RATING;
    public String API_DONALD_TRUMP_FAVORABLE_RATING;
    public String API_TED_CRUZ_FAVORABLE_RATING;
    public String API_JOHN_KASICH_FAVORABLE_RATING;

    public String API_PARTY_IDENTIFICATION;
    public String API_US_SATISFACTION;
    public String API_US_RIGHT_DIRECTION_WRONG_TRACK;
    public String API_CONGRESS_JOB_APPROVAL;
    public String API_2016_NATIONAL_PRIMARY_GENERAL;

    public String currentAPI;
    public String currentAPILabel;
    public Boolean API_2016_NATIONAL_GOP_PRIMARY_DONE = false,
            API_2016_NATIONAL_DEMOCRATIC_PRIMARY_DONE = false,

            API_REPUBLICAN_PARTY_FAVORABLE_RATING_DONE = false,
            API_DEMOCRATIC_PARTY_FAVORABLE_RATING_DONE = false,
            API_HILLARY_CLINTON_FAVORABLE_RATING_DONE = false,
            API_BERNIE_SANDERS_FAVORABLE_RATING_DONE = false,
            API_DONALD_TRUMP_FAVORABLE_RATING_DONE = false,
            API_TED_CRUZ_FAVORABLE_RATING_DONE = false,
            API_JOHN_KASICH_FAVORABLE_RATING_DONE = false,
            API_PARTY_IDENTIFICATION_DONE = false,
            API_US_SATISFACTION_DONE = false,
            API_US_RIGHT_DIRECTION_WRONG_TRACK_DONE = false,
            API_CONGRESS_JOB_APPROVAL_DONE = false;

    public int NUMBER_OF_APIS = 7;
    ProgressDialog progressBar;
    int progressBarProgress = 0;
    Candidate candidate;
    CandidateDataSource ds;
    Candidate candidateToUpdate;


    public PollsPage() {
        // Required empty public constructor
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        API_2016_NATIONAL_GOP_PRIMARY = getActivity().getResources().getString(R.string.api_2016_national_gop_primary);
        API_2016_NATIONAL_DEMOCRATIC_PRIMARY = getActivity().getResources().getString(R.string.api_2016_national_democratic_primary);
        API_REPUBLICAN_PARTY_FAVORABLE_RATING = getActivity().getResources().getString(R.string.api_republican_party_favorable_rating);
        API_DEMOCRATIC_PARTY_FAVORABLE_RATING = getActivity().getResources().getString(R.string.api_democratic_party_favorable_rating);

        //API_2016_NATIONAL_PRIMARY_GENERAL = getActivity().getResources().getString(R.string.api_2016_national_primary_general);

        API_HILLARY_CLINTON_FAVORABLE_RATING = getActivity().getResources().getString(R.string.api_hillary_clinton_favorable_rating);
        API_BERNIE_SANDERS_FAVORABLE_RATING = getActivity().getResources().getString(R.string.api_bernie_sanders_favorable_rating);
        API_DONALD_TRUMP_FAVORABLE_RATING = getActivity().getResources().getString(R.string.api_donald_trump_favorable_rating);
        API_TED_CRUZ_FAVORABLE_RATING = getActivity().getResources().getString(R.string.api_ted_cruz_favorable_rating);
        API_JOHN_KASICH_FAVORABLE_RATING = getActivity().getResources().getString(R.string.api_john_kasich_favorable_rating);

        API_PARTY_IDENTIFICATION = getActivity().getResources().getString(R.string.api_party_identification);
        API_US_SATISFACTION = getActivity().getResources().getString(R.string.api_us_satisfaction);
        API_US_RIGHT_DIRECTION_WRONG_TRACK = getActivity().getResources().getString(R.string.api_us_right_direction_wrong_track);
        API_CONGRESS_JOB_APPROVAL = getActivity().getResources().getString(R.string.api_congress_job_approval);

        //TextView textView = (TextView)getActivity().findViewById(R.id.textViewNumberOfExitPolls);
        //String s[] = ds.getArrayOfAllStates();
        //textView.setText(Arrays.toString(s));
        //textView.setText(String.valueOf(ds.getPercentageOfVoteByStateAndCandidateAndVoterGroup("Nevada","Hillary Clinton","female")));
        /*
        String currentCandidate = "Hillary Clinton", currentState, currentVoterGroup;
        String[] states = ds.getArrayOfAllStates(), voterGroups = ds.getArrayOfVoterGroupsByCandidate(currentCandidate);
        int[][] intArray = ds.generateFormattedArrayForDataMining(currentCandidate);
        int numberOfStates = ds.getNumberOfStates(), numberOfVoterGroups = ds.getNumberOfVoterGroupsByCandidate(currentCandidate);


        textView.append(currentCandidate + "\n" + "\n" + "\n");

        for(int i = 0; i < numberOfStates; i++){
            currentState = states[i];
            textView.append(currentState + "\n");
            for(int j = 0; j < numberOfVoterGroups; j++){
                currentVoterGroup = voterGroups[j];
                textView.append("----- " + currentVoterGroup + " = ");
                textView.append(String.valueOf(intArray[i][j]));
                textView.append("%" + "\n");
            }
        }
        */ /*
        for(int i = 0; i < formattedArray.length; i++){
            for(int j = 0; j < formattedArray[i].length; j++){
                textViewItemSets.append(formattedArray[i][j] + "\n");
            }
            textViewItemSets.append("\n" + "\n" + "\n");
        } *//*
        String currentCandidate = "John Kasich";
        String[][] formattedArray = ds.generateFormattedArrayForDataMining(currentCandidate);

        int numberOfStates = ds.getNumberOfStates();
        int numberOfVoterGroups = ds.getNumberOfVoterGroupsByCandidate(currentCandidate);
        String[] arrayOfVoterGroups = ds.getArrayOfVoterGroupsByCandidate(currentCandidate);


        ArrayList<DataMiningItemSet> list = ds.generateArrayListOfItemSets(formattedArray, numberOfStates, arrayOfVoterGroups, numberOfVoterGroups);
        ArrayList<DataMiningItemSet> listPruned = ds.findMostFrequentItemSetApriori(formattedArray, list, 4, 4);

        //textViewItemSets.setText(String.valueOf(ds.getNumberOfVoterGroupLabelsByCandidate(currentCandidate)));
        TextView textViewSupportCounts = (TextView)getActivity().findViewById(R.id.textViewSupportCounts);
        TextView textViewItemSets = (TextView)getActivity().findViewById(R.id.textViewItemSets);
        TextView textViewCurrentCandidateInQuestion = (TextView)getActivity().findViewById(R.id.textViewCurrentCandidateInQuestion);
        textViewCurrentCandidateInQuestion.setText(currentCandidate);
        for(DataMiningItemSet itemSet:listPruned){
            textViewItemSets.append(itemSet.getItemSetName() + "\n");
            textViewSupportCounts.append(String.valueOf(itemSet.getSupport()) + "\n");
        } */

        startProgressDialogue(view);
    }



    private void loadAPIs(){
        if(!API_2016_NATIONAL_GOP_PRIMARY_DONE){
            currentAPI = API_2016_NATIONAL_GOP_PRIMARY;
            currentAPILabel = "API_2016_NATIONAL_GOP_PRIMARY";
            new RetrieveFeedTask().execute();
        }
        else if(!API_2016_NATIONAL_DEMOCRATIC_PRIMARY_DONE) {
            currentAPI = API_2016_NATIONAL_DEMOCRATIC_PRIMARY;
            currentAPILabel = "API_2016_NATIONAL_DEMOCRATIC_PRIMARY";
            new RetrieveFeedTask().execute();
        }
        else if(!API_HILLARY_CLINTON_FAVORABLE_RATING_DONE) {
            currentAPI = API_HILLARY_CLINTON_FAVORABLE_RATING;
            currentAPILabel = "API_HILLARY_CLINTON_FAVORABLE_RATING";
            new RetrieveFeedTask().execute();
        }
        else if(!API_BERNIE_SANDERS_FAVORABLE_RATING_DONE) {
            currentAPI = API_BERNIE_SANDERS_FAVORABLE_RATING;
            currentAPILabel = "API_BERNIE_SANDERS_FAVORABLE_RATING";
            new RetrieveFeedTask().execute();
        }
        else if(!API_DONALD_TRUMP_FAVORABLE_RATING_DONE) {
            currentAPI = API_DONALD_TRUMP_FAVORABLE_RATING;
            currentAPILabel = "API_DONALD_TRUMP_FAVORABLE_RATING";
            new RetrieveFeedTask().execute();
        }
        else if(!API_TED_CRUZ_FAVORABLE_RATING_DONE) {
            currentAPI = API_TED_CRUZ_FAVORABLE_RATING;
            currentAPILabel = "API_TED_CRUZ_FAVORABLE_RATING";
            new RetrieveFeedTask().execute();
        }
        else if(!API_JOHN_KASICH_FAVORABLE_RATING_DONE) {
            currentAPI = API_JOHN_KASICH_FAVORABLE_RATING;
            currentAPILabel = "API_JOHN_KASICH_FAVORABLE_RATING";
            new RetrieveFeedTask().execute();
        }
        else {
            //progressBar.setVisibility(View.INVISIBLE);
            //textViewLoading.setVisibility(View.INVISIBLE);
            //textView.setVisibility(View.VISIBLE);
            //textViewPollTitle.setVisibility(View.VISIBLE);
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
                        //textViewPollTitle.setText(pollTitle);
                        //textView.setText(pollValues);

                        jr = new JSONArray(pollValues);
                        for(int i = 0; i<jr.length(); i++ ){
                            JSONObject currentCandidate = jr.getJSONObject(i);
                            arrayOfVoterPercentages[i][0] = currentCandidate.getString("first_name") + " " + currentCandidate.getString("last_name");
                            arrayOfVoterPercentages[i][1] = currentCandidate.getString("value");
                        }
                        for(int j = 0; j < arrayOfVoterPercentages.length; j++) {
                            if (!arrayOfVoterPercentages[j][0].equalsIgnoreCase("null null")) {
                                ds = new CandidateDataSource(getActivity());
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
                        arrayOfVoterPercentages = new String[4][2];
                        API_2016_NATIONAL_DEMOCRATIC_PRIMARY_DONE = true;

                        object = (JSONObject) new JSONTokener(response).nextValue();
                        pollTitle = object.getString("title");
                        pollValues = object.getString("estimates");
                        lastUpdated = object.getString("last_updated");
                        //textViewPollTitle = (TextView)getActivity().findViewById(R.id.textViewPollName);
                        //textViewPollTitle.setText(pollTitle);

                        //textView = (TextView) getActivity().findViewById(R.id.textViewPolls);
                        //textView.setText(pollValues);

                        jr = new JSONArray(pollValues);
                        for(int i = 0; i<jr.length(); i++ ){
                            JSONObject currentCandidate = jr.getJSONObject(i);
                            arrayOfVoterPercentages[i][0] = currentCandidate.getString("first_name") + " " + currentCandidate.getString("last_name");
                            arrayOfVoterPercentages[i][1] = currentCandidate.getString("value");
                        }
                        for(int j = 0; j < arrayOfVoterPercentages.length; j++) {
                            if (!arrayOfVoterPercentages[j][0].equalsIgnoreCase("null null")) {
                                ds = new CandidateDataSource(getActivity());
                                ds.open();
                                candidate = ds.getCandidateByName(arrayOfVoterPercentages[j][0]);
                                candidate.setHuffPercentageOfVote(Float.parseFloat(arrayOfVoterPercentages[j][1]));
                                ds.updateCandidate(candidate);
                                ds.close();
                            }
                        }
                        updateSharedPreferences("API_2016_NATIONAL_DEMOCRATIC_PRIMARY", lastUpdated);
                        break;

                    case "API_HILLARY_CLINTON_FAVORABLE_RATING":
                        API_HILLARY_CLINTON_FAVORABLE_RATING_DONE = true;

                        arrayFavorable = new String[3][2];

                        object = (JSONObject) new JSONTokener(response).nextValue();
                        pollTitle = object.getString("title");
                        pollValues = object.getString("estimates");
                        lastUpdated = object.getString("last_updated");
                        //textViewPollTitle.setText(pollTitle);
                        //textView.setText(pollValues);

                        ds = new CandidateDataSource(getActivity());
                        ds.open();
                        candidateToUpdate = ds.getCandidateByName("Hillary Clinton");

                        jr = new JSONArray(pollValues);
                        for(int i = 0; i<jr.length(); i++ ){
                            JSONObject currentCandidate = jr.getJSONObject(i);
                            arrayFavorable[i][0] = currentCandidate.getString("choice");
                            arrayFavorable[i][1] = currentCandidate.getString("value");
                        }
                        candidateToUpdate.setHuffFavorableRating(Float.parseFloat(arrayFavorable[0][1]));
                        candidateToUpdate.setHuffUnfavorableRating(Float.parseFloat(arrayFavorable[1][1]));
                        ds.updateCandidate(candidateToUpdate);
                        ds.close();
                        updateSharedPreferences("API_HILLARY_CLINTON_FAVORABLE_RATING", lastUpdated);
                        break;

                    case "API_BERNIE_SANDERS_FAVORABLE_RATING":
                        API_BERNIE_SANDERS_FAVORABLE_RATING_DONE = true;

                        arrayFavorable = new String[3][2];

                        object = (JSONObject) new JSONTokener(response).nextValue();
                        pollTitle = object.getString("title");
                        pollValues = object.getString("estimates");
                        lastUpdated = object.getString("last_updated");
                        //textViewPollTitle.setText(pollTitle);
                        //textView.setText(pollValues);

                        ds = new CandidateDataSource(getActivity());
                        ds.open();
                        candidateToUpdate = ds.getCandidateByName("Bernie Sanders");

                        jr = new JSONArray(pollValues);
                        for(int i = 0; i<jr.length(); i++ ){
                            JSONObject currentCandidate = jr.getJSONObject(i);
                            arrayFavorable[i][0] = currentCandidate.getString("choice");
                            arrayFavorable[i][1] = currentCandidate.getString("value");
                        }
                        candidateToUpdate.setHuffFavorableRating(Float.parseFloat(arrayFavorable[0][1]));
                        candidateToUpdate.setHuffUnfavorableRating(Float.parseFloat(arrayFavorable[1][1]));
                        ds.updateCandidate(candidateToUpdate);
                        ds.close();
                        updateSharedPreferences("API_BERNIE_SANDERS_FAVORABLE_RATING", lastUpdated);
                        break;

                    case "API_DONALD_TRUMP_FAVORABLE_RATING":
                        API_DONALD_TRUMP_FAVORABLE_RATING_DONE = true;

                        arrayFavorable = new String[3][2];

                        object = (JSONObject) new JSONTokener(response).nextValue();
                        pollTitle = object.getString("title");
                        pollValues = object.getString("estimates");
                        lastUpdated = object.getString("last_updated");
                        //textViewPollTitle.setText(pollTitle);
                        //textView.setText(pollValues);

                        ds = new CandidateDataSource(getActivity());
                        ds.open();
                        candidateToUpdate = ds.getCandidateByName("Donald Trump");

                        jr = new JSONArray(pollValues);
                        for(int i = 0; i<jr.length(); i++ ){
                            JSONObject currentCandidate = jr.getJSONObject(i);
                            arrayFavorable[i][0] = currentCandidate.getString("choice");
                            arrayFavorable[i][1] = currentCandidate.getString("value");
                        }
                        candidateToUpdate.setHuffFavorableRating(Float.parseFloat(arrayFavorable[0][1]));
                        candidateToUpdate.setHuffUnfavorableRating(Float.parseFloat(arrayFavorable[1][1]));
                        ds.updateCandidate(candidateToUpdate);
                        ds.close();
                        updateSharedPreferences("API_DONALD_TRUMP_FAVORABLE_RATING", lastUpdated);
                        break;

                    case "API_TED_CRUZ_FAVORABLE_RATING":
                        API_TED_CRUZ_FAVORABLE_RATING_DONE = true;

                        arrayFavorable = new String[3][2];

                        object = (JSONObject) new JSONTokener(response).nextValue();
                        pollTitle = object.getString("title");
                        pollValues = object.getString("estimates");
                        lastUpdated = object.getString("last_updated");
                        //textViewPollTitle.setText(pollTitle);
                        //textView.setText(pollValues);

                        ds = new CandidateDataSource(getActivity());
                        ds.open();
                        candidateToUpdate = ds.getCandidateByName("Ted Cruz");

                        jr = new JSONArray(pollValues);
                        for(int i = 0; i<jr.length(); i++ ){
                            JSONObject currentCandidate = jr.getJSONObject(i);
                            arrayFavorable[i][0] = currentCandidate.getString("choice");
                            arrayFavorable[i][1] = currentCandidate.getString("value");
                        }
                        candidateToUpdate.setHuffFavorableRating(Float.parseFloat(arrayFavorable[0][1]));
                        candidateToUpdate.setHuffUnfavorableRating(Float.parseFloat(arrayFavorable[1][1]));
                        ds.updateCandidate(candidateToUpdate);
                        ds.close();
                        updateSharedPreferences("API_TED_CRUZ_FAVORABLE_RATING", lastUpdated);
                        break;

                    case "API_JOHN_KASICH_FAVORABLE_RATING":
                        API_JOHN_KASICH_FAVORABLE_RATING_DONE = true;

                        arrayFavorable = new String[3][2];

                        object = (JSONObject) new JSONTokener(response).nextValue();
                        pollTitle = object.getString("title");
                        pollValues = object.getString("estimates");
                        lastUpdated = object.getString("last_updated");
                        //textViewPollTitle.setText(pollTitle);
                        //textView.setText(pollValues);

                        ds = new CandidateDataSource(getActivity());
                        ds.open();
                        candidateToUpdate = ds.getCandidateByName("John Kasich");

                        jr = new JSONArray(pollValues);
                        for(int i = 0; i<jr.length(); i++ ){
                            JSONObject currentCandidate = jr.getJSONObject(i);
                            arrayFavorable[i][0] = currentCandidate.getString("choice");
                            arrayFavorable[i][1] = currentCandidate.getString("value");
                        }
                        candidateToUpdate.setHuffFavorableRating(Float.parseFloat(arrayFavorable[0][1]));
                        candidateToUpdate.setHuffUnfavorableRating(Float.parseFloat(arrayFavorable[1][1]));
                        ds.updateCandidate(candidateToUpdate);
                        ds.close();
                        updateSharedPreferences("API_JOHN_KASICH_FAVORABLE_RATING", lastUpdated);
                        break;
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
            progressBarProgress++;
            progressBar.setProgress(progressBarProgress);

            if(progressBarProgress >= NUMBER_OF_APIS) {
                progressBar.hide();
            }
            else {
                loadAPIs();
            }
        }
    }

    private void startProgressDialogue(View V){
        progressBar = new ProgressDialog(V.getContext());
        progressBar.setCancelable(true);
        progressBar.setMessage("Updating Polls...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressBar.setProgress(0);
        progressBar.setMax(NUMBER_OF_APIS);
        Drawable customDrawable = (Drawable) getResources().getDrawable(R.drawable.custom_progressbar);
        progressBar.setProgressDrawable(customDrawable);
        progressBar.show();

        loadAPIs();
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
        if(hourCalc < 10) {
            hour = "0" + hour;
        }

        String minutes = timeOfLastUpdate.substring(14,16);

        String finalTimeOfLastUpdate = "Updated at " + hour + ":" + minutes  + " " + AMOrPM + " on " + month + "/" + day + "/" + year;

        SharedPreferences preferencesNo = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = preferencesNo.edit();
        editor.putString(apiUpdateLabel, finalTimeOfLastUpdate);
        editor.apply();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PollsPage.
     */
    // TODO: Rename and change types and number of parameters
    public static PollsPage newInstance(String param1, String param2) {
        PollsPage fragment = new PollsPage();
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
        View inflatedView = inflater.inflate(R.layout.fragment_polls_page, container, false);

        return inflatedView;
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
}
