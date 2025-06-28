package com.csi.irite

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.content.Context
import android.provider.Settings
import android.widget.Toast
import androidx.annotation.RequiresApi

class Setup2FAActivity : AppCompatActivity() {

    private lateinit var qrImageView: ImageView
    private lateinit var secretKeyText: TextView
    private lateinit var verifyButton: Button
    private lateinit var codeInput: EditText
    private lateinit var authenticator: GoogleAuthenticator2FA
    private var secretKey: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup_2fa)

        initViews()
        setupAuthenticator()
    }

    private fun initViews() {
        qrImageView = findViewById(R.id.qrImageView)
        secretKeyText = findViewById(R.id.secretKeyText)
        verifyButton = findViewById(R.id.verifyButton)
        codeInput = findViewById(R.id.codeInput)

        verifyButton.setOnClickListener {
            verifySetup()
        }
    }

    private fun setupAuthenticator() {
        authenticator = GoogleAuthenticator2FA()
        secretKey = authenticator.generateSecretKey()

        // Display secret key (for manual entry)
        secretKeyText.text = "Secret Key: $secretKey"

        // Generate QR code
        val userEmail = getCurrentUserEmail() // Implement this method
        val appName = "irite"
        authenticator.generateQRCode(userEmail, appName, secretKey, qrImageView)
    }

    private fun verifySetup() {
        val code = codeInput.text.toString().trim()

        if (code.length != 6) {
            showError("Please enter 6-digit code")
            return
        }

        if (authenticator.validateTOTP(secretKey, code)) {
            // Save secret key to secure storage
            saveSecretKey(secretKey)
            showSuccess("2FA setup successful!")
            finish()
        } else {
            showError("Invalid code. Please try again.")
        }
    }

    private fun getCurrentUserEmail(): String {
        // Implement your logic to get current user email
        val deviceId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        val label = "Device-$deviceId"
        return label
    }

    private fun saveSecretKey(key: String) {
        // Save to encrypted shared preferences or secure storage
        val sharedPref = getSharedPreferences("2fa_prefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("secret_key", key)
            putBoolean("is_2fa_enabled", true)
            apply()
        }
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showSuccess(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
