package majer.standupnetflix

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_no_internet.view.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Response
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private val TAG = MainActivity::class.java.simpleName
    private val jsonUrl = "http://majer.tangun.pl/standups.json"
    var sorting: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar as Toolbar?)

        rv.layoutManager = GridLayoutManager(this, 2) as RecyclerView.LayoutManager?

        fetchJson(jsonUrl)

        swiperefresh.isEnabled = true
        swiperefresh.setOnRefreshListener {
            fetchJson(jsonUrl)
            swiperefresh.isRefreshing = false
        }

        if (!isOnline()) {
            createNoInternetDialog()
        }
    }

    private fun createNoInternetDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = layoutInflater.inflate(R.layout.dialog_no_internet, null)

        dialogBuilder.setView(inflater)
        val alertDialog = dialogBuilder.create()
        alertDialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.show()

        inflater.button_ok.setOnClickListener {
            alertDialog.dismiss()
        }
    }

    private fun isOnline(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        return netInfo != null && netInfo.isConnectedOrConnecting
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_favorite -> {
            val intent = Intent(applicationContext, MyList::class.java)
            startActivity(intent)
            true
        }

        R.id.action_sort -> {
            if (sorting) {
                sorting = !sorting
                Toast.makeText(applicationContext, "Sortowanie: rok", Toast.LENGTH_SHORT).show()
            } else {
                sorting = !sorting
                Toast.makeText(applicationContext, "Sortowanie: ocena", Toast.LENGTH_SHORT).show()
            }
            fetchJson(jsonUrl)
            true
        }

        else -> {
            super.onOptionsItemSelected(item)
        }
    }


    private fun fetchJson(jsonUrl: String) {
        Log.d(TAG, "Attempting to fetch json")

        val request = okhttp3.Request.Builder().url(jsonUrl).build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d(TAG, "Failed to execute request")
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body()?.string()
                Log.d(TAG, "$body")

                val gson = GsonBuilder().create()

                val homeFeed = gson.fromJson(body, HomeFeed::class.java)

                var sortedHomeFeed = homeFeed.standups.sortedWith(compareBy({ it.year }))

                if (sorting) {
                    sortedHomeFeed = homeFeed.standups.sortedWith(compareBy({ it.imdb_rate }))
                }

                runOnUiThread {
                    rv.adapter = Adapter(sortedHomeFeed)
                }
            }
        })
    }

    fun <T> compareBy(vararg selectors: (T) -> Comparable<*>?): Comparator<T> {
        return Comparator<T> { a, b -> compareValuesBy(b, a, *selectors) }
    }

}