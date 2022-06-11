package com.github.kartograf.pongkt.source.utils

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import ktx.freetype.loadFreeTypeFont
import ktx.freetype.registerFreeTypeFontLoaders

object Utils {
    val pixmap = Pixmap(1, 1, Pixmap.Format.RGBA8888).also {
        it.setColor(Color.WHITE)
        it.drawPixel(0, 0)
    }

    val texture = Texture(this.pixmap)
    val region = TextureRegion(this.texture, 0, 0, 1,1)

    private fun initiateAssetManager(): AssetManager {
        val assetManager = AssetManager()
        // Calling registerFreeTypeFontLoaders is necessary in order to load TTF/OTF files:
        assetManager.registerFreeTypeFontLoaders()
        return assetManager
    }

    val assetManager = initiateAssetManager().also {
        it.loadFreeTypeFont("fonts/m5x7.ttf") {
            size = 32
        }
    }
}