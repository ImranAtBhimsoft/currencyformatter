package com.mamo.mamokeyboard

import android.os.Bundle
import android.text.SpannableStringBuilder
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.mamo.mamokeyboard.databinding.ActivityMainBinding
import com.mamo.mamokeyboard.formatter.Formatter
import com.mamo.mamokeyboard.listener.KeyClickListener
import com.mamo.mamokeyboard.utils.CommonKeys
import com.mamo.mamokeyboard.utils.CommonUtils
import com.mamo.mamokeyboard.utils.KeyEvent
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * I can use android:configChanges="orientation|screenSize" to redraw but here,
 * in this scenario, i have to explicitly redraw
 * UI to make it to fit. So i am using onSaveInstanceState
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var mFormatter: Formatter

    private lateinit var mBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        mBinding.keyBoard.setKeyClickListener(object : KeyClickListener {
            override fun onKeyClicked(which: KeyEvent) {
                mFormatter.formatOnKey(which.value, mBinding.result, which)
            }
        })

        savedInstanceState?.let {
            val savedValue = it.getString(CommonKeys.KEY_DATA).orEmpty()
            mBinding.result.post {
                mFormatter.format(SpannableStringBuilder(savedValue), mBinding.result)
            }
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(
            CommonKeys.KEY_DATA,
            CommonUtils.getValue(mBinding.result.tag?.toString())
        )
    }
}