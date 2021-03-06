package com.fibelatti.raffler.features.myraffles.presentation.list

import androidx.lifecycle.MutableLiveData
import com.fibelatti.raffler.core.functional.mapCatching
import com.fibelatti.raffler.core.functional.onFailure
import com.fibelatti.raffler.core.functional.onSuccess
import com.fibelatti.raffler.core.platform.base.BaseViewModel
import com.fibelatti.raffler.core.provider.CoroutineLauncher
import com.fibelatti.raffler.features.myraffles.CustomRaffleRepository
import com.fibelatti.raffler.features.myraffles.presentation.common.CustomRaffleModel
import com.fibelatti.raffler.features.myraffles.presentation.common.CustomRaffleModelMapper
import javax.inject.Inject

class MyRafflesViewModel @Inject constructor(
    private val customRaffleRepository: CustomRaffleRepository,
    private val customRaffleModelMapper: CustomRaffleModelMapper,
    coroutineLauncher: CoroutineLauncher
) : BaseViewModel(coroutineLauncher) {

    val customRaffles by lazy { MutableLiveData<List<CustomRaffleModel>>() }
    val showHintAndCreateNewLayout by lazy { MutableLiveData<Boolean>() }

    fun getAllCustomRaffles() {
        startInBackground {
            customRaffleRepository.getAllCustomRaffles()
                .mapCatching(customRaffleModelMapper::mapList)
                .onSuccess { list ->
                    list.takeIf { it.isNotEmpty() }
                        ?.let(customRaffles::postValue)
                        ?: showHintAndCreateNewLayout.postValue(true)
                }.onFailure(::handleError)
        }
    }
}
