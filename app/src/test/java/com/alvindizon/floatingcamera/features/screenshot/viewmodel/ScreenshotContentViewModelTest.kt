package com.alvindizon.floatingcamera.features.screenshot.viewmodel

import android.content.Context
import com.alvindizon.floatingcamera.features.screenshot.repo.ScreenshotRepository
import com.alvindizon.floatingcamera.utils.IntentUtils
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ScreenshotContentViewModelTest {

    private val screenshotRepository: ScreenshotRepository = mockk()

    private val context: Context = mockk()

    private lateinit var viewModel: ScreenshotContentViewModel

    @BeforeEach
    fun setUp() {
        viewModel = ScreenshotContentViewModel(screenshotRepository)
    }

    @Test
    fun `verify that onDestroy calls screenshotrepo release`() {
        every { screenshotRepository.release() } just Runs
        viewModel.onDestroy()

        verify(exactly = 1) { screenshotRepository.release() }
    }

    @Test
    fun `verify that onCreate creates cacheFile if screenshot repo returns non-null filename`() {
        every { screenshotRepository.getLatestScreenshotFilename() } returns "new"

        viewModel.onCreate()

        assertNotNull(viewModel.cacheFile)
    }

    @Test
    fun `verify extension func for sharing file is called when onshare is called`() {
        mockkObject(IntentUtils)
        every { IntentUtils.shareFile(context, any()) } just Runs
        every { screenshotRepository.getLatestScreenshotFilename() } returns "new"
        viewModel.onCreate()

        viewModel.onShareButtonClick(context)

        verify(exactly = 1) { IntentUtils.shareFile(context, any()) }
    }

    @Test
    fun `verify extension func for emailing file is called when onemail is called`() {
        mockkObject(IntentUtils)
        every { IntentUtils.emailFile(context, any()) } just Runs
        every { screenshotRepository.getLatestScreenshotFilename() } returns "new"
        viewModel.onCreate()

        viewModel.onEmailButtonClick(context)

        verify(exactly = 1) { IntentUtils.emailFile(context, any()) }
    }
}
