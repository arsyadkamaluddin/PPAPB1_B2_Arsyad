package site.disyfa.moneymanagement.localdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import site.disyfa.moneymanagement.model.*

@Database(entities = [User::class,Category::class,Transaction::class], version = 1, exportSchema = false)
abstract class AppRoomDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao?
    abstract fun categoryDao(): CategoryDao?
    abstract fun transactionDao(): TransactionDao?
    companion object {
        @Volatile
        private var INSTANCE: AppRoomDatabase? = null
        fun getDatabase(context: Context): AppRoomDatabase? {
            if (INSTANCE == null) {
                synchronized(AppRoomDatabase::class.java) {
                    INSTANCE = databaseBuilder(
                        context.applicationContext,
                        AppRoomDatabase::class.java, "app_database"
                    )
                        .build()
                }
            }
            return INSTANCE
        }
    }

}