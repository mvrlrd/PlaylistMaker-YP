package ru.mvrlrd.playlistmaker


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.appcompat.widget.Toolbar

class SettingsActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var switchTheme : SwitchCompat
    private lateinit var shareContainer: ViewGroup
    private lateinit var supportContainer: ViewGroup
    private lateinit var agreementContainer: ViewGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        toolbar = findViewById<Toolbar>(R.id.settingsToolbar).apply {
            setNavigationOnClickListener { onBackPressed() }
        }

        switchTheme = findViewById<SwitchCompat?>(R.id.switchTheme)
            .apply {
                isChecked = (applicationContext as App).applySavedThemeMode()
                setOnCheckedChangeListener { _, isChecked ->
                    (applicationContext as App).switchTheme(isChecked)
                }
            }

        shareContainer = findViewById<ViewGroup?>(R.id.shareContainer).apply {
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

        supportContainer = findViewById<ViewGroup?>(R.id.supportContainer).apply {
            setOnClickListener {
                val emailIntent = Intent(Intent.ACTION_SENDTO)
                emailIntent.data = Uri.parse("mailto:")
                emailIntent.putExtra(Intent.EXTRA_EMAIL, R.string.developer_email)
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, R.string.email_subject)
                emailIntent.putExtra(Intent.EXTRA_TEXT, R.string.email_text)
                startActivity(emailIntent)
            }
        }

        agreementContainer = findViewById<ViewGroup?>(R.id.agreementContainer).apply {
            setOnClickListener {
                val agreementIntent = Intent(Intent.ACTION_VIEW)
                agreementIntent.data = Uri.parse(resources.getString(R.string.praktikum_link))
                startActivity(agreementIntent)
            }
        }
    }
}