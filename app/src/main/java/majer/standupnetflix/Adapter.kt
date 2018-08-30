package majer.standupnetflix

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.single_card.view.*

class Adapter(private val homeFeed: List<StandUps>) : RecyclerView.Adapter<CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        return CustomViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.single_card, parent, false))
    }

    override fun getItemCount(): Int {
        return this.homeFeed.size
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val standup = homeFeed[position]
        val i = holder.itemView

        i.tv_title.text = standup.title
        i.tv_artist.text = standup.artist

        Picasso.get().load(standup.poster).into(i.iv_poster)

        holder.standUps = standup
    }

}
class CustomViewHolder (view: View, var standUps: StandUps? = null) : RecyclerView.ViewHolder(view) {

    companion object {
        val NAV_BAR_TITLE_KEY = "NAV_BAR_TITLE"
        val ARTIST_KEY = "ARTIST"
        val TITLE_KEY = "TITLE"
        val YEAR_KEY = "YEAR"
        val POSTER_KEY = "POSTER"
        val DESCRIPTION_KEY = "DESCRIPTION"
        val NETFLIX_LINK_KEY = "NETFLIX_LINK"
        val IMDB_RATE_KEY = "IMDB_RATE"
        val IMDB_LINK_KEY = "IMDB_LINK"
        val DURATION_MIN_KEY = "DURATION_MIN"
    }

    init {
        view.setOnClickListener {
            val intent = Intent(it.context, SingleActivity::class.java)

            with(intent) {
                putExtra(NAV_BAR_TITLE_KEY, standUps?.artist + ": " + standUps?.title)
                putExtra(ARTIST_KEY, standUps?.artist)
                putExtra(TITLE_KEY, standUps?.title)
                putExtra(YEAR_KEY, standUps?.year)
                putExtra(POSTER_KEY, standUps?.poster)
                putExtra(DESCRIPTION_KEY, standUps?.description)
                putExtra(NETFLIX_LINK_KEY, standUps?.netflix_link)
                putExtra(IMDB_RATE_KEY, standUps?.imdb_rate)
                putExtra(IMDB_LINK_KEY, standUps?.imdb_link)
                putExtra(DURATION_MIN_KEY, standUps?.duration_min)

            }
            it.context.startActivity(intent)
        }
    }

}