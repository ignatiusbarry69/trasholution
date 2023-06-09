package capstone.project.trasholution.ui.mainmenu

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import capstone.project.trasholution.logic.repository.TrasholutionRepository
import capstone.project.trasholution.logic.repository.responses.ArticleAddItem
import capstone.project.trasholution.logic.repository.responses.DataItem
import okhttp3.MultipartBody
import okhttp3.RequestBody

class MainViewModel(private val trasholutionRepository: TrasholutionRepository) : ViewModel() {
    val getListPengepul: LiveData<PagingData<DataItem>> =
        trasholutionRepository.getListPengepul().cachedIn(viewModelScope)

    fun getListArtikel(query: String): LiveData<PagingData<ArticleAddItem>> =
        trasholutionRepository.getListArtikel(query).cachedIn(viewModelScope)

    fun predict(token: String, file: MultipartBody.Part) = trasholutionRepository.predict(token, file)
}