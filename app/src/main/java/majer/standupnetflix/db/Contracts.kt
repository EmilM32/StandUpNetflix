package majer.standupnetflix.db

import android.provider.BaseColumns

val DATABASE_NAME = "mylist.db"
val DATABASE_VERSION = 10

object ListEntry : BaseColumns {
    val TABLE_NAME = "mylist"
    val _ID = "id"
    val ARTIST_COL = "artist"
    val TITLE_COL = "title"
    val YEAR_COL = "year"
    val POSTER_COL = "poster"
    val DESCRIPTION_COL = "description"
    val NETFLIX_LINK_COL = "netflix_link"
    val IMDB_RATE_COL = "imdb_rate"
    val IMDB_LINK_COL = "imdb_link"
    val DURATION_MIN_COL = "duration_min"
}