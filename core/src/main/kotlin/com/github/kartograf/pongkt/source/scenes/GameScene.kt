package com.github.kartograf.pongkt.source.scenes

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.math.Vector2
import com.github.kartograf.pongkt.source.entities.*
import com.github.kartograf.pongkt.source.utils.Utils.assetManager
import com.github.kartograf.pongkt.source.utils.Utils.pixmap
import com.github.kartograf.pongkt.source.utils.Utils.region
import com.github.kartograf.pongkt.source.utils.Utils.texture
import com.github.kartograf.pongkt.source.utils.WINDOW_WIDTH
import ktx.app.KtxScreen
import ktx.assets.*
import ktx.freetype.loadFreeTypeFont
import ktx.graphics.use
import space.earlygrey.shapedrawer.ShapeDrawer

class GameScreen: KtxScreen {

    enum class GameState {
        IDLE,
        SERVE,
        START,
        SCORED,
        LOST,
    }

    // GameScreen Variables
    // ==================================================== //
    private val font = assetManager.load<BitmapFont>("fonts/m5x7.ttf").also {
        it.finishLoading()
    }
    private var batch = PolygonSpriteBatch()
    private val shapeDrawer = ShapeDrawer(this.batch, region)
    private var currentState = GameState.IDLE

    // Entities
    // ==================================================== //
    private val player = Player.new(initialPosition = Vector2(15F, 270F), dimensions = Vector2(10F, 120F), speed = Vector2(0F, 500F))
    private val ai = AI.new(initialPosition = Vector2(WINDOW_WIDTH.toFloat() - 20, 280F), dimensions = Vector2(10F, 120F), speed = Vector2(0F, 500F))
    private val ball = Ball.new(initialPosition = Vector2(632F, 335F), dimensions = Vector2(15F, 15F), speed = Vector2(300F, 300F))
    private val entities: List<Any> = listOf(player, ai, ball)

    // Game Functions
   // ==================================================== //
    private fun update(delta: Float) {
        this.entities.filterIsInstance<Dynamics>().forEach { dynamics -> dynamics.update(delta) }
        this.exit()
    }

    override fun render(delta: Float) {

        this.batch.use {
            this.entities.filterIsInstance<Drawable>().forEach { drawable -> this.shapeDrawer.filledRectangle(drawable.rectangle) }
            this.font.asset.draw(it, "Hello", 50F, 50F)
        }

        this.update(delta)
    }

    private fun exit() {
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit()
        }
    }

    // Free memory on exit.
    // ==================================================== //
    override fun dispose() {
        this.batch.dispose()
        pixmap.dispose()
        texture.dispose()
        font.asset.dispose()
        assetManager.unloadSafely("fonts/m5x7.ttf")
    }
}