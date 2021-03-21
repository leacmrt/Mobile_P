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
//classe utilisé pour la BDD interne à android studio : SQLITE

        public static final String DATABASE_NAME = "MyDBName.db3"; //le nom de notre BDD

        public static final String PROJET_TABLE_NAME = "Projet";
        public static final String PROJET_COLUMN_ID = "id";
        public static final String PROJET_COLUMN_NAME1 = "Name1";
        public static final String PROJET_COLUMN_NAME2 = "Name2";
        public static final String PROJET_COLUMN_SCORE = "Score";
        public static final String PROJET_COLUMN_STRENGTH = "Strength";
        public static final String PROJET_COLUMN_DATE = "Date";
        private HashMap hp;

        public DBHelper(Context context) {//constructeur ,

            super(context, DATABASE_NAME , null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // TODO Auto-generated method stub
            db.execSQL( //Créé notre table Projet avec les colonnes voulues
                    "create table Projet " +
                            "(id integer primary key, name1 text,name2 text,score1 text,score2 text ,technique text, strength text,date text" +
                            ",critique text,localisation text,photo text)"
            );
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS Projet");
            onCreate(db);
        }

        public boolean insertmatch (String name1, String name2, String score, String strength, String date1,
                                    String toString4, String returnString, Bitmap photo,int sek1,int sek2) {
            //Fonction pour inserer un match

            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();

            byte[] bArray;//nous allons convertir l'image du match en BLOB pour pouvoir l'insérer dans notre BDD
            Bitmap image1 = photo;

            if(image1!=null) //si une photo a été prise (car pas obligatoir)
            {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                image1.compress(Bitmap.CompressFormat.PNG, 100, bos);//compression de la bitmap en ByteArrayOutputStream
                bArray = bos.toByteArray();//mis en forme de tableau de byte

            }else  bArray = null;

                if(numberOfRows()>5)//si le nombre de matchs est supérieure à 5
                {
                    deleteMatch(getfiMatch());//supprimer le match le moin récent
                }

            contentValues.put("name1", name1);//insertion des valeurs pour chaques colonnes
            contentValues.put("name2", name2);
            contentValues.put("score1", sek1);
            contentValues.put("score2", sek2);
            contentValues.put("technique", score);
            contentValues.put("strength", strength);
            contentValues.put("date", date1);
            contentValues.put("critique", toString4);
            contentValues.put("localisation",returnString);
            contentValues.put("photo",bArray);

            db.insert("Projet", null, contentValues);//Dans la table projet
            return true;
        }

        public Cursor getData(int id) {//pas utilisé
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor res =  db.rawQuery( "select * from Projet where id="+id+"", null );
            return res;
        }

        public int numberOfRows(){//recupère le nombre de d'entrée dans la table Projet
            SQLiteDatabase db = this.getReadableDatabase();
            int numRows = (int) DatabaseUtils.queryNumEntries(db, PROJET_TABLE_NAME);
            return numRows;
        }

        public boolean updateMatch (int id ,String name1, String name2, String score, String strength,String date) //fonction update pas utilisée
        {
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

        public Integer deleteMatch (Integer id) //supprimer un match à partir de son ID
        {
            SQLiteDatabase db = this.getWritableDatabase();
            return db.delete("Projet",
                    "id = ? ",
                    new String[] { Integer.toString(id) });
        }

        public ArrayList<String> getAllMatch() //recupère tout les matchs : pas utilisé
        {
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

    public int getfiMatch() //Recupère l'id du match le moin récent
    {
        ArrayList<Integer> array_list = new ArrayList<>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select id from Projet", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            //rempli une liste de tout les id des matchs presents dans la table PROJET
            array_list.add(res.getInt(res.getColumnIndex(PROJET_COLUMN_ID)));
            res.moveToNext();
        }
        int idtp=array_list.get(0); //le premier de la liste est le moin récent
        return idtp;
    }


}
