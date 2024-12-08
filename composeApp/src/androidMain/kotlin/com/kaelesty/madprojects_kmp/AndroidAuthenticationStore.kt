package com.kaelesty.madprojects_kmp

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.kaelesty.data.auth.store.AuthenticationContext
import com.kaelesty.data.auth.store.AuthenticationStore
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.SecretKeyFactory
import java.util.Base64

class AndroidAuthenticationStore(
    context: Context,
): AuthenticationStore {

    private val PREFS_STORAGE = "user_prefs"
    private val AU_CONTEXT = "au_context"

    private val sharedPreferences = context
        .getSharedPreferences(PREFS_STORAGE, Context.MODE_PRIVATE)

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun get(): AuthenticationContext? {
        val json = sharedPreferences.getString(AU_CONTEXT, null)
        return try {
            json?.let { Json.decodeFromString(json.decrypt(AuthSecret.key, AuthSecret.salt)) }
        } catch (e: Exception) {
            e
            null
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun save(new: AuthenticationContext) {
        sharedPreferences.edit().putString(
            AU_CONTEXT,
            Json.encodeToString(new).encrypt(AuthSecret.key, AuthSecret.salt)
        ).apply()
    }

    override suspend fun updateToken(new: String) {
        TODO("Not yet implemented")
    }

    override suspend fun drop() {
        sharedPreferences.edit().putString(AU_CONTEXT, "").apply()
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun String.encrypt(key: String, salt: String): String {
    val secretKey = generateKey(key, salt)
    val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
    val iv = ByteArray(16)
    cipher.init(Cipher.ENCRYPT_MODE, secretKey, IvParameterSpec(iv))

    val encryptedBytes = cipher.doFinal(this.toByteArray(Charsets.UTF_8))
    return Base64.getEncoder().encodeToString(encryptedBytes)
}

@RequiresApi(Build.VERSION_CODES.O)
fun String.decrypt(key: String, salt: String): String {
    val secretKey = generateKey(key, salt)
    val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
    val iv = ByteArray(16)
    cipher.init(Cipher.DECRYPT_MODE, secretKey, IvParameterSpec(iv))

    val decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(this))
    return String(decryptedBytes, Charsets.UTF_8)
}

private fun generateKey(key: String, salt: String): SecretKeySpec {
    val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
    val spec = PBEKeySpec(key.toCharArray(), salt.toByteArray(), 65536, 256)
    val secretKey = factory.generateSecret(spec)
    return SecretKeySpec(secretKey.encoded, "AES")
}