package com.example.bradleycooper.politicslive;

/**
 * Created by Bradley Cooper on 9/12/2016.
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class PoliticalPartyDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "politicalparties.db";
    private static final int DATABASE_VERSION = 1;

    //Database creation sql statement
    private static final String CREATE_TABLE_POLITICALPARTY = "create table politicalparty (_id integer primary key autoincrement, "
            + "name text not null, description text, "
            + "squarepicture text, widepicture text, "
            + "numberofhouseseats text, numberofsenateseats text, "
            + "hufffavorablerating text, huffunfavorablerating text, "
            + "site text, email text, "
            + "twitter text, percentageofhousevote text );";

    public PoliticalPartyDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_TABLE_POLITICALPARTY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(PoliticalPartyDBHelper.class.getName(), "Upgrading database from version " + oldVersion + "to " + newVersion + ", which will destroy all old date");

        db.execSQL("DROP TABLE IF EXISTS contact");
        onCreate(db);
    }
}
