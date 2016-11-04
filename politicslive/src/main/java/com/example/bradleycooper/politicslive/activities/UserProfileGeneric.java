package com.example.bradleycooper.politicslive.activities;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.bradleycooper.politicslive.R;
import com.example.bradleycooper.politicslive.adapters.CandidateAdapter;
import com.example.bradleycooper.politicslive.dataobjects.Candidate;
import com.example.bradleycooper.politicslive.dataobjects.User;
import com.example.bradleycooper.politicslive.datasources.CandidateDataSource;
import com.example.bradleycooper.politicslive.datasources.UserDataSource;

import java.util.ArrayList;

public class UserProfileGeneric extends AppCompatActivity {
    private AppCompatDelegate delegate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();


        UserDataSource userDataSource = new UserDataSource(UserProfileGeneric.this);
        userDataSource.open();
        User currentUser = userDataSource.getSpecificUser(extras.getInt("userId"));
        userDataSource.close();

        //final int colorGOPDark = ContextCompat.getColor(UserProfileGeneric.this, R.color.colorRedDarker);

        setContentView(R.layout.activity_user_profile_generic);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.setBackgroundColor(colorGOPDark);
        //delegate.setSupportActionBar(toolbar);

        setSupportActionBar(toolbar);

        ImageView imageView = (ImageView)findViewById(R.id.imageViewBackButton);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initUser(currentUser);
        initChosenCandidates(currentUser);



    }

    public void initUser(User currentUser){
        TextView textViewDisplayName = (TextView)findViewById(R.id.textViewDisplayName);
        TextView textViewUsername = (TextView)findViewById(R.id.textViewUsername);
        TextView textViewAge = (TextView)findViewById(R.id.textViewAge);
        TextView textViewGender = (TextView)findViewById(R.id.textViewGender);
        TextView textViewParty = (TextView)findViewById(R.id.textViewParty);

        textViewDisplayName.setText(currentUser.getDisplayName());
        textViewUsername.setText(currentUser.getUserName());
        textViewAge.setText(Integer.toString(currentUser.getAge()));
        textViewGender.setText(currentUser.getGender());
        textViewParty.setText(currentUser.getPartyAffiliation());

        int colorDNC = ContextCompat.getColor(UserProfileGeneric.this, R.color.colorPrimary);
        int colorGOP = ContextCompat.getColor(UserProfileGeneric.this, R.color.colorRedDark);
        int colorIndependent = ContextCompat.getColor(UserProfileGeneric.this, R.color.colorPurple);
        RelativeLayout relativeLayoutUser = (RelativeLayout)findViewById(R.id.relativeLayoutUser);
        ImageView imageViewPartyIcon = (ImageView)findViewById(R.id.imageViewPartyIcon);

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
                //relativeLayoutUser.setBackgroundColor(colorIndependent);
                break;
        }
    }

    public void initChosenCandidates(User currentUser){
        Boolean skipAdapter = false;
        TextView textViewWarning = (TextView)findViewById(R.id.textViewWarning);

        String chosenDemocrat = currentUser.getChosenDemocrat();
        String chosenRepublican = currentUser.getChosenRepublican();

        ArrayList<Candidate> arrayList = new ArrayList<Candidate>();

        CandidateDataSource candidateDataSource = new CandidateDataSource(UserProfileGeneric.this);
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
            CandidateAdapter candidateAdapter = new CandidateAdapter(UserProfileGeneric.this, arrayList);
            LinearLayout linearLayoutCandidates = (LinearLayout) findViewById(R.id.linearLayoutCandidates);

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
