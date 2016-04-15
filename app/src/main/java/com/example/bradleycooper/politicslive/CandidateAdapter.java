package com.example.bradleycooper.politicslive;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Bradley Cooper on 4/4/2016.
 */
public class CandidateAdapter extends ArrayAdapter<Candidate> {
    private ArrayList<Candidate> items;
    private Context adapterContext;
    private int democratColor, republicanColor;
    private int totalDemocratVotes, totalRepublicanVotes, candidateVotesDNC, candidateVotesGOP, votePercentage, totalVotesForParty;
    private Bitmap bitmapRepublican, bitmapDemocrat;
    public CandidateAdapter(Context context, ArrayList<Candidate> items) {
        super(context, R.layout.list_item_democrat, items);
        adapterContext = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        democratColor = ContextCompat.getColor(adapterContext, R.color.colorPrimary);
        republicanColor = ContextCompat.getColor(adapterContext, R.color.colorRed);

        bitmapDemocrat = BitmapFactory.decodeResource(adapterContext.getResources(),R.drawable.ic_action_dnc_blue);
        bitmapRepublican = BitmapFactory.decodeResource(adapterContext.getResources(),R.drawable.ic_action_gop_red);

        View v = convertView;
        try {
            final Candidate candidate = items.get(position);

            if(v == null){
                LayoutInflater vi = (LayoutInflater) adapterContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.list_item_democrat, null);
            }

            TextView candidateName = (TextView)v.findViewById(R.id.textCandidateName);
            TextView voteCount = (TextView)v.findViewById(R.id.textViewVotePercent);
            ImageView imageView = (ImageView)v.findViewById(R.id.imageViewCandidate);
            ImageView imageViewParty = (ImageView)v.findViewById(R.id.imageViewParty);

            if(candidate.getParty().equalsIgnoreCase("GOP")) {
                voteCount.setTextColor(republicanColor);
                imageViewParty.setImageBitmap(bitmapRepublican);
            }
            else{
                voteCount.setTextColor(democratColor);
                imageViewParty.setImageBitmap(bitmapDemocrat);
            }

            final int colorPressed = ContextCompat.getColor(adapterContext, R.color.colorLayoutPressed);
            final int colorWhite = ContextCompat.getColor(adapterContext, R.color.colorWhite);

            final RelativeLayout relativeLayoutCandidate = (RelativeLayout)v.findViewById(R.id.relativeLayoutCandidate);
            relativeLayoutCandidate.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()){
                        case MotionEvent.ACTION_DOWN:
                            relativeLayoutCandidate.setBackgroundColor(colorPressed);
                            break;
                        case MotionEvent.ACTION_UP:
                            Intent intent = new Intent(getContext(), CandidateProfile.class);
                            intent.putExtra("candidateId", candidate.getCandidateID());
                            adapterContext.startActivity(intent);
                            relativeLayoutCandidate.setBackgroundColor(colorWhite);
                            break;
                        case MotionEvent.ACTION_CANCEL:
                            relativeLayoutCandidate.setBackgroundColor(colorWhite);
                            break;
                    }
                    return true;

                }
            });

            if(candidate.getNumberOfVotes() > 0) {
                CandidateDataSource candidateDataSource = new CandidateDataSource(getContext());
                candidateDataSource.open();
                voteCount.setText(candidateDataSource.getPercentageOfVote(candidate.getCandidateName()));
                candidateDataSource.close();
            }
            candidateName.setText(candidate.getCandidateName());
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
