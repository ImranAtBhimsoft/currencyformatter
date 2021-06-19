package com.mamo.mamokeyboard.utils

import java.text.DecimalFormat

/**
 * Created by m.imran
 * Senior Software Engineer at
 * BhimSoft on 18/06/2021.
 */
object RegExpression {
    val DIGITS = "[^\\d.]".toRegex()
    val FORMATTER = DecimalFormat("AED #,##0.00")
}