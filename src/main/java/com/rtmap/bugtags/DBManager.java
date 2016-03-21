package com.rtmap.bugtags;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by silver on 16-3-21.
 */
public class DBManager {


    private static final String INFO = DBManager.class.toString();
    private DBHelper helper;
    private SQLiteDatabase db;

    public DBManager(Context context) {
        helper = new DBHelper(context);
        db = helper.getWritableDatabase();
        // helper.getReadableDatabase();
    }

    public void add(Map<String, String> crashMap) {
        db.beginTransaction();

        try {
            StringBuilder stringBuilder = new StringBuilder("INSERT INTO Crash (");
            for (Map.Entry<String, String> entry : crashMap.entrySet()) {

                if ("product".equals(entry.getKey())) {
                    stringBuilder.append(entry.getKey() + ") VALUES(");
                } else {
                    stringBuilder.append(entry.getKey() + ",");
                }
            }

            for (Map.Entry<String, String> entry : crashMap.entrySet()) {

                if ("product".equals(entry.getKey())) {
                    stringBuilder.append("'" + entry.getValue() + "')");
                } else {
                    stringBuilder.append("'" + entry.getValue() + "',");
                }
            }


            db.execSQL(stringBuilder.toString());
            // 设置事物
            db.setTransactionSuccessful();
        } finally {
            // 结束事物
            db.endTransaction();
        }
    }


    public void delete(String id) {
        db.delete("Crash", "cid = ?", new String[]{id});// String.valueOf(Crash.age);
    }

    public List<Map> getCrashList() {
        List<Map> mapList = new ArrayList<Map>();

        // 使用rawQuery() 方法，返回游标对象，遍历出数据库的数据
        Cursor cursor = db.rawQuery("SELECT * FROM Crash", null);
        while (cursor.moveToNext()) {
            Map map = new LinkedHashMap();
            String id = cursor.getString(cursor.getColumnIndex("cid"));
            String crash = cursor.getString(cursor.getColumnIndex("crash"));
            String dete_time = cursor.getString(cursor.getColumnIndex("date_time"));
            String sdk_int = cursor.getString(cursor.getColumnIndex("sdk_int"));
            String app_release = cursor.getString(cursor.getColumnIndex("app_release"));
            String product = cursor.getString(cursor.getColumnIndex("product"));
            String versionName = cursor.getString(cursor.getColumnIndex("versionName"));
            String model = cursor.getString(cursor.getColumnIndex("model"));
            String versionCode = cursor.getString(cursor.getColumnIndex("versionCode"));
            String app_id = cursor.getString(cursor.getColumnIndex("app_id"));
            String exception_class = cursor.getString(cursor.getColumnIndex("exception_class"));
            String app_thread = cursor.getString(cursor.getColumnIndex("app_thread"));

            map.put("crash", crash);
            map.put("date_time", dete_time);
            map.put("cid", id);
            map.put("sdk_int", sdk_int);
            map.put("app_release", app_release);
            map.put("product", product);
            map.put("versionName", versionName);
            map.put("model", model);
            map.put("versionCode", versionCode);
            map.put("app_id", app_id);
            map.put("exception_class", exception_class);
            map.put("app_thread", app_thread);
            mapList.add(map);
        }
        return mapList;
    }

    // 关闭数据库
    public void closeDB() {
        db.close();
    }
}
