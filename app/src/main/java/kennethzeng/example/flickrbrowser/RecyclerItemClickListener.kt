package kennethzeng.example.flickrbrowser

import android.content.Context
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.core.view.GestureDetectorCompat
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView

class RecyclerItemClickListener(context: Context, recyclerView: RecyclerView,
                                val listener: OnRecyclerClickListener)
    : RecyclerView.SimpleOnItemTouchListener() {
    private val TAG = "RIClickListener"

    // Gesture detector
    private val gestureDetector = GestureDetectorCompat(context, object:
        GestureDetector.SimpleOnGestureListener() {
        override fun onSingleTapUp(e: MotionEvent): Boolean {
            Log.d(TAG, "onSingleTapUp starts")
            // Find childView being tapped on
            val tappedView = recyclerView.findChildViewUnder(e.x, e.y)

            // Call onItemClicked listener
            tappedView?.let {
                listener.onItemClickedListener(it, recyclerView.getChildAdapterPosition(it))
            }
            return true
        }

        override fun onLongPress(e: MotionEvent) {
            Log.d(TAG, "onLongPress starts")
            // Find childView being tapped on
            val tappedView = recyclerView.findChildViewUnder(e.x, e.y)

            // Call onItemClicked listener
            tappedView?.let {
                listener.onItemLongClickedListener(it, recyclerView.getChildAdapterPosition(it))
            }
        }
    })

    interface OnRecyclerClickListener {
        fun onItemClickedListener(view: View, position: Int)
        fun onItemLongClickedListener(view: View, position: Int)
    }

    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        Log.d(TAG, "onInterceptTouchEvent starts")
        return gestureDetector.onTouchEvent(e)
    }
}