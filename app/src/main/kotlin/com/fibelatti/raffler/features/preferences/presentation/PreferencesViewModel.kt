package com.fibelatti.raffler.features.preferences.presentation

import androidx.lifecycle.MutableLiveData
import com.fibelatti.raffler.R
import com.fibelatti.raffler.core.extension.empty
import com.fibelatti.raffler.core.extension.isInt
import com.fibelatti.raffler.core.functional.Result
import com.fibelatti.raffler.core.functional.onFailure
import com.fibelatti.raffler.core.functional.onSuccess
import com.fibelatti.raffler.core.platform.AppConfig
import com.fibelatti.raffler.core.platform.MutableLiveEvent
import com.fibelatti.raffler.core.platform.base.BaseViewModel
import com.fibelatti.raffler.core.platform.postEvent
import com.fibelatti.raffler.core.provider.CoroutineLauncher
import com.fibelatti.raffler.core.provider.ResourceProvider
import com.fibelatti.raffler.features.preferences.Preferences
import com.fibelatti.raffler.features.preferences.PreferencesRepository
import javax.inject.Inject

class PreferencesViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository,
    private val resourceProvider: ResourceProvider,
    coroutineLauncher: CoroutineLauncher
) : BaseViewModel(coroutineLauncher) {

    val preferences by lazy { MutableLiveData<Preferences>() }
    val updateFeedback by lazy { MutableLiveEvent<String>() }
    val totalQuantityError by lazy { MutableLiveEvent<String>() }
    val raffleQuantityError by lazy { MutableLiveEvent<String>() }

    fun getPreferences() {
        startInBackground {
            preferencesRepository.getPreferences()
                .onSuccess(preferences::postValue)
                .onFailure(::handleError)
        }
    }

    fun setAppTheme(appTheme: AppConfig.AppTheme) {
        startInBackground { preferencesRepository.setAppTheme(appTheme) }
    }

    fun setAppLanguage(appLanguage: AppConfig.AppLanguage) {
        startInBackground { preferencesRepository.setLanguage(appLanguage) }
    }

    fun setLotteryDefaultValues(quantityAvailable: String, quantityToRaffle: String) {
        validateData(quantityAvailable, quantityToRaffle) { qtyTotal, qtyRaffle ->
            startInBackground {
                preferencesRepository.setLotteryDefault(qtyTotal, qtyRaffle)
                    .handleResult()
            }
        }
    }

    fun setPreferredRaffleMode(raffleMode: AppConfig.RaffleMode) {
        startInBackground {
            preferencesRepository.setPreferredRaffleMode(raffleMode)
                .handleResult()
        }
    }

    fun setRouletteMusicEnabled(value: Boolean) {
        startInBackground {
            preferencesRepository.setRouletteMusicEnabled(value)
                .handleResult()
        }
    }

    fun setRememberRaffledItems(value: Boolean) {
        startInBackground {
            preferencesRepository.rememberRaffledItems(value)
                .handleResult()
        }
    }

    fun resetAllHints() {
        startInBackground {
            preferencesRepository.resetHints()
                .handleResult()
        }
    }

    private fun Result<Unit>.handleResult() {
        onSuccess {
            getPreferences()
            updateFeedback.postEvent(resourceProvider.getString(R.string.preferences_changes_saved))
        }.onFailure(::handleError)
    }

    private fun validateData(
        quantityAvailable: String,
        quantityToRaffle: String,
        ifValid: (Int, Int) -> Unit
    ) {
        val qtyAvailable = quantityAvailable.takeIf { it.isNotBlank() } ?: "0"
        val qtyToRaffle = quantityToRaffle.takeIf { it.isNotBlank() } ?: "0"

        when {
            !qtyAvailable.isInt() -> {
                totalQuantityError.postEvent(resourceProvider.getString(R.string.lottery_quantity_validation_error))
            }
            !qtyToRaffle.isInt() -> {
                totalQuantityError.postEvent(String.empty())
                raffleQuantityError.postEvent(resourceProvider.getString(R.string.lottery_quantity_validation_error))
            }
            else -> {
                totalQuantityError.postEvent(String.empty())
                raffleQuantityError.postEvent(String.empty())

                ifValid(qtyAvailable.toInt(), qtyToRaffle.toInt())
            }
        }
    }
}
