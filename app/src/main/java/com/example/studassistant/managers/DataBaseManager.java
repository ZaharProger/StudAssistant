package com.example.studassistant.managers;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.studassistant.constants.DataBaseValues;
import com.example.studassistant.entities.LikedListElement;

import java.util.ArrayList;

public class DataBaseManager extends SQLiteOpenHelper {

    private static DataBaseManager dataBaseManager;
    private ArrayList<LikedListElement> data;

    private DataBaseManager(Context context) {
        super(context, DataBaseValues.DATABASE_NAME, null, DataBaseValues.SCHEMA);
        data = new ArrayList<>();
    }

    public static synchronized DataBaseManager create(Context context){
        if (dataBaseManager == null)
            dataBaseManager = new DataBaseManager(context);

        return dataBaseManager;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DataBaseValues.CREATE_TABLE_TEMPLATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if (newVersion == oldVersion + 1){
            oldVersion = newVersion;
            sqLiteDatabase.execSQL(DataBaseValues.DELETE_TABLE_TEMPLATE);
            onCreate(sqLiteDatabase);
        }
    }

    public boolean add(long id, String data){
        boolean result;

        try{
            SQLiteDatabase sqLiteDatabase = getWritableDatabase();
            sqLiteDatabase.execSQL(String.format("INSERT INTO %s(%s, %s) VALUES(%d, '%s');", DataBaseValues.TABLE_NAME, DataBaseValues.ID_COLUMN, DataBaseValues.TUTOR_PERSONAL_COLUMN, id, data));
            result = true;
        }
        catch (SQLException exception){
            result = false;
        }

        return result;
    }

    public boolean remove(int id){
        boolean result;

        try{
            SQLiteDatabase sqLiteDatabase = getWritableDatabase();
            sqLiteDatabase.execSQL(String.format("DELETE FROM %s WHERE %s = '%d';", DataBaseValues.TABLE_NAME, DataBaseValues.ID_COLUMN, id));
            result = true;
        }
        catch (SQLException exception){
            result = false;
        }

        return result;
    }

    public LikedListElement getData(String data){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(String.format("SELECT * FROM %s WHERE %s = '%s';", DataBaseValues.TABLE_NAME,
                                                    DataBaseValues.TUTOR_PERSONAL_COLUMN, data), null);
        LikedListElement foundData = null;

        if (cursor != null){
            if (cursor.moveToFirst())
                do {
                    int idColumnIndex = cursor.getColumnIndex(DataBaseValues.ID_COLUMN);
                    int personalColumnIndex = cursor.getColumnIndex(DataBaseValues.TUTOR_PERSONAL_COLUMN);

                    foundData = new LikedListElement(cursor.getInt(idColumnIndex), cursor.getString(personalColumnIndex), null, null);
                } while (cursor.moveToNext());

            cursor.close();
        }

        return foundData;
    }

    public ArrayList<LikedListElement> getAllData(){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery(String.format("SELECT * FROM %s;", DataBaseValues.TABLE_NAME), null);
        data.clear();
        if (cursor != null){
            if (cursor.moveToFirst())
                do {
                    int idColumnIndex = cursor.getColumnIndex(DataBaseValues.ID_COLUMN);
                    int personalColumnIndex = cursor.getColumnIndex(DataBaseValues.TUTOR_PERSONAL_COLUMN);


                    LikedListElement likedListElement = new LikedListElement();
                    likedListElement.setId(cursor.getInt(idColumnIndex));
                    likedListElement.setPersonal(cursor.getString(personalColumnIndex));

                    data.add(likedListElement);
                } while (cursor.moveToNext());

            cursor.close();
        }

        return data;
    }
}
