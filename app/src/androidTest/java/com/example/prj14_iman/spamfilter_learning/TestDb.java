package com.example.prj14_iman.spamfilter_learning;

import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import com.example.prj14_iman.spamfilter_learning.Data.LearningDbHelper;

/**
 * Created by Prj14_IMAN on 8/8/2015.
 */
public class TestDb extends AndroidTestCase {
    public static final String LOG_TAG = TestDb.class.getSimpleName();

    public void testCreateDb() throws Throwable {
        mContext.deleteDatabase(LearningDbHelper.DATABASE_NAME);
        SQLiteDatabase db = new LearningDbHelper(this.mContext).getWritableDatabase();
        assertEquals("DataBase Does Not Opened!", true, db.isOpen());
        db.close();
    }

    public void testInsertReadDb(){
        String  testWord = "IMAN";
        Integer testFreq = 10;

    }
}
