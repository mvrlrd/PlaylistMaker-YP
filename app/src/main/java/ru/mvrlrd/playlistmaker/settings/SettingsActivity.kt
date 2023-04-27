package ru.mvrlrd.playlistmaker.settings


import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import ru.mvrlrd.playlistmaker.R
import ru.mvrlrd.playlistmaker.App
import ru.mvrlrd.playlistmaker.databinding.ActivitySettingsBinding


class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var viewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, SettingsViewModel.getViewModelFactory(application as App))[SettingsViewModel::class.java]
        binding.settingsToolbar.apply {
            setNavigationOnClickListener { onBackPressed() }
        }

        binding.switchTheme
            .apply {
                viewModel.applyTheme()
//                (applicationContext as App).applySavedThemeMode()
                isChecked = isDarkTheme()
                setOnCheckedChangeListener { _, isChecked ->
                    viewModel.switchTheme(isChecked)
//                    (applicationContext as App).switchTheme(isChecked)
                }
            }

        binding.shareContainer.apply {
            setOnClickListener {
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.praktikum_link))
                    type = "text/plain"
                }
                val shareIntent = Intent.createChooser(sendIntent, null)
                startActivity(shareIntent)
            }
        }

        binding.supportContainer.apply {
            setOnClickListener {
                val emailIntent = Intent(Intent.ACTION_SENDTO)
                emailIntent.data = Uri.parse("mailto:")
                emailIntent.putExtra(Intent.EXTRA_EMAIL, R.string.developer_email)
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, R.string.email_subject)
                emailIntent.putExtra(Intent.EXTRA_TEXT, R.string.email_text)
                startActivity(emailIntent)
            }
        }

        binding.agreementContainer.apply {
            setOnClickListener {
                val agreementIntent = Intent(Intent.ACTION_VIEW)
                agreementIntent.data = Uri.parse(resources.getString(R.string.praktikum_link))
                startActivity(agreementIntent)
            }
        }
    }

    private fun isDarkTheme(): Boolean {
        return this.resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
    }
}