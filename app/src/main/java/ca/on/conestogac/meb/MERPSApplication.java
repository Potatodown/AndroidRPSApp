package ca.on.conestogac.meb;

import android.app.Application;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.Time;
import java.util.ArrayList;

public class MERPSApplication extends  Application{

    private static final String DB_NAME = "db_rps_stats";
    private static final int DB_VERSION = 1;
    private SQLiteOpenHelper helper;
    @Override
    public void onCreate(){

        helper =new SQLiteOpenHelper(this, DB_NAME, null, DB_VERSION){

            @Override
            public void onCreate(SQLiteDatabase sqLiteDatabase) {
                sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS tbl_stats(" +
                        "wins INTEGER, loses INTEGER, ties INTEGER, timestamp INTEGER)");
            }

            @Override
            public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

            }
        };

        super.onCreate();
    }

    public void addWin(int win, int time){
        SQLiteDatabase  db= helper.getWritableDatabase();
        db.execSQL("INSERT INTO tbl_stats (wins, timestamp) VALUES("+win+", "+time+")");
    }

    public int getWins(){
        SQLiteDatabase  db= helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(wins) AS wins FROM tbl_stats", null);
        int ret;

        cursor.moveToFirst();
        ret = cursor.getInt(0);
        cursor.close();

        return ret;
    }

    public void addTie(int tie, int time){
        SQLiteDatabase  db= helper.getWritableDatabase();
        db.execSQL("INSERT INTO tbl_stats (ties, timestamp) VALUES("+tie+", "+time+")");
    }

    public int getTies(){
        SQLiteDatabase  db= helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(ties) AS ties FROM tbl_stats", null);
        int ret;

        cursor.moveToFirst();
        ret = cursor.getInt(0);
        cursor.close();

        return ret;
    }

    public void addLose(int lose, int time){
        SQLiteDatabase  db= helper.getWritableDatabase();
        db.execSQL("INSERT INTO tbl_stats (loses, timestamp) VALUES("+lose+", "+time+")");
    }

    public int getLoses(){
        SQLiteDatabase  db= helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(loses) AS loses FROM tbl_stats", null);
        int ret;

        cursor.moveToFirst();
        ret = cursor.getInt(0);
        cursor.close();

        return ret;
    }

    public ArrayList getTimes(){
        SQLiteDatabase  db= helper.getReadableDatabase();
        int lastMinute = Math.round(System.currentTimeMillis()/1000);
        Cursor cursor = db.rawQuery("SELECT Sum(wins), Sum(loses), Sum(ties) FROM tbl_stats WHERE timestamp = "+lastMinute+"", null);
        ArrayList ret = new ArrayList();

        cursor.moveToFirst();
        ret.add(cursor.getInt(0));
        ret.add(cursor.getInt(1));
        ret.add(cursor.getInt(2));
        cursor.close();

        return ret;
    }
    public void resetTableStats(){
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("DELETE FROM tbl_stats");
    }
}
