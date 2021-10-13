package com.example.steelix;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class dataBase extends SQLiteOpenHelper {//******empty name, special character name of database exception

    public static final String COLUMN_DIA = "DIAMETER";
    public static final String COLUMN_BUNDLE = "BUNDLES";
    public static final String COLUMN_KG = "KGs";
    public static final String COLUMN_ROD = "RODS";

    String dia[] = {"8mm", "10mm", "12mm", "16mm", "20mm", "25mm", "32mm"};
    String nam = "Default_Preset";
    Context con;
    int[] kg = new int[]{47,53,53,56,59,46,76};
    int[] rod = new int[]{10,7,5,3,2,1,1};



    public dataBase(Context context, String name) {
        super(context, "Companies.db", null, 1);
        nam = name.replace(" ","_");
        con = context;
    }

    public dataBase(Context context) {
        super(context, "Companies.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + nam + "(" + COLUMN_DIA + " TEXT, " + COLUMN_BUNDLE + " INTEGER, " + COLUMN_KG + " INTEGER, " + COLUMN_ROD + " INTEGER " + ")";
        db.execSQL(createTable);


    }


    public int doesTableExist(String name)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT COUNT(*) FROM " + name + "", null);
        cur.moveToFirst();
        int count = cur.getInt(0);
        return count;

    }


    public void Default_Preset()
    {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        for(int i=0;i<7;i++)
        {
            cv.put(COLUMN_DIA,dia[i]);

            cv.put(COLUMN_BUNDLE,1);
            cv.put(COLUMN_KG,kg[i]);
            cv.put(COLUMN_ROD,rod[i]);
            db.insert("Default_Preset", null, cv);
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public ArrayList<String> getCompanyNames(SQLiteDatabase db) {
        ArrayList<String> arrTblNames = new ArrayList<String>();
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        if (c.moveToFirst()) {
            while ( !c.isAfterLast() ) {
                arrTblNames.add( c.getString( c.getColumnIndex("name")) );
                c.moveToNext();
            }
        }

        arrTblNames = new ArrayList<String>(arrTblNames.subList(1, arrTblNames.size()));

        return arrTblNames;

    }

    public boolean delete(String dbname) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS '" + dbname + "'");
        return true;
    }


    public boolean addItem( TextView[] c, TextView[] d) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        long insert=0, check = 0;

        for(int i=0;i<7;i++)
        {
            cv.put(COLUMN_DIA,dia[i]);
            try {
                cv.put(COLUMN_BUNDLE,1);
                cv.put(COLUMN_KG,Integer.parseInt(c[i].getText().toString()));
                cv.put(COLUMN_ROD,Integer.parseInt(d[i].getText().toString()));
            }
            catch (Exception e)
            {
                check = 0;
            }
            insert = db.insert(nam, null, cv);

        }


        if (insert == -1|| check == 0) {
            return false;
        }
        else {
            return true;

        }


    }
    public List<Integer> getValues(String column) {

        List <Integer> returndata = new ArrayList<>();
        String selectQuery = "SELECT " + column + " FROM " + nam;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);



        // looping through all rows and adding to array
        if (cursor.moveToFirst()) {
            do {
                returndata.add(cursor.getInt(cursor.getColumnIndexOrThrow(column)));

            } while (cursor.moveToNext());
        }

        return returndata;

    }

}

