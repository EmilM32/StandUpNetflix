package majer.standupnetflix.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import majer.standupnetflix.StandUps
import majer.standupnetflix.db.ListEntry.ARTIST_COL
import majer.standupnetflix.db.ListEntry.DESCRIPTION_COL
import majer.standupnetflix.db.ListEntry.DURATION_MIN_COL
import majer.standupnetflix.db.ListEntry.IMDB_LINK_COL
import majer.standupnetflix.db.ListEntry.IMDB_RATE_COL
import majer.standupnetflix.db.ListEntry.NETFLIX_LINK_COL
import majer.standupnetflix.db.ListEntry.POSTER_COL
import majer.standupnetflix.db.ListEntry.TABLE_NAME
import majer.standupnetflix.db.ListEntry.TITLE_COL
import majer.standupnetflix.db.ListEntry.YEAR_COL
import majer.standupnetflix.db.ListEntry._ID

class MylistDBTable(context: Context) {

    private val TAG = MylistDBTable::class.java.simpleName
    private val dbHelper = MylistDB(context)

    fun store(standUps: StandUps): Long {
        val db = dbHelper.writableDatabase

        val values = ContentValues()
       with(values) {
           put(ARTIST_COL, standUps.artist)
           put(TITLE_COL, standUps.title)
           put(YEAR_COL, standUps.year)
           put(POSTER_COL, standUps.poster)
           put(DESCRIPTION_COL, standUps.description)
           put(NETFLIX_LINK_COL, standUps.netflix_link)
           put(IMDB_RATE_COL, standUps.imdb_rate)
           put(IMDB_LINK_COL, standUps.imdb_link)
           put(DURATION_MIN_COL, standUps.duration_min)
       }

        val id = db.transaction {
            insert(TABLE_NAME, null, values)
        }

        Log.d(TAG, "Stored new element to the DB $standUps")

        return id
    }

    fun readElementsFromDB(): List<StandUps> {

        val columns = arrayOf(_ID, ARTIST_COL, TITLE_COL, YEAR_COL, POSTER_COL,
                DESCRIPTION_COL, NETFLIX_LINK_COL, IMDB_RATE_COL, IMDB_LINK_COL, DURATION_MIN_COL)

        val db = dbHelper.readableDatabase

        val order = "$_ID DESC"

        val cursor = db.doQuery(TABLE_NAME, columns, orderBy = order)

        return parseElementsFrom(cursor)
    }

    private fun parseElementsFrom(cursor: Cursor): MutableList<StandUps> {
        val lists = mutableListOf<StandUps>()
        while (cursor.moveToNext()) {
            val artist = cursor.getString(ARTIST_COL)
            val title = cursor.getString(TITLE_COL)
            val year = cursor.getString(YEAR_COL)
            val poster = cursor.getString(POSTER_COL)
            val description = cursor.getString(DESCRIPTION_COL)
            val netflix_link = cursor.getString(NETFLIX_LINK_COL)
            val imdb_rate = cursor.getString(IMDB_RATE_COL)
            val imdb_link = cursor.getString(IMDB_LINK_COL)
            val duration_min = cursor.getString(DURATION_MIN_COL)

            lists.add(StandUps(artist, title, year, poster, description, netflix_link, imdb_rate,
                    imdb_link, duration_min))
        }
        cursor.close()
        return lists
    }
}

private fun Cursor.getString(columnName: String) = getString(getColumnIndex(columnName))

private inline fun <T> SQLiteDatabase.transaction(function: SQLiteDatabase.() -> T): T {
    beginTransaction()
    val result = try {
        val returnValue = function()
        setTransactionSuccessful()

        returnValue
    } finally {
        endTransaction()
    }
    close()

    return result
}

private fun SQLiteDatabase.doQuery(table: String, columns: Array<String>, selection: String? = null,
                                   selectionArgs: Array<String>? = null, groupBy: String? = null,
                                   having: String? = null, orderBy: String? = null): Cursor {

    return query(table, columns, selection, selectionArgs, groupBy, having, orderBy)
}