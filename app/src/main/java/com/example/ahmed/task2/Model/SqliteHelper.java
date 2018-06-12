package com.example.ahmed.task2.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class SqliteHelper extends SQLiteOpenHelper {

    String table_name = "repo";

    public SqliteHelper(Context context) {
        super(context, "myDatabase", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table if not exists " + table_name + "(id integer primary key autoincrement, repoName varchar, description varchar,userName varchar, fork boolean)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists " + table_name);
        onCreate(sqLiteDatabase);
    }

    public void addRepo(String repoName, String description, String userName, boolean fork) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        // insert data to table
        contentValues.put("repoName", repoName);
        contentValues.put("description", description);
        contentValues.put("userName", userName);
        contentValues.put("fork", fork);
        database.insert(table_name, null, contentValues);
    }

    // Retrieve all data from table
    public ArrayList show_all_Repos() {
        ArrayList arr = new ArrayList();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cu = db.rawQuery("select * from " + table_name, null);
        cu.moveToFirst();
        while (cu.isAfterLast() == false) {
            String repoName = cu.getString(cu.getColumnIndex("repoName"));
            String description = cu.getString(cu.getColumnIndex("description"));
            String userName = cu.getString(cu.getColumnIndex("userName"));
            boolean fork = cu.getInt(4) > 0;

            arr.add(new ListItem(repoName, description, userName, fork));
            cu.moveToNext();
        }
        return arr;
    }

    // delete table
    public void deleteTable() {
        SQLiteDatabase database = getWritableDatabase();
        database.delete(table_name, null, null);
    }
}