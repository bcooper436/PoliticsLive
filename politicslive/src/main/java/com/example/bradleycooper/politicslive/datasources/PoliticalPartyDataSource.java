package com.example.bradleycooper.politicslive.datasources;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.example.bradleycooper.politicslive.dataobjects.PoliticalParty;
import com.example.bradleycooper.politicslive.dbhelpers.PoliticalPartyDBHelper;

import java.util.ArrayList;

/**
 * Created by Bradley Cooper on 9/12/2016.
 */
public class PoliticalPartyDataSource {
    private SQLiteDatabase database;
    private PoliticalPartyDBHelper dbHelper;

    public PoliticalPartyDataSource(Context context) {
        dbHelper = new PoliticalPartyDBHelper(context);
}

    public void open() throws SQLiteException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public boolean insertPoliticalParty(PoliticalParty p) {
        boolean didSucceed = false;
        try {
            ContentValues initialValues = new ContentValues();

            initialValues.put("name", p.getName());
            initialValues.put("description", p.getDescription());
            initialValues.put("squarepicture", p.getSquarePicture());
            initialValues.put("widepicture", p.getWidePicture());
            initialValues.put("numberofhouseseats", p.getNumberOfHouseSeats());
            initialValues.put("numberofsenateseats", p.getNumberOfSenateSeats());
            initialValues.put("hufffavorablerating", p.getHuffFavorableRating());
            initialValues.put("huffunfavorablerating", p.getHuffUnfavorableRating());
            initialValues.put("site", p.getSite());
            initialValues.put("email", p.getEmail());
            initialValues.put("twitter", p.getTwitter());
            initialValues.put("percentageofhousevote", p.getPercentageOfHouseVote());


            didSucceed = database.insert("politicalparty", null, initialValues) > 0;
        }
        catch (Exception e) {
            //Do nothing will return false if there is an exception
        }
        return didSucceed;
    }

    public boolean updatePoliticalParty(PoliticalParty p) {
        boolean didSucceed = false;
        try {
            Long rowId = Long.valueOf(p.getPoliticalPartyID());
            ContentValues updateValues = new ContentValues();

            updateValues.put("name", p.getName());
            updateValues.put("description", p.getDescription());
            updateValues.put("squarepicture", p.getSquarePicture());
            updateValues.put("widepicture", p.getWidePicture());
            updateValues.put("numberofhouseseats", p.getNumberOfHouseSeats());
            updateValues.put("numberofsenateseats", p.getNumberOfSenateSeats());
            updateValues.put("hufffavorablerating", p.getHuffFavorableRating());
            updateValues.put("huffunfavorablerating", p.getHuffUnfavorableRating());
            updateValues.put("site", p.getSite());
            updateValues.put("email", p.getEmail());
            updateValues.put("twitter", p.getTwitter());
            updateValues.put("percentageofhousevote", p.getPercentageOfHouseVote());

            didSucceed = database.update("politicalparty", updateValues, "_id=" + rowId, null) > 0;
        }
        catch (Exception e) {
            //Do nothing will return false if there is an exception
        }
        return didSucceed;
    }

    public int getLastPoliticalPartyID(){
        int lastId = -1;
        try {
            String query = "Select MAX(_id) from politicalparty";
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
    public ArrayList<String> getPoliticalPartyName() {
        ArrayList<String> politicalPartyNames = new ArrayList<String>();
        try{
            String query = "Select name from politicalparty";
            Cursor cursor = database.rawQuery(query, null);

            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                politicalPartyNames.add(cursor.getString(0));
                cursor.moveToNext();
            }
            cursor.close();
        }
        catch(Exception e){
            politicalPartyNames = new ArrayList<String>();
        }
        return politicalPartyNames;
    }
    public ArrayList<PoliticalParty> getPoliticalParties() {
        ArrayList<PoliticalParty> politicalParties = new ArrayList<PoliticalParty>();
        try {
            String query = "SELECT * FROM politicalparty";
            Cursor cursor = database.rawQuery(query, null);

            PoliticalParty newPoliticalParty;
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                newPoliticalParty = new PoliticalParty();
                newPoliticalParty.setPoliticalPartyID(cursor.getInt(0));
                newPoliticalParty.setName(cursor.getString(1));
                newPoliticalParty.setDescription(cursor.getString(2));
                newPoliticalParty.setSquarePicture(cursor.getString(3));
                newPoliticalParty.setWidePicture(cursor.getString(4));
                newPoliticalParty.setNumberOfHouseSeats(cursor.getInt(5));
                newPoliticalParty.setNumberOfSenateSeats(cursor.getInt(6));
                newPoliticalParty.setHuffFavorableRating(cursor.getFloat(7));
                newPoliticalParty.setHuffUnfavorableRating(cursor.getFloat(8));
                newPoliticalParty.setSite(cursor.getString(9));
                newPoliticalParty.setEmail(cursor.getString(10));
                newPoliticalParty.setTwitter(cursor.getString(11));
                newPoliticalParty.setPercentageOfHouseVote(cursor.getFloat(12));

                politicalParties.add(newPoliticalParty);
                cursor.moveToNext();
            }
            cursor.close();
        }
        catch (Exception e) {
            politicalParties = new ArrayList<PoliticalParty>();
        }
        return politicalParties;
    }
    public ArrayList<PoliticalParty> getOtherPoliticalParties(String name){
        ArrayList<PoliticalParty> politicalParties = new ArrayList<PoliticalParty>();
        try {
            String query = "SELECT * FROM politicalparty WHERE name != '" + name + "'";
            Cursor cursor = database.rawQuery(query, null);

            PoliticalParty newPoliticalParty;
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                newPoliticalParty = new PoliticalParty();
                newPoliticalParty.setPoliticalPartyID(cursor.getInt(0));
                newPoliticalParty.setName(cursor.getString(1));
                newPoliticalParty.setDescription(cursor.getString(2));
                newPoliticalParty.setSquarePicture(cursor.getString(3));
                newPoliticalParty.setWidePicture(cursor.getString(4));
                newPoliticalParty.setNumberOfHouseSeats(cursor.getInt(5));
                newPoliticalParty.setNumberOfSenateSeats(cursor.getInt(6));
                newPoliticalParty.setHuffFavorableRating(cursor.getFloat(7));
                newPoliticalParty.setHuffUnfavorableRating(cursor.getFloat(8));
                newPoliticalParty.setSite(cursor.getString(9));
                newPoliticalParty.setEmail(cursor.getString(10));
                newPoliticalParty.setTwitter(cursor.getString(11));
                newPoliticalParty.setPercentageOfHouseVote(cursor.getFloat(12));

                politicalParties.add(newPoliticalParty);
                cursor.moveToNext();
            }
            cursor.close();
        }
        catch (Exception e) {
            politicalParties = new ArrayList<PoliticalParty>();
        }
        return politicalParties;
    }
    public PoliticalParty getSpecificPoliticalParty(int politicalPartyId) {
        PoliticalParty politicalParty = new PoliticalParty();
        String query = "SELECT * FROM politicalparty WHERE _id=" + politicalPartyId;
        Cursor cursor = database.rawQuery(query, null);
        if(cursor.moveToFirst()){
            politicalParty.setPoliticalPartyID(cursor.getInt(0));
            politicalParty.setName(cursor.getString(1));
            politicalParty.setDescription(cursor.getString(2));
            politicalParty.setSquarePicture(cursor.getString(3));
            politicalParty.setWidePicture(cursor.getString(4));
            politicalParty.setNumberOfHouseSeats(cursor.getInt(5));
            politicalParty.setNumberOfSenateSeats(cursor.getInt(6));
            politicalParty.setHuffFavorableRating(cursor.getFloat(7));
            politicalParty.setHuffUnfavorableRating(cursor.getFloat(8));
            politicalParty.setSite(cursor.getString(9));
            politicalParty.setEmail(cursor.getString(10));
            politicalParty.setTwitter(cursor.getString(11));
            politicalParty.setPercentageOfHouseVote(cursor.getFloat(12));

            cursor.close();
        }
        return politicalParty;
    }
    public PoliticalParty getOPoliticalPartyByName(String name) {
        PoliticalParty politicalParty = new PoliticalParty();
        String query = "SELECT * FROM politicalparty WHERE name='" + name +"'";
        Cursor cursor = database.rawQuery(query, null);
        if(cursor.moveToFirst()){
            politicalParty.setPoliticalPartyID(cursor.getInt(0));
            politicalParty.setName(cursor.getString(1));
            politicalParty.setDescription(cursor.getString(2));
            politicalParty.setSquarePicture(cursor.getString(3));
            politicalParty.setWidePicture(cursor.getString(4));
            politicalParty.setNumberOfHouseSeats(cursor.getInt(5));
            politicalParty.setNumberOfSenateSeats(cursor.getInt(6));
            politicalParty.setHuffFavorableRating(cursor.getFloat(7));
            politicalParty.setHuffUnfavorableRating(cursor.getFloat(8));
            politicalParty.setSite(cursor.getString(9));
            politicalParty.setEmail(cursor.getString(10));
            politicalParty.setTwitter(cursor.getString(11));
            politicalParty.setPercentageOfHouseVote(cursor.getFloat(12));
            cursor.close();
        }
        return politicalParty;
    }
}