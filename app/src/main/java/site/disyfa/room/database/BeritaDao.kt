package site.disyfa.room.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface BeritaDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(berita: Berita)

    @Delete
    fun delete(berita: Berita)

    @get:Query("SELECT * from berita_table ORDER BY pubDate ASC")
    val allBerita: LiveData<List<Berita>>

    @Query("SELECT * FROM berita_table WHERE link = :link")
    fun getBerita(link: String): LiveData<List<Berita>>
}