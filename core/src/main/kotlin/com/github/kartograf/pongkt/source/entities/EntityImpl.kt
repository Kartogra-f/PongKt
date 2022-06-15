package com.github.kartograf.pongkt.source.entities

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.math.Intersector
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.github.kartograf.pongkt.source.scenes.serveNumber
import com.github.kartograf.pongkt.source.utils.AssetManager
import com.github.kartograf.pongkt.source.utils.WINDOW_WIDTH
import kotlin.math.pow

// Defining default implementations.
class CollidableImpl() : Collidable {
    override fun checkCollision(collider: Rectangle, collidesWith: Rectangle) : Boolean {
        return Intersector.overlaps(collider, collidesWith)
    }

}

class PlayableImpl() : Playable {

    override fun playerMovement(delta: Float, rectangle: Rectangle, velocity: Vector2, speed: Vector2) {
        velocity.setZero()

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            velocity.y += 1
        }

        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            velocity.y -= 1
        }

        velocity.nor()
        rectangle.y += velocity.y * speed.y * delta
    }

}

class AiControllerImpl() : AIController {

    override fun aiMovement(delta: Float, rectangle: Rectangle, velocity: Vector2, speed: Vector2, ball: Ball) {

        if ((ball.rectangle.x - rectangle.x).pow(2).pow(0.5F) < WINDOW_WIDTH/2) {
            if (rectangle.y > (ball.rectangle.y + ball.rectangle.height/2)) {
                rectangle.y -= velocity.y * speed.y * delta
            } else if (rectangle.y + rectangle.height < (ball.rectangle.y + ball.rectangle.height/2)) {
                rectangle.y += velocity.y * speed.y * delta
            }
        }
    }

}

class BallControllerImpl() : BallController {
    override fun ballMovement(delta: Float, rectangle: Rectangle, velocity: Vector2, speed: Vector2) {
        velocity.setZero()

        when (serveNumber) {
            0u -> {
                velocity.x += 1
                velocity.y += 1
            }

            1u -> {
                velocity.x -= 1
                velocity.y += 1
            }
        }

        velocity.nor()

        rectangle.y += velocity.y * speed.y * delta
        rectangle.x += velocity.x * speed.x * delta

    }
}

class DynamicsImpl(private var isBall: Boolean, initialPosition: Vector2, dimensions: Vector2, override var speed: Vector2) : Dynamics {
    override var position: Vector2 = initialPosition
    override var velocity: Vector2 = Vector2.Zero
    override var rectangle: Rectangle = Rectangle(this.position.x, this.position.y, dimensions.x, dimensions.y)

    override fun update(delta: Float) {
        this.keepInBoundary()
    }

    override fun keepInBoundary() {
        if (!isBall) {
            if (this.rectangle.y < 0F) {
                this.rectangle.y = 0F
            }

            if (this.rectangle.y >= Gdx.graphics.height - this.rectangle.height) {
                this.rectangle.y = Gdx.graphics.height - this.rectangle.height
            }
        }

        if (isBall) {
            if (rectangle.y < 5 || rectangle.y >= Gdx.graphics.height - 15) {
                AssetManager.wallHit.play()
                speed.y *= -1
            }
        }
    }
}
