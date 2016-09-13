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
 * Created by Bradley Cooper on 9/12/2016.
 */

public class PoliticalPartyAdapter extends ArrayAdapter<PoliticalParty> {
    private ArrayList<PoliticalParty> items;
    private Context adapterContext;
    private Bitmap bitmapRepublican, bitmapDemocrat, bitmapLibertarian, bitmapGreen;
    private int color;
    public PoliticalPartyAdapter(Context context, ArrayList<PoliticalParty> items) {
        super(context, R.layout.list_item_political_party, items);
        adapterContext = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        color = ContextCompat.getColor(adapterContext, R.color.colorPrimary);

        bitmapDemocrat = BitmapFactory.decodeResource(adapterContext.getResources(),R.drawable.ic_action_dnc_blue);
        bitmapRepublican = BitmapFactory.decodeResource(adapterContext.getResources(),R.drawable.ic_action_gop_red);
        bitmapLibertarian = BitmapFactory.decodeResource(adapterContext.getResources(),R.drawable.libertarian_party_sqaure);
        bitmapGreen = BitmapFactory.decodeResource(adapterContext.getResources(),R.drawable.green_party_square);

        View v = convertView;
        try {
            final PoliticalParty politicalParty = items.get(position);

            if(v == null){
                LayoutInflater vi = (LayoutInflater) adapterContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.list_item_political_party, null);
            }

            TextView politicalPartyName = (TextView)v.findViewById(R.id.textPoliticalPartyName);
            TextView numberOfHouseSeats = (TextView)v.findViewById(R.id.textViewHouseSeats);
            TextView numberOfSenateSeats = (TextView)v.findViewById(R.id.textViewSenateSeats);
            ImageView imageView = (ImageView)v.findViewById(R.id.imageViewPoliticalParty);

            final int colorPressed = ContextCompat.getColor(adapterContext, R.color.colorLayoutPressed);
            final int colorWhite = ContextCompat.getColor(adapterContext, R.color.colorWhite);

            final RelativeLayout relativeLayoutPoliticalParty = (RelativeLayout)v.findViewById(R.id.relativeLayoutPoliticalParty);
            relativeLayoutPoliticalParty.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()){
                        case MotionEvent.ACTION_DOWN:
                            relativeLayoutPoliticalParty.setBackgroundColor(colorPressed);
                            break;
                        case MotionEvent.ACTION_UP:
                            Intent intent = new Intent(getContext(), PoliticalPartyProfile.class);
                            intent.putExtra("politicalPartyId", politicalParty.getPoliticalPartyID());
                            adapterContext.startActivity(intent);
                            relativeLayoutPoliticalParty.setBackgroundColor(colorWhite);
                            break;
                        case MotionEvent.ACTION_CANCEL:
                            relativeLayoutPoliticalParty.setBackgroundColor(colorWhite);
                            break;
                    }
                    return true;

                }
            });

            politicalPartyName.setText(politicalParty.getName());
            numberOfHouseSeats.setText(String.valueOf(politicalParty.getNumberOfHouseSeats()));
            numberOfSenateSeats.setText(String.valueOf(politicalParty.getNumberOfSenateSeats()));
            byte[] byteArray = politicalParty.getSquarePicture();
            Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            imageView.setImageBitmap(bmp);
        }catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
        return v;
    }
}
