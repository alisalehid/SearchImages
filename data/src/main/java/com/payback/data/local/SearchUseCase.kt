package com.payback.data.local

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import com.payback.data.local.repository.SearchImagesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchUseCase @Inject constructor(private val searchImagesRepository: SearchImagesRepository){

    @OptIn(ExperimentalPagingApi::class)
    suspend fun invoke(load: String): Flow<PagingData<Image>>{

        return searchImagesRepository.searchImages(load)
    }
}