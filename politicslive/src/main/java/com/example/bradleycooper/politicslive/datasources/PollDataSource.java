package com.example.bradleycooper.politicslive.datasources;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.example.bradleycooper.politicslive.dataobjects.Poll;
import com.example.bradleycooper.politicslive.dbhelpers.PollDBHelper;

import java.util.ArrayList;

/**
 * Created by Bradley Cooper on 4/4/2016.
 */
public class PollDataSource {
    private SQLiteDatabase database;
    private PollDBHelper dbHelper;

    public PollDataSource(Context context) {
        dbHelper = new PollDBHelper(context);
    }

    public void open() throws SQLiteException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public boolean insertPoll(Poll p) {
        boolean didSucceed = false;
        try {
            ContentValues initialValues = new ContentValues();

            initialValues.put("name", p.getName());
            initialValues.put("description", p.getDescription());
            initialValues.put("location", p.getLocation());
            initialValues.put("result", p.getResult());
            didSucceed = database.insert("poll", null, initialValues) > 0;
        }
        catch (Exception e) {
            //Do nothing will return false if there is an exception
        }
        return didSucceed;
    }

    public boolean updatePoll(Poll p) {
        boolean didSucceed = false;
        try {
            Long rowId = Long.valueOf(p.getPollID());
            ContentValues updateValues = new ContentValues();

            updateValues.put("name", p.getName());
            updateValues.put("description", p.getDescription());
            updateValues.put("location", p.getLocation());
            updateValues.put("result", p.getResult());

            didSucceed = database.update("poll", updateValues, "_id=" + rowId, null) > 0;
        }
        catch (Exception e) {
            //Do nothing will return false if there is an exception
        }
        return didSucceed;
    }

    public int getLastPollId(){
        int lastId = -1;
        try {
            String query = "Select MAX(_id) from poll";
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
    public ArrayList<String> getPollNames() {
        ArrayList<String> pollNames = new ArrayList<String>();
        try{
            String query = "Select name from poll";
            Cursor cursor = database.rawQuery(query, null);

            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                pollNames.add(cursor.getString(0));
                cursor.moveToNext();
            }
            cursor.close();
        }
        catch(Exception e){
            pollNames = new ArrayList<String>();
        }
        return pollNames;
    }
    public ArrayList<Poll> getPolls() {
        ArrayList<Poll> polls = new ArrayList<Poll>();
        try {
            String query = "SELECT * FROM poll";
            Cursor cursor = database.rawQuery(query, null);

            Poll newPoll;
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                newPoll = new Poll();
                newPoll.setPollID(cursor.getInt(0));
                newPoll.setName(cursor.getString(1));
                newPoll.setDescription(cursor.getString(2));
                newPoll.setLocation(cursor.getString(3));
                newPoll.setResult(cursor.getString(4));
                polls.add(newPoll);
                cursor.moveToNext();
            }
            cursor.close();
        }
        catch (Exception e) {
            polls = new ArrayList<Poll>();
        }
        return polls;
    }

    public Poll getSpecificPoll(int pollId) {
        Poll poll = new Poll();
        String query = "SELECT * FROM poll WHERE _id=" + pollId;
        Cursor cursor = database.rawQuery(query, null);
        if(cursor.moveToFirst()){
            poll.setPollID(cursor.getInt(0));
            poll.setName(cursor.getString(1));
            poll.setDescription(cursor.getString(2));
            poll.setLocation(cursor.getString(3));
            poll.setResult(cursor.getString(4));
            cursor.close();
        }
        return poll;
    }
    public boolean deleteAllPolls(){
        boolean didDelete = false;
        try{
            didDelete = database.delete("poll",null,null) > 0;
        }
        catch (Exception e){

        }
        return didDelete;
    }
    public boolean deletePoll(int pollId){
        boolean didDelete = false;
        try{
            didDelete = database.delete("poll", "pollid =" + pollId, null) > 0;
        }
        catch (Exception e){
        }
        return didDelete;
    }
}
