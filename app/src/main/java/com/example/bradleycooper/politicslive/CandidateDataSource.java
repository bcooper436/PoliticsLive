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
 * Created by Bradley Cooper on 4/4/2016.
 */
public class CandidateDataSource {
    private SQLiteDatabase database;
    private CandidateDBHelper dbHelper;

    public CandidateDataSource(Context context) {
        dbHelper = new CandidateDBHelper(context);
    }

    public void open() throws SQLiteException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public boolean insertCandidate(Candidate c) {
        boolean didSucceed = false;
        try {
            ContentValues initialValues = new ContentValues();

            initialValues.put("candidatename", c.getCandidateName());
            initialValues.put("candidatedescription", c.getCandidateDescription());
            initialValues.put("numberofvotes", c.getNumberOfVotes());
            initialValues.put("squarepicture", c.getSquarePicture());
            initialValues.put("widepicture", c.getWidePicture());
            initialValues.put("party", c.getParty());
            didSucceed = database.insert("candidate", null, initialValues) > 0;
        }
        catch (Exception e) {
            //Do nothing will return false if there is an exception
        }
        return didSucceed;
    }

    public boolean updateCandidate(Candidate c) {
        boolean didSucceed = false;
        try {
            Long rowId = Long.valueOf(c.getCandidateID());
            ContentValues updateValues = new ContentValues();

            updateValues.put("candidatename", c.getCandidateName());
            updateValues.put("candidatedescription", c.getCandidateDescription());
            updateValues.put("numberofvotes", c.getNumberOfVotes());
            updateValues.put("squarepicture", c.getSquarePicture());
            updateValues.put("widepicture", c.getWidePicture());
            updateValues.put("party", c.getParty());

            didSucceed = database.update("candidate", updateValues, "_id=" + rowId, null) > 0;
        }
        catch (Exception e) {
            //Do nothing will return false if there is an exception
        }
        return didSucceed;
    }

    public int getLastCandidateId(){
        int lastId = -1;
        try {
            String query = "Select MAX(_id) from candidate";
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
    public ArrayList<String> getCandidateName() {
        ArrayList<String> candidateNames = new ArrayList<String>();
        try{
            String query = "Select candidatename from contact";
            Cursor cursor = database.rawQuery(query, null);

            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                candidateNames.add(cursor.getString(0));
                cursor.moveToNext();
            }
            cursor.close();
        }
        catch(Exception e){
            candidateNames = new ArrayList<String>();
        }
        return candidateNames;
    }
    public ArrayList<Candidate> getCandidates() {
        ArrayList<Candidate> candidates = new ArrayList<Candidate>();
        try {
            String query = "SELECT * FROM candidate";
            Cursor cursor = database.rawQuery(query, null);

            Candidate newCandidate;
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                newCandidate = new Candidate();
                newCandidate.setCandidateID(cursor.getInt(0));
                newCandidate.setCandidateName(cursor.getString(1));
                newCandidate.setCandidateDescription(cursor.getString(2));
                newCandidate.setNumberOfVotes(cursor.getInt(3));
                newCandidate.setSquarePicture(cursor.getBlob(4));
                newCandidate.setWidePicture(cursor.getBlob(5));
                newCandidate.setParty(cursor.getString(6));
                candidates.add(newCandidate);
                cursor.moveToNext();
            }
            cursor.close();
        }
        catch (Exception e) {
            candidates = new ArrayList<Candidate>();
        }
        return candidates;
    }
    public ArrayList<Candidate> getOtherCandidates(String candidateName){
        ArrayList<Candidate> candidates = new ArrayList<Candidate>();
        try {
            String query = "SELECT * FROM candidate WHERE candidatename != '" + candidateName + "'";
            Cursor cursor = database.rawQuery(query, null);

            Candidate newCandidate;
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                newCandidate = new Candidate();
                newCandidate.setCandidateID(cursor.getInt(0));
                newCandidate.setCandidateName(cursor.getString(1));
                newCandidate.setCandidateDescription(cursor.getString(2));
                newCandidate.setNumberOfVotes(cursor.getInt(3));
                newCandidate.setSquarePicture(cursor.getBlob(4));
                newCandidate.setWidePicture(cursor.getBlob(5));
                newCandidate.setParty(cursor.getString(6));
                candidates.add(newCandidate);
                cursor.moveToNext();
            }
            cursor.close();
        }
        catch (Exception e) {
            candidates = new ArrayList<Candidate>();
        }
        return candidates;
    }
    public ArrayList<Candidate> getSpecificParty(String partyString){
        ArrayList<Candidate> candidates = new ArrayList<Candidate>();
        try {
            String query = "SELECT * FROM candidate WHERE party = '" + partyString + "'";
            Cursor cursor = database.rawQuery(query, null);

            Candidate newCandidate;
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                newCandidate = new Candidate();
                newCandidate.setCandidateID(cursor.getInt(0));
                newCandidate.setCandidateName(cursor.getString(1));
                newCandidate.setCandidateDescription(cursor.getString(2));
                newCandidate.setNumberOfVotes(cursor.getInt(3));
                newCandidate.setSquarePicture(cursor.getBlob(4));
                newCandidate.setWidePicture(cursor.getBlob(5));
                newCandidate.setParty(cursor.getString(6));
                candidates.add(newCandidate);
                cursor.moveToNext();
            }
            cursor.close();
        }
        catch (Exception e) {
            candidates = new ArrayList<Candidate>();
        }
        return candidates;
    }

    public Candidate getSpecificCandidate(int candidateId) {
        Candidate candidate = new Candidate();
        String query = "SELECT * FROM candidate WHERE _id=" + candidateId;
        Cursor cursor = database.rawQuery(query, null);
        if(cursor.moveToFirst()){
            candidate.setCandidateID(cursor.getInt(0));
            candidate.setCandidateName(cursor.getString(1));
            candidate.setCandidateDescription(cursor.getString(2));
            candidate.setNumberOfVotes(cursor.getInt(3));
            candidate.setSquarePicture(cursor.getBlob(4));
            candidate.setWidePicture(cursor.getBlob(5));
            candidate.setParty(cursor.getString(6));
            cursor.close();
        }
        return candidate;
    }
    public Candidate getCandidateByName(String candidateName) {
        Candidate candidate = new Candidate();
        String query = "SELECT * FROM candidate WHERE candidatename='" + candidateName +"'";
        Cursor cursor = database.rawQuery(query, null);
        if(cursor.moveToFirst()){
            candidate.setCandidateID(cursor.getInt(0));
            candidate.setCandidateName(cursor.getString(1));
            candidate.setCandidateDescription(cursor.getString(2));
            candidate.setNumberOfVotes(cursor.getInt(3));
            candidate.setSquarePicture(cursor.getBlob(4));
            candidate.setWidePicture(cursor.getBlob(5));
            candidate.setParty(cursor.getString(6));
            cursor.close();
        }
        return candidate;
    }
    public boolean deleteCandidate(int candidateID){
        boolean didDelete = false;
        try {
            didDelete = database.delete("candidate","_id=" + candidateID, null) > 0;
        }
        catch (Exception e){

        }
        return didDelete;
    }
    public int getNumberOfVotesByParty(String partyString){
        int totalVotes = 0;
        for(Candidate c : getSpecificParty(partyString)){
            totalVotes += c.getNumberOfVotes();
        }
        return totalVotes;
    }
    public Map<String, Integer> getRepublicanVotes() {
        Map<String, Integer> counts = new HashMap<>();
        int total = 0;
        for(Candidate c : getSpecificParty("GOP")) {
            counts.put(c.getCandidateName(), c.getNumberOfVotes());
            total += c.getNumberOfVotes();
        }
        counts.put("TOTAL", total);
        return counts;
    }

    public Map<String, Integer> getDemocratVotes() {
        Map<String, Integer> counts = new HashMap<>();
        int total = 0;
        for(Candidate c : getSpecificParty("DNC")) {
            counts.put(c.getCandidateName(), c.getNumberOfVotes());
            total += c.getNumberOfVotes();
        }
        counts.put("TOTAL", total);
        return counts;
    }

    public String getPercentageOfVote(String candidateName){
        Candidate currentCandidate;
        String percentageOfVotes;
        StringBuilder sb;
        int totalVotesFromParty, votesPercentage;

        currentCandidate = getCandidateByName(candidateName);
        totalVotesFromParty = getNumberOfVotesByParty(currentCandidate.getParty());
        votesPercentage = (100 * currentCandidate.getNumberOfVotes()) / totalVotesFromParty;

        sb = new StringBuilder();
        sb.append(votesPercentage);
        sb.append("%");
        return sb.toString();
    }
    public ArrayList<Candidate> getCandidatesInOrderOfVotes(String partyString){
        ArrayList<Candidate> candidates = new ArrayList<Candidate>();
        try {
            String query = "SELECT * FROM candidate WHERE party = '" + partyString + "' ORDER BY numberofvotes DESC";
            Cursor cursor = database.rawQuery(query, null);

            Candidate newCandidate;
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                newCandidate = new Candidate();
                newCandidate.setCandidateID(cursor.getInt(0));
                newCandidate.setCandidateName(cursor.getString(1));
                newCandidate.setCandidateDescription(cursor.getString(2));
                newCandidate.setNumberOfVotes(cursor.getInt(3));
                newCandidate.setSquarePicture(cursor.getBlob(4));
                newCandidate.setWidePicture(cursor.getBlob(5));
                newCandidate.setParty(cursor.getString(6));
                candidates.add(newCandidate);
                cursor.moveToNext();
            }
            cursor.close();
        }
        catch (Exception e) {
            candidates = new ArrayList<Candidate>();
        }
        return candidates;
    }
    public int getCandidateRanking(String candidateName){
        int currentRanking = 0;
        String candidateParty = getCandidateByName(candidateName).getParty();
        ArrayList<Candidate> arrayList = getCandidatesInOrderOfVotes(candidateParty);
        int i = 1;
        for(Candidate c : arrayList){
            if(c.getCandidateName().equalsIgnoreCase(candidateName)){
                currentRanking = i;
            }
            i++;
        }
        return currentRanking;
    }
    public void clearAllVotes(){
        ContentValues cv = new ContentValues();
        cv.put("numberofvotes", 0);
        String where = "UPDATE candidate SET numberofvotes = 0";
        database.execSQL(where);
    }
    public boolean deleteAllCandidates(){
        boolean didDelete = false;
        try{
            didDelete = database.delete("candidate",null,null) > 0;
        }
        catch (Exception e){

        }
        return didDelete;
    }
}
