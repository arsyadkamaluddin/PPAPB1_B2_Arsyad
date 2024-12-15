package site.disyfa.moneymanagement.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "user_table")
data class User(
    @PrimaryKey
    @SerializedName("_id")
    @Expose(serialize = false) // Exclude from serialization
    var id: String = "abc",
    var name: String,
    var email: String,
    var password: String
)
