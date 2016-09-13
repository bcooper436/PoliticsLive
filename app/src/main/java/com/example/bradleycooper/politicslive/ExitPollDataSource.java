package com.example.bradleycooper.politicslive;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Bradley Cooper on 4/4/2016.
 */
public class ExitPollDataSource {
    private SQLiteDatabase database;
    private ExitPollDBHelper dbHelper;

    public ExitPollDataSource(Context context) {
        dbHelper = new ExitPollDBHelper(context);
    }

    public void open() throws SQLiteException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public boolean insertExitPoll(ExitPoll p) {
        boolean didSucceed = false;
        try {
            ContentValues initialValues = new ContentValues();

            initialValues.put("state", p.getState());
            initialValues.put("totalresponders", p.getTotalResponders());
            initialValues.put("candidate", p.getCandidate());
            initialValues.put("votergroup", p.getVoterGroup());
            initialValues.put("percentageofvote", p.getPercentageOfVote());
            initialValues.put("votergrouplabel", p.getVoterGroupLabel());
            didSucceed = database.insert("exitpoll", null, initialValues) > 0;
        }
        catch (Exception e) {
            //Do nothing will return false if there is an exception
        }
        return didSucceed;
    }

    public boolean updateExitPoll(ExitPoll p) {
        boolean didSucceed = false;
        try {
            Long rowId = Long.valueOf(p.getExitPollID());
            ContentValues updateValues = new ContentValues();

            updateValues.put("state", p.getState());
            updateValues.put("totalresponders", p.getTotalResponders());
            updateValues.put("candidate", p.getCandidate());
            updateValues.put("votergroup", p.getVoterGroup());
            updateValues.put("percentageofvote", p.getPercentageOfVote());
            updateValues.put("votergrouplabel", p.getVoterGroupLabel());

            didSucceed = database.update("exitpoll", updateValues, "_id=" + rowId, null) > 0;
        }
        catch (Exception e) {
            //Do nothing will return false if there is an exception
        }
        return didSucceed;
    }

    public int getLastExitPollId(){
        int lastId = -1;
        try {
            String query = "Select MAX(_id) from exitpoll";
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
    public ArrayList<ExitPoll> getExitPolls() {
        ArrayList<ExitPoll> exitPolls = new ArrayList<ExitPoll>();
        try {
            String query = "SELECT * FROM exitpoll";
            Cursor cursor = database.rawQuery(query, null);

            ExitPoll newExitPoll;
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                newExitPoll = new ExitPoll();
                newExitPoll.setExitPollID(cursor.getInt(0));
                newExitPoll.setState(cursor.getString(1));
                newExitPoll.setTotalResponders(cursor.getInt(2));
                newExitPoll.setCandidate(cursor.getString(3));
                newExitPoll.setVoterGroup(cursor.getString(4));
                newExitPoll.setPercentageOfVote(cursor.getInt(5));
                newExitPoll.setVoterGroupLabel(cursor.getString(6));
                exitPolls.add(newExitPoll);
                cursor.moveToNext();
            }
            cursor.close();
        }
        catch (Exception e) {
            exitPolls = new ArrayList<ExitPoll>();
        }
        return exitPolls;
    }
    public int getNumberOfExitPolls() {
        int numberToReturn = 0;
        for(ExitPoll exitPoll: getExitPolls()){
            numberToReturn++;
        }
        return numberToReturn;
    }
    public ArrayList<ExitPoll> getExitPollsByState(String state) {
        ArrayList<ExitPoll> exitPolls = new ArrayList<ExitPoll>();
        try {
            String query = "SELECT * FROM exitpoll WHERE state='" + state + "'";
            Cursor cursor = database.rawQuery(query, null);

            ExitPoll newExitPoll;
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                newExitPoll = new ExitPoll();
                newExitPoll.setExitPollID(cursor.getInt(0));
                newExitPoll.setState(cursor.getString(1));
                newExitPoll.setTotalResponders(cursor.getInt(2));
                newExitPoll.setCandidate(cursor.getString(3));
                newExitPoll.setVoterGroup(cursor.getString(4));
                newExitPoll.setPercentageOfVote(cursor.getInt(5));
                newExitPoll.setVoterGroupLabel(cursor.getString(6));
                exitPolls.add(newExitPoll);
                cursor.moveToNext();
            }
            cursor.close();
        }
        catch (Exception e) {
            exitPolls = new ArrayList<ExitPoll>();
        }
        return exitPolls;
    }
    public int getNumberOfExitPollsByState(String state){
        int numberToReturn = 0;
        for(ExitPoll exitPoll: getExitPollsByState(state)){
            numberToReturn++;
        }
        return numberToReturn;
    }
    public ArrayList<ExitPoll> getExitPollsByCandidate(String candidate) {
        ArrayList<ExitPoll> exitPolls = new ArrayList<ExitPoll>();
        try {
            String query = "SELECT * FROM exitpoll WHERE candidate='" + candidate + "'";
            Cursor cursor = database.rawQuery(query, null);

            ExitPoll newExitPoll;
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                newExitPoll = new ExitPoll();
                newExitPoll.setExitPollID(cursor.getInt(0));
                newExitPoll.setState(cursor.getString(1));
                newExitPoll.setTotalResponders(cursor.getInt(2));
                newExitPoll.setCandidate(cursor.getString(3));
                newExitPoll.setVoterGroup(cursor.getString(4));
                newExitPoll.setPercentageOfVote(cursor.getInt(5));
                newExitPoll.setVoterGroupLabel(cursor.getString(6));
                exitPolls.add(newExitPoll);
                cursor.moveToNext();
            }
            cursor.close();
        }
        catch (Exception e) {
            exitPolls = new ArrayList<ExitPoll>();
        }
        return exitPolls;
    }
    public int getNumberOfExitPollsByCandidate(String candidate){
        int numberToReturn = 0;
        for(ExitPoll exitPoll: getExitPollsByCandidate(candidate)){
            numberToReturn++;
        }
        return numberToReturn;
    }
    public ArrayList<ExitPoll> getExitPolls(String candidate, String votergroup){
        ArrayList<ExitPoll> exitPolls = new ArrayList<ExitPoll>();
        try {
            String query = "SELECT * FROM exitpoll WHERE candidate='" + candidate + "' AND votergroup='" + votergroup + "'";
            Cursor cursor = database.rawQuery(query, null);

            ExitPoll newExitPoll;
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                newExitPoll = new ExitPoll();
                newExitPoll.setExitPollID(cursor.getInt(0));
                newExitPoll.setState(cursor.getString(1));
                newExitPoll.setTotalResponders(cursor.getInt(2));
                newExitPoll.setCandidate(cursor.getString(3));
                newExitPoll.setVoterGroup(cursor.getString(4));
                newExitPoll.setPercentageOfVote(cursor.getInt(5));
                newExitPoll.setVoterGroupLabel(cursor.getString(6));
                exitPolls.add(newExitPoll);
                cursor.moveToNext();
            }
            cursor.close();
        }
        catch (Exception e) {
            exitPolls = new ArrayList<ExitPoll>();
        }
        return exitPolls;
    }
    public String getExitPollsAverage(String candidate, String votergroup){
        int numberOfPolls = 0, totalPercentage = 0, average;
        String averageString;
        try {
            String query = "SELECT * FROM exitpoll WHERE candidate='" + candidate + "'";
            Cursor cursor = database.rawQuery(query, null);

            ExitPoll newExitPoll;
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                if(cursor.getString(4).equalsIgnoreCase(votergroup)) {
                    totalPercentage += cursor.getInt(5);
                    numberOfPolls++;
                }
                cursor.moveToNext();
            }
            cursor.close();
        }
        catch (Exception e) {
        }
        if(numberOfPolls != 0) {
            average = totalPercentage / numberOfPolls;
        }
        else {
            average = 0;
        }
        averageString = String.valueOf(average) + "%";
        return averageString;
    }

    public int getNumberOfExitPolls(String candidate, String votergroup){
        int numberToReturn = 0;
        for(ExitPoll exitPoll: getExitPolls(candidate, votergroup)){
            numberToReturn++;
        }
        return numberToReturn;
    }

    public ExitPoll getSpecificExitPoll(int exitPollId) {
        ExitPoll exitPoll = new ExitPoll();
        String query = "SELECT * FROM exitpoll WHERE _id=" + exitPollId;
        Cursor cursor = database.rawQuery(query, null);
        if(cursor.moveToFirst()){
            exitPoll.setExitPollID(cursor.getInt(0));
            exitPoll.setState(cursor.getString(1));
            exitPoll.setTotalResponders(cursor.getInt(2));
            exitPoll.setCandidate(cursor.getString(3));
            exitPoll.setVoterGroup(cursor.getString(4));
            exitPoll.setPercentageOfVote(cursor.getInt(5));
            exitPoll.setVoterGroupLabel(cursor.getString(6));

            cursor.close();
        }
        return exitPoll;
    }
    public boolean deleteAllExitPolls(){
        boolean didDelete = false;
        try{
            didDelete = database.delete("exitpoll",null,null) > 0;
        }
        catch (Exception e){

        }
        return didDelete;
    }
    public boolean deleteExitPoll(int exitPollId){
        boolean didDelete = false;
        try{
            didDelete = database.delete("exitpoll", "exitpollid =" + exitPollId, null) > 0;
        }
        catch (Exception e){
        }
        return didDelete;
    }
    public int getNumberOfCandidates(){
        String query = "SELECT DISTINCT candidate FROM exitpoll";
        Cursor cursor = database.rawQuery(query, null);
        if (cursor.getCount() > 0 && cursor.getColumnCount() > 0) {
            cursor.close();
            return cursor.getCount();
        } else {
            cursor.close();
            return 0;
        }
    }
    public String getTopVoterGroupByVoterGroupLabelAndStateAndCandidate(String voterGroupLabel, String state, String candidate){
        String topVoterGroup = "";
        int topVoterGroupCount = 0;
        String query = "SELECT * FROM exitpoll WHERE votergrouplabel='" + voterGroupLabel + "' AND state='" + state + "' AND candidate='" + candidate + "'";
        Cursor cursor = database.rawQuery(query, null);

        ExitPoll newExitPoll;
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {

            newExitPoll = new ExitPoll();
            newExitPoll.setExitPollID(cursor.getInt(0));
            newExitPoll.setState(cursor.getString(1));
            newExitPoll.setTotalResponders(cursor.getInt(2));
            newExitPoll.setCandidate(cursor.getString(3));
            newExitPoll.setVoterGroup(cursor.getString(4));
            newExitPoll.setPercentageOfVote(cursor.getInt(5));
            newExitPoll.setVoterGroupLabel(cursor.getString(6));

            if(newExitPoll.getPercentageOfVote()>topVoterGroupCount){
                topVoterGroupCount = newExitPoll.getPercentageOfVote();
                topVoterGroup = newExitPoll.getVoterGroup();
            }
            cursor.moveToNext();
        }

        return topVoterGroup;
    }
    public int getNumberOfTotalRespondersByStateAndCandidate(String state, String candidate){
        int numberOfTotalResponders = 0;

        String query = "SELECT DISTINCT totalresponders FROM exitpoll WHERE state='" + state + "' AND candidate='" + candidate + "'";
        Cursor cursor = database.rawQuery(query, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            numberOfTotalResponders = cursor.getInt(0);
            cursor.moveToNext();
        }
        cursor.close();

        return numberOfTotalResponders;
    }
    public int getPercentageOfVoteByStateAndCandidateAndVoterGroup(String state, String candidate, String voterGroup){
        int percentageOfVote = 0;

        String query = "SELECT * FROM exitpoll WHERE state='" + state + "' AND candidate='" + candidate + "' AND votergroup='" + voterGroup + "'";
        Cursor cursor = database.rawQuery(query, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            percentageOfVote = cursor.getInt(5);
            cursor.moveToNext();
        }
        cursor.close();

        return percentageOfVote;
    }
    public int getPercentageOfVoteByStateAndCandidateAndVoterGroup(String state, String candidate, String voterGroup1, String voterGroup2){
        int percentageOfVote = 0;

        String query = "SELECT * FROM exitpoll WHERE state='" + state + "' AND candidate='" + candidate + "' AND votergroup='" + voterGroup1 + "' AND votergroup='" + voterGroup2 +"'";
        Cursor cursor = database.rawQuery(query, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            percentageOfVote = cursor.getInt(5);
            cursor.moveToNext();
        }
        cursor.close();

        return percentageOfVote;
    }

    public int getNumberOfStates(){
        int numberOfStates = 0;

        String query = "SELECT DISTINCT state FROM exitpoll";
        Cursor cursor = database.rawQuery(query, null);
        if (cursor.getCount() > 0 && cursor.getColumnCount() > 0) {
            cursor.close();
            return cursor.getCount();
        } else {
            cursor.close();
            return 0;
        }
    }
    public String[] getArrayOfAllStates(){
        String[] states = new String[getNumberOfStates()];
        int i = 0;

        String query = "SELECT DISTINCT state FROM exitpoll ORDER BY LOWER(state)";
        Cursor cursor = database.rawQuery(query, null);

        ExitPoll newExitPoll;
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            states[i] = cursor.getString(0);
            i++;
            cursor.moveToNext();
        }
        cursor.close();

        return states;
    }
    public String[] getArrayOfVoterGroupsByCandidate(String candidate){
        String[] voterGroups = new String[getNumberOfVoterGroupsByCandidate(candidate)];
        int i = 0;

        String query = "SELECT DISTINCT votergroup FROM exitpoll WHERE candidate='" + candidate + "'";
        Cursor cursor = database.rawQuery(query, null);

        ExitPoll newExitPoll;
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            voterGroups[i] = cursor.getString(0);
            i++;
            cursor.moveToNext();
        }
        cursor.close();

        return voterGroups;
    }
    public int getNumberOfVoterGroupsByCandidate(String candidate){
        int numberOfVoterGroups = 0;

        String query = "SELECT DISTINCT votergroup FROM exitpoll Where candidate='" + candidate + "'";
        Cursor cursor = database.rawQuery(query, null);
        if (cursor.getCount() > 0 && cursor.getColumnCount() > 0) {
            cursor.close();
            return cursor.getCount();
        } else {
            cursor.close();
            return 0;
        }
    }

    public String[] getArrayOfAllVoterGroupLabelsByCandidate(String candidate){
        String[] voterGroupLabels = new String[getNumberOfVoterGroupLabelsByCandidate(candidate)];
        int i = 0;

        String query = "SELECT DISTINCT votergrouplabel FROM exitpoll WHERE candidate='" + candidate +"'";
        Cursor cursor = database.rawQuery(query, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            voterGroupLabels[i] = cursor.getString(0);
            i++;
            cursor.moveToNext();
        }
        cursor.close();

        return voterGroupLabels;
    }
    public int getNumberOfVoterGroupLabelsByCandidate(String candidate){
        String query = "SELECT DISTINCT votergrouplabel FROM exitpoll WHERE candidate='" + candidate + "'";
        Cursor cursor = database.rawQuery(query, null);
        if (cursor.getCount() > 0 && cursor.getColumnCount() > 0) {
            cursor.close();
            return cursor.getCount();
        } else {
            cursor.close();
            return 0;
        }
    }
    /*
    public String[] getArrayOfAllVoterGroupLabels(){
        String[] voterGroupLabels = new String[getNumberOfVoterGroupLabels()];
        int i = 0;

        String query = "SELECT DISTINCT votergrouplabel FROM exitpoll";
        Cursor cursor = database.rawQuery(query, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            voterGroupLabels[i] = cursor.getString(0);
            i++;
            cursor.moveToNext();
        }
        cursor.close();

        return voterGroupLabels;
    }
    public int getNumberOfVoterGroupLabels(){
        String query = "SELECT DISTINCT votergrouplabel FROM exitpoll";
        Cursor cursor = database.rawQuery(query, null);
        if (cursor.getCount() > 0 && cursor.getColumnCount() > 0) {
            cursor.close();
            return cursor.getCount();
        } else {
            cursor.close();
            return 0;
        }
    } */

    public String getTopDemographicFromVoterGroupByCandidateByState(String candidate, String state, String voterGroupLabel){
        String topVoterGroup = "";

        switch (voterGroupLabel){
            case "gender":
                int percentMale = getPercentageOfVoteByStateAndCandidateAndVoterGroup(state, candidate, "male");
                int percentFemale = getPercentageOfVoteByStateAndCandidateAndVoterGroup(state, candidate, "female");
                if(percentMale > percentFemale){
                    topVoterGroup = "male";
                }
                else {
                    topVoterGroup = "female";
                }
                break;
            case "age":
                int percent17To29 = getPercentageOfVoteByStateAndCandidateAndVoterGroup(state, candidate, "17_29");
                int percent30To44 = getPercentageOfVoteByStateAndCandidateAndVoterGroup(state, candidate, "30_44");
                int percent45To64 = getPercentageOfVoteByStateAndCandidateAndVoterGroup(state, candidate, "45_64");
                int percent65Plus = getPercentageOfVoteByStateAndCandidateAndVoterGroup(state, candidate, "65_plus");
                topVoterGroup = "17_to_29";
                if(percent30To44 >= percent17To29 && percent30To44 >= percent45To64 && percent30To44 >= percent65Plus) {
                    topVoterGroup = "30_44";
                }
                else if (percent45To64 >= percent17To29 && percent45To64 >= percent30To44 && percent45To64 >= percent65Plus ) {
                    topVoterGroup = "45_64";
                }
                else if(percent65Plus >= percent17To29 && percent65Plus >= percent30To44 && percent65Plus >= percent45To64) {
                    topVoterGroup = "65_plus";
                }
                break;
            case "race":
                int percentWhite = getPercentageOfVoteByStateAndCandidateAndVoterGroup(state, candidate, "white");
                int percentBlack = getPercentageOfVoteByStateAndCandidateAndVoterGroup(state, candidate, "black");
                int percentLatino = getPercentageOfVoteByStateAndCandidateAndVoterGroup(state, candidate, "latino");
                int percentOther = getPercentageOfVoteByStateAndCandidateAndVoterGroup(state, candidate, "other");
                topVoterGroup = "white";
                if(percentBlack >= percentWhite && percentBlack >= percentLatino && percentBlack >= percentOther) {
                    topVoterGroup = "black";
                }
                else if (percentLatino >= percentWhite && percentLatino >= percentBlack && percentLatino >= percentLatino ) {
                    topVoterGroup = "latino";
                }
                else if(percentOther >= percentWhite && percentOther >= percentBlack && percentOther >= percentLatino) {
                    topVoterGroup = "other";
                }
                break;
            case "education":
                int percentHighschoolOrLess = getPercentageOfVoteByStateAndCandidateAndVoterGroup(state, candidate, "highschool_or_less");
                int percentSomeCollege = getPercentageOfVoteByStateAndCandidateAndVoterGroup(state, candidate, "some_college");
                int percentCollegeGraduate = getPercentageOfVoteByStateAndCandidateAndVoterGroup(state, candidate, "college_graduate");
                int percentPostgraduate = getPercentageOfVoteByStateAndCandidateAndVoterGroup(state, candidate, "postgraduate");
                topVoterGroup = "highschool_or_less";
                if(percentSomeCollege >= percentHighschoolOrLess && percentSomeCollege >= percentCollegeGraduate && percentSomeCollege >= percentPostgraduate) {
                    topVoterGroup = "some_college";
                }
                else if (percentCollegeGraduate >= percentHighschoolOrLess && percentCollegeGraduate >= percentSomeCollege && percentCollegeGraduate >= percentSomeCollege ) {
                    topVoterGroup = "college_graduate";
                }
                else if (percentPostgraduate >= percentHighschoolOrLess && percentPostgraduate >= percentSomeCollege && percentPostgraduate >= percentCollegeGraduate ) {
                    topVoterGroup = "postgraduate";
                }
                break;
            case "income":
                int percentLessThan50k = getPercentageOfVoteByStateAndCandidateAndVoterGroup(state, candidate, "less_than_50k");
                int percent50kTo100k = getPercentageOfVoteByStateAndCandidateAndVoterGroup(state, candidate, "50k_to_100k");
                int percentGreaterThan50k = getPercentageOfVoteByStateAndCandidateAndVoterGroup(state, candidate, "greater_than_100k");
                topVoterGroup = "less_than_50k";
                if(percent50kTo100k >= percentLessThan50k && percent50kTo100k >= percentGreaterThan50k) {
                    topVoterGroup = "50k_to_100k";
                }
                else if (percentGreaterThan50k >= percentLessThan50k && percentGreaterThan50k >= percent50kTo100k) {
                    topVoterGroup = "greater_than_100k";
                }
                break;
            case "party_affiliation":
                int democrats = getPercentageOfVoteByStateAndCandidateAndVoterGroup(state, candidate, "democrats");
                int republicans = getPercentageOfVoteByStateAndCandidateAndVoterGroup(state, candidate, "republicans");
                int independents = getPercentageOfVoteByStateAndCandidateAndVoterGroup(state, candidate, "independents");
                topVoterGroup = "democrats";
                if(republicans >= democrats && republicans >= independents) {
                    topVoterGroup = "republicans";
                }
                else if (independents >= democrats && independents >= republicans) {
                    topVoterGroup = "independents";
                }
                break;
            case "idealogy":
                int liberal = getPercentageOfVoteByStateAndCandidateAndVoterGroup(state, candidate, "liberal");
                int moderate = getPercentageOfVoteByStateAndCandidateAndVoterGroup(state, candidate, "moderate");
                int conservative = getPercentageOfVoteByStateAndCandidateAndVoterGroup(state, candidate, "conservative");
                topVoterGroup = "liberal";
                if(moderate >= liberal && moderate >= conservative) {
                    topVoterGroup = "moderate";
                }
                else if (conservative >= liberal && conservative >= moderate) {
                    topVoterGroup = "conservative";
                }
                break;
            case "marriage_status":
                int married = getPercentageOfVoteByStateAndCandidateAndVoterGroup(state, candidate, "married");
                int single = getPercentageOfVoteByStateAndCandidateAndVoterGroup(state, candidate, "single");
                if(married > single){
                    topVoterGroup = "married";
                }
                else {
                    topVoterGroup = "single";
                }
                break;
            case "religion":
                int evangelical = getPercentageOfVoteByStateAndCandidateAndVoterGroup(state, candidate, "evangelical");
                int not_evangelical = getPercentageOfVoteByStateAndCandidateAndVoterGroup(state, candidate, "not_evangelical");
                if(evangelical > not_evangelical){
                    topVoterGroup = "evangelical";
                }
                else {
                    topVoterGroup = "not_evangelical";
                }
                break;
            case "most_important_issue":
                if(candidate.equals("Bernie Sanders") || candidate.equals("Hillary Clinton")){
                    int health_care = getPercentageOfVoteByStateAndCandidateAndVoterGroup(state, candidate, "health_care");
                    int economy = getPercentageOfVoteByStateAndCandidateAndVoterGroup(state, candidate, "economy");
                    int terrorism = getPercentageOfVoteByStateAndCandidateAndVoterGroup(state, candidate, "terrorism");
                    int income_inequality = getPercentageOfVoteByStateAndCandidateAndVoterGroup(state, candidate, "income_inequality");

                    topVoterGroup = "health_care";
                    if(economy >= health_care && economy >= terrorism && economy >= income_inequality) {
                        topVoterGroup = "economy";
                    }
                    else if (terrorism >= health_care && terrorism >= economy && terrorism >= income_inequality ) {
                        topVoterGroup = "terrorism";
                    }
                    else if (income_inequality >= health_care && income_inequality >= economy && income_inequality >= terrorism ) {
                        topVoterGroup = "income_inequality";
                    }
                }
                else {
                    int immigration = getPercentageOfVoteByStateAndCandidateAndVoterGroup(state, candidate, "immigration");
                    int economy = getPercentageOfVoteByStateAndCandidateAndVoterGroup(state, candidate, "economy");
                    int terrorism = getPercentageOfVoteByStateAndCandidateAndVoterGroup(state, candidate, "terrorism");
                    int government_spending = getPercentageOfVoteByStateAndCandidateAndVoterGroup(state, candidate, "government_spending");

                    topVoterGroup = "immigration";
                    if(economy >= immigration && economy >= terrorism && economy >= government_spending) {
                        topVoterGroup = "economy";
                    }
                    else if (terrorism >= immigration && terrorism >= economy && terrorism >= government_spending ) {
                        topVoterGroup = "terrorism";
                    }
                    else if (government_spending >= immigration && government_spending >= economy && government_spending >= terrorism ) {
                        topVoterGroup = "government_spending";
                    }
                }
                break;
            case "feelings_about_government":
                int enthusiastic = getPercentageOfVoteByStateAndCandidateAndVoterGroup(state, candidate, "enthusiastic");
                int satisfied = getPercentageOfVoteByStateAndCandidateAndVoterGroup(state, candidate, "satisfied");
                int dissatisfied = getPercentageOfVoteByStateAndCandidateAndVoterGroup(state, candidate, "dissatisfied");
                int angry = getPercentageOfVoteByStateAndCandidateAndVoterGroup(state, candidate, "angry");
                topVoterGroup = "enthusiastic";
                if(satisfied >= enthusiastic && satisfied >= dissatisfied && satisfied >= angry) {
                    topVoterGroup = "satisfied";
                }
                else if (dissatisfied >= enthusiastic && dissatisfied >= satisfied && dissatisfied >= angry ) {
                    topVoterGroup = "dissatisfied";
                }
                else if (angry >= enthusiastic && angry >= satisfied && angry >= dissatisfied ) {
                    topVoterGroup = "angry";
                }
                break;
            case "preferred_candidate_experience":
                int experienced = getPercentageOfVoteByStateAndCandidateAndVoterGroup(state, candidate, "experienced");
                int outsider = getPercentageOfVoteByStateAndCandidateAndVoterGroup(state, candidate, "outsider");
                if(experienced > outsider){
                    topVoterGroup = "experienced";
                }
                else {
                    topVoterGroup = "outsider";
                }
                break;
            case "region":
                int urban = getPercentageOfVoteByStateAndCandidateAndVoterGroup(state, candidate, "urban");
                int suburban = getPercentageOfVoteByStateAndCandidateAndVoterGroup(state, candidate, "suburban");
                int rural = getPercentageOfVoteByStateAndCandidateAndVoterGroup(state, candidate, "rural");
                topVoterGroup = "urban";
                if(suburban >= urban && suburban >= rural) {
                    topVoterGroup = "suburban";
                }
                else if (rural >= urban && rural >= suburban) {
                    topVoterGroup = "rural";
                }
                break;
            case "decided_to_vote":
                int decided_long_ago = getPercentageOfVoteByStateAndCandidateAndVoterGroup(state, candidate, "decided_long_ago");
                int decided_in_last_month = getPercentageOfVoteByStateAndCandidateAndVoterGroup(state, candidate, "decided_in_last_month");
                int decided_in_last_week = getPercentageOfVoteByStateAndCandidateAndVoterGroup(state, candidate, "decided_in_last_week");
                int decided_today = getPercentageOfVoteByStateAndCandidateAndVoterGroup(state, candidate, "decided_today");
                topVoterGroup = "decided_long_ago";
                if(decided_in_last_month >= decided_long_ago && decided_in_last_month >= decided_in_last_week && decided_in_last_month >= decided_today) {
                    topVoterGroup = "decided_in_last_month";
                }
                else if (decided_in_last_week >= decided_long_ago && decided_in_last_week >= decided_in_last_month && decided_in_last_week >= decided_today ) {
                    topVoterGroup = "decided_in_last_week";
                }
                else if (decided_today >= decided_long_ago && decided_today >= decided_in_last_month && decided_today >= decided_in_last_week ) {
                    topVoterGroup = "decided_today";
                }
                break;
            case "how_much_like_obama":
                int like_obama = getPercentageOfVoteByStateAndCandidateAndVoterGroup(state, candidate, "like_obama");
                int more_liberal = getPercentageOfVoteByStateAndCandidateAndVoterGroup(state, candidate, "more_liberal");
                int less_liberal = getPercentageOfVoteByStateAndCandidateAndVoterGroup(state, candidate, "less_liberal");
                topVoterGroup = "like_obama";
                if(more_liberal >= like_obama && more_liberal >= less_liberal) {
                    topVoterGroup = "more_liberal";
                }
                else if (less_liberal >= like_obama && less_liberal >= more_liberal) {
                    topVoterGroup = "less_liberal";
                }
                break;
            default:
                topVoterGroup = "";
                break;
        }
        return topVoterGroup;
    }

    public String[][] generateFormattedArrayForDataMining(String candidate){
        String[] states = getArrayOfAllStates(),
                voterGroupLabels = getArrayOfAllVoterGroupLabelsByCandidate(candidate);
        int numberOfVoterGroupLabels = getNumberOfVoterGroupLabelsByCandidate(candidate);

        String currentState, currentVoterGroup, currentVoterGroupLabel;

        int rowCount = getNumberOfStates(), columnCount = numberOfVoterGroupLabels + 1;
        String[][] arrayFormattedPercentages = new String[rowCount][columnCount];
        for(int i = 0; i < rowCount; i++){
            currentState = states[i];
            arrayFormattedPercentages[i][0] = currentState;
            for(int j = 1; j < columnCount; j++){
                currentVoterGroupLabel = voterGroupLabels[j - 1];
                currentVoterGroup = getTopVoterGroupByVoterGroupLabelAndStateAndCandidate(currentVoterGroupLabel,currentState,candidate);
                arrayFormattedPercentages[i][j] = currentVoterGroup;
            }
        }

        return arrayFormattedPercentages;
    }
    public void pruneSet(ArrayList<DataMiningItemSet> listOfCandidates, int supportMinimum){
        Iterator<DataMiningItemSet> iter = listOfCandidates.iterator();
        while(iter.hasNext()){
            if(iter.next().getSupport()<supportMinimum){
                iter.remove();
            }
        }
    }
    public String getMostCommonTypeOfVoterByCandidate(String candidate){
        String returnString = "";

        String[][] formattedArray = generateFormattedArrayForDataMining(candidate);

        ArrayList<DataMiningItemSet> listOfCandidates = generateArrayListOfItemSets(formattedArray, getNumberOfStates(), getArrayOfVoterGroupsByCandidate(candidate), getNumberOfVoterGroupsByCandidate(candidate));

        ArrayList<DataMiningItemSet> resultsList = findMostFrequentItemSetApriori(formattedArray, listOfCandidates, 4, 5);

        DataMiningItemSet currentDataMiningItemSet;
        int currentMaxSupportCount = 0;
        for(int i =0; i < resultsList.size(); i++){
            currentDataMiningItemSet = resultsList.get(i);
            if(currentDataMiningItemSet.getSupport() > currentMaxSupportCount){
                returnString = "The typlical " + candidate + " supporter is " + currentDataMiningItemSet.getItemSetName();
            }
        }

        return returnString;
    }

    public ArrayList<DataMiningItemSet> findMostFrequentItemSetApriori(String[][] formattedList, ArrayList<DataMiningItemSet> listOfCandidates, int supportMinimum, int confidenceMinimum){
        /* First Step - Prune list of itemsets */
        pruneSet(listOfCandidates, supportMinimum);

        /* Second Step - Generate the set of frequent 2-itemsets */
        ArrayList<DataMiningItemSet> twoFrequent = generateSetOfFrequentNItemsets(formattedList, listOfCandidates, 2);


        /* Second Step Part Two - Prune the set of frequent 2-itemsets using minimum support count */
        pruneSet(twoFrequent, supportMinimum);

        /* Third Step - repeat step two until no new frequent item sets can be generated */
        ArrayList<DataMiningItemSet> nFrequent = twoFrequent;
        ArrayList<DataMiningItemSet> temp = nFrequent;

        /*
        int freqNum = 3;
        while(temp.size()>=5){
            temp = generateSetOfFrequentNItemsets(formattedList,nFrequent,freqNum);
            pruneSet(temp, supportMinimum);
            if(temp.size()>5){
                freqNum++;
                nFrequent = temp;
            }
            else {
                return nFrequent;
            }
        }
        */
        Set<DataMiningItemSet> uniqueList;

        nFrequent = generateSetOfFrequentNItemsets(formattedList, temp, 3);
        pruneSet(nFrequent, supportMinimum);

        temp = nFrequent;

        nFrequent = generateSetOfFrequentNItemsets(formattedList, temp, 4);
        pruneSet(nFrequent, supportMinimum);

        temp = nFrequent;

        nFrequent = generateSetOfFrequentNItemsets(formattedList, temp, 5);
        pruneSet(nFrequent, supportMinimum);

        Set<DataMiningItemSet> uniqueSet = new HashSet<>(nFrequent);
        uniqueSet.retainAll(nFrequent);
        nFrequent.clear();
        nFrequent.addAll(uniqueSet);



        return nFrequent;
    }

    public ArrayList<DataMiningItemSet> generateSetOfFrequentNItemsets(String[][] formattedList, ArrayList<DataMiningItemSet> listOfCandidates, int freq){
        ArrayList<DataMiningItemSet> freqSet = new ArrayList<DataMiningItemSet>();
        DataMiningItemSet itemSetTemp1, itemSetTemp2, itemSetTemp3, itemSetTemp4, itemSetTemp5, itemSetTemp6, itemSetTemp7, itemTempCombined;
        String one, two;
        String[] oneSplited, twoSplited;
        String split_one, split_two, split_three, split_four, split_five, split_six, split_seven, split_eight;

        switch(freq){
            case 2:

                for(int i = 0; i < listOfCandidates.size(); i++) {
                    itemSetTemp1 = listOfCandidates.get(i);
                    for (int j = i + 1; j < listOfCandidates.size(); j++) {
                        itemSetTemp2 = listOfCandidates.get(j);

                        itemTempCombined = combineDataMiningSets(formattedList, itemSetTemp1, itemSetTemp2);
                        freqSet.add(itemTempCombined);

                    }
                }
                break;
            case 3:

                for(int i = 0; i < listOfCandidates.size(); i++) {
                    itemSetTemp1 = listOfCandidates.get(i);
                    one = itemSetTemp1.getItemSetName();
                    oneSplited = one.split("\\s+");
                    split_one = oneSplited[0];
                    split_two = oneSplited[1];

                    for (int j = i + 1; j < listOfCandidates.size(); j++) {
                        itemSetTemp2 = listOfCandidates.get(j);

                        two = itemSetTemp2.getItemSetName();
                        twoSplited = two.split("\\s+");
                        split_three = twoSplited[0];
                        split_four = twoSplited[1];

                        if(split_two.equalsIgnoreCase(split_three)){
                            itemTempCombined = combineDataMiningSets(formattedList, split_one, split_two, split_four);
                            freqSet.add(itemTempCombined);
                        }
                    }
                }

                break;
            case 4:

                for(int i = 0; i < listOfCandidates.size(); i++) {
                    itemSetTemp1 = listOfCandidates.get(i);
                    one = itemSetTemp1.getItemSetName();
                    oneSplited = one.split("\\s+");
                    split_one = oneSplited[0];
                    split_two = oneSplited[1];
                    split_three = oneSplited[2];

                    for (int j = i + 1; j < listOfCandidates.size(); j++) {
                        itemSetTemp2 = listOfCandidates.get(j);

                        two = itemSetTemp2.getItemSetName();
                        twoSplited = two.split("\\s+");
                        split_four = twoSplited[0];
                        split_five = twoSplited[1];
                        split_six = twoSplited[2];


                        List<String> list1 = new ArrayList<String>(Arrays.asList(oneSplited));
                        List<String> list2 = new ArrayList<String>(Arrays.asList(twoSplited));
                        list1.retainAll(list2);




                        Set<String> setNewWhat = new HashSet<String>();
                        setNewWhat.add(split_one);
                        setNewWhat.add(split_two);
                        setNewWhat.add(split_three);
                        setNewWhat.add(split_four);
                        setNewWhat.add(split_five);
                        setNewWhat.add(split_six);

                        if(setNewWhat.size() <= 4 ) {
                            String[] thisShouldDoIt = new String[4];
                            int p = 0;
                            for (String stuff : setNewWhat) {
                                thisShouldDoIt[p] = stuff;
                                p++;
                            }
                            itemTempCombined = combineDataMiningSets(formattedList, thisShouldDoIt[0], thisShouldDoIt[1], thisShouldDoIt[2], thisShouldDoIt[3]);
                            freqSet.add(itemTempCombined);
                        }
                    }
                }

                break;
            case 5:

                for(int i = 0; i < listOfCandidates.size(); i++) {
                    itemSetTemp1 = listOfCandidates.get(i);
                    one = itemSetTemp1.getItemSetName();
                    oneSplited = one.split("\\s+");
                    split_one = oneSplited[0];
                    split_two = oneSplited[1];
                    split_three = oneSplited[2];
                    split_four = oneSplited[3];

                    for (int j = i + 1; j < listOfCandidates.size(); j++) {
                        itemSetTemp2 = listOfCandidates.get(j);

                        two = itemSetTemp2.getItemSetName();
                        twoSplited = two.split("\\s+");
                        split_five = twoSplited[0];
                        split_six = twoSplited[1];
                        split_seven = twoSplited[2];
                        split_eight = twoSplited[3];

                        Set<String> setNewWhat = new HashSet<String>();
                        setNewWhat.add(split_one);
                        setNewWhat.add(split_two);
                        setNewWhat.add(split_three);
                        setNewWhat.add(split_four);
                        setNewWhat.add(split_five);
                        setNewWhat.add(split_six);
                        setNewWhat.add(split_seven);
                        setNewWhat.add(split_eight);

                        if(setNewWhat.size() <= 5 ) {
                            String[] thisShouldDoIt = new String[5];
                            int p = 0;
                            for (String stuff : setNewWhat) {
                                thisShouldDoIt[p] = stuff;
                                p++;
                            }
                            itemTempCombined = combineDataMiningSets(formattedList, thisShouldDoIt[0], thisShouldDoIt[1], thisShouldDoIt[2], thisShouldDoIt[3], thisShouldDoIt[4]);
                            freqSet.add(itemTempCombined);
                        }
                    }
                }

                break;

        }
        return freqSet;
    }

    public DataMiningItemSet combineDataMiningSets(String[][] formattedArray, DataMiningItemSet set1, DataMiningItemSet set2){
        DataMiningItemSet newItemSet = new DataMiningItemSet();
        String voterGroup1 = set1.getItemSetName();
        String voterGroup2 = set2.getItemSetName();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(voterGroup1);
        stringBuilder.append(" ");
        stringBuilder.append(voterGroup2);
        String voterGroupCombined = stringBuilder.toString();
        int supportCountCombined = getSupportCountFromFormattedList(formattedArray, voterGroup1, voterGroup2);

        newItemSet.setItemSetName(voterGroupCombined);
        newItemSet.setSupport(supportCountCombined);

        return newItemSet;
    }
    public DataMiningItemSet combineDataMiningSets(String[][] formattedArray, String set1, String set2, String set3){
        DataMiningItemSet newItemSet = new DataMiningItemSet();
        String voterGroup1 = set1;
        String voterGroup2 = set2;
        String voterGroup3 = set3;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(voterGroup1);
        stringBuilder.append(" ");
        stringBuilder.append(voterGroup2);
        stringBuilder.append(" ");
        stringBuilder.append(voterGroup3);
        String voterGroupCombined = stringBuilder.toString();
        int supportCountCombined = getSupportCountFromFormattedList(formattedArray, voterGroup1, voterGroup2, voterGroup3);

        newItemSet.setItemSetName(voterGroupCombined);
        newItemSet.setSupport(supportCountCombined);

        return newItemSet;
    }
    public DataMiningItemSet combineDataMiningSets(String[][] formattedArray, String set1, String set2, String set3, String set4){
        DataMiningItemSet newItemSet = new DataMiningItemSet();
        String voterGroup1 = set1;
        String voterGroup2 = set2;
        String voterGroup3 = set3;
        String voterGroup4 = set4;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(voterGroup1);
        stringBuilder.append(" ");
        stringBuilder.append(voterGroup2);
        stringBuilder.append(" ");
        stringBuilder.append(voterGroup3);
        stringBuilder.append(" ");
        stringBuilder.append(voterGroup4);
        String voterGroupCombined = stringBuilder.toString();
        int supportCountCombined = getSupportCountFromFormattedList(formattedArray, voterGroup1, voterGroup2, voterGroup3, voterGroup4);

        newItemSet.setItemSetName(voterGroupCombined);
        newItemSet.setSupport(supportCountCombined);

        return newItemSet;
    }
    public DataMiningItemSet combineDataMiningSets(String[][] formattedArray, String set1, String set2, String set3, String set4, String set5){
        DataMiningItemSet newItemSet = new DataMiningItemSet();
        String voterGroup1 = set1;
        String voterGroup2 = set2;
        String voterGroup3 = set3;
        String voterGroup4 = set4;
        String voterGroup5 = set5;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(voterGroup1);
        stringBuilder.append(" ");
        stringBuilder.append(voterGroup2);
        stringBuilder.append(" ");
        stringBuilder.append(voterGroup3);
        stringBuilder.append(" ");
        stringBuilder.append(voterGroup4);
        stringBuilder.append(" ");
        stringBuilder.append(voterGroup5);
        String voterGroupCombined = stringBuilder.toString();
        int supportCountCombined = getSupportCountFromFormattedList(formattedArray, voterGroup1, voterGroup2, voterGroup3, voterGroup4, voterGroup5);

        newItemSet.setItemSetName(voterGroupCombined);
        newItemSet.setSupport(supportCountCombined);

        return newItemSet;
    }

    public ArrayList<DataMiningItemSet> generateArrayListOfItemSets(String[][] formattedArray, int numberOfStates , String[] arrayOfVoterGroups, int numberOfVoterGroups){
        ArrayList<DataMiningItemSet> listOfCandidates = new ArrayList<>();
        DataMiningItemSet currentItemSet;

        String currentVoterGroup;
        int currentSupportCount = 0;
        int columnCount = numberOfVoterGroups;

        for (int j = 0; j < columnCount; j++ ) {
            currentVoterGroup = arrayOfVoterGroups[j];
            currentItemSet = new DataMiningItemSet();
            currentItemSet.setItemSetName(arrayOfVoterGroups[j]);

            currentSupportCount = getSupportCountFromFormattedList(formattedArray, currentVoterGroup);

            currentItemSet.setSupport(currentSupportCount);
            listOfCandidates.add(currentItemSet);
        }

        return listOfCandidates;
    }
    public int getSupportCountFromFormattedList(String[][] formattedArray, String currentVoterGroup){
        int supportCount = 0;

        for(int i = 0; i < formattedArray.length; i++){
            for(int j = 0; j < formattedArray[i].length; j++){
                if(formattedArray[i][j].equalsIgnoreCase(currentVoterGroup)){
                    supportCount++;
                }
            }
        }

        return supportCount;
    }
    public int getSupportCountFromFormattedList(String[][] formattedArray, String currentVoterGroup, String currentVoterGroup2){
        int supportCount = 0;
        boolean currentVoterGroupFoundTemp = false, currentVoterGroup2FoundTemp = false;

        for(int i = 0; i < formattedArray.length; i++){
            for(int j = 0; j < formattedArray[i].length; j++){
                if(formattedArray[i][j].equalsIgnoreCase(currentVoterGroup)){
                    currentVoterGroupFoundTemp = true;
                }
                if(formattedArray[i][j].equalsIgnoreCase(currentVoterGroup2)) {
                    currentVoterGroup2FoundTemp = true;
                }
            }
            if(currentVoterGroupFoundTemp && currentVoterGroup2FoundTemp){
                supportCount++;
            }
            currentVoterGroupFoundTemp = false;
            currentVoterGroup2FoundTemp = false;
        }
        return supportCount;
    }
    public int getSupportCountFromFormattedList(String[][] formattedArray, String currentVoterGroup, String currentVoterGroup2, String currentVoterGroup3){
        int supportCount = 0;
        boolean currentVoterGroupFoundTemp = false, currentVoterGroup2FoundTemp = false, currentVoterGroup3FoundTemp = false;

        for(int i = 0; i < formattedArray.length; i++){
            for(int j = 0; j < formattedArray[i].length; j++){
                if(formattedArray[i][j].equalsIgnoreCase(currentVoterGroup)){
                    currentVoterGroupFoundTemp = true;
                }
                if(formattedArray[i][j].equalsIgnoreCase(currentVoterGroup2)) {
                    currentVoterGroup2FoundTemp = true;
                }
                if(formattedArray[i][j].equalsIgnoreCase(currentVoterGroup3)) {
                    currentVoterGroup3FoundTemp = true;
                }
            }
            if(currentVoterGroupFoundTemp && currentVoterGroup2FoundTemp && currentVoterGroup3FoundTemp){
                supportCount++;
            }
            currentVoterGroupFoundTemp = false;
            currentVoterGroup2FoundTemp = false;
            currentVoterGroup3FoundTemp = false;
        }
        return supportCount;
    }
    public int getSupportCountFromFormattedList(String[][] formattedArray, String currentVoterGroup, String currentVoterGroup2, String currentVoterGroup3, String currentVoterGroup4){
        int supportCount = 0;
        boolean currentVoterGroupFoundTemp = false, currentVoterGroup2FoundTemp = false, currentVoterGroup3FoundTemp = false, currentVoterGroup4FoundTemp = false;

        for(int i = 0; i < formattedArray.length; i++){
            for(int j = 0; j < formattedArray[i].length; j++){
                if(formattedArray[i][j].equalsIgnoreCase(currentVoterGroup)){
                    currentVoterGroupFoundTemp = true;
                }
                if(formattedArray[i][j].equalsIgnoreCase(currentVoterGroup2)) {
                    currentVoterGroup2FoundTemp = true;
                }
                if(formattedArray[i][j].equalsIgnoreCase(currentVoterGroup3)) {
                    currentVoterGroup3FoundTemp = true;
                }
                if(formattedArray[i][j].equalsIgnoreCase(currentVoterGroup4)) {
                    currentVoterGroup4FoundTemp = true;
                }
            }
            if(currentVoterGroupFoundTemp && currentVoterGroup2FoundTemp && currentVoterGroup3FoundTemp && currentVoterGroup4FoundTemp){
                supportCount++;
            }
            currentVoterGroupFoundTemp = false;
            currentVoterGroup2FoundTemp = false;
            currentVoterGroup3FoundTemp = false;
            currentVoterGroup4FoundTemp = false;
        }
        return supportCount;
    }
    public int getSupportCountFromFormattedList(String[][] formattedArray, String currentVoterGroup, String currentVoterGroup2, String currentVoterGroup3, String currentVoterGroup4, String currentVoterGroup5){
        int supportCount = 0;
        boolean currentVoterGroupFoundTemp = false, currentVoterGroup2FoundTemp = false, currentVoterGroup3FoundTemp = false, currentVoterGroup4FoundTemp = false, currentVoterGroup5FoundTemp = false;

        for(int i = 0; i < formattedArray.length; i++){
            for(int j = 0; j < formattedArray[i].length; j++){
                if(formattedArray[i][j].equalsIgnoreCase(currentVoterGroup)){
                    currentVoterGroupFoundTemp = true;
                }
                if(formattedArray[i][j].equalsIgnoreCase(currentVoterGroup2)) {
                    currentVoterGroup2FoundTemp = true;
                }
                if(formattedArray[i][j].equalsIgnoreCase(currentVoterGroup3)) {
                    currentVoterGroup3FoundTemp = true;
                }
                if(formattedArray[i][j].equalsIgnoreCase(currentVoterGroup4)) {
                    currentVoterGroup4FoundTemp = true;
                }
                if(formattedArray[i][j].equalsIgnoreCase(currentVoterGroup5)) {
                    currentVoterGroup5FoundTemp = true;
                }
            }
            if(currentVoterGroupFoundTemp && currentVoterGroup2FoundTemp && currentVoterGroup3FoundTemp && currentVoterGroup4FoundTemp && currentVoterGroup5FoundTemp){
                supportCount++;
            }
            currentVoterGroupFoundTemp = false;
            currentVoterGroup2FoundTemp = false;
            currentVoterGroup3FoundTemp = false;
            currentVoterGroup4FoundTemp = false;
            currentVoterGroup5FoundTemp = false;
        }
        return supportCount;
    }

}
