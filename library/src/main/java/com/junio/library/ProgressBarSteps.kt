package com.junio.library

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.LinearLayout


class ProgressBarSteps : LinearLayout, View.OnTouchListener, View.OnClickListener {
    companion object {
        private val PROGRESS_STEP_LAYOUT_PARAM = LayoutParams(0, LayoutParams.WRAP_CONTENT, 1f)
        private val SPACE_LAYOUT_PARAM = LayoutParams(5, LayoutParams.WRAP_CONTENT)
    }

    private var stepsCount: Int = 0
    private var stepBackgroundColor: Int = 0
    private var stepForegroundColor: Int = 0

    private var steps: MutableList<ProgressStep> = ArrayList()
    private var currentStep: Int = 0
    private var pressTime: Long = 0L
    private var limit: Long = 500L
    private var positionX: Int = 0
    private var positionY: Int = 0
    private var blockAnimationEnd: Boolean = false

    private lateinit var objectAnimator: ValueAnimator
    private lateinit var viewChangeSteps: View
    private var listener: OnListener? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet) {
        init(context, attributeSet)
    }

    private fun init(context: Context, attributeSet: AttributeSet?) {
        orientation = HORIZONTAL
        setOnTouchListener(this)
        setOnClickListener(this)

        getAttributes(context, attributeSet)
        bindViews()
    }

    private fun getAttributes(context: Context, attributeSet: AttributeSet?) {
        val typedArray =
            context.obtainStyledAttributes(attributeSet, R.styleable.ProgressBarSteps, 0, 0)

        stepsCount = typedArray.getInt(R.styleable.ProgressBarSteps_pbsStepsCount, 0)
        stepBackgroundColor =
            typedArray.getColor(R.styleable.ProgressBarSteps_pbsBackgroundColor, Color.GRAY)
        stepForegroundColor =
            typedArray.getColor(R.styleable.ProgressBarSteps_pbsForegroundColor, Color.WHITE)

        typedArray.recycle()
    }

    private fun bindViews() {
        steps.clear()
        removeAllViews()

        for (i in 0 until stepsCount) {
            val step = createStep()

            steps.add(step)
            addView(step)

            if ((i + 1) < stepsCount) {
                addView(createSpace())
            }
        }
    }

    private fun createStep(): ProgressStep {
        val progressStep = ProgressStep(context, null, stepBackgroundColor, stepForegroundColor)
        progressStep.layoutParams = PROGRESS_STEP_LAYOUT_PARAM

        return progressStep
    }

    private fun createSpace(): View {
        val view = View(context)
        view.layoutParams = SPACE_LAYOUT_PARAM

        return view
    }

    override fun onTouch(view: View?, event: MotionEvent?): Boolean {
        when (event!!.action) {
            MotionEvent.ACTION_DOWN -> {
                positionX = event.x.toInt()
                positionY = event.y.toInt()

                pressTime = System.currentTimeMillis()
                pause()
                return false
            }

            MotionEvent.ACTION_UP -> {
                val now = System.currentTimeMillis()
                resume()
                return limit < now - pressTime
            }
        }
        return false
    }

    override fun onClick(view: View?) {
        if (positionX <= (rootView.width / 2)) {
            clickReverse()
            return
        }

        clickSkip()
    }

    fun startProgress() {
        listener?.changeStep(currentStep)

        objectAnimator = ObjectAnimator.ofFloat(0f, 1f)
        objectAnimator.apply {
            addUpdateListener { anim ->
                steps[currentStep].updateProgress(anim.animatedValue as Float)
            }

            addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animator: Animator?) {
                }

                override fun onAnimationEnd(animator: Animator?) {
                    if (blockAnimationEnd) {
                        return
                    }

                    if (currentStep == (stepsCount - 1)) {
                        listener?.finishSteps()
                        return
                    }

                    currentStep++

                    listener?.changeStep(currentStep)
                    animator?.start()
                }

                override fun onAnimationCancel(animator: Animator?) {
                }

                override fun onAnimationStart(animator: Animator?) {
                }
            })

            duration = 5000
            interpolator = AccelerateDecelerateInterpolator()
            start()
        }
    }

    private fun pause() {
        objectAnimator.pause()
    }

    private fun resume() {
        objectAnimator.resume()
    }

    private fun skip() {
        if (currentStep == (stepsCount - 1)) {
            steps[currentStep].updateProgress(1f)
            objectAnimator.cancel()
            return
        }

        blockAnimationEnd = true

        objectAnimator.cancel()
        steps[currentStep].updateProgress(1f)
        currentStep++
        startProgress()

        blockAnimationEnd = false
    }

    private fun reverse() {
        if (currentStep == 0) {
            return
        }

        blockAnimationEnd = true

        objectAnimator.cancel()
        steps[currentStep].updateProgress(0f)
        currentStep--
        startProgress()

        blockAnimationEnd = false
    }

    private fun clickReverse() {
        reverse()
    }

    private fun clickSkip() {
        skip()
    }

//    fun setRootView(view: View) {
//        viewChangeSteps = view
//
//        viewChangeSteps.setOnTouchListener(this)
//        viewChangeSteps.setOnClickListener(this)
//    }

    fun setListener(listener: OnListener?) {
        this.listener = listener
    }

    interface OnListener {
        fun changeStep(position: Int)
        fun finishSteps()
    }
}