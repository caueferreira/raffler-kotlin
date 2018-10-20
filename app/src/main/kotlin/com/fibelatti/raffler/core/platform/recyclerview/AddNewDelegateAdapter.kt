package com.fibelatti.raffler.core.platform.recyclerview

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.fibelatti.raffler.R
import com.fibelatti.raffler.core.extension.setShapeBackgroundColor
import com.fibelatti.raffler.core.platform.base.BaseDelegateAdapter
import com.fibelatti.raffler.core.platform.base.BaseViewType
import kotlinx.android.synthetic.main.list_item_add_new.view.*
import javax.inject.Inject

class AddNewDelegateAdapter @Inject constructor() : BaseDelegateAdapter {
    var colorList: List<Int> = ArrayList()
    var clickListener: () -> Unit = {}

    override fun getLayoutRes(): Int = R.layout.list_item_add_new

    override fun bindView(itemView: View, item: BaseViewType, viewHolder: RecyclerView.ViewHolder) {
        with(itemView) {
            layoutRoot.setShapeBackgroundColor(colorList[viewHolder.layoutPosition % colorList.size])
            setOnClickListener { clickListener() }
        }
    }
}
