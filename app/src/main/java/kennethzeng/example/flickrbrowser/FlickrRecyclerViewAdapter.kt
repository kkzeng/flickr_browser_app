package kennethzeng.example.flickrbrowser

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class FlickrImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var thumbnail = itemView.findViewById<ImageView>(R.id.thumbnail)
    var title = itemView.findViewById<TextView>(R.id.title_text)
}

class FlickrRecyclerViewAdapter(private var photoList: List<Photo>):
    RecyclerView.Adapter<FlickrImageViewHolder>() {
    private val TAG = "FlickrAdapter"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlickrImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.browse_photo, parent, false)
        return FlickrImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: FlickrImageViewHolder, position: Int) {
        // Load photo from the URL
        // ImageView
        val photoItem = photoList[position]
        Picasso.get()
            .load(photoItem.image)
            .placeholder(R.drawable.img_placeholder)
            .error(R.drawable.img_placeholder)
            .into(holder.thumbnail)

        holder.title.text = photoItem.title
    }

    fun loadNewData(newPhotoList: List<Photo>) {
        photoList = newPhotoList
        notifyDataSetChanged()
    }

    fun getPhoto(pos: Int): Photo? {
        return if(photoList.size <= pos) null else photoList[pos]
    }

    override fun getItemCount(): Int {
        return photoList.size
    }
}