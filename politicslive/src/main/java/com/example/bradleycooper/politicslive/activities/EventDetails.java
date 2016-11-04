package com.example.bradleycooper.politicslive.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bradleycooper.politicslive.R;
import com.example.bradleycooper.politicslive.dataobjects.Event;
import com.example.bradleycooper.politicslive.datasources.EventDataSource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class EventDetails extends AppCompatActivity {
    Event currentEvent;
    private int idDNC, colorDown, colorUp, colorPrimaryColor, colorMotionPress, colorMotionPressDNC, colorMotionPressGOP, democratColor, democratColorLight, democratColorDark, democratColorDarker, idGOP, republicanColor, republicanColorLight, republicanColorDarker;
    private String eventName, eventDescription, date, month, day, time, channel, moderator, site, location;

    private int numberOfHouseSeats, numberOfSenateSeats;
    private Toolbar toolbar;
    private ImageView imgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        EventDataSource eds = new EventDataSource(EventDetails.this);
        eds.open();
        currentEvent = eds.getSpecificEvent(extras.getInt("eventId"));
        eds.close();

        setContentView(R.layout.activity_event_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(currentEvent.getName());  // provide compatibility to all the versions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            final int primaryDark = ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark);
            window.setStatusBarColor(primaryDark);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        democratColor = ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary);
        democratColorDark = ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark);
        democratColorDarker = ContextCompat.getColor(getApplicationContext(), R.color.colorBlueDarker);

        democratColorLight = ContextCompat.getColor(getApplicationContext(), R.color.colorBlueLight);
        republicanColor = ContextCompat.getColor(getApplicationContext(), R.color.colorRedDark);
        republicanColorLight = ContextCompat.getColor(getApplicationContext(), R.color.colorRedLight);
        republicanColorDarker = ContextCompat.getColor(getApplicationContext(), R.color.colorRedDarker);
        idGOP = getResources().getIdentifier("com.example.bradleycooper.politicslive:drawable/ic_action_gop_red_light", null, null);
        idDNC = getResources().getIdentifier("com.example.bradleycooper.politicslive:drawable/ic_action_dnc_blue_light", null, null);



        initProfileResources();
        initEvent(extras.getInt("eventId"));
    }
    private void initProfileResources() {
        final TextView textViewSite = (TextView) findViewById(R.id.textViewSite);

        colorDown = ContextCompat.getColor(EventDetails.this, R.color.colorLayoutPressed);
        colorUp = ContextCompat.getColor(EventDetails.this, R.color.colorBackgroundGrey);

        textViewSite.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        textViewSite.setBackgroundColor(colorDown);
                        break;
                    case MotionEvent.ACTION_UP:
                        textViewSite.setBackgroundColor(0x00000000);
                        Intent browserIntentSite = new Intent(Intent.ACTION_VIEW, Uri.parse(currentEvent.getSite()));
                        startActivity(browserIntentSite);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        textViewSite.setBackgroundColor(0x00000000);
                        break;
                }
                return true;
            }
        });
    }
    private void initEvent(int id) {
        eventDescription = currentEvent.getDescription();
        date = currentEvent.getDate();
        location = currentEvent.getLocation();
        moderator = currentEvent.getModerator();
        site = currentEvent.getSite();
        channel = currentEvent.getChannel();
        time = currentEvent.getTime();



        TextView textViewDescription = (TextView)findViewById(R.id.textViewEventDescription);
        TextView textViewModerator = (TextView)findViewById(R.id.textViewEventModerator);
        TextView textViewEventLocation = (TextView)findViewById(R.id.textViewEventLocation);
        ImageView imageViewModerator = (ImageView)findViewById(R.id.imageViewModeratorPic);

        textViewDescription.setText(eventDescription);
        textViewModerator.setText(moderator);
        textViewEventLocation.setText(location);

        imageViewModerator.setImageBitmap(loadImageFromStorage(currentEvent.getModeratorPic()));
    }
    private Bitmap loadImageFromStorage(String path) {
        {
            try {
                File f = new File(path);
                Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
                return b;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

}
