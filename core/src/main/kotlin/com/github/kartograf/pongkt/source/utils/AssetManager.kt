package com.github.kartograf.pongkt.source.utils

import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureRegion
import ktx.assets.getAsset
import ktx.freetype.loadFreeTypeFont
import ktx.freetype.registerFreeTypeFontLoaders


object AssetManager : Disposable {

    enum class AssetPath(val path: String) {

    }

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

    fun loadAssets() {
        manager.loadFreeTypeFont(STANDARD_FONT) {
            size = 32
        }
        manager.finishLoading()
    }

    fun makeFont() {
        standardGameFont = manager.getAsset(STANDARD_FONT)
    }

    override fun dispose() {
        this.standardGameFont.dispose()
        this.pixmap.dispose()
        this.texture.dispose()
        this.manager.dispose()
    }

}