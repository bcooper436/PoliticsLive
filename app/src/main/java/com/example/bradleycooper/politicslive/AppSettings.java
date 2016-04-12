package com.example.bradleycooper.politicslive;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class AppSettings extends AppCompatActivity {
    UserDataSource userDataSource;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button buttonClearUserTable = (Button)findViewById(R.id.buttonClearUserTable);
        buttonClearUserTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAllUsers();
            }
        });

        Button buttonClearAllVotes = (Button)findViewById(R.id.buttonClearAllVotes);
        buttonClearAllVotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearAllVotes();
            }
        });
        TextView textViewAppVersion = (TextView)findViewById(R.id.textViewAppVersion);
        textViewAppVersion.setOnTouchListener(new View.OnTouchListener() {
            Handler handler = new Handler();

            int numberOfTaps = 0;
            long lastTapTimeMs = 0;
            long touchDownMs = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        touchDownMs = System.currentTimeMillis();
                        break;
                    case MotionEvent.ACTION_UP:
                        handler.removeCallbacksAndMessages(null);

                        if ((System.currentTimeMillis() - touchDownMs) > ViewConfiguration.getTapTimeout()) {
                            //it was not a tap

                            numberOfTaps = 0;
                            lastTapTimeMs = 0;
                            break;
                        }

                        if (numberOfTaps > 0
                                && (System.currentTimeMillis() - lastTapTimeMs) < ViewConfiguration.getDoubleTapTimeout()) {
                            numberOfTaps += 1;
                        } else {
                            numberOfTaps = 1;
                        }

                        lastTapTimeMs = System.currentTimeMillis();

                        if (numberOfTaps == 3) {
                            Toast.makeText(getApplicationContext(), "You are now in developer mode.", Toast.LENGTH_SHORT).show();
                            TextView textViewDeveloperOptions = (TextView)findViewById(R.id.textViewDeveloperOptions);
                            Button buttonDeleteAllUsers = (Button)findViewById(R.id.buttonClearUserTable);
                            Button buttonClearAllVotes = (Button)findViewById(R.id.buttonClearAllVotes);

                            textViewDeveloperOptions.setVisibility(View.VISIBLE);
                            buttonDeleteAllUsers.setVisibility(View.VISIBLE);
                            buttonClearAllVotes.setVisibility(View.VISIBLE);
                        }
                }

                return true;
            }
        });
    }

    private void deleteAllUsers(){
        clearUserPreferences();
        userDataSource = new UserDataSource(AppSettings.this);
        userDataSource.open();
        if(userDataSource.deleteAllUsers()){
            AlertDialog alertDialog = new AlertDialog.Builder(AppSettings.this).create();
            alertDialog.setTitle("Success");
            alertDialog.setMessage("The user table has been cleared");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();

        }
        else{
            AlertDialog alertDialog = new AlertDialog.Builder(AppSettings.this).create();
            alertDialog.setTitle("Error");
            alertDialog.setMessage("The user table has not been cleared");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();

        }
        userDataSource.close();
    }
    private void clearAllVotes(){
        CandidateDataSource candidateDataSource = new CandidateDataSource(AppSettings.this);
        candidateDataSource.open();
        candidateDataSource.clearAllVotes();

        candidateDataSource.close();
    }
    public void clearUserPreferences(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Username","");
        editor.putString("Password", "");
        editor.apply();
    }

}
