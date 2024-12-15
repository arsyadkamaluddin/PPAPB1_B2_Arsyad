package site.disyfa.moneymanagement.localdb

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import site.disyfa.moneymanagement.model.ApiCategory
import site.disyfa.moneymanagement.model.Category

@Dao
interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(category: Category)

    @Query("DELETE FROM category_table")
    fun deleteAllCategories()

    @Query("SELECT * FROM category_table WHERE user_id=:user_id")
    fun getCategories(user_id: String?): LiveData<List<Category>>

    @Query("SELECT * FROM category_table WHERE id = :id")
    fun getCategory(id: Int): LiveData<Category>
}
