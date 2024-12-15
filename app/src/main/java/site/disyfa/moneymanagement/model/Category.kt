package site.disyfa.moneymanagement.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "category_table")
data class Category(
    @PrimaryKey
    @SerializedName("_id")
    @Expose(serialize = false) // Exclude from serialization
    var id: String = "abc",
    val user_id: String?,
    var name: String?,
    var icon: Int,
    var colour: Int
)
