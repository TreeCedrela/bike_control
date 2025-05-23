package com.example.autobike.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.autobike.Util;
import com.example.autobike.entity.SportRecord;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class RecordDBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "user.db1";
    private static final String TABLE_NAME = "sport_record";
    private static final int DB_VERSION = 1;
    private static RecordDBHelper mHelper = null;
    private SQLiteDatabase mRDB = null;
    private SQLiteDatabase mWDB = null;

    private RecordDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public static RecordDBHelper getInstance(Context context) {
        if (mHelper == null) {
            mHelper = new RecordDBHelper(context);
        }
        return mHelper;
    }

    //数据库读连接
    public SQLiteDatabase OpenRead() {
        if (mRDB == null || !mRDB.isOpen()) {
            mRDB = mHelper.getReadableDatabase();
        }
        return mRDB;
    }

    //数据库写连接
    public SQLiteDatabase OpenWrite() {
        if (mWDB == null || !mWDB.isOpen()) {
            mWDB = mHelper.getWritableDatabase();
        }
        return mWDB;
    }

    //数据库连接关闭
    public void closeLink() {
        if (mRDB != null && mRDB.isOpen()) {
            mRDB.close();
            mRDB = null;
        }

        if (mWDB != null && mWDB.isOpen()) {
            mWDB.close();
            mWDB = null;
        }
    }

    //创建数据库表
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.d("Database", "onCreate called");
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
                " id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                Util.DistanceSum + " VARCHAR NOT NULL," +
                Util.AverageSpeed + " VARCHAR NOT NULL," +
                Util.ElapsedTime + " VARCHAR NOT NULL," +
                Util.Altitude + " VARCHAR NOT NULL," +
                Util.TimeDate + " VARCHAR NOT NULL," +
                Util.Status + " INTEGER NOT NULL," +
                Util.ImageURI + " VARCHAR NOT NULL);";
        sqLiteDatabase.execSQL(sql);
        Log.d("Database", "Table created: " + TABLE_NAME);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME); // 删除旧表
        onCreate(db); // 重新创建表
    }


    //insert sport record
    public long InsertRecord(SportRecord record) {
        onCreate(mWDB);
        ContentValues values = new ContentValues();
        values.put(Util.DistanceSum, record.getDistanceSum());
        values.put(Util.AverageSpeed, record.getAverageSpeed());
        values.put(Util.ElapsedTime, record.getElapsedTime());
        values.put(Util.Altitude, record.getAltitude());
        values.put(Util.TimeDate, record.getTimeDate().toString());
        values.put(Util.ImageURI, record.getImageUrl());
        return mWDB.insert(TABLE_NAME, null, values);
    }

    // query records list by a specific month
    public List<SportRecord> QueryRecords() {

        List<SportRecord> recordList=new ArrayList<>();
        //TODO impl it by database sql
//        String mo = month.toString();

        Cursor cursor = mRDB.rawQuery("SELECT * FROM sport_record ",new String[]{});
        if(cursor.isBeforeFirst()){
            return recordList;
        }

        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy_MM_dd");


//        recordList.add(new SportRecord("/2",30.2f,2f,200f,20f,LocalDate.of(2002,2,2),0));
//        recordList.add(new SportRecord("/3",30.3f,3f,300f,30f,LocalDate.of(2003,3,3),1));*//*
//        recordList.add(new SportRecord("/1",30.1f,1f,100f,10f,LocalDate.of(2001,1,1),0));
        do{
            recordList.add(new SportRecord(cursor.getString(7),
                    Float.parseFloat(cursor.getString(1)),
                    Float.parseFloat(cursor.getString(2)),
                    Float.parseFloat(cursor.getString(3)),
                    Float.parseFloat(cursor.getString(4)),
                    LocalDate.parse(cursor.getString(5),df),
                    cursor.getInt(6)));
        }while (cursor.moveToNext());

        return recordList;
    }

    // query month list that records have
    public List<Integer> QueryMonthList(Integer id) {
        //TODO impl it by database sql
        Cursor cursor = mRDB.rawQuery("SELECT DISTINCT strftime('%m', TimeDate) FROM sport_record",null);
        List<Integer> monthList=new ArrayList<>();
if(cursor.isBeforeFirst()){return  monthList;}
        /*monthList.add(7);
        monthList.add(8);
        monthList.add(9);*/

        do{
            monthList.add(Integer.parseInt(cursor.getString(0)));
        }while (cursor.moveToNext());

        return monthList;
    }



    //update a sport record by id of param's record
    public Long UpdateRecord(SportRecord record) {
        //TODO impl it by database sql
        String ImageURI = null;

        ContentValues values = new ContentValues();
        values.put(Util.DistanceSum, record.getDistanceSum());
        values.put(Util.AverageSpeed, record.getAverageSpeed());
        values.put(Util.ElapsedTime, record.getElapsedTime());
        values.put(Util.Altitude, record.getAltitude());
        values.put(Util.TimeDate, record.getTimeDate().toString());
        values.put(Util.ImageURI, record.getImageUrl());

        Cursor cursor = mRDB.query(TABLE_NAME,null,"imageUri=?",
                new String[]{record.getTimeDate().toString()},null,null,null);

        while (cursor.moveToNext()){
            ImageURI = cursor.getString(5);
        }

        return (long) mWDB.update(TABLE_NAME, values, "imageUri=?", new String[]{ImageURI});
    }

}
