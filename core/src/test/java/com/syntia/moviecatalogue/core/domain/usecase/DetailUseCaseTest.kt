package com.syntia.moviecatalogue.core.domain.usecase

import com.syntia.moviecatalogue.base.domain.model.result.ResultWrapper
import com.syntia.moviecatalogue.core.domain.model.detail.CastUiModel
import com.syntia.moviecatalogue.core.domain.model.detail.DetailUiModel
import com.syntia.moviecatalogue.core.domain.repository.DetailRepository
import com.syntia.moviecatalogue.core.domain.usecase.interactor.DetailInteractor
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
class DetailUseCaseTest : BaseTest() {

  private lateinit var detailUseCase: DetailUseCase

  @Mock
  private lateinit var detailRepository: DetailRepository

  override fun setUp() {
    super.setUp()
    detailUseCase = DetailInteractor(detailRepository)
  }

  @Test
  fun `Given when getDetails then return data from repository`() {
    val expected = mock<DetailUiModel>()

    dispatcher.runBlockingTest {
      whenever(detailRepository.getDetails(MEDIA_TYPE, ID)) doReturn getFlow(
          ResultWrapper.Success(expected))

      val flow = detailUseCase.getDetails(MEDIA_TYPE, ID)

      flow.collectLatest { actual ->
        verify(detailRepository).getDetails(MEDIA_TYPE, ID)
        Assert.assertEquals(expected, (actual as ResultWrapper.Success).data)

        verifyNoMoreInteractions(detailRepository)
      }
    }
  }

  @Test
  fun `Given when getDetailCasts then return data from repository`() {
    val expected = mock<MutableList<CastUiModel>>()

    dispatcher.runBlockingTest {
      whenever(detailRepository.getDetailCasts(MEDIA_TYPE, ID)) doReturn getFlow(
          ResultWrapper.Success(expected))

      val flow = detailUseCase.getDetailCasts(MEDIA_TYPE, ID)

      flow.collectLatest { actual ->
        verify(detailRepository).getDetailCasts(MEDIA_TYPE, ID)
        Assert.assertEquals(expected, (actual as ResultWrapper.Success).data)

        verifyNoMoreInteractions(detailRepository)
      }
    }
  }
}