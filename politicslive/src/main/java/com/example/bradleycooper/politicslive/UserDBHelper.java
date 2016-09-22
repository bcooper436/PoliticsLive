package com.example.bradleycooper.politicslive;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Bradley Cooper on 4/5/2016.
 */
public class UserDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "users.db";
    private static final int DATABASE_VERSION = 2;

    //Database creation sql statement
    private static final String CREATE_TABLE_USER = "create table user (_id integer primary key autoincrement, "
            + "displayname text, username text, "
            + "password text, partyaffiliation text, "
            + "age text, gender text, "
            + "chosenDemocrat text, chosenRepublican text, "
            + "profilepicture text); ";

    public UserDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_TABLE_USER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(UserDBHelper.class.getName(), "Upgrading database from version " + oldVersion + "to " + newVersion + ", which will destroy all old date");

        db.execSQL("DROP TABLE IF EXISTS contact");
        onCreate(db);
    }
}
