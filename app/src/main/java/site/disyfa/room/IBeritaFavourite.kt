package site.disyfa.room

import androidx.lifecycle.LiveData
import site.disyfa.room.model.News

interface IBeritaFavourite {
    fun onInsertBerita(berita: News)
    fun onDeleteBerita(berita: News)
    fun getBerita(link: String, callback: (Boolean) -> Unit)
}