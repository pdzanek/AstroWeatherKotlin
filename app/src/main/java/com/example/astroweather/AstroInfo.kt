package com.example.astroweather

import android.annotation.SuppressLint
import com.astrocalculator.AstroCalculator
import com.astrocalculator.AstroDateTime
import org.joda.time.*
import org.joda.time.format.DateTimeFormat

internal class AstroInfo(dateTime: String, latitude: String, longitude: String) {
    private val astroDateTime: AstroDateTime
    private val astroCalculator: AstroCalculator
    private var year: Int = 0
    private var month: Int = 0
    private var day: Int = 0
    private var hour: Int = 0
    private var minute: Int = 0
    private var second: Int = 0
    val sunSunriseTime: String
        get() = if (astroCalculator.sunInfo.sunrise.toString() != astroCalculator.sunInfo.sunset.toString()) {
            astroCalculator.sunInfo.sunrise.toString()
        } else {
            "Brak"
        }
    val sunAzimuthRise: String
        @SuppressLint("DefaultLocale")
        get() = if (astroCalculator.sunInfo.sunrise.toString() != astroCalculator.sunInfo.sunset.toString()) {
            String.format("Azymut: %.4f", astroCalculator.sunInfo.azimuthRise)
        } else {
            "Brak"
        }
    val sunSunsetTime: String
        get() = if (astroCalculator.sunInfo.sunrise.toString() != astroCalculator.sunInfo.sunset.toString()) {
            astroCalculator.sunInfo.sunset.toString()
        } else {
            "Brak"
        }
    val sunAzimuthSunset: String
        @SuppressLint("DefaultLocale")
        get() = if (astroCalculator.sunInfo.sunrise.toString() != astroCalculator.sunInfo.sunset.toString()) {
            String.format("Azymut: %.4f", astroCalculator.sunInfo.azimuthSet)
        } else {
            "Brak"
        }
    val sunTwilightMorning: String
        get() = astroCalculator.sunInfo.twilightMorning.toString()
    val sunTwilightEvening: String
        get() = astroCalculator.sunInfo.twilightEvening.toString()
    val moonriseTime: String
        get() {
            return if ((""+astroCalculator.moonInfo.moonrise).contains("null",ignoreCase = true)) {
                val astroDateTimeTemp = astroDateTime
                astroDateTimeTemp.day = astroDateTime.day - 1
                val astroCalculatorTemp = astroCalculator
                astroCalculatorTemp.dateTime = astroDateTimeTemp
                val moonInfoTemp = astroCalculatorTemp.moonInfo
                ""+moonInfoTemp.moonrise
            } else
                ""+astroCalculator.moonInfo.moonrise
        }
    val moonset: String
        get(){
            return if ((""+astroCalculator.moonInfo.moonset).contains("null",ignoreCase = true)) {
                val astroDateTimeTemp = astroCalculator.dateTime
                astroDateTimeTemp.day=astroDateTime.day+1
                val astroCalculatorTemp = AstroCalculator(astroDateTimeTemp,astroCalculator.location)
                val moonInfoTemp = astroCalculatorTemp.moonInfo
                return ""+moonInfoTemp.moonset
            } else
                "" + astroCalculator.moonInfo.moonset
        }
    val nextNewMoon: String
        get() = ""+astroCalculator.moonInfo.nextNewMoon
    val nextFullMoon: String
        get() = ""+astroCalculator.moonInfo.nextFullMoon
    val moonState: String
        get() = ""+Math.round(astroCalculator.moonInfo.illumination * 100) + "%"

    val lunarMonth: String
        @SuppressLint("DefaultLocale")
        get() {
            val lastNewMoon = astroCalculator.moonInfo.nextNewMoon
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
        val loc = AstroCalculator.Location(java.lang.Double.parseDouble(latitude), java.lang.Double.parseDouble(longitude))
        astroCalculator = AstroCalculator(astroDateTime, loc)
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

    fun updateValues(dateTime: String, latitude: String,longitude: String) {
        getDatetimeFields(dateTime)
        astroDateTime.year = year
        astroDateTime.month = month
        astroDateTime.day = day
        astroDateTime.hour = hour
        astroDateTime.minute = minute
        astroDateTime.second = second
        astroCalculator.dateTime = astroDateTime
        astroCalculator.location=AstroCalculator.Location(java.lang.Double.parseDouble(latitude), java.lang.Double.parseDouble(longitude))
    }
}