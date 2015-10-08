package com.syt.balram.syt;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.loopj.android.image.SmartImage;
import com.syt.balram.syt.DB.DBOperation;

public class MyChat extends AppCompatActivity {
    SmartImage imgPic;
    TextView tvName,tvDate;
    DBOperation db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_chat);

        tvName= (TextView) findViewById(R.id.my_chat_name);
        tvDate= (TextView) findViewById(R.id.my_chat_date);

        db=new DBOperation(this);
        db.createAndInitializeTables();

       Cursor cursor= db.getChatData();


        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                System.out.print(cursor.getString(0)+","+cursor.getString(1)+","+cursor.getString(2)+","+cursor.getString(3)+","+cursor.getString(4));
            } while (cursor.moveToNext());
        }
        cursor.close();

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
}
