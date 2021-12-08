package kennethzeng.example.flickrbrowser

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.squareup.picasso.Picasso
import kennethzeng.example.flickrbrowser.databinding.ActivitySearchBinding

class PhotoDetailsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_details)
        activateToolbar(true)

        // Get the details from the intent
        val photo = intent.getSerializableExtra(PHOTO_TRANSFER) as Photo

        val title = findViewById<TextView>(R.id.photo_title)
        val author = findViewById<TextView>(R.id.photo_author)
        val tags = findViewById<TextView>(R.id.photo_tags)

        title.text = resources.getString(R.string.photo_title,photo.title)
        author.text = photo.author
        tags.text = photo.tags

        val imageView = findViewById<ImageView>(R.id.photo_display)
        Picasso.get()
            .load(photo.link)
            .placeholder(R.drawable.img_placeholder)
            .error(R.drawable.img_placeholder)
            .into(imageView)
    }
}