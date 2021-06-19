package com.mamo.mamokeyboard.formatter

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.mamo.mamokeyboard.R
import com.mamo.mamokeyboard.utils.KeyEvent
import com.mamo.mamokeyboard.utils.RegExpression
import javax.inject.Inject


/**
 * Created by m.imran
 * Senior Software Engineer at
 * BhimSoft on 18/06/2021.
 */
class Formatter @Inject constructor(private val mContext: Context) {
    private companion object {
        private const val MAX_CHARACTERS_LIMIT = 15
    }

    fun formatOnKey(value: String, textView: TextView, whichEvent: KeyEvent) {
        var textToFormat = SpannableStringBuilder("")
        val originalText = textView.tag?.toString()
        originalText?.let {
            if (whichEvent != KeyEvent.DELETE && it.contains(".")) {
                var fraPart = it.substring(it.indexOf("."), it.length)
                fraPart = fraPart.replace(".", "")
                if (fraPart.length == 2) {
                    return
                }
            }
            textToFormat.append(it)
        }

        when (whichEvent) {
            KeyEvent.DELETE -> {
                if (textToFormat.isNotEmpty()) {
                    textToFormat = textToFormat.delete(textToFormat.length - 1, textToFormat.length)
                }
            }
            KeyEvent.DOT -> {
                if (!textToFormat.contains(".")) {
                    if (textToFormat.isEmpty()) {
                        textToFormat.append("0")
                    }
                    textToFormat.append(".")
                }
            }
            else -> {
                textToFormat.append(value)
            }
        }
        format(textToFormat, textView)
    }

    fun format(
        textToFormat: SpannableStringBuilder,
        textView: TextView
    ) {
        if (textToFormat.length > MAX_CHARACTERS_LIMIT) {
            Toast.makeText(mContext, "Limit Exceeded", Toast.LENGTH_SHORT).show()
            return
        }
        textView.tag = textToFormat.toString()
        textView.text = getColoredFormattedSpan(textToFormat)
//        if (textToFormat.isNotEmpty()) {
//            textView.text = getColoredFormattedSpan(textToFormat)
//        } else {
//            textView.setTextColor(ContextCompat.getColor(mContext, R.color.colorTextHint))
//            textView.text = mContext.getString(R.string.aed_0_00)
//        }
    }

    /**
     * @param original : Spannable from which we want to do calculations
     * @return SpannableString  Colored Spannable
     */
    fun getColoredFormattedSpan(original: SpannableStringBuilder): SpannableString {
        if (original.isEmpty()) {
            val empty = SpannableString(mContext.getString(R.string.aed_0_00))
            empty.setSpan(
                ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.colorTextHint)),
                0,
                empty.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            return empty
        }
        if (original.length == 1 && original.endsWith(".")) {
            original.clear()
            original.append("0.")
        }
        val formattedText = RegExpression.FORMATTER.format(original.toString().toDouble())
        val textToSpan = SpannableString(formattedText)
        val start = 0
        var end = textToSpan.length - 3
        if (original.contains(".")) {
            end = getEndPointForTwoDecimal(original, textToSpan)
            //end = getEndPointForMoreThanTwoDecimal(span, textToSpan)
        }
        textToSpan.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.colorTextToolbar)),
            start,
            end,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        textToSpan.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.colorTextHint)),
            end,
            textToSpan.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return textToSpan
    }

    /**
     * @param original : Spannable from which we want to do calculations
     * @param textToSpan : Spannable to which Span will apply
     * @return end :Int  End point
     */
    private fun getEndPointForTwoDecimal(
        original: SpannableStringBuilder,
        textToSpan: SpannableString
    ): Int {
        val fraPartLength = original.substring(original.indexOf("."), original.length).length
        val length = textToSpan.length + fraPartLength - 3
        //This check is useful especially if you're
        // running Instrumented Test with wrong input
        return if (length > textToSpan.length) textToSpan.length else length
    }

    /**
     * @param original : Spannable from which we want to do calculations
     * @param textToSpan : Spannable to which Span will apply
     * @return end :Int  End point
     */
    @Suppress("unused")
    private fun getEndPointForMoreThanTwoDecimal(
        original: SpannableStringBuilder,
        textToSpan: SpannableString
    ): Int {
        val fracPartLength = original.substring(original.indexOf("."), original.length).length
        return if (fracPartLength > 3) {
            textToSpan.length
        } else {
            textToSpan.length + fracPartLength - 3

        }
    }
}