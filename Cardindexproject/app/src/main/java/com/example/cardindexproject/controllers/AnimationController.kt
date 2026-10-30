package com.example.cardindexproject.controllers

import android.content.Context
import androidx.annotation.DrawableRes
import pl.droidsonroids.gif.GifDrawable

class AnimationController {
    private var idGifIcon: Int
    private var context: Context
    private var gifDrawable: GifDrawable
    private var threadCheckAnim: ThreadCheckAnim? = null

    // Позиции состояний анимации
    private var arrayGifPositions: Array<Int> = arrayOf(0)
    private var gifPosition: Int = 0
    private var nextGifPosition: Int = 0

    /** * Constructor for animation controller.
     * @param context Context.
     * @param idGifIcon Drawable resource id of gif. */
    constructor(context: Context, @DrawableRes idGifIcon: Int) {
        this.context = context
        this.idGifIcon = idGifIcon
        this.gifDrawable = GifDrawable(context.resources, idGifIcon)
        this.gifDrawable.stop()
    }

    /** * Initializing gif positions of animation. */
    fun setGifPositions(arrayGifPositions: Array<Int>) {
        if (arrayGifPositions.isEmpty()) throw Exception("No data in arrayGifPositions")
        val durationAnimation = GifDrawable(context.resources, idGifIcon).duration
        for (i in arrayGifPositions.indices)
            if (arrayGifPositions[i] > durationAnimation || arrayGifPositions[i] < 0)
                throw Exception("Bad gif position of $i element in arrayGifPositions")
        this.arrayGifPositions = arrayGifPositions
        this.gifDrawable.seekTo(arrayGifPositions[0])
    }

    /** * Get GifDrawable of gif.
     * @return GifDrawable of gif. */
    fun getGifDrawable(): GifDrawable {
        return gifDrawable
    }

    /** * Get animation state of gif (true is active, false is disable).
     * @return Animation state of gif (true is active, false is disable). */
    fun getAnimState(): Boolean {
        threadCheckAnim?.let { return it.isAlive }; return false
    }

    /** * Start animation.
     * @return New position of animation. */
    fun startAnimation(): Int {
        threadCheckAnim?.let { if (it.isAlive) return -1 }
        if (arrayGifPositions.isEmpty()) throw Exception("Please use setGifPositions function for init positions of gif animation")
        nextGifPosition = (gifPosition + 1) % arrayGifPositions.size
        startAnimThread()
        return nextGifPosition
    }

    /** * Start animation.
     * @param nextGifPosition next position of gif animation. */
    fun startAnimation(nextGifPosition: Int) {
        threadCheckAnim?.let { if (it.isAlive) return }
        if (arrayGifPositions.isEmpty()) throw Exception("Please use setGifPositions function for init positions of gif animation")
        if (nextGifPosition == this.gifPosition) return
        this.nextGifPosition = nextGifPosition % arrayGifPositions.size
        startAnimThread()
    }

    /** * Stop animation. */
    fun stopAnimation() {
        threadCheckAnim?.let { it.stopThread() }
    }

    /** * Change position of animation without animation play.
     * @param newGifPosition gif position of animation. */
    fun changeAnimPosition(newGifPosition: Int) {
        if (arrayGifPositions.isEmpty()) throw Exception("Please use setGifPositions function for init positions of gif animation")
        if (newGifPosition != this.gifPosition) {
            this.gifPosition = newGifPosition % arrayGifPositions.size
            gifDrawable.seekTo(arrayGifPositions[this.gifPosition])
        }
    }

    /** * Start animation thread. */
    private fun startAnimThread() {
        threadCheckAnim = ThreadCheckAnim()
        threadCheckAnim!!.start()
        gifDrawable.start()
    }

    /** * Thread for menu animation controller. */
    private inner class ThreadCheckAnim : Thread() {
        private val SLEEPING_TIME = 100L
        private var isThreadStopped: Boolean = false

        /** * Stop thread for menu animation. */
        fun stopThread() {
            isThreadStopped = true
        }

        /** * Run thread for check menu animation. */
        override fun run() {
            super.run()
            while (getCondition()) {
                if (isThreadStopped)
                    break
                sleep(SLEEPING_TIME)
            }
            gifDrawable.stop()
            changeAnimPosition(nextGifPosition)
        }

        /** * Get condition for check animation.
         * @return Result of condition for check animation. */
        private fun getCondition(): Boolean {
            if (nextGifPosition >= gifPosition)
                return gifDrawable.currentPosition < arrayGifPositions[nextGifPosition]
            return gifDrawable.currentPosition < arrayGifPositions[nextGifPosition] || gifDrawable.currentPosition >= arrayGifPositions[gifPosition]
        }
    }
}