package capstone.project.trasholution.logic.repository.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import capstone.project.trasholution.logic.repository.responses.ArticleItem
import capstone.project.trasholution.logic.repository.responses.DataItem

@Dao
interface ArtikelDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArtikel(story: List<ArticleItem>)

    @Query("SELECT * FROM article_entity")
    fun getAllArtikel(): PagingSource<Int, ArticleItem>

    @Query("DELETE FROM article_entity")
    suspend fun deleteAll()
}