package com.syntia.moviecatalogue.core.domain.usecase

import androidx.paging.PagingData
import com.syntia.moviecatalogue.core.domain.model.search.SearchResultUiModel
import kotlinx.coroutines.flow.Flow

interface SearchUseCase {

  suspend fun searchByQuery(query: String): Flow<PagingData<SearchResultUiModel>>
}