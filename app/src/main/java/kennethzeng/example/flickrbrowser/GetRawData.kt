package kennethzeng.example.flickrbrowser

import android.os.AsyncTask
import android.util.Log
import java.io.IOException
import java.lang.Exception
import java.net.MalformedURLException
import java.net.URL

enum class DownloadStatus {
    OK, IDLE, NOT_INITIALIZED, FAILED_OR_EMPTY, PERMISSIONS_ERROR, ERROR
}

private val TAG = "GetRawData"
class GetRawData(private val listener: OnDownloadCompleteListener): AsyncTask<String, Void, String>() {

    private var downloadStatus = DownloadStatus.IDLE

    interface OnDownloadCompleteListener {
        fun onDownloadComplete(data: String, status: DownloadStatus)
    }
    override fun doInBackground(vararg params: String?): String {
        // Check if we've been given a URL
        if(params[0].isNullOrEmpty()) {
            downloadStatus = DownloadStatus.NOT_INITIALIZED
            return "No URL specified"
        }

        try {
            downloadStatus = DownloadStatus.OK
            return URL(params[0]).readText()
        }
        catch(e: Exception) {
            val errorMsg = when(e) {
                is MalformedURLException -> {
                    downloadStatus = DownloadStatus.NOT_INITIALIZED
                    "doInBackground: Invalid URL ${e.message}"
                }
                is IOException -> {
                    downloadStatus = DownloadStatus.FAILED_OR_EMPTY
                    "doInBackground: IOException ${e.message}"
                }
                is SecurityException -> {
                    downloadStatus = DownloadStatus.PERMISSIONS_ERROR
                    "doInBackground: SecurityException ${e.message}"
                }
                else -> {
                    downloadStatus = DownloadStatus.ERROR
                    "doInBackground: Unknown error ${e.message}"
                }
            }
            Log.e(TAG, errorMsg)
            return errorMsg
        }
    }

    override fun onPostExecute(result: String) {
        Log.d(TAG, "onPostExecute called")
        listener.onDownloadComplete(result, downloadStatus)
    }
}