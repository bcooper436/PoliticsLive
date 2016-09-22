package com.example.bradleycooper.politicslive;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Bradley Cooper on 4/4/2016.
 */
public class CandidateDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "candidates.db";
    private static final int DATABASE_VERSION = 3;

    //Database creation sql statement
    private static final String CREATE_TABLE_CANDIDATE = "create table candidate (_id integer primary key autoincrement, "
            + "candidatename text not null, candidatedescription text, "
            + "numberofvotes text, squarepicture text, "
            + "widepicture text, party text, "
            + "delegatecount text, huffpercentageofvote text, "
            + "huffpercentageofvotegeneral text, hufffavorablerating text, "
            + "huffunfavorablerating text, site text , "
            + "email text, twitter text , "
            + "electiontype text, typeofvoter text );";


    public CandidateDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_TABLE_CANDIDATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(CandidateDBHelper.class.getName(), "Upgrading database from version " + oldVersion + "to " + newVersion + ", which will destroy all old date");

        db.execSQL("DROP TABLE IF EXISTS contact");
        onCreate(db);
    }
}
