package com.syntia.moviecatalogue.core.domain.usecase.interactor

import com.syntia.moviecatalogue.base.domain.model.result.ResultWrapper
import com.syntia.moviecatalogue.core.domain.model.detail.CastUiModel
import com.syntia.moviecatalogue.core.domain.model.detail.DetailUiModel
import com.syntia.moviecatalogue.core.domain.repository.DetailRepository
import com.syntia.moviecatalogue.core.domain.usecase.DetailUseCase
import kotlinx.coroutines.flow.Flow

class DetailInteractor(private val detailRepository: DetailRepository) : DetailUseCase {

  override suspend fun getDetails(mediaType: String,
      id: Int): Flow<ResultWrapper<DetailUiModel>> = detailRepository.getDetails(mediaType, id)

  override suspend fun getDetailCasts(mediaType: String,
      id: Int): Flow<ResultWrapper<MutableList<CastUiModel>>> = detailRepository.getDetailCasts(
      mediaType, id)
}