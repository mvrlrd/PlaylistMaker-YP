package ru.mvrlrd.playlistmaker.settings.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.mvrlrd.playlistmaker.R
import ru.mvrlrd.playlistmaker.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding: FragmentSettingsBinding
        get() = _binding ?: throw RuntimeException("FragmentSettingsBinding == null")
    private val viewModel: SettingsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.swThemeSwitcher
            .apply {
                isChecked = viewModel.isDarkThemeOn()
                setOnCheckedChangeListener { _, isChecked ->
                    viewModel.switchTheme(isChecked)
                }
            }

        binding.shareButton.apply {
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val INTENT_TYPE_FOR_SENDING = "text/plain"
        private const val MAIL_TO = "mailto:"
    }
}