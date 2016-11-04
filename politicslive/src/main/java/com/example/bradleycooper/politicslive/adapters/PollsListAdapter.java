package com.example.bradleycooper.politicslive.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.bradleycooper.politicslive.R;
import com.example.bradleycooper.politicslive.dataobjects.Candidate;

import java.util.ArrayList;

/**
 * Created by Bradley Cooper on 4/26/2016.
 */
public class PollsListAdapter extends ArrayAdapter<Candidate> {
    private ArrayList<Candidate> items;
    private Context adapterContext;
    private Bitmap bitmapRepublican, bitmapDemocrat;
    public PollsListAdapter(Context context, ArrayList<Candidate> items) {
        super(context, R.layout.list_item_democrat, items);
        adapterContext = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        //bitmapDemocrat = BitmapFactory.decodeResource(adapterContext.getResources(), R.drawable.ic_action_dnc_blue);

        View v = convertView;
        try {
            final Candidate candidate = items.get(position);

            if(v == null){
                LayoutInflater vi = (LayoutInflater) adapterContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.card_polls_button, null);
            }

            TextView pollName = (TextView)v.findViewById(R.id.textViewPollName);


            final int colorPressed = ContextCompat.getColor(adapterContext, R.color.colorLayoutPressed);
            final int colorWhite = ContextCompat.getColor(adapterContext, R.color.colorWhite);

            final RelativeLayout relativeLayoutPoll = (RelativeLayout)v.findViewById(R.id.relativeLayoutPoll);
            relativeLayoutPoll.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            relativeLayoutPoll.setBackgroundColor(colorPressed);
                            break;
                        case MotionEvent.ACTION_UP:
                            /*Intent intent = new Intent(getContext(), CandidateProfile.class);
                            intent.putExtra("candidateId", candidate.getCandidateID());
                            adapterContext.startActivity(intent); */
                            relativeLayoutPoll.setBackgroundColor(colorWhite);
                            break;
                        case MotionEvent.ACTION_CANCEL:
                            relativeLayoutPoll.setBackgroundColor(colorWhite);
                            break;
                    }
                    return true;

                }
            });

            /* byte[] byteArray = candidate.getSquarePicture();
            Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            imageView.setImageBitmap(bmp); */
        }catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
        return v;
    }
}
