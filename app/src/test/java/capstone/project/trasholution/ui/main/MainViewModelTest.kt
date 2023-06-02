package capstone.project.trasholution.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import capstone.project.trasholution.DataDummy
import capstone.project.trasholution.MainDispatcherRule
import capstone.project.trasholution.getOrAwaitValue
import capstone.project.trasholution.logic.repository.TrasholutionRepository
import capstone.project.trasholution.logic.repository.responses.DataItem
import capstone.project.trasholution.ui.mainmenu.MainViewModel
import capstone.project.trasholution.ui.mainmenu.collector.PengepulAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Mock
    private lateinit var trasholutionRepository: TrasholutionRepository

    @Test
    fun `when Get Story Should Not Null and Return Data`() = runTest {
        val dummyData = DataDummy.generateDummyDataResponse()
        val data: PagingData<DataItem> = DataPagingSource.snapshot(dummyData)
        val expectedData = MutableLiveData<PagingData<DataItem>>()
        expectedData.value = data
        Mockito.`when`(trasholutionRepository.getListPengepul()).thenReturn(expectedData)

        val mainViewModel = MainViewModel(trasholutionRepository)
        val actualData: PagingData<DataItem> = mainViewModel.getListPengepul.getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = PengepulAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualData)
        advanceUntilIdle()
        Mockito.verify(trasholutionRepository).getListPengepul()
        Assert.assertNotNull(differ.snapshot()) //Memastikan data tidak null.
        Assert.assertEquals(dummyData.size, differ.snapshot().size) //Memastikan jumlah data sesuai dengan yang diharapkan.
        Assert.assertEquals(dummyData[0], differ.snapshot()[0]) //Memastikan data pertama yang dikembalikan sesuai.
    }

    @Test
    fun `when Get Story Empty Should Return No Data`() = runTest {
        val data: PagingData<DataItem> = PagingData.from(emptyList())
        val expectedData = MutableLiveData<PagingData<DataItem>>()
        expectedData.value = data
        Mockito.`when`(trasholutionRepository.getListPengepul()).thenReturn(expectedData)
        val mainViewModel = MainViewModel(trasholutionRepository)
        val actualData: PagingData<DataItem> = mainViewModel.getListPengepul.getOrAwaitValue()
        val differ = AsyncPagingDataDiffer(
            diffCallback = PengepulAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualData)
        Assert.assertEquals(0, differ.snapshot().size)//Memastikan jumlah data yang dikembalikan nol.
    }
}

class DataPagingSource : PagingSource<Int, LiveData<List<DataItem>>>() {
    companion object {
        fun snapshot(items: List<DataItem>): PagingData<DataItem> {
            return PagingData.from(items)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, LiveData<List<DataItem>>>): Int {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<DataItem>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }
}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}