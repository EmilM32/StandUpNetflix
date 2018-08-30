package majer.standupnetflix.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MylistDB(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    private val SQL_CREATE_ENTRIES = "CREATE TABLE ${ListEntry.TABLE_NAME} (" +
            "${ListEntry._ID} INTEGER PRIMARY KEY," +
            "${ListEntry.ARTIST_COL} TEXT," +
            "${ListEntry.TITLE_COL} TEXT," +
            "${ListEntry.YEAR_COL} TEXT," +
            "${ListEntry.POSTER_COL} TEXT," +
            "${ListEntry.DESCRIPTION_COL} TEXT," +
            "${ListEntry.NETFLIX_LINK_COL} TEXT," +
            "${ListEntry.IMDB_RATE_COL} TEXT," +
            "${ListEntry.IMDB_LINK_COL} TEXT," +
            "${ListEntry.DURATION_MIN_COL} TEXT" +
            ")"

    private val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${ListEntry.TABLE_NAME}"

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    fun deleteData(title : String): Int {
        val db = this.writableDatabase
        return db.delete(ListEntry.TABLE_NAME,"title = ?", arrayOf(title))
    }

}