package com.example.bradleycooper.politicslive;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

public class CandidateProfile extends AppCompatActivity {

    Candidate currentCandidate;
    private int idDNC, democratColor, democratColorLight, democratColorDark, idGOP, republicanColor, republicanColorLight, republicanColorDark;
    private String candidateParty;
    private Toolbar toolbar;
    private ImageView imgView;
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.BLACK);
        }


        democratColor = ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary);
        democratColorDark = ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark);
        democratColorLight = ContextCompat.getColor(getApplicationContext(), R.color.colorBlueLight);
        republicanColor = ContextCompat.getColor(getApplicationContext(), R.color.colorRedDark);
        republicanColorLight = ContextCompat.getColor(getApplicationContext(), R.color.colorRedLight);
        republicanColorDark = ContextCompat.getColor(getApplicationContext(), R.color.colorRedDarker);
        idGOP = getResources().getIdentifier("com.example.bradleycooper.politicslive:drawable/ic_action_gop_red_light", null, null);
        idDNC = getResources().getIdentifier("com.example.bradleycooper.politicslive:drawable/ic_action_dnc_blue_light", null, null);

        imgView = (ImageView)findViewById(R.id.imageViewBackButton);
        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        candidateParty = currentCandidate.getParty();

        initCandidate(extras.getInt("candidateId"));
    }
    private void initCandidate(int id) {
        CandidateDataSource ds = new CandidateDataSource(CandidateProfile.this);
        ds.open();
        currentCandidate = ds.getSpecificCandidate(id);

        TextView textView_name = (TextView)findViewById(R.id.textViewCandidateName);
        TextView textView_description = (TextView)findViewById(R.id.textViewDescription);
        ImageView imageViewWide = (ImageView)findViewById(R.id.imageViewWidePicture);

        TextView textView_total_votes = (TextView)findViewById(R.id.textViewTotalVotes);
        TextView textView_average_age = (TextView)findViewById(R.id.textViewAverageAge);
        TextView textView_male_percentage = (TextView)findViewById(R.id.textViewMalePercentage);
        TextView textView_female_percentage = (TextView)findViewById(R.id.textViewFemalePercentage);

        textView_name.setText(currentCandidate.getCandidateName());
        textView_description.setText(currentCandidate.getCandidateDescription());
        if(candidateParty.equalsIgnoreCase("DNC")) {
            textView_total_votes.setBackgroundResource(R.drawable.circle_dnc);
            textView_average_age.setBackgroundResource(R.drawable.circle_dnc);
            textView_male_percentage.setBackgroundResource(R.drawable.circle_dnc);
            textView_female_percentage.setBackgroundResource(R.drawable.circle_dnc);
        }
        else{
            textView_total_votes.setBackgroundResource(R.drawable.circle_gop);
            textView_average_age.setBackgroundResource(R.drawable.circle_gop);
            textView_male_percentage.setBackgroundResource(R.drawable.circle_gop);
            textView_female_percentage.setBackgroundResource(R.drawable.circle_gop);
        }
        textView_total_votes.setText(Integer.toString(currentCandidate.getNumberOfVotes()));

        byte[] byteArray = currentCandidate.getWidePicture();
        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        imageViewWide.setImageBitmap(bmp);
        ds.close();
    }
}
