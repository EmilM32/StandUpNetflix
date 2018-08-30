package majer.standupnetflix

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_my_list.*
import kotlinx.android.synthetic.main.activity_single.*
import kotlinx.android.synthetic.main.dialog_my_list.*
import kotlinx.android.synthetic.main.dialog_my_list.view.*
import kotlinx.android.synthetic.main.single_my_list.view.*
import majer.standupnetflix.db.MylistDB
import majer.standupnetflix.db.MylistDBTable
import majer.todolist.ClickListener

class MyList : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_list)

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.my_list)

        rv_mylist.layoutManager = LinearLayoutManager(this)
        rv_mylist.adapter = MylistAdapter(MylistDBTable(this).readElementsFromDB())

        rv_mylist.addOnItemTouchListener(RecyclerTouchListener(applicationContext, rv_mylist,
                object : ClickListener {

            @SuppressLint("SetTextI18n")
            override fun onClick(view: View, position: Int) {

                val dialogBuilder = AlertDialog.Builder(this@MyList)
                val inflater = layoutInflater.inflate(R.layout.dialog_my_list, null)

                dialogBuilder.setView(inflater)
                val alertDialog = dialogBuilder.create()
                alertDialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                alertDialog.show()

                val fromDB = MylistDBTable(this@MyList).readElementsFromDB()[position]

                Picasso.get().load(fromDB.poster).into(inflater.d_poster)

                with(inflater) {
                    d_artist.text = fromDB.artist
                    d_title.text = fromDB.title
                    d_year.text = fromDB.year + " rok"
                    d_duration.text = fromDB.duration_min + " min"
                    d_imdb_rate.text = fromDB.imdb_rate + "/10"
                    d_description.text = fromDB.description

                    d_netflix_link.setOnClickListener {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(fromDB.netflix_link))
                        startActivity(intent)
                    }
                    d_imdb_link.setOnClickListener {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(fromDB.imdb_link))
                        startActivity(intent)
                    }

                    d_button_close.setOnClickListener {
                        alertDialog.cancel()
                    }

                    d_remove.setOnClickListener {
                        Toast.makeText(applicationContext, context.getString(R.string.remove_from_my_list), Toast.LENGTH_SHORT).show()
                        MylistDB(this@MyList).deleteData(inflater.d_title.text as String)
                        this@MyList.recreate()
                    }
                }

            }
        }))
    }
}
