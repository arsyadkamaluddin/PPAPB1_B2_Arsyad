package site.disyfa.gallery

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import okhttp3.OkHttpClient
import okhttp3.Request
import site.disyfa.gallery.databinding.ItemImageBinding
import java.io.IOException

class ImageAdapter(private val context: Context, private val itemCount: Int) : RecyclerView.Adapter<ImageAdapter.ItemDisasterViewHolder>() {

    inner class ItemDisasterViewHolder(private val binding: ItemImageBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            fun getRedirectedUrl(apiUrl: String, callback: (String?) -> Unit) {
                val client = OkHttpClient.Builder().followRedirects(false).build()
                val request = Request.Builder().url(apiUrl).build()
                client.newCall(request).enqueue(object : okhttp3.Callback {
                    override fun onFailure(call: okhttp3.Call, e: IOException) {
                        Handler(Looper.getMainLooper()).post { callback(null) } // Run on main thread
                    }
                    override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                        val redirectedUrl = if (response.isRedirect) {
                            response.header("Location")
                        } else {
                            null
                        }
                        Handler(Looper.getMainLooper()).post { callback(redirectedUrl) } // Run on main thread
                    }
                })
            }
            getRedirectedUrl("https://picsum.photos/300/300") { redirectedUrl ->

                Glide.with(context)
                    .load(redirectedUrl)
                    .error(android.R.drawable.ic_dialog_alert) // Fallback image on error
                    .into(binding.imgItem)

            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemDisasterViewHolder {
        val binding = ItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemDisasterViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return itemCount
    }

    override fun onBindViewHolder(holder: ItemDisasterViewHolder, position: Int) {
        holder.bind()
    }
}
