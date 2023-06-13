package capstone.project.trasholution.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import capstone.project.trasholution.logic.repository.TrasholutionRepository
import capstone.project.trasholution.logic.repository.Result

class ProfileViewModel(private val trasholutionRepository: TrasholutionRepository): ViewModel(){
    fun getMyData(token: String, user: String): LiveData<Result<String>> = trasholutionRepository.getMyData(token, user)
}