package ru.mvrlrd.playlistmaker


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity


class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

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


}