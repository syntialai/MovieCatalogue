package com.syntia.moviecatalogue.base.data.source.paging

import com.syntia.moviecatalogue.base.data.remote.response.base.ListItemResponse

class RemotePagingSource<T : Any, U : Any>(
    private val fetchData: suspend (Int) -> ListItemResponse<T>,
    private val mapper: (ListItemResponse<T>) -> List<U>) : BasePagingSource<T, U>() {

  override val initialPage: Int = 1

  override suspend fun getResult(page: Int): List<U> {
    val response = fetchData.invoke(page)
    return mapper.invoke(response)
  }
}