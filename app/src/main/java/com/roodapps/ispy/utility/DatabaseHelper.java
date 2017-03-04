package com.roodapps.ispy.utility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.JsonReader;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.roodapps.ispy.litemodels.Photo;

public class DatabaseHelper extends SQLiteOpenHelper
{
    private Context mContext;

    SQLiteDatabase db;

    private static final String TAG = "DatabaseHelper";
    // Database version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "local.db";

    // Table Names
    private static final String TABLE_PHOTOS = "photos";
    private static final String TABLE_SAVE = "save";

    // Common column names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";

    // PHOTOS column names
    private static final String KEY_CLUE = "clue";
    private static final String KEY_ANSWER = "answer";

    // Create Table Statements
    // PHOTOS statement
    private static final String CREATE_TABLE_PHOTOS = "CREATE TABLE "
            + TABLE_PHOTOS + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
            + KEY_CLUE + " TEXT," + KEY_ANSWER + " TEXT" + ")";

    private static final String CREATE_TABLE_SAVE = "CREATE TABLE "
            + TABLE_SAVE + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT" + ")";

    public DatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mContext = context;
        Log.i(TAG, "testC");
        db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        // create tables
        db.execSQL(CREATE_TABLE_PHOTOS);
        db.execSQL(CREATE_TABLE_SAVE);
        Log.i(TAG, "testD");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PHOTOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SAVE);

        // create new tables
        onCreate(db);
    }

    /*
     * PHOTOS methods
     */

    public boolean insertPhoto(String name, String clue, String answer)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(KEY_NAME, name);
        contentValues.put(KEY_CLUE, clue);
        contentValues.put(KEY_ANSWER, answer);

        long result = db.insert(TABLE_PHOTOS, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    public Cursor findPhoto(String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_PHOTOS + " WHERE " + KEY_ID + " = " + id;
        return db.rawQuery(query, null);
    }

    public int getTableLength()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_PHOTOS
                + " ORDER BY " + KEY_ID + " DESC LIMIT 1";
        Cursor res = db.rawQuery(query, null);
        res.moveToNext();
        return res.getInt(0);
    }

    public Integer deletePhoto(String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_PHOTOS, KEY_ID + " = ?", new String[] { id });
    }

    /*
     * SAVE methods
     */

    public boolean newSave()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(KEY_NAME, "1");

        long result = db.insert(TABLE_SAVE, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    public boolean updateSave(String id, String name)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(KEY_NAME, name);

        db.update(TABLE_SAVE, contentValues, KEY_ID + " = ?", new String[] { id });
        return true;
    }

    public Cursor getSave()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_SAVE + " WHERE " + KEY_ID + " = 1";
        return db.rawQuery(query, null);
    }

    // --- JSON PARSING ---
    public List<Photo> readJsonStream(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        try {
            return readPhotoArray(reader);
        } finally {
            reader.close();
        }
    }

    public List<Photo> readPhotoArray(JsonReader reader) throws IOException {
        List<Photo> messages = new ArrayList<>();

        reader.beginArray();
        while (reader.hasNext()) {
            messages.add(readPhoto(reader));
        }
        reader.endArray();
        return messages;
    }

    public Photo readPhoto(JsonReader reader) throws IOException {
        int id = -1;
        String name = null;
        String clue = null;
        String answer = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String item = reader.nextName();
            if (item.equals("id")) {
                id = reader.nextInt();
            } else if (item.equals("name")) {
                name = reader.nextString();
            } else if (item.equals("clue")) {
                clue = reader.nextString();
            } else if (item.equals("answer")) {
                answer = reader.nextString();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return new Photo(id, name, clue, answer);
    }
}
