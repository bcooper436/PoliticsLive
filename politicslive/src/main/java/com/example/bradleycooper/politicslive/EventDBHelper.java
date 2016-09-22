package com.example.bradleycooper.politicslive;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Bradley Cooper on 9/12/2016.
 */
public class EventDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "events.db";
    private static final int DATABASE_VERSION = 1;

    //Database creation sql statement
    private static final String CREATE_TABLE_EVENT = "create table event (_id integer primary key autoincrement, "
            + "name text not null, description text, "
            + "date text, time text, "
            + "channel text, channelpic text, "
            + "moderator text, moderatorpic text, "
            + "site text, location text );";

    public EventDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_TABLE_EVENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(PoliticalPartyDBHelper.class.getName(), "Upgrading database from version " + oldVersion + "to " + newVersion + ", which will destroy all old date");

        db.execSQL("DROP TABLE IF EXISTS contact");
        onCreate(db);
    }
}
