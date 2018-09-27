package com.fibelatti.raffler.core.di.modules

import android.content.Context
import com.fibelatti.raffler.App
import com.fibelatti.raffler.core.platform.AppResourceProvider
import com.fibelatti.raffler.core.provider.AppThreadProvider
import com.fibelatti.raffler.core.provider.ResourceProvider
import com.fibelatti.raffler.core.provider.ThreadProvider
import dagger.Binds
import dagger.Module

@Module(includes = [CoreModule.Binder::class])
object CoreModule {
    @Module
    interface Binder {
        @Binds
        fun bindContext(app: App): Context

        @Binds
        fun bindThreadProvider(appThreadProvider: AppThreadProvider): ThreadProvider

        @Binds
        fun bindResourceProvider(appResourceProvider: AppResourceProvider): ResourceProvider
    }
}