package site.disyfa.moneymanagement.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import site.disyfa.moneymanagement.util.StringGenerator

data class ApiCategory(
    val user_id: String?,
    var name: String?,
    var icon: Int,
    var colour: Int
)

