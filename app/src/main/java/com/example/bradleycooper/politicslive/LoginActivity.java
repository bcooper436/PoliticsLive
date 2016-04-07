package com.example.bradleycooper.politicslive;

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
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    UserDataSource userDataSource;
    User currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final EditText editTextUserName = (EditText)findViewById(R.id.editText_user_name);
        final EditText editTextPassword = (EditText)findViewById(R.id.editText_password);


        Button loginButton = (Button)findViewById(R.id.buttonLogIn);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextUserName.length() > 0 && editTextPassword.length() > 0) {

                    userDataSource = new UserDataSource(LoginActivity.this);
                    userDataSource.open();
                    currentUser = userDataSource.getSpecificUserFromLoginInfo(editTextUserName.getText().toString(), editTextPassword.getText().toString());
                    userDataSource.close();
                    if (currentUser.getUserName() != null) {
                        storeUserPreferences(editTextUserName.getText().toString(), editTextPassword.getText().toString());
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    } else {
                        AlertDialog alertDialog2 = new AlertDialog.Builder(LoginActivity.this).create();
                        alertDialog2.setMessage("Your username or password is incorrect, try again.");
                        alertDialog2.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog2.show();

                    }
                } else {
                    editTextPassword.setHintTextColor(Color.parseColor("#F07D7D"));
                    editTextUserName.setHintTextColor(Color.parseColor("#F07D7D"));
                }
            }
        });
    }

    public void Skip(View view){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Skip","Yes");
        editor.apply();

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    public void CreateNewAccount(View view){
        Intent intent = new Intent(LoginActivity.this, CreateUser.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    public void storeUserPreferences(String username, String password){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Username",username);
        editor.putString("Password", password);
        editor.apply();
    }

}
