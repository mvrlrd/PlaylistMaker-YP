package ru.mvrlrd.playlistmaker.settings.ui


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.mvrlrd.playlistmaker.R
import ru.mvrlrd.playlistmaker.databinding.ActivitySettingsBinding


class SettingsActivity : AppCompatActivity() {
    private var _binding: ActivitySettingsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SettingsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.settingsToolbar.apply {
            setNavigationOnClickListener { onBackPressed() }
        }

        binding.switchTheme
            .apply {
                isChecked = viewModel.isDarkThemeOn()
                setOnCheckedChangeListener { _, isChecked ->
                    viewModel.switchTheme(isChecked)
                }
            }

        binding.shareContainer.apply {
            setOnClickListener {
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.praktikum_link))
                    type = INTENT_TYPE_FOR_SENDING
                }
                val shareIntent = Intent.createChooser(sendIntent, null)
                startActivity(shareIntent)
            }
        }

        binding.supportContainer.apply {
            setOnClickListener {
                val emailIntent = Intent(Intent.ACTION_SENDTO)
                emailIntent.data = Uri.parse(MAIL_TO)
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
    companion object{
        private const val INTENT_TYPE_FOR_SENDING = "text/plain"
        private const val MAIL_TO = "mailto:"
    }
}