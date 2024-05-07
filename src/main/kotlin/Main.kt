package dev.shalaga44

import java.nio.charset.StandardCharsets
import java.security.SecureRandom
import java.util.*
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

fun generateAESKey(): SecretKey {
    val keyGen = KeyGenerator.getInstance("AES")
    keyGen.init(256)
    return keyGen.generateKey()
}

fun encryptUuidToBase64(uuid: UUID, secretKey: SecretKey): String {

    val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
    val iv = ByteArray(16)
    SecureRandom().nextBytes(iv)
    val ivSpec = IvParameterSpec(iv)


    cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec)


    val uuidBytes = uuid.toString().toByteArray(Charsets.UTF_8)
    val encryptedBytes = cipher.doFinal(uuidBytes)


    val combined = iv + encryptedBytes
    return Base64.getEncoder().encodeToString(combined)
}

fun decryptBase64ToUuid(encryptedBase64: String, secretKey: SecretKey): String {

    val combined = Base64.getDecoder().decode(encryptedBase64)
    val iv = combined.sliceArray(0 until 16)
    val encryptedBytes = combined.sliceArray(16 until combined.size)


    val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
    val ivSpec = IvParameterSpec(iv)
    cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec)


    val decryptedBytes = cipher.doFinal(encryptedBytes)
    return String(decryptedBytes, StandardCharsets.UTF_8)
}

fun main() {

    val aesKey = generateAESKey()


    repeat(1000) {

        val originalUuid = UUID.randomUUID()
        val encryptedUuidBase64 = encryptUuidToBase64(originalUuid, aesKey)
        val decryptedUuid = decryptBase64ToUuid(encryptedUuidBase64, aesKey).let { UUID.fromString(it) }

        println("Original  UUID: $originalUuid")
        println("Encrypted UUID (Base64): $encryptedUuidBase64")
        println("Decrypted UUID: $decryptedUuid")
        assert(originalUuid == decryptedUuid)
    }
}
