package org.springframework.samples.petclinic.test

import org.assertj.core.api.Assertions.assertThat
import kotlin.text.MatchResult.Destructured

class RegexMatcher(private val regex: Regex) {
    private lateinit var _destructed: Destructured

    val destructed: Destructured
        get() = _destructed

    fun match(value: String): Destructured {
        val matchResult = regex.matchEntire(value)
        assertThat(matchResult).isNotNull

        _destructed = matchResult!!.destructured
        return _destructed
    }


}
