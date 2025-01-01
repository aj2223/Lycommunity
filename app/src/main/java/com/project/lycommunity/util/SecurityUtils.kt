package com.project.lycommunity.util

import at.favre.lib.crypto.bcrypt.BCrypt

object SecurityUtils {
    fun hashPassword(password: String): String {
        return BCrypt.withDefaults()
            .hashToString(12, password.toCharArray()) // 12 is the cost factor
    }

    fun verifyPassword(password: String, hashedPassword: String): Boolean {
        val result = BCrypt.verifyer().verify(password.toCharArray(), hashedPassword)
        return result.verified
    }
}