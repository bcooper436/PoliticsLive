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
public class EventAdapterSmall extends ArrayAdapter<Event> {
    private ArrayList<Event> items;
    private Context adapterContext;
    public EventAdapterSmall(Context context, ArrayList<Event> items) {
        super(context, R.layout.card_event_small, items);
        adapterContext = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        try {
            final Event currentEvent = items.get(position);

            if(v == null){
                LayoutInflater vi = (LayoutInflater) adapterContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.card_event_small, null);
            }

            final RelativeLayout relativeLayoutEventSmall = (RelativeLayout)v.findViewById(R.id.relativeLayoutEventSmall);
            TextView textViewEventSmallName = (TextView)v.findViewById(R.id.textViewEventSmallName);
            TextView textViewEventSmallMonth = (TextView)v.findViewById(R.id.textViewEventSmallMonth);
            TextView textViewEventSmallDay = (TextView)v.findViewById(R.id.textViewEventSmallDay);
            ImageView imageViewEventSmallChannelPic = (ImageView)v.findViewById(R.id.imageViewEventSmallChannelPic);



            textViewEventSmallName.setText(currentEvent.getName());
            textViewEventSmallMonth.setText(formatMonth(currentEvent.getDate()));
            textViewEventSmallDay.setText(formatDay(currentEvent.getDate()));
            byte[] byteArray = currentEvent.getChannelPic();
            Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            imageViewEventSmallChannelPic.setImageBitmap(bmp);

            final int colorPressed = ContextCompat.getColor(adapterContext, R.color.colorMaterialGrey500);
            final int colorWhite = ContextCompat.getColor(adapterContext, R.color.colorMaterialGrey700);
            relativeLayoutEventSmall.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()){
                        case MotionEvent.ACTION_DOWN:
                            relativeLayoutEventSmall.setBackgroundColor(colorPressed);
                            break;
                        case MotionEvent.ACTION_UP:
                            Intent intent = new Intent(getContext(), EventProfile.class);
                            intent.putExtra("eventId", currentEvent.getEventID());
                            adapterContext.startActivity(intent);
                            relativeLayoutEventSmall.setBackgroundColor(colorWhite);
                            break;
                        case MotionEvent.ACTION_CANCEL:
                            relativeLayoutEventSmall.setBackgroundColor(colorWhite);
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
    public String formatMonth(String date){
        String formattedMonth;
        String month = date.substring(0,2);
        switch(month){
            case "01":
                formattedMonth = "Jan";
                break;
            case "02":
                formattedMonth = "Feb";
                break;
            case "03":
                formattedMonth = "Mar";
                break;
            case "04":
                formattedMonth = "Apr";
                break;
            case "05":
                formattedMonth = "May";
                break;
            case "06":
                formattedMonth = "Jun";
                break;
            case "07":
                formattedMonth = "Jul";
                break;
            case "08":
                formattedMonth = "Aug";
                break;
            case "09":
                formattedMonth = "Sep";
                break;
            case "10":
                formattedMonth = "Oct";
                break;
            case "11":
                formattedMonth = "Nov";
                break;
            case "12":
                formattedMonth = "Dec";
                break;
            default:
                formattedMonth = "error";
                break;
        }
        return formattedMonth;
    }
    public String formatDay(String date){
        String formattedDate = "";
        formattedDate = date.substring(2,4);
        return formattedDate;
    }
}
