package com.mxi.goalkeeper.database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

/**
 * Created by parth on 17/5/16.
 */
public class SQLiteTD {

    Context mcon;
    private static String dbname = "Goalkeeper.db";

    static String db_path = Environment.getExternalStorageDirectory()
            .toString() + "/" + dbname;

    SQLiteDatabase db;
    public static final String KEY_ROWID = "id";

    public SQLiteTD(Context con) {
        // TODO Auto-generated constructor stub
        mcon = con;
        db = mcon.openOrCreateDatabase(db_path, Context.MODE_PRIVATE, null);

        // Database Table for store all Availabilities list
        db.execSQL("CREATE TABLE IF NOT EXISTS Availabilities(id INTEGER PRIMARY KEY AUTOINCREMENT,  "
                + " week_day VARCHAR, start_time VARCHAR,end_time VARCHAR,user_id VARCHAR); ");

    }

    public void nseartAvailabilities(String week_day, String start_time, String end_time,String user_id) {


        String query = "INSERT INTO Availabilities(week_day,start_time,end_time,user_id)VALUES ('"

                + week_day + "','"
                + start_time + "','"
                + end_time + "','"
                + user_id + "')";

        try {
            db.execSQL(query);
        } catch (SQLException e) {
            Log.e("Error Availabilities", e.getMessage());
        }
    }

    public Cursor getAvailabilities(String user_id) {
        Cursor cur = null;
        try {
            String query = "SELECT  * FROM  Availabilities where user_id = '"+user_id+"'";
            cur = db.rawQuery(query, null);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e("Error:getAvailability ", e.getMessage());
        }

        return cur;
    }

   /* public void inseartORreplace(String week_day, String start_time, String end_time) {


        String query = "INSERT OR REPLACE INTO Availabilities(week_day,start_time,end_time)VALUES ('"

                + week_day + "','"
                + start_time + "','"
                + end_time + "')";

        try {
            db.execSQL(query);
        } catch (SQLException e) {
            Log.e("Error Availabilities", e.getMessage());
        }
    }
*/
    public void deleteTable() {

        try {

            db.execSQL("delete from Availabilities");

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            Log.e("Error : Delete table", e.getMessage());
        }

    }

}
