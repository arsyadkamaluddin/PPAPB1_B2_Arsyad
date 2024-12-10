package site.disyfa.room

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import site.disyfa.room.model.News

class NewsAdapter (private val newsList: List<News>, private val callback: IBeritaFavourite) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>(){
    inner class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageNews: ImageView = itemView.findViewById(R.id.img_item)
        val textTitle: TextView = itemView.findViewById(R.id.title_item)
        val textDescription: TextView = itemView.findViewById(R.id.desc_item)
        val favouriteButton: ImageView = itemView.findViewById(R.id.favourite_button)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)
        return NewsViewHolder(view)
    }
    override fun getItemCount(): Int {
        return newsList.size
    }
    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val news = newsList[position]
        holder.textTitle.text = news.title
        holder.textDescription.text = news.description
        holder.textTitle.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(news.link) // Ambil URL dari data API
            holder.itemView.context.startActivity(intent)
        }
        val addFavDrawable = ContextCompat.getDrawable(holder.itemView.context, R.drawable.add_fav)
        val delFavDrawable = ContextCompat.getDrawable(holder.itemView.context, R.drawable.del_fav)

        callback.getBerita(news.link) { isFavorit ->
            if (isFavorit) {
                holder.favouriteButton.setImageDrawable(delFavDrawable)
            } else {
                holder.favouriteButton.setImageDrawable(addFavDrawable)
            }
        }
        holder.favouriteButton.setOnClickListener{
            val currentDrawable = holder.favouriteButton.drawable
            if (currentDrawable?.constantState == addFavDrawable?.constantState) {
                callback.onInsertBerita(news)
                holder.favouriteButton.setImageDrawable(delFavDrawable)
            } else {
                callback.onDeleteBerita(news)
                holder.favouriteButton.setImageDrawable(addFavDrawable)
            }
        }
        Glide.with(holder.itemView.context)
            .load(news.thumbnail)
            .into(holder.imageNews)
    }
}