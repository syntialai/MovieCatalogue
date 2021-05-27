package com.syntia.moviecatalogue.core.data.source.remote.datasource.impl

import com.syntia.moviecatalogue.base.data.remote.response.ResponseWrapper
import com.syntia.moviecatalogue.base.data.source.datasource.BaseRemoteDataSource
import com.syntia.moviecatalogue.core.data.source.remote.datasource.DetailRemoteDataSource
import com.syntia.moviecatalogue.core.data.source.remote.response.detail.Detail
import com.syntia.moviecatalogue.core.data.source.remote.response.detail.MediaCredits
import com.syntia.moviecatalogue.core.data.source.remote.service.DetailService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

class DetailRemoteDataSourceImpl(private val detailService: DetailService,
    override val ioDispatcher: CoroutineDispatcher) : DetailRemoteDataSource, BaseRemoteDataSource() {

  override suspend fun getMovieDetails(id: Int): Flow<ResponseWrapper<Detail>> {
    return createFlow {
      detailService.getMovieDetails(id)
    }
  }

  override suspend fun getTvDetails(id: Int): Flow<ResponseWrapper<Detail>> {
    return createFlow {
      detailService.getTvDetails(id)
    }
  }

  override suspend fun getMovieCredits(id: Int): Flow<ResponseWrapper<MediaCredits>> {
    return createFlow {
      detailService.getMovieCredits(id)
    }
  }

  override suspend fun getTvCredits(id: Int): Flow<ResponseWrapper<MediaCredits>> {
    return createFlow {
      detailService.getTvCredits(id)
    }
  }
}