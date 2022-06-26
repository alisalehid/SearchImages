package com.payback.data.local.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import com.payback.data.local.Image
import kotlinx.coroutines.flow.Flow

interface SearchImagesRepository {
    @ExperimentalPagingApi
    fun searchImages(searchString: String): Flow<PagingData<Image>>
}