package com.syntia.moviecatalogue.features.home

import com.syntia.moviecatalogue.core.domain.model.tvshow.TvShowsUiModel
import com.syntia.moviecatalogue.core.domain.usecase.TrendingUseCase
import com.syntia.moviecatalogue.core.helper.BaseViewModelTest
import com.syntia.moviecatalogue.features.home.adapter.HomeTvShowsAdapter
import com.syntia.moviecatalogue.features.home.viewmodel.HomeTvShowsViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class HomeTvShowsViewModelTest : BaseViewModelTest() {

  private lateinit var viewModel: HomeTvShowsViewModel

  private val trendingUseCase = mock<TrendingUseCase>()

  override fun setUp() {
    super.setUp()
    viewModel = HomeTvShowsViewModel(trendingUseCase)
  }

  @Test
  fun `Given when fetch popular tv shows then success update live data`() {
    val data = listOf(TvShowsUiModel(
        id = ID,
        image = IMAGE,
        releasedYear = YEAR,
        title = NAME,
        voteAverage = VOTE_AVERAGE_STRING
    ))
    val flow = data.getFakePagingData()
    val differ = getAsyncPagingDataDiffer(HomeTvShowsAdapter.diffCallback)

    rule.dispatcher.runBlockingTest {
      val job = launch {
        flow.collectLatest { data ->
          differ.submitData(data)
        }
      }

      whenever(trendingUseCase.getPopularTvShows()) doReturn flow

      viewModel.fetchTvShows()
      verify(trendingUseCase).getPopularTvShows()
      advanceUntilIdle()

      Assert.assertTrue(differ.snapshot().contains(data[0]))
      Assert.assertNotNull(viewModel.tvShows.value)

      verifyNoMoreInteractions(trendingUseCase)
      job.cancel()
    }
  }
}