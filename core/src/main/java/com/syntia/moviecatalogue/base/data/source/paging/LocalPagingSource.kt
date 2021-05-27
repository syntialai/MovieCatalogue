package com.syntia.moviecatalogue.base.data.source.paging

class LocalPagingSource<T : Any, U : Any>(
    private val fetchData: suspend (Int, Int) -> List<T>,
    private val mapper: (List<T>) -> List<U>) : BasePagingSource<T, U>() {

  override val initialPage: Int = 0

  override suspend fun getResult(page: Int): List<U> {
    val response = fetchData.invoke(page, PAGE_SIZE)
    return mapper.invoke(response)
  }
}
