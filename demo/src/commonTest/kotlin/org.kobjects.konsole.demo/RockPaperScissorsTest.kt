package org.kobjects.konsole.demo

import kotlin.test.Test
import kotlin.test.assertEquals

class RockPaperScissorsTest {

    @Test
    fun testParsing() {
        assertEquals(null, parse("foobar"))
        assertEquals(Choice.ROCK, parse("r"))
    }
}