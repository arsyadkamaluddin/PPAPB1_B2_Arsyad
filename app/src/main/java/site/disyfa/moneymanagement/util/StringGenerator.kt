package site.disyfa.moneymanagement.util

import java.security.MessageDigest
import java.security.SecureRandom
import java.text.DecimalFormat
import java.util.UUID

class StringGenerator {
    companion object {
        // Menggunakan UUID
        fun generateUUIDString(): String {
            return UUID.randomUUID().toString().replace("-", "").take(24)
        }

        // Menggunakan Secure Random dan Hashing
        fun generateHashedString(): String {
            val randomBytes = ByteArray(16)
            SecureRandom().nextBytes(randomBytes)
            val hash = MessageDigest.getInstance("SHA-256").digest(randomBytes)
            return hash.joinToString("") { "%02x".format(it) }.take(24)
        }

        fun formatToRupiah(amount: Int): String {
            val formatter = DecimalFormat("#,###")
            return "Rp ${formatter.format(amount)}"
        }
    }
}
