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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Created by Bradley Cooper on 9/12/2016.
 */
public class PoliticalPartyAdapterSmall extends ArrayAdapter<PoliticalParty> {
    private ArrayList<PoliticalParty> items;
    private Context adapterContext;
    private Bitmap bitmapRepublican, bitmapDemocrat, bitmapLibertarian, bitmapGreen;
    private int color;
    public PoliticalPartyAdapterSmall(Context context, ArrayList<PoliticalParty> items) {
        super(context, R.layout.card_political_party_small, items);
        adapterContext = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        try {
            final PoliticalParty politicalParty = items.get(position);

            if(v == null){
                LayoutInflater vi = (LayoutInflater) adapterContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.card_political_party_small, null);
            }

            final RelativeLayout relativeLayoutPoliticalPartySmall = (RelativeLayout)v.findViewById(R.id.relativeLayoutPoliticalPartySmall);
            TextView politicalPartyLabel = (TextView)v.findViewById(R.id.textViewPoliticalPartyLabel);
            ImageView imageView = (ImageView)v.findViewById(R.id.imageViewPoliticalPartyIcon);

            politicalPartyLabel.setText(politicalParty.getName());
            imageView.setImageBitmap(loadImageFromStorage(politicalParty.getSquarePicture()));

            final int colorPressed = ContextCompat.getColor(adapterContext, R.color.colorLayoutPressed);
            final int colorWhite = ContextCompat.getColor(adapterContext, R.color.colorWhite);
            relativeLayoutPoliticalPartySmall.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()){
                        case MotionEvent.ACTION_DOWN:
                            relativeLayoutPoliticalPartySmall.setBackgroundColor(colorPressed);
                            break;
                        case MotionEvent.ACTION_UP:
                            Intent intent = new Intent(getContext(), PoliticalPartyProfile.class);
                            intent.putExtra("politicalPartyId", politicalParty.getPoliticalPartyID());
                            adapterContext.startActivity(intent);
                            relativeLayoutPoliticalPartySmall.setBackgroundColor(colorWhite);
                            break;
                        case MotionEvent.ACTION_CANCEL:
                            relativeLayoutPoliticalPartySmall.setBackgroundColor(colorWhite);
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
