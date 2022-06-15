package com.github.kartograf.pongkt.source.utils

import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureRegion
import ktx.assets.getAsset
import ktx.assets.load
import ktx.freetype.loadFreeTypeFont
import ktx.freetype.registerFreeTypeFontLoaders


object AssetManager : Disposable {

    val pixmap = Pixmap(1, 1, Pixmap.Format.RGBA8888).also {
        it.setColor(Color.WHITE)
        it.drawPixel(0, 0)
    }

    val texture = Texture(this.pixmap)
    val region = TextureRegion(this.texture, 0, 0, 1,1)

    var manager: AssetManager = AssetManager().also {
        it.registerFreeTypeFontLoaders()
    }
        private set

    lateinit var standardGameFont: BitmapFont
    lateinit var standardGameFontScore: BitmapFont
    lateinit var padHit: Sound
    lateinit var wallHit: Sound
    lateinit var scored: Sound

    fun loadAssets() {
        manager.loadFreeTypeFont(STANDARD_FONT) {
            size = 32
        }

        manager.loadFreeTypeFont(STANDARD_FONT_SCORE) {
            size = 64
        }

        manager.load<Sound>(PAD_HIT)
        manager.load<Sound>(WALL_HIT)
        manager.load<Sound>(SCORED)

        manager.finishLoading()
    }

    fun makeFont() {
        standardGameFont = manager.getAsset(STANDARD_FONT)
        standardGameFontScore = manager.getAsset(STANDARD_FONT_SCORE)
        padHit = manager.getAsset(PAD_HIT)
        wallHit = manager.getAsset(WALL_HIT)
        scored = manager.getAsset(SCORED)
    }

    override fun dispose() {
        this.standardGameFont.dispose()
        this.standardGameFontScore.dispose()
        this.padHit.dispose()
        this.wallHit.dispose()
        this.scored.dispose()
        this.pixmap.dispose()
        this.texture.dispose()
        this.manager.dispose()
    }

}