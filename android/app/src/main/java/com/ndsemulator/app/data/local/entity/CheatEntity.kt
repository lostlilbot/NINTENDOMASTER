package com.ndsemulator.app.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.ndsemulator.app.domain.model.Cheat

/**
 * Room entity for cheat codes.
 */
@Entity(
    tableName = "cheats",
    foreignKeys = [
        ForeignKey(
            entity = GameEntity::class,
            parentColumns = ["id"],
            childColumns = ["gameId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("gameId")]
)
data class CheatEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val gameId: Long,
    val name: String,
    val code: String,
    val enabled: Boolean = false
) {
    fun toDomain(): Cheat {
        return Cheat(
            id = id,
            gameId = gameId,
            name = name,
            code = code,
            enabled = enabled
        )
    }
    
    companion object {
        fun fromDomain(cheat: Cheat): CheatEntity {
            return CheatEntity(
                id = cheat.id,
                gameId = cheat.gameId,
                name = cheat.name,
                code = cheat.code,
                enabled = cheat.enabled
            )
        }
    }
}
