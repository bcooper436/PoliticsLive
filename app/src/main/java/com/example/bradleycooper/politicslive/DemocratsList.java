package com.example.bradleycooper.politicslive;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;


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

    private int democratColor;

    Context thisContext;
    ArrayList<Candidate> arrayListCandidates;
    CandidateAdapterRanking adapter;

    ArrayList<User> arrayListUsers;

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



        CandidateDataSource dataSource = new CandidateDataSource(thisContext);
        dataSource.open();
        arrayListCandidates = dataSource.getCandidatesInOrderOfVotes("DNC");
        dataSource.close();

        adapter = new CandidateAdapterRanking(thisContext,arrayListCandidates);
        ListView listViewDNC = (ListView)getView().findViewById(R.id.listViewDNC);
        listViewDNC.setAdapter(adapter);

        listViewDNC.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position, long id) {
                Candidate selectedCandidate = arrayListCandidates.get(position);
                Intent intent = new Intent(thisContext, CandidateProfile.class);
                intent.putExtra("candidateId", selectedCandidate.getCandidateID());
                startActivity(intent);
            }
        });
        listViewDNC.setFocusable(false);



        UserDataSource userDataSource = new UserDataSource(thisContext);
        userDataSource.open();
        arrayListUsers = userDataSource.getUsersByParty("Democrat");



        democratColor = ContextCompat.getColor(thisContext, R.color.colorPrimary);

        TextView textViewTotalVotes = (TextView) getView().findViewById(R.id.textViewTotalVotes);
        TextView textViewTotalVotesLabel = (TextView) getView().findViewById(R.id.textViewTotalVotesLabel);
        TextView textViewRegisteredPartyMembers = (TextView) getView().findViewById(R.id.textViewRegisteredPartyMembers);
        TextView textViewRegisteredPartyMembersLabel = (TextView)getView().findViewById(R.id.textViewRegisteredPartyMembersLabel);
        TextView textViewAverageAge = (TextView) getView().findViewById(R.id.textViewAverageAge);
        TextView textViewAverageAgeLabel = (TextView) getView().findViewById(R.id.textViewAverageAgeLabel);
        TextView textViewGenderLabel = (TextView) getView().findViewById(R.id.textViewGenderBreakdown);
        TextView textViewMalePercentage = (TextView) getView().findViewById(R.id.textViewMalePercentage);
        TextView textViewFemalePercentage = (TextView) getView().findViewById(R.id.textViewFemalePercentage);

        if(arrayListUsers.size() > 0) {
            textViewRegisteredPartyMembers.setText(Integer.toString(userDataSource.getPartyMembers("Democrat")));
            textViewAverageAge.setText(Integer.toString(userDataSource.getAverageVoterAge("Democrat")));
            textViewMalePercentage.setText(Integer.toString(userDataSource.getGenderPercentage("Democrat", "Male")));
            textViewFemalePercentage.setText(Integer.toString(userDataSource.getGenderPercentage("Democrat", "Female")));
        }

        textViewTotalVotes.setText(Integer.toString(getTotalVotes(arrayListCandidates)));
        textViewTotalVotes.setBackgroundResource(R.drawable.circle_dnc);
        textViewTotalVotesLabel.setText("Total votes for Democratic Candidates = ");
        textViewRegisteredPartyMembers.setBackgroundResource(R.drawable.circle_dnc);
        textViewRegisteredPartyMembersLabel.setText("Registered Democratic Voters = ");
        textViewAverageAge.setBackgroundResource(R.drawable.circle_dnc);
        textViewAverageAgeLabel.setText("Average Age of Democratic Voter = ");
        textViewMalePercentage.setBackgroundResource(R.drawable.circle_dnc);
        textViewFemalePercentage.setBackgroundResource(R.drawable.circle_dnc);
        userDataSource.close();

        final int colorDown = ContextCompat.getColor(thisContext, R.color.colorLayoutPressed);
        final int colorDownLight = ContextCompat.getColor(thisContext, R.color.colorLayoutPressedLight);

        final int colorUp = ContextCompat.getColor(thisContext, R.color.colorBackgroundGrey);
        final int colorWhite = ContextCompat.getColor(thisContext, R.color.colorWhite);

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
                        AlertDialog alertDialog2 = new AlertDialog.Builder(thisContext).create();
                        alertDialog2.setTitle("Success");
                        alertDialog2.setMessage("Your account has been created");
                        alertDialog2.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog2.show();
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        relativeLayoutBrowseUsers.setBackgroundColor(colorUp);
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
    public int getTotalVotes(ArrayList<Candidate> arrayListCandidates){
        int totalVotes = 0;
        for(Candidate c : arrayListCandidates) {
            totalVotes += c.getNumberOfVotes();
        }
        return totalVotes;
    }
    public int getNumberOfRegisteredDemocrats(ArrayList<User> arrayListUsers) {
        int registeredDemocrats = 0;
        for(User u : arrayListUsers) {
            registeredDemocrats++;
        }
        return registeredDemocrats;
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
