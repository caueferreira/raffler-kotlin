package com.fibelatti.raffler.core.di.modules.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
@Suppress("UNCHECKED_CAST")
class ViewModelFactory @Inject constructor(
    private val creators: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val creator = creators[modelClass]
            ?: creators.asIterable().firstOrNull { modelClass.isAssignableFrom(it.key) }?.value
            ?: throw IllegalArgumentException("Unknown ViewModel class $modelClass")

        return try {
            creator.get() as T
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    inline fun <reified T : ViewModel> of(activity: FragmentActivity): T =
        ViewModelProviders.of(activity, this)[T::class.java]

    inline fun <reified T : ViewModel> of(fragment: Fragment): T =
        ViewModelProviders.of(fragment, this)[T::class.java]
}