package com.example.prj14_iman.spamfilter_learning.Data;

import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.TreeSet;

/**
 * Created by Prj14_IMAN on 8/8/2015.
 */
public class LearningDbContract {
    public static final class SpamWordsTable implements BaseColumns{
        public static final String TABLE_NAME = "SpamWords";
        public static final String COL_WORD = "Word";       // STRING
        public static final String COL_FREQ = "Freq";       // INTEGER
        public static final String ROW_SUM_ALL = "__CMD_SumALLWords__";
        public static final String ROW_COUNT_ALL = "__CMD_CountALLWords__";
        public static final String ROW_COUNT_ALL_DOCS = "__CMD_CountALLDocs__";
    }

    public static final class HamWordsTable implements BaseColumns{
        public static final String TABLE_NAME = "HamWords";
        public static final String COL_WORD = "Word";       // STRING
        public static final String COL_FREQ = "Freq";       // INTEGER
        public static final String ROW_SUM_ALL = "__CMD_SumALLWords__";
        public static final String ROW_COUNT_ALL = "__CMD_CountALLWords__";
        public static final String ROW_COUNT_ALL_DOCS = "__CMD_CountALLDocs__";
    }

    public static final class SpammersTable implements BaseColumns{
        public static final String TABLE_NAME = "Spammers";
        public static final String COL_NAME = "Name";       // STRING
        public static final String COL_NUMBER = "Number";       // INTEGER

    }

    // TODO: Use Spam Message Details (Contact Notes)
}
