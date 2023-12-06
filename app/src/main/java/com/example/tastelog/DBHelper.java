package com.example.tastelog;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public static  final int DATABASE_VERSION = 1;
    public DBHelper(Context context) {
        super(context, "favorite_db", null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String favoriteSQL = "create table tb_favorite " +
                "(_id integer primary key autoincrement," +
                "category," +
                "comment," +
                "image)";
        db.execSQL(favoriteSQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        if(newV == DATABASE_VERSION) {
            db.execSQL("drop table tb_favorite");
            onCreate(db);
        }
    }
}
