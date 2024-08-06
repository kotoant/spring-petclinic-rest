package org.springframework.samples.petclinic.util

import org.apache.commons.lang3.RandomStringUtils

object Util {
    fun getRandomString(length: Int): String = RandomStringUtils.randomAlphabetic(length)
    fun getRandomStrings(count: Int, length: Int): List<String> = (1..count).map { getRandomString(length) }
}
