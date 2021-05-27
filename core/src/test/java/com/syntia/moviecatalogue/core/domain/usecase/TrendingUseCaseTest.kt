package com.syntia.moviecatalogue.core.domain.usecase

import androidx.paging.PagingData
import com.syntia.moviecatalogue.base.domain.model.result.ResultWrapper
import com.syntia.moviecatalogue.core.domain.model.movie.MovieUiModel
import com.syntia.moviecatalogue.core.domain.model.trending.TrendingItemUiModel
import com.syntia.moviecatalogue.core.domain.model.tvshow.TvShowsUiModel
import com.syntia.moviecatalogue.core.domain.repository.TrendingRepository
import com.syntia.moviecatalogue.core.domain.usecase.interactor.TrendingInteractor
import com.syntia.moviecatalogue.core.helper.BaseTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Test
import org.mockito.Mock
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class TrendingUseCaseTest : BaseTest() {

  private lateinit var trendingUseCase: TrendingUseCase

  @Mock
  private lateinit var trendingRepository: TrendingRepository

  override fun setUp() {
    super.setUp()
    trendingUseCase = TrendingInteractor(trendingRepository)
  }

  @Test
  fun `Given when getTrendingItems then return data from repository`() {
    val expected = mock<List<TrendingItemUiModel>>()

    dispatcher.runBlockingTest {
      whenever(trendingRepository.getTrendingItems()) doReturn getFlow(ResultWrapper.Success(expected))

      val flow = trendingUseCase.getTrendingItems()

      flow.collectLatest { actual ->
        verify(trendingRepository).getTrendingItems()
        Assert.assertEquals(expected, (actual as ResultWrapper.Success).data)

        verifyNoMoreInteractions(trendingRepository)
      }
    }
  }

  @Test
  fun `Given when getPopularMovies then return data from repository`() {
    val expected = mock<PagingData<MovieUiModel>>()

    dispatcher.runBlockingTest {
      whenever(trendingRepository.getPopularMovies()) doReturn getFlow(expected)

      val flow = trendingUseCase.getPopularMovies()

      flow.collectLatest { actual ->
        verify(trendingRepository).getPopularMovies()
        Assert.assertEquals(expected, actual)

        verifyNoMoreInteractions(trendingRepository)
      }
    }
  }

  @Test
  fun `Given when getPopularTvShows then return data from repository`() {
    val expected = mock<PagingData<TvShowsUiModel>>()

    dispatcher.runBlockingTest {
      whenever(trendingRepository.getPopularTvShows()) doReturn getFlow(expected)

      val flow = trendingUseCase.getPopularTvShows()

      flow.collectLatest { actual ->
        verify(trendingRepository).getPopularTvShows()
        Assert.assertEquals(expected, actual)

        verifyNoMoreInteractions(trendingRepository)
      }
    }
  }
}