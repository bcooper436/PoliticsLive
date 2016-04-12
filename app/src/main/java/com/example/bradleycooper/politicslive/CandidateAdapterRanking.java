package com.example.bradleycooper.politicslive;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
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
    private int totalDemocratVotes, totalRepublicanVotes, candidateVotesDNC, candidateVotesGOP, votePercentage, totalVotesForParty;
    private Bitmap bitmapRepublican, bitmapDemocrat;
    public CandidateAdapterRanking(Context context, ArrayList<Candidate> items) {
        super(context, R.layout.list_item_democrat_ranked, items);
        adapterContext = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        democratColor = ContextCompat.getColor(adapterContext, R.color.colorPrimary);
        republicanColor = ContextCompat.getColor(adapterContext, R.color.colorRed);


        View v = convertView;
        try {
            Candidate candidate = items.get(position);

            if(v == null){
                LayoutInflater vi = (LayoutInflater) adapterContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.list_item_democrat_ranked, null);
            }

            TextView candidateName = (TextView)v.findViewById(R.id.textCandidateName);
            TextView voteCount = (TextView)v.findViewById(R.id.textViewVotePercent);
            TextView ranking = (TextView)v.findViewById(R.id.textViewCandidatePlace);
            ImageView imageView = (ImageView)v.findViewById(R.id.imageViewCandidate);

            if(candidate.getParty().equalsIgnoreCase("GOP")) {
                voteCount.setTextColor(republicanColor);
            }
            else{
                voteCount.setTextColor(democratColor);
            }

            if(candidate.getNumberOfVotes() > 0) {
                CandidateDataSource candidateDataSource = new CandidateDataSource(getContext());
                candidateDataSource.open();
                voteCount.setText(candidateDataSource.getPercentageOfVote(candidate.getCandidateName()));
                String rankingString = (Integer.toString(candidateDataSource.getCandidateRanking(candidate.getCandidateName())));
                StringBuilder sb = new StringBuilder();
                sb.append(rankingString);
                if(rankingString.equalsIgnoreCase("1")) {
                    sb.append("st");
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

        }catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
        return v;
    }
}
