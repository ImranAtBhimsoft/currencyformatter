package com.mamo.mamokeyboard.formatter

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

/**
 * Created by m.imran
 * Senior Software Engineer at
 * BhimSoft on 18/06/2021.
 */
@Module
@InstallIn(SingletonComponent::class)
class FormatterModule {
    @Provides
    fun provideFormatter(@ApplicationContext context:Context): Formatter = Formatter(context)
}