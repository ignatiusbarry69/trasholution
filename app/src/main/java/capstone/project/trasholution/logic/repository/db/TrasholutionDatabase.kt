package capstone.project.trasholution.logic.repository.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import capstone.project.trasholution.logic.repository.responses.ArticleAddItem
//import capstone.project.trasholution.logic.repository.responses.ArticleItem
import capstone.project.trasholution.logic.repository.responses.DataItem

@Database(
    entities = [RemoteKeys::class, DataItem::class, ArticleAddItem::class],
    version = 1,
    exportSchema = false
)
abstract class TrasholutionDatabase : RoomDatabase() {

    abstract fun pengepulDao(): PengepulDao
    abstract fun artikelDao(): ArtikelDao
    abstract fun remoteKeysDao(): RemoteKeysDao

    companion object {
        @Volatile
        private var INSTANCE: TrasholutionDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): TrasholutionDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    TrasholutionDatabase::class.java, "trasholution_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}