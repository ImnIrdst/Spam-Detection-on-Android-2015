package com.example.prj14_iman.spamfilter_learning.Data;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.prj14_iman.spamfilter_learning.Data.LearningDbContract.*;

import java.util.ArrayList;

/**
 * Created by Prj14_IMAN on 8/8/2015.
 */
public class LearningDbHelper extends SQLiteOpenHelper{
    public final String LOG_TAG = LearningDbHelper.class.getSimpleName();

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Learning.db";

    public LearningDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_TABLE_SPAM_WORDS =
                "CREATE TABLE " + SpamWordsTable.TABLE_NAME + " ( "                         // TODO: Add If Not Exist
                        + SpamWordsTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + SpamWordsTable.COL_WORD + " TEXT NOT NULL, "
                        + SpamWordsTable.COL_FREQ + " INTEGER NOT NULL, "
                        + "UNIQUE ( " + SpamWordsTable.COL_WORD + " ) ON CONFLICT IGNORE);" // TODO: Can Be Set To Fail
                ;
        final String SQL_CREATE_TABLE_HAM_WORDS =
                "CREATE TABLE " + HamWordsTable.TABLE_NAME + " ( "                          // TODO: Add If Not Exist
                        + HamWordsTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + HamWordsTable.COL_WORD + " TEXT NOT NULL, "
                        + HamWordsTable.COL_FREQ + " INTEGER NOT NULL, "
                        + "UNIQUE ( " + HamWordsTable.COL_WORD + " ) ON CONFLICT IGNORE);" // TODO: Can Be Set To Fail
                ;
        final String SQL_CREATE_TABLE_SPAMMERS =
                "CREATE TABLE " + SpammersTable.TABLE_NAME + " ( "                            // TODO: Add If Not Exist
                        + SpammersTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + SpammersTable.COL_NAME + " TEXT NOT NULL, "
                        + SpammersTable.COL_NUMBER + " INTEGER NOT NULL,"
                        + "UNIQUE ( " + SpammersTable.COL_NUMBER + " ) ON CONFLICT IGNORE);" // TODO: Can Be Set To Fail
                ;
        db.execSQL(SQL_CREATE_TABLE_SPAM_WORDS);
        db.execSQL(SQL_CREATE_TABLE_HAM_WORDS);
        db.execSQL(SQL_CREATE_TABLE_SPAMMERS);
        //db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + SpamWordsTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + HamWordsTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + SpammersTable.TABLE_NAME);
        onCreate(db);
    }
}
