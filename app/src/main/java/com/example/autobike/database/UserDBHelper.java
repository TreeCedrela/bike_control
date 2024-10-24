package com.example.autobike.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.autobike.enity.user;

public class UserDBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "user.db1";
    private static final String TABLE_NAME = "user_info1";
    private static final int DB_VERSION = 1;
    private static UserDBHelper mHelper = null;
    private SQLiteDatabase mRDB = null;
    private SQLiteDatabase mWDB = null;

    private UserDBHelper(Context context){
        super(context,DB_NAME,null, DB_VERSION);
    }

    public static UserDBHelper getInstance(Context context){
        if(mHelper == null){
            mHelper = new UserDBHelper(context);
        }
        return mHelper;
    }

    //数据库读连接
    public SQLiteDatabase OpenRead(){
        if(mRDB == null || !mRDB.isOpen()){
            mRDB = mHelper.getReadableDatabase();
        }
        return mRDB;
    }

    //数据库写连接
    public SQLiteDatabase OpenWrite(){
        if(mWDB == null || !mWDB.isOpen()){
            mWDB = mHelper.getWritableDatabase();
        }
        return mWDB;
    }

    //数据库连接关闭
    public void closeLink(){
        if(mRDB != null && mRDB.isOpen()){
            mRDB.close();
            mRDB = null;
        }

        if(mWDB != null && mWDB.isOpen()){
            mWDB.close();
            mWDB = null;
        }
    }

    //创建数据库表
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
                " _id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                " name VARCHAR NOT NULL," +
                " password VARCHAR NOT NULL," +
                " e_mail VARCHAR NOT NULL," +
                " remember INTEGER NOT NULL);" ;
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    //数据库添加
    public long insert(user user){
        ContentValues values = new ContentValues();
        values.put("name",user.name);
        values.put("password",user.password);
        values.put("e_mail",user.e_mail);
        values.put("remember",user.remember);
        return mWDB.insert(TABLE_NAME,null,values);
    }

    public String QueryPassword(String name){
        Cursor cursor = mRDB.query(TABLE_NAME,null,"name=?",new String[]{name},null,null,null);
        String password = null;

        while (cursor.moveToNext()){
            password = cursor.getString(2);
        }
        
        return password;
    }

    public int QueryRemember(){
        Cursor cursor = mRDB.query(TABLE_NAME,null,"remember=?",new String[]{"1"},null,null,null);
        int Remember = 0;

        while (cursor.moveToNext()){
            Remember = cursor.getInt(4);
        }

        return Remember;
    }

    public void remember(String name){
        Cursor cursor = mRDB.query(TABLE_NAME,null,"name=?",new String[]{name},null,null,null);
        ContentValues values = new ContentValues();
        String password = null;
        String remember = null;
        while (cursor.moveToNext()){
            password = cursor.getString(2);
            remember = cursor.getString(3);
        }
        values.put("name",name);
        values.put("password",password);
        values.put("e_mail",remember);
        values.put("remember",1);
        mRDB.update(TABLE_NAME,values,"name=?",new String[]{name});
    }
}
