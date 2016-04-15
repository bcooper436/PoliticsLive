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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

    public int userId;
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

        ((MainActivity) getActivity()).hideFloatingActionButton();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String userName = preferences.getString("Username", "");

        userDataSource = new UserDataSource(getActivity());
        userDataSource.open();
        currentUser = userDataSource.getSpecificUserFromLoginInfo(userName,"zun3ukit");
        userDataSource.close();
        userId = currentUser.getUserID();

        final int colorDown = ContextCompat.getColor(getActivity(), R.color.colorLayoutPressed);
        final int colorUp = ContextCompat.getColor(getActivity(), R.color.colorBackgroundGrey);
        final TextView textViewChangeVote = (TextView)getActivity().findViewById(R.id.textViewChangeVote);
        final TextView textViewDeleteAccount = (TextView)getActivity().findViewById(R.id.textViewDeleteAccount);
        final TextView textViewLogOff = (TextView)getActivity().findViewById(R.id.textViewLogOff);

        textViewChangeVote.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        textViewChangeVote.setBackgroundColor(colorDown);
                        break;
                    case MotionEvent.ACTION_UP:
                        textViewChangeVote.setBackgroundColor(colorUp);
                        Intent intent = new Intent(getActivity(), Ballot.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        textViewChangeVote.setBackgroundColor(colorUp);
                        break;
                }
                return true;
            }
        });
        textViewDeleteAccount.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        textViewDeleteAccount.setBackgroundColor(colorDown);
                        break;
                    case MotionEvent.ACTION_UP:
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                        builder1.setMessage("Are you sure you want to delete your account?");
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
                                        UserDataSource userDataSource = new UserDataSource(getActivity());
                                        userDataSource.open();
                                        if(userDataSource.deleteUser(userId)){
                                            AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                                            alertDialog.setTitle("Success");
                                            alertDialog.setMessage("Your account has been deleted");
                                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.dismiss();
                                                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                            startActivity(intent);
                                                        }
                                                    });
                                            alertDialog.show();
                                        }
                                        else {
                                            AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                                            alertDialog.setTitle("Failure");
                                            alertDialog.setMessage("Your account has not been deleted");
                                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.dismiss();
                                                        }
                                                    });
                                            alertDialog.show();

                                        }
                                    }
                                });

                        AlertDialog alert11 = builder1.create();
                        alert11.show();
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        textViewDeleteAccount.setBackgroundColor(colorUp);
                        break;
                }
                return true;
            }
        });
        textViewLogOff.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        textViewLogOff.setBackgroundColor(colorDown);
                        break;
                    case MotionEvent.ACTION_UP:
                        textViewLogOff.setBackgroundColor(colorUp);
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
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        textViewLogOff.setBackgroundColor(colorUp);
                        break;
                }
                return true;
            }
        });

        initUser(currentUser);
        initChosenCandidates(currentUser);
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
    public void initUser(User currentUser){
        TextView textViewDisplayName = (TextView)getActivity().findViewById(R.id.textViewDisplayName);
        TextView textViewUsername = (TextView)getActivity().findViewById(R.id.textViewUsername);
        TextView textViewAge = (TextView)getActivity().findViewById(R.id.textViewAge);
        TextView textViewGender = (TextView)getActivity().findViewById(R.id.textViewGender);
        TextView textViewParty = (TextView)getActivity().findViewById(R.id.textViewParty);

        textViewDisplayName.setText(currentUser.getDisplayName());
        textViewUsername.setText(currentUser.getUserName());
        textViewAge.setText(Integer.toString(currentUser.getAge()));
        textViewGender.setText(currentUser.getGender());
        textViewParty.setText(currentUser.getPartyAffiliation());

        int colorDNC = ContextCompat.getColor(getActivity(), R.color.colorPrimary);
        int colorGOP = ContextCompat.getColor(getActivity(), R.color.colorRedDark);
        int colorIndependent = ContextCompat.getColor(getActivity(), R.color.colorPurple);
        RelativeLayout relativeLayoutUser = (RelativeLayout)getActivity().findViewById(R.id.relativeLayoutUser);
        ImageView imageViewPartyIcon = (ImageView)getActivity().findViewById(R.id.imageViewPartyIcon);

        switch(currentUser.getPartyAffiliation()){
            case "Democrat":
                imageViewPartyIcon.setBackgroundResource(R.drawable.ic_action_dnc_color);
                //relativeLayoutUser.setBackgroundColor(colorDNC);
                break;
            case "Republican":
                imageViewPartyIcon.setBackgroundResource(R.drawable.ic_action_gop_color);
                //relativeLayoutUser.setBackgroundColor(colorGOP);
                break;
            default:
                imageViewPartyIcon.setVisibility(View.INVISIBLE);
                relativeLayoutUser.setBackgroundColor(colorIndependent);
                break;
        }
    }

    public void initChosenCandidates(User currentUser){
        Boolean skipAdapter = false;
        TextView textViewWarning = (TextView)getActivity().findViewById(R.id.textViewWarning);

        String chosenDemocrat = currentUser.getChosenDemocrat();
        String chosenRepublican = currentUser.getChosenRepublican();

        ArrayList<Candidate> arrayList = new ArrayList<Candidate>();

        CandidateDataSource candidateDataSource = new CandidateDataSource(getActivity());
        candidateDataSource.open();
        if(chosenDemocrat == null){
            if(chosenRepublican == null) {
                textViewWarning.setVisibility(View.VISIBLE);
                skipAdapter = true;
            }
        }
        else {
            Candidate chosenCandidateDNC = candidateDataSource.getCandidateByName(chosenDemocrat);
            arrayList.add(chosenCandidateDNC);
        }
        if(chosenRepublican == null){
            if(chosenDemocrat == null) {
                textViewWarning.setVisibility(View.VISIBLE);
                skipAdapter = true;
            }
        }
        else {
            Candidate chosenCandidateGOP = candidateDataSource.getCandidateByName(chosenRepublican);
            arrayList.add(chosenCandidateGOP);
        }
        candidateDataSource.close();

        if(!skipAdapter) {
            CandidateAdapter candidateAdapter = new CandidateAdapter(getActivity(), arrayList);
            LinearLayout linearLayoutCandidates = (LinearLayout)getActivity().findViewById(R.id.linearLayoutCandidates);

            final int adapterCount = candidateAdapter.getCount();
            for (int i = 0; i < adapterCount; i++) {
                final int iFinal = i;
                View item = candidateAdapter.getView(i, null, null);
                linearLayoutCandidates.addView(item);
                item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });
            }
        }

    }
}
