package majer.standupnetflix

data class StandUps(
        val artist: String,
        val title: String,
        val year: String,
        val poster: String,
        val description: String,
        val netflix_link: String,
        val imdb_rate: String,
        val imdb_link: String,
        val duration_min: String
)