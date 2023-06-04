package capstone.project.trasholution.logic.repository

import androidx.lifecycle.LiveData
import androidx.paging.*
import capstone.project.trasholution.logic.repository.db.TrasholutionDatabase
import capstone.project.trasholution.logic.repository.responses.ArticleItem
import capstone.project.trasholution.logic.repository.retrofit.ApiService
import capstone.project.trasholution.logic.repository.responses.DataItem
import capstone.project.trasholution.logic.utils.AppExecutors

class TrasholutionRepository private constructor(
    private val apiService: ApiService,
//    private val preference: UserPreference,
    private val appExecutors: AppExecutors,
    private val database: TrasholutionDatabase
) {

    fun getListPengepul(): LiveData<PagingData<DataItem>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 4
            ),
            remoteMediator = CollectorRemoteMediator(database,apiService),
            pagingSourceFactory = {
                database.pengepulDao().getAllPengepul()
            }
        ).liveData
    }

    fun getListArtikel(): LiveData<PagingData<ArticleItem>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 4
            ),
            remoteMediator = ArticleRemoteMediator(database,apiService),
            pagingSourceFactory = {
                database.artikelDao().getAllArtikel()
            }
        ).liveData
    }

    companion object {
        @Volatile
        private var instance: TrasholutionRepository? = null
        fun getInstance(
            apiService: ApiService,
//            preference: UserPreference,
            appExecutors: AppExecutors,
            database: TrasholutionDatabase,
        ): TrasholutionRepository =
            instance ?: synchronized(this) {
                instance ?: TrasholutionRepository(apiService, appExecutors,database)
            }.also { instance = it }
    }
}