package com.mamo.mamokeyboard.widgets

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewTreeObserver
import android.widget.LinearLayout
import com.mamo.mamokeyboard.R
import com.mamo.mamokeyboard.databinding.LayoutKeyIconBinding
import com.mamo.mamokeyboard.databinding.LayoutKeyTextBinding
import com.mamo.mamokeyboard.listener.KeyClickListener
import com.mamo.mamokeyboard.utils.KeyEvent


/**
 * Created by m.imran
 * Senior Software Engineer at
 * BhimSoft on 17/06/2021.
 *
 *
 * This custom view will shape Keys automatically
 * based on available size (width/height).
 */
class AdjustableKeysContainer : LinearLayout, View.OnClickListener {
    private var mInflater: LayoutInflater
    private var mCellSize: Int = 0
    private var mKeyClickListener: KeyClickListener? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr)

    init {
        gravity = Gravity.CENTER
        orientation = VERTICAL
        mInflater = LayoutInflater.from(context)
        val maxWidth = resources.getDimensionPixelSize(R.dimen.maxKeyWidth)
        val margin = resources.getDimensionPixelSize(R.dimen.cellMargin)
        viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                val cellWidth = measuredWidth / 3
                val cellHeight = measuredHeight / 4

                mCellSize = maxWidth.coerceAtMost(cellWidth.coerceAtMost(cellHeight)) - (2 * margin)
                Log.d(
                    ">>>>AdjKeyCon",
                    "$measuredHeight and $measuredWidth , $cellWidth and $cellHeight and $maxWidth , mCellSize=$mCellSize"
                )
                addItems()
            }

        })
    }

    private fun addItems() {
        val param = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)

        addView(getDigitsRow(1, 3), param)
        addView(getDigitsRow(4, 6), param)
        addView(getDigitsRow(7, 9), param)

        val lastRow = LinearLayout(context)
        lastRow.orientation = HORIZONTAL
        val dot = getTextualKey(lastRow, context.getString(R.string.dot))
        dot.root.tag = KeyEvent.DOT
        dot.root.setOnClickListener(this)
        lastRow.addView(dot.root)
        val zero = getTextualKey(lastRow, context.getString(R.string.zero))
        lastRow.addView(zero.root)

        val backSpace = LayoutKeyIconBinding.inflate(mInflater, lastRow, false)
        backSpace.root.tag = KeyEvent.DELETE
        backSpace.root.setOnClickListener(this)
        backSpace.root.layoutParams.height = mCellSize
        backSpace.root.layoutParams.width = mCellSize
        lastRow.addView(backSpace.root)

        addView(lastRow, param)
    }

    private fun getDigitsRow(start: Int, end: Int): LinearLayout {
        val rowContainer = LinearLayout(context)
        rowContainer.orientation = HORIZONTAL

        for (value in start.rangeTo(end)) {
            val textBinding = getTextualKey(rowContainer, "$value")
            rowContainer.addView(textBinding.root)
        }
        return rowContainer
    }

    private fun getTextualKey(
        rowContainer: LinearLayout,
        value: String
    ): LayoutKeyTextBinding {
        val textBinding = LayoutKeyTextBinding.inflate(mInflater, rowContainer, false)
        textBinding.text.text = value
        textBinding.root.layoutParams.height = mCellSize
        textBinding.root.layoutParams.width = mCellSize
        textBinding.root.tag = value
        textBinding.root.setOnClickListener(this)
        return textBinding
    }

    fun setKeyClickListener(listener: KeyClickListener) {
        mKeyClickListener = listener
    }

    override fun onClick(v: View?) {
        val keyEvent: KeyEvent = when (val value = v?.tag) {
            "0" -> KeyEvent.ZERO
            "1" -> KeyEvent.ONE
            "2" -> KeyEvent.TWO
            "3" -> KeyEvent.THREE
            "4" -> KeyEvent.FOUR
            "5" -> KeyEvent.FIVE
            "6" -> KeyEvent.SIX
            "7" -> KeyEvent.SEVEN
            "8" -> KeyEvent.EIGHT
            "9" -> KeyEvent.NINE
            else -> value as KeyEvent
        }
        mKeyClickListener?.onKeyClicked(keyEvent)
    }
}