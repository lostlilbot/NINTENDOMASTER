package com.ndsemulator.app.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.ndsemulator.app.domain.model.SaveState

/**
 * Room entity for save states.
 */
@Entity(
    tableName = "save_states",
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
data class SaveStateEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val gameId: Long,
    val slot: Int,
    val data: ByteArray,
    val timestamp: Long = System.currentTimeMillis(),
    val screenshotPath: String? = null
) {
    fun toDomain(): SaveState {
        return SaveState(
            id = id,
            gameId = gameId,
            slot = slot,
            data = data,
            timestamp = timestamp,
            screenshotPath = screenshotPath
        )
    }
    
    companion object {
        fun fromDomain(saveState: SaveState): SaveStateEntity {
            return SaveStateEntity(
                id = saveState.id,
                gameId = saveState.gameId,
                slot = saveState.slot,
                data = saveState.data,
                timestamp = saveState.timestamp,
                screenshotPath = saveState.screenshotPath
            )
        }
    }
    
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as SaveStateEntity
        return id == other.id && slot == other.slot && timestamp == other.timestamp
    }
    
    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + slot
        result = 31 * result + timestamp.hashCode()
        return result
    }
}
