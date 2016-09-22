package com.example.bradleycooper.politicslive;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bradley Cooper on 9/12/2016.
 */
public class EventDataSource {
    private SQLiteDatabase database;
    private EventDBHelper dbHelper;

    public EventDataSource(Context context) {
        dbHelper = new EventDBHelper(context);
    }

    public void open() throws SQLiteException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public boolean insertEvent(Event event) {
        boolean didSucceed = false;
        try {
            ContentValues initialValues = new ContentValues();

            initialValues.put("name", event.getName());
            initialValues.put("description", event.getDescription());
            initialValues.put("date", event.getDate());
            initialValues.put("time", event.getTime());
            initialValues.put("channel", event.getChannel());
            initialValues.put("channelpic", event.getChannelPic());
            initialValues.put("moderator", event.getModerator());
            initialValues.put("moderatorpic", event.getModeratorPic());
            initialValues.put("site", event.getSite());
            initialValues.put("location", event.getLocation());

            didSucceed = database.insert("event", null, initialValues) > 0;
        }
        catch (Exception e) {
            //Do nothing will return false if there is an exception
        }
        return didSucceed;
    }

    public boolean updateEvent(Event event) {
        boolean didSucceed = false;
        try {
            Long rowId = Long.valueOf(event.getEventID());
            ContentValues updateValues = new ContentValues();

            updateValues.put("name", event.getName());
            updateValues.put("description", event.getDescription());
            updateValues.put("date", event.getDate());
            updateValues.put("time", event.getTime());
            updateValues.put("channel", event.getChannel());
            updateValues.put("channelpic", event.getChannelPic());
            updateValues.put("moderator", event.getModerator());
            updateValues.put("moderatorpic", event.getModeratorPic());
            updateValues.put("site", event.getSite());
            updateValues.put("location", event.getLocation());

            didSucceed = database.update("event", updateValues, "_id=" + rowId, null) > 0;
        }
        catch (Exception e) {
            //Do nothing will return false if there is an exception
        }
        return didSucceed;
    }

    public int getLastEventId(){
        int lastId = -1;
        try {
            String query = "Select MAX(_id) from event";
            Cursor cursor = database.rawQuery(query, null);

            cursor.moveToFirst();
            lastId = cursor.getInt(0);
            cursor.close();
        }
        catch (Exception e){
            lastId = -1;
        }
        return lastId;
    }
    public ArrayList<Event> getEvents() {
        ArrayList<Event> events = new ArrayList<Event>();
        try {
            String query = "SELECT * FROM event";
            Cursor cursor = database.rawQuery(query, null);

            Event newEvent;
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                newEvent = new Event();
                newEvent.setEventID(cursor.getInt(0));
                newEvent.setName(cursor.getString(1));
                newEvent.setDescription(cursor.getString(2));
                newEvent.setDate(cursor.getString(3));
                newEvent.setTime(cursor.getString(4));
                newEvent.setChannel(cursor.getString(5));
                newEvent.setChannelPic(cursor.getString(6));
                newEvent.setModerator(cursor.getString(7));
                newEvent.setModeratorPic(cursor.getString(8));
                newEvent.setSite(cursor.getString(9));
                newEvent.setLocation(cursor.getString(10));

                events.add(newEvent);
                cursor.moveToNext();
            }
            cursor.close();
        }
        catch (Exception e) {
            events = new ArrayList<Event>();
        }
        return events;
    }
    public ArrayList<Event> getOtherEvents(String eventName){
        ArrayList<Event> events = new ArrayList<Event>();
        try {
            String query = "SELECT * FROM event WHERE name != '" + eventName + "'";
            Cursor cursor = database.rawQuery(query, null);

            Event newEvent;
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                newEvent = new Event();
                newEvent.setEventID(cursor.getInt(0));
                newEvent.setName(cursor.getString(1));
                newEvent.setDescription(cursor.getString(2));
                newEvent.setDate(cursor.getString(3));
                newEvent.setTime(cursor.getString(4));
                newEvent.setChannel(cursor.getString(5));
                newEvent.setChannelPic(cursor.getString(6));
                newEvent.setModerator(cursor.getString(7));
                newEvent.setModeratorPic(cursor.getString(8));
                newEvent.setSite(cursor.getString(9));
                newEvent.setLocation(cursor.getString(10));

                events.add(newEvent);
                cursor.moveToNext();
            }
            cursor.close();
        }
        catch (Exception e) {
            events = new ArrayList<Event>();
        }
        return events;
    }


    public Event getSpecificEvent(int eventId) {
        Event event = new Event();
        String query = "SELECT * FROM event WHERE _id=" + eventId;
        Cursor cursor = database.rawQuery(query, null);
        if(cursor.moveToFirst()){

            event.setEventID(cursor.getInt(0));
            event.setName(cursor.getString(1));
            event.setDescription(cursor.getString(2));
            event.setDate(cursor.getString(3));
            event.setTime(cursor.getString(4));
            event.setChannel(cursor.getString(5));
            event.setChannelPic(cursor.getString(6));
            event.setModerator(cursor.getString(7));
            event.setModeratorPic(cursor.getString(8));
            event.setSite(cursor.getString(9));
            event.setLocation(cursor.getString(10));

            cursor.close();
        }
        return event;
    }
}