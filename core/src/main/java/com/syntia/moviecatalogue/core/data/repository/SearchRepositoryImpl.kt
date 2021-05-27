package com.syntia.moviecatalogue.core.data.repository

import androidx.paging.PagingData
import com.syntia.moviecatalogue.base.data.remote.response.base.ListItemResponse
import com.syntia.moviecatalogue.base.data.repository.BaseRepository
import com.syntia.moviecatalogue.core.data.source.remote.datasource.SearchRemoteDataSource
import com.syntia.moviecatalogue.core.data.source.remote.response.search.SearchResult
import com.syntia.moviecatalogue.core.domain.model.search.SearchResultUiModel
import com.syntia.moviecatalogue.core.domain.repository.SearchRepository
import com.syntia.moviecatalogue.core.utils.mapper.SearchMapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

class SearchRepositoryImpl(private val searchRemoteDataSource: SearchRemoteDataSource,
    override val ioDispatcher: CoroutineDispatcher) : SearchRepository, BaseRepository() {

  override suspend fun searchByQuery(query: String): Flow<PagingData<SearchResultUiModel>> {
    val searchLambda: (suspend (Int) -> ListItemResponse<SearchResult>) = { page: Int ->
      searchRemoteDataSource.searchByQuery(page, query)
    }
    return getNetworkPagingFlow(searchLambda, SearchMapper::toSearchResultUiModels)
  }
}