package com.acdev.pokedex.DaoInterfaces

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.acdev.pokedex.ModelClasses.PokedexListEntry
import com.acdev.pokedex.ModelClasses.RemoteKeys

@Dao
interface RemoteKeysDao {
    @Query("SELECT * FROM KEYS_TABLE WHERE id=:id")
    suspend fun getRemoteKey(id:String): RemoteKeys?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKeysIntoDB(remoteKey: RemoteKeys)

    @Query("DELETE FROM KEYS_TABLE WHERE id = :id")
    suspend fun deleteById(id: String)
}