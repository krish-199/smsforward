package com.pierreduchemin.smsforward

import org.junit.Test
import java.util.regex.Pattern
import org.junit.Assert.assertTrue
import org.junit.Assert.assertFalse

class RegexTest {
    @Test
    fun testRegexMatches() {
        val regex = "AXISBk"
        val source = "CM-AXISBk"

        // Behavior after change: use find() and CASE_INSENSITIVE | UNICODE_CASE
        val pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE or Pattern.UNICODE_CASE)
        val matcher = pattern.matcher(source)

        assertTrue("Should find AXISBk in CM-AXISBk", matcher.find())
    }

    @Test
    fun testCaseInsensitive() {
        val regex = "axisbk"
        val source = "CM-AXISBk"
        val pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE or Pattern.UNICODE_CASE)
        val matcher = pattern.matcher(source)

        assertTrue("Should find axisbk in CM-AXISBk case-insensitively", matcher.find())
    }

    @Test
    fun testExactMatchStillWorks() {
        val regex = "^CM-AXISBk$"
        val source = "CM-AXISBk"
        val nonExactSource = "X-CM-AXISBk-Y"
        val pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE or Pattern.UNICODE_CASE)

        val matcher = pattern.matcher(source)
        assertTrue("Should match exactly if anchors are used", matcher.find())

        val nonExactMatcher = pattern.matcher(nonExactSource)
        assertFalse(
            "Anchored regex should reject non-exact source",
            nonExactMatcher.find()
        )
    }
}
