package com.example.bradleycooper.politicslive.activities;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.example.bradleycooper.politicslive.R;
import com.example.bradleycooper.politicslive.adapters.PoliticalPartyAdapter;
import com.example.bradleycooper.politicslive.dataobjects.PoliticalParty;
import com.example.bradleycooper.politicslive.datasources.PoliticalPartyDataSource;

import java.util.ArrayList;

public class AllPoliticalParties extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_political_parties);
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

        LinearLayout linearLayoutPoliticalParties = (LinearLayout)findViewById(R.id.linearLayoutPoliticalParties);
        ArrayList<PoliticalParty> arrayListPoliticalParties;
        PoliticalPartyAdapter adapterPoliticalParties;

        PoliticalPartyDataSource dataSource = new PoliticalPartyDataSource(AllPoliticalParties.this);
        dataSource.open();
        arrayListPoliticalParties = dataSource.getPoliticalParties();
        dataSource.close();

        adapterPoliticalParties = new PoliticalPartyAdapter(AllPoliticalParties.this,arrayListPoliticalParties);
        final int adapterCount = adapterPoliticalParties.getCount();
        for(int i = 0; i < adapterCount; i++){
            View item = adapterPoliticalParties.getView(i, null, null);
            linearLayoutPoliticalParties.addView(item);
        }
    }

}
