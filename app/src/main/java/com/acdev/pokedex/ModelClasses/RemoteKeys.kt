package com.acdev.pokedex.ModelClasses

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.acdev.pokedex.Utils.tableNameOfRemoteKeys

@Entity(tableName = tableNameOfRemoteKeys)
data class RemoteKeys(
    @PrimaryKey(autoGenerate = false)
    val id:String,
    val nextOffset: Int,
)
