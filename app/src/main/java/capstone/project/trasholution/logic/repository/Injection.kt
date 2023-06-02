package capstone.project.trasholution.logic.repository

import android.content.Context
import capstone.project.trasholution.logic.repository.db.TrasholutionDatabase
import capstone.project.trasholution.logic.repository.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): TrasholutionRepository {
        val apiService = ApiConfig.getApiService()
//        val preference = UserPreference(context)
        val database = TrasholutionDatabase.getDatabase(context)
        val appExecutors = capstone.project.trasholution.logic.utils.AppExecutors()

        return TrasholutionRepository.getInstance(apiService,appExecutors,database)
    }
}