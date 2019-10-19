package com.example.cctv;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "WriterID.db";
    public static final String TABLE_NAME = "writer";
    public static final String COLUMN_ID = "id";
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE writer" + "(id text primary key)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS writer");
        onCreate(db);
    }

    public Cursor getData(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from writer where id=" + id + "", null);
        return res;
    }

    public void insert(String id) {
        // 읽고 쓰기가 가능하게 DB 열기
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("INSERT INTO writer (id) VALUES ('"+id+"')");
    }

    public void update(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        // 입력한 항목과 일치하는 행의 가격 정보 수정
        ContentValues contentValues = new ContentValues();
        contentValues.put("id",id);
        db.update("writer",contentValues,"id = ? ",new String[]{id});
    }

    public void delete(String id) {
        SQLiteDatabase db = getWritableDatabase();

        db.execSQL("DELETE FROM writer WHERE id= \"" + id + "\"");
    }

    public ArrayList getResult() {
        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = getReadableDatabase();
        ArrayList arrayList = new ArrayList();
        Cursor res = db.rawQuery("select * from writer", null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            arrayList.add(res.getString(res.getColumnIndex(COLUMN_ID)));
            res.moveToNext();
        }
        return arrayList;
    }
}
