package com.andersenlab.boilerplate.model.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.andersenlab.boilerplate.model.Image;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

import static com.andersenlab.boilerplate.model.db.ImageContract.ImageEntry;

/**
 * Helper class to work with database.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "BoilerplateApp.db";
    private static final int DATABASE_VERSION = 1;
    private static DatabaseHelper sInstance;

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (sInstance == null)
            sInstance = new DatabaseHelper(context.getApplicationContext());

        return sInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ImageContract.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }

    public long addImage(Image image) {
        SQLiteDatabase db = getWritableDatabase();

        long insertedId = -1;

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(ImageEntry.COLUMN_CONTENT_DESCRIPTION, image.getContentDescription());
            values.put(ImageEntry.COLUMN_IMAGE_URL, image.getImageUrl());

            insertedId = db.insert(ImageEntry.TABLE_NAME, null, values);
            db.setTransactionSuccessful();
        } catch (Exception exc) {
            Timber.e(exc);
        } finally {
            db.endTransaction();
            close();
        }

        return insertedId;
    }

    public Image getImage(long id) {
        SQLiteDatabase db = getReadableDatabase();

        Image image = null;
        Cursor cursor = db.query(ImageEntry.TABLE_NAME, null,
                ImageEntry.COLUMN_IMAGE_ID + " = ?", new String[]{Long.toString(id)},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            image = getImage(cursor);
        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        close();
        return image;
    }

    public List<Image> getAllImages() {
        SQLiteDatabase db = getReadableDatabase();

        List<Image> imageList = new ArrayList<>();
        Cursor cursor = db.query(ImageEntry.TABLE_NAME, null,
                null, null, null, null, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Image image = getImage(cursor);
                    imageList.add(image);
                } while (cursor.moveToNext());
            }
        } catch (Exception exc) {
            Timber.e(exc);
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            close();
        }

        return imageList;
    }

    public void deleteImage(long id) {
        deleteRow(ImageEntry.TABLE_NAME, ImageEntry.COLUMN_IMAGE_ID + " = ?", new String[]{Long.toString(id)});
    }

    public void deleteAllImages() {
        deleteRow(ImageEntry.TABLE_NAME, null, null);
    }

    private Image getImage(Cursor cursor) {
        Image image = null;

        if (cursor != null) {
            image = new Image();
            image.setId(cursor.getLong(cursor.getColumnIndex(ImageEntry.COLUMN_IMAGE_ID)));
            image.setContentDescription(cursor.getString(cursor.getColumnIndex(ImageEntry.COLUMN_CONTENT_DESCRIPTION)));
            image.setImageUrl(cursor.getString(cursor.getColumnIndex(ImageEntry.COLUMN_IMAGE_URL)));
        }

        return image;
    }

    private void deleteRow(String tableName, String whereClause, String[] args) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(tableName, whereClause, args);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Timber.e(e, e.getClass().getCanonicalName());
        } finally {
            db.endTransaction();
            close();
        }
    }
}
