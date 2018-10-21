package com.fibelatti.raffler.features.myraffles.presentation.customraffledetails

import android.view.View
import com.fibelatti.raffler.R
import com.fibelatti.raffler.core.extension.visibleIf
import com.fibelatti.raffler.core.platform.base.BaseAdapter
import com.fibelatti.raffler.features.myraffles.presentation.common.CustomRaffleItemModel
import kotlinx.android.synthetic.main.list_item_custom_raffle_item.view.*
import javax.inject.Inject

class CustomRaffleDetailsAdapter @Inject constructor() : BaseAdapter<CustomRaffleItemModel>() {

    var clickListener: (index: Int, isSelected: Boolean) -> Unit = { _, _ -> }

    override fun getLayoutRes(): Int = R.layout.list_item_custom_raffle_item

    override fun View.bindView(item: CustomRaffleItemModel, viewHolder: ViewHolder) {
        item.run {
            layoutRootCustomRaffleItem.isSelected = included
            imageViewSelected.visibleIf(included, otherwiseVisibility = View.INVISIBLE)
            textViewCustomRaffleItemDescription.text = description

            setOnClickListener {
                layoutRootCustomRaffleItem.isSelected = !layoutRootCustomRaffleItem.isSelected
                imageViewSelected.visibleIf(layoutRootCustomRaffleItem.isSelected, otherwiseVisibility = View.INVISIBLE)
                clickListener(viewHolder.adapterPosition, layoutRootCustomRaffleItem.isSelected)
            }
        }
    }
}
