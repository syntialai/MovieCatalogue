package com.syntia.moviecatalogue.core.domain.usecase

import androidx.paging.PagingData
import com.syntia.moviecatalogue.core.domain.model.search.SearchResultUiModel
import com.syntia.moviecatalogue.core.domain.repository.SearchRepository
import com.syntia.moviecatalogue.core.domain.usecase.interactor.SearchInteractor
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
class SearchUseCaseTest : BaseTest() {

  private lateinit var searchUseCase: SearchUseCase

  @Mock
  private lateinit var searchRepository: SearchRepository

  override fun setUp() {
    super.setUp()
    searchUseCase = SearchInteractor(searchRepository)
  }

  @Test
  fun `Given when searchByQuery then return data from repository`() {
    val expected = mock<PagingData<SearchResultUiModel>>()

    dispatcher.runBlockingTest {
      whenever(searchRepository.searchByQuery(QUERY)) doReturn getFlow(expected)

      val flow = searchUseCase.searchByQuery(QUERY)

      flow.collectLatest { actual ->
        verify(searchRepository).searchByQuery(QUERY)
        Assert.assertEquals(expected, actual)

        verifyNoMoreInteractions(searchRepository)
      }
    }
  }
}