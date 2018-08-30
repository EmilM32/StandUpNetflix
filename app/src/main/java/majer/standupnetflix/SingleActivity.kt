package majer.standupnetflix

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_single.*
import kotlinx.android.synthetic.main.dialog_my_list.view.*
import majer.standupnetflix.db.MylistDB
import majer.standupnetflix.db.MylistDBTable

class SingleActivity : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single)

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val navBarTitle = intent.getStringExtra(CustomViewHolder.NAV_BAR_TITLE_KEY)
        val standupArtist = intent.getStringExtra(CustomViewHolder.ARTIST_KEY)
        val standupTitle = intent.getStringExtra(CustomViewHolder.TITLE_KEY)
        val standupYear = intent.getStringExtra(CustomViewHolder.YEAR_KEY)
        val standupPoster = intent.getStringExtra(CustomViewHolder.POSTER_KEY)
        val standupDescription = intent.getStringExtra(CustomViewHolder.DESCRIPTION_KEY)
        val standupNetflixLink = intent.getStringExtra(CustomViewHolder.NETFLIX_LINK_KEY)
        val standupImdbRate = intent.getStringExtra(CustomViewHolder.IMDB_RATE_KEY)
        val standupImdbLink = intent.getStringExtra(CustomViewHolder.IMDB_LINK_KEY)
        val standupDurationMin = intent.getStringExtra(CustomViewHolder.DURATION_MIN_KEY)

        supportActionBar?.title = navBarTitle

        sa_artist.text = standupArtist
        sa_title.text = standupTitle
        sa_year.text = "$standupYear rok"
        sa_imdb_rate.text = "$standupImdbRate / 10"
        sa_duration_min.text = "$standupDurationMin min"
        sa_description.text = standupDescription

        Picasso.get().load(standupPoster).into(sa_poster)

        sa_netflixButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(standupNetflixLink))
            startActivity(intent)
        }

        sa_ImdbButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(standupImdbLink))
            startActivity(intent)
        }

        sa_fav.text = ""
        sa_fav.textOn = ""
        sa_fav.textOff = ""

        sa_fav.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                addToDB(standupArtist, standupTitle, standupYear, standupPoster, standupDescription,
                        standupNetflixLink, standupImdbRate, standupImdbLink, standupDurationMin)
            }
            buttonView.setOnCheckedChangeListener { buttonView, isChecked ->
                if (!isChecked) {
                    MylistDB(this@SingleActivity).deleteData(sa_title.text as String)
                    this@SingleActivity.recreate()
                    displayMessage(getString(R.string.remove_from_my_list))
                }
            }
        }
    }

    private fun addToDB(standupArtist: String, standupTitle: String, standupYear: String, standupPoster: String,
                        standupDescription: String, standupNetflixLink: String, standupImdbRate: String,
                        standupImdbLink: String, standupDurationMin: String) {

        val standUps = StandUps(standupArtist, standupTitle, standupYear, standupPoster,
                standupDescription, standupNetflixLink, standupImdbRate, standupImdbLink,
                standupDurationMin)

        val id = MylistDBTable(this).store(standUps)

        if (id == -1L) {
            displayMessage(getString(R.string.not_add))
        } else {
            displayMessage(getString(R.string.add_to_my_list))
        }
    }

    private fun displayMessage(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }
}
