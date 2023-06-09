package capstone.project.trasholution.logic.repository.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import capstone.project.trasholution.logic.repository.responses.ArticleAddItem
//import capstone.project.trasholution.logic.repository.responses.ArticleItem
import capstone.project.trasholution.logic.repository.responses.DataItem

@Dao
interface ArtikelDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArtikel(story: List<ArticleAddItem>)

    @Query("SELECT * FROM article_entity WHERE createDate LIKE '%' || :query || '%' OR username LIKE '%' || :query || '%' OR title LIKE '%' || :query || '%' OR jenisSampah LIKE '%' || :query || '%' OR content LIKE '%' || :query || '%'")
    fun getAllArtikel(query: String): PagingSource<Int, ArticleAddItem>



    @Query("DELETE FROM article_entity")
    suspend fun deleteAll()
}