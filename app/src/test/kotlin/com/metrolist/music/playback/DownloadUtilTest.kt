package com.metrolist.music.playback

import androidx.media3.exoplayer.offline.Download
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import java.io.File
import java.nio.file.Files

@RunWith(RobolectricTestRunner::class)
class DownloadUtilTest {
    @Test
    fun `completed downloads are not prepared again`() {
        assertFalse(shouldPrepareDownload(Download.STATE_COMPLETED))
        assertTrue(shouldPrepareDownload(Download.STATE_FAILED))
        assertTrue(shouldPrepareDownload(null))
    }

    @Test
    fun `downloadArtworkUrls keeps distinct song and album covers`() {
        assertEquals(
            listOf("song-cover", "album-cover"),
            downloadArtworkUrls("song-cover", "album-cover"),
        )
        assertEquals(
            listOf("song-cover"),
            downloadArtworkUrls("song-cover", "song-cover"),
        )
        assertEquals(emptyList<String>(), downloadArtworkUrls("", null))
    }

    @Test
    fun `sanitizeFilename removes invalid characters`() {
        assertEquals("Song_Title_Artist", sanitizeFilename("Song:Title/Artist"))
        assertEquals("Clean Title", sanitizeFilename("Clean Title"))
        assertEquals("Title_Name_", sanitizeFilename("Title*Name?"))
    }
}
