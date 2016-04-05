package com.example.bradleycooper.politicslive;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class CandidateProfile extends AppCompatActivity {

    Candidate currentCandidate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();

        setContentView(R.layout.activity_candidate_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initCandidate(extras.getInt("candidateId"));
    }
    private void initCandidate(int id) {
        CandidateDataSource ds = new CandidateDataSource(CandidateProfile.this);
        ds.open();
        currentCandidate = ds.getSpecificCandidate(id);

        TextView textView_name = (TextView)findViewById(R.id.textViewCandidateName);
        ImageView imageViewWide = (ImageView)findViewById(R.id.imageViewWide);

        textView_name.setText(currentCandidate.getCandidateName());
        byte[] byteArray = currentCandidate.getWidePicture();
        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        imageViewWide.setImageBitmap(bmp);
    }
}
