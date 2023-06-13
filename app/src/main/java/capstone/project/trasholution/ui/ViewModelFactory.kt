package capstone.project.trasholution.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import capstone.project.trasholution.logic.repository.Injection
import capstone.project.trasholution.logic.repository.TrasholutionRepository
import capstone.project.trasholution.ui.mainmenu.MainViewModel
import capstone.project.trasholution.ui.map.MapsViewModel
import capstone.project.trasholution.ui.profile.ProfileViewModel

class ViewModelFactory private constructor(private val TrasholutionRepository: TrasholutionRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(TrasholutionRepository) as T
            }
            modelClass.isAssignableFrom(MapsViewModel::class.java) -> {
                MapsViewModel(TrasholutionRepository) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(TrasholutionRepository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }
}