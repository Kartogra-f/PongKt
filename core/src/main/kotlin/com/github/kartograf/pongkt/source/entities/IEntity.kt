package com.github.kartograf.pongkt.source.entities

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2

// Defining components.
interface Playable {
    fun playerMovement(delta: Float)
}

interface AIController {
    fun aiMovement(delta: Float)
}

interface Shape {
    var rectangle: Rectangle
}

interface Position {
    var position: Vector2
}

interface Drawable : Shape, Position

interface Dynamics : Shape, Position {
    var velocity: Vector2
    val speed: Vector2

    fun update(delta: Float)
    fun keepInBoundary()
}

// Defining default implementations.
class DynamicsPlayerImpl(initialPosition: Vector2, dimensions: Vector2, override val speed: Vector2) : Dynamics, Playable {
    override var position: Vector2 = initialPosition
    override var velocity: Vector2 = Vector2.Zero
    override var rectangle: Rectangle = Rectangle(this.position.x, this.position.y, dimensions.x, dimensions.y)

    override fun update(delta: Float) {
        this.playerMovement(delta)
        this.keepInBoundary()
    }

    override fun keepInBoundary() {
        if (this.rectangle.y < 0) {
            this.rectangle.y = 0F
        }

        if (this.rectangle.y >= Gdx.graphics.height.toFloat() - this.rectangle.height) {
            this.rectangle.y = Gdx.graphics.height.toFloat() - this.rectangle.height
        }
    }

    override fun playerMovement(delta: Float) {
        this.velocity.setZero()
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            this.velocity.y += 1
        }

        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            this.velocity.y -= 1
        }

        this.velocity.nor()
        this.rectangle.y += this.velocity.y * this.speed.y * delta
    }
}

class DynamicsAiImpl(initialPosition: Vector2, dimensions: Vector2, override val speed: Vector2) : Dynamics, AIController {
    override var position: Vector2 = initialPosition
    override var velocity: Vector2 = Vector2.Zero
    override var rectangle: Rectangle = Rectangle(this.position.x, this.position.y, dimensions.x, dimensions.y)

    override fun update(delta: Float) {
        this.aiMovement(delta)
        this.keepInBoundary()
    }

    override fun keepInBoundary() {
        if (this.rectangle.y < 0) {
            this.rectangle.y = 0F
        }

        if (this.rectangle.y >= Gdx.graphics.height.toFloat() - this.rectangle.height) {
            this.rectangle.y = Gdx.graphics.height.toFloat() - this.rectangle.height
        }
    }

    override fun aiMovement(delta: Float) {

    }
}

class DynamicsBallImpl(initialPosition: Vector2, dimensions: Vector2, override val speed: Vector2) : Dynamics, AIController {
    override var position: Vector2 = initialPosition
    override var velocity: Vector2 = Vector2.Zero
    override var rectangle: Rectangle = Rectangle(this.position.x, this.position.y, dimensions.x, dimensions.y)

    override fun update(delta: Float) {
        this.aiMovement(delta)
        this.keepInBoundary()
    }

    override fun keepInBoundary() {
    }

    override fun aiMovement(delta: Float) {

    }
}