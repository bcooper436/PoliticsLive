package com.example.bradleycooper.politicslive;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Bradley Cooper on 4/5/2016.
 */
public class UserAdapter extends ArrayAdapter<User> {
    private ArrayList<User> items;
    private Context adapterContext;

    public UserAdapter(Context context, ArrayList<User> items) {
        super(context, R.layout.list_item_user, items);
        adapterContext = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        try {
            User user = items.get(position);
            if(v == null){
                LayoutInflater vi = (LayoutInflater) adapterContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.list_item_user, null);
            }
            TextView textViewDisplayName = (TextView)v.findViewById(R.id.textViewDisplayName);
            TextView textViewUsername = (TextView)v.findViewById(R.id.textViewUsername);
            /*TextView textViewAge = (TextView)v.findViewById(R.id.textViewAge);
            TextView textViewGender = (TextView)v.findViewById(R.id.textViewGender);*/

            textViewDisplayName.setText(user.getDisplayName());
            textViewUsername.setText(user.getUserName()); /*
            textViewAge.setText(Integer.toString(user.getAge()));
            textViewGender.setText(user.getGender());*/

        }catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
        return v;
    }
}
