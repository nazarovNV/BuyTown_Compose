package com.example.buytowncompose.data.source

import android.content.Context
import android.util.Base64
import java.security.MessageDigest
import java.security.SecureRandom
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

/** Local demo accounts. Passwords are stored as salted hashes. */
class LocalAccounts(context: Context) {
    private val preferences = context.getSharedPreferences("local_accounts", Context.MODE_PRIVATE)

    fun exists(login: String): Boolean = login.trim() == "demo" || preferences.contains(login.trim())

    fun register(login: String, password: String): Boolean {
        val username = login.trim()
        if (username.isBlank() || password.isBlank() || username == "demo" || preferences.contains(username)) return false
        val salt = ByteArray(16).also { SecureRandom().nextBytes(it) }
        return preferences.edit().putString(username, "${encode(salt)}:${encode(hash(password, salt))}").commit()
    }

    fun authenticate(login: String, password: String): Boolean {
        val username = login.trim()
        if (username == "demo") return password == "demo123"
        val stored = preferences.getString(username, null) ?: return false
        val parts = stored.split(':')
        if (parts.size != 2) return false
        return MessageDigest.isEqual(decode(parts[1]), hash(password, decode(parts[0])))
    }

    private fun hash(password: String, salt: ByteArray): ByteArray {
        val spec = PBEKeySpec(password.toCharArray(), salt, 65536, 256)
        return try {
            SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1").generateSecret(spec).encoded
        } finally {
            spec.clearPassword()
        }
    }

    private fun encode(bytes: ByteArray) = Base64.encodeToString(bytes, Base64.NO_WRAP)
    private fun decode(text: String) = Base64.decode(text, Base64.NO_WRAP)
}
