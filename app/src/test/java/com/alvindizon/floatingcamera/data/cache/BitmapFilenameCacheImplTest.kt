package com.alvindizon.floatingcamera.data.cache

import com.alvindizon.floatingcamera.data.file.BitmapFileSaver
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class BitmapFilenameCacheImplTest {

    private lateinit var bitmapFilenameCache: BitmapFilenameCacheImpl

    private val bitmapFileSaver: BitmapFileSaver = mockk()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        bitmapFilenameCache = BitmapFilenameCacheImpl()
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `verify that filename is added to list on bitmap save`() = runTest {
        val expected = "filename"
        coEvery { bitmapFileSaver.saveBitmap(any()) } returns expected

        bitmapFilenameCache.saveFilename(expected)

        assertEquals(expected, bitmapFilenameCache.getLatestScreenshotFilename())
    }

    @Test
    fun `verify that filenames are removed when clearCache is called`() = runTest {
        val expected = "filename"
        coEvery { bitmapFileSaver.saveBitmap(any()) } returns expected

        bitmapFilenameCache.saveFilename(expected)

        bitmapFilenameCache.clearCache()

        assert(bitmapFilenameCache.fileNameList.isEmpty())
    }
}
