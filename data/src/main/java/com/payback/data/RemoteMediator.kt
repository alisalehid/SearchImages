package com.payback.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.payback.data.local.Image
import com.payback.data.local.db.LocalDB
import com.payback.data.local.db.RemoteKey
import com.payback.data.remote.api.API
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class RemoteMediator(
    private val api: API,
    private val search: String,
    private val database: LocalDB
) : RemoteMediator<Int, Image>()
{
    override suspend fun load(loadType: LoadType, state: PagingState<Int, Image>): MediatorResult {



        val page: Int = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextPage?.minus(1) ?: 1
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.previousPage
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextPage
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }

        try {
            val response = api.searchForImage(search, state.config.pageSize, page)
            val images = response.hits

            images.map {
                it.searchTerm = search
            }

            val endOfPaginationReached = images.isEmpty()
            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.imageDao().clearAll()
                    database.remoteKeyDao().clearRemoteKeys()
                }
                val prevKey = if (page == 1 ) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1

                val keys = images.map {
                    RemoteKey(previousPage = prevKey, nextPage = nextKey, imageId = it.id)
                }
                database.remoteKeyDao().insertAll(keys)
                database.imageDao().insertAll(images)
            }

            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }

    }

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Image>): RemoteKey? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { image ->
                database.remoteKeyDao().remoteKeysImageId(image.id)
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Image>): RemoteKey? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { image ->
                database.remoteKeyDao().remoteKeysImageId(image.id)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, Image>
    ): RemoteKey? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.let { image ->
                database.remoteKeyDao().remoteKeysImageId(image.id)
            }
        }
    }
}