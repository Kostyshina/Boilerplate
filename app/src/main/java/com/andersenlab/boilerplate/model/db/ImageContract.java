package com.andersenlab.boilerplate.model.db;

public class ImageContract {

    static final String CREATE_TABLE = "CREATE TABLE " + ImageEntry.TABLE_NAME + "(" +
            ImageEntry.COLUMN_IMAGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            ImageEntry.COLUMN_CONTENT_DESCRIPTION + " TEXT, " +
            ImageEntry.COLUMN_IMAGE_URL + " TEXT NOT NULL);";

    private ImageContract() {}

    static class ImageEntry {

        static final String TABLE_NAME = "image";
        static final String COLUMN_IMAGE_ID = "_id";
        static final String COLUMN_IMAGE_URL = "imageUrl";
        static final String COLUMN_CONTENT_DESCRIPTION = "contentDescription";

        private ImageEntry() {}
    }
}
