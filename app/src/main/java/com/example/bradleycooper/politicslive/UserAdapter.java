package com.example.bradleycooper.politicslive;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Bradley Cooper on 4/5/2016.
 */
public class UserAdapter extends ArrayAdapter<User> {
    private ArrayList<User> items;
    private Context adapterContext;
    private int colorPress, colorWhite;

    public UserAdapter(Context context, ArrayList<User> items) {
        super(context, R.layout.list_item_user, items);
        adapterContext = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        colorPress = ContextCompat.getColor(adapterContext, R.color.colorLayoutPressedLight);
        colorWhite = ContextCompat.getColor(adapterContext, R.color.colorWhite);

        View v = convertView;
        try {
            final User user = items.get(position);
            if(v == null){
                LayoutInflater vi = (LayoutInflater) adapterContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.list_item_user, null);
            }
            TextView textViewDisplayName = (TextView)v.findViewById(R.id.textViewDisplayName);
            TextView textViewUsername = (TextView)v.findViewById(R.id.textViewUsername);
            TextView textViewAge = (TextView)v.findViewById(R.id.textViewAge);
            TextView textViewGender = (TextView)v.findViewById(R.id.textViewGender);
            TextView textViewParty = (TextView)v.findViewById(R.id.textViewParty);

            textViewDisplayName.setText(user.getDisplayName());
            textViewUsername.setText(user.getUserName());
            textViewAge.setText(Integer.toString(user.getAge()));
            textViewGender.setText(user.getGender());
            textViewParty.setText(user.getPartyAffiliation());

            final RelativeLayout relativeLayoutUser = (RelativeLayout)v.findViewById(R.id.relativeLayoutUser);
            relativeLayoutUser.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()){
                        case MotionEvent.ACTION_DOWN:
                            relativeLayoutUser.setBackgroundColor(colorPress);
                            break;
                        case MotionEvent.ACTION_UP:
                            Intent intent = new Intent(getContext(), UserProfileGeneric.class);
                            intent.putExtra("userId", user.getUserID());
                            adapterContext.startActivity(intent);
                            relativeLayoutUser.setBackgroundColor(colorWhite);
                            break;
                        case MotionEvent.ACTION_CANCEL:
                            relativeLayoutUser.setBackgroundColor(colorWhite);
                            break;
                    }
                    return true;

                }
            });


        }catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
        return v;
    }

    public void sortList(String sortString){

    }
}
