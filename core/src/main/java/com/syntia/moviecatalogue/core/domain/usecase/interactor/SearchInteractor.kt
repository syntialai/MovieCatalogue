package com.syntia.moviecatalogue.core.domain.usecase.interactor

import androidx.paging.PagingData
import com.syntia.moviecatalogue.core.domain.model.search.SearchResultUiModel
import com.syntia.moviecatalogue.core.domain.repository.SearchRepository
import com.syntia.moviecatalogue.core.domain.usecase.SearchUseCase
import kotlinx.coroutines.flow.Flow

class SearchInteractor(private val searchRepository: SearchRepository) : SearchUseCase {

  override suspend fun searchByQuery(
      query: String): Flow<PagingData<SearchResultUiModel>> = searchRepository.searchByQuery(query)
}