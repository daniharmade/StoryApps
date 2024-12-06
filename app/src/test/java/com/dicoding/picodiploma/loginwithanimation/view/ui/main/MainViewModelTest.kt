package com.dicoding.picodiploma.loginwithanimation.view.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.dicoding.picodiploma.loginwithanimation.adapter.StoryListAdapter
import com.dicoding.picodiploma.loginwithanimation.data.repository.AppRepository
import com.dicoding.picodiploma.loginwithanimation.data.response.StoryDetail
import com.dicoding.picodiploma.loginwithanimation.view.ui.DataDummy
import com.dicoding.picodiploma.loginwithanimation.view.ui.MainDispatcherRule
import com.dicoding.picodiploma.loginwithanimation.view.ui.dashboard.home.HomeViewModel
import com.dicoding.picodiploma.loginwithanimation.view.ui.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
    private lateinit var quoteRepository: AppRepository

    @Test
    fun `when Get Story Should Not Null and Return Data`() = runTest {
        val dummyQuote = DataDummy.generateDummyStoryResponse()
        val data: PagingData<StoryDetail> = QuotePagingSource.snapshot(dummyQuote)
        val expectedQuote = MutableLiveData<PagingData<StoryDetail>>()
        expectedQuote.value = data
        Mockito.`when`(quoteRepository.getStoryPagingSource()).thenReturn(expectedQuote)

        val mainViewModel = HomeViewModel(quoteRepository)
        val actualQuote: PagingData<StoryDetail> = mainViewModel.stories.getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryListAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualQuote)

        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(dummyQuote.size, differ.snapshot().size)
        Assert.assertEquals(dummyQuote[0], differ.snapshot()[0])
    }

    @Test
    fun `when Get Story Empty Should Return No Data`() = runTest {
        val data: PagingData<StoryDetail> = PagingData.from(emptyList())
        val expectedQuote = MutableLiveData<PagingData<StoryDetail>>()
        expectedQuote.value = data
        Mockito.`when`(quoteRepository.getStoryPagingSource()).thenReturn(expectedQuote)
        val mainViewModel = HomeViewModel(quoteRepository)
        val actualQuote: PagingData<StoryDetail> = mainViewModel.stories.getOrAwaitValue()
        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryListAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )

        differ.submitData(actualQuote)
        Assert.assertEquals(0, differ.snapshot().size)
    }
}

class QuotePagingSource : PagingSource<Int, LiveData<List<StoryDetail>>>() {
    companion object {
        fun snapshot(items: List<StoryDetail>): PagingData<StoryDetail> {
            return PagingData.from(items)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, LiveData<List<StoryDetail>>>): Int {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<StoryDetail>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }
}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}