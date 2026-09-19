package com.example.muslimvn.data.util

import android.content.Context
import com.example.muslimvn.data.local.entities.AyahEntity
import com.example.muslimvn.data.local.entities.SurahEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import org.json.JSONArray
import javax.inject.Inject

class QuranJsonParser @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val footnoteRegex = Regex("""\[\d+]""")

    fun parseQuranData(): Pair<List<SurahEntity>, List<AyahEntity>> {
        val surahs = mutableListOf<SurahEntity>()
        val ayahs = mutableListOf<AyahEntity>()

        try {
            val jsonString = context.assets.open("quran_vi.json")
                .bufferedReader()
                .use { it.readText() }
                .replace("\uFEFF", "")

            val jsonArray = JSONArray(jsonString)

            for (i in 0 until jsonArray.length()) {
                val surahObject = jsonArray.getJSONObject(i)
                val surahNumber = surahObject.getInt("number")
                
                surahs.add(
                    SurahEntity(
                        number = surahNumber,
                        nameArabic = surahObject.getString("nameArabic").replace("\uFEFF", "").trim(),
                        nameVietnamese = surahObject.getString("nameVietnamese").trim(),
                        totalAyahs = surahObject.getInt("totalAyahs"),
                        revelationType = surahObject.getString("revelationType")
                    )
                )

                val ayahsArray = surahObject.getJSONArray("ayahs")
                for (j in 0 until ayahsArray.length()) {
                    val ayahObject = ayahsArray.getJSONObject(j)
                    val rawArabic = ayahObject.getString("textArabic").replace("\uFEFF", "").trim()
                    val rawVietnamese = ayahObject.getString("textVietnamese")
                    val cleanVietnamese = footnoteRegex.replace(rawVietnamese, "").trim()

                    ayahs.add(
                        AyahEntity(
                            surahId = surahNumber,
                            ayahNumber = ayahObject.getInt("number"),
                            textArabic = rawArabic,
                            textVietnamese = cleanVietnamese
                        )
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return Pair(surahs, ayahs)
    }
}
