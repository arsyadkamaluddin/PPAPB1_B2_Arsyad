package site.disyfa.moneymanagement.localdb

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import site.disyfa.moneymanagement.model.Transaction

@Dao
interface TransactionDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(transaction: Transaction)

    @Update
    fun update(transaction: Transaction)

    @Delete
    fun delete(transaction: Transaction)

    @Query("DELETE FROM transaction_table")
    fun deleteAllTransactions()

    @Query("SELECT * FROM transaction_table")
    fun getAllTransactions(): LiveData<List<Transaction>>

    @Query("SELECT * FROM transaction_table WHERE id = :id LIMIT 1")
    fun getTransaction(id: String): LiveData<Transaction>

    @Query("SELECT * FROM transaction_table WHERE user_id=:user_id ORDER BY date DESC")
    fun getUserTransaction(user_id: String?): LiveData<List<Transaction>>
}
