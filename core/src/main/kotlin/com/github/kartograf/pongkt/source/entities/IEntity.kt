package com.github.kartograf.pongkt.source.entities

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.math.Intersector
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.github.kartograf.pongkt.source.scenes.serveNumber

// Defining components.
// Internal components -- START
interface Shape {
    var rectangle: Rectangle
}

interface Position {
    var position: Vector2
}

interface Velocity {
    var velocity: Vector2
    val speed: Vector2
}

interface Drawable : Shape, Position
// Internal components -- END

interface Playable {
    fun playerMovement(delta: Float, rectangle: Rectangle, velocity: Vector2, speed: Vector2)
}

interface AIController {
    fun aiMovement(delta: Float, rectangle: Rectangle, velocity: Vector2, speed: Vector2, ball: Ball)
}

interface BallController {
    fun ballMovement(delta: Float, rectangle: Rectangle, velocity: Vector2, speed: Vector2)
}

interface BallReflection {
    fun ballReflect(rectangle: Rectangle, speed: Vector2)
}

interface Collidable {
    fun checkCollision(collider: Rectangle, collidesWith: Rectangle) : Boolean
}


interface Dynamics : Shape, Position, Velocity {
    override var velocity: Vector2
    override var speed: Vector2

    fun update(delta: Float)
    fun keepInBoundary()
}