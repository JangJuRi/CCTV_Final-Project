package com.example.cctv;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class goodHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "GoodNum.db";
    public static final String TABLE_NAME = "good";
    public static final String COLUMN_ID = "num";
    public goodHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE good" + "(num text primary key)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS good");
        onCreate(db);
    }

    public ArrayList getData(String num) {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList arrayList = new ArrayList();
        Cursor res = db.rawQuery("select * from good where num=" + num + "", null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            arrayList.add(res.getString(res.getColumnIndex(COLUMN_ID)));
            res.moveToNext();
        }
        return arrayList;
    }

    public void insert(String num) {
        // 읽고 쓰기가 가능하게 DB 열기
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("INSERT INTO good (num) VALUES ('"+num+"')");
    }

    public void update(String num) {
        SQLiteDatabase db = this.getWritableDatabase();
        // 입력한 항목과 일치하는 행의 가격 정보 수정
        ContentValues contentValues = new ContentValues();
        contentValues.put("num",num);
        db.update("good",contentValues,"num = ? ",new String[]{num});
    }

    public void delete(String num) {
        SQLiteDatabase db = getWritableDatabase();

        db.execSQL("DELETE FROM good WHERE num= \"" + num + "\"");
    }

    public ArrayList getResult() {
        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = getReadableDatabase();
        ArrayList arrayList = new ArrayList();
        Cursor res = db.rawQuery("select * from good", null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            arrayList.add(res.getString(res.getColumnIndex(COLUMN_ID)));
            res.moveToNext();
        }
        return arrayList;
    }
}