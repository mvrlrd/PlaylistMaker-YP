package ru.mvrlrd.playlistmaker


import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.appcompat.widget.SwitchCompat
import androidx.appcompat.widget.Toolbar

class SettingsActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var switchTheme : SwitchCompat
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        toolbar = findViewById<Toolbar>(R.id.settingsToolbar).apply {
            setNavigationOnClickListener { onBackPressed() }
        }


        switchTheme = findViewById(R.id.switchTheme)
        when (isDarkModeOn()) {
            true -> {switchTheme.isChecked = true}
           false -> {switchTheme.isChecked = false}
        }
        switchTheme.setOnCheckedChangeListener { _, isChecked ->
            val editor = getSharedPreferences(SWITCH_ENABLED, MODE_PRIVATE).edit()
            if (isChecked) {
                editor.putBoolean(SWITCH_CONDITION, true)
                editor.apply()
                AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)
            } else {
                editor.putBoolean(SWITCH_CONDITION, false)
                editor.apply()
                AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)
            }
        }
        val supportButton = findViewById<ImageButton>(R.id.support_button)
        supportButton.setOnClickListener {
            val emailIntent = Intent(Intent.ACTION_SENDTO)
            emailIntent.data = Uri.parse("mailto:")
            emailIntent.putExtra(Intent.EXTRA_EMAIL, R.string.developer_email)
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, R.string.email_subject)
            emailIntent.putExtra(Intent.EXTRA_TEXT, R.string.email_text)
            startActivity(emailIntent)
        }
        val agreementButton = findViewById<ImageButton>(R.id.agreement_button)
        agreementButton.setOnClickListener {
            val agreementIntent = Intent(Intent.ACTION_VIEW)
            agreementIntent.data = Uri.parse(resources.getString(R.string.praktikum_link))
            startActivity(agreementIntent)
        }
        val shareButton = findViewById<ImageButton>(R.id.shareButton)
        shareButton.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.praktikum_link))
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }
    }

private fun isDarkModeOn(): Boolean {
    val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
    return currentNightMode == Configuration.UI_MODE_NIGHT_YES
}

    companion object {
       private const val SWITCH_ENABLED = "SWITCH_ENABLED"
       private const val SWITCH_CONDITION = "switch_condition"
    }
}