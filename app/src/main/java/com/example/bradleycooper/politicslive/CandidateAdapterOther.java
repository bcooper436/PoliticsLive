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
public class CandidateAdapterOther extends ArrayAdapter<Candidate> {
    private ArrayList<Candidate> items;
    private Context adapterContext;
    public CandidateAdapterOther(Context context, ArrayList<Candidate> items) {
        super(context, R.layout.list_item_democrat, items);
        adapterContext = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        try {
            final Candidate candidate = items.get(position);

            if(v == null){
                LayoutInflater vi = (LayoutInflater) adapterContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.card_candidate_other, null);
            }

            TextView candidateName = (TextView)v.findViewById(R.id.textCandidateName);
            ImageView imageView = (ImageView)v.findViewById(R.id.imageViewCandidate);

            candidateName.setText(candidate.getCandidateName());
            byte[] byteArray = candidate.getSquarePicture();
            Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            imageView.setImageBitmap(bmp);

            final int colorDownLight = ContextCompat.getColor(adapterContext, R.color.colorLayoutPressed);

            final int colorWhite = ContextCompat.getColor(adapterContext, R.color.colorWhite);

            final RelativeLayout relativeLayoutCandidateOther = (RelativeLayout)v.findViewById(R.id.relativeLayoutCandidateOther);
            relativeLayoutCandidateOther.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            relativeLayoutCandidateOther.setBackgroundColor(colorDownLight);
                            break;
                        case MotionEvent.ACTION_UP:
                            Intent intent = new Intent(getContext(), CandidateProfile.class);
                            intent.putExtra("candidateId", candidate.getCandidateID());
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            adapterContext.startActivity(intent);
                            relativeLayoutCandidateOther.setBackgroundColor(colorWhite);
                            break;
                        case MotionEvent.ACTION_CANCEL:
                            relativeLayoutCandidateOther.setBackgroundColor(colorWhite);
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
}
