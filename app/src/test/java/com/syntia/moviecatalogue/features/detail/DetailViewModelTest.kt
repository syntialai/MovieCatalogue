package com.syntia.moviecatalogue.features.detail

import androidx.lifecycle.MutableLiveData
import com.syntia.moviecatalogue.base.domain.model.result.ResultWrapper
import com.syntia.moviecatalogue.core.domain.model.detail.CastUiModel
import com.syntia.moviecatalogue.core.domain.model.detail.DetailUiModel
import com.syntia.moviecatalogue.core.domain.usecase.DetailUseCase
import com.syntia.moviecatalogue.core.domain.usecase.FavoriteItemUseCase
import com.syntia.moviecatalogue.features.detail.viewmodel.DetailViewModel
import com.syntia.moviecatalogue.helper.BaseViewModelTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class DetailViewModelTest : BaseViewModelTest() {

  private lateinit var viewModel: DetailViewModel

  private val detailUseCase = mock<DetailUseCase>()

  private val favoriteItemUseCase = mock<FavoriteItemUseCase>()

  private val idCaptor = argumentCaptor<Int>()

  private val typeCaptor = argumentCaptor<String>()

  private val detailUiModelCaptor = argumentCaptor<DetailUiModel>()

  override fun setUp() {
    super.setUp()
    viewModel = DetailViewModel(detailUseCase, favoriteItemUseCase)
  }

  @Test
  fun `Given when fetchDetails and type is movie then success update live data`() {
    val expected = DetailUiModel(
        id = ID,
        image = IMAGE,
        title = TITLE,
        releaseOrFirstAirDate = DATE,
        episodeCount = 10,
        genres = GENRES_UI_MODEL,
        language = LANGUAGE,
        overview = null,
        rating = Pair(VOTE_AVERAGE_STRING, VOTE_AVERAGE_DIVIDED),
        runtime = null
    )
    val response = getFlow(ResultWrapper.Success(expected)).flowOn(dispatcher)

    rule.dispatcher.runBlockingTest {
      whenever(detailUseCase.getDetails(any(), any())) doReturn response

      viewModel.setIdAndType(ID, MEDIA_TYPE_MOVIE)
      viewModel.fetchDetails()

      verify(detailUseCase).getDetails(typeCaptor.capture(), idCaptor.capture())
      assertCaptors(MEDIA_TYPE_MOVIE)
      delay(1000)

      viewModel.details.observeForTesting { actual ->
        assertEquals(expected, actual)
      }

      verifyNoMoreInteractions(detailUseCase)
    }
  }

  @Test
  fun `Given when fetchDetails and type is tv then success update live data`() {
    val expected = DetailUiModel(
        id = ID,
        image = IMAGE,
        title = NAME,
        releaseOrFirstAirDate = DATE,
        episodeCount = 10,
        genres = GENRES_UI_MODEL,
        language = LANGUAGE,
        overview = null,
        rating = Pair(VOTE_AVERAGE_STRING, VOTE_AVERAGE_DIVIDED),
        runtime = null
    )
    val response = getFlow(ResultWrapper.Success(expected)).flowOn(dispatcher)

    rule.dispatcher.runBlockingTest {
      whenever(detailUseCase.getDetails(any(), any())) doReturn response

      viewModel.setIdAndType(ID, MEDIA_TYPE_TV)
      viewModel.fetchDetails()

      verify(detailUseCase).getDetails(typeCaptor.capture(), idCaptor.capture())
      assertCaptors(MEDIA_TYPE_TV)
      delay(1000)

      viewModel.details.observeForTesting { actual ->
        assertEquals(expected, actual)
      }

      verifyNoMoreInteractions(detailUseCase)
    }
  }

  @Test
  fun `Given when fetchCasts then success update live data`() {
    val uiModel = mutableListOf(CastUiModel(
        id = CAST_ID,
        character = CAST_CHARACTER,
        name = CAST_NAME,
        image = CAST_IMAGE
    ))
    val expected = ResultWrapper.Success(uiModel)
    val response = getFlow(expected, true).flowOn(dispatcher)

    rule.dispatcher.runBlockingTest {
      whenever(detailUseCase.getDetailCasts(any(), any())) doReturn response

      viewModel.setIdAndType(ID, MEDIA_TYPE_MOVIE)
      viewModel.fetchCasts()

      verify(detailUseCase).getDetailCasts(typeCaptor.capture(), idCaptor.capture())
      assertCaptors(MEDIA_TYPE_MOVIE)
      delay(5000)

      viewModel.casts.observeForTesting { data ->
        assertEquals(uiModel, data)
      }

      verifyNoMoreInteractions(detailUseCase)
    }
  }

  @Test
  fun `Given when fetchCasts and got error then update loading state`() {
    val response = getFlow(ResultWrapper.Error(""), true).flowOn(dispatcher)

    rule.dispatcher.runBlockingTest {
      whenever(detailUseCase.getDetailCasts(any(), any())) doReturn response

      viewModel.setIdAndType(ID, MEDIA_TYPE_MOVIE)
      viewModel.fetchCasts()

      verify(detailUseCase).getDetailCasts(typeCaptor.capture(), idCaptor.capture())
      assertCaptors(MEDIA_TYPE_MOVIE)
      delay(1000)

      verifyNoMoreInteractions(detailUseCase)
    }
  }

  @Test
  fun `Given when getIsFavoriteItem and media type is movie then success update live data`() {
    val expected = true

    rule.dispatcher.runBlockingTest {
      whenever(favoriteItemUseCase.getIsMovieExist(any())) doReturn getFlow(expected,
          true).flowOn(dispatcher)

      viewModel.setIdAndType(ID, MEDIA_TYPE_MOVIE)
      viewModel.getIsFavoriteItem()

      verify(favoriteItemUseCase).getIsMovieExist(idCaptor.capture())
      assertIdCaptor()
      delay(1000)

      assertIsFavoriteItem(expected)

      verifyNoMoreInteractions(favoriteItemUseCase)
    }
  }

  @Test
  fun `Given when getIsFavoriteItem and media type is tv then success update live data`() {
    val expected = false

    rule.dispatcher.runBlockingTest {
      whenever(favoriteItemUseCase.getIsTvShowExist(any())) doReturn getFlow(expected,
          true).flowOn(dispatcher)

      viewModel.setIdAndType(ID, MEDIA_TYPE_TV)
      viewModel.getIsFavoriteItem()

      verify(favoriteItemUseCase).getIsTvShowExist(idCaptor.capture())
      assertIdCaptor()
      delay(1000)

      assertIsFavoriteItem(expected)

      verifyNoMoreInteractions(favoriteItemUseCase)
    }
  }

  @Test
  fun `Given when updateFavoriteItem and is added is false and media type is movie then success update live data`() {
    val expected = DetailUiModel(
        id = ID,
        image = IMAGE,
        title = TITLE,
        releaseOrFirstAirDate = DATE,
        episodeCount = 10,
        genres = GENRES_UI_MODEL,
        language = LANGUAGE,
        overview = null,
        rating = Pair(VOTE_AVERAGE_STRING, VOTE_AVERAGE_DIVIDED),
        runtime = null
    )
    viewModel.setPrivateField("_details", MutableLiveData(expected))

    rule.dispatcher.runBlockingTest {
      whenever(favoriteItemUseCase.addMovie(any())).thenReturn(Unit)

      viewModel.setIdAndType(ID, MEDIA_TYPE_MOVIE)
      viewModel.updateFavoriteItem()

      verify(favoriteItemUseCase).addMovie(detailUiModelCaptor.capture())
      assertEquals(expected, detailUiModelCaptor.firstValue)
      assertIsFavoriteItem(true)
    }
  }

  @Test
  fun `Given when updateFavoriteItem and is added is false and media type is tv then success update live data`() {
    val expected = DetailUiModel(
        id = ID,
        image = IMAGE,
        title = TITLE,
        releaseOrFirstAirDate = DATE,
        episodeCount = 10,
        genres = GENRES_UI_MODEL,
        language = LANGUAGE,
        overview = null,
        rating = Pair(VOTE_AVERAGE_STRING, VOTE_AVERAGE_DIVIDED),
        runtime = null
    )
    viewModel.setPrivateField("_details", MutableLiveData(expected))

    rule.dispatcher.runBlockingTest {
      whenever(favoriteItemUseCase.addTvShows(any())).thenReturn(Unit)

      viewModel.setIdAndType(ID, MEDIA_TYPE_TV)
      viewModel.updateFavoriteItem()

      verify(favoriteItemUseCase).addTvShows(detailUiModelCaptor.capture())
      assertEquals(expected, detailUiModelCaptor.firstValue)
      assertIsFavoriteItem(true)
    }
  }

  @Test
  fun `Given when updateFavoriteItem and is added is true and media type is movie then success update live data`() {
    val expected = DetailUiModel(
        id = ID,
        image = IMAGE,
        title = TITLE,
        releaseOrFirstAirDate = DATE,
        episodeCount = 10,
        genres = GENRES_UI_MODEL,
        language = LANGUAGE,
        overview = null,
        rating = Pair(VOTE_AVERAGE_STRING, VOTE_AVERAGE_DIVIDED),
        runtime = null
    )
    viewModel.setPrivateField("_details", MutableLiveData(expected))
    viewModel.setPrivateField("_isFavoriteItem", MutableLiveData(true))

    rule.dispatcher.runBlockingTest {
      whenever(favoriteItemUseCase.deleteMovieById(any())).thenReturn(Unit)

      viewModel.setIdAndType(ID, MEDIA_TYPE_MOVIE)
      viewModel.updateFavoriteItem()

      verify(favoriteItemUseCase).deleteMovieById(idCaptor.capture())
      assertIdCaptor()
      assertIsFavoriteItem(false)
    }
  }

  @Test
  fun `Given when updateFavoriteItem and is added is true and media type is tv then success update live data`() {
    val expected = DetailUiModel(
        id = ID,
        image = IMAGE,
        title = TITLE,
        releaseOrFirstAirDate = DATE,
        episodeCount = 10,
        genres = GENRES_UI_MODEL,
        language = LANGUAGE,
        overview = null,
        rating = Pair(VOTE_AVERAGE_STRING, VOTE_AVERAGE_DIVIDED),
        runtime = null
    )
    viewModel.setPrivateField("_details", MutableLiveData(expected))
    viewModel.setPrivateField("_isFavoriteItem", MutableLiveData(true))

    rule.dispatcher.runBlockingTest {
      whenever(favoriteItemUseCase.deleteTvShowsById(any())).thenReturn(Unit)

      viewModel.setIdAndType(ID, MEDIA_TYPE_TV)
      viewModel.updateFavoriteItem()

      verify(favoriteItemUseCase).deleteTvShowsById(idCaptor.capture())
      assertIdCaptor()
      assertIsFavoriteItem(false)
    }
  }

  private fun assertCaptors(type: String) {
    assertEquals(type, typeCaptor.firstValue)
    assertIdCaptor()
  }

  private fun assertIdCaptor() {
    assertEquals(ID, idCaptor.firstValue)
  }

  private fun assertIsFavoriteItem(expected: Boolean) {
    viewModel.isFavoriteItem.observeForTesting { actual ->
      assertEquals(expected, actual)
    }
  }
}