package com.k1apps.backgammon

import javax.inject.Inject

class DiceStatus @Inject constructor() {
    private var enable1 = false
    private var enable2 = false
    private var twice = false
    var enable: Boolean
        get() {
            return enable1 || enable2
        }
        set(value) {
            if (value) {
                enable()
            } else {
                disable()
            }
        }

    private fun enable() {
        if (enable1.not()) {
            enable1 = true
        } else if (twice) {
            enable2 = true
        }
    }

    private fun disable() {
        if (enable1) {
            enable1 = false
        } else if (twice) {
            enable2 = false
        }
    }

    internal fun isFullEnabled(): Boolean {
        if (enable1 && enable2) {
            return true
        }
        if (enable1 && twice.not()) {
            return true
        }
        return false
    }

    fun setTwice(twice: Boolean) {
        this.twice = twice
    }

    fun enableCount(): Byte {
        var count = 0.toByte()
        if (enable1) {
            count++
        }
        if (enable2) {
            count++
        }
        return count;
    }
}
