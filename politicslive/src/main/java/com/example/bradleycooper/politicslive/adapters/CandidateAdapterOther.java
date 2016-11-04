package com.example.bradleycooper.politicslive.adapters;

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

import com.example.bradleycooper.politicslive.activities.CandidateProfile;
import com.example.bradleycooper.politicslive.R;
import com.example.bradleycooper.politicslive.dataobjects.Candidate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
            ImageView imageView = (ImageView)v.findViewById(R.id.imageViewPoliticalParty);

            candidateName.setText(candidate.getCandidateName());
            //byte[] byteArray = candidate.getSquarePicture();
            //Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            //imageView.setImageBitmap(bmp);
            imageView.setImageBitmap(loadImageFromStorage(candidate.getSquarePicture()));

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
    private Bitmap loadImageFromStorage(String path) {
        {
            try {
                File f = new File(path);
                Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
                //ImageView img = (ImageView) getActivity().findViewById(R.id.imageViewTesting);
                //img.setImageBitmap(b);
                return b;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

}
