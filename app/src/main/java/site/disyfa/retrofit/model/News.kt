package site.disyfa.retrofit.model

import com.google.gson.annotations.SerializedName

data class News(
    @SerializedName("link")
    val link: String, val title: String,
    @SerializedName("pubDate")
    val pubDate: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("thumbnail")
    val thumbnail: String
)
