package com.fibelatti.raffler.features.myraffles.presentation.grouping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.fibelatti.raffler.R
import com.fibelatti.raffler.core.extension.clearError
import com.fibelatti.raffler.core.extension.error
import com.fibelatti.raffler.core.extension.getColorGradientForListSize
import com.fibelatti.raffler.core.extension.hideKeyboard
import com.fibelatti.raffler.core.extension.observe
import com.fibelatti.raffler.core.extension.showError
import com.fibelatti.raffler.core.extension.textAsString
import com.fibelatti.raffler.core.extension.withDefaultDecoration
import com.fibelatti.raffler.core.extension.withLinearLayoutManager
import com.fibelatti.raffler.core.platform.base.BaseFragment
import com.fibelatti.raffler.features.myraffles.presentation.common.CustomRaffleDraftedAdapter
import com.fibelatti.raffler.features.myraffles.presentation.common.CustomRaffleDraftedModel
import com.fibelatti.raffler.features.myraffles.presentation.customraffledetails.CustomRaffleDetailsViewModel
import kotlinx.android.synthetic.main.fragment_custom_raffle_grouping.*
import javax.inject.Inject

class CustomRaffleGroupingFragment : BaseFragment() {

    @Inject
    lateinit var adapter: CustomRaffleDraftedAdapter

    private val customRaffleDetailsViewModel by lazy {
        viewModelFactory.get<CustomRaffleDetailsViewModel>(requireActivity())
    }
    private val customRaffleGroupingViewModel by lazy {
        viewModelFactory.get<CustomRaffleGroupingViewModel>(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injector.inject(this)
        customRaffleGroupingViewModel.run {
            error(error, ::handleError)
            observe(quantityError, ::handleQuantityError)
            observe(groups, ::handleGroups)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_custom_raffle_grouping, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupLayout()
        setupRecyclerView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        layoutRoot.hideKeyboard()
    }

    private fun setupLayout() {
        layoutTitle.setTitle(R.string.custom_raffle_details_mode_grouping)
        layoutTitle.setNavigateUp(R.drawable.ic_close) { layoutRoot.findNavController().navigateUp() }

        buttonRaffle.setOnClickListener {
            customRaffleDetailsViewModel.customRaffle.value?.let { raffle ->
                if (radioButtonByGroup.isChecked) {
                    customRaffleGroupingViewModel.getGroupsByQuantity(
                        raffle.includedItems,
                        editTextTotalQuantity.textAsString()
                    )
                } else {
                    customRaffleGroupingViewModel.getGroupsByItemQuantity(
                        raffle.includedItems,
                        editTextTotalQuantity.textAsString()
                    )
                }
            }
        }
    }

    private fun setupRecyclerView() {
        recyclerViewItems.withLinearLayoutManager()
            .withDefaultDecoration()
            .adapter = adapter
    }

    private fun handleQuantityError(message: String) {
        if (message.isNotEmpty()) {
            inputLayoutQuantity.showError(message)
        } else {
            inputLayoutQuantity.clearError()
        }
    }

    private fun handleGroups(winners: List<CustomRaffleDraftedModel>) {
        layoutRoot.hideKeyboard()
        adapter.run {
            colorList = getColorGradientForListSize(
                requireContext(),
                R.color.color_accent,
                R.color.color_primary,
                winners.size
            )
            setItems(winners)
        }
    }
}
