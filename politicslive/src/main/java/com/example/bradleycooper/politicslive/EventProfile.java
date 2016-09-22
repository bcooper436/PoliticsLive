package com.example.bradleycooper.politicslive;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class EventProfile extends AppCompatActivity {

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

        democratColor = ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary);
        democratColorDark = ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark);
        democratColorDarker = ContextCompat.getColor(getApplicationContext(), R.color.colorBlueDarker);

        democratColorLight = ContextCompat.getColor(getApplicationContext(), R.color.colorBlueLight);
        republicanColor = ContextCompat.getColor(getApplicationContext(), R.color.colorRedDark);
        republicanColorLight = ContextCompat.getColor(getApplicationContext(), R.color.colorRedLight);
        republicanColorDarker = ContextCompat.getColor(getApplicationContext(), R.color.colorRedDarker);
        idGOP = getResources().getIdentifier("com.example.bradleycooper.politicslive:drawable/ic_action_gop_red_light", null, null);
        idDNC = getResources().getIdentifier("com.example.bradleycooper.politicslive:drawable/ic_action_dnc_blue_light", null, null);

        EventDataSource eds = new EventDataSource(EventProfile.this);
        eds.open();
        currentEvent = eds.getSpecificEvent(extras.getInt("eventId"));
        eds.close();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.BLACK);
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_event_profile);


        imgView = (ImageView)findViewById(R.id.imageViewBackButton);
        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        initProfileResources();
        initEvent(extras.getInt("eventId"));
        //initOtherCandidates(candidateName);
        //initIssues(candidateName);
    }
    /*
    private void initOtherCandidates(String nameOfCurrentCandidate){
        CandidateDataSource candidateDataSource = new CandidateDataSource(this);
        candidateDataSource.open();
        ArrayList<Candidate> arrayListCandidatesOther = candidateDataSource.getOtherCandidates(nameOfCurrentCandidate);
        candidateDataSource.close();
        CandidateAdapterOther candidateAdapterOther = new CandidateAdapterOther(this, arrayListCandidatesOther);
        LinearLayout linearLayoutCandidatesOther = (LinearLayout)findViewById(R.id.linearLayoutOtherCandidates);

        final int adapterCount = candidateAdapterOther.getCount();
        for(int i = 0 ; i < adapterCount ; i++) {
            final int iFinal = i;
            View item = candidateAdapterOther.getView(i, null, null);
            linearLayoutCandidatesOther.addView(item);
        }
    }*/
    private void initProfileResources() {
        final TextView textViewSite = (TextView) findViewById(R.id.textViewSite);

        colorDown = ContextCompat.getColor(EventProfile.this, R.color.colorLayoutPressed);
        colorUp = ContextCompat.getColor(EventProfile.this, R.color.colorBackgroundGrey);

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
        eventName = currentEvent.getName();
        eventDescription = currentEvent.getDescription();
        date = currentEvent.getDate();
        location = currentEvent.getLocation();
        moderator = currentEvent.getModerator();
        site = currentEvent.getSite();
        channel = currentEvent.getChannel();
        time = currentEvent.getTime();



        TextView textViewName = (TextView)findViewById(R.id.textViewEventName);
        TextView textViewDescription = (TextView)findViewById(R.id.textViewEventDescription);
        TextView textViewModerator = (TextView)findViewById(R.id.textViewEventModerator);
        TextView textViewEventLocation = (TextView)findViewById(R.id.textViewEventLocation);
        ImageView imageViewWide = (ImageView)findViewById(R.id.imageViewEventChannelPic);

        textViewName.setText(eventName);
        textViewDescription.setText(eventDescription);
        textViewModerator.setText(moderator);
        textViewEventLocation.setText(location);


        /*byte[] byteArray = currentEvent.getChannelPic();
        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        imageViewWide.setImageBitmap(bmp);*/



    }
}
