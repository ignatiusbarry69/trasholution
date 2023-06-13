package capstone.project.trasholution.ui.map

import androidx.lifecycle.LiveData
import capstone.project.trasholution.logic.repository.Result
import androidx.lifecycle.ViewModel
import capstone.project.trasholution.logic.repository.TrasholutionRepository
import capstone.project.trasholution.logic.repository.responses.DataItem


class MapsViewModel(private val trasholutionRepository: TrasholutionRepository): ViewModel(){
    fun getLocation(): LiveData<Result<List<DataItem>>> = trasholutionRepository.getLocation()
}