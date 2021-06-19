package com.mamo.mamokeyboard

import android.text.SpannableStringBuilder
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.mamo.mamokeyboard.formatter.Formatter
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.mamo.mamokeyboard", appContext.packageName)
    }

    @Test
    fun formatting_isCorrect() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        val expectedInput1 = "."
        val expectedOutput1 = "AED 0.00"
        val expectedInput2 = "0."
        val expectedOutput2 = "AED 0.00"
        val expectedInput3 = "1.2"
        val expectedOutput3 = "AED 1.20"
        val expectedInput4 = "12.01"
        val expectedOutput4 = "AED 12.01"
        val formatter = Formatter(appContext)
        assertEquals(
            expectedOutput1,
            formatter.getColoredFormattedSpan(SpannableStringBuilder(expectedInput1)).toString()
        )
        assertEquals(
            expectedOutput2,
            formatter.getColoredFormattedSpan(SpannableStringBuilder(expectedInput2)).toString()
        )
        assertEquals(
            expectedOutput3,
            formatter.getColoredFormattedSpan(SpannableStringBuilder(expectedInput3)).toString()
        )
        assertEquals(
            expectedOutput4,
            formatter.getColoredFormattedSpan(SpannableStringBuilder(expectedInput4)).toString()
        )
    }

    @Test
    fun isWorkingOnWrongInput() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        val expectedInput1 = "123456789.1234"
        val expectedOutput1 = "AED 123,456,789.12"
        val expectedInput2 = "1234567890.1234"
        val expectedOutput2 = "AED 1,234,567,890.12"

        val formatter = Formatter(appContext)
        assertEquals(
            expectedOutput1,
            formatter.getColoredFormattedSpan(SpannableStringBuilder(expectedInput1)).toString()
        )
        assertEquals(
            expectedOutput2,
            formatter.getColoredFormattedSpan(SpannableStringBuilder(expectedInput2)).toString()
        )
    }
}