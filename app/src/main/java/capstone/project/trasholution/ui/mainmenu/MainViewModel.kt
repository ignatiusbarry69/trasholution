package capstone.project.trasholution.ui.mainmenu

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import capstone.project.trasholution.logic.repository.TrasholutionRepository
import capstone.project.trasholution.logic.repository.responses.ArticleItem
import capstone.project.trasholution.logic.repository.responses.DataItem

class MainViewModel(private val trasholutionRepository: TrasholutionRepository) : ViewModel() {
    val getListPengepul: LiveData<PagingData<DataItem>> =
        trasholutionRepository.getListPengepul().cachedIn(viewModelScope)

    val getListArtikel: LiveData<PagingData<ArticleItem>> =
        trasholutionRepository.getListArtikel().cachedIn(viewModelScope)
}