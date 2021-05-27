package com.syntia.moviecatalogue.core.domain.usecase

import androidx.paging.PagingData
import com.syntia.moviecatalogue.core.domain.model.detail.DetailUiModel
import com.syntia.moviecatalogue.core.domain.model.movie.MovieUiModel
import com.syntia.moviecatalogue.core.domain.model.tvshow.TvShowsUiModel
import com.syntia.moviecatalogue.core.domain.repository.FavoriteItemRepository
import com.syntia.moviecatalogue.core.domain.usecase.interactor.FavoriteItemInteractor
import com.syntia.moviecatalogue.core.helper.BaseTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mock
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class FavoriteItemUseCaseTest : BaseTest() {

  private lateinit var favoriteItemUseCase: FavoriteItemUseCase

  @Mock
  private lateinit var favoriteItemRepository: FavoriteItemRepository

  override fun setUp() {
    super.setUp()
    favoriteItemUseCase = FavoriteItemInteractor(favoriteItemRepository)
  }

  @Test
  fun `Given when getFavoriteMovies then return data from repository`() {
    val expected = mock<PagingData<MovieUiModel>>()

    dispatcher.runBlockingTest {
      whenever(favoriteItemRepository.getFavoriteMovies()) doReturn getFlow(expected)

      val flow = favoriteItemUseCase.getFavoriteMovies()

      flow.collectLatest { actual ->
        verify(favoriteItemRepository).getFavoriteMovies()
        assertEquals(expected, actual)

        verifyNoMoreInteractions(favoriteItemRepository)
      }
    }
  }

  @Test
  fun `Given when getFavoriteTvShows then return data from repository`() {
    val expected = mock<PagingData<TvShowsUiModel>>()

    dispatcher.runBlockingTest {
      whenever(favoriteItemRepository.getFavoriteTvShows()) doReturn getFlow(expected)

      val flow = favoriteItemUseCase.getFavoriteTvShows()

      flow.collectLatest { actual ->
        verify(favoriteItemRepository).getFavoriteTvShows()
        assertEquals(expected, actual)

        verifyNoMoreInteractions(favoriteItemRepository)
      }
    }
  }

  @Test
  fun `Given when getIsMovieExist then return data from repository`() {
    val expected = true

    dispatcher.runBlockingTest {
      whenever(favoriteItemRepository.getIsMovieExist(ID)) doReturn getFlow(expected)

      val flow = favoriteItemUseCase.getIsMovieExist(ID)

      flow.collectLatest { actual ->
        verify(favoriteItemRepository).getIsMovieExist(ID)
        assertEquals(expected, actual)

        verifyNoMoreInteractions(favoriteItemRepository)
      }
    }
  }

  @Test
  fun `Given when getIsTvShowExist then return data from repository`() {
    val expected = false

    dispatcher.runBlockingTest {
      whenever(favoriteItemRepository.getIsTvShowExist(ID)) doReturn getFlow(expected)

      val flow = favoriteItemUseCase.getIsTvShowExist(ID)

      flow.collectLatest { actual ->
        verify(favoriteItemRepository).getIsTvShowExist(ID)
        assertEquals(expected, actual)

        verifyNoMoreInteractions(favoriteItemRepository)
      }
    }
  }

  @Test
  fun `Given when addMovie then verify repository is called`() {
    val expected = mock<DetailUiModel>()

    dispatcher.runBlockingTest {
      whenever(favoriteItemRepository.addMovie(expected)).thenReturn(Unit)

      favoriteItemUseCase.addMovie(expected)

      verify(favoriteItemRepository).addMovie(expected)
      verifyNoMoreInteractions(favoriteItemRepository)
    }
  }

  @Test
  fun `Given when addTvShow then verify repository is called`() {
    val expected = mock<DetailUiModel>()

    dispatcher.runBlockingTest {
      whenever(favoriteItemRepository.addTvShows(expected)).thenReturn(Unit)

      favoriteItemUseCase.addTvShows(expected)

      verify(favoriteItemRepository).addTvShows(expected)
      verifyNoMoreInteractions(favoriteItemRepository)
    }
  }

  @Test
  fun `Given when deleteMovieById then verify repository is called`() {
    dispatcher.runBlockingTest {
      whenever(favoriteItemRepository.deleteMovieById(ID)).thenReturn(Unit)

      favoriteItemUseCase.deleteMovieById(ID)

      verify(favoriteItemRepository).deleteMovieById(ID)
      verifyNoMoreInteractions(favoriteItemRepository)
    }
  }

  @Test
  fun `Given when deleteTvShowsById then verify repository is called`() {
    dispatcher.runBlockingTest {
      whenever(favoriteItemRepository.deleteTvShowsById(ID)).thenReturn(Unit)

      favoriteItemUseCase.deleteTvShowsById(ID)

      verify(favoriteItemRepository).deleteTvShowsById(ID)
      verifyNoMoreInteractions(favoriteItemRepository)
    }
  }
}