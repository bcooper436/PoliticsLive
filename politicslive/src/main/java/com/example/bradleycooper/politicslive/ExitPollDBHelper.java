package com.example.bradleycooper.politicslive;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Bradley Cooper on 4/4/2016.
 */
public class ExitPollDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "exitpolls.db";
    private static final int DATABASE_VERSION = 1;

    //Database creation sql statement
    private static final String CREATE_TABLE_EXITPOLL = "create table exitpoll (_id integer primary key autoincrement, "
            + "state text, totalresponders text, "
            + "candidate text, votergroup text, "
            + "percentageofvote text, votergrouplabel text);";

    public ExitPollDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_TABLE_EXITPOLL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(CandidateDBHelper.class.getName(), "Upgrading database from version " + oldVersion + "to " + newVersion + ", which will destroy all old date");

        db.execSQL("DROP TABLE IF EXISTS contact");
        onCreate(db);
    }
}
