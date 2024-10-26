package com.example.autobike.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.autobike.Util;
import com.example.autobike.entity.SportRecord;

import java.time.LocalDate;
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
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
                " id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                Util.DistanceSum + "  VARCHAR NOT NULL," +
                Util.AverageSpeed+" VARCHAR NOT NULL," +
                Util.ElapsedTime+" VARCHAR NOT NULL," +
                Util.Altitude+" VARCHAR NOT NULL," +
                Util.TimeDate + " VARCHAR NOT NULL," +
                Util.Status + " INTEGER NOT NULL," +
                Util.ImageURI + " VARCHAR NOT NULL);";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    //insert sport record
    public long InsertRecord(SportRecord record) {
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
    public List<SportRecord> QueryRecordsByMonth(Integer month) {
        //TODO impl it by database sql

        List<SportRecord> recordList=new ArrayList<>();
        recordList.add(new SportRecord("/1",30.1f,1f,100f,10f,LocalDate.of(2001,1,1),0));
        recordList.add(new SportRecord("/2",30.2f,2f,200f,20f,LocalDate.of(2002,2,2),0));
        recordList.add(new SportRecord("/3",30.3f,3f,300f,30f,LocalDate.of(2003,3,3),1));

        return recordList;
    }

    // query month list that records have
    public List<Integer> QueryMonthList(Integer id) {
        //TODO impl it by database sql

        List<Integer> monthList=new ArrayList<>();
        monthList.add(7);
        monthList.add(8);
        monthList.add(9);

        return monthList;
    }



    //update a sport record by id of param's record
    public Long UpdateRecord(SportRecord record) {
        //TODO impl it by database sql

        return 0L;
    }

}
