package com.junio.library

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class ProgressStep : View {
    private lateinit var backViewPaint: Paint
    private lateinit var progressViewPaint: Paint

    var stepBackgroundColor: Int
    var stepForegroundColor: Int


    private var progress = 0f

    constructor(context: Context) : this(context, null)

    constructor(
        context: Context,
        attributeSet: AttributeSet?,
        stepBackgroundColor: Int = Color.GRAY,
        stepForegroundColor: Int = Color.WHITE
    ) : super(context, attributeSet) {
        this.stepBackgroundColor = stepBackgroundColor
        this.stepForegroundColor = stepForegroundColor

        init()
    }

    private fun init() {
        backViewPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        backViewPaint.color = stepBackgroundColor
        backViewPaint.strokeWidth = 8f
        backViewPaint.style = Paint.Style.STROKE

        progressViewPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        progressViewPaint.color = stepForegroundColor
        progressViewPaint.strokeWidth = 8f
        progressViewPaint.style = Paint.Style.STROKE
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val widthStepProgress = (width * progress)

        canvas?.drawLine(0f, 0f, width.toFloat(), 0f, backViewPaint)
        canvas?.drawLine(0f, 0f, widthStepProgress, 0f, progressViewPaint)
    }

    fun updateProgress(progress: Float) {
        this.progress = progress
        invalidate()
    }
}