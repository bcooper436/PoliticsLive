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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;



/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListOfPoliticalParties.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListOfPoliticalParties#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListOfPoliticalParties extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ArrayList<PoliticalParty> arrayListPoliticalParties;
    PoliticalPartyAdapter adapterPoliticalParties;
    ListView listViewPoliticalParties;

    private OnFragmentInteractionListener mListener;

    public ListOfPoliticalParties() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListOfPoliticalParties.
     */
    // TODO: Rename and change types and number of parameters
    public static ListOfPoliticalParties newInstance(String param1, String param2) {
        ListOfPoliticalParties fragment = new ListOfPoliticalParties();
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
        return inflater.inflate(R.layout.fragment_list_of_political_parties, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayout linearLayoutPoliticalParties = (LinearLayout)getView().findViewById(R.id.linearLayoutPoliticalParties);
        ArrayList<PoliticalParty> arrayListPoliticalParties;
        PoliticalPartyAdapter adapterPoliticalParties;

        PoliticalPartyDataSource dataSource = new PoliticalPartyDataSource(getActivity());
        dataSource.open();
        arrayListPoliticalParties = dataSource.getPoliticalParties();
        dataSource.close();

        adapterPoliticalParties = new PoliticalPartyAdapter(getActivity(),arrayListPoliticalParties);
        final int adapterCount = adapterPoliticalParties.getCount();
        for(int i = 0; i < adapterCount; i++){
            View item = adapterPoliticalParties.getView(i, null, null);
            linearLayoutPoliticalParties.addView(item);
        }

        final int colorPressed = ContextCompat.getColor(getActivity(), R.color.colorLayoutPressed);
        final int colorWhite = ContextCompat.getColor(getActivity(), R.color.colorBackgroundGrey);


        final RelativeLayout relativeLayoutPoliticalPartiesMoreInfo = (RelativeLayout)getView().findViewById(R.id.relativeLayoutPoliticalPartiesMoreInfo);
        relativeLayoutPoliticalPartiesMoreInfo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        relativeLayoutPoliticalPartiesMoreInfo.setBackgroundColor(colorPressed);
                        break;
                    case MotionEvent.ACTION_UP:
                        Intent browserIntentEmail = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.scholastic.com/teachers/article/origins-and-functions-political-parties"));
                        startActivity(browserIntentEmail);
                        relativeLayoutPoliticalPartiesMoreInfo.setBackgroundColor(colorWhite);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        relativeLayoutPoliticalPartiesMoreInfo.setBackgroundColor(colorWhite);
                        break;
                }
                return true;

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
