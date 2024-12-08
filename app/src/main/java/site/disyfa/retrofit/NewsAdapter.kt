package site.disyfa.retrofit

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import site.disyfa.retrofit.databinding.ItemNewsBinding
import site.disyfa.retrofit.model.News

class NewsAdapter (private val newsList: List<News>) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>(){
    inner class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageNews: ImageView = itemView.findViewById(R.id.img_item)
        val textTitle: TextView = itemView.findViewById(R.id.title_item)
        val textDescription: TextView = itemView.findViewById(R.id.desc_item)
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
        Glide.with(holder.itemView.context)
            .load(news.thumbnail)
            .into(holder.imageNews)
//        holder.bind(newsList[position])
    }
}