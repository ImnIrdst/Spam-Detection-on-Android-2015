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
    public boolean isInitialized = false;

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

    public void initialCountAndSum(){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            if(getFreqOfWordInSpams(SpamWordsTable.ROW_SUM_ALL)!=0){
                isInitialized = true; return;
            }

            insertWordInitially(db, SpamWordsTable.ROW_SUM_ALL,
                    SpamWordsTable.TABLE_NAME, SpamWordsTable.COL_WORD, SpamWordsTable.COL_FREQ);
            insertWordInitially(db, SpamWordsTable.ROW_COUNT_ALL,
                    SpamWordsTable.TABLE_NAME, SpamWordsTable.COL_WORD, SpamWordsTable.COL_FREQ);
            insertWordInitially(db, SpamWordsTable.ROW_COUNT_ALL_DOCS,
                    SpamWordsTable.TABLE_NAME, SpamWordsTable.COL_WORD, SpamWordsTable.COL_FREQ);
            increaseWordFreq(db, SpamWordsTable.ROW_SUM_ALL, -1,
                    SpamWordsTable.TABLE_NAME, SpamWordsTable.COL_WORD, SpamWordsTable.COL_FREQ);
            increaseWordFreq(db, SpamWordsTable.ROW_COUNT_ALL, -1,
                    SpamWordsTable.TABLE_NAME, SpamWordsTable.COL_WORD, SpamWordsTable.COL_FREQ);
            increaseWordFreq(db, SpamWordsTable.ROW_COUNT_ALL_DOCS, -1,
                    SpamWordsTable.TABLE_NAME, SpamWordsTable.COL_WORD, SpamWordsTable.COL_FREQ);

            insertWordInitially(db, HamWordsTable.ROW_SUM_ALL,
                    HamWordsTable.TABLE_NAME, HamWordsTable.COL_WORD, HamWordsTable.COL_FREQ);
            insertWordInitially(db, HamWordsTable.ROW_COUNT_ALL,
                    HamWordsTable.TABLE_NAME, HamWordsTable.COL_WORD, HamWordsTable.COL_FREQ);
            insertWordInitially(db, HamWordsTable.ROW_COUNT_ALL_DOCS,
                    HamWordsTable.TABLE_NAME, HamWordsTable.COL_WORD, HamWordsTable.COL_FREQ);
            increaseWordFreq(db, HamWordsTable.ROW_SUM_ALL, -1,
                    HamWordsTable.TABLE_NAME, HamWordsTable.COL_WORD, HamWordsTable.COL_FREQ);
            increaseWordFreq(db, HamWordsTable.ROW_COUNT_ALL, -1,
                    HamWordsTable.TABLE_NAME, HamWordsTable.COL_WORD, HamWordsTable.COL_FREQ);
            increaseWordFreq(db, HamWordsTable.ROW_COUNT_ALL_DOCS, -1,
                    HamWordsTable.TABLE_NAME, HamWordsTable.COL_WORD, HamWordsTable.COL_FREQ);
            isInitialized = true;
        }catch (Exception e){
            Log.v("initialCountAndSum", e.toString());
        }
    }

    public void insertWordInitially(SQLiteDatabase db, String word,
                                    String TABLE_NAME, String COL_WORD, String COL_FREQ){
        final String SQL_INSERT_WORD_INITIALLY =
                "INSERT INTO " + TABLE_NAME + " ( "
                        + COL_WORD + ", " + COL_FREQ + ") "
                        + "VALUES (? , 1);"
                ;
        db.execSQL(SQL_INSERT_WORD_INITIALLY, new String[]{word});
    }

    public void increaseWordFreq(SQLiteDatabase db, String word, Integer queryFreq,
                                    String TABLE_NAME, String COL_WORD, String COL_FREQ){
        queryFreq = queryFreq+1;
        final String SQL_INCREASE_FREQ =
                "UPDATE " + SpamWordsTable.TABLE_NAME + " "
                        + "SET " + SpamWordsTable.COL_FREQ + " = ? "
                        + "WHERE " + SpamWordsTable.COL_WORD + " = ? ;"
                ;
        db.execSQL(SQL_INCREASE_FREQ, new String[]{queryFreq.toString(), word});
    }

    public void InsertWordIntoSpams(String word){
        SQLiteDatabase db = this.getWritableDatabase();
        Integer queryFreq = getFreqOfWordInSpams(word);
        if(queryFreq == 0){
            insertWordInitially(db, word,
                    SpamWordsTable.TABLE_NAME, SpamWordsTable.COL_WORD, SpamWordsTable.COL_FREQ);

            increaseWordFreq(db, SpamWordsTable.ROW_COUNT_ALL, getFreqOfWordInSpams(SpamWordsTable.ROW_COUNT_ALL),
                    SpamWordsTable.TABLE_NAME, SpamWordsTable.COL_WORD, SpamWordsTable.COL_FREQ);

            Log.v(LOG_TAG, "This Values Inserted as Spam: " + word + " 1"); // TODO: For Debug Clear
        }
        else{
            increaseWordFreq(db, word, queryFreq,
                    SpamWordsTable.TABLE_NAME, SpamWordsTable.COL_WORD, SpamWordsTable.COL_FREQ);

            Log.v(LOG_TAG, "This Values Updated as Spam: " + word + " " + queryFreq); // TODO: For Debug Clear
        }

        increaseWordFreq(db, SpamWordsTable.ROW_SUM_ALL, getFreqOfWordInSpams(SpamWordsTable.ROW_SUM_ALL),
                SpamWordsTable.TABLE_NAME, SpamWordsTable.COL_WORD, SpamWordsTable.COL_FREQ);

        // TODO: add db.close();
    }

    public void InsertWordIntoHams(String word){
        SQLiteDatabase db = this.getWritableDatabase();
        Integer queryFreq = getFreqOfWordInHams(word);
        if(queryFreq == 0){
            insertWordInitially(db, word,
                    HamWordsTable.TABLE_NAME, HamWordsTable.COL_WORD, HamWordsTable.COL_FREQ);

            increaseWordFreq(db, HamWordsTable.ROW_COUNT_ALL, getFreqOfWordInHams(HamWordsTable.ROW_COUNT_ALL),
                    HamWordsTable.TABLE_NAME, HamWordsTable.COL_WORD, HamWordsTable.COL_FREQ);

            Log.v(LOG_TAG, "This Values Inserted as Ham: " + word + " 1"); // TODO: For Debug Clear
        }
        else{
            increaseWordFreq(db, word, queryFreq,
                    HamWordsTable.TABLE_NAME, HamWordsTable.COL_WORD, HamWordsTable.COL_FREQ);
            Log.v(LOG_TAG, "This Values Updated as Ham: " + word + " " + queryFreq); // TODO: For Debug Clear
        }

        increaseWordFreq(db, HamWordsTable.ROW_SUM_ALL, getFreqOfWordInHams(HamWordsTable.ROW_SUM_ALL),
                HamWordsTable.TABLE_NAME, HamWordsTable.COL_WORD, HamWordsTable.COL_FREQ);
        // TODO: add db.close();
    }

    public int getFreqOfWordInSpams(String word){
        SQLiteDatabase db = this.getWritableDatabase();

        final String SQL_SELECT_WORD_FREQ =
                "SELECT * "
                        + "FROM " + SpamWordsTable.TABLE_NAME + " "
                        + "WHERE " + SpamWordsTable.COL_WORD + "= ? ;"
                ;
        Cursor cursor = db.rawQuery(SQL_SELECT_WORD_FREQ, new String[]{word});

        if(cursor == null || cursor.getCount() <= 0){
            return 0;
        }
        else{
            cursor.moveToFirst();
            String  queryWord = cursor.getString(cursor.getColumnIndex(SpamWordsTable.COL_WORD)); // TODO: For Debug CLear
            Integer queryFreq = cursor.getInt(cursor.getColumnIndex(SpamWordsTable.COL_FREQ));
            return queryFreq;
        }
        // TODO: add db.close();
    }

    public int getFreqOfWordInHams(String word){
        SQLiteDatabase db = this.getWritableDatabase();

        final String SQL_SELECT_WORD_FREQ =
                "SELECT * "
                        + "FROM " + HamWordsTable.TABLE_NAME + " "
                        + "WHERE " + HamWordsTable.COL_WORD + "= ? ;"
                ;
        Cursor cursor = db.rawQuery(SQL_SELECT_WORD_FREQ, new String[]{word});

        if(cursor.getCount() <= 0){
            return 0;
        }
        else{
            cursor.moveToFirst();
            String  queryWord = cursor.getString(cursor.getColumnIndex(HamWordsTable.COL_WORD)); // TODO: For Debug CLear
            Integer queryFreq = cursor.getInt(cursor.getColumnIndex(HamWordsTable.COL_FREQ));
            return queryFreq;
        }
        // TODO: add db.close();
    }

    public void setMessageAsSpam(String msg){
        if(!isInitialized) initialCountAndSum();
        ArrayList<String> words = preProcessMsg(msg);
        for(String word : words){
            InsertWordIntoSpams(word.trim());
        }

        SQLiteDatabase db = this.getWritableDatabase();
        increaseWordFreq(db, SpamWordsTable.ROW_COUNT_ALL_DOCS, getFreqOfWordInSpams(SpamWordsTable.ROW_COUNT_ALL_DOCS),
                SpamWordsTable.TABLE_NAME, SpamWordsTable.COL_WORD, SpamWordsTable.COL_FREQ);
    }

    public void setMessageAsHam(String msg){
        if(!isInitialized) initialCountAndSum();
        ArrayList<String> words = preProcessMsg(msg);
        for(String word : words){
            InsertWordIntoHams(word.trim());
        }

        SQLiteDatabase db = this.getWritableDatabase();
        increaseWordFreq(db, HamWordsTable.ROW_COUNT_ALL_DOCS, getFreqOfWordInSpams(HamWordsTable.ROW_COUNT_ALL_DOCS),
                HamWordsTable.TABLE_NAME, HamWordsTable.COL_WORD, HamWordsTable.COL_FREQ);
    }

    public boolean isSpam(String msg){
        if(!isInitialized) initialCountAndSum();
        double spamProbability=1;
        double hamProbability=1;

        double numberOfSpamDocs = getFreqOfWordInSpams(SpamWordsTable.ROW_COUNT_ALL_DOCS);
        double numberOfHamDocs  = getFreqOfWordInHams(HamWordsTable.ROW_COUNT_ALL_DOCS);
        double numberOfAllDocs  = numberOfHamDocs + numberOfSpamDocs;

        spamProbability *= (numberOfSpamDocs/numberOfAllDocs); // P(ClassSpam)
        hamProbability  *= (numberOfHamDocs/numberOfAllDocs);  // P(ClassHam)

        double sumOfAllSpamWords    = getFreqOfWordInSpams(SpamWordsTable.ROW_SUM_ALL);
        double countOfAllSpamWords  = getFreqOfWordInSpams(SpamWordsTable.ROW_COUNT_ALL);
        double sumOfAllHamWords     = getFreqOfWordInHams(HamWordsTable.ROW_SUM_ALL);
        double countOfAllHamWords   = getFreqOfWordInHams(HamWordsTable.ROW_COUNT_ALL);

        double freqInSpams, freqInHams;
        ArrayList<String> words = preProcessMsg(msg);
        for(String word : words){
            freqInSpams = getFreqOfWordInSpams(word);
            freqInHams  = getFreqOfWordInHams(word);

            spamProbability *= ((freqInSpams+1)/(sumOfAllSpamWords + countOfAllSpamWords)); //P(wi|ClassSpam)
            hamProbability  *= ((freqInHams+1 )/(sumOfAllHamWords + countOfAllHamWords));   //P(wi|ClassHam)
        }

        Log.v("in IsSpam", spamProbability + " " + hamProbability);
        return spamProbability > hamProbability;
    }

    public ArrayList<String> preProcessMsg(String msg){
        StringBuilder sb = new StringBuilder();
        for(int i=0 ; i<msg.length() ; i++){
            if(Character.isLetterOrDigit(msg.charAt(i)) || msg.charAt(i)=='\'') sb.append(msg.charAt(i));
            else sb.append(" ");
        }
        String[] tmp = sb.toString().trim().split(" ");
        ArrayList<String> words = new ArrayList<>();
        for(String word : tmp) { // TODO: Delete Stop Words (Intelligent Preprocess is The Key)
            if (word.trim().length() == 0) continue;
            words.add(word);
        }
        return words;
    }

    public Cursor getAllData(String table){ // TODO: For Debug Clear
        if(!isInitialized) initialCountAndSum();
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + table, null);
    }

    // TODO: add Helper Functions for Spammers & Details Table

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + SpamWordsTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + HamWordsTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + SpammersTable.TABLE_NAME);
        onCreate(db);
    }
}
