package com.example.bradleycooper.politicslive;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.TextView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AllCandidates.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AllCandidates#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllCandidates extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private OnCommunicateActivityListener activityCommunicator;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Activity thisActivity;
    ArrayList<Candidate> arrayListGOP, arrayListDNC;
    CandidateAdapter adapterGOP, adapterDNC;
    private OnFragmentInteractionListener mListener;
    ListView listViewGOP, listViewDNC;

    public AllCandidates() {
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

        return inflater.inflate(R.layout.fragment_all_candidates, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        CandidateDataSource dataSource = new CandidateDataSource(getActivity());
        dataSource.open();
        arrayListGOP = dataSource.getCandidatesInOrderOfVotes("GOP");
        arrayListDNC = dataSource.getCandidatesInOrderOfVotes("DNC");
        dataSource.close();

        adapterGOP = new CandidateAdapter(getActivity(),arrayListGOP);
        adapterDNC = new CandidateAdapter(getActivity(),arrayListDNC);

        listViewGOP = (ListView)getView().findViewById(R.id.listViewRepublican);
        listViewDNC = (ListView)getView().findViewById(R.id.listViewDemocrat);
        listViewGOP.setAdapter(adapterGOP);
        listViewGOP.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position, long id) {
                Candidate selectedCandidate = arrayListGOP.get(position);
                Intent intent = new Intent(getContext(), CandidateProfile.class);
                intent.putExtra("candidateId", selectedCandidate.getCandidateID());
                startActivity(intent);
            }
        });
        listViewDNC.setAdapter(adapterDNC);
        listViewDNC.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position, long id) {
                Candidate selectedCandidate = arrayListDNC.get(position);
                Intent intent = new Intent(getContext(), CandidateProfile.class);
                intent.putExtra("candidateId", selectedCandidate.getCandidateID());
                startActivity(intent);
            }
        });

        final int colorDown = ContextCompat.getColor(getActivity(), R.color.colorLayoutPressed);
        final int colorUp = ContextCompat.getColor(getActivity(), R.color.colorBackgroundGrey);

        final TextView textViewDemocraticCandidatesLabel = (TextView)getView().findViewById(R.id.textViewDemocraticCandidatesLabel);
        final TextView textViewRepublicansCandidatesLabel = (TextView)getView().findViewById(R.id.textViewRepublicanCandidatesLabel);

        textViewDemocraticCandidatesLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment;
                FragmentManager fragmentManager = getFragmentManager();
                fragment = new DemocratsList();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, fragment)
                        .commit();
                activityCommunicator.passDataToActivity(R.id.nav_DNC);
            }
        });

        textViewRepublicansCandidatesLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment;
                FragmentManager fragmentManager = getFragmentManager();
                fragment = new RepublicansList();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, fragment)
                        .commit();
                activityCommunicator.passDataToActivity(R.id.nav_GOP);
            }
        });
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

    public interface OnCommunicateActivityListener{
        void passDataToActivity(int nevID);
    }
}