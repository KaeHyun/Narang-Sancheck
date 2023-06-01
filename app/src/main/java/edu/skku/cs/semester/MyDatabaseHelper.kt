package edu.skku.cs.semester

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MyDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "mydatabase2.db"
        private const val DATABASE_VERSION = 1

        // Define your table and column names
        const val TABLE_NAME = "storage"
        const val COLUMN_DATE = "date"
        const val COLUMN_MINUTE = "minute"
        const val COLUMN_SECOND = "second"
        const val COLUMN_STEPS = "steps"
        const val COLUMN_WALK_DISTANCE = "walk_distance"
        const val COLUMN_IMAGE_PATH = "image_path"
        const val COLUMN_MOOD = "mood"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        // Create your table
        val createTableQuery = "CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_DATE TEXT," +
                "$COLUMN_MINUTE INTEGER," +
                "$COLUMN_SECOND INTEGER," +
                "$COLUMN_STEPS INTEGER," +
                "$COLUMN_WALK_DISTANCE REAL," +
                "$COLUMN_IMAGE_PATH TEXT," +
                "$COLUMN_MOOD TEXT" + ")"

        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Handle database upgrades if needed
    }
}
