package com.syntia.moviecatalogue.base.data.source.datasource

import com.google.gson.Gson
import com.syntia.moviecatalogue.base.data.remote.response.ResponseWrapper
import com.syntia.moviecatalogue.base.data.remote.response.error.ErrorResponse
import java.io.IOException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException

abstract class BaseRemoteDataSource {

  abstract val ioDispatcher: CoroutineDispatcher

  fun <T> createFlow(dataFetch: suspend () -> T): Flow<ResponseWrapper<T>> {
    return flow {
      try {
        emit(ResponseWrapper.Success(dataFetch.invoke()))
      } catch (throwable: Throwable) {
        emit(when (throwable) {
          is IOException -> ResponseWrapper.NetworkError
          is HttpException -> getErrorResponseWrapper(throwable)
          else -> ResponseWrapper.Error()
        })
      }
    }.flowOn(ioDispatcher)
  }

  private fun getErrorResponseWrapper(exception: HttpException): ResponseWrapper.Error {
    return ResponseWrapper.Error(exception.code(), getErrorResponse(exception))
  }

  private fun getErrorResponse(exception: HttpException) = try {
    Gson().fromJson(exception.response()?.errorBody()?.charStream(), ErrorResponse::class.java)
  } catch (ex: Exception) {
    null
  }
}