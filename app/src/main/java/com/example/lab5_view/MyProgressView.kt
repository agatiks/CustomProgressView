package com.example.lab5_view

import android.animation.ValueAnimator
import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.DecelerateInterpolator

class MyProgressView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet, defStyleAttr: Int = 0, defStyleRes: Int = 0,
) :
    View(context, attrs, defStyleAttr, defStyleRes) {
    private val cstate: Int = 0
    private var valueColor: Int
    private var shineColor: Int
    private var size: Int
    private var progress: Int

        init {
            val at: TypedArray = context.obtainStyledAttributes(
                attrs,
                R.styleable.MyProgressView,
                defStyleAttr,
                defStyleRes
            )
            try {
                valueColor = at.getColor(R.styleable.MyProgressView_valueColor, 0)
                shineColor = at.getColor(R.styleable.MyProgressView_shineColor, 1)
                size = at.getDimensionPixelSize(R.styleable.MyProgressView_size, 0)
                progress = at.getInt(R.styleable.MyProgressView_progress, 0)
            } finally {
                at.recycle()
            }
        }
    private var circleScale: Float = 0f
        set(value){
            field = value
            invalidate()
        }
    private var gradientScale: Float = 0f
        set(value){
            field = value
            invalidate()
        }

    //Animation
    private var gradientAnimator = ValueAnimator.ofFloat(1f, 100f).apply {
        duration = 1000L
        addUpdateListener {
            gradientScale = it.animatedValue as Float
        }
        interpolator = DecelerateInterpolator()
        repeatCount = ValueAnimator.INFINITE
    }
    private var circleAnimator = ValueAnimator.ofFloat(0f, 100f).apply {
        duration = 4000L
        addUpdateListener {
            circleScale = it.animatedValue as Float
        }
        repeatCount = ValueAnimator.INFINITE
        repeatMode = ValueAnimator.REVERSE
    }
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        circleAnimator.start()
        gradientAnimator.start()
    }

    private val paint = Paint()


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val save = canvas.save()
        val cx = width/2f
        val cy = height/2f
        Log.i("DEB", "${cx}+${cy}")
        val rad = circleScale/100*size+1
        Log.i("DEB", "${rad}")

        paint.shader = //LinearGradient(0f, 0f, 100f, 20f, Color.RED, Color.GREEN, Shader.TileMode.MIRROR)
            RadialGradient(
                cx, cy, rad * gradientScale / 100, intArrayOf(
                    valueColor,
                    Color.WHITE,
                    valueColor
                ), floatArrayOf(0f, rad * gradientScale / 100, rad), Shader.TileMode.CLAMP
            )
        //paint.color = valueColor
        canvas.drawCircle(width / 2f, height / 2f, rad, paint)
        canvas.restoreToCount(save)
    }

    private class MyProgressState: BaseSavedState {
        var progress = 0L
        var gradient = 0L
        var val_prog = 0F
        var val_grad = 0F
        //var prAnimator = null;
        constructor(superState: Parcelable?) : super(superState)
        constructor(parcel: Parcel) : super(parcel) {
            progress = parcel.readLong()
            gradient = parcel.readLong()
            val_prog = parcel.readFloat()
            val_grad = parcel.readFloat()
        }

        override fun writeToParcel(out: Parcel?, flags: Int) {
            super.writeToParcel(out, flags)
            out?.writeLong(progress)
            out?.writeLong(gradient)
            out?.writeFloat(val_prog)
            out?.writeFloat(val_grad)
        }
        companion object {
            @JvmField
            val CREATOR = object : Parcelable.Creator<MyProgressState> {
                override fun createFromParcel(source: Parcel): MyProgressState = MyProgressState(source)
                override fun newArray(size: Int): Array<MyProgressState?> = arrayOfNulls(size)
            }
        }

    }

    override fun onSaveInstanceState(): Parcelable? {
        val state = MyProgressState(super.onSaveInstanceState())
        state.progress = circleAnimator.currentPlayTime
        state.gradient = gradientAnimator.currentPlayTime
        state.val_grad = gradientScale
        state.val_prog = circleScale
        circleAnimator.cancel()
        gradientAnimator.cancel()
        return state
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        state as MyProgressState
        super.onRestoreInstanceState(state)
        circleScale = state.val_prog
        gradientScale = state.val_grad
        circleAnimator.currentPlayTime = state.progress
        gradientAnimator.currentPlayTime = state.gradient
        circleAnimator.start()
        gradientAnimator.start()
    }
}