package com.example.bradleycooper.politicslive;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CandidateProfile extends AppCompatActivity {

    Candidate currentCandidate;
    private int idDNC, democratColor, democratColorLight, democratColorDark, idGOP, republicanColor, republicanColorLight, republicanColorDark;
    private String candidateParty;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();

        CandidateDataSource ds = new CandidateDataSource(CandidateProfile.this);
        ds.open();
        currentCandidate = ds.getSpecificCandidate(extras.getInt("candidateId"));

        if(currentCandidate.getParty().equalsIgnoreCase("DNC")) {
            super.setTheme(R.style.AppThemeDemocrat);
        } else{
            super.setTheme(R.style.AppThemeDemocrat);
        }
        setContentView(R.layout.activity_candidate_profile);



        democratColor = ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary);
        democratColorDark = ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark);
        democratColorLight = ContextCompat.getColor(getApplicationContext(), R.color.colorBlueLight);
        republicanColor = ContextCompat.getColor(getApplicationContext(), R.color.colorRedDark);
        republicanColorLight = ContextCompat.getColor(getApplicationContext(), R.color.colorRedLight);
        republicanColorDark = ContextCompat.getColor(getApplicationContext(), R.color.colorRedDarker);
        idGOP = getResources().getIdentifier("com.example.bradleycooper.politicslive:drawable/ic_action_gop_red_light", null, null);
        idDNC = getResources().getIdentifier("com.example.bradleycooper.politicslive:drawable/ic_action_dnc_blue_light", null, null);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        candidateParty = currentCandidate.getParty();

        initCandidate(extras.getInt("candidateId"));
    }
    private void initCandidate(int id) {
        CandidateDataSource ds = new CandidateDataSource(CandidateProfile.this);
        ds.open();
        currentCandidate = ds.getSpecificCandidate(id);

        TextView textView_name = (TextView)findViewById(R.id.textViewCandidateName);
        TextView textView_description = (TextView)findViewById(R.id.textViewDescription);
        ImageView imageViewPartyIcon = (ImageView)findViewById(R.id.imageViewPartyIcon);
        ImageView imageViewWide = (ImageView)findViewById(R.id.imageViewWide);
        RelativeLayout relativeLayoutPartyColor = (RelativeLayout)findViewById(R.id.relativeLayoutPartyColor);

        textView_name.setText(currentCandidate.getCandidateName());
        textView_description.setText(currentCandidate.getCandidateDescription());
        if(candidateParty.equalsIgnoreCase("DNC")) {
            imageViewPartyIcon.setImageResource(idDNC);
            relativeLayoutPartyColor.setBackgroundColor(democratColor);
        }
        else{
            imageViewPartyIcon.setImageResource(idGOP);
            relativeLayoutPartyColor.setBackgroundColor(republicanColor);
        }
        byte[] byteArray = currentCandidate.getWidePicture();
        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        imageViewWide.setImageBitmap(bmp);
    }
}
