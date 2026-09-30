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
    fun `getDownloadDir returns non-null directory`() {
        val context = RuntimeEnvironment.getApplication()
        val dir = getDownloadDir(context)
        assertNotNull(dir)
        assertTrue(dir.path.contains("Metrolist") || dir.path.contains("download"))
    }

    @Test
    fun `migrateLegacyDownloads moves files from old location to new location`() {
        val context = RuntimeEnvironment.getApplication()
        val tempFolder = Files.createTempDirectory("download_test").toFile()
        try {
            val oldDir = context.filesDir.resolve("download")
            oldDir.mkdirs()
            val dummyFile = File(oldDir, "test_file.exo")
            dummyFile.writeText("test data")

            val newDir = File(tempFolder, "Metrolist")
            migrateLegacyDownloads(context, newDir)

            assertFalse(oldDir.exists())
            val migratedFile = File(newDir, "test_file.exo")
            assertTrue(migratedFile.exists())
            assertEquals("test data", migratedFile.readText())
        } finally {
            tempFolder.deleteRecursively()
        }
    }
}
