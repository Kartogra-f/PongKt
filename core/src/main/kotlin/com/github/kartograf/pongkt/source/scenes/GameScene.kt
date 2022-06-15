package com.github.kartograf.pongkt.source.scenes

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch
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
import kotlin.random.Random
import kotlin.random.nextUInt

var serveNumber: UInt = Random.nextUInt(2u)

class GameScreen: KtxScreen {

    enum class GameState {
        IDLE,
        SERVE,
        START,
        SCORED,
        OUTCOME,
    }

    // GameScreen Variables
    // ==================================================== //
    private var batch = PolygonSpriteBatch()
    private val camera = OrthographicCamera().also { it.setToOrtho(false, 1280F, 700F) }

    private val shapeDrawer = ShapeDrawer(this.batch, region)
    private var currentState = GameState.IDLE
    private var pScored: Boolean = false
    private var aScored: Boolean = false
    private var playerScore: UInt = 0u
    private var aiScore: UInt = 0u

    // Entities
    // ==================================================== //
    private val player = Player.new(initialPosition = Vector2(20F, 300F), dimensions = Vector2(10F, 120F), speed = Vector2(0F, 490F))
    private val ai = AI.new(initialPosition = Vector2(WINDOW_WIDTH.toFloat() - 30, 300F), dimensions = Vector2(10F, 120F), speed = Vector2(0F, 780F))
    private val ball = Ball.new(initialPosition = Vector2(WINDOW_WIDTH.toFloat()/2 - 8, 370F), dimensions = Vector2(15F, 15F), speed = Vector2(700F, 700F))
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
                AssetManager.padHit.play()
                it.speed.x *= -1.05F
            }
            it.ballMovement(delta, it.rectangle, it.velocity, it.speed)
            if (it.speed.x.coerceAtMost(1600F) == 1600F) it.speed.x = 1600F
        }

        // Updating ai.
        this.ai.aiMovement(delta, this.ai.rectangle, this.ai.velocity, this.ai.speed, this.ball)

        // Update player.
        this.player.playerMovement(delta, this.player.rectangle, this.player.velocity, this.player.speed)
    }

    // Render and call our update function. All game logic belong here.
    override fun render(delta: Float) {
        changeBackgroundColor()
        camera.update()

        this.batch.use {
            it.projectionMatrix = camera.combined

            // Draw entities.
            this.entities.filterIsInstance<Drawable>().forEach { drawable -> this.shapeDrawer.filledRectangle(drawable.rectangle) }
            AssetManager.standardGameFontScore.draw(it, "$playerScore", WINDOW_WIDTH.toFloat()/2 - 120, WINDOW_HEIGHT.toFloat()/2 + 270)
            AssetManager.standardGameFontScore.draw(it, "$aiScore", WINDOW_WIDTH.toFloat()/2 + 100, WINDOW_HEIGHT.toFloat()/2 + 270)

            when (currentState) {
                GameState.IDLE -> {
                    createFlashingText(it, AssetManager.standardGameFont, "PRESS SPACE TO START!", Vector2(WINDOW_WIDTH.toFloat()/2 - 120, WINDOW_HEIGHT.toFloat()/2 + 150))

                    if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                        currentState = GameState.SERVE
                    }
                }

                GameState.SERVE -> {
                    createFlashingText(it, AssetManager.standardGameFont, "PRESS SPACE TO SERVE!", Vector2(WINDOW_WIDTH.toFloat()/2 - 120, WINDOW_HEIGHT.toFloat()/2 + 150))

                    when (serveNumber) {
                        0u -> AssetManager.standardGameFont.draw(it, "AI SERVES!", (WINDOW_WIDTH.toFloat()/2) - 60, (WINDOW_HEIGHT.toFloat()/2) + 100)
                        1u -> AssetManager.standardGameFont.draw(it, "PLAYER SERVES!", (WINDOW_WIDTH.toFloat()/2) - 80, (WINDOW_HEIGHT.toFloat()/2) + 100)
                    }

                    if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                        currentState = GameState.START
                    }
                }

                GameState.START -> {
                    this.update(delta)
                    if (ball.rectangle.x > WINDOW_WIDTH - 4) {
                        AssetManager.scored.play()
                        playerScore++
                        pScored = true
                        currentState = GameState.SCORED
                    }

                    if (ball.rectangle.x <= -4) {
                        AssetManager.scored.play()
                        aiScore++
                        aScored = true
                        currentState = GameState.SCORED
                    }
                }

                GameState.SCORED -> {
                    if (pScored && playerScore != 4u) {
                        createFlashingText(it, AssetManager.standardGameFont, "YOU SCORED! PRESS SPACE TO SERVE!", Vector2(WINDOW_WIDTH.toFloat()/2 - 197, WINDOW_HEIGHT.toFloat()/2 + 150))

                        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                            gameReset(playerScored = true, aiScored = false, state = GameState.SERVE)
                        }
                    } else {
                        createFlashingText(it, AssetManager.standardGameFont, "AI SCORED! PRESS SPACE TO CONTINUE!", Vector2(WINDOW_WIDTH.toFloat()/2 - 197, WINDOW_HEIGHT.toFloat()/2 + 150))

                        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                            gameReset(playerScored = false, aiScored = true, state = GameState.SERVE)
                        }
                    }

                    if (aiScore == 4u || playerScore == 4u) {
                        currentState = GameState.OUTCOME
                    }
                }

                GameState.OUTCOME -> {

                    if (aiScore == 4u) {
                        createFlashingText(it, AssetManager.standardGameFont, "YOU LOST! PRESS SPACE TO RESTART!", Vector2(WINDOW_WIDTH.toFloat()/2 - 197, WINDOW_HEIGHT.toFloat()/2 + 150))
                    } else {
                        createFlashingText(it, AssetManager.standardGameFont, "YOU WON! PRESS SPACE TO RESTART!", Vector2(WINDOW_WIDTH.toFloat()/2 - 197, WINDOW_HEIGHT.toFloat()/2 + 150))
                    }

                    if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                        gameReset(playerScored = false, aiScored = false, state = GameState.IDLE)
                    }
                }
            }
        }
        this.exit()
    }

    // Reset our game, including entities to start position.
    private fun gameReset(playerScored: Boolean, aiScored: Boolean, state: GameState) {
        this.ball.also {
            it.rectangle.x = WINDOW_WIDTH.toFloat()/2 - 8
            it.rectangle.y = 370F
            it.speed.x = 700F
            it.speed.y = 700F
        }

        this.player.also {
            it.rectangle.y = 300F
        }

        this.ai.also {
            it.rectangle.y = 300F
        }

        this.currentState = state

        serveNumber = if (playerScored) {
            1u
        } else if (aiScored) {
            0u
        } else {
            Random.nextUInt(0u, 2u)
        }

        if (playerScored) {
            pScored = false
        } else {
            aScored = false
        }

        if (!playerScored && !aiScored) {
            playerScore = 0u
            aiScore = 0u
        }

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
        this.disposeSafely()
    }
}