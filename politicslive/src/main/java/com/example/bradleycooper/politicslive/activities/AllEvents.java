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
import com.example.bradleycooper.politicslive.adapters.EventAdapterSmall;
import com.example.bradleycooper.politicslive.dataobjects.Event;
import com.example.bradleycooper.politicslive.datasources.EventDataSource;

import java.util.ArrayList;

public class AllEvents extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_events);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        initEvents();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            final int primaryDark = ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark);
            window.setStatusBarColor(primaryDark);
        }
    }
    private void initEvents(){
        LinearLayout linearLayoutEvents = (LinearLayout)findViewById(R.id.linearLayoutEvents);
        ArrayList<Event> arrayListEvents;
        EventAdapterSmall adapterEventsSmall;

        EventDataSource dataSourceEvents = new EventDataSource(AllEvents.this);
        dataSourceEvents.open();
        arrayListEvents = dataSourceEvents.getEvents();
        dataSourceEvents.close();

        adapterEventsSmall = new EventAdapterSmall(AllEvents.this,arrayListEvents);
        final int adapterCount = adapterEventsSmall.getCount();
        for(int i = 0; i < adapterCount; i++){
            View item = adapterEventsSmall.getView(i, null, null);
            linearLayoutEvents.addView(item);
        }
    }

}
