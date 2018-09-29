package com.fibelatti.raffler.features.preferences.presentation

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fibelatti.raffler.R
import com.fibelatti.raffler.core.extension.gone
import com.fibelatti.raffler.core.extension.observe
import com.fibelatti.raffler.core.extension.remove
import com.fibelatti.raffler.core.extension.setTitle
import com.fibelatti.raffler.core.platform.AppConfig.MARKET_BASE_URL
import com.fibelatti.raffler.core.platform.AppConfig.PLAY_STORE_BASE_URL
import com.fibelatti.raffler.core.platform.BaseFragment
import com.fibelatti.raffler.features.preferences.PreferencesRepository
import kotlinx.android.synthetic.main.fragment_preferences.*

private const val RESTART_DELAY = 1000L

class PreferencesFragment : BaseFragment() {

    private val preferencesViewModel by lazy {
        viewModelFactory.of<PreferencesViewModel>(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injector.inject(this)
        preferencesViewModel.run {
            observe(rouletteMusicEnabled) { checkBoxRouletteMusic.isChecked = it }
            observe(appTheme, ::setupTheme)

            getPreferences()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_preferences, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupLayout()
        setupListeners()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        setTitle(R.string.title_preferences)
    }

    private fun setupLayout() {
        try {
            val pInfo = requireContext().packageManager.getPackageInfo(requireContext().packageName, 0)
            textViewAppVersion.text = getString(R.string.preferences_app_version, pInfo.versionName)
        } catch (e: Exception) {
            textViewAppVersion.gone()
        }
    }

    private fun setupListeners() {
        checkBoxRouletteMusic.setOnCheckedChangeListener { _, isChecked ->
            preferencesViewModel.setRouletteMusicEnabled(isChecked)
        }

        buttonResetHints.setOnClickListener { preferencesViewModel.resetAllHints() }

        setupShareAndRate()
    }

    private fun setupTheme(appTheme: PreferencesRepository.AppTheme) {
        radioGroupTheme.setOnCheckedChangeListener(null)

        if (appTheme == PreferencesRepository.AppTheme.CLASSIC) {
            radioButtonThemeClassic.isChecked = true
        } else {
            radioButtonThemeDark.isChecked = true
        }

        radioGroupTheme.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radioButtonThemeClassic -> {
                    preferencesViewModel.setAppTheme(PreferencesRepository.AppTheme.CLASSIC)
                }
                R.id.radioButtonThemeDark -> {
                    preferencesViewModel.setAppTheme(PreferencesRepository.AppTheme.DARK)
                }
            }

            activity?.recreate()
        }
    }

    private fun setupShareAndRate() {
        val appName = requireContext().packageName.remove(".debug")

        buttonShare.setOnClickListener {
            val message = getString(R.string.preferences_share_text, "$PLAY_STORE_BASE_URL$appName")
            val share = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, message)
            }

            startActivity(Intent.createChooser(share, getString(R.string.preferences_share_title)))
        }

        buttonRate.setOnClickListener {
            try {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("$MARKET_BASE_URL$appName")))
            } catch (exception: ActivityNotFoundException) {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("$PLAY_STORE_BASE_URL$appName")))
            }
        }
    }
}