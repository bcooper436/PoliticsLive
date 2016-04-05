package com.example.bradleycooper.politicslive;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class CreateUser extends AppCompatActivity {

    User newUser;
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;
    public static final String Username = "userNameKey";
    public static final String Password = "passwordKey";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        final EditText editTextFirstName = (EditText) findViewById(R.id.editText_first_name);
        final EditText editTextLastName = (EditText) findViewById(R.id.editText_last_name);
        final EditText editTextUserName = (EditText) findViewById(R.id.editText_user_name);
        final EditText editTextPassword = (EditText) findViewById(R.id.editText_password);
        final EditText editTextPasswordConfirm = (EditText) findViewById(R.id.editText_password_confirm);
        final EditText editTextAge = (EditText) findViewById(R.id.editText_age);

        final RadioGroup radioGroupGender = (RadioGroup)findViewById(R.id.radioGroupGender);
        final RadioGroup radioGroupParty = (RadioGroup)findViewById(R.id.radioGroupParty);

        Button createAccountButton = (Button) findViewById(R.id.buttonCreateAccount);
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextUserName.length() > 0
                        && editTextPassword.length() > 0
                        && editTextPasswordConfirm.length() > 0
                        && editTextFirstName.length() > 0
                        && editTextLastName.length() > 0
                        && editTextPassword.getText().toString().equals(editTextPasswordConfirm.getText().toString())
                        ) {

                    int selectedIdGender = radioGroupGender.getCheckedRadioButtonId();
                    RadioButton radioButtonGender = (RadioButton) findViewById(selectedIdGender);
                    int selectedIdParty = radioGroupParty.getCheckedRadioButtonId();
                    RadioButton radioButtonParty = (RadioButton) findViewById(selectedIdParty);

                    newUser = new User();
                    newUser.setDisplayName(editTextFirstName.getText().toString() + " " + editTextLastName.getText().toString());
                    newUser.setUserName(editTextUserName.getText().toString());
                    newUser.setPassword(editTextPassword.getText().toString());
                    newUser.setAge(Integer.parseInt(editTextAge.getText().toString()));
                    newUser.setGender(radioButtonGender.getText().toString());
                    newUser.setPartyAffiliation(radioButtonParty.getText().toString());

                    UserDataSource ds = new UserDataSource(CreateUser.this);
                    ds.open();


                    boolean wasSuccessful = false;
                    if(newUser.getUserID() == -1) {
                        wasSuccessful = ds.insertUser(newUser);
                        int newId = ds.getLastUserId();
                        newUser.setUserID(newId);
                    }
                    else {
                        wasSuccessful = ds.updateUser(newUser);
                    }
                    ds.close();

                    if(wasSuccessful) {
                        storeUserPreferences();
                        AlertDialog alertDialog = new AlertDialog.Builder(CreateUser.this).create();
                        alertDialog.setTitle("Success");
                        alertDialog.setMessage("Your account is created. Password= " + newUser.getPassword() + ", Username = " + newUser.getUserName() + ", Display Name = " + newUser.getDisplayName() + ", Age = " + newUser.getAge() + ", Party = " + newUser.getPartyAffiliation() + ", Gender = " + newUser.getGender());
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        Intent intent = new Intent(CreateUser.this, MainActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                    }
                                });
                        alertDialog.show();
                    }
                    else {
                        AlertDialog alertDialog = new AlertDialog.Builder(CreateUser.this).create();
                        alertDialog.setTitle("Failure");
                        alertDialog.setMessage("Something went wrong, your account was not created. Please try again later. ");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                    }
                }
                    else {
                    editTextPassword.setHintTextColor(Color.parseColor("#F07D7D"));
                    editTextPasswordConfirm.setHintTextColor(Color.parseColor("#F07D7D"));
                    editTextUserName.setHintTextColor(Color.parseColor("#F07D7D"));
                    editTextFirstName.setHintTextColor(Color.parseColor("#F07D7D"));
                    editTextLastName.setHintTextColor(Color.parseColor("#F07D7D"));
                }
            }
        });

    }

    public void choosePicture(View view) {
    }

    public void storeUserPreferences(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Username","Bcooper");
        editor.apply();
    }
}