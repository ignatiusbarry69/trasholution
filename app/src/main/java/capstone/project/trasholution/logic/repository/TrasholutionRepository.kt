package capstone.project.trasholution.logic.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.*
import capstone.project.trasholution.logic.repository.db.TrasholutionDatabase
import capstone.project.trasholution.logic.repository.responses.ArticleAddItem
//import capstone.project.trasholution.logic.repository.responses.ArticleItem
import capstone.project.trasholution.logic.repository.retrofit.ApiService
import capstone.project.trasholution.logic.repository.responses.DataItem
import capstone.project.trasholution.logic.repository.responses.ModelResponse
import capstone.project.trasholution.logic.repository.responses.PengepulResponse
import capstone.project.trasholution.logic.utils.AppExecutors
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


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
                pageSize = 5
            ),
            remoteMediator = CollectorRemoteMediator(database,apiService),
            pagingSourceFactory = {
                database.pengepulDao().getAllPengepul()
            }
        ).liveData
    }

    fun getListArtikel(query: String): LiveData<PagingData<ArticleAddItem>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 4
            ),
            remoteMediator = ArticleRemoteMediator(database,apiService),
            pagingSourceFactory = {
                database.artikelDao().getAllArtikel(query)
            }
        ).liveData
    }

    fun predict(token: String,file: MultipartBody.Part): LiveData<Result<String>> {
        val result = MutableLiveData<Result<String>>()
        result.value = Result.Loading
        val client = apiService.predict(token, file)
        client.enqueue(object : Callback<ModelResponse> {
            override fun onResponse(
                call: Call<ModelResponse>,
                response: Response<ModelResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error) {
                        result.value = Result.Success(responseBody.data)
                    }
                } else {
                    result.value = Result.Error(response.message())
                }
            }

            override fun onFailure(call: Call<ModelResponse>, t: Throwable) {
                result.value = Result.Error(t.toString())
            }
        })
        return result
    }

    fun getLocation(): LiveData<Result<List<DataItem>>> {
        val result = MutableLiveData<Result<List<DataItem>>>()
        apiService.getLocation()
            .enqueue(object : Callback<PengepulResponse> {
                override fun onResponse(
                    call: Call<PengepulResponse>,
                    response: Response<PengepulResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null && !responseBody.error) {
                            Log.d("TAG", "${responseBody.data[0].lat}, ${responseBody.data[0].lon} ")
                            result.value = Result.Success(responseBody.data)
                        }
                    }
                }
                override fun onFailure(call: Call<PengepulResponse>, t: Throwable) {
                    result.value = Result.Error("error")
                }
            })
        return result
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