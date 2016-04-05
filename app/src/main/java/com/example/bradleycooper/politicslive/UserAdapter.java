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
        super(context, R.layout.list_item_democrat, items);
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
                v = vi.inflate(R.layout.list_item_democrat, null);
            }

            TextView userName = (TextView)v.findViewById(R.id.textCandidateName);

            userName.setText(user.getDisplayName());

        }catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
        return v;
    }
}
