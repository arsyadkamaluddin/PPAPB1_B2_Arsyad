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
import site.disyfa.room.database.Berita
import site.disyfa.room.model.News

class BeritaAdapter (private val newsList: List<Berita>, private val callback: IBeritaFavourite) : RecyclerView.Adapter<BeritaAdapter.BeritaViewHolder>(){
    inner class BeritaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageNews: ImageView = itemView.findViewById(R.id.img_item)
        val textTitle: TextView = itemView.findViewById(R.id.title_item)
        val textDescription: TextView = itemView.findViewById(R.id.desc_item)
        val favouriteButton: ImageView = itemView.findViewById(R.id.favourite_button)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BeritaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)
        return BeritaViewHolder(view)
    }
    override fun getItemCount(): Int {
        return newsList.size
    }
    override fun onBindViewHolder(holder: BeritaViewHolder, position: Int) {
        val berita = newsList[position]
        holder.textTitle.text = berita.title
        holder.textDescription.text = berita.description
        holder.textTitle.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(berita.link) // Ambil URL dari data API
            holder.itemView.context.startActivity(intent)
        }
        val delFavDrawable = ContextCompat.getDrawable(holder.itemView.context, R.drawable.del_fav)
        holder.favouriteButton.setImageDrawable(delFavDrawable)
        holder.favouriteButton.setOnClickListener{
                callback.onDeleteBerita(News(
                    berita.link, berita.title, berita.pubDate, berita.description, berita.thumbnail
                ))
        }
        Glide.with(holder.itemView.context)
            .load(berita.thumbnail)
            .into(holder.imageNews)
    }
}