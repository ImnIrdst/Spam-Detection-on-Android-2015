package com.example.prj14_iman.spamfilter_learning;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.prj14_iman.spamfilter_learning.Data.LearningDataSource;
import com.example.prj14_iman.spamfilter_learning.Data.LearningDbHelper;


public class MainActivity extends ActionBarActivity {
    EditText mainEditText;
    TextView mainTextView;
    Button   markAsSpamButton;
    Button   markAsHamButton;
    Button   getSpamsButton;
    Button   getHamsButton;
    Button   isSpamButton;
    LearningDataSource learningDataSource;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainEditText        = (EditText) findViewById(R.id.mainEditText);
        mainTextView        = (TextView) findViewById(R.id.mainTextView);
        markAsSpamButton    = (Button) findViewById(R.id.markAsSpamButton);
        markAsHamButton     = (Button) findViewById(R.id.markAsHamButton);
        getSpamsButton      = (Button) findViewById(R.id.getSpamsButton);
        getHamsButton       = (Button) findViewById(R.id.getHamsButton);
        isSpamButton   = (Button) findViewById(R.id.isSpamButton);
        learningDataSource = new LearningDataSource(this);
        learningDataSource.open();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onMarkAsSpamButtonClicked(View view) {
        learningDataSource.setMessageAsSpam(mainEditText.getText().toString());
    }

    public void onGetSpamsButtonClicked(View view) { // TODO: For Debug Clear
        mainTextView.setText("");
        LearningDbHelper db = new LearningDbHelper(this);
        Cursor cursor = learningDataSource.getAllData("SpamWords");
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            String  queryWord = cursor.getString(cursor.getColumnIndex("Word"));
            Integer queryFreq = cursor.getInt(cursor.getColumnIndex("Freq"));
            mainTextView.append(queryWord + ": \t" + queryFreq + "\n");
            cursor.moveToNext();
        }
        learningDataSource.close();
    }

    public void onMarkAsHamButtonClicked(View view) {
        learningDataSource.setMessageAsHam(mainEditText.getText().toString());
    }

    public void onGetHamsButtonClicked(View view) { // TODO: For Debug Clear
        mainTextView.setText("");
        Cursor cursor = learningDataSource.getAllData("HamWords");
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            String  queryWord = cursor.getString(cursor.getColumnIndex("Word"));
            Integer queryFreq = cursor.getInt(cursor.getColumnIndex("Freq"));
            mainTextView.append(queryWord + ": \t" + queryFreq + "\n");
            cursor.moveToNext();
        }
        learningDataSource.close();
    }

    public void onIsSpamClicked(View view) {
        boolean isSpam = learningDataSource.isSpam(mainEditText.getText().toString());
        if(isSpam) mainTextView.setText("This Is a Spam");
        if(!isSpam) mainTextView.setText("This Isn't a Spam");
    }
}
