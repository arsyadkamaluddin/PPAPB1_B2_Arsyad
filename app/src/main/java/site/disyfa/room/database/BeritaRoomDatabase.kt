package site.disyfa.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase

@Database(entities = [Berita::class], version = 1, exportSchema = false)
abstract class BeritaRoomDatabase: RoomDatabase() {
    abstract fun beritaDao() : BeritaDao?
    companion object {
        @Volatile
        private var INSTANCE: BeritaRoomDatabase? = null
        fun getDatabase(context: Context): BeritaRoomDatabase? {
            if (INSTANCE == null) {
                synchronized(BeritaRoomDatabase::class.java) {
                    INSTANCE = databaseBuilder(
                        context.applicationContext,
                        BeritaRoomDatabase::class.java, "berita_database"
                    )
                        .build()
                }
            }
            return INSTANCE
        }
    }

}