package com.guralnya.notification_tracker.model.settings

import android.content.Context
import android.security.KeyPairGeneratorSpec
import android.util.Base64
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
        private const val ALIAS = "NotificationTracker"
        private const val CIPHER_TYPE = "RSA/ECB/PKCS1Padding"
        private const val ALGORITHM = "RSA"
        private const val OPEN_SSL_CIPHER_PROVIDE = "AndroidOpenSSL"
        private const val BCWORKAROUND_CIPHER_PROVIDE = "AndroidKeyStoreBCWorkaround"
    }

    init {
        initKeyStore()
        createKey()
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

    private fun createKey() {
        try {
            if (!keyStore.containsAlias(ALIAS)) {
                val start = Calendar.getInstance()
                val end = Calendar.getInstance()
                end.add(Calendar.YEAR, 5)
                val spec = KeyPairGeneratorSpec.Builder(context)
                    .setAlias(ALIAS)
                    .setSubject(X500Principal("CN=$ALIAS, O=Android Authority"))
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


    private fun encryptString(string: String): String? {
        try {
            val privateKeyEntry = keyStore.getEntry(ALIAS, null) as KeyStore.PrivateKeyEntry
            val publicKey = privateKeyEntry.certificate.publicKey as RSAPublicKey
            val encrypt = getCipher()
            encrypt.init(Cipher.ENCRYPT_MODE, publicKey)
            val outputStream = ByteArrayOutputStream()
            val cipherOutputStream = CipherOutputStream(
                outputStream, encrypt
            )
            cipherOutputStream.write(string.toByteArray(Charsets.UTF_8))
            cipherOutputStream.close()
            val bytes = outputStream.toByteArray()
            return Base64.encodeToString(bytes, Base64.DEFAULT)
        } catch (e: Exception) {
            //TODO: Add log
        }
        return null
    }

    private fun decryptString(string: String): String? {
        try {
            val privateKeyEntry = keyStore.getEntry(ALIAS, null) as KeyStore.PrivateKeyEntry
            val decrypt = getCipher()
            decrypt.init(Cipher.DECRYPT_MODE, privateKeyEntry.privateKey)
            val cipherInputStream = CipherInputStream(
                ByteArrayInputStream(Base64.decode(string, Base64.DEFAULT)), decrypt
            )
            val bytes = cipherInputStream.readBytes()
            return String(bytes, 0, bytes.size, Charsets.UTF_8)

        } catch (e: Exception) {
            //TODO: Add log
        }
        return null
    }

    fun deleteKey() {
        keyStore.deleteEntry(ALIAS)
    }

    fun getPassword(): String? {
        val encryptedPwd = preferences.getPinCode()
        return if (encryptedPwd != null) decryptString(encryptedPwd) else null
    }

    fun setPassword(password: String) {
        val encryptedPwd = encryptString(password)
        preferences.savePinCode(encryptedPwd)
    }
}