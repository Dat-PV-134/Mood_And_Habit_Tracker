package com.rekoj134.moodandhabittracker.presentation.focus

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

class CustomProgressCircle : View {
    private var remainingPercent = 0f

    fun setRemaining(percent: Float) {
        remainingPercent = percent
        invalidate()
    }

    private val slicePaint = Paint().apply {
        color = Color.parseColor("#4DFA9999")
        style = Paint.Style.FILL
    }

    private val sliceRect = RectF()

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val centerX = width / 2f
        val centerY = height / 2f
        val radius = (width.coerceAtMost(height) / 2f)

        // Define the path for the slice
        sliceRect.set(
            centerX - radius,
            centerY - radius,
            centerX + radius,
            centerY + radius
        )

        // Draw the slice
        val angle = remainingPercent * 360 / 100f

        canvas.rotate(360f - angle, centerX, centerY)
        canvas.drawArc(sliceRect, 270f, angle, true, slicePaint)
    }
}