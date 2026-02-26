package com.ndsemulator.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ndsemulator.app.domain.model.Game
import com.ndsemulator.app.domain.model.GameFileType
import com.ndsemulator.app.domain.model.GameMetadata

/**
 * Room entity for game data.
 */
@Entity(tableName = "games")
data class GameEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val filePath: String,
    val fileName: String,
    val fileSize: Long,
    val fileType: String,
    val coverArtPath: String? = null,
    val metadataJson: String? = null,
    val lastPlayed: Long? = null,
    val playTime: Long = 0,
    val dateAdded: Long = System.currentTimeMillis(),
    val isFavorite: Boolean = false
) {
    fun toDomain(metadata: GameMetadata? = null): Game {
        return Game(
            id = id,
            title = title,
            filePath = filePath,
            fileName = fileName,
            fileSize = fileSize,
            fileType = GameFileType.valueOf(fileType),
            coverArtPath = coverArtPath,
            metadata = metadata,
            lastPlayed = lastPlayed,
            playTime = playTime,
            dateAdded = dateAdded,
            isFavorite = isFavorite
        )
    }
    
    companion object {
        fun fromDomain(game: Game): GameEntity {
            return GameEntity(
                id = game.id,
                title = game.title,
                filePath = game.filePath,
                fileName = game.fileName,
                fileSize = game.fileSize,
                fileType = game.fileType.name,
                coverArtPath = game.coverArtPath,
                metadataJson = game.metadata?.let {
                    "${it.gameTitle}|${it.gameCode}|${it.makerCode}|${it.cardSize}|${it.isDsiEnhanced}|${it.isDSiExclusive}|${it.isHomebrew}"
                },
                lastPlayed = game.lastPlayed,
                playTime = game.playTime,
                dateAdded = game.dateAdded,
                isFavorite = game.isFavorite
            )
        }
    }
}
