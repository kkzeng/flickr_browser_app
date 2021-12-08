package kennethzeng.example.flickrbrowser

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

private const val TAG = "MainActivity"
private const val REQUEST_CODE = 5001

class MainActivity : BaseActivity(), GetRawData.OnDownloadCompleteListener,
    GetFlickrJsonData.OnJsonParsedListener,
    RecyclerItemClickListener.OnRecyclerClickListener {

    private val recyclerViewAdapter = FlickrRecyclerViewAdapter(ArrayList())
    private val searchActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityRes ->
        if(activityRes.resultCode == RESULT_OK) {
            activityRes.data?.let {
                val query = it.getStringExtra(QUERY_EXTRA)
                if(query != null) {
                    val url = createUri(
                        "https://www.flickr.com/services/feeds/photos_public.gne",
                        query, "en-us", true
                    )
                    val getRawData = GetRawData(this)
                    getRawData.execute(url)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate called")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        activateToolbar(false)
        Log.d(TAG, "onCreate finished")

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = recyclerViewAdapter
        recyclerView.addOnItemTouchListener(RecyclerItemClickListener(this, recyclerView,this))

        val url = createUri(
            "https://www.flickr.com/services/feeds/photos_public.gne",
            "android, oreo", "en-us", true
        )
        val getRawData = GetRawData(this)
        getRawData.execute(url)
    }

    private fun createUri(
        baseUrl: String, searchCriteria: String,
        language: String, matchAll: Boolean
    ): String {
        return Uri.parse(baseUrl).buildUpon().appendQueryParameter("tags", searchCriteria)
            .appendQueryParameter("tagmode", if (matchAll) "ALL" else "ANY")
            .appendQueryParameter("lang", language)
            .appendQueryParameter("format", "json")
            .appendQueryParameter("nojsoncallback", "1")
            .build()
            .toString()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        Log.d(TAG, "onCreateOptionsMenu called")
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.action_search -> {
                val intent = Intent(this, SearchActivity::class.java)
                searchActivityResultLauncher.launch(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onJsonParsed(data: List<Photo>) {
        Log.d(TAG, "onJsonParsed called")
        recyclerViewAdapter.loadNewData(data)
    }

    override fun onError(exception: Exception) {
        Log.e(TAG, "onError, exception is ${exception.message}")
    }

    override fun onDownloadComplete(data: String, status: DownloadStatus) {
        if (status == DownloadStatus.OK) {
            Log.d(TAG, "onDownloadComplete called")
            val getFlickrJsonData = GetFlickrJsonData(this)
            getFlickrJsonData.execute(data)
        } else {
            Log.d(TAG, "onDownloadComplete failed with status $status, error is $data")
        }
    }

    override fun onItemClickedListener(view: View, position: Int) {
        Toast.makeText(this, "Normal tap at position $position", Toast.LENGTH_SHORT).show()
    }

    override fun onItemLongClickedListener(view: View, position: Int) {
        val photo = recyclerViewAdapter.getPhoto(position)
        if(photo != null) {
            val intent = Intent(this, PhotoDetailsActivity::class.java)
            intent.putExtra(PHOTO_TRANSFER, photo)
            startActivity(intent)
        }
    }
}