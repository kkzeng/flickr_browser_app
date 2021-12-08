package kennethzeng.example.flickrbrowser

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.SearchView

val QUERY_EXTRA = "QUERY_EXTRA"

class SearchActivity : BaseActivity() {
    private val TAG = "SearchActivity"

    private var searchView: SearchView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        activateToolbar(true)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView = menu.findItem(R.id.app_bar_search)?.actionView as SearchView

        val searchableInfo = searchManager.getSearchableInfo(componentName)
        searchView?.setSearchableInfo(searchableInfo)

        // SearchView should be open on default
        searchView?.isIconified = false

        searchView?.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView?.clearFocus()

                // Pass back search results to MainActivity
                if(query.isNullOrEmpty()) {
                    setResult(RESULT_CANCELED)
                }
                else {
                    val intent = Intent().apply {
                        putExtra(QUERY_EXTRA, query)
                    }
                    setResult(RESULT_OK, intent)
                }

                finish()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean = false

        })

        searchView?.setOnCloseListener {
            setResult(RESULT_CANCELED)
            finish()
            false
        }

        return true
    }
}