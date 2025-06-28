package com.csi.irite

import android.graphics.Bitmap
import android.util.Log
import android.widget.ImageView
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder
import org.apache.commons.codec.binary.Base32
import java.security.SecureRandom
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import kotlin.experimental.and

class GoogleAuthenticator2FA {

    companion object {
        private const val SECRET_KEY_LENGTH = 20
        private const val TOTP_WINDOW = 30 // 30 seconds
        private const val TOTP_DIGITS = 6
    }

    /**
     * Generate a random secret key for the user
     */
    fun generateSecretKey(): String {
        val buffer = ByteArray(SECRET_KEY_LENGTH)
        SecureRandom().nextBytes(buffer)
        return Base32().encodeToString(buffer)
    }

    /**
     * Generate QR code for Google Authenticator setup
     */
    fun generateQRCode(
        userEmail: String,
        appName: String,
        secretKey: String,
        imageView: ImageView
    ) {
        val qrData = "otpauth://totp/$appName:$userEmail?secret=$secretKey&issuer=$appName"

        try {
            val barcodeEncoder = BarcodeEncoder()
            val bitmap: Bitmap = barcodeEncoder.encodeBitmap(qrData, BarcodeFormat.QR_CODE, 400, 400)
            imageView.setImageBitmap(bitmap)
        } catch (e: WriterException) {
            e.printStackTrace()
        }
    }

    /**
     * Validate TOTP code entered by user
     */
    fun validateTOTP(secretKey: String, userCode: String): Boolean {
        val currentTime = System.currentTimeMillis() / 1000L
        val timeWindow = currentTime / TOTP_WINDOW

        // Check current window and adjacent windows for clock skew tolerance
        for (i in -1..1) {
            val testTime = timeWindow + i
            val generatedCode = generateTOTP(secretKey, testTime)
            Log.d("_Login", "generatedCode $generatedCode")
            Log.d("_Login", "userCode $userCode")
            if (generatedCode == userCode) {
                return true
            }
        }
        return false
    }

    /**
     * Generate TOTP code for given secret and time
     */
    private fun generateTOTP(secretKey: String, timeWindow: Long): String {
        val key = Base32().decode(secretKey)
        val timeBytes = ByteArray(8)

        // Convert time to byte array
        var time = timeWindow
        for (i in 7 downTo 0) {
            timeBytes[i] = (time and 0xFF).toByte()
            time = time shr 8
        }

        // Generate HMAC-SHA1
        val hmac = Mac.getInstance("HmacSHA1")
        val keySpec = SecretKeySpec(key, "RAW")
        hmac.init(keySpec)
        val hash = hmac.doFinal(timeBytes)

        // Dynamic truncation
        val offset = (hash[hash.size - 1] and 0x0F).toInt()
        val truncatedHash = ((hash[offset].toInt() and 0x7F) shl 24) or
                ((hash[offset + 1].toInt() and 0xFF) shl 16) or
                ((hash[offset + 2].toInt() and 0xFF) shl 8) or
                (hash[offset + 3].toInt() and 0xFF)

        // Generate 6-digit code
        val code = truncatedHash % 1000000
        return String.format("%06d", code)
    }
}