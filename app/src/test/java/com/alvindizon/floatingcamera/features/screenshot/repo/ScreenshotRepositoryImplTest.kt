package com.alvindizon.floatingcamera.features.screenshot.repo

import android.content.Intent
import android.graphics.Bitmap
import com.alvindizon.floatingcamera.data.cache.BitmapFilenameCache
import com.alvindizon.floatingcamera.features.screenshot.ScreenshotManager
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ScreenshotRepositoryImplTest {

    private val bitmapFilenameCache: BitmapFilenameCache = mockk()

    private val screenshotManager: ScreenshotManager = mockk()

    private lateinit var repo: ScreenshotRepositoryImpl

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        repo = ScreenshotRepositoryImpl(bitmapFilenameCache, screenshotManager)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `verify screenshotmanager receives intent when initialize is called`() {
        val captureIntent = slot<Intent>()
        val intent: Intent = mockk {
            every { action } returns "someaction"
        }
        every { screenshotManager.initialize(capture(captureIntent)) } just Runs

        repo.initialize(intent)

        verify(exactly = 1) { screenshotManager.initialize(any()) }
        assertEquals("someaction", captureIntent.captured.action)
    }

    @Test
    fun `verify bitmapcache receives bitmap when saveBitmap is called`() = runTest {
        val captureBitmap = slot<Bitmap>()
        val bitmap: Bitmap = mockk {
            every { width } returns 100
        }
        every { bitmapFilenameCache.saveBitmap(capture(captureBitmap)) } just Runs

        repo.saveBitmap(bitmap)

        verify(exactly = 1) { bitmapFilenameCache.saveBitmap(any()) }
        assertEquals(100, captureBitmap.captured.width)
    }

    @Test
    fun `verify getlatestscreenshotfilename returns correct value from bitmapfilenamecache`() {
        val expectedFilename = "/data/bleah/bmp/abcdef.png"
        every { bitmapFilenameCache.getLatestScreenshotFilename() } returns expectedFilename

        val result = repo.getLatestScreenshotFilename()
        assertEquals(expectedFilename, result)
    }

    @Test
    fun `verify capture function returns correct value when screenshotmanager capture is called`() {
        val expectedBitmap: Bitmap = mockk {
            every { width } returns 100
        }
        every { screenshotManager.capture() } returns expectedBitmap

        val result = repo.capture()

        verify(exactly = 1) { screenshotManager.capture() }
        assertNotNull(result)
        assertEquals(expectedBitmap.width, result?.width)
    }

    @Test
    fun `verify that bitmapFilenameCache and screenshotManager release funcs are called`() {
        every { bitmapFilenameCache.clearCache() } just Runs
        every { screenshotManager.release() } just Runs

        repo.release()

        verify(exactly = 1) { bitmapFilenameCache.clearCache() }
        verify(exactly = 1) { screenshotManager.release() }
    }
}
