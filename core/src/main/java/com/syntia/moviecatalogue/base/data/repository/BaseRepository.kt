package com.syntia.moviecatalogue.base.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.syntia.moviecatalogue.base.data.remote.response.ResponseWrapper
import com.syntia.moviecatalogue.base.data.remote.response.base.ListItemResponse
import com.syntia.moviecatalogue.base.data.source.paging.BasePagingSource
import com.syntia.moviecatalogue.base.data.source.paging.LocalPagingSource
import com.syntia.moviecatalogue.base.data.source.paging.RemotePagingSource
import com.syntia.moviecatalogue.base.domain.model.result.ResultWrapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.single

abstract class BaseRepository {

  abstract val ioDispatcher: CoroutineDispatcher

  protected fun <Response, Result> fetchRemote(
      fetchData: suspend () -> Flow<ResponseWrapper<Response>>,
      mapper: (Response) -> Result): Flow<ResultWrapper<Result>> {
    return flow {
      emit(ResultWrapper.Loading)
      when (val response = fetchData().single()) {
        is ResponseWrapper.Error -> emit(ResultWrapper.Error(response.error?.message))
        is ResponseWrapper.NetworkError -> emit(ResultWrapper.NetworkError)
        is ResponseWrapper.Success -> emit(ResultWrapper.Success(mapper.invoke(response.data)))
      }
    }.flowOn(ioDispatcher)
  }

  protected fun <T : Any, U : Any> createLocalPagingFlow(localFetch: suspend (Int, Int) -> List<T>,
      mapper: (List<T>) -> List<U>): Flow<PagingData<U>> {
    return Pager(
        config = PagingConfig(
            pageSize = BasePagingSource.PAGE_SIZE,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { LocalPagingSource(localFetch, mapper) }).flow.flowOn(ioDispatcher)
  }

  protected fun <T : Any, U : Any> getNetworkPagingFlow(
      apiFetch: suspend (Int) -> ListItemResponse<T>,
      mapper: (ListItemResponse<T>) -> List<U>): Flow<PagingData<U>> {
    return Pager(
        config = PagingConfig(
            pageSize = BasePagingSource.PAGE_SIZE,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { RemotePagingSource(apiFetch, mapper) }).flow.flowOn(ioDispatcher)
  }
}