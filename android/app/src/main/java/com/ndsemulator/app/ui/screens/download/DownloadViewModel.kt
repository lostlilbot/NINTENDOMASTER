package com.ndsemulator.app.ui.screens.download

import android.os.Environment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ndsemulator.app.domain.repository.GameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

/**
 * ViewModel for the Download screen.
 */
@HiltViewModel
class DownloadViewModel @Inject constructor(
    private val gameRepository: GameRepository
) : ViewModel() {
    
    private val _url = MutableStateFlow("")
    val url: StateFlow<String> = _url.asStateFlow()
    
    private val _isDownloading = MutableStateFlow(false)
    val isDownloading: StateFlow<Boolean> = _isDownloading.asStateFlow()
    
    private val _progress = MutableStateFlow(0)
    val progress: StateFlow<Int> = _progress.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    private val _downloadComplete = MutableStateFlow(false)
    val downloadComplete: StateFlow<Boolean> = _downloadComplete.asStateFlow()
    
    private val client = OkHttpClient()
    
    fun updateUrl(newUrl: String) {
        _url.value = newUrl
        _error.value = null
    }
    
    fun downloadRom() {
        val urlValue = _url.value
        if (urlValue.isBlank()) {
            _error.value = "Please enter a URL"
            return
        }
        
        viewModelScope.launch {
            _isDownloading.value = true
            _progress.value = 0
            _error.value = null
            _downloadComplete.value = false
            
            try {
                withContext(Dispatchers.IO) {
                    val request = Request.Builder()
                        .url(urlValue)
                        .build()
                    
                    val response = client.newCall(request).execute()
                    
                    if (!response.isSuccessful) {
                        throw Exception("Download failed: ${response.code}")
                    }
                    
                    val body = response.body ?: throw Exception("Empty response")
                    val contentLength = body.contentLength()
                    
                    // Get filename from URL or Content-Disposition header
                    val fileName = extractFileName(urlValue, response.header("Content-Disposition"))
                    val downloadDir = Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DOWNLOADS
                    )
                    
                    if (!downloadDir.exists()) {
                        downloadDir.mkdirs()
                    }
                    
                    val outputFile = File(downloadDir, fileName)
                    
                    // Download with progress
                    body.byteStream().use { input ->
                        FileOutputStream(outputFile).use { output ->
                            val buffer = ByteArray(8192)
                            var bytesRead: Int
                            var totalBytesRead = 0L
                            
                            while (input.read(buffer).also { bytesRead = it } != -1) {
                                output.write(buffer, 0, bytesRead)
                                totalBytesRead += bytesRead
                                
                                if (contentLength > 0) {
                                    _progress.value = ((totalBytesRead * 100) / contentLength).toInt()
                                }
                            }
                        }
                    }
                    
                    // Add to game library
                    gameRepository.importRom(outputFile.absolutePath)
                }
                
                _downloadComplete.value = true
            } catch (e: Exception) {
                _error.value = e.message ?: "Download failed"
            } finally {
                _isDownloading.value = false
            }
        }
    }
    
    private fun extractFileName(url: String, contentDisposition: String?): String {
        // Try to get filename from Content-Disposition header
        contentDisposition?.let { cd ->
            val parts = cd.split("filename=")
            if (parts.size > 1) {
                val filename = parts[1].trim().removeSurrounding("\"")
                if (filename.isNotBlank()) {
                    return filename
                }
            }
        }
        
        // Fall back to extracting from URL
        return url.substringAfterLast("/").takeIf { it.isNotBlank() } 
            ?: "download_${System.currentTimeMillis()}.nds"
    }
}
