@file:JvmName("Lwjgl3Launcher")

package com.github.kartograf.pongkt.lwjgl3

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.github.kartograf.pongkt.Game
import com.github.kartograf.pongkt.source.utils.WINDOW_HEIGHT
import com.github.kartograf.pongkt.source.utils.WINDOW_TITLE
import com.github.kartograf.pongkt.source.utils.WINDOW_WIDTH

/** Launches the desktop (LWJGL3) application. */
fun main() {
    Lwjgl3Application(Game(), Lwjgl3ApplicationConfiguration().apply {
        setTitle(WINDOW_TITLE)
        useVsync(true)
        setWindowedMode(WINDOW_WIDTH, WINDOW_HEIGHT)
        setResizable(false)
        setWindowIcon(*(arrayOf(128, 64, 32, 16).map { "libgdx$it.png" }.toTypedArray()))
    })
}
