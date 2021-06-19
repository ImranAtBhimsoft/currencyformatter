package com.mamo.mamokeyboard

import com.mamo.mamokeyboard.utils.RegExpression
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val expectedInput1 = "123456789.1234"
        val expectedOutput1 = "AED 123,456,789.12"
        val expectedInput2 = "1234567890.1234"
        val expectedOutput2 = "AED 1,234,567,890.12"

        assertEquals(expectedOutput1, RegExpression.FORMATTER.format(expectedInput1.toDouble()))
        assertEquals(expectedOutput2, RegExpression.FORMATTER.format(expectedInput2.toDouble()))
    }
}