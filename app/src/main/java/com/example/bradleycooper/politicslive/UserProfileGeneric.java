package com.example.bradleycooper.politicslive;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

public class UserProfileGeneric extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();

        UserDataSource userDataSource = new UserDataSource(UserProfileGeneric.this);
        userDataSource.open();
        User currentUser = userDataSource.getSpecificUser(extras.getInt("userId"));


        setContentView(R.layout.activity_user_profile_generic);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView imageView = (ImageView)findViewById(R.id.imageViewBackButton);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initUser(currentUser);
    }

    public void initUser(User currentUser){
        TextView textViewDisplayName = (TextView)findViewById(R.id.textViewDisplayName);
        TextView textViewUsername = (TextView)findViewById(R.id.textViewUsername);
        TextView textViewAge = (TextView)findViewById(R.id.textViewAge);
        TextView textViewGender = (TextView)findViewById(R.id.textViewGender);
        TextView textViewParty = (TextView)findViewById(R.id.textViewParty);
        RelativeLayout relativeLayoutUser = (RelativeLayout)findViewById(R.id.relativeLayoutUser);
        ImageView imageViewPartyIcon = (ImageView)findViewById(R.id.imageViewPartyIcon);

        textViewDisplayName.setText(currentUser.getDisplayName());
        textViewUsername.setText(currentUser.getUserName());
        textViewAge.setText(Integer.toString(currentUser.getAge()));
        textViewGender.setText(currentUser.getGender());
        textViewParty.setText(currentUser.getPartyAffiliation());
    }

}
