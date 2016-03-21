package com.rtmap.bugtags;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by silver on 16-3-21.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Crash_db.db";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        // 设置SQLiteDatabase.CursorFactory 为null
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 初始化创建一个表
        db.execSQL("CREATE TABLE IF NOT EXISTS Crash"
                + "(cid INTEGER PRIMARY KEY AUTOINCREMENT, crash TEXT, app_id  varchar(30),date_time varchar(30), versionName  varchar(30)" +
                ", versionCode  varchar(30), model  varchar(30), sdk_int  varchar(30), app_release  varchar(30), product  varchar(30) ,exception_class  varchar(30),app_thread  varchar(30) )");
    }

    // 如果数据库版本改变，则会调用
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 新增一个字段
        db.execSQL("ALTER TABLE Crash ADD COLUMN other STRING");
    }
}
