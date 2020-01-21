package com.junio.steps_progress_view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.junio.library.ProgressBarSteps
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), ProgressBarSteps.OnListener {
    private var listImage: MutableList<Int> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progressbarSteps.post { int() }
    }

    private fun int() {
        listImage.add(R.drawable.step_one)
        listImage.add(R.drawable.step_two)
        listImage.add(R.drawable.step_three)
        listImage.add(R.drawable.step_four)

        progressbarSteps.setListener(this)
        progressbarSteps.startProgress()
    }

    override fun changeStep(position: Int) {
        image.setImageResource(listImage[position])
    }

    override fun finishSteps() {
    }
}
