package com.example.lab05_v2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "contact_details";

    private static final String ID_COLUMN_NAME = "person_id";
    private static final String NAME_COLUMN_NAME = "name";
    private static final String DOB_COLUMN_NAME = "dob";
    private static final String EMAIL_COLUMN_NAME = "email";

    private static final String AVATAR_COLUMN_NAME = "avatar";
    private SQLiteDatabase database;
    private static final String DATABASE_CREATE_QUERY = String.format(
            "CREATE TABLE %s (" +
                    "%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "%s TEXT, " +
                    "%s TEXT, " +
                    "%s TEXT, " +
                    "%s TEXT)",
            DATABASE_NAME, ID_COLUMN_NAME, NAME_COLUMN_NAME, DOB_COLUMN_NAME, EMAIL_COLUMN_NAME, AVATAR_COLUMN_NAME);
    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, 1);
        database = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);
        Log.w(this.getClass().getName(), DATABASE_NAME + " database upgrate to version " + newVersion + " -old data lost");
        onCreate(db);
    }

    public long insertDetails(Person person) {
        ContentValues rowValues = new ContentValues();
        rowValues.put(NAME_COLUMN_NAME, person.getName());
        rowValues.put(DOB_COLUMN_NAME, person.getDob());
        rowValues.put(EMAIL_COLUMN_NAME, person.getEmail());
        rowValues.put(AVATAR_COLUMN_NAME, person.getAvatar());
        return database.insertOrThrow(DATABASE_NAME, null, rowValues);
    }

    public Person[] getDetails() {
        Cursor results = database.query(DATABASE_NAME,
                new String[]{ID_COLUMN_NAME, NAME_COLUMN_NAME, DOB_COLUMN_NAME, EMAIL_COLUMN_NAME, AVATAR_COLUMN_NAME},
                null, null, null, null, ID_COLUMN_NAME);

        Person[] persons = new Person[results.getCount()];
        int index = 0;

        results.moveToFirst();
        while (!results.isAfterLast()) {
            int id = results.getInt(0);
            String name = results.getString(1);
            String dob = results.getString(2);
            String email = results.getString(3);
            String avatar = results.getString(4);

            Person person = new Person(id, name, dob, email, avatar);
            persons[index] = person;

            index++;
            results.moveToNext();
        }

        results.close();
        return persons;
    }
}
