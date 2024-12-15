package site.disyfa.moneymanagement.localdb

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import site.disyfa.moneymanagement.model.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(user: User)

    @Query("DELETE FROM user_table")
    fun deleteAllUsers()

    @Update
    fun update(user: User)

    @Query("SELECT * FROM user_table WHERE id = :id LIMIT 1")
    fun getUser(id: String): LiveData<User>

    @Query("SELECT * FROM user_table WHERE email = :email LIMIT 1")
    fun getUserByEmail(email: String): LiveData<User>

    @Query("SELECT password FROM user_table WHERE email = :email LIMIT 1")
    fun getPassword(email: String): LiveData<String>
}