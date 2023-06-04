package capstone.project.trasholution.logic.repository.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import capstone.project.trasholution.logic.repository.responses.DataItem

@Dao
interface PengepulDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPengepul(story: List<DataItem>)

    @Query("SELECT * FROM pengepul_entity")
    fun getAllPengepul(): PagingSource<Int, DataItem>

    @Query("DELETE FROM pengepul_entity")
    suspend fun deleteAll()
}