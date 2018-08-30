package majer.standupnetflix

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.single_my_list.view.*

class MylistAdapter(private val elements: List<StandUps>) : RecyclerView.Adapter<ElementsViewHolder>() {

    override fun getItemCount(): Int = elements.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ElementsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_my_list, parent, false)
        return ElementsViewHolder(view)
    }

    override fun onBindViewHolder(holder: ElementsViewHolder, index: Int) {
        val element = elements[index]
        val c = holder.card

        c.ml_artist.text = element.artist
        c.ml_title.text = element.title
        c.ml_imdb_rate.text = element.imdb_rate
        c.ml_duration.text = element.duration_min

        Picasso.get().load(element.poster).transform(PicassoCircleTransformation()).into(c.ml_poster)

        c.ml_netflix_link.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(element.netflix_link))
            it.context.startActivity(intent)
        }

        c.ml_imdb_link.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(element.imdb_link))
            it.context.startActivity(intent)
        }
    }

}
class ElementsViewHolder(val card: View, var standUps: StandUps? = null) : RecyclerView.ViewHolder(card)
