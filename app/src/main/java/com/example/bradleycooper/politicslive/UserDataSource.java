package com.example.bradleycooper.politicslive;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.util.ArrayList;

/**
 * Created by Bradley Cooper on 4/4/2016.
 */
public class UserDataSource {
    private SQLiteDatabase database;
    private UserDBHelper dbHelper;

    public UserDataSource(Context context) {
        dbHelper = new UserDBHelper(context);
    }

    public void open() throws SQLiteException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public boolean insertUser(User u) {
        boolean didSucceed = false;
        try {
            ContentValues initialValues = new ContentValues();

            initialValues.put("displayname", u.getDisplayName());
            initialValues.put("username", u.getUserName());
            initialValues.put("password", u.getPassword());
            initialValues.put("partyaffiliation", u.getPartyAffiliation());
            initialValues.put("age", u.getAge());
            initialValues.put("gender", u.getGender());
            //initialValues.put("chosendemocrat", u.getChosenDemocrat());
            //initialValues.put("chosenrepublican", u.getChosenRepublican());
            //initialValues.put("profilepicture", u.getProfilePicture());
            didSucceed = database.insert("user", null, initialValues) > 0;
        }
        catch (Exception e) {
            //Do nothing will return false if there is an exception
        }
        return didSucceed;
    }

    public boolean updateUser(User u) {
        boolean didSucceed = false;
        try {
            Long rowId = Long.valueOf(u.getUserID());
            ContentValues updateValues = new ContentValues();

            updateValues.put("displayname", u.getDisplayName());
            updateValues.put("username", u.getUserName());
            updateValues.put("password", u.getPassword());
            updateValues.put("partyaffiliation", u.getPartyAffiliation());
            updateValues.put("age", u.getAge());
            updateValues.put("gender", u.getGender());
            updateValues.put("chosendemocrat", u.getChosenDemocrat());
            updateValues.put("chosenrepublican", u.getChosenRepublican());
            updateValues.put("profilepicture", u.getProfilePicture());

            didSucceed = database.update("user", updateValues, "_id=" + rowId, null) > 0;
        }
        catch (Exception e) {
            //Do nothing will return false if there is an exception
        }
        return didSucceed;
    }

    public int getLastUserId(){
        int lastId = -1;
        try {
            String query = "Select MAX(_id) from user";
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
    public ArrayList<String> getUserName() {
        ArrayList<String> userNames = new ArrayList<String>();
        try{
            String query = "Select username from user";
            Cursor cursor = database.rawQuery(query, null);

            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                userNames.add(cursor.getString(0));
                cursor.moveToNext();
            }
            cursor.close();
        }
        catch(Exception e){
            userNames = new ArrayList<String>();
        }
        return userNames;
    }
    public ArrayList<User> getUsers() {
        ArrayList<User> users = new ArrayList<User>();
        try {
            String query = "SELECT * FROM user";
            Cursor cursor = database.rawQuery(query, null);

            User newUser;
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                newUser = new User();
                newUser.setUserID(cursor.getInt(0));
                newUser.setDisplayName(cursor.getString(1));
                newUser.setUserName(cursor.getString(2));
                newUser.setPassword(cursor.getString(3));
                newUser.setPartyAffiliation(cursor.getString(4));
                newUser.setAge(cursor.getInt(5));
                newUser.setGender(cursor.getString(6));
                newUser.setChosenDemocrat(cursor.getString(7));
                newUser.setChosenRepublican(cursor.getString(8));
                newUser.setProfilePicture(cursor.getBlob(9));
                users.add(newUser);
                cursor.moveToNext();
            }
            cursor.close();
        }
        catch (Exception e) {
            users = new ArrayList<User>();
        }
        return users;
    }
    public ArrayList<User> getUsersByParty(String party) {
        ArrayList<User> users = new ArrayList<User>();
        try {
            String query = "SELECT * FROM user WHERE partyaffiliation = '" + party + "'";
            Cursor cursor = database.rawQuery(query, null);

            User newUser;
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                newUser = new User();
                newUser.setUserID(cursor.getInt(0));
                newUser.setDisplayName(cursor.getString(1));
                newUser.setUserName(cursor.getString(2));
                newUser.setPassword(cursor.getString(3));
                newUser.setPartyAffiliation(cursor.getString(4));
                newUser.setAge(cursor.getInt(5));
                newUser.setGender(cursor.getString(6));
                newUser.setChosenDemocrat(cursor.getString(7));
                newUser.setChosenRepublican(cursor.getString(8));
                newUser.setProfilePicture(cursor.getBlob(9));
                users.add(newUser);
                cursor.moveToNext();
            }
            cursor.close();
        }
        catch (Exception e) {
            users = new ArrayList<User>();
        }
        return users;
    }

    public User getSpecificUser(int userId) {
        User user = new User();
        String query = "SELECT * FROM user WHERE _id=" + userId;
        Cursor cursor = database.rawQuery(query, null);
        if(cursor.moveToFirst()){
            user.setUserID(cursor.getInt(0));
            user.setDisplayName(cursor.getString(1));
            user.setUserName(cursor.getString(2));
            user.setPassword(cursor.getString(3));
            user.setPartyAffiliation(cursor.getString(4));
            user.setAge(cursor.getInt(5));
            user.setGender(cursor.getString(6));
            user.setChosenDemocrat(cursor.getString(7));
            user.setChosenRepublican(cursor.getString(8));
            user.setProfilePicture(cursor.getBlob(9));
            cursor.close();
        }
        return user;
    }
    public User getSpecificUserFromLoginInfo(String username, String password) {
        User user = new User();
        String query = "SELECT * FROM user WHERE username = '" + username + "'";
        Cursor cursor = database.rawQuery(query, null);
        if(cursor.moveToFirst()){
            user.setUserID(cursor.getInt(0));
            user.setDisplayName(cursor.getString(1));
            user.setUserName(cursor.getString(2));
            user.setPassword(cursor.getString(3));
            user.setPartyAffiliation(cursor.getString(4));
            user.setAge(cursor.getInt(5));
            user.setGender(cursor.getString(6));
            user.setChosenDemocrat(cursor.getString(7));
            user.setChosenRepublican(cursor.getString(8));
            user.setProfilePicture(cursor.getBlob(9));
            cursor.close();
        }
        return user;
    }
    public int getAverageVoterAge(String party){
        int averageAgeOfVoter = 0, totalVoters = 0;
        for(User u : getUsersByParty(party)) {
            averageAgeOfVoter += u.getAge();
            totalVoters++;
        }
        return averageAgeOfVoter/totalVoters;
    }
    public int getPartyMembers(String party){
        int partyMembers = 0;
        for(User u : getUsersByParty(party)){
            partyMembers++;
        }
        return partyMembers;
    }
    public int getGenderPercentage(String party, String gender){
        int males = 0, females = 0, totalVoters = 0;

        for(User u : getUsersByParty(party)){
            switch (u.getGender()){
                case "Male":
                    males++;
                    totalVoters++;
                    break;
                case "Female":
                    females++;
                    totalVoters++;
                    break;
                default:
                    totalVoters++;
                    break;
            }
        }

        if(gender.equalsIgnoreCase("Male")) {
            return (100 * males)/totalVoters;
        }
        else {
            return (100 * females)/totalVoters;
        }
    }

    public boolean deleteAllUsers(){
        boolean didDelete = false;
        try{
            didDelete = database.delete("user",null,null) > 0;
        }
        catch (Exception e){

        }
        return didDelete;
    }
}
