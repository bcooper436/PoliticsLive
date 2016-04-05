package com.example.bradleycooper.politicslive;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;

public class Ballot extends AppCompatActivity {

    private CheckBox checkBoxBernie, checkBoxHilary, checkBoxTrump, checkBoxCruz, checkBoxKasich;
    private RelativeLayout layoutBernie, layoutHilary,  layoutTrump, layoutCruz, layoutKasich;

    private int white, black, chosenDemocratColor, chosenRepublicanColor;

    private Button buttonClearSelections, buttonSubmit;
    public String chosenRepublican = "";
    public String chosenDemocrat = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ballot);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        buttonClearSelections = (Button)findViewById(R.id.button_clear_selections);
        buttonClearSelections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chosenRepublican = "";
                chosenDemocrat = "";
                checkBoxBernie.setChecked(false);
                checkBoxHilary.setChecked(false);
                checkBoxTrump.setChecked(false);
                checkBoxCruz.setChecked(false);
                checkBoxKasich.setChecked(false);
                checkBoxHilary.setTextColor(black);
                checkBoxBernie.setTextColor(black);
                checkBoxTrump.setTextColor(black);
                checkBoxCruz.setTextColor(black);
                checkBoxKasich.setTextColor(black);

                layoutHilary.setBackgroundColor(white);
                layoutBernie.setBackgroundColor(white);
                layoutTrump.setBackgroundColor(white);
                layoutCruz.setBackgroundColor(white);
                layoutKasich.setBackgroundColor(white);
            }
        });

        buttonSubmit = (Button)findViewById(R.id.button_submit);
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chosenDemocrat.equals("") || chosenRepublican.equals("")) {
                    AlertDialog alertDialog = new AlertDialog.Builder(Ballot.this).create();
                    alertDialog.setTitle("Error");
                    alertDialog.setMessage("Please select one candidate from each party.");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();

                }
                else {
                    CandidateDataSource ds = new CandidateDataSource(Ballot.this);
                    ds.open();
                    if(!chosenDemocrat.equals("")) {
                        Candidate currentCandidateDNC = ds.getCandidateByName(chosenDemocrat);
                        currentCandidateDNC.setNumberOfVotes(currentCandidateDNC.getNumberOfVotes() + 1);
                        ds.updateCandidate(currentCandidateDNC);
                    }
                    if(!chosenRepublican.equals("")) {
                        Candidate currentCandidateGOP = ds.getCandidateByName(chosenRepublican);
                        currentCandidateGOP.setNumberOfVotes(currentCandidateGOP.getNumberOfVotes() + 1);
                        ds.updateCandidate(currentCandidateGOP);
                    }
                    ds.close();
                    AlertDialog alertDialog = new AlertDialog.Builder(Ballot.this).create();
                    alertDialog.setTitle("Success");
                    alertDialog.setMessage("You votes have been cast for " + chosenDemocrat + " and " + chosenRepublican + ".  If you want to change your vote, just return to this page!");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    Intent intent = new Intent(Ballot.this, MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }
                            });
                    alertDialog.show();

                }
            }
        });
        white = ContextCompat.getColor(getApplicationContext(), R.color.colorWhite);
        black = ContextCompat.getColor(getApplicationContext(), R.color.colorBlack);
        chosenDemocratColor = ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary);
        chosenRepublicanColor = ContextCompat.getColor(getApplicationContext(),R.color.colorAccent);

        layoutBernie = (RelativeLayout)findViewById(R.id.layoutBernie);
        layoutHilary = (RelativeLayout)findViewById(R.id.layoutHilary);
        checkBoxBernie = (CheckBox)findViewById(R.id.checkBoxBernie);
        checkBoxHilary = (CheckBox)findViewById(R.id.checkBoxHilary);
        checkBoxBernie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkBoxBernie.isChecked()) {
                    chosenDemocrat = "Bernie Sanders";
                    layoutBernie.setBackgroundColor(chosenDemocratColor);
                    checkBoxBernie.setTextColor(white);
                    checkBoxHilary.setTextColor(black);
                    layoutHilary.setBackgroundColor(white);
                    checkBoxHilary.setChecked(false);
                }
                else {
                    chosenDemocrat = "";
                    layoutBernie.setBackgroundColor(white);
                    checkBoxBernie.setChecked(false);
                    checkBoxBernie.setTextColor(black);
                }
            }
        });
        checkBoxHilary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkBoxHilary.isChecked()) {
                    chosenDemocrat = "Hilary Clinton";
                    layoutHilary.setBackgroundColor(chosenDemocratColor);
                    layoutBernie.setBackgroundColor(white);
                    checkBoxHilary.setTextColor(white);
                    checkBoxBernie.setTextColor(black);
                    checkBoxBernie.setChecked(false);
                }
                else {
                    chosenDemocrat = "";
                    layoutHilary.setBackgroundColor(white);
                    checkBoxHilary.setChecked(false);
                    checkBoxHilary.setTextColor(black);
                }
            }
        });

        layoutTrump = (RelativeLayout)findViewById(R.id.layoutTrump);
        layoutCruz = (RelativeLayout)findViewById(R.id.layoutCruz);
        layoutKasich = (RelativeLayout)findViewById(R.id.layoutKasich);
        checkBoxTrump = (CheckBox)findViewById(R.id.checkBoxTrump);
        checkBoxCruz = (CheckBox)findViewById(R.id.checkBoxCruz);
        checkBoxKasich = (CheckBox)findViewById(R.id.checkBoxKasich);
        checkBoxTrump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkBoxTrump.isChecked()) {
                    chosenRepublican = "Donald Trump";
                    layoutTrump.setBackgroundColor(chosenRepublicanColor);
                    checkBoxTrump.setTextColor(white);
                    checkBoxCruz.setTextColor(black);
                    checkBoxKasich.setTextColor(black);
                    layoutCruz.setBackgroundColor(white);
                    layoutKasich.setBackgroundColor(white);
                    checkBoxCruz.setChecked(false);
                    checkBoxKasich.setChecked(false);
                }
                else {
                    chosenRepublican = "";
                    layoutTrump.setBackgroundColor(white);
                    checkBoxTrump.setTextColor(black);
                    checkBoxTrump.setChecked(false);
                }
            }
        });
        checkBoxCruz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkBoxCruz.isChecked()) {
                    chosenRepublican = "Ted Cruz";
                    layoutTrump.setBackgroundColor(white);
                    layoutCruz.setBackgroundColor(chosenRepublicanColor);
                    checkBoxTrump.setTextColor(black);
                    checkBoxCruz.setTextColor(white);
                    checkBoxKasich.setTextColor(black);
                    layoutKasich.setBackgroundColor(white);
                    checkBoxTrump.setChecked(false);
                    checkBoxKasich.setChecked(false);
                }
                else {
                    chosenRepublican = "";
                    checkBoxCruz.setChecked(false);
                    layoutCruz.setBackgroundColor(white);
                    checkBoxCruz.setTextColor(black);
                }
            }
        });
        checkBoxKasich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkBoxKasich.isChecked()) {
                    chosenRepublican = "John Kasich";
                    layoutTrump.setBackgroundColor(white);
                    layoutCruz.setBackgroundColor(white);
                    layoutKasich.setBackgroundColor(chosenRepublicanColor);
                    checkBoxTrump.setTextColor(black);
                    checkBoxCruz.setTextColor(black);
                    checkBoxKasich.setTextColor(white);
                    checkBoxTrump.setChecked(false);
                    checkBoxCruz.setChecked(false);
                }
                else {
                    chosenRepublican = "";
                    checkBoxKasich.setChecked(false);
                    checkBoxKasich.setTextColor(black);
                    layoutKasich.setBackgroundColor(white);
                }
            }
        });
    }
}
