package site.disyfa.moneymanagement.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import site.disyfa.moneymanagement.util.StringGenerator
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class ApiTransaction(
    val user_id: String?,
    var amount: Int,
    var type: String,
    var category: String,
    var description: String,
    var categoryColor: Int=0,
    var categoryIcon: Int=0,
    var date: String = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
)
