package com.payback.data.local.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.payback.data.remote.api.API
import com.payback.data.local.Image
import java.io.IOException

class Pagination (
    private val resultString: String,
    private val api: API
        ) : PagingSource <Int , Image> () {


    override fun getRefreshKey(state: PagingState<Int, Image>): Int? {

        return state.anchorPosition

    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Image> {
        val page = params.key ?: 1

        return try {

            val data = api.searchForImage(resultString, params.loadSize, page)
            LoadResult.Page(

                data = data.hits,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (data.hits.isEmpty()) null else page + 1

            )
        } catch (t : Throwable) {
            var exception = t
            if (t is IOException) {

                exception = IOException("check your internet connection and try again. ")
            }

            LoadResult.Error(exception)

        }

    }


}