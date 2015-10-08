package com.syt.balram.syt.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.syt.balram.syt.BE.ChatPeopleBE;

import java.util.Map;
import java.util.Set;

/**
 * Created by Balram on 5/19/2015.
 */
public class DBOperation {

    String DB_TABLE;
    int DB_VERSION = 1;
    static String[] DATABASE_CREATE;
    private Context context;
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;
    public static String TAG = "GCM DB";


    public DBOperation(Context ctx) {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    public void createAndInitializeTables() {
        try {
            ChatPeopleBE mytable = new ChatPeopleBE();
            String[] tableCreateArray = { mytable.getDatabaseCreateQuery() };
            DBOperation operation = new DBOperation(context, tableCreateArray);
            operation.open();
            operation.close();
            Log.i(TAG, "DB Created");
        } catch (Exception e) {
            Log.d(TAG, "Error creating table " + e.getMessage());
        }
    }

    public DBOperation(Context ctx, String[] query) {
        this.context = ctx;
        DATABASE_CREATE = query;
        DBHelper = new DatabaseHelper(context);
    }


    public Cursor getTableRow(String tablename, String[] dbFields,
                              String condition, String order, String limit) throws SQLException {
        DB_TABLE = tablename;
        Cursor mCursor = db.query(false, DB_TABLE, dbFields, condition, null,
                null, null, order, limit);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }



    public Cursor getDataFromTable(String personID) {

        DBOperation operationObj = new DBOperation(context);
        operationObj.open();
        ChatPeopleBE peopleTable = new ChatPeopleBE();
        String condition = "";
        if (personID != null) {
            condition = peopleTable.getPERSON_ID() + " = '"
                    + personID + "'";
        }
        String[] dbFields = { peopleTable.getPERSON_ID(),
                peopleTable.getPERSON_CHAT_MESSAGE(),
                peopleTable.getPERSON_CHAT_TO_FROM(),
                peopleTable.getCHAT_DATE()};
        Cursor cursor = operationObj.getTableRow(peopleTable.getTableName(),
                dbFields, condition, peopleTable.getPERSON_ID() + " ASC ",
                null);
        operationObj.close();

        return cursor;

    }

    public Cursor getChatData() {

        DBOperation operationObj = new DBOperation(context);
        operationObj.open();
        ChatPeopleBE peopleTable = new ChatPeopleBE();

        String[] dbFields = { peopleTable.getPERSON_ID(),
                peopleTable.getPERSON_CHAT_MESSAGE(),
                peopleTable.getPERSON_CHAT_TO_FROM(),
                peopleTable.getCHAT_DATE()};
        DB_TABLE=peopleTable.getTableName();
        Cursor cursor=db.query(true, DB_TABLE, dbFields, null, null, null, null, peopleTable.getPERSON_ID() + " ASC ",
                null);
        operationObj.close();

        return cursor;

    }


   /* public Cursor getData(){

        DBOperation operationObj = new DBOperation(context);
        operationObj.open();
        ChatPeopleBE peopleTable = new ChatPeopleBE();
        String condition = "";
            condition = peopleTable.getPERSON_ID();


        String[] dbFields = { peopleTable.getPERSON_ID()};
        Cursor res=operationObj.getTableRow(peopleTable.getTableName(),
                dbFields, condition, peopleTable.getPERSON_ID() + " ASC ",
                null);
      //  Cursor res = db.rawQuery("select DISTINCT device_id from people_table ", null );
        operationObj.close();
        return res;
    }*/

    public long insertTableData(String tablename, ContentValues values)
            throws SQLException {
        DB_TABLE = tablename;
        ContentValues contentValues = new ContentValues();
        Set<Map.Entry<String, Object>> s = values.valueSet();
        String new_val = "";
        for (Map.Entry<String, Object> entry : s) {
            new_val = values.getAsString(entry.getKey());
            contentValues.put(entry.getKey(), new_val);
        }
        return db.insert(DB_TABLE, null, contentValues);
    }
    public DBOperation open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        DBHelper.close();
    }

    private class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, "mydb.db", null, DB_VERSION);
        }


        public void onCreate(SQLiteDatabase db) {
            try {
                for (String s : DATABASE_CREATE) {
                    db.execSQL(s);
                }
            } catch (Exception e) {
            }
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);
            onCreate(db);
        }
    }

}
