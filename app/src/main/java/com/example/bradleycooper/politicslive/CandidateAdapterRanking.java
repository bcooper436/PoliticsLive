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

import java.util.ArrayList;

/**
 * Created by Bradley Cooper on 4/4/2016.
 */
public class CandidateAdapterRanking extends ArrayAdapter<Candidate> {
    private ArrayList<Candidate> items;
    private Context adapterContext;
    private int democratColor, republicanColor;
    private int colorPrimary,colorPrimaryDark , colorMotionPress, colorMotionPressGOP, colorMotionPressDNC, democratColorDark, republicanColorDark, totalDemocratVotes, totalRepublicanVotes, candidateVotesDNC, candidateVotesGOP, votePercentage, totalVotesForParty;
    private Bitmap bitmapRepublican, bitmapDemocrat;
    public CandidateAdapterRanking(Context context, ArrayList<Candidate> items) {
        super(context, R.layout.list_item_democrat_ranked, items);
        adapterContext = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        democratColor = ContextCompat.getColor(adapterContext, R.color.colorBlueLight);
        republicanColor = ContextCompat.getColor(adapterContext, R.color.colorRedLight);
        colorMotionPressDNC = ContextCompat.getColor(adapterContext, R.color.colorBlueLight2);
        colorMotionPressGOP = ContextCompat.getColor(adapterContext, R.color.colorRedLight2);
        democratColorDark = ContextCompat.getColor(adapterContext, R.color.colorBlueDark);
        republicanColorDark = ContextCompat.getColor(adapterContext, R.color.colorRedDarker);

        View v = convertView;
        try {
            final Candidate candidate = items.get(position);

            if(v == null){
                LayoutInflater vi = (LayoutInflater) adapterContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.list_item_democrat_ranked, null);
            }

            TextView candidateName = (TextView)v.findViewById(R.id.textCandidateName);
            TextView voteCount = (TextView)v.findViewById(R.id.textViewVotePercent);
            TextView ranking = (TextView)v.findViewById(R.id.textViewCandidatePlace);
            ImageView imageView = (ImageView)v.findViewById(R.id.imageViewPoliticalParty);
            ImageView imageViewCrown = (ImageView)v.findViewById(R.id.imageViewCrown);

            final RelativeLayout relativeLayoutPartyColor = (RelativeLayout)v.findViewById(R.id.relativeLayoutPartyColor);
            if(candidate.getParty().equalsIgnoreCase("GOP")) {
                relativeLayoutPartyColor.setBackgroundColor(republicanColor);
                colorPrimary = republicanColor;
                colorMotionPress = colorMotionPressGOP;
                colorPrimaryDark = republicanColorDark;
            }
            else{
                relativeLayoutPartyColor.setBackgroundColor(democratColor);
                colorPrimary = democratColor;
                colorMotionPress = colorMotionPressDNC;
                colorPrimaryDark = democratColorDark;
            }
            relativeLayoutPartyColor.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()){
                        case MotionEvent.ACTION_DOWN:
                            relativeLayoutPartyColor.setBackgroundColor(colorMotionPress);
                            break;
                        case MotionEvent.ACTION_UP:
                            Intent intent = new Intent(getContext(), CandidateProfile.class);
                            intent.putExtra("candidateId", candidate.getCandidateID());
                            adapterContext.startActivity(intent);
                            relativeLayoutPartyColor.setBackgroundColor(colorPrimary);
                            break;
                        case MotionEvent.ACTION_CANCEL:
                            relativeLayoutPartyColor.setBackgroundColor(colorPrimary);
                            break;
                    }
                    return true;

                }
            });

            if(candidate.getNumberOfVotes() > 0) {
                CandidateDataSource candidateDataSource = new CandidateDataSource(getContext());
                candidateDataSource.open();
                voteCount.setText(candidateDataSource.getPercentageOfVote(candidate.getCandidateName()));
                String rankingString = (Integer.toString(candidateDataSource.getCandidateRanking(candidate.getCandidateName())));
                StringBuilder sb = new StringBuilder();
                sb.append(rankingString);
                if(rankingString.equalsIgnoreCase("1")) {
                    sb.append("st");
                    imageViewCrown.setVisibility(View.VISIBLE);
                }
                else if(rankingString.equalsIgnoreCase("2")){
                    sb.append("nd");
                }
                else if(rankingString.equalsIgnoreCase("3")){
                    sb.append("rd");
                }
                else {
                    sb.append("th");
                }
                sb.append(":");
                ranking.setText(sb.toString());
                candidateDataSource.close();
            }
            candidateName.setText(candidate.getCandidateName());
            byte[] byteArray = candidate.getSquarePicture();
            Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            imageView.setImageBitmap(bmp);
            voteCount.setTextColor(colorPrimaryDark);
        }catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
        return v;
    }
}
