package site.disyfa.room.database

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "berita_table",indices = [Index(value = ["link"], unique = true)])
data class Berita(
    @PrimaryKey
    @ColumnInfo(name="link")
    val link: String,
    @ColumnInfo(name="title")
    val title: String,
    @ColumnInfo(name="pubDate")
    val pubDate: String,
    @ColumnInfo(name="description")
    val description: String,
    @ColumnInfo(name="thumbnail")
    val thumbnail: String
)
