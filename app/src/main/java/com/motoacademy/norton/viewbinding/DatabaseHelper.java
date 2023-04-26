package com.motoacademy.norton.viewbinding;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.motoacademy.norton.viewbinding.domain.Person;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static DatabaseHelper sInstance;
    public static synchronized DatabaseHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }


    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 4);
    }

    private static final String DATABASE_NAME = "person.db";
    private static final String TABLE_NAME = "tbl_names";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_USERID = "userId";
    private static final String COLUMN_FNAME = "fname";
    private static final String COLUMN_LNAME = "Lname";
    private static final int DATABASE_VERSION = 3;

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_NAMES_TABLE = "CREATE TABLE " + TABLE_NAME +
                "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_FNAME + " TEXT," +
                COLUMN_LNAME + " TEXT," +
                COLUMN_USERID + " TEXT);";

        sqLiteDatabase.execSQL(CREATE_NAMES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(sqLiteDatabase);
        }
    }

    public void addPerson(Person person) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues  values = new ContentValues();
            values.put(COLUMN_FNAME, person.getFirstName());
            values.put(COLUMN_LNAME, person.getLastName());
            values.put(COLUMN_USERID, person.getUserId());

            int userAlreadyExist = verifyPersonAlreadyExists(person.getUserId());
            if(userAlreadyExist > -1) {
                db.update(TABLE_NAME, values, COLUMN_USERID + " = " + person.getUserId(), null);
            } else {
                db.insertOrThrow(TABLE_NAME, null, values);
            }
            db.setTransactionSuccessful();
        }catch (Exception e) {
            Log.d("DB ERROR", "Error while trying to add person");
        } finally {
            db.endTransaction();
        }
    }

    public int verifyPersonAlreadyExists(String id) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME+" WHERE " + COLUMN_USERID + " = " + id + "", null);
            if(cursor != null) {
                cursor.moveToFirst();
                return cursor.getColumnIndex(COLUMN_USERID);
            }
            return -1;
        }catch (Exception e) {
            Log.d("DB ERROR", "Error while trying verifying person");
        } finally {
            db.endTransaction();
        }
        return -1;
    }

    public Person fetchPerson(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Log.i("ID", ""+id);
//            Cursor cursor = db.rawQuery("SELECT * FROM tbl_names WHERE userId = " + id, null);
            Cursor newCursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

            String currentId = (newCursor.getString(0));
            String fName = (newCursor.getString(1));
            String lName = (newCursor.getString(2));

            Log.i("PERSON", ""+currentId+fName+lName);


            if(newCursor != null) {
                newCursor.moveToFirst();
                int userIdIndex = newCursor.getColumnIndex(COLUMN_USERID);
                String userId = newCursor.getString(userIdIndex);

                int nameIndex = newCursor.getColumnIndexOrThrow(COLUMN_FNAME);
                String firstName = newCursor.getString(nameIndex);

                int lastNameIndex = newCursor.getColumnIndexOrThrow(COLUMN_LNAME);
                String lastName = newCursor.getString(lastNameIndex);

                Person person = new Person(firstName, lastName, id);
                return person;
            }
            return null;
        }catch (Exception e) {
            Log.d("DB ERROR", "Error while trying load person" + e);
        } finally {
            db.endTransaction();
        }
        return null;
    }
}
