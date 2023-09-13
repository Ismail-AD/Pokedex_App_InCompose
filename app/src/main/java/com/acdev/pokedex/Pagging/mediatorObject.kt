package com.acdev.pokedex.Pagging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.acdev.pokedex.ApiCalls.PokeApi
import com.acdev.pokedex.Mapper.toPokeList
import com.acdev.pokedex.ModelClasses.PokedexListEntry
import com.acdev.pokedex.ModelClasses.RemoteKeys
import com.acdev.pokedex.ModelClasses.RoomDB
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class mediatorObject @Inject constructor(private val roomDB: RoomDB, private val pokeApi: PokeApi) :
    RemoteMediator<Int, PokedexListEntry>() {
    private val REMOTE_KEY_ID = "pokemon"
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PokedexListEntry>,
    ): MediatorResult {
        return try {
            val currentPage = when (loadType) {
                LoadType.REFRESH -> 0
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val remoteKey = roomDB.remoteKeysDao().getRemoteKey(REMOTE_KEY_ID)
                    if (remoteKey == null || remoteKey.nextOffset == 0)
                        return MediatorResult.Success(endOfPaginationReached = true)
                    remoteKey.nextOffset
                }
            }
            val apiCallResult =
                pokeApi.getPokemonList(limit = state.config.pageSize, offset = currentPage)
            val resList = apiCallResult.results
            val nextOffset = extractOffsetValue(apiCallResult.next) ?: 0
            roomDB.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    roomDB.pokeListDao().clearAll()
                    roomDB.remoteKeysDao().deleteById(REMOTE_KEY_ID)
                }
                roomDB.pokeListDao().insertDataIntoDB(resList.toPokeList())
                roomDB.remoteKeysDao().insertKeysIntoDB(RemoteKeys(id = REMOTE_KEY_ID,nextOffset = nextOffset))
            }
            MediatorResult.Success(endOfPaginationReached = resList.size < state.config.pageSize)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

    private fun extractOffsetValue(urlString: String): Int? {
        val regex = Regex("offset=(\\d+)")
        val matchResult = regex.find(urlString)
        return matchResult?.groupValues?.get(1)?.toIntOrNull()
    }
}