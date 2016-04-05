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
 * Created by Bradley Cooper on 4/4/2016.
 */
public class CandidateAdapter extends ArrayAdapter<Candidate> {
    private ArrayList<Candidate> items;
    private Context adapterContext;

    public CandidateAdapter(Context context, ArrayList<Candidate> items) {
        super(context, R.layout.list_item_democrat, items);
        adapterContext = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        try {
            Candidate candidate = items.get(position);

            if(v == null){
                LayoutInflater vi = (LayoutInflater) adapterContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.list_item_democrat, null);
            }

            TextView candidateName = (TextView)v.findViewById(R.id.textCandidateName);
            TextView voteCount = (TextView)v.findViewById(R.id.textViewVoteCount);
            ImageView imageView = (ImageView)v.findViewById(R.id.userPictureThumb);


            candidateName.setText(candidate.getCandidateName());
            String voteCountString = Integer.toString(candidate.getNumberOfVotes());
            voteCount.setText(voteCountString);
            byte[] byteArray = candidate.getSquarePicture();
            Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            imageView.setImageBitmap(bmp);

        }catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
        return v;
    }
}
