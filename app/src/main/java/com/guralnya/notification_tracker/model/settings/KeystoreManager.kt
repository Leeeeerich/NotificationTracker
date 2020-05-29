@file:Suppress("DEPRECATION")

package com.guralnya.notification_tracker.model.settings

import android.content.Context
import android.security.KeyPairGeneratorSpec
import android.util.Base64
import com.guralnya.notification_tracker.BuildConfig
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.math.BigInteger
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.KeyStoreException
import java.security.NoSuchAlgorithmException
import java.security.cert.CertificateException
import java.security.interfaces.RSAPublicKey
import java.util.*
import javax.crypto.Cipher
import javax.crypto.CipherInputStream
import javax.crypto.CipherOutputStream
import javax.security.auth.x500.X500Principal

//TODO: Has to be optimized
@Suppress("DEPRECATION")
class KeystoreManager(private val context: Context, private val preferences: PreferencesManager) {
    private lateinit var keyStore: KeyStore

    companion object {
        private const val ANDROID_KEY_STORE = "AndroidKeyStore"
        private const val ALIAS_PIN = BuildConfig.PIN_ALIAS
        private const val ALIAS_DB_PASS = BuildConfig.DB_PASS_ALIAS
        private const val CIPHER_TYPE = "RSA/ECB/PKCS1Padding"
        private const val ALGORITHM = "RSA"
        private const val OPEN_SSL_CIPHER_PROVIDE = "AndroidOpenSSL"
        private const val BCWORKAROUND_CIPHER_PROVIDE = "AndroidKeyStoreBCWorkaround"
    }

    init {
        initKeyStore()
        createKey(ALIAS_PIN)
        createKey(ALIAS_DB_PASS)
    }

    @Throws(
        KeyStoreException::class,
        CertificateException::class,
        NoSuchAlgorithmException::class,
        IOException::class
    )
    private fun initKeyStore() {
        keyStore = KeyStore.getInstance(ANDROID_KEY_STORE)
        keyStore.load(null)
    }

    private fun createKey(alias: String) {
        try {
            if (!keyStore.containsAlias(alias)) {
                val start = Calendar.getInstance()
                val end = Calendar.getInstance()
                end.add(Calendar.YEAR, 5)
                val spec = KeyPairGeneratorSpec.Builder(context)
                    .setAlias(alias)
                    .setSubject(X500Principal("CN=$alias, O=Android Authority"))
                    .setSerialNumber(BigInteger.ONE)
                    .setStartDate(start.time)
                    .setEndDate(end.time)
                    .build()
                val generator = KeyPairGenerator.getInstance(
                    ALGORITHM,
                    ANDROID_KEY_STORE
                )
                generator.initialize(spec)
                val generateKeyPair = generator.generateKeyPair()
            }
        } catch (e: Exception) {
            //TODO: Add log
        }
    }

    private fun getCipher(): Cipher = Cipher.getInstance(CIPHER_TYPE, BCWORKAROUND_CIPHER_PROVIDE)


    private fun encryptString(alias: String, data: String): String? {
        try {
            val privateKeyEntry = keyStore.getEntry(alias, null) as KeyStore.PrivateKeyEntry
            val publicKey = privateKeyEntry.certificate.publicKey as RSAPublicKey
            val encrypt = getCipher()
            encrypt.init(Cipher.ENCRYPT_MODE, publicKey)
            val outputStream = ByteArrayOutputStream()
            val cipherOutputStream = CipherOutputStream(
                outputStream, encrypt
            )
            cipherOutputStream.write(data.toByteArray(Charsets.UTF_8))
            cipherOutputStream.close()
            val bytes = outputStream.toByteArray()
            return Base64.encodeToString(bytes, Base64.DEFAULT)
        } catch (e: Exception) {
        }
        return null
    }

    private fun decryptString(alias: String, data: String): String? {
        try {
            val privateKeyEntry = keyStore.getEntry(alias, null) as KeyStore.PrivateKeyEntry
            val decrypt = getCipher()
            decrypt.init(Cipher.DECRYPT_MODE, privateKeyEntry.privateKey)
            val cipherInputStream = CipherInputStream(
                ByteArrayInputStream(Base64.decode(data, Base64.DEFAULT)), decrypt
            )
            val bytes = cipherInputStream.readBytes()
            return String(bytes, 0, bytes.size, Charsets.UTF_8)

        } catch (e: Exception) {
        }
        return null
    }

    fun deleteKeyPin() {
        keyStore.deleteEntry(ALIAS_PIN)
    }

    fun deleteKeyDbPass() {
        keyStore.deleteEntry(ALIAS_DB_PASS)
    }

    fun getPin(): String? {
        val encryptedPwd = preferences.getPinCode()
        return if (encryptedPwd != null) decryptString(ALIAS_PIN, encryptedPwd) else null
    }

    fun setPin(password: String) {
        val encryptedPwd = encryptString(ALIAS_PIN, password)
        preferences.savePinCode(encryptedPwd)
    }

    fun getDbPass(): String? {
        val encryptedPwd = preferences.getDbPass()
        return if (encryptedPwd != null) decryptString(ALIAS_DB_PASS, encryptedPwd) else null
    }

    fun setDbPass(password: String) {
        val encryptedPwd = encryptString(ALIAS_DB_PASS, password)
        preferences.saveDbPass(encryptedPwd)
    }
}