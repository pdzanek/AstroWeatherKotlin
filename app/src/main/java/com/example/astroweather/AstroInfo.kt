package com.example.astroweather

import android.annotation.SuppressLint
import com.astrocalculator.AstroCalculator
import com.astrocalculator.AstroCalculator.SunInfo
import com.astrocalculator.AstroDateTime
import com.astrocalculator.AstroCalculator.MoonInfo
import org.joda.time.*
import org.joda.time.format.DateTimeFormat

internal class AstroInfo(dateTime: String, latitude: String, longitude: String) {
    private val astroDateTime: AstroDateTime
    private val astroCalculator: AstroCalculator
    private val sunInfo: SunInfo
    private val moonInfo: MoonInfo
    private var year: Int = 0
    private var month: Int = 0
    private var day: Int = 0
    private var hour: Int = 0
    private var minute: Int = 0
    private var second: Int = 0
    val sunSunriseTime: String
        get() = if (sunInfo.sunrise.toString() != sunInfo.sunset.toString()) {
            sunInfo.sunrise.toString()
        } else {
            "Brak"
        }
    val sunAzimuthRise: String
        @SuppressLint("DefaultLocale")
        get() = if (sunInfo.sunrise.toString() != sunInfo.sunset.toString()) {
            String.format("Azymut: %.4f", sunInfo.azimuthRise)
        } else {
            "Brak"
        }
    val sunSunsetTime: String
        get() = if (sunInfo.sunrise.toString() != sunInfo.sunset.toString()) {
            sunInfo.sunset.toString()
        } else {
            "Brak"
        }
    val sunAzimuthSunset: String
        @SuppressLint("DefaultLocale")
        get() = if (sunInfo.sunrise.toString() != sunInfo.sunset.toString()) {
            String.format("Azymut: %.4f", sunInfo.azimuthSet)
        } else {
            "Brak"
        }
    val sunTwilightMorning: String
        get() = sunInfo.twilightMorning.toString()
    val sunTwilightEvening: String
        get() = sunInfo.twilightEvening.toString()
    val moonriseTime: String
        get() {
            return if (moonInfo.moonrise.toString() == "null") {
                val astroDateTimeTemp = astroDateTime
                astroDateTime.day = astroDateTime.day - 1
                val astroCalculatorTemp = astroCalculator
                astroCalculatorTemp.dateTime = astroDateTimeTemp
                val moonInfoTemp = astroCalculatorTemp.moonInfo
                moonInfoTemp.moonrise.toString()
            } else
                moonInfo.moonrise.toString()
        }
    val moonset: String
        get() {
            if (moonInfo.moonset.toString() == "null") {
                val astroDateTimeTemp = astroDateTime
                astroDateTime.day = astroDateTime.day + 1
                val astroCalculatorTemp = astroCalculator
                astroCalculatorTemp.dateTime = astroDateTimeTemp
                val moonInfoTemp = astroCalculatorTemp.moonInfo
                return moonInfoTemp.moonset.toString()
            } else
                return moonInfo.moonset.toString()
        }
    val nextNewMoon: String
        get() = moonInfo.nextNewMoon.toString()
    val nextFullMoon: String
        get() = moonInfo.nextFullMoon.toString()
    val moonState: String
        get() = Math.round(moonInfo.illumination * 100).toString() + "%"

    val lunarMonth: String
        @SuppressLint("DefaultLocale")
        get() {
            val lastNewMoon = moonInfo.nextNewMoon
            val yy = lastNewMoon.year
            val MM = lastNewMoon.month
            val dd = lastNewMoon.day
            val HH = lastNewMoon.hour
            val mm = lastNewMoon.minute
            val ss = lastNewMoon.second

            val now = DateTime()
            val lastNewMoonDateTime = DateTime(yy, MM, dd, HH, mm, ss, 0)
            var duration = Minutes.minutesBetween(lastNewMoonDateTime, now).minutes / 60.0 / 24 % 29.531
            if (duration < 0) duration += 29.531
            return String.format("%.2f dnia", duration)
        }

    init {
        getDatetimeFields(dateTime)
        val timezoneoffset = 1
        val daylightSaving = true
        astroDateTime = AstroDateTime(year, month, day, hour, minute, second, timezoneoffset, daylightSaving)
        val loc =
            AstroCalculator.Location(java.lang.Double.parseDouble(latitude), java.lang.Double.parseDouble(longitude))
        astroCalculator = AstroCalculator(astroDateTime, loc)
        sunInfo = astroCalculator.sunInfo
        moonInfo = astroCalculator.moonInfo
    }

    private fun getDatetimeFields(dateTime: String) {
        val formatter = DateTimeFormat.forPattern("yyyy/MM/dd HH:mm:ss")
        val dt = formatter.parseDateTime(dateTime)
        year = dt.year
        month = dt.monthOfYear
        day = dt.dayOfMonth
        hour = dt.hourOfDay
        minute = dt.minuteOfHour
        second = dt.secondOfMinute
    }

    fun updateValues(dateTime: String) {
        getDatetimeFields(dateTime)
        astroDateTime.year = year
        astroDateTime.month = month
        astroDateTime.day = day
        astroDateTime.hour = hour
        astroDateTime.minute = minute
        astroDateTime.second = second
        astroCalculator.dateTime = astroDateTime
    }
}