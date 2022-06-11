package com.github.kartograf.pongkt.source.utils

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch
import com.badlogic.gdx.math.Vector2
import kotlin.math.floor

object Utils {
    private var timeSince: Float = 0F
    fun createFlashingText(batch: PolygonSpriteBatch, font: BitmapFont, text: String, position: Vector2) {
        this.timeSince += Gdx.graphics.deltaTime
        val blink = floor(timeSince) % 2
        if (blink == 0F) {
            font.draw(batch, text, position.x, position.y)
        }
    }
}