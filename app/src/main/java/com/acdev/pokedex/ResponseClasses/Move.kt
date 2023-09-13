package com.acdev.pokedex.ResponseClasses

data class Move(
    val move: MoveX,
    val version_group_details: List<VersionGroupDetail>
)