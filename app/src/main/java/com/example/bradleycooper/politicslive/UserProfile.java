package com.example.bradleycooper.politicslive;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UserProfile.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UserProfile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserProfile extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Activity thisActivity;
    Context thisContext;
    ArrayList<User> arrayList;
    UserAdapter adapter;
    UserDataSource userDataSource;
    User currentUser;
    Candidate candidateDemocrat, candidateRepublican;
    private OnFragmentInteractionListener mListener;

    public UserProfile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserProfile.
     */
    // TODO: Rename and change types and number of parameters
    public static UserProfile newInstance(String param1, String param2) {
        UserProfile fragment = new UserProfile();
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
        return inflater.inflate(R.layout.fragment_user_profile, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String userName = preferences.getString("Username", "");

        userDataSource = new UserDataSource(getActivity());
        userDataSource.open();
        currentUser = userDataSource.getSpecificUserFromLoginInfo(userName,"zun3ukit");
        userDataSource.close();

        Button buttonLogOff = (Button)getView().findViewById(R.id.buttonLogout);

        buttonLogOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                builder1.setMessage("Are you sure you want to logout?");
                builder1.setCancelable(true);

                builder1.setNegativeButton(
                        "Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                builder1.setPositiveButton(
                        "Logout",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                clearUserPreferences();
                                Intent intent = new Intent(getActivity(), LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        });

        initUser(currentUser);
    }
    public void initUser(User currentUser) {
        candidateDemocrat = null;
        candidateRepublican = null;
        CandidateDataSource ds = new CandidateDataSource(getActivity());
        ds.open();
        if(currentUser.getChosenDemocrat() != null) {
            Candidate candidateDemocrat = ds.getCandidateByName(currentUser.getChosenDemocrat());
        }
        if(currentUser.getChosenRepublican() != null) {
            Candidate candidateRepublican = ds.getCandidateByName(currentUser.getChosenRepublican());
        }

        TextView textViewDisplayName = (TextView)getView().findViewById(R.id.textViewDisplayName);
        TextView textViewUserName = (TextView)getView().findViewById(R.id.textViewUserName);
        TextView textViewAge = (TextView)getView().findViewById(R.id.textViewAge);
        TextView textViewGender = (TextView)getView().findViewById(R.id.textViewGender);
        TextView textViewParty = (TextView)getView().findViewById(R.id.textViewParty);
        TextView textChosenDemocrat = (TextView)getView().findViewById(R.id.textViewChosenDemocrat);
        TextView textChosenRepublican = (TextView)getView().findViewById(R.id.textViewChosenRepublican);
        ImageView imageViewChosenDemocrat = (ImageView)getView().findViewById(R.id.imageViewChosenDemocrat);
        ImageView imageViewChosenRepublican = (ImageView)getView().findViewById(R.id.imageViewChosenRepublican);

        ImageView imageViewPartyIcon = (ImageView)getView().findViewById(R.id.imageViewPartyIcon);
        ImageView imageViewProfilePicture =(ImageView)getView().findViewById(R.id.imageViewProfilePicture);
        RelativeLayout relativeLayoutPartyColor = (RelativeLayout)getView().findViewById(R.id.relativeLayoutPartyColor);

        int idGOP = getResources().getIdentifier("com.example.bradleycooper.politicslive:drawable/ic_action_gop_red", null, null);
        int idDNC = getResources().getIdentifier("com.example.bradleycooper.politicslive:drawable/ic_action_dnc_blue", null, null);
        int idGOPProfilePicture = getResources().getIdentifier("com.example.bradleycooper.politicslive:drawable/contact_red", null, null);
        int idDNCProfilePicture = getResources().getIdentifier("com.example.bradleycooper.politicslive:drawable/contact_blue", null, null);


        if(currentUser.getPartyAffiliation().equalsIgnoreCase("Democrat")){
            imageViewPartyIcon.setImageResource(idDNC);
            imageViewProfilePicture.setImageResource(idDNCProfilePicture);
        }
        else {
            imageViewPartyIcon.setImageResource(idGOP);
            imageViewProfilePicture.setImageResource(idGOPProfilePicture);
        }
        textViewDisplayName.setText(currentUser.getDisplayName());
        textViewUserName.setText(currentUser.getUserName());
        textViewAge.setText(String.valueOf(currentUser.getAge()));
        textViewGender.setText(currentUser.getGender());
        textViewParty.setText(currentUser.getPartyAffiliation());
        textChosenDemocrat.setText(currentUser.getChosenDemocrat());
        textChosenRepublican.setText(currentUser.getChosenRepublican());

        if(candidateDemocrat != null){
            byte[] byteArrayDemocrat = candidateDemocrat.getSquarePicture();
            Bitmap bmpDemocrat = BitmapFactory.decodeByteArray(byteArrayDemocrat, 0, byteArrayDemocrat.length);
            imageViewChosenDemocrat.setImageBitmap(bmpDemocrat);
        }
        if(candidateRepublican != null) {
            byte[] byteArrayRepublican = candidateRepublican.getSquarePicture();
            Bitmap bmpRepublican = BitmapFactory.decodeByteArray(byteArrayRepublican, 0, byteArrayRepublican.length);
            imageViewChosenRepublican.setImageBitmap(bmpRepublican);
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
    public void clearUserPreferences(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Username","");
        editor.putString("Password", "");
        editor.apply();
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
