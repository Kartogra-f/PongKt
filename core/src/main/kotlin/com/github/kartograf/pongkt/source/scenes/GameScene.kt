package com.github.kartograf.pongkt.source.scenes

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch
import com.badlogic.gdx.math.Intersector
import com.badlogic.gdx.math.Vector2
import com.github.kartograf.pongkt.source.entities.*
import com.github.kartograf.pongkt.source.utils.AssetManager
import com.github.kartograf.pongkt.source.utils.AssetManager.region
import com.github.kartograf.pongkt.source.utils.Utils.changeBackgroundColor
import com.github.kartograf.pongkt.source.utils.Utils.createFlashingText
import com.github.kartograf.pongkt.source.utils.WINDOW_HEIGHT
import com.github.kartograf.pongkt.source.utils.WINDOW_WIDTH
import ktx.app.KtxScreen
import ktx.assets.disposeSafely
import ktx.graphics.use
import space.earlygrey.shapedrawer.ShapeDrawer
import java.awt.Color
import kotlin.random.Random
import kotlin.random.nextUInt

var serveNumber: UInt = Random.nextUInt(0u,2u)

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
    private var batch = PolygonSpriteBatch()
    private val camera = OrthographicCamera().also {
        it.setToOrtho(false, 1280F, 700F)
    }

    private val shapeDrawer = ShapeDrawer(this.batch, region)
    private var currentState = GameState.IDLE

    // Entities
    // ==================================================== //
    private val player = Player.new(initialPosition = Vector2(15F, 300F), dimensions = Vector2(10F, 120F), speed = Vector2(0F, 500F))
    private val ai = AI.new(initialPosition = Vector2(WINDOW_WIDTH.toFloat() - 25, 300F), dimensions = Vector2(10F, 120F), speed = Vector2(0F, 500F))
    private val ball = Ball.new(initialPosition = Vector2(WINDOW_WIDTH.toFloat()/2, 370F), dimensions = Vector2(15F, 15F), speed = Vector2(500F, 500F))
    private val entities: List<Any> = listOf(player, ai, ball)

    // Game Functions
   // ==================================================== //
    override fun show() {
        AssetManager.loadAssets()
        AssetManager.makeFont()
    }

    // Update entities.
    private fun update(delta: Float) {
        // Update our Dynamics to ensure the Dynamic entities are kept within the boundaries.
        this.entities.filterIsInstance<Dynamics>().forEach { dynamics -> dynamics.update(delta) }

        // Updating ball.
        this.ball.also {
            if (it.checkCollision(it.rectangle, this.player.rectangle) || it.checkCollision(it.rectangle, this.ai.rectangle)) {
                it.speed.x *= -1.05F
            }

            it.ballMovement(delta, it.rectangle, it.velocity, it.speed)
        }

        // Updating ai.
        this.ai.aiMovement(delta, this.ai.rectangle, this.ai.velocity, this.ai.speed, this.ball)

        // Update player.
        this.player.playerMovement(delta, this.player.rectangle, this.player.velocity, this.player.speed)

    }

    override fun render(delta: Float) {
        changeBackgroundColor()

        camera.update()

        this.batch.use {
            it.projectionMatrix = camera.combined

            // Draw entities.
            this.entities.filterIsInstance<Drawable>().forEach { drawable -> this.shapeDrawer.filledRectangle(drawable.rectangle) }


            when (currentState) {
                GameState.IDLE -> {
                    createFlashingText(it, AssetManager.standardGameFont, "PRESS SPACE TO START!", Vector2(WINDOW_WIDTH.toFloat()/2 - 110, WINDOW_HEIGHT.toFloat()/2 + 150))
                    if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                        currentState = GameState.SERVE
                    }
                }

                GameState.SERVE -> {
                    createFlashingText(it, AssetManager.standardGameFont, "PRESS SPACE TO BEGIN!", Vector2(WINDOW_WIDTH.toFloat()/2 - 110, WINDOW_HEIGHT.toFloat()/2 + 150))

                    when (serveNumber) {
                        0u -> AssetManager.standardGameFont.draw(it, "AI SERVES!", (WINDOW_WIDTH.toFloat()/2) - 55, (WINDOW_HEIGHT.toFloat()/2) + 100)
                        1u -> AssetManager.standardGameFont.draw(it, "PLAYER SERVES!", (WINDOW_WIDTH.toFloat()/2) - 70, (WINDOW_HEIGHT.toFloat()/2) + 100)
                    }

                    if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                        currentState = GameState.START
                    }
                }

                GameState.START -> {
                    this.update(delta)
                }
                else -> {}
            }
        }

        this.exit()
    }

    // If ESC key is pressed, quit game.
    private fun exit() {
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit()
        }
    }

    // Free memory on exit.
    // ==================================================== //
    override fun dispose() {
        this.batch.dispose()
        AssetManager.disposeSafely()
    }
}