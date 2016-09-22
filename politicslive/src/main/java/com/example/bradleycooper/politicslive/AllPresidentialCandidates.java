package com.example.bradleycooper.politicslive;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

public class AllPresidentialCandidates extends AppCompatActivity {
    ArrayList<Candidate> arrayListGOP, arrayListDNC, arrayListLIB;
    CandidateAdapter adapterGOP, adapterDNC, adapterLIB;
    ListView listViewGOP, listViewDNC, listViewLIB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_presidential_candidates);
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

        initCandidates();

    }
    private void initCandidates(){
        LinearLayout linearLayoutRepublican = (LinearLayout)findViewById(R.id.linearLayoutRepublican);
        LinearLayout linearLayoutDemocrat = (LinearLayout)findViewById(R.id.linearLayoutDemocrat);
        LinearLayout linearLayoutLibertarian = (LinearLayout)findViewById(R.id.linearLayoutLibertarian);

        ArrayList<Candidate> arrayListCandidatesRepublican, arrayListCandidatesDemocrat, arrayListCandidatesLibertarian;
        CandidateAdapter candidateAdapterRepublican, candidateAdapterDemocrat, candidateAdapterLibertarian;

        CandidateDataSource dataSourceCandidates = new CandidateDataSource(AllPresidentialCandidates.this);
        dataSourceCandidates.open();
        arrayListCandidatesRepublican = dataSourceCandidates.getSpecificParty("GOP");
        arrayListCandidatesDemocrat = dataSourceCandidates.getSpecificParty("DNC");
        arrayListCandidatesLibertarian = dataSourceCandidates.getSpecificParty("Libertarian");

        dataSourceCandidates.close();

        candidateAdapterRepublican = new CandidateAdapter(AllPresidentialCandidates.this,arrayListCandidatesRepublican);
        final int adapterCountRepublican = candidateAdapterRepublican.getCount();
        for(int i = 0; i < adapterCountRepublican; i++){
            View item = candidateAdapterRepublican.getView(i, null, null);
            linearLayoutRepublican.addView(item);
        }
        candidateAdapterDemocrat = new CandidateAdapter(AllPresidentialCandidates.this,arrayListCandidatesDemocrat);
        final int adapterCountDemocrat = candidateAdapterDemocrat.getCount();
        for(int i = 0; i < adapterCountDemocrat; i++){
            View item = candidateAdapterDemocrat.getView(i, null, null);
            linearLayoutDemocrat.addView(item);
        }
        candidateAdapterLibertarian = new CandidateAdapter(AllPresidentialCandidates.this,arrayListCandidatesLibertarian);
        final int adapterCountLibertarian = candidateAdapterLibertarian.getCount();
        for(int i = 0; i < adapterCountLibertarian; i++){
            View item = candidateAdapterLibertarian.getView(i, null, null);
            linearLayoutLibertarian.addView(item);
        }

    }

}
