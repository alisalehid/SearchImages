package com.payback.data.local.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.payback.data.Constants
import com.payback.data.local.Image
import com.payback.data.local.db.LocalDB
import com.payback.data.local.pagination.Pagination
import com.payback.data.remote.api.API
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class SearchImagesRepositoryImpl @Inject constructor(
    private val api: API,
    private val localDB : LocalDB,
) : SearchImagesRepository {

    override fun searchImages(searchString: String): Flow<PagingData<Image>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = Constants.ALL_PAGE),
            pagingSourceFactory = {
                Pagination(searchString, api)
            }
        ).flow
    }

}