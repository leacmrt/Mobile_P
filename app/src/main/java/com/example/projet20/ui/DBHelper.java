package com.example.projet20.ui;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;


import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


public class DBHelper extends SQLiteOpenHelper {

        public static final String DATABASE_NAME = "MyDBName.db2";
        public static final String PROJET_TABLE_NAME = "Projet";
        public static final String PROJET_COLUMN_ID = "id";
        public static final String PROJET_COLUMN_NAME1 = "Name1";
        public static final String PROJET_COLUMN_NAME2 = "Name2";
        public static final String PROJET_COLUMN_SCORE = "Score";
        public static final String PROJET_COLUMN_STRENGTH = "Strength";
        public static final String PROJET_COLUMN_DATE = "Date";
        private HashMap hp;

        public DBHelper(Context context) {
            super(context, DATABASE_NAME , null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // TODO Auto-generated method stub
            db.execSQL(
                    "create table Projet " +
                            "(id integer primary key, name1 text,name2 text,score text, strength text,date text,critique text,localisation text,photo text)"
            );
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS Projet");
            onCreate(db);
        }

        public boolean insertmatch (String name1, String name2, String score, String strength, String date1, String toString4, String returnString, Bitmap photo) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();

            Bitmap image1 = photo;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            image1.compress(Bitmap.CompressFormat.PNG, 100, bos);
            byte[] bArray = bos.toByteArray();

            contentValues.put("name1", name1);
            contentValues.put("name2", name2);
            contentValues.put("score", score);
            contentValues.put("strength", strength);
            contentValues.put("date", date1);
            contentValues.put("critique", toString4);
            contentValues.put("localisation",returnString);
            contentValues.put("photo",bArray);

            db.insert("Projet", null, contentValues);
            return true;
        }

        public Cursor getData(int id) {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor res =  db.rawQuery( "select * from Projet where id="+id+"", null );
            return res;
        }

        public int numberOfRows(){
            SQLiteDatabase db = this.getReadableDatabase();
            int numRows = (int) DatabaseUtils.queryNumEntries(db, PROJET_TABLE_NAME);
            return numRows;
        }

        public boolean updateMatch (int id ,String name1, String name2, String score, String strength,String date) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("name", name1);
            contentValues.put("name2", name2);
            contentValues.put("score", score);
            contentValues.put("strength", strength);
            contentValues.put("date", date);
            db.update("Projet", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
            return true;
        }

        public Integer deleteMatch (Integer id) {
            SQLiteDatabase db = this.getWritableDatabase();
            return db.delete("Projet",
                    "id = ? ",
                    new String[] { Integer.toString(id) });
        }

        public ArrayList<String> getAllMatch() {
            ArrayList<String> array_list = new ArrayList<String>();

            //hp = new HashMap();
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor res =  db.rawQuery( "select * from Projet", null );
            res.moveToFirst();

            while(res.isAfterLast() == false){
                array_list.add(res.getString(res.getColumnIndex(PROJET_COLUMN_NAME1)));
                res.moveToNext();
            }
            return array_list;
        }


}
