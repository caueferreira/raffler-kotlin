package com.fibelatti.raffler.core.platform

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.fibelatti.raffler.R
import com.fibelatti.raffler.core.extension.visible
import kotlinx.android.synthetic.main.layout_title.view.*

class TitleLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_title, this, true)
    }

    fun setTitle(@StringRes titleRes: Int) {
        setTitle(context.getString(titleRes))
    }

    fun setTitle(title: String) {
        textViewTitle.text = title
    }

    fun navigateUp(navigateUp: () -> Unit) {
        buttonNavigateBack.apply {
            visible()
            setOnClickListener { navigateUp() }
        }
    }
}
