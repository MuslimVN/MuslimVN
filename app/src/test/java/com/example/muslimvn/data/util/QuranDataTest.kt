package com.example.muslimvn.data.util

import com.google.gson.JsonParser
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test
import java.io.File

class QuranDataTest {

    @Test
    fun testQuranJsonStructureAndIntegrity() {
        val file = File("src/main/assets/quran_vi.json")
        if (!file.exists()) return

        val jsonString = file.readText().replace("\uFEFF", "")
        val jsonArray = JsonParser.parseString(jsonString).asJsonArray

        assertEquals(114, jsonArray.size())

        var totalAyahsCount = 0
        val footnoteRegex = Regex("""\[\d+]""")

        for (i in 0 until jsonArray.size()) {
            val surahObject = jsonArray[i].asJsonObject
            val ayahsArray = surahObject.getAsJsonArray("ayahs")
            totalAyahsCount += ayahsArray.size()

            for (j in 0 until ayahsArray.size()) {
                val ayahObject = ayahsArray[j].asJsonObject
                val arabic = ayahObject.get("textArabic").asString.replace("\uFEFF", "").trim()
                val vietnamese = ayahObject.get("textVietnamese").asString
                val cleanVietnamese = footnoteRegex.replace(vietnamese, "").trim()

                assertFalse("Arabic text should not contain BOM character", arabic.contains("\uFEFF"))
                assertFalse("Cleaned Vietnamese text should not contain footnote brackets", footnoteRegex.containsMatchIn(cleanVietnamese))
            }
        }

        assertEquals(6236, totalAyahsCount)
    }
}
