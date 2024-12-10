package site.disyfa.room.model

import com.google.gson.annotations.SerializedName

data class NewsList(
    @SerializedName("link")
    val link: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("image")
    val image: String,
    @SerializedName("posts")
    val posts: List<News>
)
