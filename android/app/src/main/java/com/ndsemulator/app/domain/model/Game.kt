package com.ndsemulator.app.domain.model

/**
 * Represents a Nintendo DS game in the library.
 */
data class Game(
    val id: Long = 0,
    val title: String,
    val filePath: String,
    val fileName: String,
    val fileSize: Long,
    val fileType: GameFileType,
    val coverArtPath: String? = null,
    val metadata: GameMetadata? = null,
    val lastPlayed: Long? = null,
    val playTime: Long = 0,
    val dateAdded: Long = System.currentTimeMillis(),
    val isFavorite: Boolean = false
)

/**
 * Supported game file types.
 */
enum class GameFileType {
    NDS,    // .nds files
    ZIP,    // .zip archives
    SEVEN_ZIP, // .7z archives
    UNKNOWN
}

/**
 * Game metadata from ROM header.
 */
data class GameMetadata(
    val gameTitle: String,
    val gameCode: String,
    val makerCode: String,
    val cardSize: Int,
    val isDsiEnhanced: Boolean = false,
    val isDSiExclusive: Boolean = false,
    val isHomebrew: Boolean = false
)

/**
 * Game save data.
 */
data class GameSave(
    val id: Long = 0,
    val gameId: Long,
    val slot: Int,
    val data: ByteArray,
    val timestamp: Long = System.currentTimeMillis()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as GameSave
        return id == other.id && slot == other.slot && timestamp == other.timestamp
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + slot
        result = 31 * result + timestamp.hashCode()
        return result
    }
}

/**
 * Save state for quick saves.
 */
data class SaveState(
    val id: Long = 0,
    val gameId: Long,
    val slot: Int,
    val data: ByteArray,
    val timestamp: Long = System.currentTimeMillis(),
    val screenshotPath: String? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as SaveState
        return id == other.id && slot == other.slot && timestamp == other.timestamp
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + slot
        result = 31 * result + timestamp.hashCode()
        return result
    }
}

/**
 * Cheat code for a game.
 */
data class Cheat(
    val id: Long = 0,
    val gameId: Long,
    val name: String,
    val code: String,
    val enabled: Boolean = false
)
