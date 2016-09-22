package com.example.bradleycooper.politicslive;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.github.mikephil.charting.utils.Utils;

public class CreateUser extends AppCompatActivity {

    User newUser;
    SharedPreferences sharedpreferences;
    private String mGender = "Other/Unaffiliated";
    private String mParty = "Other/Unaffiliated";

    private int selectedIdGender = -1;
    private int selectedIdParty = -1;

    private RadioButton radioButtonGender;
    private RadioButton radioButtonParty;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorBlueDark));
        }

        final EditText editTextFirstName = (EditText) findViewById(R.id.editText_first_name);
        final EditText editTextLastName = (EditText) findViewById(R.id.editText_last_name);
        final EditText editTextUserName = (EditText) findViewById(R.id.editText_username);
        final EditText editTextPassword = (EditText) findViewById(R.id.editText_password);
        final EditText editTextPasswordConfirm = (EditText) findViewById(R.id.editText_password_confirm);
        final EditText editTextAge = (EditText) findViewById(R.id.editText_age);

        final RadioGroup radioGroupGender = (RadioGroup)findViewById(R.id.radioGroupGender);
//        final RadioButton radioButtonGenderMale = (RadioButton)findViewById(R.id.radioButtonMale);
//        final RadioButton radioButtonGenderFemale = (RadioButton)findViewById(R.id.radioButtonFemale);
//        final RadioButton radioButtonGenderOther = (RadioButton)findViewById(R.id.radioButtonOther);
//        radioGroupGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                if (checkedId == R.id.radioButtonMale) {
//                    mGender = "Male";
//                } else if (checkedId == R.id.radioButtonFemale) {
//                    mGender = "Female";
//                } else if (checkedId == R.id.radioButtonOther) {
//                    mGender = "Other/Unaffiliated";
//                }
//            }
//        });


        final RadioGroup radioGroupParty = (RadioGroup)findViewById(R.id.radioGroupParty);
//        final RadioButton radioButtonPartyGOP = (RadioButton)findViewById(R.id.radioButton);
//        final RadioButton radioButtonPartyDNC = (RadioButton)findViewById(R.id.radioButton2);
//        final RadioButton radioButtonPartyOther = (RadioButton)findViewById(R.id.radioButton3);
//        radioGroupParty.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                if (checkedId == R.id.radioButton) {
//                    mParty = "Republican";
//                } else if (checkedId == R.id.radioButton2) {
//                    mParty = "Democrat";
//                } else if (checkedId == R.id.radioButton3) {
//                    mParty = "Other/Unaffiliated";
//                }
//            }
//        });

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

                    if(radioGroupGender.getCheckedRadioButtonId() != -1){
                        selectedIdGender = radioGroupGender.getCheckedRadioButtonId();
                        View radioButton = radioGroupGender.findViewById(selectedIdGender);
                        int radioId = radioGroupGender.indexOfChild(radioButton);
//                        radioButtonGender = (RadioButton) findViewById(selectedIdGender);
                        radioButtonGender = (RadioButton) radioGroupGender.getChildAt(radioId);
                    }else{
                        radioButtonGender = (RadioButton) findViewById(R.id.radioButtonOther);
                    }
//                    int selectedIdGender = radioGroupGender.getCheckedRadioButtonId();
//                    RadioButton radioButtonGender = (RadioButton) findViewById(selectedIdGender);
                    if(radioGroupParty.getCheckedRadioButtonId() != -1){
                        selectedIdParty = radioGroupParty.getCheckedRadioButtonId();
                        View radioButton = radioGroupParty.findViewById(selectedIdParty);
                        int radioId = radioGroupParty.indexOfChild(radioButton);
                        radioButtonParty = (RadioButton) radioGroupParty.getChildAt(radioId);
                    }else{
                        radioButtonParty = (RadioButton) findViewById(R.id.radioButtonIndependent);
                    }
//                    int selectedIdParty = radioGroupParty.getCheckedRadioButtonId();
//                    RadioButton radioButtonParty = (RadioButton) findViewById(selectedIdParty);

                    newUser = new User();
                    newUser.setDisplayName(editTextFirstName.getText().toString() + " " + editTextLastName.getText().toString());
                    newUser.setUserName(editTextUserName.getText().toString());
                    newUser.setPassword(editTextPassword.getText().toString());
                    if(TextUtils.isEmpty(editTextAge.getText().toString())){
                        newUser.setAge(0);
                    }else{
                        newUser.setAge(Integer.parseInt(editTextAge.getText().toString()));
                    }

                    newUser.setGender(radioButtonGender.getText().toString());
                    newUser.setPartyAffiliation(radioButtonParty.getText().toString());
//                    newUser.setGender(mGender);
//
//                    newUser.setPartyAffiliation(mParty);


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
                        storeUserPreferences(newUser.getUserName(), newUser.getPassword());
                        AlertDialog alertDialog = new AlertDialog.Builder(CreateUser.this).create();
                        alertDialog.setTitle("Success");
                        alertDialog.setMessage("Your account has been created");
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

    public void storeUserPreferences(String username, String password){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Username",username);
        editor.putString("Password",password);
        editor.apply();
    }
}