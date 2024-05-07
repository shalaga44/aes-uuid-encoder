# UUID Encryption with AES

A Kotlin project that encrypts and decrypts UUIDs using AES encryption with Base64 encoding.

## Features

- **AES Encryption**: Uses the AES algorithm with CBC mode for secure encryption.
- **UUID Conversion**: Converts encrypted data back to its original UUID format.


### Usage

1. Generate an AES key and encrypt a UUID:
```kotlin
val aesKey = generateAESKey()
val originalUuid = UUID.randomUUID()
val encryptedUuidBase64 = encryptUuidToBase64(originalUuid, aesKey)
```

2. Decrypt an encrypted UUID:
```kotlin
val decryptedUuid = decryptBase64ToUuid(encryptedUuidBase64, aesKey).let { UUID.fromString(it) }
```

## License

This project is licensed under the MIT License.
