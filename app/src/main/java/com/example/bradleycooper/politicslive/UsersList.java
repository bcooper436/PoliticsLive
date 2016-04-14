package com.example.bradleycooper.politicslive;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UsersList.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UsersList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UsersList extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public UsersList() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UsersList.
     */
    // TODO: Rename and change types and number of parameters
    public static UsersList newInstance(String param1, String param2) {
        UsersList fragment = new UsersList();
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
        return inflater.inflate(R.layout.fragment_users_list, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((MainActivity) getActivity()).hideFloatingActionButton();

        Spinner spinnerFilter = (Spinner) getView().findViewById(R.id.spinnerFilter);
        ArrayAdapter<CharSequence> adapterFilter = ArrayAdapter.createFromResource(getActivity(), R.array.filter_array, R.layout.spinner_item);
        adapterFilter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilter.setAdapter(adapterFilter);

        Spinner spinnerSort = (Spinner) getView().findViewById(R.id.spinnerSort);
        ArrayAdapter<CharSequence> adapterSort = ArrayAdapter.createFromResource(getActivity(), R.array.sort_array, R.layout.spinner_item);
        adapterSort.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSort.setAdapter(adapterSort);

        initListViewUsers();
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


    public void initListViewUsers(){
        ListView listViewUsers = (ListView)getView().findViewById(R.id.listViewUsers);
        UserDataSource userDataSource = new UserDataSource(getActivity());
        userDataSource.open();
        ArrayList<User> arrayListUsers = userDataSource.getUsers();
        userDataSource.close();
        UserAdapter userAdapter = new UserAdapter(getActivity(),arrayListUsers);
        listViewUsers.setAdapter(userAdapter);
    }
}
