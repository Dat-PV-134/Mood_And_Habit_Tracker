package com.rekoj134.moodandhabittracker.presentation.focus

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.RelativeLayout
import androidx.appcompat.widget.AppCompatImageView
import com.rekoj134.moodandhabittracker.R

class CustomFocusClock @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr, defStyleRes) {
    private val face: AppCompatImageView
    private val hour: AppCompatImageView
    private val minute: AppCompatImageView
    private val second: AppCompatImageView

    private val HOURS: Long = 3600000L
    private val MINUTES: Long = 60000L
    private val SECONDS: Long = 1000L

    init {
        inflate(context, R.layout.custom_focus_clock, this)

        face = findViewById(R.id.face)
        hour = findViewById(R.id.hour_hand)
        minute = findViewById(R.id.minute_hand)
        second = findViewById(R.id.second_hand)

        val typedArray: TypedArray = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.SimpleAnalogClock,
            defStyleAttr,
            defStyleRes
        )

        val faceDrawable: Drawable? =
            typedArray.getDrawable(R.styleable.SimpleAnalogClock_faceDrawable)
        val hourDrawable: Drawable? =
            typedArray.getDrawable(R.styleable.SimpleAnalogClock_hourDrawable)
        val minuteDrawable: Drawable? =
            typedArray.getDrawable(R.styleable.SimpleAnalogClock_minuteDrawable)
        val secondDrawable: Drawable? =
            typedArray.getDrawable(R.styleable.SimpleAnalogClock_secondDrawable)

        setFaceDrawable(faceDrawable ?: context.getDrawable(R.drawable.clock_00_face))
            .setHourDrawable(hourDrawable ?: context.getDrawable(R.drawable.clock_00_short))
            .setMinuteDrawable(minuteDrawable ?: context.getDrawable(R.drawable.clock_00_long))
            .setSecondDrawable(secondDrawable ?: context.getDrawable(R.drawable.clock_00_second))

        val faceColor: Int = typedArray.getColor(R.styleable.SimpleAnalogClock_faceTint, -1)
        val hourColor: Int = typedArray.getColor(R.styleable.SimpleAnalogClock_hourTint, -1)
        val minuteColor: Int = typedArray.getColor(R.styleable.SimpleAnalogClock_minuteTint, -1)
        val secondColor: Int = typedArray.getColor(R.styleable.SimpleAnalogClock_secondTint, -1)
        if (faceColor != -1) setFaceTint(faceColor)
        if (hourColor != -1) setHourTint(hourColor)
        if (minuteColor != -1) setMinuteTint(minuteColor)
        if (secondColor != -1) setSecondTint(secondColor)

        rotateHourHand(typedArray.getFloat(R.styleable.SimpleAnalogClock_hourRotation, 0f))
        rotateMinuteHand(typedArray.getFloat(R.styleable.SimpleAnalogClock_minuteRotation, 0f))
        rotateSecondHand(typedArray.getFloat(R.styleable.SimpleAnalogClock_secondRotation, 0f))
    }

    fun setFaceDrawable(drawable: Drawable?): CustomFocusClock {
        face.setImageDrawable(drawable)
        return this
    }

    fun setHourDrawable(drawable: Drawable?): CustomFocusClock {
        hour.setImageDrawable(drawable)
        return this
    }

    fun setMinuteDrawable(drawable: Drawable?): CustomFocusClock {
        minute.setImageDrawable(drawable)
        return this
    }

    fun setSecondDrawable(drawable: Drawable?): CustomFocusClock {
        second.setImageDrawable(drawable)
        return this
    }

    fun rotateHourHand(angle: Float): CustomFocusClock {
        hour.rotation = angle
        return this
    }

    fun rotateMinuteHand(angle: Float): CustomFocusClock {
        minute.rotation = angle
        return this
    }

    fun rotateSecondHand(angle: Float): CustomFocusClock {
        second.rotation = angle
        return this
    }

    fun setTime(hour: Int, min: Int, seconds: Int, millis: Int): CustomFocusClock {
        val hourMillis: Long = hour.toLong() * HOURS
        val minMillis: Long = min.toLong() * MINUTES
        val secondMillis: Long = seconds.toLong() * SECONDS

        rotateHourHand((0.0000083 * (hourMillis + minMillis + secondMillis + millis)).toFloat())
        rotateMinuteHand((0.0001 * (minMillis + secondMillis + millis)).toFloat())
        rotateSecondHand((0.006 * (secondMillis + millis)).toFloat())
        return this
    }

    fun setTime(hour: Int, min: Int, seconds: Int): CustomFocusClock {
        return setTime(hour, min, seconds, 0)
    }

    fun setFaceTint(color: Int): CustomFocusClock {
        face.setColorFilter(color)
        return this
    }

    fun setHourTint(color: Int): CustomFocusClock {
        hour.setColorFilter(color)
        return this
    }

    fun setMinuteTint(color: Int): CustomFocusClock {
        minute.setColorFilter(color)
        return this
    }

    fun setSecondTint(color: Int): CustomFocusClock {
        second.setColorFilter(color)
        return this
    }
}
