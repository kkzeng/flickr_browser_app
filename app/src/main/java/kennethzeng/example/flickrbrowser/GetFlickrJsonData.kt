package kennethzeng.example.flickrbrowser

import android.os.AsyncTask
import org.json.JSONException
import org.json.JSONObject

class GetFlickrJsonData(private val listener: OnJsonParsedListener): AsyncTask<String, Void, List<Photo>>() {

    private val TAG = "GetFlickrJsonData"

    interface OnJsonParsedListener {
        fun onJsonParsed(data: List<Photo>)
        fun onError(exception: Exception)
    }

    override fun doInBackground(vararg params: String?): List<Photo>? {
        // Json parsing
        val arrayList = ArrayList<Photo>()
        try {
            val jsonData = JSONObject(params[0])
            val itemsArray = jsonData.getJSONArray("items")
            for(i in 0 until itemsArray.length()) {
                val jsonPhoto = itemsArray.getJSONObject(i)
                val title = jsonPhoto.getString("title")
                val author = jsonPhoto.getString("author")
                val authorId = jsonPhoto.getString("author_id")
                val tags = jsonPhoto.getString("tags")
                val imageUrl = jsonPhoto.getJSONObject("media").getString("m")
                val fullSizeLink = imageUrl.replaceFirst("_m.jpg", "_b.jpg")

                val photoObject = Photo(title, author, authorId, fullSizeLink, tags, imageUrl)
                arrayList.add(photoObject)
            }
        }
        catch (e: JSONException) {
            e.printStackTrace()
            listener.onError(e)
            cancel(true)
        }

        return arrayList
    }

    override fun onPostExecute(result: List<Photo>) {
        super.onPostExecute(result)
        // Callback for parsing finishing
        listener.onJsonParsed(result)
    }
}